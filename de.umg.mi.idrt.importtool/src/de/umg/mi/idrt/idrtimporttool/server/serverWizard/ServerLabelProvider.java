package de.umg.mi.idrt.idrtimporttool.server.serverWizard;

import java.io.File;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GCData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.importtool.misc.FileHandler;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class ServerLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {

		File imgImportFile;
		imgImportFile = FileHandler.getBundleFile("/images/i2b2_16.png");

		Device device = new Device() {

			@Override
			public void internal_dispose_GC(long hDC, GCData data) {

			}

			@Override
			public long internal_new_GC(GCData data) {
				return 0;
			}
		};
		Image imgImport = new Image(device, imgImportFile.getAbsolutePath());

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
