package de.umg.mi.idrt.ioe.wizards;

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
import de.umg.mi.idrt.idrtimporttool.messages.Messages;
import de.umg.mi.idrt.importtool.misc.FileHandler;
import org.eclipse.swt.layout.FillLayout;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class UploadProjectWizardPage2 extends WizardPage {

	private Composite container;
	private static Button checkSaveSettings;
	private static Button checkTruncate;
	private static Button checkTerms;
	private Label labelSetTotalNum;
	private static Button setTotalNumBtn;
	// private static Text csvSeperatorext;

	private Label label;
	private static Button checkTruncateQueries;
	private Label lblStopDatabaseIndexing;
	private static Button btnIgnore;
	private static Button btnStop;
	private static Button btnDrop;
	
	public UploadProjectWizardPage2() {
		super("Upload Project");
		setTitle("Upload Project");
		setDescription("Please enter your settings.");
	}

	public static boolean getDropIndex(){
		return btnDrop.getSelection();
	}

	public static boolean getSaveContext() {
		return checkSaveSettings.getSelection();
	}

	public static boolean getStopIndex(){
		return btnStop.getSelection();
	}

	public static boolean getTerms() {
		return checkTerms.getSelection();
	}
	
	public static boolean getTruncate() {
		return checkTruncate.getSelection();
	}
	public static boolean getCleanUp() {
		return setTotalNumBtn.getSelection();
	}
	public static boolean getTruncateQueries() {
		return checkTruncateQueries.getSelection();
	}

	private Composite composite;

	@Override
	public boolean canFlipToNextPage() {
		return true;
	}
	@Override
	public void createControl(final Composite parent) {
		try{
		File properties = FileHandler.getBundleFile("/cfg/Default.properties");
		final Properties defaultProps = new Properties();
		defaultProps.load(new FileReader(properties));

		container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

		Label truncateLabel = new Label(container, SWT.FILL | SWT.CENTER);
		truncateLabel.setText(Messages.CSVWizardPageTwo_TruncateProject);
		truncateLabel.setToolTipText(Messages.CSVWizardPageTwo_TruncateProjectToolTip);

		checkTruncate = new Button(container, SWT.CHECK);
		checkTruncate.setSelection(false);
		
		label = new Label(container, SWT.SHADOW_IN | SWT.CENTER);
		label.setToolTipText("Truncates the Project!");
		label.setText("Truncate Previous Queries?");
		
		checkTruncateQueries = new Button(container, SWT.CHECK);
		checkTruncateQueries.setSelection(false);
		labelSetTotalNum = new Label(container, SWT.SHADOW_IN | SWT.CENTER);
		labelSetTotalNum.setText(Messages.CSVWizardPageTwo_CleanUp);

		setTotalNumBtn = new Button(container, SWT.CHECK);
		setTotalNumBtn.setSelection(Boolean.parseBoolean(defaultProps
				.getProperty("cleanUp"))); 
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
		return super.getNextPage();
	}
}
