package de.umg.mi.idrt.idrtimporttool.server.Settings;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class ServerDragSourceListener implements DragSourceListener {

	private final TreeViewer viewer;

	public ServerDragSourceListener(TreeViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		System.out.println("Finshed Drag");
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		// Here you do the convertion to the type which is expected.
		try {
			IStructuredSelection selection = (IStructuredSelection) viewer
					.getSelection();
			Server server = (Server) selection.getFirstElement();

			System.out.println("dragged server: " + server.toString());
			if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
				event.data = server.getUniqueID();
			}
		} catch (Exception e) {
			MessageDialog.openError(viewer.getControl().getShell(), "Failure",
					"You cannot drop this target here.");
		}

	}

	@Override
	public void dragStart(DragSourceEvent event) {
		System.out.println("Start Drag");
	}

}
