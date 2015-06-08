package de.umg.mi.idrt.idrtimporttool.ImportWizard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.umg.mi.idrt.idrtimporttool.messages.Messages;
import de.umg.mi.idrt.importtool.misc.FileHandler;

import org.eclipse.swt.layout.FillLayout;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class BiobankWizardPage2 extends WizardPage {

	public static String getExternalIDFilePath() {
		return idText.getText();
	}
	private Composite container;
	private static Text folderMainText; 
	private static String mainPath = "";
	private static Button checkSaveSettings;
	private static Button checkTruncate;
	private static Text idText;
	private Label labelSetTotalNum;

	// private static Text csvSeperatorext;

	private static Button setTotalNumBtn;
	private Label label;
	private static Button checkTruncateQueries;
	private Label lblStopDatabaseIndexing;
	private static Button btnIgnore;
	private static Button btnStop;
	private static Button btnDrop;
	
	private Composite composite;
	private Label lblLanguage;
	private static Text languageText;
	private Label lblRasclientid;
	private Label lblRasprojectno;
	private static Text rasclientidText;
	private static Text rasprojectnoText;


	public static boolean getCleanUp() {
		return setTotalNumBtn.getSelection();
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



	public static boolean getSaveContext() {
		return checkSaveSettings.getSelection();
	}


	public static boolean getTruncate() {
		return checkTruncate.getSelection();
	}
	
	public static boolean getTruncateQueries() {
		return checkTruncateQueries.getSelection();
	}


	public BiobankWizardPage2() {
		super("StarLIMS Setup");
		setTitle("StarLIMS Setup");
//		setDescription("Please enter your StarLIMS database connection data");
	}
	
	

	@Override
	public boolean isPageComplete() {
		return !languageText.getText().equals("")&& !rasclientidText.getText().equals("") && !rasprojectnoText.getText().equals("");
	}
//	
	@Override
	public boolean canFlipToNextPage() {
		return false;
	}

	@Override
	public void createControl(final Composite parent) {
		try {
			System.out.println("CSV super: " + super.getShell().getSize());
			File properties = FileHandler.getBundleFile("/cfg/Default.properties");
			final Properties defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));

			container = new Composite(parent, SWT.NULL);
			GridLayout layout = new GridLayout(3, false);
			container.setLayout(layout);
			
			lblLanguage = new Label(container, SWT.FILL | SWT.CENTER);
			lblLanguage.setText("Language");
			
			languageText = new Text(container, SWT.BORDER);
			languageText.setText("ENG");
			languageText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			new Label(container, SWT.NONE);
			
			lblRasclientid = new Label(container, SWT.NONE);
			lblRasclientid.setText("Rasclientid");
			
			rasclientidText = new Text(container, SWT.BORDER);
			rasclientidText.setText("Internal");
			rasclientidText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			new Label(container, SWT.NONE);
			
			lblRasprojectno = new Label(container, SWT.NONE);
			lblRasprojectno.setText("Rasprojectno");
			
			rasprojectnoText = new Text(container, SWT.BORDER);
			rasprojectnoText.setText("GCKD-Studie");
			rasprojectnoText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			new Label(container, SWT.NONE);
			Label truncateLabel = new Label(container, SWT.FILL | SWT.CENTER);
			truncateLabel.setText(Messages.CSVWizardPageTwo_TruncateProject);
			truncateLabel.setToolTipText(Messages.CSVWizardPageTwo_TruncateProjectToolTip);
			System.out.println(container.getSize());
			checkTruncate = new Button(container, SWT.CHECK);
			checkTruncate.setSelection(false);

			new Label(container, SWT.NONE);
			
			label = new Label(container, SWT.SHADOW_IN | SWT.CENTER);
			label.setToolTipText("Truncates the Project!");
			label.setText("Truncate Previous Queries?");
			
			checkTruncateQueries = new Button(container, SWT.CHECK);
			checkTruncateQueries.setSelection(false);
			new Label(container, SWT.NONE);
			
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
			new Label(container, SWT.NONE);

			//TODO REIMPLEMENT
			labelSetTotalNum = new Label(container, SWT.SHADOW_IN | SWT.CENTER);
			labelSetTotalNum.setText(Messages.CSVWizardPageTwo_CleanUp);

			setTotalNumBtn = new Button(container, SWT.CHECK);
			setTotalNumBtn.setSelection(Boolean.parseBoolean(defaultProps
					.getProperty("cleanUp"))); 
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


	public static boolean getDropIndex(){
		return btnDrop.getSelection();
	}
	public static boolean getStopIndex(){
		return btnStop.getSelection();
	}

	public static String getLanguage() {
		return languageText.getText();
	}

	public static String getRasprojectno() {
		return rasprojectnoText.getText();
	}

	public static String getRasclientid() {
		return rasclientidText.getText();
	}
}
