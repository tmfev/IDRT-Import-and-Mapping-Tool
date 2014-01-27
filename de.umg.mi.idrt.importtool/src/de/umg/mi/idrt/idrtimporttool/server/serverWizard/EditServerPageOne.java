package de.umg.mi.idrt.idrtimporttool.server.serverWizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.umg.mi.idrt.idrtimporttool.importidrt.IDRTImport;
import de.umg.mi.idrt.idrtimporttool.messages.Messages;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class EditServerPageOne extends WizardPage {

	private static Text ipText;
	private static Text PortText;
	private static Text DBUserText;
	private static Text DBUserPasswordText;
	private static Text DBSIDText;
	private static Text uniqueIDText;
	private static Label DBTest;
	private static Composite container1;
	private static Combo DBTypeCombo;
	private static Label DBIntegratedSecurity;
	private static Button DBMSSQLUseWinAuth;
	private static Label DBUserPassword;
	private static Label DBUser;

	/**
	 * @return the dBSIDText
	 */
	public static String getDBSIDText() {
		return DBSIDText.getText();
	}

	/**
	 * @return the dBUserPasswordText
	 */
	public static String getDBUserPasswordText() {
		return DBUserPasswordText.getText();
	}

	/**
	 * @return the dBUserText
	 */
	public static String getDBUserText() {
		return DBUserText.getText();
	}

	/**
	 * @return the ipText
	 */
	public static String getIpText() {
		return ipText.getText();
	}

	/**
	 * @return the portText
	 */
	public static String getPortText() {
		return PortText.getText();
	}

	public static String getUniqueIDText() {
		return uniqueIDText.getText();
	}

	private Server server;
	private Label DBTypeLabel;

	public EditServerPageOne(Server server) {
		super(Messages.EditServerPageOne_ServerSetup);
		this.server = server;
		setTitle(Messages.EditServerPageOne_ServerSetup);
		setDescription(Messages.EditServerPageOne_EditServer);
	}

	// /**
	// * @return the dBSchemaText
	// */
	// public static String getDBSchemaText() {
	// return DBSchemaText.getText();
	// }

	@Override
	public void createControl(Composite parent) {
		container1 = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(2, true);
		container1.setLayout(layout);

		Label uniqueID = new Label(container1, SWT.FILL | SWT.CENTER);
		uniqueID.setText(Messages.EditServerPageOne_UniqueID);

		uniqueIDText = new Text(container1, SWT.FILL);
		uniqueIDText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));
		uniqueIDText.setText(server.getUniqueID());
		uniqueIDText.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!uniqueIDText.getText().isEmpty()) {
					EditServerPageOne.this.setPageComplete(true);
				} else {
					EditServerPageOne.this.setPageComplete(false);
				}
			}
		});

		Label ip = new Label(container1, SWT.FILL | SWT.CENTER);
		ip.setText(Messages.EditServerPageOne_DBHostname);
		// ipText = new Text(container, SWT.BORDER | SWT.SINGLE);
		ipText = new Text(container1, SWT.FILL);
		ipText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		ipText.setText(server.getIp());
		ipText.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!ipText.getText().isEmpty()) {
					EditServerPageOne.this.setPageComplete(true);
				}
			}
		});

		Label Port = new Label(container1, SWT.FILL | SWT.CENTER);
		Port.setText(Messages.EditServerPageOne_DBPort);
		// ipText = new Text(container, SWT.BORDER | SWT.SINGLE);
		PortText = new Text(container1, SWT.FILL);
		PortText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,
				1));
		PortText.setText(server.getPort());
		PortText.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!PortText.getText().isEmpty()) {
					EditServerPageOne.this.setPageComplete(true);
				}
			}
		});
		DBUser = new Label(container1, SWT.FILL | SWT.CENTER);
		DBUser.setText(Messages.EditServerPageOne_DBUser);
		// ipText = new Text(container, SWT.BORDER | SWT.SINGLE);
		DBUserText = new Text(container1, SWT.FILL);
		DBUserText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));
		DBUserText.setText(server.getUser());
		DBUserText.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!DBUserText.getText().isEmpty()) {
					EditServerPageOne.this.setPageComplete(true);
				}
			}
		});

		DBUserPassword = new Label(container1, SWT.FILL | SWT.CENTER);
		DBUserPassword.setText(Messages.EditServerPageOne_DBPassword);
		// ipText = new Text(container, SWT.BORDER | SWT.SINGLE);
		DBUserPasswordText = new Text(container1, SWT.FILL);

		DBUserPasswordText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));
		DBUserPasswordText.setText(server.getPassword());
		DBUserPasswordText.setEchoChar('*');
		DBUserPasswordText.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!DBUserPasswordText.getText().isEmpty()) {
					EditServerPageOne.this.setPageComplete(true);
				}
			}
		});

		Label DBSID = new Label(container1, SWT.FILL | SWT.CENTER);
		DBSID.setText(Messages.EditServerPageOne_DBSID);
		// ipText = new Text(container, SWT.BORDER | SWT.SINGLE);
		DBSIDText = new Text(container1, SWT.FILL);
		DBSIDText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));
		DBSIDText.setText(server.getSID());
		DBSIDText.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!DBSIDText.getText().isEmpty()) {
					EditServerPageOne.this.setPageComplete(true);
				}
			}
		});

		DBTypeLabel = new Label(container1, SWT.SHADOW_IN | SWT.CENTER);
		DBTypeLabel.setText(Messages.EditServerPageOne_DBType);

		DBTypeCombo = new Combo(container1, SWT.READ_ONLY);
		DBIntegratedSecurity = new Label(container1, SWT.SHADOW_IN | SWT.CENTER);
		DBMSSQLUseWinAuth = new Button(container1, SWT.CHECK);
		DBTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		DBTypeCombo.setItems(Server.getComboItems());
		for (int i = 0; i < DBTypeCombo.getItems().length; i++) {
			if (DBTypeCombo.getItem(i).equalsIgnoreCase(server.getDatabaseType())) {
				DBTypeCombo.select(i);
				if (server.getDatabaseType().equalsIgnoreCase("mssql")) { //$NON-NLS-1$
					DBIntegratedSecurity.setEnabled(true);
					DBMSSQLUseWinAuth.setEnabled(true);
					if(server.isUseWinAuth()) {
						DBMSSQLUseWinAuth.setSelection(true);
						DBUserPassword.setEnabled(false);
						DBUserPasswordText.setEnabled(false);
						DBUser.setEnabled(false);
						DBUserText.setEnabled(false);
					}

				}
				else {
					DBIntegratedSecurity.setEnabled(false);
					DBMSSQLUseWinAuth.setEnabled(false);
					DBMSSQLUseWinAuth.setSelection(false);
				}
				break;	
			}
		}
		DBTypeCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(DBTypeCombo.getItem(DBTypeCombo.getSelectionIndex()).equalsIgnoreCase("mssql")) { //$NON-NLS-1$
					DBIntegratedSecurity.setEnabled(true);
					DBMSSQLUseWinAuth.setEnabled(true);
					DBUserPassword.setEnabled(!DBMSSQLUseWinAuth.getSelection());
					DBUserPasswordText.setEnabled(!DBMSSQLUseWinAuth.getSelection());
					DBUser.setEnabled(!DBMSSQLUseWinAuth.getSelection());
					DBUserText.setEnabled(!DBMSSQLUseWinAuth.getSelection());
				}
				else {
					DBUserPassword.setEnabled(true);
					DBUserPasswordText.setEnabled(true);
					DBUser.setEnabled(true);
					DBUserText.setEnabled(true);
					DBIntegratedSecurity.setEnabled(false);
					DBMSSQLUseWinAuth.setEnabled(false);
					DBMSSQLUseWinAuth.setSelection(false);

				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		DBIntegratedSecurity.setText(Messages.EditServerPageOne_UseWinAuth);
		//		DBIntegratedSecurity.setEnabled(false);
		setControl(container1);


//		DBMSSQLUseWinAuth.setEnabled(false);
		DBMSSQLUseWinAuth.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				DBUserPassword.setEnabled(!DBMSSQLUseWinAuth.getSelection());
				DBUserPasswordText.setEnabled(!DBMSSQLUseWinAuth.getSelection());
				DBUser.setEnabled(!DBMSSQLUseWinAuth.getSelection());
				DBUserText.setEnabled(!DBMSSQLUseWinAuth.getSelection());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// Label DBSchema = new Label(container1, SWT.FILL|SWT.CENTER);
		// DBSchema.setText("DB Schema");
		// // ipText = new Text(container, SWT.BORDER | SWT.SINGLE);
		// DBSchemaText = new Text(container1, SWT.FILL);
		// DBSchemaText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
		// false, 1, 1));
		// DBSchemaText.setText("TODO SCHEMA");
		// DBSchemaText.addKeyListener(new KeyListener() {
		// public void keyPressed(KeyEvent e) {
		// }
		// public void keyReleased(KeyEvent e) {
		// if (!DBSchemaText.getText().isEmpty()) {
		// setPageComplete(true);
		// }
		// }
		// });

		Button buttonTestDB = new Button(container1, SWT.PUSH);
		buttonTestDB.setText(Messages.EditServerPageOne_TestDB);
		DBTest = new Label(container1, SWT.FILL | SWT.CENTER);
		DBTest.setText("?"); //$NON-NLS-1$
		// Button ButtonTestDB = new Button("Test DB connectivity");
		buttonTestDB.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				DBTest.setText(Messages.EditServerPageOne_TestDBConnActive);
				Color color_black = container1.getDisplay().getSystemColor(
						SWT.COLOR_BLACK);
				DBTest.setForeground(color_black);
				DBTest.pack();
				DBTest.redraw();
				DBTest.update();
				if (IDRTImport.testDB(ipText.getText(), PortText.getText(),
						DBUserText.getText(), DBUserPasswordText.getText(),
						DBSIDText.getText(),DBTypeCombo.getText(),DBMSSQLUseWinAuth.getSelection())) {
					DBTest.setText(Messages.EditServerPageOne_Success);
					Color color_red = container1.getDisplay().getSystemColor(
							SWT.COLOR_GREEN);
					DBTest.setForeground(color_red);
					DBTest.pack();
				} else {
					// RGB value = Color.red;
					DBTest.setText(Messages.EditServerPageOne_Failure);
					// org.eclipse.swt.graphics.Color red = new CSWT.COLOR_RED;
					Color color_red = container1.getDisplay().getSystemColor(
							SWT.COLOR_RED);
					DBTest.setForeground(color_red);
					DBTest.setToolTipText(IDRTImport.error);
					DBTest.pack();
				}
			}
		});

		setControl(container1);
		setPageComplete(true);
	}

	/**
	 * @return
	 */
	public static String getDBType() {
		return DBTypeCombo.getText();
	}
	public static boolean getCheckUseWinAuth() {
		return DBMSSQLUseWinAuth.getSelection();
	}
	// private static void setText(String text){
	// DBTest.setText(text);
	// DBTest.redraw();
	// DBTest.update();
	// }
}
