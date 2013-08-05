package de.umg.mi.idrt.ioe.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class ServerSettingsCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Preferences prefsRoot = Preferences.userRoot();
		Preferences myPrefs = prefsRoot.node("Server1");
		
		myPrefs.put("key", "value");
		File file = new File("asd");
		
		try {
			myPrefs.exportNode(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		
		
//		Preferences prefs = Preferences.userNodeForPackage(getClass());
//		
//		
//		prefs.put("key", "value");
//		
//		System.out.println(prefs.toString());
//		try {
//			System.out.println(prefs.keys()[0]);
//		} catch (BackingStoreException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		try {
//			File file = new File("asd");
//			prefs.exportNode(new FileOutputStream(file));
//			System.out.println(file.getAbsolutePath());
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (BackingStoreException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return null;
	}

}
