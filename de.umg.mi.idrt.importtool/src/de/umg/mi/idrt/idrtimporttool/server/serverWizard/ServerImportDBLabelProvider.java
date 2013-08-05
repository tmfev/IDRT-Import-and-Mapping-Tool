package de.umg.mi.idrt.idrtimporttool.server.serverWizard;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerTable;
import de.umg.mi.idrt.idrtimporttool.server.Settings.User;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class ServerImportDBLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {
		if (element instanceof Server) {
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_FOLDER);
		}
		return PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_OBJ_FILE);
	}

	@Override
	public String getText(Object element) {
		if (element instanceof Server) {
			Server server = (Server) element;
			return server.getName();
		} else if (element instanceof User) {
			return ((User) element).getName();
		}

		else if (element instanceof ServerTable) {
			return ((ServerTable) element).getName();
		}
		return element.toString();
	}

}
