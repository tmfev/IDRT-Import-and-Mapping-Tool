package de.umg.mi.idrt.idrtimporttool.ImportWizard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.jface.wizard.IWizardPage;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.umg.mi.idrt.idrtimporttool.messages.Messages;
import de.umg.mi.idrt.importtool.misc.FileHandler;
import de.umg.mi.idrt.importtool.views.ServerView;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class CSVWizardPage2 extends WizardPage {

	public static String getExternalIDFilePath() {
		return idText.getText();
	}
	private Composite container;
	private static Text folderCSVText;
	private static Text folderMainText; 
	private static String idPath = ""; 
	private static String CSVpath = ""; 
	private static String mainPath = "";
	private static Button checkSaveSettings;
	private static Button checkTruncate;
	private static Button checkTerms;
	private static Button btnUsepid;
	private static Button idButton;

	private static Label lblIdfile;
	private static Text quoteCharText;
	private static Text idText;
	private static Button btnRADIOIdfile;
	private static Button btnRADIOCsvfile;
	private Label lblDatePattern;
	private static Combo datePatternCombo;
	private Label labelCleanUp;

	// private static Text csvSeperatorext;

	private static Button cleanUpBtn;
	private Label label;
	private static Button checkTruncateQueries;

	public static boolean getBtnRADIOCsvfile() {
		return btnRADIOCsvfile.getSelection();
	}

	public static boolean getBtnRADIOIdfile() {
		return btnRADIOIdfile.getSelection();
	}

	public static boolean getCleanUp() {
		return cleanUpBtn.getSelection();
	}

	/**
	 * @return the folderText
	 */
	public static String getFolderCSVText() {
		return folderCSVText.getText();
	}

	/**
	 * @return the folderMainText
	 */
	public static String getFolderMainText() {
		return folderMainText.getText();
	}

	public static String getMainPath() {
		return mainPath;
	}

	/**
	 * @return the path
	 */
	public static String getPath() {
		return CSVpath;
	}

	public static String getPattern() {
		return datePatternCombo.getText();
	}

	public static String getQuoteCharText() {
		return quoteCharText.getText();
	}

	public static boolean getSaveContext() {
		return checkSaveSettings.getSelection();
	}

	public static boolean getTerms() {
		return checkTerms.getSelection();
	}

	public static boolean getTruncate() {
		return checkTruncate.getSelection();
	}
	
	public static boolean getTruncateQueries() {
		return checkTruncateQueries.getSelection();
	}

	/**
	 * use pidgenerator?
	 */
	public static boolean getUsePid() {
		return btnUsepid.getSelection();
	}

	public CSVWizardPage2() {
		super(Messages.CSVWizardPageTwo_CSVImportSettings);
		setTitle(Messages.CSVWizardPageTwo_CSVImportSettings);
		setDescription(Messages.CSVWizardPageTwo_CSVImportSettings);
	}
	
	

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
//	@Override
//	public boolean isPageComplete() {
//		return !getTargetFolderText().isEmpty();
//	}
//	
	@Override
	public boolean canFlipToNextPage() {
		return true;
	}

	@Override
	public void createControl(final Composite parent) {
		try {
			File properties = FileHandler.getBundleFile("/cfg/Default.properties");
			final Properties defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));

			container = new Composite(parent, SWT.NULL);
			GridLayout layout = new GridLayout(3, false);
			container.setLayout(layout);

			Label truncateLabel = new Label(container, SWT.FILL | SWT.CENTER);
			truncateLabel.setText(Messages.CSVWizardPageTwo_TruncateProject);
			truncateLabel.setToolTipText(Messages.CSVWizardPageTwo_TruncateProjectToolTip);

			checkTruncate = new Button(container, SWT.CHECK);
			checkTruncate.setSelection(false);

			new Label(container, SWT.NONE);
			
			label = new Label(container, SWT.SHADOW_IN | SWT.CENTER);
			label.setToolTipText("Truncates the Project!");
			label.setText("Truncate Previous Queries?");
			
			checkTruncateQueries = new Button(container, SWT.CHECK);
			checkTruncateQueries.setSelection(false);
			new Label(container, SWT.NONE);

			labelCleanUp = new Label(container, SWT.SHADOW_IN | SWT.CENTER);
			labelCleanUp.setText(Messages.CSVWizardPageTwo_CleanUp);

			cleanUpBtn = new Button(container, SWT.CHECK);
			cleanUpBtn.setSelection(Boolean.parseBoolean(defaultProps
					.getProperty("cleanUp"))); 
			new Label(container, SWT.NONE);

			Label folder = new Label(container, SWT.FILL | SWT.CENTER);
			folder.setText(Messages.CSVWizardPageTwo_CSVFolder);
			folder.setToolTipText(Messages.CSVWizardPageTwo_CSVFolderToolTip);

			CSVpath = defaultProps.getProperty("folderCSV"); 
			final DirectoryDialog dlg = new DirectoryDialog(parent.getShell());
			dlg.setText(Messages.CSVWizardPageTwo_CSVFolder);
			dlg.setFilterPath(defaultProps.getProperty("folderCSV")); 

			folderCSVText = new Text(container, SWT.FILL);
			folderCSVText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false, 1, 1));

			if (ServerView.getCsvPathSpecific() != null) {
				folderCSVText.setText(ServerView.getCsvPathSpecific());
			} else {
				folderCSVText.setText(CSVpath);
			}
			folderCSVText.setEditable(false);
			folderCSVText.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					if (!folderCSVText.getText().isEmpty()) {
						CSVWizardPage2.this.setPageComplete(true);
					}
				}
			});

			Button button = new Button(container, SWT.PUSH);
			button.setText("..."); 
			button.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					CSVpath = dlg.open().replaceAll("\\\\", "/");  //$NON-NLS-2$
					CSVpath += "/"; 
					folderCSVText.setText(CSVpath);
				}
			});

			Label lblUsePidgenerator = new Label(container, SWT.NONE);
			lblUsePidgenerator.setText(Messages.CSVWizardPageTwo_UsePidGen);

			btnUsepid = new Button(container, SWT.CHECK);
			btnUsepid.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					btnRADIOCsvfile.setEnabled(!btnRADIOCsvfile.getEnabled());
					btnRADIOIdfile.setEnabled(!btnRADIOIdfile.getEnabled());
					lblIdfile.setEnabled(btnRADIOIdfile.getSelection()
							&& btnUsepid.getSelection());
					idText.setEnabled(btnRADIOIdfile.getSelection()
							&& btnUsepid.getSelection());
					idButton.setEnabled(btnRADIOIdfile.getSelection()
							&& btnUsepid.getSelection());
				}
			});
			new Label(container, SWT.NONE);

			btnRADIOCsvfile = new Button(container, SWT.RADIO);
			btnRADIOCsvfile.setText(Messages.CSVWizardPageTwo_IDATandItemInCSV);
			btnRADIOCsvfile.setToolTipText(Messages.CSVWizardPageTwo_IDATInSameCSV);
			btnRADIOCsvfile.setEnabled(false);
			btnRADIOCsvfile.setSelection(true);
			btnRADIOCsvfile.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {

				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					lblIdfile.setEnabled(false);
					idText.setEnabled(false);
					idButton.setEnabled(false);
				}
			});
			btnRADIOIdfile = new Button(container, SWT.RADIO);
			btnRADIOIdfile.setText(Messages.CSVWizardPageTwo_IDATinExternalFile);
			btnRADIOIdfile.setToolTipText(Messages.CSVWizardPageTwo_IDATinExternalFile);
			btnRADIOIdfile.setEnabled(false);
			btnRADIOIdfile.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					lblIdfile.setEnabled(true);
					idText.setEnabled(true);
					idButton.setEnabled(true);
				}
			});
			new Label(container, SWT.NONE);

			lblIdfile = new Label(container, SWT.NONE);
			lblIdfile.setText(Messages.CSVWizardPageTwo_IDFile);
			lblIdfile.setEnabled(false);

			idText = new Text(container, SWT.NONE);
			idText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
					false, 1, 1));
			idText.setText(defaultProps.getProperty("idFile")); 
			idText.setEnabled(false);

			idButton = new Button(container, SWT.PUSH);
			idButton.setText("..."); 
			idButton.setEnabled(false);

			final FileDialog fd = new FileDialog(parent.getShell());
			fd.setText("ID File"); 
			fd.setFilterPath(defaultProps.getProperty("idFile")); 
			idButton.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					idPath = fd.open().replaceAll("\\\\", "/");  //$NON-NLS-2$
					idText.setText(idPath);
				}
			});

			lblDatePattern = new Label(container, SWT.NONE);
			lblDatePattern.setText(Messages.CSVWizardPageTwo_DatePattern);

			datePatternCombo = new Combo(container, SWT.NONE);

			datePatternCombo.setItems(new String[] { "yyyy-MM-dd", 
					"yyyy.MM.dd", "dd-MM-yyyy", "dd-MM-yy", "dd.MM.yyyy",  //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					"dd.MM.yy" }); 
			datePatternCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
					true, false, 1, 1));
			datePatternCombo.setText(defaultProps.getProperty("datePattern")); 
			new Label(container, SWT.NONE);

			Label labelQuoteChar = new Label(container, SWT.NONE);
			labelQuoteChar.setText(Messages.CSVWizardPageTwo_QuoteChar);
			labelQuoteChar.setToolTipText(Messages.CSVWizardPageTwo_QuoteCharToolTip);

			quoteCharText = new Text(container, SWT.FILL);
			quoteCharText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false, 1, 1));
			quoteCharText.setText(defaultProps.getProperty("quoteChar")); 
			quoteCharText.setEditable(true);

			new Label(container, SWT.NONE);
			
			Label labelImportTerms = new Label(container, SWT.NONE);
			labelImportTerms.setText(Messages.CSVWizardPageTwo_ImpAndMapST);
			labelImportTerms
					.setToolTipText(Messages.CSVWizardPageTwo_ImpAndMapSTToolTip);
			checkTerms = new Button(container, SWT.CHECK);
			checkTerms.setSelection(false);

			new Label(container, SWT.NONE);

			Label labelSaveContext = new Label(container, SWT.NONE);
			labelSaveContext.setText(Messages.CSVWizardPageTwo_SaveSettings);
			checkSaveSettings = new Button(container, SWT.CHECK);
			checkSaveSettings.setSelection(false);
		
			setControl(container);
			new Label(container, SWT.NONE);
			setPageComplete(false);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public IWizardPage getNextPage() {
		CSVImportWizard.setThree(new CSVWizardPage3());
		return CSVImportWizard.three;
	}
}
