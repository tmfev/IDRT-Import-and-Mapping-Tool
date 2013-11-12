package de.umg.mi.idrt.ioe.OntologyTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;

import de.umg.mi.idrt.ioe.Activator;
import de.umg.mi.idrt.ioe.Resource;

public class FileHandling {

	
	public static String getTempFilePath(String filename){
		
		String filePath = "";
		
		try {
			Bundle bundle = Activator.getDefault().getBundle();
			Path path = new Path("/" + Resource.Files.TEMP_FOLDER + "/"); //$NON-NLS-1$
			URL url = FileLocator.find(bundle, path,
					Collections.EMPTY_MAP);
		
			URL miscFileUrl = FileLocator.toFileURL(url);
			File misc = new File(miscFileUrl.getPath());
			filePath = misc.getAbsolutePath().replaceAll("\\\\", "/") + "/" + filename;
		
			
		} catch (FileNotFoundException  e) {
			e.printStackTrace();
			filePath = "C:\tmp.csv";
		} catch (IOException e) {
			e.printStackTrace();
			filePath = "C:\tmp.csv";
		}
		
		
		return filePath;
		
	}
public static String getCFGFilePath(String filename){
		
		String filePath = "";
		
		try {
			Bundle bundle = Activator.getDefault().getBundle();
			Path path = new Path("/" + "cfg" + "/"); //$NON-NLS-1$
			URL url = FileLocator.find(bundle, path,
					Collections.EMPTY_MAP);
		
			URL miscFileUrl = FileLocator.toFileURL(url);
			File misc = new File(miscFileUrl.getPath());
			filePath = misc.getAbsolutePath().replaceAll("\\\\", "/") + "/" + filename;
		
			
		} catch (FileNotFoundException  e) {
			e.printStackTrace();
//			filePath = "C:\tmp.csv";
		} catch (IOException e) {
			e.printStackTrace();
//			filePath = "C:\tmp.csv";
		}
		
		
		return filePath;
		
	}
	
}
