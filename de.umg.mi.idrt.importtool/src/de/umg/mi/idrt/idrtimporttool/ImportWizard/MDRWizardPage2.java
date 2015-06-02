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
	private static Button checkTerms;
	private static Text MDRDesignation;
	private static Text MDRBaseURL;
	private static Text MDRInstance;
	
	private String MDRDESIGNATION = "27678";
	private String MDRBASEURL = "https://mdr.imise.uni-leipzig.de";
	private String MDRINSTANCE = "IDRT";

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


//	public static String getQuoteCharText() {
//		return MDRStartText.getText();
//	}

	public static boolean getTerms() {
		return checkTerms.getSelection();
	}

	public MDRWizardPage2() {
		super("Import from MDR");
		setTitle("Import from MDR");
//		setDescription("MDR Import");
	}


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
			
			MDRDesignation = new Text(container, SWT.FILL);
			MDRDesignation.setText(MDRDESIGNATION);
			MDRDesignation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false, 1, 1));
			MDRDesignation.setEditable(true);
			MDRDesignation.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					checkFinish();
				}
			});

			Label labelMDRBaseURL = new Label(container, SWT.NONE);
			labelMDRBaseURL.setText("MDR BaseURL:");
			labelMDRBaseURL.setToolTipText("MDR BaseURL:");
			
			MDRBaseURL = new Text(container, SWT.FILL);
			MDRBaseURL.setText(MDRBASEURL);
			MDRBaseURL.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false, 1, 1));
			MDRBaseURL.setEditable(true);
			MDRBaseURL.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					checkFinish();
				}
			});
			
			Label labelMDRInstance = new Label(container, SWT.NONE);
			labelMDRInstance.setText("MDR Instance:");
			labelMDRInstance.setToolTipText("MDR BaseURL:");
			
			MDRInstance = new Text(container, SWT.FILL);
			MDRInstance.setText(MDRINSTANCE);
			MDRInstance.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false, 1, 1));
			MDRInstance.setEditable(true);
			MDRInstance.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					checkFinish();
				}
			});
			
			checkFinish();
			System.out.println(container.getSize());

//			boolean indexStop = Boolean.parseBoolean(defaultProps.getProperty("IndexStop","false"));
//			boolean indexDrop = Boolean.parseBoolean(defaultProps.getProperty("IndexDrop","false"));

			CSVpath = defaultProps.getProperty("folderCSV");

			setControl(container);
			setPageComplete(false);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private void checkFinish(){
		try{
			int start;
			start = Integer.parseInt(MDRDesignation.getText());
			if (start>0 && !getMDRBaseURL().isEmpty() && !getMDRInstance().isEmpty()){
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
			if (MDRDesignation.getText().isEmpty())
				setErrorMessage("MDR Start Designation is empty");
			else
			setErrorMessage("MDR Start Designation is not a number!");
		}
		getWizard().getContainer().updateButtons();
	}

	@Override
	public IWizardPage getNextPage() {
		CSVImportWizard.setThree(new CSVWizardPage3());
		return CSVImportWizard.three;
	}

	public static int getMDRDesignation() {
		return Integer.parseInt(MDRDesignation.getText());
	}

	public static String getMDRInstance() {
		return MDRInstance.getText();
	}

	public static String getMDRBaseURL() {
		return MDRBaseURL.getText();
	}
}
