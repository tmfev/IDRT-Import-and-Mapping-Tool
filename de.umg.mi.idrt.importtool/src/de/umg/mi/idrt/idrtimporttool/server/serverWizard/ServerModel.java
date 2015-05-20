package de.umg.mi.idrt.idrtimporttool.server.serverWizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class ServerModel {

	public List<Server> getCategories() {
		List<Server> categories = new ArrayList<Server>();

		HashMap<String, Server> serverList = ServerList.getTargetServers();
		Set<String> serverNames = serverList.keySet();
		Iterator<String> it = serverNames.iterator();

		while (it.hasNext()) {
			String server = it.next();
			Server currentServer = serverList.get(server);
			categories.add(currentServer);
		}

		return categories;
	}

//	public List<String> getCategoriesString() {
//		List<String> categories = new ArrayList<String>();
//
//		HashMap<String, Server> serverList = ServerList.getTargetServers();
//		Set<String> serverNames = serverList.keySet();
//		Iterator<String> it = serverNames.iterator();
//
//		while (it.hasNext()) {
//			String server = it.next();
//			Server currentServer = serverList.get(server);
//			categories.add(currentServer.getUniqueID());
//		}
//
//		return categories;
//	}

}
