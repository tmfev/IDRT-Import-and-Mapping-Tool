package de.umg.mi.idrt.idrtimporttool.server.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

import de.umg.mi.idrt.idrtimporttool.importidrt.ServerView;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class DeleteTargetServerCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("delete server command");
		TreeViewer viewer = ServerView.getTargetServersViewer();

		boolean result;
		if (viewer.getTree().getSelectionCount() > 1) {
			result = MessageDialog.openConfirm(Display.getDefault()
					.getActiveShell(),
					"Delete Multiple Servers?",
					"Do you really want to delete the selected servers?");
		} else {
			String currentServerName = viewer.getTree().getSelection()[0]
					.getText();
			result = MessageDialog.openConfirm(Display.getDefault()
					.getActiveShell(), "Delete "
					+ currentServerName + "?",
					"Do you really want to delete the server "
							+ currentServerName + "?");
		}

		if (result) {

			if (viewer.getTree().getSelectionCount() > 1) {
				System.out.println("multiple servers");
				for (int i = 0; i < viewer.getTree().getSelectionCount(); i++) {

					TreeItem currentTreeItem = viewer.getTree().getSelection()[i];
					String serverUniqueID = currentTreeItem.getText();
					Server currentServer = null;
					if (ServerList.isServer(serverUniqueID)) {
						System.out.println("is server");
						currentServer = ServerList.getTargetServers().get(
								serverUniqueID);
					} else {
						System.out.println("getting parent:");
						TreeItem parentTreeitem = currentTreeItem
								.getParentItem();
						serverUniqueID = parentTreeitem.getText();
						currentServer = ServerList.getTargetServers().get(
								serverUniqueID);
					}
					ServerList.removeServer(currentServer);
				}
				viewer.refresh();
			} else {
				TreeItem currentTreeItem = viewer.getTree().getSelection()[0];
				String serverUniqueID = currentTreeItem.getText();
				Server currentServer = null;
				if (ServerList.isServer(serverUniqueID)) {
					System.out.println("is server");
					currentServer = ServerList.getTargetServers().get(serverUniqueID);

				} else {
					System.out.println("getting parent:");
					TreeItem parentTreeitem = currentTreeItem.getParentItem();
					serverUniqueID = parentTreeitem.getText();
					currentServer = ServerList.getTargetServers().get(serverUniqueID);
				}
				ServerList.removeServer(currentServer);
				viewer.refresh();
				if (viewer.getTree().getItemCount() > 0) {
					System.out.println("setselection");
					System.out.println(viewer.getTree().getItem(0).getText());
					viewer.getTree().select(viewer.getTree().getItem(0));

				}
			}

		}
		return viewer;
	}

}
