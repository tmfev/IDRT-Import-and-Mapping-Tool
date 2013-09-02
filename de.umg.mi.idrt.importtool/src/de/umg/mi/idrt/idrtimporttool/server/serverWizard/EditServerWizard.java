package de.umg.mi.idrt.idrtimporttool.server.serverWizard;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;

import de.umg.mi.idrt.idrtimporttool.importidrt.IDRTImport;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class EditServerWizard extends Wizard {

	protected EditServerPageOne one;
	private Server server;
	private String serverUniqueID;
	private boolean isTargetServer;

	public EditServerWizard(Server server, boolean isTargetServer) {
		super();
		this.isTargetServer = isTargetServer;
		this.server = server;
		serverUniqueID = server.getUniqueID();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		one = new EditServerPageOne(server);
		// two = new CSVWizardPageTwo();
		addPage(one);
		// addPage(two);
	}

	@Override
	public boolean performFinish() {


		Server editedServer = new Server(EditServerPageOne.getUniqueIDText(),
				EditServerPageOne.getIpText(), EditServerPageOne.getPortText(),
				EditServerPageOne.getDBUserText(),
				EditServerPageOne.getDBUserPasswordText(),
				EditServerPageOne.getDBSIDText(),EditServerPageOne.getDBType(),EditServerPageOne.getCheckUseWinAuth());

		if (!ServerList.getSourceServers().containsKey(
				editedServer.getUniqueID())
				&& isTargetServer) { // !ServerList.getServers().containsKey(editedServer.getUniqueID())

			if (ServerList.getSourceServers().containsKey(serverUniqueID)) {
				ServerList.removeServer(serverUniqueID);
				if (IDRTImport.testDB(editedServer)) {
					ServerList.addSourceServer(editedServer);
				} else {
					boolean result = MessageDialog
							.openConfirm(
									Display.getDefault()
									.getActiveShell(),
									"Server not Responding!",
									"The Server cannot be reached. There might be an error in the settings.\nSave anyway?");
					if (result) {
						ServerList.addSourceServer(editedServer);
					} else {
						ServerList.addSourceServer(server);
						return false;
					}
				}
			} else if (ServerList.getTargetServers().containsKey(serverUniqueID)) {
				ServerList.removeServer(serverUniqueID);
				if (IDRTImport.testDB(editedServer)) {
					ServerList.addServer(editedServer);
				} else {
					boolean result = MessageDialog
							.openConfirm(
									Display.getDefault()
									.getActiveShell(),
									"Server not Responding!",
									"The Server cannot be reached. There might be an error in the settings.\nSave anyway?");
					if (result) {
						ServerList.addServer(editedServer);
					} else {
						ServerList.addServer(server);
						return false;
					}
				}
			}
			return true;
		} else if (!ServerList.getTargetServers().containsKey(
				editedServer.getUniqueID())
				&& !isTargetServer) {
			if (ServerList.getSourceServers().containsKey(serverUniqueID)) {
				ServerList.removeServer(serverUniqueID);
				if (IDRTImport.testDB(editedServer)) {
					ServerList.addSourceServer(editedServer);
				} else {
					boolean result = MessageDialog
							.openConfirm(
									Display.getDefault()
									.getActiveShell(),
									"Server not Responding!",
									"The Server cannot be reached. There might be an error in the settings.\nSave anyway?");
					if (result) {
						ServerList.addSourceServer(editedServer);
					} else {
						ServerList.addSourceServer(server);
						return false;
					}
				}
			} else if (ServerList.getTargetServers().containsKey(serverUniqueID)) {
				ServerList.removeServer(serverUniqueID);
				if (IDRTImport.testDB(editedServer)) {
					ServerList.addServer(editedServer);
				} else {
					boolean result = MessageDialog
							.openConfirm(
									Display.getDefault()
									.getActiveShell(),
									"Server not Responding!",
									"The Server cannot be reached. There might be an error in the settings.\nSave anyway?");
					if (result) {
						ServerList.addServer(editedServer);
					} else {
						ServerList.addServer(server);
						return false;
					}
				}
			}
			return true;
		} else {
			MessageDialog.openInformation(Display.getDefault()
					.getActiveShell(), "Server already exists.",
					"The Server already exists. Please choose another Name.");
			return false;
		}

	}

}