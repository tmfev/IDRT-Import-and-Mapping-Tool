package de.umg.mi.idrt.importtool.misc;

import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.importtool.views.ServerView;

public class DatabaseStatsThread extends Thread{
	
	public void run(Server server, String selectedItemString) {
		
		ServerView.setLblObservationsCurrent(server
				.getConcepts(selectedItemString));
		ServerView.setLblPatientsCurrent(server
				.getPatients(selectedItemString));
	
	}

}
