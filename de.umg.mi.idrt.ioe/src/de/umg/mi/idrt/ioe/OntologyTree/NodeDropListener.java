package de.umg.mi.idrt.ioe.OntologyTree;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;

import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> Department of
 *         Medical Informatics Goettingen www.mi.med.uni-goettingen.de
 * 
 *         drop listener for getting data from the source tree to the target
 *         tree
 * 
 */

public class NodeDropListener extends ViewerDropAdapter {

	private int dropOperation;
	private MyOntologyTree myOT;
	private final Viewer viewer;
	private OntologyTreeNode sourceNode = null;
	private OntologyTreeNode targetNode = null;

	public NodeDropListener(Viewer viewer) {
		super(viewer);
		this.viewer = viewer;
		setExpandEnabled(true);
	}

	@Override
	protected int determineLocation(DropTargetEvent event) {
		return 3; //only ON the target
	}

	@Override
	public void drop(DropTargetEvent event) {
		System.out.println("@drop(event)");
		myOT = OntologyEditorView.getI2b2ImportTool().getMyOntologyTrees();
		OntologyEditorView.setNotYetSaved(true);

		if (event.item == null) {
			System.err.println("event.item == null");
			event.item = OntologyEditorView.getTargetTreeViewer().getTree();
			targetNode = myOT.getSubRootNode();
			event.item.setData(targetNode);
		}
		if(event.item.getData() instanceof OntologyTreeNode) {

			targetNode = (OntologyTreeNode) determineTarget(event);
			if (targetNode.isLeaf()) {
				System.err.println("TARGET IS LEAF!\nAddSourcePath NYI!");
				OntologyEditorView.setNotYetSaved(true);
				super.drop(event);
			}
			else {
				OntologyEditorView.setNotYetSaved(true);
				super.drop(event);
			}
		}
		else {
			MessageDialog.openError(Application.getShell(), "Error", "You cannot drop this item here!");
		}
	}

	@Override
	public boolean performDrop(Object data) {
		String path = data.toString();

		Console.info("Dropped performed with data:");
		Console.info(" - data: " + data.getClass().getSimpleName());
		Console.info(" - data_toString: " + (String) path);

		if (this.targetNode != null) {
			Console.info(" - targetNode: " + targetNode.getName());
		} 

		Console.info(" - things to copy: SourceNode (\""
				+ path + "\") or TargetNode (\""
				+ targetNode.getTreePath() + "\") ");
		sourceNode = myOT.getOntologyTreeSource().getNodeLists().getNodeByPath(path);
		if (sourceNode == null) {
			IStructuredSelection selection = (IStructuredSelection) OntologyEditorView.getStagingTreeViewer()
					.getSelection();
			sourceNode = (OntologyTreeNode) selection.getFirstElement();
		}
		if (data.toString().equals("stagingTreeViewer")){
			if (targetNode.isLeaf()) {
				System.err.println("NYI Is LEAF");	
				if (sourceNode == null)
					System.out.println("NULL");
				targetNode.getTargetNodeAttributes().addStagingPath(sourceNode.getTreePath());
			}
			else {
				myOT.dropCommandCopyNodes(targetNode.getTreePath(),dropOperation);
			}
		}
		else {
			sourceNode = myOT.getOntologyTreeTarget().getNodeLists().getNodeByPath(path);
			if (myOT.getSubRootNode() == sourceNode || sourceNode.getParent() == targetNode)
				System.err.println("SOURCE IS ROOT || TARGET IS PARENT");
			else {
				if (sourceNode.getTreePath() != targetNode.getTreePath()) {
					if (!targetNode.isLeaf()) {
						OntologyTreeNode node2 = myOT.moveTargetNode(sourceNode, targetNode);
						if (node2 != null)
							OntologyEditorView.getTargetTreeViewer().setExpandedState(node2,
									true);
					}
					else {
						for (OntologyTreeSubNode subNode : sourceNode.getTargetNodeAttributes().getSubNodeList()) {
							targetNode.getTargetNodeAttributes().addStagingPath(subNode.getStagingPath());
						}
					}
					sourceNode.removeFromParent();
				}
				else {
					System.err.println("TARGET == SOURCE");
				}
			}
		}
		viewer.refresh();
		return false;
	}

	@Override
	public boolean validateDrop(Object target, int operation,
			TransferData transferType) {
		dropOperation = operation;
		return true;
	}
}