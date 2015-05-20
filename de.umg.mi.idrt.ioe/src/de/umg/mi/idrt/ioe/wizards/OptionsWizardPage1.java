package de.umg.mi.idrt.ioe.wizards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.umg.mi.idrt.importtool.misc.FileHandler;


/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class OptionsWizardPage1 extends WizardPage {
	private static Button showTotalNumButton;
	private static Text txtPIDURL;
	private static String pidURL;
	private static String exportLogPath;
	private static String sysoLogPath;
	private static Text txtLlogPath;
	private static Button checkSysoLog;
	private static Button checkFfilterProjects;
	private static Button hideTempTablesButton;
	private static Properties defaultProps;
	private static boolean changed = false;
	public OptionsWizardPage1() {
		super("Options");
		setTitle("Options");
		setDescription("Edit your Settings");
		System.out.println("OptionsWizardPage1@IOE"); 
	}
	public static String getSysoLogPath() {
		return sysoLogPath;
	}
	public static String getShowTotalnumButton() {
		return String.valueOf(showTotalNumButton.getSelection());
	}
	public static String getTxtPIDURL() {
		return txtPIDURL.getText();
	}
	private Text sysoLogLocationPath;

	private static Text guessRowtext;

	private static Text targetFolderText;

	private static String targetFolder;

	public static boolean getChanged() {
		return changed;
	}

	public static String getFilter() {
		return String.valueOf(checkFfilterProjects.getSelection());
	}

	public static String getGuessRowtext() {
		return guessRowtext.getText();
	}

	public static String getHideTempTablesButton() {
		return String.valueOf(hideTempTablesButton.getSelection());
	}

	public static String getSysoLog() {
		return String.valueOf(checkSysoLog.getSelection());
	}

	/**
	 * @return the targetFolderText
	 */
	public static String getTargetFolderText() {
		return targetFolderText.getText();
	}

	public static String getTxtLog() {
		return txtLlogPath.getText();
	}

	@Override
	public void createControl(Composite parent) {
		try {
			File properties = FileHandler.getBundleFile("/cfg/Default.properties");
			defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));
			pidURL = defaultProps.getProperty("PIDURL");
			exportLogPath = defaultProps.getProperty("log");
			targetFolder = defaultProps.getProperty("MDPDName");
			Composite composite = new Composite(parent, SWT.NONE);
			setControl(composite);
			composite.setLayout(new GridLayout(3, false));
			Label lblTargetFolder = new Label(composite, SWT.NONE);
			lblTargetFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
					false, 1, 1));
			lblTargetFolder.setText("Target Folder:");
			targetFolderText = new Text(composite, SWT.BORDER);
			targetFolderText.setText(targetFolder);
			targetFolderText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			targetFolderText.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {

				}

				@Override
				public void keyReleased(KeyEvent e) {
					if (targetFolderText.getText().isEmpty()) {
						setPageComplete(false);
						setErrorMessage("Target Folder cannot be empty!");
					}
					else {
						setPageComplete(true);
						setErrorMessage(null);
					}
				}
			});
			new Label(composite, SWT.NONE);
			Label label_6 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
			label_6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
					false, 2, 1));
			new Label(composite, SWT.NONE);

			Label lblPidgenUrl = new Label(composite, SWT.NONE);
			lblPidgenUrl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
					false, 1, 1));
			lblPidgenUrl.setText("PIDGen URL:");

			txtPIDURL = new Text(composite, SWT.BORDER);
			txtPIDURL.setText(pidURL);
			txtPIDURL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
					false, 1, 1));
			new Label(composite, SWT.NONE);
			Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
			label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
					false, 2, 1));
			new Label(composite, SWT.NONE);

			Label lblExportlog = new Label(composite, SWT.NONE);
			lblExportlog.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
					false, 1, 1));
			lblExportlog.setText("Export-Log Location:");

			txtLlogPath = new Text(composite, SWT.BORDER);
			txtLlogPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
					false, 1, 1));
			txtLlogPath.setText(exportLogPath);
			final FileDialog fd = new FileDialog(parent.getShell());
			fd.setText("ID File");
			fd.setFilterPath(defaultProps.getProperty("idFile"));
			fd.setFilterExtensions(new String[] { "*.log" });
			Button idButton = new Button(composite, SWT.PUSH);
			idButton.setText("...");
			idButton.setEnabled(true);
			idButton.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					exportLogPath = fd.open().replaceAll("\\\\", "/");
					if (!exportLogPath.endsWith(".log")) {
						exportLogPath += ".log";
					}
					txtLlogPath.setText(exportLogPath);
					changed = true;
				}
			});
			Label label_1 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
			label_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
					false, 2, 1));
			new Label(composite, SWT.NONE);
			Label lblSysolog = new Label(composite, SWT.NONE);
			lblSysolog.setText("Syso-Log:");
			lblSysolog.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
					false, 1, 1));
			boolean sysoLog = ((defaultProps.getProperty("sysoLog")
					.equals("true")) ? true : false);

			checkSysoLog = new Button(composite, SWT.CHECK);
			checkSysoLog.setSelection(sysoLog);
			checkSysoLog.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					changed = true;
				}
			});
			new Label(composite, SWT.NONE);

			Label lblSysologLocation = new Label(composite, SWT.NONE);
			lblSysologLocation.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
					false, false, 1, 1));
			lblSysologLocation.setText("Syso-Log Location:");

			sysoLogLocationPath = new Text(composite, SWT.BORDER);
			sysoLogLocationPath.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
					true, false, 1, 1));
			sysoLogPath = defaultProps.getProperty("sysoLogLoc");
			sysoLogLocationPath.setText(sysoLogPath);
			Button sysoLogButton = new Button(composite, SWT.NONE);
			sysoLogButton.setText("...");
			sysoLogButton.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					sysoLogPath = fd.open().replaceAll("\\\\", "/");
					if (!sysoLogPath.endsWith(".log")) {
						sysoLogPath += ".log";
					}
					sysoLogLocationPath.setText(sysoLogPath);
					changed = true;
				}
			});
			Label label_2 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
			label_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
					false, 2, 1));
			new Label(composite, SWT.NONE);
			Label lblFilterProjects = new Label(composite, SWT.NONE);
			lblFilterProjects.setText("Filter Projects:");
			lblFilterProjects.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
					false, false, 1, 1));
			boolean checkFilter = ((defaultProps.getProperty("filter")
					.equals("true")) ? true : false);
			checkFfilterProjects = new Button(composite, SWT.CHECK);
			checkFfilterProjects.setSelection(checkFilter);
			checkFfilterProjects.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
				}
			});
			new Label(composite, SWT.NONE);
			Label lblHideTemporaryTables = new Label(composite, SWT.NONE);
			lblHideTemporaryTables.setText("Hide Temporary/Secondary Tables:");

			boolean checkHideTemp = ((defaultProps.getProperty("hideTemp")
					.equals("true")) ? true : false);
			hideTempTablesButton = new Button(composite, SWT.CHECK);
			hideTempTablesButton.setSelection(checkHideTemp);

			new Label(composite, SWT.NONE);

			Label lblShowTotalNumber = new Label(composite, SWT.NONE);
			lblShowTotalNumber.setText("Show Total Number of Patients:");

			boolean checkShowTotalNum = ((defaultProps.getProperty("showTotalNum")
					.equals("true")) ? true : false);
			showTotalNumButton = new Button(composite, SWT.CHECK);
			showTotalNumButton.setSelection(checkShowTotalNum);
			new Label(composite, SWT.NONE);

			Label label_3 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
			label_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
					false, 2, 1));
			new Label(composite, SWT.NONE);

			Label label_4 = new Label(composite, SWT.NONE);
			label_4.setText("Guess-Rows (-1 = all):");
			label_4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
					false, 1, 1));

			guessRowtext = new Text(composite, SWT.BORDER);
			guessRowtext.setText(defaultProps.getProperty("guessRows"));
			guessRowtext.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false, 1, 1));
			new Label(composite, SWT.NONE);

			Label label_7 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
			GridData gd_label_7 = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
			gd_label_7.widthHint = 101;
			label_7.setLayoutData(gd_label_7);
			new Label(composite, SWT.NONE);
			Button btnClearChecks = new Button(composite, SWT.NONE);
			btnClearChecks.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					File properties = de.umg.mi.idrt.ioe.misc.FileHandler.getBundleFile("/cfg/Default.properties");
					try {
						defaultProps.load(new FileReader(properties));
						defaultProps.setProperty("loadProjectDontAskAgain", "false");
						defaultProps.store(new FileWriter(properties), "");
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			
			btnClearChecks.setText("Clear Checks");
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			setPageComplete(true);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
