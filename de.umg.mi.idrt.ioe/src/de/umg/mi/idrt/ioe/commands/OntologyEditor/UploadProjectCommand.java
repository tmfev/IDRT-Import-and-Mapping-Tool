package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;

import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;
import de.umg.mi.idrt.ioe.wizards.UploadProjectWizard;

public class UploadProjectCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (OntologyEditorView.isInit() && !OntologyEditorView.getTargetSchemaName().isEmpty() && !OntologyEditorView.getTargetSchemaName().startsWith("Drop i2b2")) {
			WizardDialog wizardDialog = new WizardDialog(OntologyEditorView.getTargetTreeViewer().getControl().getShell(), new UploadProjectWizard());
			wizardDialog.open();
		}
		else {
			MessageDialog.openError(Application.getShell(), "No Target Project", "No Target Project given!");
		}
		return null;
	}
}