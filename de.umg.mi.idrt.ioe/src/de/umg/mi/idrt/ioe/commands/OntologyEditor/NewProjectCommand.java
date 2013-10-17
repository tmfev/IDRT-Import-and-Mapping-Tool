package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;

import de.umg.mi.idrt.ioe.view.OntologyEditorView;
import de.umg.mi.idrt.ioe.wizards.NewProjectWizard;

public class NewProjectCommand extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		if (OntologyEditorView.isInit()) {
			WizardDialog wizardDialog = new WizardDialog(OntologyEditorView.getTargetTreeViewer().getControl().getShell(), new NewProjectWizard());
			wizardDialog.open();
		}
		return null;
	}
}