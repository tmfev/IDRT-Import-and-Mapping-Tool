package de.umg.mi.idrt.idrtimporttool.server.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.TreeItem;

import de.umg.mi.idrt.idrtimporttool.ImportWizard.DBWizardPage2;
import de.umg.mi.idrt.idrtimporttool.importidrt.ServerView;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.EditServerWizard;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class EditSourceServerCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TreeViewer viewer = ServerView.getSourceServerViewer();
		if ((DBWizardPage2.getImportDBViewer() != null)
				&& !DBWizardPage2.getImportDBViewer().getTree().isDisposed()) {
			viewer = DBWizardPage2.getImportDBViewer();
		}
		if (viewer.getTree().getSelection().length > 0) {
			TreeItem currentTreeItem = viewer.getTree().getSelection()[0];
			String serverUniqueID = currentTreeItem.getText();
			if (ServerList.isServer(serverUniqueID)) {
				Server currentServer = ServerList.getSourceServers().get(
						serverUniqueID);
				if (currentServer == null) {
					serverUniqueID = currentTreeItem.getParentItem().getText();
					currentServer = ServerList.getSourceServers().get(
							serverUniqueID);
				}
				WizardDialog wizardDialog = new WizardDialog(viewer
						.getControl().getShell(), new EditServerWizard(
						currentServer, false));
				wizardDialog.open();
			} else {
				TreeItem parentTreeitem = currentTreeItem.getParentItem();
				serverUniqueID = parentTreeitem.getText();
				Server currentServer = ServerList.getSourceServers().get(
						serverUniqueID);

				WizardDialog wizardDialog = new WizardDialog(viewer
						.getControl().getShell(), new EditServerWizard(
						currentServer, false));
				wizardDialog.open();
			}

		}
		if (((DBWizardPage2.getImportDBViewer() != null) && !DBWizardPage2
				.getImportDBViewer().getTree().isDisposed())
				|| !ServerView.getSourceServerViewer().getTree().isDisposed()) {
			ServerView.refresh();
		} else {
			DBWizardPage2.refresh();
		}
		viewer.refresh();
		return viewer;
	}
}
