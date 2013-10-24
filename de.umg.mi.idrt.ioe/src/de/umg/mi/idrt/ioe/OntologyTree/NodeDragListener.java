package de.umg.mi.idrt.ioe.OntologyTree;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> Department of
 *         Medical Informatics Goettingen www.mi.med.uni-goettingen.de
 * 
 *         drag listener for getting data from the source tree to the target tree
 *         
 */

public class NodeDragListener implements DragSourceListener {


	public NodeDragListener() {
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		Console.info("Finshed Drag");
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		Console.info("dragSetData");
		event.doit=true;
				event.data="stagingTreeViewer";
		// Here you do the convertion to the type which is expected.
		//		Iterator<OntologyTreeNode> nodeIterator = selection.iterator();
		//		while (nodeIterator.hasNext()) {
		//			OntologyTreeNode firstElement = nodeIterator.next();
		//			System.out.println("NEXT: " + firstElement.getName());
		//			Console.info("firstElement dataType:"
		//					+ selection.getFirstElement().getClass().getSimpleName());
		//
		//			if (OTNodeTransfer.getInstance().isSupportedType(event.dataType)) {
		//				event.data = firstElement.getName();// firstElement.getShortDescription()
		//				// +
		//				event.doit=true;
		//				// " " +
		//				// firstElement.getLongDescription();
		//
		//			}
		//
		//			if (firstElement != null
		//					&& firstElement.getOntologyCellAttributes() != null) {
		//				event.data = firstElement.getOntologyCellAttributes()
		//						.getC_FULLNAME();
		//				Console.info("... dragged the node with the treePath of \""
		//						+ firstElement.getTreePath() + "\"");
		//				event.doit=true;
		//			} else {
		//				event.data = "";
		//				Console.info("... dragged nothing");
		//			}
		//		}
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		Console.info("Start Drag");
		event.doit = true;
		//
		//		IStructuredSelection selection = (IStructuredSelection) viewer
		//				.getSelection();
		//
		//		Console.info(" - number of elements:" + selection.size());
		//
		//		Iterator<OntologyTreeNode> nodeIterator = selection.iterator();
		//		while (nodeIterator.hasNext()) {
		//			OntologyTreeNode node = nodeIterator.next();
		//			System.out.println("NEXT2: " + node.getName());
		//			//		OntologyTreeNode node = (OntologyTreeNode) selection.getFirstElement();
		//
		//			if (node != null) {
		//				Console.info(" - node.getNodeType(): " + node.getNodeType());
		//				event.doit=true;
		//			}
		//			if (NodeType.I2B2ROOT.equals(node.getNodeType())) {
		//				event.doit = false;
		//				Application
		//				.getStatusView()
		//				.addMessage(
		//						new SystemMessage(
		//								"The user entity tried to drag the node \'"
		//										+ node.getName()
		//										+ "\', but thats not even possible at all.",
		//										SystemMessage.MessageType.ERROR,
		//										SystemMessage.MessageLocation.MAIN));
		//			} 
		//		}
	}

}