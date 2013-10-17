package de.umg.mi.idrt.ioe.OntologyTree;

import org.eclipse.jface.dialogs.MessageDialog;
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
		OntologyEditorView.setNotYetSaved(true);
		if (event.item == null) {
			targetNode = null;
		}
		else if(event.item.getData() instanceof OntologyTreeNode) {
			OntologyTreeNode treeNode = (OntologyTreeNode) event.item.getData();

			Console.info("getDate: "
					+ (treeNode.getName()));

			targetNode = (OntologyTreeNode) determineTarget(event);
			OntologyEditorView.setNotYetSaved(true);
		}
		else {
			MessageDialog.openError(Application.getShell(), "Error", "You cannot drop this item here!");
		}
		super.drop(event);
	}

	@Override
	public boolean performDrop(Object data) {
		String path = data.toString();
		myOT = OntologyEditorView.getI2b2ImportTool().getMyOntologyTrees();

		Console.info("Dropped performed with data:");
		Console.info(" - data: " + data.getClass().getSimpleName());
		Console.info(" - data_toString: " + (String) path);

		if (this.targetNode != null) {
			Console.info(" - targetNode: " + targetNode.getName());
		} else {
			Console.info(" - targetNode is Null!");
			targetNode = myOT.getSubRootNode();
		}

		Console.info(" - things to copy: SourceNode (\""
				+ path + "\") or TargetNode (\""
				+ targetNode.getTreePath() + "\") ");
		System.out.println(path);
		sourceNode = myOT.getOntologyTreeSource().getNodeLists().getNodeByPath(path);
		if (data.toString().equals("abc")){
			myOT.dropCommandCopyNodes(targetNode.getTreePath(),dropOperation);
		}
		else {
			sourceNode = myOT.getOntologyTreeTarget().getNodeLists().getNodeByPath(path);
			if (myOT.getSubRootNode() == sourceNode)
				System.err.println("SOURCE IS ROOT");
			else {
				OntologyTreeNode node2 =	myOT.moveTargetNode(sourceNode, targetNode);

				if (node2 != null)
					OntologyEditorView.getTargetTreeViewer().setExpandedState(node2,
							true);
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