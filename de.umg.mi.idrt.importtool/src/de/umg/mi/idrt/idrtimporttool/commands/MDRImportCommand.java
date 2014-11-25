package de.umg.mi.idrt.idrtimporttool.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;

import de.umg.mi.idrt.idrtimporttool.ImportWizard.MDRImportWizard;
import de.umg.mi.idrt.idrtimporttool.importidrt.Application;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class MDRImportCommand extends AbstractHandler {

	private static MDRImportWizard wiz;

	public static MDRImportWizard getWizard() {
		return wiz;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		wiz = new MDRImportWizard();
		WizardDialog wizardDialog = new WizardDialog(Application.getShell(),
				wiz);
		wizardDialog.open();
		return null;
	}

}
