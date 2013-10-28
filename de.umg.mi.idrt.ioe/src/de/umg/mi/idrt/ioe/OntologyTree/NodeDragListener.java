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
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		Console.info("Start Drag");
		event.doit = true;
	}

}