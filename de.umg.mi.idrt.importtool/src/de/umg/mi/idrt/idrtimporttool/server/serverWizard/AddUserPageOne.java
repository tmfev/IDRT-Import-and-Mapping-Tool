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

import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.SimplePassGen;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class AddUserPageOne extends WizardPage {

	private static Text fullname_answer;
	private static Text email_answer;
	private static Text userPassword_answer;
	private static Text username_answer;
	private static Composite container1;
	/**
	 * @return the portText
	 */
	public static String getEmail() {
		return email_answer.getText();
	}
	public static String getGeneratedPassword(int i) {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		return (SimplePassGen.generate(alphabet, i));
	}
	// private static void setText(String text){
	// DBTest.setText(text);
	// DBTest.redraw();
	// DBTest.update();
	// }
	private Label lblRetypePassword;
	private Text retypePassword_answer;
	private Label lblSendEmail;
	private static Button checkEMailButton;
	/**
	 * @return the ipText
	 */
	public static String getFullname() {
		return fullname_answer.getText();
	}
	public static boolean getSendEmail() {
		return checkEMailButton.getSelection();
	}
	public static String getUsername() {
		return username_answer.getText();
	}
	/**
	 * @return the dBUserPasswordText
	 */
	public static String getUserPasswordText() {
		return userPassword_answer.getText();
	}

	private Button btnGeneratePassword;

	private Label label;

	private Composite composite;

	private Label lblLength;

	private Text passwordLength;

	private Label label_1;

	public AddUserPageOne(Server server) {
		super("Add I2B2 User");
		setTitle("Add I2B2 User");
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
		username_answer.setText("");

		username_answer.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!username_answer.getText().isEmpty()
						&& !userPassword_answer.getText().isEmpty()) {
					AddUserPageOne.this.setPageComplete(true);
					AddUserPageOne.this.setErrorMessage(null);
				} else if (userPassword_answer.getText().isEmpty()) {
					AddUserPageOne.this.setErrorMessage("Missing Password!");
				} else {
					AddUserPageOne.this.setErrorMessage("Missing Username");
					AddUserPageOne.this.setPageComplete(false);
				}
			}
		});

		Label fullname = new Label(container1, SWT.FILL | SWT.CENTER);
		fullname.setText("Fullname");
		fullname_answer = new Text(container1, SWT.FILL);
		fullname_answer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));
		fullname_answer.setText("");

		Label email = new Label(container1, SWT.FILL | SWT.CENTER);
		email.setText("E-Mail");
		email_answer = new Text(container1, SWT.FILL);
		email_answer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));
		email_answer.setText("");
		new Label(container1, SWT.NONE);
		new Label(container1, SWT.NONE);
		label = new Label(container1, SWT.SEPARATOR | SWT.HORIZONTAL);
		new Label(container1, SWT.NONE);
		new Label(container1, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2,
				1));

		btnGeneratePassword = new Button(container1, SWT.NONE);
		btnGeneratePassword.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				String generatedPassword = getGeneratedPassword(Integer
						.parseInt(passwordLength.getText()));
				userPassword_answer.setText(generatedPassword);
				retypePassword_answer.setText(generatedPassword);
				if (!retypePassword_answer.getText().equals(
						userPassword_answer.getText())) {
					AddUserPageOne.this.setPageComplete(false);
					AddUserPageOne.this
							.setErrorMessage("Passwords do not match!");

				} else if (!username_answer.getText().isEmpty()
						&& !userPassword_answer.getText().isEmpty()
						&& retypePassword_answer.getText().equals(
								userPassword_answer.getText())) {
					AddUserPageOne.this.setPageComplete(true);
					AddUserPageOne.this.setErrorMessage(null);
				} else if (userPassword_answer.getText().isEmpty()) {
					AddUserPageOne.this.setPageComplete(false);
					AddUserPageOne.this.setErrorMessage("Missing Password!");
				} else if (username_answer.getText().isEmpty()) {
					AddUserPageOne.this.setPageComplete(false);
					AddUserPageOne.this.setErrorMessage("Missing Username");
				}
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
					AddUserPageOne.this.setPageComplete(false);
					AddUserPageOne.this
							.setErrorMessage("Passwords do not match!");

				} else if (!username_answer.getText().isEmpty()
						&& !userPassword_answer.getText().isEmpty()
						&& retypePassword_answer.getText().equals(
								userPassword_answer.getText())) {
					AddUserPageOne.this.setPageComplete(true);
					AddUserPageOne.this.setErrorMessage(null);
				} else if (userPassword_answer.getText().isEmpty()) {
					AddUserPageOne.this.setPageComplete(false);
					AddUserPageOne.this.setErrorMessage("Missing Password!");
				} else if (username_answer.getText().isEmpty()) {
					AddUserPageOne.this.setPageComplete(false);
					AddUserPageOne.this.setErrorMessage("Missing Username");
				}
			}
		});

		// });

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
		label_1 = new Label(container1, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				2, 1));
		new Label(container1, SWT.NONE);
		new Label(container1, SWT.NONE);
		lblSendEmail = new Label(container1, SWT.NONE);
		lblSendEmail.setText("Send E-Mail");

		checkEMailButton = new Button(container1, SWT.CHECK);

		retypePassword_answer.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!retypePassword_answer.getText().equals(
						userPassword_answer.getText())) {
					AddUserPageOne.this.setPageComplete(false);
					AddUserPageOne.this
							.setErrorMessage("Passwords do not match!");

				} else if (!username_answer.getText().isEmpty()
						&& !userPassword_answer.getText().isEmpty()
						&& retypePassword_answer.getText().equals(
								userPassword_answer.getText())) {
					AddUserPageOne.this.setPageComplete(true);
					AddUserPageOne.this.setErrorMessage(null);
				} else if (userPassword_answer.getText().isEmpty()) {
					AddUserPageOne.this.setPageComplete(false);
					AddUserPageOne.this.setErrorMessage("Missing Password!");
				} else if (username_answer.getText().isEmpty()) {
					AddUserPageOne.this.setPageComplete(false);
					AddUserPageOne.this.setErrorMessage("Missing Username");
				}
			}
		});
		setPageComplete(false);
		setErrorMessage("Missing Username");
	}
}
