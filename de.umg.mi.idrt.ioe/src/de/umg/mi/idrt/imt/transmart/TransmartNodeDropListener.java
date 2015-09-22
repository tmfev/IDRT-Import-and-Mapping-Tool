package de.umg.mi.idrt.imt.transmart;


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

public class TransmartNodeDropListener extends ViewerDropAdapter {

	private final Viewer viewer;
	private TransmartConfigTreeItem sourceNode = null;

	private static DefaultMutableTreeNode targetNode = null;

	public TransmartNodeDropListener(Viewer viewer) {
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

		if (event.item == null) {
		}
		if(event.item.getData() instanceof TransmartConfigTreeItem) {
			System.out.println("TRUE");
			targetNode = (TransmartConfigTreeItem) determineTarget(event);
			if (((TransmartConfigTreeItem)targetNode).isLeaf()) {
				System.out.println("isLeaf");
				OntologyEditorView.setNotYetSaved(true);
				super.drop(event);
			}
			else {
				System.out.println("ELSE");
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
		Console.info("Dropped performed with data:");
		Console.info(" - data: " + data.getClass().getSimpleName());
		IStructuredSelection selection = (IStructuredSelection) data;
		


		if (targetNode instanceof TransmartConfigTreeItem){
			System.out.println("targetNode == TransmartConfigTreeItem");

			Iterator<MutableTreeNode> nodeIterator = selection.iterator();
			while (nodeIterator.hasNext()) {
				MutableTreeNode mNode = nodeIterator.next();
				if (mNode instanceof TransmartConfigTreeItem) {
					sourceNode = (TransmartConfigTreeItem) mNode;
				}
				//				System.out.println("TARGET = OTTREENODE");
				//					sourceNode = myOT.getOntologyTreeTarget().getNodeLists().getNodeByPath(path);
				if (TransmartConfigTree.getRoot() == sourceNode || sourceNode.getParent() == (TransmartConfigTreeItem)targetNode)
					System.err.println("SOURCE IS ROOT || TARGET IS PARENT");
				else {
					//Not dropped on self
					if (sourceNode != ((TransmartConfigTreeItem) targetNode)) {
//						System.out.println("sourceNode.getTreePath() != ((OntologyTreeNode) targetNode).getTreePath()");
						
						//Target is a Folder
						if (!((TransmartConfigTreeItem)targetNode).isLeaf()) {
//							System.out.println("!((OntologyTreeNode)targetNode).isLeaf()");
							
							//Move the node
							TransmartConfigTreeItem parent = sourceNode.getParent();
							
							((TransmartConfigTreeItem) targetNode).addNode(sourceNode);
							parent.removeNode(sourceNode);
						}
						sourceNode.removeFromParent();
					}
					else {
						System.err.println("TARGET == SOURCE");
					}
				}
			}
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