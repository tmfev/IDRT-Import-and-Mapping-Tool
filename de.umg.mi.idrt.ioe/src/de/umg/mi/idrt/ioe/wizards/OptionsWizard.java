package de.umg.mi.idrt.ioe.wizards;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import de.umg.mi.idrt.importtool.misc.FileHandler;
import de.umg.mi.idrt.importtool.views.ServerView;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class OptionsWizard extends Wizard {

	public OptionsWizard() {
		super();
		System.out.println("CONSTRUCTOR");
		setNeedsProgressMonitor(true);
	}
	private Properties defaultProps;

	protected OptionsWizardPage1 one;

	@Override
	public void addPages() {
		one = new OptionsWizardPage1();
		addPage(one);
	}

	@Override
	public boolean performFinish() {
		try {
			File properties = FileHandler.getBundleFile("/cfg/Default.properties");
			defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));

			defaultProps.setProperty("PIDURL",
					OptionsWizardPage1.getTxtPIDURL());
			defaultProps.setProperty("log", OptionsWizardPage1.getTxtLog());
			defaultProps.setProperty("sysoLog",
					OptionsWizardPage1.getSysoLog());
			defaultProps
					.setProperty("filter", OptionsWizardPage1.getFilter());
			defaultProps.setProperty("sysoLogLoc",
					OptionsWizardPage1.getSysoLogPath());
			defaultProps.setProperty("guessRows",
					OptionsWizardPage1.getGuessRowtext());
			defaultProps.setProperty("hideTemp",
					OptionsWizardPage1.getHideTempTablesButton());
			defaultProps.setProperty("MDPDName",
					OptionsWizardPage1.getTargetFolderText());
			defaultProps.store(new FileWriter(properties), "");
			boolean restart = false;
			if (OptionsWizardPage1.getChanged()) {
				restart = MessageDialog
						.openConfirm(Display.getDefault().getActiveShell(),
								"Restart required",
								"You must restart the programm for the changes to take effect.");
			}

			if (restart) {
				PlatformUI.getWorkbench().restart();
			}
			ServerView.refresh();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

}