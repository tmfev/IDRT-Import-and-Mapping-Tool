package routines;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import de.goettingen.i2b2.importtool.idrt.StatusListener.StatusListener;
import au.com.bytecode.opencsv.CSVWriter;


public class ExportDB {
	private static String ORACLEDRIVER = "oracle.jdbc.driver.OracleDriver";
	private static String MSSQLDRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static String MYSQLDRIVER = "com.mysql.jdbc.Driver";
	private static Connection connect = null;
	private static Statement statement = null;
	private static ResultSet resultSet = null;

	/**
	 * Exports the table from the server
	 * @param server source server
	 * @param output output file
	 */
	public static void exportDB(String serverName, String serverIP, String serverPort, String serverSID,
			String serverSchema, String serverUsername, String serverPassword, 
			String serverTable, String outputFileName, String databaseType, String mssqlSchema){

		File output = new File(outputFileName+serverName.replaceAll(" ", "_")+"_"+serverSchema+"_"+serverTable+".csv");
		System.out.println("exporting DB Table: " + serverTable + " to: " + output.getAbsolutePath());
		System.out.println("DATABASETYPE: " + databaseType);
		try {
			CSVWriter rotatedOutput = new CSVWriter(new FileWriter(output), '\t');

			System.out.println();
			
			DriverManager.setLoginTimeout(2);
			if(databaseType == null) {
				System.out.println("DATABASE == NULL");
				Class.forName(ORACLEDRIVER);
				connect = DriverManager.getConnection("jdbc:oracle:thin:@" + serverIP + ":" + serverPort + ":"
						+ serverSID, serverUsername, serverPassword);
			}
		else if (databaseType.equalsIgnoreCase("oracle")) {
				Class.forName(ORACLEDRIVER);
				connect = DriverManager.getConnection("jdbc:oracle:thin:@" + serverIP + ":" + serverPort + ":"
						+ serverSID, serverUsername, serverPassword);
			}
			else if (databaseType.equalsIgnoreCase("mysql")) {
				Class.forName(MYSQLDRIVER);
				connect =  DriverManager.getConnection("jdbc:mysql://" + serverIP + ":" + serverPort + "/"
						+ serverSID, serverUsername, serverPassword);	
			}
			else if (databaseType.equalsIgnoreCase("mssql")) {
				Class.forName(MSSQLDRIVER);
				if (serverPort.length()>0)
					connect = DriverManager.getConnection("jdbc:sqlserver://" + serverIP + ":" 
							+ serverPort, serverUsername, serverPassword);
					else {
						connect = DriverManager.getConnection("jdbc:sqlserver://" + serverIP, serverUsername, serverPassword);
					}
			}
			connect.setAutoCommit(false);
//			statement = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
//					ResultSet.CONCUR_READ_ONLY);
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			long start = System.currentTimeMillis();
			if (databaseType.equalsIgnoreCase("mssql"))
			resultSet = statement
					.executeQuery("select count(1) from " + serverSchema + "." + mssqlSchema + "."+serverTable );// + " where rownum > 100000 and rownum < 100005");
			else 
				resultSet = statement
				.executeQuery("select count(1) from " + serverSchema + "."+serverTable );// + " where rownum > 100000 and rownum < 100005");
		
			
			resultSet.next();
			int rowcount = resultSet.getInt(1);
			
			if (databaseType.equalsIgnoreCase("mssql"))
			resultSet = statement
					.executeQuery("select * from " + serverSchema + "." + mssqlSchema + "."+serverTable);
			else
				resultSet = statement
			.executeQuery("select * from " + serverSchema + "."+serverTable);
				
			
			//			resultOutput.writeAll(resultSet, true);
			//			System.out.println("WRITING RESULTSET DONE!");
			int columnCount = resultSet.getMetaData().getColumnCount();
			/**
			 * header
			 */
			String[]header = new String[columnCount];

			for (int j = 1; j <= columnCount; j++) {
				header[j-1]=resultSet.getMetaData().getColumnName(j);
			}
			rotatedOutput.writeNext(header);
			/**
			 * data
			 */
			int rows =rowcount;
			System.out.println("ROWS: " + rows);
			float rowsPerPercent = (float)(100/(float)rows);
			float percent=0;
			int next = 0;
			String[]nextLine = new String[columnCount];
			while (resultSet.next()) {
				next++;
				if (next%10000==0){
//					rotatedOutput.flush();
				}
				float oldPercent = percent;
				percent+=rowsPerPercent;
//									System.out.println(percent+"%");
				if (Math.ceil((double)(percent))> Math.ceil((double)oldPercent))
										StatusListener.setStatusPID(percent, "Extracting Table " + serverTable, output.getAbsolutePath());
				for (int i = 1; i <= columnCount; i++) {
					//					System.out.print(i + " " + resultSet.getString(i) + " " +resultSet.getMetaData().getColumnTypeName(i) + " | ");
					if (resultSet.getMetaData().getColumnTypeName(i).equals("DATE")){
						Date bla = resultSet.getDate(i);
						//						String[] dateString = bla.toString().split("-");
						//						System.out.println("date: " + bla);
						//						nextLine[i-1]=dateString[2]+"."+dateString[1]+"."+dateString[0];
						nextLine[i-1]=bla.toString();
					}
					else{
						nextLine[i-1]=resultSet.getString(i);
					}
				}
				rotatedOutput.writeNext(nextLine);
			}
			long end = System.currentTimeMillis() - start;
			System.out.println("time: " + end/1000 + ","+end%1000+"s");
			statement.close();
			resultSet.close();
			rotatedOutput.close();
			System.out.println("finish export from db to " + output.getAbsolutePath());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
