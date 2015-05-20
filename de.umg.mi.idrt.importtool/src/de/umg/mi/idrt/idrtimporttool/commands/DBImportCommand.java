package de.umg.mi.idrt.idrtimporttool.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;

import de.umg.mi.idrt.idrtimporttool.ImportWizard.DBImportWizard;
import de.umg.mi.idrt.idrtimporttool.importidrt.Application;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class DBImportCommand extends AbstractHandler {

	static DBImportWizard impWiz;

	public static DBImportWizard getImpWiz() {
		return impWiz;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		impWiz = new DBImportWizard();
		WizardDialog wizardDialog = new WizardDialog(Application.getShell(),
				impWiz);
		wizardDialog.open();
		return null;
	}
}
