package de.umg.mi.idrt.idrtimporttool.server.serverWizard;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;

import de.umg.mi.idrt.idrtimporttool.importidrt.Application;
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
	private String activator;

	public EditServerWizard(Server server, boolean isTargetServer, String activator) {
		super();
		this.isTargetServer = isTargetServer;
		this.server = server;
		serverUniqueID = server.getUniqueID();
		setNeedsProgressMonitor(true);
		this.activator = activator;
	}

	@Override
	public void addPages() {
		one = new EditServerPageOne(server,activator);
		addPage(one);
	}

	@Override
	public boolean performFinish() {

		Server editedServer = new Server(EditServerPageOne.getUniqueIDText(),
				EditServerPageOne.getIpText(), EditServerPageOne.getPortText(),
				EditServerPageOne.getDBUserText(),
				EditServerPageOne.getDBUserPasswordText(),
				EditServerPageOne.getDBSIDText(),EditServerPageOne.getDBType(),EditServerPageOne.getCheckUseWinAuth(),
				EditServerPageOne.getCheckStorePassword());
		editedServer.setWhType(EditServerPageOne.getDB_WH_Combo());
		System.out.println("EDITED SERVER");
		System.out.println(editedServer);
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
									Application.getShell(),
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
									Application.getShell(),
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
									Application.getShell(),
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
			MessageDialog.openInformation(Application.getShell(), "Server already exists.",
					"The Server already exists. Please choose another Name.");
			return false;
		}
	
	}

}