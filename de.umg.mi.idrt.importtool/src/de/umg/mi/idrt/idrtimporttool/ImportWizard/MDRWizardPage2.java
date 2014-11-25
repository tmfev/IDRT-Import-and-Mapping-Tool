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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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

import org.eclipse.swt.layout.FillLayout;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class MDRWizardPage2 extends WizardPage {

	private static boolean canFinish = false;
	private Composite container;
	private static Text folderMainText; 
	private static String idPath = ""; 
	private static String CSVpath = ""; 
	private static String mainPath = "";
	private static Button checkSaveSettings;
	private static Button checkTruncate;
	private static Button checkTerms;
	private static Text MDRStartText;
	private Label label;
	private static Button checkTruncateQueries;
	private Label lblStopDatabaseIndexing;
	private static Button btnIgnore;
	private static Button btnStop;
	private static Button btnDrop;

	private Composite composite;

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


	public static String getQuoteCharText() {
		return MDRStartText.getText();
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

	public MDRWizardPage2() {
		super("MDR Import");
		setTitle("MDR Import");
//		setDescription("MDR Import");
	}



	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	//	@Override
	//	public boolean isPageComplete() {
	//		return !getTargetFolderText().isEmpty();
	//	}
	//	


	public static boolean canFinish(){
		return canFinish;
	}


	@Override
	public boolean canFlipToNextPage() {
		return true;
	}

	@Override
	public void createControl(final Composite parent) {
		try {
			System.out.println("CSV super: " + super.getShell().getSize());
			setErrorMessage("MDR Start Designation is empty");
			File properties = FileHandler.getBundleFile("/cfg/Default.properties");
			final Properties defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));

			container = new Composite(parent, SWT.NULL);
			GridLayout layout = new GridLayout(2, false);
			container.setLayout(layout);

			Label labelMDRStart = new Label(container, SWT.NONE);
			labelMDRStart.setText("MDR Start Designation:");
			labelMDRStart.setToolTipText("MDR Start Designation:");
			
			MDRStartText = new Text(container, SWT.FILL);
			MDRStartText.setText("27678");
			MDRStartText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false, 1, 1));
			canFinish=true;
			MDRStartText.setEditable(true);
			MDRStartText.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					
					int start;
					try{
						start = Integer.parseInt(MDRStartText.getText());
						if (start>0){
							setErrorMessage(null);
							canFinish=true;

						}
						else {
							canFinish=false;
							setErrorMessage("MDR Start Designation must be positive!");
						}
					}				
					catch (Exception e2) {
						canFinish=false;
						if (MDRStartText.getText().isEmpty())
							setErrorMessage("MDR Start Designation is empty");
						else
						setErrorMessage("MDR Start Designation is not a number!");

					}
					
					getWizard().getContainer().updateButtons();
				}
			});
			Label truncateLabel = new Label(container, SWT.FILL | SWT.CENTER);
			truncateLabel.setText(Messages.CSVWizardPageTwo_TruncateProject);
			truncateLabel.setToolTipText(Messages.CSVWizardPageTwo_TruncateProjectToolTip);
			System.out.println(container.getSize());
			checkTruncate = new Button(container, SWT.CHECK);
			checkTruncate.setSelection(false);

			label = new Label(container, SWT.SHADOW_IN | SWT.CENTER);
			label.setToolTipText("Truncates the Project!");
			label.setText("Truncate Previous Queries?");

			checkTruncateQueries = new Button(container, SWT.CHECK);
			checkTruncateQueries.setSelection(false);

			lblStopDatabaseIndexing = new Label(container, SWT.SHADOW_IN | SWT.CENTER);
			lblStopDatabaseIndexing.setToolTipText("Handles the database indexes");
			lblStopDatabaseIndexing.setText(Messages.CSVWizardPage2_lblStopDatabaseIndexing_text);

			composite = new Composite(container, SWT.NONE);
			composite.setLayout(new FillLayout(SWT.HORIZONTAL));
			//			gd_composite.widthHint = 91;
			composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

			boolean indexStop = Boolean.parseBoolean(defaultProps.getProperty("IndexStop","false"));
			boolean indexDrop = Boolean.parseBoolean(defaultProps.getProperty("IndexDrop","false"));

			btnIgnore = new Button(composite, SWT.RADIO);
			btnIgnore.setText(Messages.CSVWizardPage2_btnIgnore_text);
			btnIgnore.setSelection(!(indexStop||indexDrop));
			btnStop = new Button(composite, SWT.RADIO);
			btnStop.setText(Messages.CSVWizardPage2_btnStop_text);
			btnStop.setSelection(indexStop);
			btnDrop = new Button(composite, SWT.RADIO);
			btnDrop.setText(Messages.CSVWizardPage2_btnDrop_text);
			btnDrop.setSelection(indexDrop);

			CSVpath = defaultProps.getProperty("folderCSV"); 
			//TODO REIMPLEMENT
			//			Label labelImportTerms = new Label(container, SWT.NONE);
			//			labelImportTerms.setText(Messages.CSVWizardPageTwo_ImpAndMapST);
			//			labelImportTerms
			//					.setToolTipText(Messages.CSVWizardPageTwo_ImpAndMapSTToolTip);
			//			checkTerms = new Button(container, SWT.CHECK);
			//			checkTerms.setSelection(false);
			//
			//			new Label(container, SWT.NONE);

			Label labelSaveContext = new Label(container, SWT.NONE);
			labelSaveContext.setText(Messages.CSVWizardPageTwo_SaveSettings);
			checkSaveSettings = new Button(container, SWT.CHECK);
			checkSaveSettings.setSelection(false);

			setControl(container);
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

	public static boolean getDropIndex(){
		return btnDrop.getSelection();
	}
	public static boolean getStopIndex(){
		return btnStop.getSelection();
	}

	public static int getMDRInstance() {
		return Integer.parseInt(MDRStartText.getText());
	}
}
