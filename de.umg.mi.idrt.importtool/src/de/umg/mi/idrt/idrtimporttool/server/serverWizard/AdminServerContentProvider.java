package de.umg.mi.idrt.idrtimporttool.server.serverWizard;

import java.util.HashSet;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.umg.mi.idrt.idrtimporttool.server.Settings.I2b2Project;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class AdminServerContentProvider implements ITreeContentProvider {

	private AdminServerModel model;

	@Override
	public void dispose() {
	}

	@Override
	public Object[] getChildren(Object parentElement) {

		if (parentElement instanceof Server) {
			Server server = (Server) parentElement;
			HashSet<I2b2Project> users = ServerList.getUsersTargetServer(server);
//			HashSet<String> users = ServerList.getI2B2Projects(server);
			return users.toArray();
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
		model = (AdminServerModel) newInput;
	}

}