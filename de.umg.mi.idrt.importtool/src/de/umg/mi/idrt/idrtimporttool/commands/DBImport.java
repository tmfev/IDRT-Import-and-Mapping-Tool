package de.umg.mi.idrt.idrtimporttool.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import au.com.bytecode.opencsv.CSVWriter;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class DBImport {

	private static Connection connect;
	private static ResultSet rset;
	private static Server server;

	/**
	 * Constructor
	 * @param server
	 */
	public DBImport(Server server) {
		DBImport.server = server;
		connect();
	}

	public static ResultSet getRset() {
		return rset;
	}

	public void connect() {
		connect = server.getConnection();
	}

	/**
	 * Retrieves coloumn names from db table
	 * @return
	 */
	public String[] getColoumnNames() {
		try {
			int count = rset.getMetaData().getColumnCount();
			String[] names = new String[count + 1];
			names[0] = "Spaltenname (Pflicht)";
			for (int i = 1; i <= count; i++) {
				names[i] = rset.getMetaData().getColumnName(i);
			}
			return names;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String[] getDataTypes() {
		try {
			int count = rset.getMetaData().getColumnCount();

			String[] names = new String[count + 1];
			names[0] = "Datentyp (Pflicht)";
			for (int i = 1; i <= count; i++) {
				String currentColumn = rset.getMetaData().getColumnTypeName(i);
				if (currentColumn.toLowerCase().equals("date")) {
					names[i] = "Date";
				} else if (currentColumn.toLowerCase().equals("number")) {
					names[i] = "Float";
				} else if (currentColumn.toLowerCase().equals("varchar2")) {
					names[i] = "String";
				} else {
					names[i] = currentColumn;
				}
			}
			return names;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns the resultset
	 */
	public void getResultSet() {
		Statement stmt;
		try {
			stmt = connect.createStatement();
			rset = stmt.executeQuery("select * from " + server.getSchema()
					+ "." + server.getTable());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes data from exported db file
	 * @param file
	 * @param names
	 * @param fileDelim
	 */
	public void writeData(File file, String[] names, char fileDelim) {
		try {
			CSVWriter dbOutput = new CSVWriter(new FileWriter(file), fileDelim);

			// dbOutput.writeAll(rset, true);
			int count = rset.getMetaData().getColumnCount();
			String[] output = new String[count];
			String[] newNames = new String[names.length - 1];
			for (int i = 1; i < names.length; i++) {
				newNames[i - 1] = names[i];
			}
			dbOutput.writeNext(newNames);
			while (rset.next()) {
				for (int i = 1; i <= count; i++) {
					if (rset.getMetaData().getColumnTypeName(i)
							.equalsIgnoreCase("date")) {
						String date = rset.getDate(i).toString();
						String[] dateSplit = date.split("-");
						output[i - 1] = dateSplit[2] + "." + dateSplit[1] + "."
								+ dateSplit[0];
					} else if (rset.getMetaData().getColumnTypeName(i)
							.equalsIgnoreCase("number")) {
						output[i - 1] = rset.getString(i);
					} else {
						output[i - 1] = rset.getString(i);
					}
				}
				dbOutput.writeNext(output);
			}

			dbOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Writes Metadata file needed for import
	 * @param file
	 * @param names
	 * @param types
	 * @param niceNames
	 * @param metaInfos
	 * @param fileDelim
	 */
	public void writeMetaDataFile(File file, String[] names, String[] types,
			String[] niceNames, String[] metaInfos, char fileDelim) {

		try {
			CSVWriter dbOutput = new CSVWriter(new FileWriter(file), fileDelim);
			dbOutput.writeNext(names);
			dbOutput.writeNext(types);
			dbOutput.writeNext(niceNames);
			dbOutput.writeNext(metaInfos);
			dbOutput.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
