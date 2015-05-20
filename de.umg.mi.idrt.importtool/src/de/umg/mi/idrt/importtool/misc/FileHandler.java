package de.umg.mi.idrt.importtool.misc;

import java.io.File;
import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;

import de.umg.mi.idrt.idrtimporttool.importidrt.Activator;

/**
* @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
* Department of Medical Informatics Goettingen
* www.mi.med.uni-goettingen.de
* 
* IMPORT TOOL FILE HANDLER
 */
public class FileHandler {

	public static File getBundleFile(String filePath) {
		try {
			Bundle bundle;
			bundle = Activator.getDefault().getBundle();
			URL mainURL = FileLocator.find(bundle, new Path(""), null);
			URL mainFileUrl = FileLocator.toFileURL(mainURL);
			File mainPath = new File(mainFileUrl.getPath());
			return new File(mainPath+filePath);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
