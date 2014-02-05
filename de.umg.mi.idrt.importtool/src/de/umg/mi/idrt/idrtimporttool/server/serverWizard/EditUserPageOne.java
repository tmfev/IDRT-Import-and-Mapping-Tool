package de.umg.mi.idrt.idrtimporttool.server.serverWizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.umg.mi.idrt.idrtimporttool.server.Settings.I2B2User;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.SimplePassGen;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class EditUserPageOne extends WizardPage {

	private static Text fullname_answer;
	private static Text email_answer;
	private static Text userPassword_answer;
	private static Text username_answer;
	private static Composite container1;
	private static String oldUserName;
	/**
	 * @return the portText
	 */
	public static String getEmail() {
		return email_answer.getText();
	}
	/**
	 * @return the ipText
	 */
	public static String getFullname() {
		return fullname_answer.getText();
	}
	public static String getGeneratedPassword(int i) {
		String alphabet = "ABCDEFGHJKLMNOPQRSTUVWXYZabcdefghjkimnopqrstuvwxyz";
		return (SimplePassGen.generate(alphabet, i));
	}
	public static String getNewUserID() {
		return username_answer.getText();
	}
	public static boolean getSendEmail() {
		return checkEMailButton.getSelection();
	}
	public static String getUsername() {
		return oldUserName;
	}
	/**
	 * @return the dBUserPasswordText
	 */
	public static String getUserPasswordText() {
		return userPassword_answer.getText();
	}
	private Label lblLength;
	private Button btnGeneratePassword;
	private Label label;

	private Composite composite;

	private I2B2User user;

	private Label lblRetypePassword;

	private Text retypePassword_answer;

	private Label lblSendEmail;

	private static Button checkEMailButton;

	private Text passwordLength;

	public EditUserPageOne(Server servers, I2B2User user) {
		super("Server Setup");
		setUser(user);
		setTitle("Edit User");
		setDescription("Edit User");
	}

	@Override
	public void createControl(Composite parent) {
		container1 = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(2, false);
		container1.setLayout(layout);

		Label username = new Label(container1, SWT.FILL | SWT.CENTER);
		username.setText("Username");

		username_answer = new Text(container1, SWT.FILL);
		username_answer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));
		username_answer.setText(user.getUserName());
		oldUserName = user.getUserName();
		Label fullname = new Label(container1, SWT.FILL | SWT.CENTER);
		fullname.setText("Fullname");
		// ipText = new Text(container, SWT.BORDER | SWT.SINGLE);
		fullname_answer = new Text(container1, SWT.FILL);
		fullname_answer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));
		fullname_answer.setText(user.getFullname());
		fullname_answer.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!fullname_answer.getText().isEmpty()) {
					EditUserPageOne.this.setPageComplete(true);
				}
			}
		});

		Label email = new Label(container1, SWT.FILL | SWT.CENTER);
		email.setText("E-Mail");
		// ipText = new Text(container, SWT.BORDER | SWT.SINGLE);
		email_answer = new Text(container1, SWT.FILL);
		email_answer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));
		email_answer.setText(user.getEmail());
		email_answer.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!email_answer.getText().isEmpty()) {
					EditUserPageOne.this.setPageComplete(true);
				}
			}
		});

		new Label(container1, SWT.NONE);
		new Label(container1, SWT.NONE);
		label = new Label(container1, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2,
				1));
		new Label(container1, SWT.NONE);
		new Label(container1, SWT.NONE);

		btnGeneratePassword = new Button(container1, SWT.NONE);
		btnGeneratePassword.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				String generatedPassword = getGeneratedPassword(Integer
						.parseInt(passwordLength.getText()));
				userPassword_answer.setText(generatedPassword);
				retypePassword_answer.setText(generatedPassword);
			}
		});
		btnGeneratePassword.setText("Generate Password");

		composite = new Composite(container1, SWT.NONE);
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_composite.widthHint = 162;
		composite.setLayoutData(gd_composite);
		composite.setLayout(new GridLayout(2, false));

		lblLength = new Label(composite, SWT.NONE);
		lblLength.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false,
				1, 1));
		lblLength.setText("Length");

		passwordLength = new Text(composite, SWT.BORDER);
		passwordLength.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		passwordLength.setText("5");
		Label userPassword = new Label(container1, SWT.FILL | SWT.CENTER);
		userPassword.setText("User Password");
		// ipText = new Text(container, SWT.BORDER | SWT.SINGLE);
		userPassword_answer = new Text(container1, SWT.FILL);

		userPassword_answer.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, false, 1, 1));
		userPassword_answer.setText("");
		userPassword_answer.setEchoChar('*');
		userPassword_answer.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!retypePassword_answer.getText().equals(
						userPassword_answer.getText())) {
					EditUserPageOne.this.setPageComplete(false);
					EditUserPageOne.this
							.setErrorMessage("Passwords do not match!");

				} else if ((userPassword_answer.getText().isEmpty() && retypePassword_answer
						.getText().isEmpty())
						|| retypePassword_answer.getText().equals(
								userPassword_answer.getText())) {
					EditUserPageOne.this.setPageComplete(true);
					EditUserPageOne.this.setErrorMessage(null);
				}
			}
		});

		setControl(container1);

		lblRetypePassword = new Label(container1, SWT.FILL);
		lblRetypePassword.setAlignment(SWT.LEFT);
		lblRetypePassword.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false, 1, 1));
		lblRetypePassword.setText("Retype Password");

		retypePassword_answer = new Text(container1, SWT.NONE);
		retypePassword_answer.setText("");
		retypePassword_answer.setEchoChar('*');
		retypePassword_answer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		new Label(container1, SWT.NONE);
		new Label(container1, SWT.NONE);
		label = new Label(container1, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2,
				1));
		new Label(container1, SWT.NONE);
		new Label(container1, SWT.NONE);
		lblSendEmail = new Label(container1, SWT.NONE);
		lblSendEmail.setText("Send E-Mail");

		checkEMailButton = new Button(container1, SWT.CHECK);
		new Label(container1, SWT.NONE);

		retypePassword_answer.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!retypePassword_answer.getText().equals(
						userPassword_answer.getText())) {
					EditUserPageOne.this.setPageComplete(false);
					EditUserPageOne.this
							.setErrorMessage("Passwords do not match!");

				} else if ((userPassword_answer.getText().isEmpty() && retypePassword_answer
						.getText().isEmpty())
						|| retypePassword_answer.getText().equals(
								userPassword_answer.getText())) {
					EditUserPageOne.this.setPageComplete(true);
					EditUserPageOne.this.setErrorMessage(null);
				}
			}
		});
		setPageComplete(true);
	}

	public I2B2User getUser() {
		return user;
	}

	public void setUser(I2B2User user) {
		this.user = user;
	}
}
