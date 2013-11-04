package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;

import de.umg.mi.idrt.ioe.view.OntologyEditorView;
import de.umg.mi.idrt.ioe.wizards.EditInstanceWizard;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 * Department of Medical Informatics Goettingen
 * www.mi.med.uni-goettingen.de
 */
public class EditInstanceCommand extends AbstractHandler { 

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (OntologyEditorView.isInit()) {
			WizardDialog wizardDialog = new WizardDialog(OntologyEditorView.getTargetTreeViewer().getControl().getShell(), new EditInstanceWizard());
			wizardDialog.open();
		}
		return null;
	}
}
