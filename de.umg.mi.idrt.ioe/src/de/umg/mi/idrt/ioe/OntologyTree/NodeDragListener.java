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

	private TreeViewer viewer;
	public NodeDragListener(TreeViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		Console.info("Finshed Drag");
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		Console.info("dragSetData2");
		event.doit=true;
		event.data="stagingTreeViewer";
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		if (selection.getFirstElement() instanceof OntologyTreeNode) {
			OntologyTreeNode firstElement = (OntologyTreeNode) selection
					.getFirstElement();
			if (firstElement.isModifier()){
				System.out.println("MODIFIERS");
				event.doit=false;
			}
			else {
				Console.info("Start Drag");
				event.doit = true;
			}
			
		}
		
	}

}