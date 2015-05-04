package de.umg.mi.idrt.idrtimporttool.ExportDB;

import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class ExportConfig {

	public ExportConfig(Server server, String schema, String table) {
		setServer(server);
		setSchema(schema);
		setTable(table);
	}

	private Server server;
	private String schema;

	private String table;

	public String getSchema() {
		return schema;
	}

	public Server getServer() {
		return server;
	}

	public String getTable() {
		return table;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public void setTable(String table) {
		this.table = table;
	}

	@Override
	public String toString() {
		return server.getIp() + " | " + server.getName() + " | " + schema
				+ " | " + table;
	}
}
