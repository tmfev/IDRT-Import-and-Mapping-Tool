package de.umg.mi.idrt.ioe.wizards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.osgi.framework.Bundle;

import de.umg.mi.idrt.idrtimporttool.messages.Messages;
import de.umg.mi.idrt.ioe.Activator;

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

	private Label labelCleanUp;
	private static Button cleanUpBtn;

	// private static Text csvSeperatorext;


	public static boolean getCleanUp() {
		return cleanUpBtn.getSelection();
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

	public UploadProjectWizardPage2() {
		super("Settings");
		setTitle("Settings");
		setDescription("Please enter your settings.");
	}

	@Override
	public boolean canFlipToNextPage() {
		return true;
	}

	@Override
	public void createControl(final Composite parent) {
		try {
			Bundle bundle = Activator.getDefault().getBundle();
			Path path = new Path("/cfg/Default.properties"); 
			URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
			URL fileUrl = null;

			fileUrl = FileLocator.toFileURL(url);
			File properties = new File(fileUrl.getPath());
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

			labelCleanUp = new Label(container, SWT.SHADOW_IN | SWT.CENTER);
			labelCleanUp.setText(Messages.CSVWizardPageTwo_CleanUp);

			cleanUpBtn = new Button(container, SWT.CHECK);
			cleanUpBtn.setSelection(Boolean.parseBoolean(defaultProps
					.getProperty("cleanUp"))); 
			new Label(container, SWT.NONE);


			Label labelSaveContext = new Label(container, SWT.NONE);
			labelSaveContext.setText(Messages.CSVWizardPageTwo_SaveSettings);
			checkSaveSettings = new Button(container, SWT.CHECK);
			checkSaveSettings.setSelection(false);
		
			setControl(container);
			new Label(container, SWT.NONE);
			setPageComplete(true);
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
