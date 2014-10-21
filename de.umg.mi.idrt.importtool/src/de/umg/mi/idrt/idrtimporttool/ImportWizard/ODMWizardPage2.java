package de.umg.mi.idrt.idrtimporttool.ImportWizard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.umg.mi.idrt.idrtimporttool.messages.Messages;
import de.umg.mi.idrt.importtool.misc.FileHandler;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class ODMWizardPage2 extends WizardPage {

	private Composite container;
	private static Text folderODMText;
	private static Text folderMainText;
	private static String odmPath = ""; 
	private static String completeCodelist;
	// private static String mainPath = "";
	private static Button checkIncludePids;
	private static Button checkContext;
	private static Button checkTruncate;
	private static Button checkTruncateQueries;
	private static Button checkTerms;
	private static Button checkCleanUp;
	private static Button checkCompleteCodelists;
	
	private static Button btnIgnore;
	private static Button btnStop;
	private static Button btnDrop;
	private Label lblStopDatabaseIndexing;
	private Composite composite;
	private Label label;

	public static boolean getTruncateQueries() {
		return checkTruncateQueries.getSelection();
	}

	public static boolean getCleanUp() {
		return checkCleanUp.getSelection();
	}
	
	/**
	 * @return the completeCodelist
	 */
	public static boolean getCompleteCodelist() {
		return checkCompleteCodelists.getSelection();
	}

	/**
	 * @return the folderMainText
	 */
	public static String getFolderMainText() {
		return folderMainText.getText();
	}

	/**
	 * @return the folderText
	 */
	public static String getFolderODMText() {
		return folderODMText.getText();
	}

	/**
	 * @return the check
	 */
	public static boolean getIncludePids() {
		return checkIncludePids.getSelection();
	}

	/**
	 * @return the path
	 */
	public static String getPath() {
		return odmPath;
	}


	/**
	 * @return the checkContext
	 */
	public static boolean getSaveContext() {
		return checkContext.getSelection();
	}

	public static boolean getTerms() {
		return checkTerms.getSelection();
	}

	public static boolean getTruncate() {
		return checkTruncate.getSelection();
	}

	public ODMWizardPage2() {
		super(Messages.ODMWizardPageTwo_ODMImportSettings);
		setTitle(Messages.ODMWizardPageTwo_ODMImportSettings); 
		setDescription(Messages.ODMWizardPageTwo_ODMImportSettings); 
	}

	@Override
	public void createControl(Composite parent) {
		try {
			File properties = FileHandler.getBundleFile("/cfg/Default.properties");
			Properties defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));
			container = new Composite(parent, SWT.NULL);
			GridLayout layout = new GridLayout(3, false);
			container.setLayout(layout);

			Label truncateLabel = new Label(container, SWT.FILL | SWT.CENTER);
			truncateLabel.setText(Messages.ODMWizardPageTwo_TruncateProject);
			truncateLabel.setToolTipText(Messages.ODMWizardPageTwo_TruncateProjToolTip);
			checkTruncate = new Button(container, SWT.CHECK);
			checkTruncate.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
			checkTruncate.setSelection(false);

			new Label(container, SWT.NONE);
			
			label = new Label(container, SWT.SHADOW_IN | SWT.CENTER);
			label.setToolTipText("Truncates the Project!");
			label.setText("Truncate Previous Queries?");
			
			checkTruncateQueries = new Button(container, SWT.CHECK);
			checkTruncateQueries.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
			checkTruncateQueries.setSelection(false);
			new Label(container, SWT.NONE);
			lblStopDatabaseIndexing = new Label(container, SWT.SHADOW_IN | SWT.CENTER);
			lblStopDatabaseIndexing.setToolTipText("Truncates the Project!");
			lblStopDatabaseIndexing.setText(Messages.CSVWizardPage2_lblStopDatabaseIndexing_text);
			
			composite = new Composite(container, SWT.NONE);
			composite.setLayout(new FillLayout(SWT.HORIZONTAL));
			GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
			gd_composite.widthHint = 91;
			composite.setLayoutData(gd_composite);
			
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
//			Label cleanUplabel = new Label(container, SWT.SHADOW_IN | SWT.CENTER);
//			cleanUplabel.setText(Messages.ODMWizardPageTwo_CleanUp);

//			checkCleanUp = new Button(container, SWT.CHECK);
//			checkCleanUp.setSelection(Boolean.parseBoolean(defaultProps
//					.getProperty("cleanUp"))); 
//			new Label(container, SWT.NONE);

			Label folder = new Label(container, SWT.FILL | SWT.CENTER);
			folder.setText(Messages.ODMWizardPageTwo_ODMFolder);
			folder.setToolTipText(Messages.ODMWizardPageTwo_ODMFolderToolTip);

			odmPath = defaultProps.getProperty("folderODM");
			
			final DirectoryDialog dlg = new DirectoryDialog(parent.getShell());
			dlg.setText(Messages.ODMWizardPageTwo_ODMFolder); 
			dlg.setFilterPath(defaultProps.getProperty("folderODM")); 

			folderODMText = new Text(container, SWT.FILL);
			folderODMText.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true,
					false, 1, 1));
			folderODMText.setText(odmPath);
			folderODMText.setEditable(false);
			folderODMText.addKeyListener(new KeyListener() {
				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					if (!folderODMText.getText().isEmpty()) {
						ODMWizardPage2.this.setPageComplete(true);
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
					odmPath = dlg.open().replaceAll("\\\\", "/");  
					odmPath += "/"; 
					folderODMText.setText(odmPath);
				}
			});
			
			Label includeCodelists = new Label(container, SWT.NONE);
			includeCodelists.setText(Messages.ODMWizardPageTwo_ImportCodelists);
			
			completeCodelist = defaultProps.getProperty("importCodelist","false");
			
			checkCompleteCodelists = new Button(container, SWT.CHECK);
			checkCompleteCodelists.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
			checkCompleteCodelists.setSelection(Boolean.parseBoolean(completeCodelist));
			
			new Label(container, SWT.NONE);

			Label labelCheck = new Label(container, SWT.NONE);
			labelCheck.setText(Messages.ODMWizardPageTwo_IncludePid);
			checkIncludePids = new Button(container, SWT.CHECK);
			checkIncludePids.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
			checkIncludePids.setSelection(false);

			new Label(container, SWT.NONE);

			//TODO REIMPLEMENT
//			Label labelImportTerms = new Label(container, SWT.NONE);
//			labelImportTerms.setText(Messages.ODMWizardPageTwo_ImportSTandMap);
//			labelImportTerms
//					.setToolTipText(Messages.ODMWizardPageTwo_ImportSTandMapToolTip);
//			checkTerms = new Button(container, SWT.CHECK);
//			checkTerms.setSelection(false);
//			new Label(container, SWT.NONE);
			Label labelSaveContext = new Label(container, SWT.NONE);
			labelSaveContext.setText(Messages.ODMWizardPageTwo_SaveSettings);
			checkContext = new Button(container, SWT.CHECK);
			checkContext.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
			checkContext.setSelection(false);
			setControl(container);
			new Label(container, SWT.NONE);
			setPageComplete(true);
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
}
