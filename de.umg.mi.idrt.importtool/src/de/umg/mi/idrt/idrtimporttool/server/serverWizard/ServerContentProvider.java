package de.umg.mi.idrt.idrtimporttool.server.serverWizard;

import java.util.HashSet;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import de.umg.mi.idrt.idrtimporttool.importidrt.Application;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.importtool.views.ServerView;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class ServerContentProvider implements ITreeContentProvider {

	private ServerModel model;

	@Override
	public void dispose() {
	}

	@Override
	public Object[] getChildren(Object parentElement) {

		if (parentElement instanceof Server) {
			Server server = (Server) parentElement;
//			if (!server.isSavePassword() && server.getNotStoredPassword() == null) {
//				PasswordDialog dialog = new PasswordDialog(new Shell());
//			    dialog.open();
//				server.setNotStoredPassword(dialog.getPassword());
//			}
				HashSet<String> users = ServerList.getUsersTargetServer(server);
				if (users != null)
					return users.toArray();
				else {
					ServerView.refresh();
					return null;
					
				}
//			}
//			else {
//				MessageDialog.openError(Application.getShell(), "NYI", "NYI");
//			}
		}
		return null;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return model.getCategories().toArray();
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof Server) {
			return true;
		}
		return false;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		model = (ServerModel) newInput;
	}

}