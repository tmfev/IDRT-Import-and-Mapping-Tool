package de.umg.mi.idrt.idrtimporttool.server.serverWizard;

import java.util.HashSet;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;

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


			HashSet<String> users = ServerList.getUsersTargetServer(server);
			if (users != null)
				return users.toArray();
			else 
				return null;
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