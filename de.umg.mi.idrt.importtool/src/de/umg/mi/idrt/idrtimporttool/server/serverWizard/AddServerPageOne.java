package de.umg.mi.idrt.idrtimporttool.server.serverWizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;

import de.umg.mi.idrt.idrtimporttool.importidrt.IDRTImport;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;

import org.eclipse.swt.widgets.Combo;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class AddServerPageOne extends WizardPage {

	private static Text ipText;
	private static Text PortText;
	private static Text DBUserText;
	private static Text DBUserPasswordText;
	private static Label DBUserPassword;
	private static Text DBSIDText;
	private static Text DBSchemaText;
	private static Text uniqueIDText;
	private static ToolTip tip;
	private static Label DBUser;
	private static Label DBTest;
	private static Composite container1;
	private Label DBTypeLabel;
	private static Combo DBTypeCombo;
	private static Label DBIntegratedSecurity;
	private static Button DBMSSQLUseWinAuth;

	/**
	 * @return the dBSchemaText
	 */
	public static String getDBSchemaText() {
		return DBSchemaText.getText();
	}

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

	/**
	 * @return the uniqueIDText
	 */
	public static String getUniqueIDText() {
		return uniqueIDText.getText();
	}

	public AddServerPageOne() {
		super("Server Setup");
		setTitle("Server Setup");
		setDescription("Edit your server");
	}

	@Override
	public void createControl(Composite parent) {
		container1 = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(2, true);
		container1.setLayout(layout);

		Label uniqueID = new Label(container1, SWT.FILL | SWT.CENTER);
		uniqueID.setText("Unique Identifier");

		uniqueIDText = new Text(container1, SWT.FILL);
		tip = new ToolTip(uniqueIDText.getShell(), SWT.BALLOON);
		uniqueIDText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));
		// uniqueIDText.setText("");
		uniqueIDText.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!uniqueIDText.getText().isEmpty()
						&& !ServerList.getTargetServers().containsKey(
								uniqueIDText.getText())
								&& !ServerList.getSourceServers().containsKey(
										uniqueIDText.getText())) {
					AddServerPageOne.this.setPageComplete(true);
				} else {
					AddServerPageOne.this.setPageComplete(false);
					if (ServerList.getTargetServers().containsKey(
							uniqueIDText.getText())) {
						uniqueIDText
						.setToolTipText("Unique Server ID already exists!");

						tip.setMessage("Unique Server ID already exists!");
						Point loc = uniqueIDText.toDisplay(uniqueIDText
								.getLocation());
						// tip.setLocation(loc.x + uniqueIDText.getSize().x -
						// uniqueIDText.getBorderWidth(), loc.y);
						tip.setLocation(loc);
						tip.setVisible(true);
						uniqueIDText.redraw();
					} else if (uniqueIDText.getText().isEmpty()) {
						uniqueIDText.setToolTipText("Unique Server ID empty!");
						tip.setMessage("Unique Server ID empty!");
						Point loc = uniqueIDText.toDisplay(uniqueIDText
								.getLocation());
						// tip.setLocation(loc.x + uniqueIDText.getSize().x -
						// uniqueIDText.getBorderWidth(), loc.y);
						tip.setLocation(loc);
						tip.setVisible(true);
						uniqueIDText.redraw();
					} else {
						tip.setVisible(false);
						uniqueIDText.redraw();
					}
				}
			}
		});

		uniqueIDText.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if (ServerList.getTargetServers().containsKey(uniqueIDText.getText())) {
					uniqueIDText
					.setToolTipText("Unique Server ID already exists!");
					tip.setMessage("Unique Server ID already exists!");
					Point loc = uniqueIDText.toDisplay(uniqueIDText
							.getLocation());
					tip.setLocation(loc);
					tip.setVisible(true);
					uniqueIDText.redraw();
				} else if (uniqueIDText.getText().isEmpty()) {
					uniqueIDText.setToolTipText("Unique Server ID empty!");
					tip.setMessage("Unique Server ID empty!");
					Point loc = uniqueIDText.toDisplay(uniqueIDText
							.getLocation());
					tip.setLocation(loc);
					tip.setVisible(true);
					uniqueIDText.redraw();
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				tip.setVisible(false);
			}
		});

		Label ip = new Label(container1, SWT.FILL | SWT.CENTER);
		ip.setText("IP");
		// ipText = new Text(container, SWT.BORDER | SWT.SINGLE);
		ipText = new Text(container1, SWT.FILL);
		ipText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		ipText.setText("");
		// ipText.addKeyListener(new KeyListener() {
		// public void keyPressed(KeyEvent e) {
		// }
		// public void keyReleased(KeyEvent e) {
		// if (!ipText.getText().isEmpty()) {
		// setPageComplete(true);
		// }
		// }
		// });

		Label Port = new Label(container1, SWT.FILL | SWT.CENTER);
		Port.setText("Port");
		// ipText = new Text(container, SWT.BORDER | SWT.SINGLE);
		PortText = new Text(container1, SWT.FILL);
		PortText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,
				1));
		PortText.setText("");
		// PortText.addKeyListener(new KeyListener() {
		// public void keyPressed(KeyEvent e) {
		// }
		// public void keyReleased(KeyEvent e) {
		// if (!PortText.getText().isEmpty()) {
		// setPageComplete(true);
		// }
		// }
		// });
		DBUser = new Label(container1, SWT.FILL | SWT.CENTER);
		DBUser.setText("DB Username");
		// ipText = new Text(container, SWT.BORDER | SWT.SINGLE);
		DBUserText = new Text(container1, SWT.FILL);
		DBUserText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));
		DBUserText.setText("");
		// DBUserText.addKeyListener(new KeyListener() {
		// public void keyPressed(KeyEvent e) {
		// }
		// public void keyReleased(KeyEvent e) {
		// if (!DBUserText.getText().isEmpty()) {
		// setPageComplete(true);
		// }
		// }
		// });

		DBUserPassword = new Label(container1, SWT.FILL | SWT.CENTER);
		DBUserPassword.setText("DB Password");
		// ipText = new Text(container, SWT.BORDER | SWT.SINGLE);
		DBUserPasswordText = new Text(container1, SWT.FILL);

		DBUserPasswordText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));
		DBUserPasswordText.setText("");
		DBUserPasswordText.setEchoChar('*');
		// DBUserPasswordText.addKeyListener(new KeyListener() {
		// public void keyPressed(KeyEvent e) {
		// }
		// public void keyReleased(KeyEvent e) {
		// if (!DBUserPasswordText.getText().isEmpty()) {
		// setPageComplete(true);
		// }
		// }
		// });

		Label DBSID = new Label(container1, SWT.FILL | SWT.CENTER);
		DBSID.setText("DB SID");
		// ipText = new Text(container, SWT.BORDER | SWT.SINGLE);
		DBSIDText = new Text(container1, SWT.FILL);
		DBSIDText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));
		DBSIDText.setText("");

		DBTypeLabel = new Label(container1, SWT.SHADOW_IN | SWT.CENTER);
		DBTypeLabel.setText("DB Type");

		DBTypeCombo = new Combo(container1, SWT.READ_ONLY);
		DBTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		DBTypeCombo.setItems(Server.getComboItems());
		DBTypeCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println();
				if(DBTypeCombo.getItem(DBTypeCombo.getSelectionIndex()).equalsIgnoreCase("mssql")) {
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
		DBIntegratedSecurity = new Label(container1, SWT.SHADOW_IN | SWT.CENTER);
		DBIntegratedSecurity.setText("Use Windows Authentication?");
		DBIntegratedSecurity.setEnabled(false);
		setControl(container1);

		DBMSSQLUseWinAuth = new Button(container1, SWT.CHECK);
		DBMSSQLUseWinAuth.setEnabled(false);
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

		Button buttonTestDB = new Button(container1, SWT.PUSH);
		buttonTestDB.setText("Test DB connectivity");

		// Button ButtonTestDB = new Button("Test DB connectivity");
		buttonTestDB.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {

				DBTest.setText("Testing DB Connection...");
				Color color_black = container1.getDisplay().getSystemColor(
						SWT.COLOR_BLACK);
				DBTest.setForeground(color_black);
				DBTest.pack();
				DBTest.redraw();
				DBTest.update();

				if (IDRTImport.testDB(ipText.getText(), PortText.getText(),
						DBUserText.getText(), DBUserPasswordText.getText(),
						DBSIDText.getText(),DBTypeCombo.getText(),DBMSSQLUseWinAuth.getSelection())) {
					System.out.println("working");

					DBTest.setText("Success");
					Color color_red = container1.getDisplay().getSystemColor(
							SWT.COLOR_GREEN);
					DBTest.setForeground(color_red);
					DBTest.pack();
				} else {
					DBTest.setText("Failure");
					Color color_red = container1.getDisplay().getSystemColor(
							SWT.COLOR_RED);
					DBTest.setForeground(color_red);
					DBTest.setToolTipText(IDRTImport.error);
					DBTest.pack();
					System.out.println("broken");
					System.out.println(IDRTImport.error);
				}
			}
		});
		DBTest = new Label(container1, SWT.FILL | SWT.CENTER);
		DBTest.setText("?");
		setPageComplete(false);
	}

	/**
	 * @return the dBTypeCombo
	 */
	public static String getDBType() {
		return DBTypeCombo.getText();
	}

	public static boolean getCheckUseWinAuth() {
		return DBMSSQLUseWinAuth.getSelection();
	}

}
