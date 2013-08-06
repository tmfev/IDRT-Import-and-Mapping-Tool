package de.umg.mi.idrt.ioe.OntologyTree;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;

import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.SystemMessage;
import de.umg.mi.idrt.ioe.view.OTNodeTransfer;


public class NodeDragListener implements DragSourceListener {

	private final TreeViewer viewer;

	public NodeDragListener(TreeViewer viewer) {
		this.viewer = viewer;
		
		
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		System.out.println("Finshed Drag");
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		System.out.println("dragSetData");

		// Here you do the convertion to the type which is expected.
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		
		System.out.println("firstElement dataType:" + selection.getFirstElement().getClass().getSimpleName());
		
		OntologyTreeNode firstElement = (OntologyTreeNode) selection.getFirstElement();


		if (OTNodeTransfer.getInstance().isSupportedType(event.dataType)) {
			event.data = firstElement.getName();// firstElement.getShortDescription() +
										// " " +
										// firstElement.getLongDescription();

		}
		
		
		
		if ( firstElement != null && firstElement.getOntologyCellAttributes() != null ){
			event.data = firstElement.getOntologyCellAttributes().getC_FULLNAME();
			System.out.println("... dragged the node with the treePath of \"" + firstElement.getTreePath() + "\"");
		} else {
			event.data = "";
			System.out.println("... dragged nothing");
		}

		
		
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		
		System.out.println("Start Drag");
		
		
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		
		
		System.out.println(" - number of elements:" + selection.size());
		
		OntologyTreeNode node = (OntologyTreeNode) selection.getFirstElement();
		
		
		if ( node == null )
			System.out.println(" - node is null");
		
		System.out.println(" - node.getNodeType(): " + node.getNodeType());
		
		if ( node == null ){
			
			// nothing to do here
			
		} else if ( NodeType.I2B2ROOT.equals( node.getNodeType() ) ){
			 event.doit = false;
			 Application.getStatusView().addMessage(
						new SystemMessage("The user entity tried to drag the node \'"
								+ node.getName() + "\', but thats not even possible at all.",
								SystemMessage.MessageType.ERROR,
								SystemMessage.MessageLocation.MAIN));
		} else if ( selection.size() > 1 ){
			
			Application.getStatusView().addMessage(
					new SystemMessage("The user entity is trying to drag multiple nodes. Just the first selection will be used and the other"
							+ ( selection.size() == 2 ? " node" : " " + ( selection.size() - 1 ) + " nodes" ) + " will be ignored for now.",
							SystemMessage.MessageType.INFO,
							SystemMessage.MessageLocation.MAIN));
			
		}
		
		
		
	}

}