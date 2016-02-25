package de.umg.mi.idrt.admin.wizard;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.jface.wizard.Wizard;

import de.umg.mi.idrt.idrtimporttool.server.Settings.I2B2User;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.importtool.views.AdminTargetServerView;


/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class EditUserWizard extends Wizard {

	protected EditUserPageOne one;
	private Server server;
	private I2B2User user;

	public EditUserWizard(I2B2User user, Server server) {
		super();
		this.user = user;
		this.server = server;
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		one = new EditUserPageOne(server, user);
		addPage(one);
	}

	@Override
	public boolean performFinish() {

		// try {
		final String newFullname = EditUserPageOne.getFullname();

		final String newEmail = EditUserPageOne.getEmail();
		final String oldUsername = EditUserPageOne.getUsername();
		final String newUsername = EditUserPageOne.getNewUserID();

		// Class.forName("oracle.jdbc.driver.OracleDriver");
		//
		// DriverManager.setLoginTimeout(2);
		// connect = DriverManager.getConnection(
		// "jdbc:oracle:thin:@" + server.getIp() + ":"
		// + server.getPort() + ":" + server.getSID(),
		// server.getUser(), server.getPassWord());
		// connect.setAutoCommit(true);
		// statement = connect.createStatement();
		java.util.Date today = new java.util.Date();
		java.sql.Date date = new java.sql.Date(today.getTime());
		// Result set get the result of the SQL query
		ServerList.editUser(server, newUsername, oldUsername, newFullname,
				newEmail, date);

		if (!EditUserPageOne.getUserPasswordText().isEmpty()) {
			final String newPassword = I2B2User
					.getHashedPassword(EditUserPageOne.getUserPasswordText());
			ServerList.editUserPW(server, oldUsername, newPassword);

			// statement
			// .executeQuery("update i2b2pm.pm_user_data set password='"+newPassword
			// + "' where user_id='" + oldUsername + "'");
		}
		AdminTargetServerView.refresh();
		if (EditUserPageOne.getSendEmail()) {

			try {
				URI mailtoURI = new URI("mailto:" + newEmail
						+ "?subject=New%20Password&body=Hello%20"
						+ newFullname.replace(" ", "%20")
						+ "!%0A%0AYour%20Login-Name%20is:%20" + oldUsername
						+ "%0AYour%20new%20password%20is:%20"
						+ EditUserPageOne.getUserPasswordText()
						+ "%0A%0A%0ARegards,%0A%0Ai2b2-Admin%20Goettingen");
				Desktop.getDesktop().mail(mailtoURI);
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		// TreeItem[] a =
		// AdminServerCommand.getUserTree().getTree().getSelection();
		// AdminServerCommand.getUserTree().getTree().select(a[0]);
		return true;
		// } catch (ClassNotFoundException e) {
		// e.printStackTrace();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// return false;

	}

}