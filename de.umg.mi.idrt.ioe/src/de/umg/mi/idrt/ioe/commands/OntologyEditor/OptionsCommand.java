package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;

import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.wizards.OptionsWizard;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class OptionsCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
System.out.println("OPTIONS");
		WizardDialog wizardDialog = new WizardDialog(Application.getShell(),
				new OptionsWizard());
		System.out.println("OPTIONS B4 OPEN");
		wizardDialog.open();
		System.out.println("OPTIONS AF OPEN");
		return null;

	}
}
