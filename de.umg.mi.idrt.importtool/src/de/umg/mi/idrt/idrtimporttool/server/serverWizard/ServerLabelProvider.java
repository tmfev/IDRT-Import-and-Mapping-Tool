package de.umg.mi.idrt.idrtimporttool.server.serverWizard;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.ResourceManager;

import de.umg.mi.idrt.idrtimporttool.server.Settings.I2b2Project;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class ServerLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {

//		File imgImportFile = FileHandler.getBundleFile("/images/i2b2_16.png");
//		File tmImageFile = FileHandler.getBundleFile("/images/searchtool16.png");
//		if (tmImageFile== null)
//			try {
//				throw new Exception();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		Image tmImage = ResourceManager.getPluginImage("de.umg.mi.idrt.importtool", "images/searchtool16.png");
		Image imgImport = ResourceManager.getPluginImage("de.umg.mi.idrt.importtool", "images/i2b2_16.png");
//		Image imgImport = new Image(device, imgImportFile.getAbsolutePath());
//		Image tmImage = new Image(device, tmImageFile.getAbsolutePath());
		if (element instanceof Server) {
			if (((Server)element).getWhType().equalsIgnoreCase("transmart")){
				return tmImage;
			}
			else {
				return PlatformUI.getWorkbench().getSharedImages()
						.getImage(ISharedImages.IMG_OBJ_FOLDER);
			}
		}
		return imgImport;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof Server) {
			Server server = (Server) element;
			return server.getName();
		}
		else if (element instanceof I2b2Project)
		return ((I2b2Project) element).getName();
		else
			return element.toString();
		// return ((Todo) element).getSummary();
	}

}
