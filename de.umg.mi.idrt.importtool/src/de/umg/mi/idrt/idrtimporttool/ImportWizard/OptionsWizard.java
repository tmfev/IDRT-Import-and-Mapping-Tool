package de.umg.mi.idrt.idrtimporttool.ImportWizard;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Properties;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

import de.umg.mi.idrt.idrtimporttool.importidrt.Activator;
import de.umg.mi.idrt.idrtimporttool.importidrt.ServerView;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class OptionsWizard extends Wizard {

	private Properties defaultProps;
	protected OptionsWizardPageOne one;

	public OptionsWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		one = new OptionsWizardPageOne();
		addPage(one);
	}

	@Override
	public boolean performFinish() {
		try {
			Bundle bundle = Activator.getDefault().getBundle();
			Path path = new Path("/cfg/Default.properties"); 
			URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
			URL fileUrl = FileLocator.toFileURL(url);

			File properties = new File(fileUrl.getPath());
			defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));

			defaultProps.setProperty("PIDURL",
					OptionsWizardPageOne.getTxtPIDURL());
			defaultProps.setProperty("log", OptionsWizardPageOne.getTxtLog());
			defaultProps.setProperty("sysoLog",
					OptionsWizardPageOne.getSysoLog());
			defaultProps
					.setProperty("filter", OptionsWizardPageOne.getFilter());
			defaultProps.setProperty("sysoLogLoc",
					OptionsWizardPageOne.getSysoLogPath());
			defaultProps.setProperty("guessRows",
					OptionsWizardPageOne.getGuessRowtext());
			defaultProps.setProperty("hideTemp",
					OptionsWizardPageOne.getHideTempTablesButton());
			defaultProps.store(new FileWriter(properties), "");
			boolean restart = false;
			if (OptionsWizardPageOne.getChanged()) {
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