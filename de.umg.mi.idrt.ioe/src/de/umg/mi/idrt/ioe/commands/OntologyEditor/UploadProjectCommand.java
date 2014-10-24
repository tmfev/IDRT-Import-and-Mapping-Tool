package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import java.util.HashMap;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.wizards.UploadProjectWizard;

public class UploadProjectCommand extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		UploadProjectWizard wiz = new UploadProjectWizard();
		WizardDialog wizardDialog = new WizardDialog(Application.getShell(),
				wiz);
		wizardDialog.open();
		return null;
	}
}