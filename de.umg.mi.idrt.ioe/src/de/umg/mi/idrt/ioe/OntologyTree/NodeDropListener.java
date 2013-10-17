package de.umg.mi.idrt.ioe.OntologyTree;

import java.awt.datatransfer.Transferable;
import java.util.Iterator;

import javax.swing.JTree;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
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
		OntologyEditorView.setNotYetSaved(true);
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
//			System.out.println(sourceNode.getName());
//			if (sourceNode == null) {
//				System.out.println("MOVE");
//				sourceNode = myOT.getOntologyTreeTarget().getNodeLists().getNodeByPath(path);
//				OntologyTreeNode node2 =	myOT.moveTargetNode(sourceNode, targetNode);
//
//				if (node2 != null)
//					OntologyEditorView.getTargetTreeViewer().setExpandedState(node2,
//							true);
//			}
//			else
				if (data.toString().equals("abc")){
				System.out.println("COPY");
				myOT.dropCommandCopyNodes(targetNode.getTreePath());
//				System.out.println("TARGET TREE PATH: " + sourceNode.getTreePath() + " " + sourceNode.getLevel());
			}
				else {
					System.out.println("MOVE");
					sourceNode = myOT.getOntologyTreeTarget().getNodeLists().getNodeByPath(path);
					OntologyTreeNode node2 =	myOT.moveTargetNode(sourceNode, targetNode);
	
					if (node2 != null)
						OntologyEditorView.getTargetTreeViewer().setExpandedState(node2,
								true);
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