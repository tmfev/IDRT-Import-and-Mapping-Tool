package de.umg.mi.idrt.idrtimporttool.ExportDB;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import au.com.bytecode.opencsv.CSVWriter;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class ExportDB {

	private static Connection connect = null;
	private static Statement statement = null;
	private static ResultSet resultSet = null;

	/**
	 * Exports the table from the server
	 * 
	 * @param server
	 *            source server
	 * @param output
	 *            output file
	 */
	public static void exportDB(Server server, File output) {
//				+ output.getAbsolutePath());
		try {
			CSVWriter rotatedOutput = new CSVWriter(new FileWriter(output),
					'\t');
			connect = server.getConnection();

			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement.executeQuery("select * from "
					+ server.getSchema() + "." + server.getTable());
			int columnCount = resultSet.getMetaData().getColumnCount();

			/**
			 * header
			 */
			String[] header = new String[columnCount];

			for (int j = 1; j <= columnCount; j++) {
				header[j - 1] = resultSet.getMetaData().getColumnName(j);
			}
			rotatedOutput.writeNext(header);
			/**
			 * data
			 */
			while (resultSet.next()) {
				String[] nextLine = new String[columnCount];
				for (int i = 1; i <= columnCount; i++) {
//					System.out.print(i + " " + resultSet.getString(i) + " "
//							+ resultSet.getMetaData().getColumnTypeName(i)
//							+ " | ");
					if (resultSet.getMetaData().getColumnTypeName(i)
							.equals("DATE")) {
						Date bla = resultSet.getDate(i);
						String[] dateString = bla.toString().split("-");
						nextLine[i - 1] = dateString[2] + "." + dateString[1]
								+ "." + dateString[0];
					} else {
						nextLine[i - 1] = resultSet.getString(i);
					}
				}
				rotatedOutput.writeNext(nextLine);
			}
			rotatedOutput.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
