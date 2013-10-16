package de.umg.mi.idrt.ioe.OntologyTree;

import javax.swing.JTree;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import de.umg.mi.idrt.ioe.ActionCommand;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
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

	private MyOntologyTree myOT;
	private final Viewer viewer;
	private OntologyTreeNode sourceNode = null;
	private OntologyTreeNode targetNode = null;

	public NodeDropListener(Viewer viewer) {
		super(viewer);
		this.viewer = viewer;
		setExpandEnabled(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerDropAdapter#determineLocation(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	@Override
	protected int determineLocation(DropTargetEvent event) {
		// TODO Auto-generated method stub
		return 3;
	}

	
	@Override
	public void drop(DropTargetEvent event) {
//		int location = this.determineLocation(event);
		// String target = (String) determineTarget(event);
		if (event.item == null) {
			System.out.println("Null");
			targetNode = null;
		}
		else if(event.item.getData() instanceof OntologyTreeNode) {
			OntologyTreeNode treeNode = (OntologyTreeNode) event.item.getData();


			Console.info("getDate: "
					+ (treeNode.getName()));

			targetNode = (OntologyTreeNode) determineTarget(event);
			System.out.println("TargetNodeName: " + targetNode.getName());
			OntologyEditorView.setNotYetSaved(true);
		}
		else {
			MessageDialog.openError(Application.getShell(), "Error", "You cannot drop this item here!");
		}
		super.drop(event);
	}

	@Override
	public boolean performDrop(Object data) {
		myOT = OntologyEditorView.getI2b2ImportTool().getMyOntologyTrees();

		Console.info("Dropped performed with data:");
		Console.info(" - data: " + data.getClass().getSimpleName());
		Console.info(" - data_toString: " + (String) data.toString());

		if (this.targetNode != null) {
			Console.info(" - targetNode: " + targetNode.getName());
		} else {
			Console.info(" - targetNode is Null!");
			targetNode = myOT.getSubRootNode();
		}

		Console.info(" - data this.myOT?: "
				+ (this.myOT == null ? "isNull" : "is NOT null"));
		Console.info(" - data this.myOT.getViewTree()?: "
				+ (this.myOT.getViewTreeSource() == null ? "isNull"
						: "is NOT null"));

		Console.info(" - data size?: "
				+ this.myOT.getViewTreeSource().stringPathToViewTreeNode.size());
		System.out
		.println(" - data size2?: "
				+ this.myOT.getViewTreeTarget().stringPathToViewTreeNode
				.size());

		Console.info("OTCOPY:");

		Console.info(" - things to copy: SourceNode (\""
				+ data.toString() + "\") or TargetNode (\""
				+ targetNode.getTreePath() + "\") ");
		System.out.println("data.toString(): " + data.toString());
		//		myOT.dropCommandCopyNodes(data.toString(), targetNode.getTreePath());

		sourceNode = myOT.getOntologyTreeSource().getNodeLists().getNodeByPath(data.toString());
		if (sourceNode == null) {
			System.out.println("MOVE");
			sourceNode = myOT.getOntologyTreeTarget().getNodeLists().getNodeByPath(data.toString());
			OntologyTreeNode node =	myOT.moveTargetNode(sourceNode, targetNode);
			
			if (node != null)
			OntologyEditorView.getTargetTreeViewer().setExpandedState(node,
					true);
		}
		else {
			System.out.println("COPY");
			myOT.dropCommandCopyNodes(sourceNode.getTreePath(), targetNode.getTreePath());
			System.out.println("TARGET TREE PATH: " + sourceNode.getTreePath() + " " + sourceNode.getLevel());
			OntologyTreeNode node = myOT.getOntologyTreeTarget().getNodeLists().getNodeByPath(sourceNode.getTreePath());
			if (node == null)
			System.out.println("NODE NULL");
			else
				System.out.println("NODE NOT NULL");
			
//			OTNodeLists.listNodeByPath();
//			OntologyEditorView.getI2b2ImportTool().getMyOntologyTrees().gets
			
		}
		viewer.refresh();

		return false;
	}

	@Override
	public boolean validateDrop(Object target, int operation,
			TransferData transferType) {
		return true;

	}

}