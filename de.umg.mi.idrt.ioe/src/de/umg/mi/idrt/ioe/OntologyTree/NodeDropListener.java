package de.umg.mi.idrt.ioe.OntologyTree;

import java.util.Iterator;
import java.util.LinkedHashSet;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

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

	public static DefaultMutableTreeNode getTargetNode() {
		return targetNode;
	}
	private int dropOperation;
	private MyOntologyTrees myOT;
	private final Viewer viewer;
	private OntologyTreeNode sourceNode = null;

	private static DefaultMutableTreeNode targetNode = null;

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
//		System.out.println("@drop(event !!)");
		myOT = OntologyEditorView.getMyOntologyTree();
		OntologyEditorView.setNotYetSaved(true);

		if (event.item == null) {
			event.item = OntologyEditorView.getTargetTreeViewer().getTree();
			targetNode = OntologyEditorView.getOntologyTargetTree().getI2B2RootNode();
			event.item.setData(targetNode);
		}
		if(event.item.getData() instanceof OntologyTreeNode) {
//			System.out.println("TRUE");
			targetNode = (OntologyTreeNode) determineTarget(event);
			if (((OntologyTreeNode)targetNode).isLeaf()) {
//				System.out.println("isLeaf");
				OntologyEditorView.setNotYetSaved(true);
				super.drop(event);
			}
			else {
//				System.out.println("ELSE");
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
		IStructuredSelection selection = (IStructuredSelection) OntologyEditorView.getTargetTreeViewer()
				.getSelection();
		Console.info("Dropped performed with data:");
		Console.info(" - data: " + data.getClass().getSimpleName());
		Console.info(" - data_toString: " + (String) path);

		if (data.toString().equals("stagingTreeViewer")){
//			System.out.println("data = stagingTreeViewer");
			//			if (targetNode instanceof OntologyTreeNode) {
			//				if (targetNode.isLeaf()) {
			myOT.dropCommandCopyNodes(dropOperation);
			//TODO Add as Start/End Date
			//					Iterator<OntologyTreeNode> nodeIterator = selection.iterator();
			//					while (nodeIterator.hasNext()) {
			//						OntologyTreeNode node = nodeIterator.next();
			//					((OntologyTreeNode) targetNode).getTargetNodeAttributes().addStagingPath(node.getTreePath());
			//					}
			//				}
			//				else {
			//					myOT.dropCommandCopyNodes(dropOperation);
			//				}
			//			}
			//			else if (targetNode instanceof OntologyTreeSubNode) {
			//				myOT.dropCommandCopyNodes(dropOperation);
			//				OntologyTreeSubNode subNode = (OntologyTreeSubNode)targetNode;
			//				Iterator<OntologyTreeNode> nodeIterator = selection.iterator();
			//				while (nodeIterator.hasNext()) {
			//					OntologyTreeNode node = nodeIterator.next();
			//					subNode.getParent().getTargetNodeAttributes().addStagingPath(node.getTreePath());
			//				}
			//			}
		}
		else if (data.toString().equals("subNode")) {
//			System.out.println("data = subnode");
			OntologyTreeSubNode subNode = null;// = NodeMoveDragListener.getSubNode();


			Iterator<MutableTreeNode> nodeIterator = selection.iterator();
			while (nodeIterator.hasNext()) {
				MutableTreeNode mNode = nodeIterator.next();
				if (mNode instanceof OntologyTreeSubNode) {
					subNode = ((OntologyTreeSubNode) mNode);
				}

				if (targetNode instanceof OntologyTreeNode) {
					((OntologyTreeNode) targetNode).getTargetNodeAttributes().addStagingPath(subNode.getStagingPath());
					subNode.getParent().getTargetNodeAttributes().removeSubNode(subNode);
				}
				else if (targetNode instanceof OntologyTreeSubNode) {
					((OntologyTreeSubNode) targetNode).getParent().getTargetNodeAttributes().addStagingPath(subNode.getStagingPath());
					subNode.getParent().getTargetNodeAttributes().removeSubNode(subNode);
				}
			}
		}
		else if (targetNode instanceof OntologyTreeNode){
			System.out.println("targetNode == OntologyTreeNode");

			Iterator<MutableTreeNode> nodeIterator = selection.iterator();
			while (nodeIterator.hasNext()) {
				MutableTreeNode mNode = nodeIterator.next();
				if (mNode instanceof OntologyTreeNode) {
					sourceNode = (OntologyTreeNode) mNode;
				}
				else if (mNode instanceof OntologyTreeSubNode) {
					sourceNode = ((OntologyTreeSubNode) mNode).getStagingParentNode();
				}
				//				System.out.println("TARGET = OTTREENODE");
				//					sourceNode = myOT.getOntologyTreeTarget().getNodeLists().getNodeByPath(path);
				if (OntologyEditorView.getOntologyTargetTree().getI2B2RootNode() == sourceNode || sourceNode.getParent() == (OntologyTreeNode)targetNode)
					System.err.println("SOURCE IS ROOT || TARGET IS PARENT");
				else {
					//Not dropped on self
					if (sourceNode.getTreePath() != ((OntologyTreeNode) targetNode).getTreePath()) {
//						System.out.println("sourceNode.getTreePath() != ((OntologyTreeNode) targetNode).getTreePath()");
						
						//Target is a Folder
						if (!((OntologyTreeNode)targetNode).isLeaf()) {
//							System.out.println("!((OntologyTreeNode)targetNode).isLeaf()");
							
							//Move the node
							OntologyTreeNode node2 = myOT.moveTargetNode(sourceNode, (OntologyTreeNode) targetNode);
							
							if (node2 != null)
								OntologyEditorView.getTargetTreeViewer().setExpandedState(node2,
										true);
						}
						else {
//							System.out.println("ELSE!!!");
							for (OntologyTreeSubNode subNode : sourceNode.getTargetNodeAttributes().getSubNodeList()) {
//								System.out.println("for");
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
		}
		else if (targetNode instanceof OntologyTreeSubNode) {

			//			System.out.println("targetNode instanceof OntologyTreeSubNode");
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