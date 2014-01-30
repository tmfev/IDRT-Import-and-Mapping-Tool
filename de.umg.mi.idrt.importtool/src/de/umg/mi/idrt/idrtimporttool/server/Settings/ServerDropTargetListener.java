package de.umg.mi.idrt.idrtimporttool.server.Settings;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;

import de.umg.mi.idrt.idrtimporttool.importidrt.Application;
import de.umg.mi.idrt.idrtimporttool.importidrt.ServerView;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class ServerDropTargetListener extends ViewerDropAdapter {

	public ServerDropTargetListener(Viewer viewer) {
		super(viewer);
	}

	@Override
	public void drop(DropTargetEvent event) {
		int location = determineLocation(event);
		// String target = ((Server) determineTarget(event)).getUniqueID();
		String translatedLocation = "";
		switch (location) {
			case 1:
				translatedLocation = "Dropped before the target ";
				break;
			case 2:
				translatedLocation = "Dropped after the target ";
				break;
			case 3:
				translatedLocation = "Dropped on the target ";
				break;
			case 4:
				translatedLocation = "Dropped into nothing ";
				break;
		}
		super.drop(event);
	}

	@Override
	public boolean performDrop(Object data) {
		Server draggedServer = ServerList.getTargetServers().get(data);
		
		if (draggedServer != null) {
		Server newServer = new Server("copy of " + draggedServer.getUniqueID(),
				draggedServer.getIp(), draggedServer.getPort(),
				draggedServer.getUser(), draggedServer.getPassword(),
				draggedServer.getSID(),draggedServer.getDatabaseType(),draggedServer.isUseWinAuth(),draggedServer.isSavePassword());

		ServerList.addSourceServer(newServer);
		TreeViewer viewer = ServerView.getSourceServerViewer();
		viewer.refresh();
		}
		else {
			MessageDialog.openError(Application.getShell(), "Error", "You cannot drop this item here!");
			}
		return false;
	}

	@Override
	public boolean validateDrop(Object target, int operation,
			TransferData transferType) {
		return true;
	}

}
