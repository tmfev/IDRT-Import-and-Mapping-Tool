package de.umg.mi.idrt.idrtimporttool.server.serverWizard;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GCData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

import de.umg.mi.idrt.idrtimporttool.importidrt.Activator;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class ServerLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {

		File imgImportFile;
		try {
			Bundle bundle = Activator.getDefault().getBundle();
			Path imgImportPath = new Path("/images/i2b2_16.png"); //$NON-NLS-1$
			URL url = FileLocator.find(bundle, imgImportPath,
					Collections.EMPTY_MAP);
			URL fileUrl = FileLocator.toFileURL(url);
			imgImportFile = new File(fileUrl.getPath());

			Device device = new Device() {

				@Override
				public void internal_dispose_GC(int hDC, GCData data) {

				}

				@Override
				public int internal_new_GC(GCData data) {
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
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

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
