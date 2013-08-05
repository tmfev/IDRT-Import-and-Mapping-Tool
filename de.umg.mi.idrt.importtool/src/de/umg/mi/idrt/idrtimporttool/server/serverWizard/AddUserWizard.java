package de.umg.mi.idrt.idrtimporttool.server.serverWizard;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.umg.mi.idrt.idrtimporttool.server.Settings.I2B2User;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class AddUserWizard extends Wizard {

	protected AddUserPageOne one;
	private Server server;

	public AddUserWizard(Server server) {
		super();
		this.server = server;
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		one = new AddUserPageOne(server);
		addPage(one);
	}

	@Override
	public boolean performFinish() {

		final String newFullname = AddUserPageOne.getFullname();
		final String newEmail = AddUserPageOne.getEmail();
		final String username = AddUserPageOne.getUsername();
		final String newPassword = I2B2User.getHashedPassword(AddUserPageOne
				.getUserPasswordText());

		java.util.Date today = new java.util.Date();
		java.sql.Date date = new java.sql.Date(today.getTime());

		if (!username.isEmpty() && !newPassword.isEmpty()) {
			ServerList.addUser(server, username, newFullname, newPassword,
					newEmail, date);

			if (AddUserPageOne.getSendEmail() && !newEmail.isEmpty()) {
				System.out.println("sending email");
				URI mailtoURI;
				try {
					mailtoURI = new URI("mailto:" + newEmail
							+ "?subject=New%20Password&body=Hello%20"
							+ newFullname.replace(" ", "%20")
							+ "!%0A%0AYour%20Login-Name%20is:%20" + username
							+ "%0AYour%20new%20password%20is:%20"
							+ AddUserPageOne.getUserPasswordText()
							+ "%0A%0A%0ARegards,%0A%0Ai2b2-Admin%20Goettingen");
					Desktop.getDesktop().mail(mailtoURI);
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else if (AddUserPageOne.getSendEmail() && newEmail.isEmpty()) {
				MessageDialog.openError(Display.getDefault()
						.getActiveShell(), "Error", "Missing EMail!");
				return false;
			}
		}
		// TreeItem[] a =
		// AdminServerCommand.getUserTree().getTree().getSelection();
		// AdminServerCommand.getUserTree().getTree().select(a[0]);
		return true;

	}

}