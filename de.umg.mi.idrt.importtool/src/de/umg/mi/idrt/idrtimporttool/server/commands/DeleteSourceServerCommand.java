package de.umg.mi.idrt.idrtimporttool.server.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;

import de.umg.mi.idrt.idrtimporttool.ImportWizard.DBWizardPage2;
import de.umg.mi.idrt.idrtimporttool.importidrt.Application;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.importtool.views.ServerView;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class DeleteSourceServerCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TreeViewer viewer = ServerView.getSourceServerViewer();
		if ((DBWizardPage2.getImportDBViewer() != null)
				&& !DBWizardPage2.getImportDBViewer().getTree().isDisposed()) {
			viewer = DBWizardPage2.getImportDBViewer();
		}

		boolean result;
		if (viewer.getTree().getSelectionCount() > 1) {
			result = MessageDialog.openConfirm(Application.getShell(),
					"Delete Multiple Servers?",
					"Do you really want to delete the selected servers?");
		} else {
			String currentServerName = viewer.getTree().getSelection()[0]
					.getText();
			result = MessageDialog.openConfirm(Application.getShell(), "Delete "
					+ currentServerName + "?",
					"Do you really want to delete the server "
							+ currentServerName + "?");
		}

		if (result) {

			if (viewer.getTree().getSelectionCount() > 1) {
				for (int i = 0; i < viewer.getTree().getSelectionCount(); i++) {

					TreeItem currentTreeItem = viewer.getTree().getSelection()[i];
					String serverUniqueID = currentTreeItem.getText();
					Server currentServer = null;
					if (ServerList.isServer(serverUniqueID)) {
						currentServer = ServerList.getSourceServers().get(
								serverUniqueID);
					} else {
						TreeItem parentTreeitem = currentTreeItem
								.getParentItem();
						serverUniqueID = parentTreeitem.getText();
						currentServer = ServerList.getSourceServers().get(
								serverUniqueID);
					}
					ServerList.removeServer(currentServer);
				}

			} else {
				TreeItem currentTreeItem = viewer.getTree().getSelection()[0];
				String serverUniqueID = currentTreeItem.getText();
				Server currentServer = null;
				if (ServerList.isServer(serverUniqueID)) {
					currentServer = ServerList.getSourceServers().get(
							serverUniqueID);

				} else {
					TreeItem parentTreeitem = currentTreeItem.getParentItem();
					serverUniqueID = parentTreeitem.getText();
					currentServer = ServerList.getSourceServers().get(
							serverUniqueID);

					if (currentServer == null) {
						// noch mal zum parent
						serverUniqueID = parentTreeitem.getParentItem()
								.getText();
						currentServer = ServerList.getSourceServers().get(
								serverUniqueID);
					}
				}
				ServerList.removeServer(currentServer);
				if (((DBWizardPage2.getImportDBViewer() != null) && !DBWizardPage2
						.getImportDBViewer().getTree().isDisposed())
						|| !ServerView.getSourceServerViewer().getTree()
								.isDisposed()) {
					ServerView.refresh();
				} else {
					DBWizardPage2.refresh();
				}
				if (viewer.getTree().getItemCount() > 0) {
					viewer.getTree().select(viewer.getTree().getItem(0));
				}
			}
		}
		return viewer;
	}

}
