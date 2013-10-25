package de.umg.mi.idrt.ioe.OntologyTree;

import javax.swing.tree.DefaultMutableTreeNode;

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
	private DefaultMutableTreeNode targetNode = null;

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
			event.item = OntologyEditorView.getTargetTreeViewer().getTree();
			targetNode = myOT.getSubRootNode();
			event.item.setData(targetNode);
		}
		if(event.item.getData() instanceof OntologyTreeNode) {

			targetNode = (OntologyTreeNode) determineTarget(event);
			if (((OntologyTreeNode)targetNode).isLeaf()) {
				OntologyEditorView.setNotYetSaved(true);
				super.drop(event);
			}
			else {
				OntologyEditorView.setNotYetSaved(true);

				super.drop(event);
			}
		}
		else if (event.item.getData() instanceof OntologyTreeSubNode) {
			OntologyEditorView.setNotYetSaved(true);
			targetNode = (OntologyTreeSubNode)determineTarget(event);
			super.drop(event);
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

		sourceNode = myOT.getOntologyTreeSource().getNodeLists().getNodeByPath(path);
		if (sourceNode == null) {
			IStructuredSelection selection = (IStructuredSelection) OntologyEditorView.getStagingTreeViewer()
					.getSelection();
			sourceNode = (OntologyTreeNode) selection.getFirstElement();
		}
		if (data.toString().equals("stagingTreeViewer")){
			if (targetNode instanceof OntologyTreeNode) {
				if (targetNode.isLeaf()) {
					((OntologyTreeNode) targetNode).getTargetNodeAttributes().addStagingPath(sourceNode.getTreePath());
				}
				else {
					myOT.dropCommandCopyNodes(((OntologyTreeNode) targetNode).getTreePath(),dropOperation);
				}
			}
			else if (targetNode instanceof OntologyTreeSubNode) {
				OntologyTreeSubNode subNode = (OntologyTreeSubNode)targetNode;
				subNode.getParent().getTargetNodeAttributes().addStagingPath(sourceNode.getTreePath());
			}
		}
		else if (data.toString().equals("subNode")) {
			OntologyTreeSubNode subNode = NodeMoveDragListener.getSubNode();

			if (targetNode instanceof OntologyTreeNode) {
				((OntologyTreeNode) targetNode).getTargetNodeAttributes().addStagingPath(subNode.getStagingPath());
				subNode.getParent().getTargetNodeAttributes().removeSubNode(subNode);
			}
			else if (targetNode instanceof OntologyTreeSubNode) {
				((OntologyTreeSubNode) targetNode).getParent().getTargetNodeAttributes().addStagingPath(subNode.getStagingPath());
				subNode.getParent().getTargetNodeAttributes().removeSubNode(subNode);
			}
		}
		else if (targetNode instanceof OntologyTreeNode){
			sourceNode = myOT.getOntologyTreeTarget().getNodeLists().getNodeByPath(path);
			if (myOT.getSubRootNode() == sourceNode || sourceNode.getParent() == (OntologyTreeNode)targetNode)
				System.err.println("SOURCE IS ROOT || TARGET IS PARENT");
			else {
				if (sourceNode.getTreePath() != ((OntologyTreeNode) targetNode).getTreePath()) {
					if (!((OntologyTreeNode)targetNode).isLeaf()) {
						OntologyTreeNode node2 = myOT.moveTargetNode(sourceNode, (OntologyTreeNode) targetNode);
						if (node2 != null)
							OntologyEditorView.getTargetTreeViewer().setExpandedState(node2,
									true);
					}
					else {
						for (OntologyTreeSubNode subNode : sourceNode.getTargetNodeAttributes().getSubNodeList()) {
							((OntologyTreeNode) targetNode).getTargetNodeAttributes().addStagingPath(subNode.getStagingPath());
						}
					}
					sourceNode.removeFromParent();
				}
				else {
					System.err.println("TARGET == SOURCE");
				}
			}
		}
		else if (targetNode instanceof OntologyTreeSubNode) {
			sourceNode = myOT.getOntologyTreeTarget().getNodeLists().getNodeByPath(path);
			OntologyTreeSubNode subNode = ((OntologyTreeSubNode)targetNode);

			for (OntologyTreeSubNode sub : sourceNode.getTargetNodeAttributes().getSubNodeList()) {
				subNode.getParent().getTargetNodeAttributes().addStagingPath(sub.getStagingPath());
			}
			sourceNode.removeFromParent();
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