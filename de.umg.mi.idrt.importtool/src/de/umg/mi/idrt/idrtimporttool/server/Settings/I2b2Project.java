package de.umg.mi.idrt.idrtimporttool.server.Settings;

import java.io.Serializable;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class I2b2Project implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;
	private Server server;

	public I2b2Project(String name, Server server) {
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
		return "I2b2Project: " + name + " @ " + server.toString();
	}

}
