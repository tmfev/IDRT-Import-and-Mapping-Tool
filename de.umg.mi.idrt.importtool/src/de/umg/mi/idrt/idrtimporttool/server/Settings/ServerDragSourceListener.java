package de.umg.mi.idrt.idrtimporttool.server.Settings;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;

import de.umg.mi.idrt.idrtimporttool.importidrt.Application;

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
//		try {
//			IStructuredSelection selection = (IStructuredSelection) viewer
//					.getSelection();
//
//			if (selection.getFirstElement() instanceof Server) {
//				System.out.println("SERVER DRAG");
//				Server server = (Server) selection.getFirstElement();
//				System.out.println(server.getName());
//				if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
//					System.out.println("is supported");
//					event.data = server.getUniqueID();
//				}
//				else
//					System.out.println("is not supported");
//			}
//			else {
//				event.data = selection.getFirstElement();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			MessageDialog.openError(Application.getShell(), "Failure",
//					"You cannot drop this target here.");
//		}
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		event.data = selection.getFirstElement();
	}

	@Override
	public void dragStart(DragSourceEvent event) {
//		IStructuredSelection selection = (IStructuredSelection) viewer
//				.getSelection();
//
//		if (selection.getFirstElement() instanceof I2b2Project) {
//			System.out.println("PROJECT");
//			event.data = selection.getFirstElement();
//		}
//		else if (selection.getFirstElement() instanceof Server){
//			System.out.println("SERVER");
//			event.data = selection.getFirstElement();
//		}
		 
	}

}
