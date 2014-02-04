package de.umg.mi.idrt.idrtimporttool.server.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;

import de.umg.mi.idrt.idrtimporttool.ImportWizard.DBWizardPage2;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.AddSourceServerWizard;
import de.umg.mi.idrt.importtool.views.ServerView;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class AddSourceServerCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TreeViewer viewer = ServerView.getSourceServerViewer();
		if ((DBWizardPage2.getImportDBViewer() != null)
				&& !DBWizardPage2.getImportDBViewer().getTree().isDisposed()) {
			viewer = DBWizardPage2.getImportDBViewer();
		}
		WizardDialog wizardDialog = new WizardDialog(viewer.getControl()
				.getShell(), new AddSourceServerWizard());
		wizardDialog.open();
		if (((DBWizardPage2.getImportDBViewer() != null) && !DBWizardPage2
				.getImportDBViewer().getTree().isDisposed())
				|| !ServerView.getSourceServerViewer().getTree().isDisposed()) {
			ServerView.refresh();
		} else {
			DBWizardPage2.refresh();
		}
		return viewer;
	}
}
