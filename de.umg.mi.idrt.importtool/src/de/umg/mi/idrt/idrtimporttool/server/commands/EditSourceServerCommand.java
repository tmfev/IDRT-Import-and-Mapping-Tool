package de.umg.mi.idrt.idrtimporttool.server.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.TreeItem;

import de.umg.mi.idrt.idrtimporttool.ImportWizard.DBWizardPageTwo;
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
		if ((DBWizardPageTwo.getImportDBViewer() != null)
				&& !DBWizardPageTwo.getImportDBViewer().getTree().isDisposed()) {
			viewer = DBWizardPageTwo.getImportDBViewer();
		}
		if (viewer.getTree().getSelection().length > 0) {
			TreeItem currentTreeItem = viewer.getTree().getSelection()[0];
			String serverUniqueID = currentTreeItem.getText();
			System.out.println("testing: " + serverUniqueID);
			if (ServerList.isServer(serverUniqueID)) {
				System.out.println("is server");
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
				System.out.println("getting parent:");
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
		if (((DBWizardPageTwo.getImportDBViewer() != null) && !DBWizardPageTwo
				.getImportDBViewer().getTree().isDisposed())
				|| !ServerView.getSourceServerViewer().getTree().isDisposed()) {
			ServerView.refresh();
		} else {
			DBWizardPageTwo.refresh();
		}
		viewer.refresh();
		return viewer;
	}
}
