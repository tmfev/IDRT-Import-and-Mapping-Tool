package de.umg.mi.idrt.idrtimporttool.server.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;

import de.umg.mi.idrt.idrtimporttool.ImportWizard.DBWizardPageTwo;
import de.umg.mi.idrt.idrtimporttool.importidrt.ServerView;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.AddSourceServerWizard;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class AddSourceServerCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TreeViewer viewer = ServerView.getSourceServerViewer();
		if ((DBWizardPageTwo.getImportDBViewer() != null)
				&& !DBWizardPageTwo.getImportDBViewer().getTree().isDisposed()) {
			viewer = DBWizardPageTwo.getImportDBViewer();
		}
		WizardDialog wizardDialog = new WizardDialog(viewer.getControl()
				.getShell(), new AddSourceServerWizard());
		wizardDialog.open();
		if (((DBWizardPageTwo.getImportDBViewer() != null) && !DBWizardPageTwo
				.getImportDBViewer().getTree().isDisposed())
				|| !ServerView.getSourceServerViewer().getTree().isDisposed()) {
			ServerView.refresh();
		} else {
			DBWizardPageTwo.refresh();
		}
		return viewer;
	}
}
