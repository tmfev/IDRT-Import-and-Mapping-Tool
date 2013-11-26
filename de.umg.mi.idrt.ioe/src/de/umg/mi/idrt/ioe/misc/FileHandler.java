package de.umg.mi.idrt.ioe.misc;

import java.io.File;
import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;

import de.umg.mi.idrt.ioe.Activator;

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

	public static String getTempFilePath(String filename){
System.out.println(getBundleFile("/temp/").getAbsolutePath().replaceAll("\\\\", "/")+"/"+filename);
		return getBundleFile("/temp/").getAbsolutePath().replaceAll("\\\\", "/")+"/"+filename;

	}
	public static String getCFGFilePath(String filename){

		return getBundleFile("/cfg/").getAbsolutePath().replaceAll("\\\\", "/")+"/"+filename;

	}

}
