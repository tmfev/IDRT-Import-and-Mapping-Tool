package de.umg.mi.idrt.idrtimporttool.server.serverWizard;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.idrtimporttool.server.Settings.User;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class ServerSourceContentProvider implements ITreeContentProvider {

	private ServerImportDBModel model;

	@Override
	public void dispose() {
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Server) {

			Server server = (Server) parentElement;

			List<User> users = ServerList.getUsersSourceServer(server);
			if (users != null) {
				return users.toArray();
			} else {
				return null;
			}

		} else if (parentElement instanceof User) {
			User user = (User) parentElement;
			return ServerList.getTables(user).toArray();
		} else {
			return null;
		}
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
		if ((element instanceof Server) || (element instanceof User)) {
			return true;
		}
		return false;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		model = (ServerImportDBModel) newInput;
	}
}