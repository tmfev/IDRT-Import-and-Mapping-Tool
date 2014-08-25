package de.umg.mi.idrt.idrtimporttool.server.serverWizard;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.ResourceManager;

import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class AdminServerLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {

//		File imgImportFile;
//		imgImportFile = FileHandler.getBundleFile("/images/i2b2_16.png");

		Image imgImport = ResourceManager.getPluginImage("de.umg.mi.idrt.importtool", "images/i2b2_16.png");
		
//		Image imgImport = new Image(device, imgImportFile.getAbsolutePath());

		if (element instanceof Server) {
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_FOLDER);
		}
		return imgImport;
		// return PlatformUI.getWorkbench().getSharedImages()
		// .getImage(ISharedImages.IMG_OBJ_FILE);

		// PlatformUI.getWorkbench().getSharedImages()
		// .getImage(ISharedImages.IMG_OBJ_FOLDER);

	}

	@Override
	public String getText(Object element) {
		if (element instanceof Server) {
			Server server = (Server) element;
			return server.getName();
		}
		return element.toString();
		// return ((Todo) element).getSummary();
	}

}
