package de.umg.mi.idrt.idrtimporttool.server.serverWizard;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import de.umg.mi.idrt.idrtimporttool.importidrt.Application;
import de.umg.mi.idrt.idrtimporttool.importidrt.IDRTImport;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class AddServerWizard extends Wizard {

	protected AddServerPageOne one;
	private String activator;
	public AddServerWizard(String activator) {
		super();
		setNeedsProgressMonitor(true);
		this.activator = activator;
	}

	@Override
	public void addPages() {
		one = new AddServerPageOne(activator);
		// two = new CSVWizardPageTwo();
		addPage(one);
		// addPage(two);
	}

	@Override
	public boolean performFinish() {
		Server newServer = new Server(AddServerPageOne.getUniqueIDText(),
				AddServerPageOne.getIpText(), AddServerPageOne.getPortText(),
				AddServerPageOne.getDBUserText(),
				AddServerPageOne.getDBUserPasswordText(),
				AddServerPageOne.getDBSIDText(),AddServerPageOne.getDBType(),AddServerPageOne.getCheckUseWinAuth(),AddServerPageOne.getCheckStorePassword());
		newServer.setWhType(AddServerPageOne.getDB_WH_Combo());

		if (IDRTImport.testDB(newServer)) {
			ServerList.addServer(newServer);
		} else {
			boolean result = MessageDialog
					.openConfirm(
							Application.getShell(),
							"Server not Responding!",
							"The Server cannot be reached. There might be an error in the settings.\nSave anyway?");
			if (result) {
				ServerList.addServer(newServer);
			} else {
				return false;
			}
		}
		return true;
	}

}