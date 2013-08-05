package de.umg.mi.idrt.idrtimporttool.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;

import de.umg.mi.idrt.idrtimporttool.ImportWizard.P21ImportWizard;
import de.umg.mi.idrt.idrtimporttool.importidrt.Application;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class P21ImportCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		WizardDialog wizardDialog = new WizardDialog(Application.getShell(),
				new P21ImportWizard());
		wizardDialog.open();
		return null;
	}

}
