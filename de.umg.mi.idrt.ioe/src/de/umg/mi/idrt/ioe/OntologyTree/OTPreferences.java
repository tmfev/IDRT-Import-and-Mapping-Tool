package de.umg.mi.idrt.ioe.OntologyTree;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class OTPreferences {

	public OTPreferences (){
		
		
		
		Preferences preferences = (Preferences) ConfigurationScope.INSTANCE
				.getNode("de.vogella.preferences.test");

		
			
		
			Preferences sub1 = preferences.node("node1");
			Preferences sub2 = preferences.node("node2");
			sub1.put("h1", "Hello");
			sub1.put("h2", "Hello again");
			sub2.put("h1", "Moin");
			try {
				// Forces the application to save the preferences
				preferences.flush();
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
			
		
		return;
	}
	
}
