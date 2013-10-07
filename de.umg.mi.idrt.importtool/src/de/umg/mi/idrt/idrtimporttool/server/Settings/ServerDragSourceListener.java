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
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		// Here you do the convertion to the type which is expected.
		try {
			IStructuredSelection selection = (IStructuredSelection) viewer
					.getSelection();

			if (selection.getFirstElement() instanceof Server) {
				Server server = (Server) selection.getFirstElement();
				System.out.println(server.getName());
				if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
					event.data = server.getUniqueID();
				}
			}
			else {
				event.data = selection.getFirstElement();
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openError(viewer.getControl().getShell(), "Failure",
					"You cannot drop this target here.");
		}

	}

	@Override
	public void dragStart(DragSourceEvent event) {
	}

}
