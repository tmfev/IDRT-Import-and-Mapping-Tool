package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;
import de.umg.mi.idrt.ioe.wizards.EditInstanceWizard;
import de.umg.mi.idrt.ioe.wizards.RegexWizard;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 * Department of Medical Informatics Goettingen
 * www.mi.med.uni-goettingen.de
 */
public class AddRegexCommand extends AbstractHandler { 

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
			WizardDialog regexDialog = new WizardDialog(Application.getShell(), new RegexWizard());
			regexDialog.open();
		return null;
	}
}
