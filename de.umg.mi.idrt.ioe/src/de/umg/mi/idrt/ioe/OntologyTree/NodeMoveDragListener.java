package de.umg.mi.idrt.ioe.OntologyTree;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;

/**
 * @author Benjamin Baum
 * <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 * @author Christian Bauer
 * <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen 
 *         www.mi.med.uni-goettingen.de
 *         
 *         drag listener for getting data from the source tree to the target tree
 */

public class NodeMoveDragListener implements DragSourceListener {

	private static OntologyTreeSubNode subNode;
	public static OntologyTreeSubNode getSubNode() {
		return subNode;
	}

	public static void setSubNode(OntologyTreeSubNode subNode2) {
		subNode = subNode2;
	}

	private final TreeViewer viewer;

	public NodeMoveDragListener(TreeViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		System.out.println("dragFinished");
		viewer.refresh();
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		System.out.println("dragSetData");
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		if (selection.getFirstElement() instanceof OntologyTreeNode) {
			OntologyTreeNode firstElement = (OntologyTreeNode) selection
					.getFirstElement();
			if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
				event.data = firstElement.getTreePath();
			}
		}
		else if (selection.getFirstElement() instanceof OntologyTreeSubNode) {
			setSubNode((OntologyTreeSubNode) selection
					.getFirstElement());
			event.data = "subNode";
		}
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		System.out.println("@dragStart");
	}

}