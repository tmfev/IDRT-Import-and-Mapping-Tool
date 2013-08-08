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
public class AddSourceServerWizard extends Wizard {

	protected AddServerPageOne one;

	public AddSourceServerWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		one = new AddServerPageOne();
		// two = new CSVWizardPageTwo();
		addPage(one);
		// addPage(two);
	}

	@Override
	public boolean performFinish() {
		System.out.println("finish");
		Server newServer = new Server(AddServerPageOne.getUniqueIDText(),
				AddServerPageOne.getIpText(), AddServerPageOne.getPortText(),
				AddServerPageOne.getDBUserText(),
				AddServerPageOne.getDBUserPasswordText(),
				AddServerPageOne.getDBSIDText(),AddServerPageOne.getDBType(),AddServerPageOne.getCheckUseWinAuth());

		if (IDRTImport.testDB(newServer)) {
			ServerList.addSourceServer(newServer);
		} else {
			boolean result = MessageDialog
					.openConfirm(
							Display.getDefault()
							.getActiveShell(),
							"Server not Responding!",
							"The Server cannot be reached. There might be an error in the settings.\nSave anyway?");
			if (result) {
				ServerList.addSourceServer(newServer);
			} else {
				return false;
			}
		}
		return true;
	}

}