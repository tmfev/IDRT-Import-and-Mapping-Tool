package de.umg.mi.idrt.admin.wizard;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class PasswordDialog extends Dialog {
	private Text passwordField;
	private String passwordString;
	private Button btnSavePassword;
	private boolean savePassword;

	/**
	 * @return the btnSavePassword
	 */
	public Button getBtnSavePassword() {
		return btnSavePassword;
	}

	/**
	 * @param btnSavePassword the btnSavePassword to set
	 */
	public void setBtnSavePassword(Button btnSavePassword) {
		this.btnSavePassword = btnSavePassword;
	}

	public PasswordDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void cancelPressed()
	{
		savePassword=btnSavePassword.getSelection();
		passwordField.setText("");
		super.cancelPressed();
	}

	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText("Please enter the DB Password");
		newShell.setSize(300, 150);
		//        newShell.set
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite comp = (Composite) super.createDialogArea(parent);

		GridLayout layout = (GridLayout) comp.getLayout();
		layout.numColumns = 2;

		Label passwordLabel = new Label(comp, SWT.RIGHT);
		passwordLabel.setText("Password: ");
		passwordField = new Text(comp, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
		passwordField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		new Label(comp, SWT.NONE);

		btnSavePassword = new Button(comp, SWT.CHECK);
		btnSavePassword.setText("Save Password");

		return comp;
	}

	public String getPassword()
	{
		return passwordString;
	}

	@Override
	protected void okPressed()
	{
		savePassword=btnSavePassword.getSelection();
		passwordString = passwordField.getText();
		
		super.okPressed();
	}
	/**
	 * 
	 */
	 public boolean getSavePassword() {
		 return savePassword;
	 }
}