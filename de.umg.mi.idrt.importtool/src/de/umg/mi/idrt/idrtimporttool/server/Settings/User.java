package de.umg.mi.idrt.idrtimporttool.server.Settings;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class User {

	private String name;
	private Server server;

	public User(String name, Server server) {
		this.name = name;
		this.server = server;
	}

	public String getName() {
		return name;
	}

	public Server getServer() {
		return server;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}

}
