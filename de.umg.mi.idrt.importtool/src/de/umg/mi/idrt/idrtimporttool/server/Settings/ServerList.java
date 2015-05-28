package de.umg.mi.idrt.idrtimporttool.server.Settings;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.prefs.BackingStoreException;

import org.eclipse.jface.dialogs.MessageDialog;

import de.umg.mi.idrt.idrtimporttool.importidrt.Application;
import de.umg.mi.idrt.importtool.misc.FileHandler;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class ServerList {

	final static int iterations = 3;
	private static HashMap<String, String> userServer= new HashMap<String, String>();

	private static Connection connect = null;
	private static HashMap<String, Server> servers = new HashMap<String, Server>();
	private static HashMap<String, Server> importDBServers = new HashMap<String, Server>();
	private static HashMap<String, ServerTable> tableMap;
	private static Statement statement = null;
	private static ResultSet resultSet = null;
	private static File serverFile;
	private static File serverImportDBFile;
	private static Properties defaultProps;
	private static String[] USERROLES = { "USER", "EDITOR", "DATA_PROT",
		"DATA_OBFSC", "DATA_LDS", "DATA_DEID", "DATA_AGG" };


	public static void addServer(Server server) {
		try {
			if (!importDBServers.containsKey(server.getUniqueID())) {
				servers.put(server.getUniqueID(), server);
				ObjectOutputStream os = new ObjectOutputStream(
						new FileOutputStream(serverFile));
				os.writeObject(servers);
				os.flush();
				os.close();
			} else {
				System.err.println("server vorhanden");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void addSourceServer(Server loadServer) {
		if (!servers.containsKey(loadServer.getUniqueID())) {
			importDBServers.put(loadServer.getUniqueID(), loadServer);
			try {

				ObjectOutputStream os = new ObjectOutputStream(
						new FileOutputStream(serverImportDBFile));
				os.writeObject(importDBServers);
				os.flush();
				os.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("Server vorhanden");
		}
	}

	public static void addUser(Server server, String username,
			String newFullname, String newPassword, String newEmail, Date date) {
		try {
			DriverManager.setLoginTimeout(2);
			connect = server.getConnection();
			connect.setAutoCommit(true);
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			String sql = "insert into i2b2pm.pm_user_data (user_id, full_name, password, email, change_date, entry_date, changeby_char, status_cd) values ('"
					+ username
					+ "', '"
					+ newFullname
					+ "','"
					+ newPassword
					+ "', '"
					+ newEmail
					+ "', TO_DATE('"
					+ date
					+ "','yyyy-mm-dd'), TO_DATE('"
					+ date
					+ "','yyyy-mm-dd'), 'IDRT', 'A')";
			resultSet = statement.executeQuery(sql);
			connect.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void assignUserToProject(String username, String project,
			Server server) {
		try {
			DriverManager.setLoginTimeout(2);
			connect = server.getConnection();

			statement = connect.createStatement();
			java.util.Date today = new java.util.Date();
			java.sql.Date date = new java.sql.Date(today.getTime());
			connect.setAutoCommit(true);
			for (String role : USERROLES) {
				String sql = "insert into i2b2pm.pm_project_user_roles (project_id, user_id, user_role_cd, change_date, entry_date, changeby_char, status_cd) "
						+ "values ('"
						+ project
						+ "','"
						+ username
						+ "','"
						+ role
						+ "', TO_DATE('"
						+ date
						+ "','yyyy-mm-dd'), TO_DATE('"
						+ date
						+ "','yyyy-mm-dd'), 'IDRT', 'A')";
				resultSet = statement.executeQuery(sql);
			}
			connect.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Server deserializeImportServer(File importServerFile) {

		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(
					importServerFile));
			Server server = (Server) is.readObject();
			is.close();
			return server;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HashMap<String, Server> deserializeServer(File file) {
		try {
			if (!file.exists()) {
				file = new File(file.getAbsolutePath());
				file.createNewFile();				
			}
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(
					file));
			HashMap<String, Server> list  = (HashMap<String, Server>) is.readObject();
			is.close();
			return list;

		} catch (EOFException ee) {
			System.err.println("ServerList: <" +  file.getName() + "> is empty!");
			return new HashMap<String, Server>();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void editUser(Server server, String newUsername,
			String oldUsername, String newFullname, String newEmail, Date date) {

		try {

			DriverManager.setLoginTimeout(2);
			connect = server.getConnection();
			connect.setAutoCommit(true);
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			statement.executeQuery("update i2b2pm.pm_user_data set user_id='"
					+ newUsername + "', full_name='" + newFullname
					+ "',email='" + newEmail + "',change_date=TO_DATE('" + date
					+ "','yyyy-mm-dd') where user_id='" + oldUsername + "'");

			if (!newUsername.equals(oldUsername)) {
				statement
				.executeQuery("update i2b2pm.pm_user_session set user_id='"
						+ newUsername
						+ "' where user_id='"
						+ oldUsername + "'");
				statement
				.executeQuery("update i2b2pm.pm_project_user_roles set user_id='"
						+ newUsername
						+ "' where user_id='"
						+ oldUsername + "'");
			}
			connect.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void editUserPW(Server server, String oldUsername,
			String newPassword) {
		try {
			DriverManager.setLoginTimeout(2);
			connect = server.getConnection();
			connect.setAutoCommit(true);
			statement = connect.createStatement();
			statement.executeQuery("update i2b2pm.pm_user_data set password='"
					+ newPassword + "' where user_id='" + oldUsername + "'");
			connect.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean getAdmin(Server server, String username,
			String project) {
		try {
			DriverManager.setLoginTimeout(2);
			connect = server.getConnection();

			statement = connect.createStatement();
			connect.setAutoCommit(true);
			String sql = "select user_role_cd from i2b2pm.pm_project_user_roles where user_id='"
					+ username
					+ "' and project_id='"
					+ project
					+ "' and user_role_cd='ADMIN'";
			resultSet = statement.executeQuery(sql);
			String admin = "";
			while (resultSet.next()) {
				admin = resultSet.getString("user_role_cd");
			}
			connect.close();
			if (admin.toLowerCase().equals("admin")) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static LinkedList<String> getAllUsersFromServer(Server server) {
		try {

			if (server.getUser().toLowerCase().equals("system")
					|| server.getUser().toLowerCase().equals("sys")) {
				DriverManager.setLoginTimeout(2);
				connect = server.getConnection();
				statement = connect.createStatement();
				// Result set get the result of the SQL query
				LinkedList<String> users = new LinkedList<String>();
				resultSet = statement
						.executeQuery("select distinct user_id from i2b2pm.pm_user_data order by user_id asc");

				while (resultSet.next()) {
					String user = resultSet.getString("user_id");
					if (!user.contains("SERVICE_ACCOUNT")) {
						users.add(user.toLowerCase());
					}
				}
				connect.close();
				return users;
			} else {
				throw new Exception(
						"Not enough Database priviliges, try system or sys!");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			MessageDialog.openError(Application.getShell(), "Error", e.getMessage());
			System.err.println(e.getMessage());
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openError(Application.getShell(), "Error", e.getMessage());
			System.err.println(e.getMessage());
			LinkedList<String> users = new LinkedList<String>();
			return users;
		}
		return null;
	}

	public static LinkedList<String> getAssignedUsersFromProject(Server server,
			String project) {
		try {
			LinkedList<String> users = new LinkedList<String>();
			if (!project.isEmpty()) {

				DriverManager.setLoginTimeout(2);
				connect = server.getConnection();

				statement = connect.createStatement();
				// Result set get the result of the SQL query

				resultSet = statement
						.executeQuery("select distinct user_id from i2b2pm.pm_project_user_roles where UPPER(project_id)=UPPER('"
								+ project + "') order by user_id asc");

				while (resultSet.next()) {
					String user = resultSet.getString("user_id");
					if (!user.contains("SERVICE_ACCOUNT")) {
						users.add(user);
					}
				}
				connect.close();
			}
			return users;

		} catch (SQLException e) {
			MessageDialog.openError(Application.getShell(), "Error", e.getMessage());
			System.err.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public static HashSet<String> getI2B2Projects(Server server) {
		try {
			DriverManager.setLoginTimeout(2);
			connect = server.getConnection();

			statement = connect.createStatement();
			// Result set get the result of the SQL query
			HashSet<String> users = new HashSet<String>();
			if (server.getUser().toLowerCase().equals("system")
					|| server.getUser().toLowerCase().equals("sys")) {
				resultSet = statement
						.executeQuery("select project_id from i2b2pm.pm_project_data");

				while (resultSet.next()) {
					users.add(resultSet.getString("project_id"));
				}
			} else {
				users = new HashSet<String>();
				throw new Exception(
						"Not enough Database priviliges, try system or sys!");

				// users.add(server.getUser());
			}
			connect.close();
			return users;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			MessageDialog.openError(Application.getShell(), "Error", e.getMessage());
			System.err.println(e.getMessage());
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			MessageDialog.openError(Application.getShell(), "Error", e.getMessage());
			e.printStackTrace();
			HashSet<String> users = new HashSet<String>();
			return users;
		}
		return null;
	}

	public static I2B2User getI2B2UserData(String user, String project,
			Server server) {

		try {
			user = user.toUpperCase();
			I2B2User i2b2User = new I2B2User(user);
			DriverManager.setLoginTimeout(2);
			connect = server.getConnection();
			
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement
					.executeQuery("select * from (select entry_date from i2b2pm.pm_user_session where user_id=UPPER('"
							+ user
							+ "') order by entry_date desc) where rownum <=1");
			i2b2User.setLastLogin(null);
			while (resultSet.next()) {
				i2b2User.setLastLogin(resultSet.getTimestamp("entry_date"));
			}
			i2b2User.setLastQuery(null);
			resultSet = statement
					.executeQuery("select *from i2b2pm.pm_user_data where user_id='"
							+ user + "'");
			i2b2User.setFullname("");
			i2b2User.setEmail("");
			i2b2User.setStatus("");

			while (resultSet.next()) {
				i2b2User.setFullname(resultSet.getString("full_name"));
				i2b2User.setEmail(resultSet.getString("email"));
				i2b2User.setStatus(resultSet.getString("status_cd"));
			}

			connect.close();
			return i2b2User;
		} catch (SQLException e) {
			MessageDialog.openError(Application.getShell(), "Error", e.getMessage());
			System.err.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public static boolean getManager(Server server, String username,
			String project) {
		try {
			username = username.toUpperCase();
			project = project.toUpperCase();
			DriverManager.setLoginTimeout(2);
			connect = server.getConnection();

			statement = connect.createStatement();
			connect.setAutoCommit(true);
			String sql = "select user_role_cd from i2b2pm.pm_project_user_roles where user_id=UPPER('"
					+ username
					+ "') and project_id=UPPER('"
					+ project
					+ "') and user_role_cd='MANAGER'";
			resultSet = statement.executeQuery(sql);
			String admin = "";
			while (resultSet.next()) {
				admin = resultSet.getString("user_role_cd");
			}
			connect.close();
			if (admin.toLowerCase().equals("manager")) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static ResultSet getPreview(String table, String schema,
			Server server) {
		ResultSet resultSet = null;
		try {
			DriverManager.setLoginTimeout(2);
			connect = server.getConnection();

			statement = connect.createStatement();

			if (server.getDatabaseType().equalsIgnoreCase("oracle")) {
				resultSet = statement.executeQuery("select * from " + schema + "."
						+ table + " where rownum <= 5");
			}
			else if (server.getDatabaseType().equalsIgnoreCase("mysql")) {
				resultSet = statement.executeQuery("select * from " + schema+"."+table);// + " where rownum <= 5");
			}
			else if (server.getDatabaseType().equalsIgnoreCase("mssql")){
				ServerTable currentTable = getTableMap().get(table);
				resultSet = statement.executeQuery("select * from " + currentTable.getDatabaseUser()+"."+currentTable.getDatabaseSchema()+"."+currentTable.getName());// + " where rownum <= 5");
			}
			else if (server.getDatabaseType().equalsIgnoreCase("postgres")){
				System.out.println("POSTGRES");
				ServerTable currentTable = getTableMap().get(table);
				resultSet = statement.executeQuery("select * from " + currentTable.getDatabaseUser()+"."+currentTable.getName());// + " where rownum <= 5");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	public static HashMap<String, Server> getSourceServers() {
		return importDBServers;
	}

	/**
	 * @return the tableMap
	 */
	public static HashMap<String, ServerTable> getTableMap() {
		return tableMap;
	}

	/**
	 * Returns all Tables or Views from a User. Usable for ImportDB schemas.
	 * 
	 * @param user
	 * @return ServerTables
	 */
	public static List<ServerTable> getTables(I2b2Project user) {
		try {
			tableMap = new HashMap<String, ServerTable>();
			File properties = FileHandler.getBundleFile("/cfg/Default.properties");

			defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));
			Server server = user.getServer();
			DriverManager.setLoginTimeout(2);
			connect = server.getConnection();
			List<ServerTable> tables = new LinkedList<ServerTable>();
			statement = connect.createStatement();
			if(user.getServer().getDatabaseType().equalsIgnoreCase("oracle")) {
				if (user.getServer().getUser().toLowerCase().equals("system")) {
					resultSet = statement
							.executeQuery("select table_name,temporary,secondary from dba_tables where owner='"
									+ user.getName() + "'");
					while (resultSet.next()) {
						Boolean temp = resultSet.getString("temporary").equals("Y");
						Boolean secondary = resultSet.getString("secondary")
								.equals("Y");
						String table = resultSet.getString("table_name");

						if (defaultProps.getProperty("hideTemp").equals("true")) {
							if (!temp && !secondary) {
								ServerTable newTable = new ServerTable(server,
										user.getName(), table);
								tables.add(newTable);
							}
						} else {
							ServerTable newTable = new ServerTable(server,
									user.getName(), table);
							tables.add(newTable);
						}

					}
					resultSet = statement
							.executeQuery("select view_name from dba_views where owner ='"
									+ user.getName() + "'");
					while (resultSet.next()) {
						String table = resultSet.getString("view_name");
						ServerTable newTable = new ServerTable(server,
								user.getName(), table);
						tables.add(newTable);
					}
					connect.close();
					return tables;
				} else {
					resultSet = statement
							.executeQuery("select table_name, temporary, secondary from user_tables");

					while (resultSet.next()) {
						String table = resultSet.getString("table_name");
						Boolean temp = resultSet.getString("temporary").equals("Y");
						Boolean secondary = resultSet.getString("secondary")
								.equals("Y");

						if (defaultProps.getProperty("hideTemp").equals("true")) {
							if (!temp && !secondary) {
								ServerTable newTable = new ServerTable(server,
										user.getName(), table);
								tables.add(newTable);
							}
						} else {
							ServerTable newTable = new ServerTable(server,
									user.getName(), table);
							tables.add(newTable);
						}
					}

					resultSet = statement
							.executeQuery("select view_name from user_views");
					while (resultSet.next()) {
						String table = resultSet.getString("view_name");
						ServerTable newTable = new ServerTable(server,
								user.getName(), table);
						tables.add(newTable);
					}
				}
			}
			else if (user.getServer().getDatabaseType().equalsIgnoreCase("mysql")) {
				resultSet = statement
						.executeQuery("use " + user.getName());
				resultSet = statement
						.executeQuery("show tables");
				while (resultSet.next()) {
					String table = resultSet.getString(1);
					ServerTable newTable = new ServerTable(server,
							user.getName(), table);
					tables.add(newTable);
				}
			}
			else if(user.getServer().getDatabaseType().equalsIgnoreCase("mssql")) {
				statement.execute("use " + user.getName());
				resultSet = statement
						.executeQuery("select * from information_schema.tables;");
				while (resultSet.next()) {
					String table = resultSet.getString("table_name");
					String schema = resultSet.getString("table_schema");
					ServerTable newTable = new ServerTable(server,
							user.getName(), table);
					newTable.setDatabaseSchema(schema);
					tables.add(newTable);
					tableMap.put(table, newTable);
				}

				//					resultSet = statement
				//							.executeQuery("SELECT name FROM sysobjects WHERE xtype = 'v'");
				//					while (resultSet.next()) {
				//						String table = resultSet.getString("name");
				//						ServerTable newTable = new ServerTable(server,
				//								user.getName(), table);
				//						tables.add(newTable);
				//					}
			}
			else if(user.getServer().getDatabaseType().equalsIgnoreCase("postgres")) {
//				statement.execute("use " + user.getName());
				System.out.println("USER: " + user.getName());
				resultSet = statement
						.executeQuery("SELECT * FROM information_schema.tables where table_schema = '"+user.getName()+"'");
				while (resultSet.next()) {
					String table = resultSet.getString("table_name");
					String schema = resultSet.getString("table_schema");
					ServerTable newTable = new ServerTable(server,
							user.getName(), table);
					newTable.setDatabaseSchema(schema);
					tables.add(newTable);
					tableMap.put(table, newTable);
				}

				//					resultSet = statement
				//							.executeQuery("SELECT name FROM sysobjects WHERE xtype = 'v'");
				//					while (resultSet.next()) {
				//						String table = resultSet.getString("name");
				//						ServerTable newTable = new ServerTable(server,
				//								user.getName(), table);
				//						tables.add(newTable);
				//					}
			}
			connect.close();
			return tables;


			// return tables;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns all Servers stored
	 * 
	 * @return
	 */
	public static HashMap<String, Server> getTargetServers() {
		return servers;
	}

	public static HashMap<String,String> getUserServer(){
		return userServer;
	}

	/**
	 * Returns DBImport Users
	 * 
	 * @param server
	 * @return users form DBImport
	 */
	public static List<I2b2Project> getUsersSourceServer(Server server) {
		try {
			DriverManager.setLoginTimeout(1);
			connect = server.getConnection();

			statement = connect.createStatement();
			List<I2b2Project> userList = new LinkedList<I2b2Project>();

			if (server.getDatabaseType().equalsIgnoreCase("oracle")) {
				if (server.getUser().toLowerCase().equals("system")) {
					// Result set get the result of the SQL query
					resultSet = statement
							.executeQuery("select username from all_users");

					while (resultSet.next()) {
						String user = resultSet.getString("username");
						I2b2Project newUser = new I2b2Project(user, server);
						userList.add(newUser);
					}
				} else {
					I2b2Project newUser = new I2b2Project(server.getUser(), server);
					userList.add(newUser);
				}
			}
			else if (server.getDatabaseType().equalsIgnoreCase("mysql")){

				resultSet = statement
						.executeQuery("show databases");

				while (resultSet.next()) {
					String user = resultSet.getString("Database");
					I2b2Project newUser = new I2b2Project(user, server);
					userList.add(newUser);
				}
			}
			else if (server.getDatabaseType().equalsIgnoreCase("mssql")){

				//SELECT * FROM sys.databases
				resultSet = statement
						.executeQuery("SELECT * FROM sys.databases");

				while (resultSet.next()) {
					String user = resultSet.getString("name");
					I2b2Project newUser = new I2b2Project(user, server);
					userList.add(newUser);
				}
				//					User newUser = new User(server.getUser(), server);
				//					userList.add(newUser);
			}
			else if (server.getDatabaseType().equalsIgnoreCase("postgres")){
//TODO
				//SELECT * FROM sys.databases
				resultSet = statement
						.executeQuery("select schema_name from information_schema.schemata");
				while (resultSet.next()) {
					String user = resultSet.getString("schema_name");
					I2b2Project newUser = new I2b2Project(user, server);
					userList.add(newUser);
				}
				//					User newUser = new User(server.getUser(), server);
				//					userList.add(newUser);
			}

			connect.close();
			return userList;
		} catch (SQLException e) {
			MessageDialog.openError(Application.getShell(), "Error", e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * returns all i2b2 projects
	 * 
	 * @param server
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static HashSet<I2b2Project> getUsersTargetServer(Server server) {
		try {
			DriverManager.setLoginTimeout(2);
			connect = server.getConnection();

			if (connect!=null) {
				statement = connect.createStatement();
				// Result set get the result of the SQL query
				HashSet<I2b2Project> users;
				if (server.getDatabaseType().equalsIgnoreCase("oracle")) {
					if (server.getUser().toLowerCase().equals("system")) {
						resultSet = statement
								.executeQuery("select username from all_users");
						users = getResultSet(resultSet, server);

					} else {
						users = new HashSet<I2b2Project>();
						users.add(new I2b2Project(server.getUser(),server));
						userServer.put(server.getUser(), server.getName());
					}
				}
				else if (server.getDatabaseType().equalsIgnoreCase("mysql")) {
					resultSet = statement
							.executeQuery("show databases");

					users = getResultSet(resultSet, server);
				}
				else if (server.getWhType().equalsIgnoreCase("transmart")){
					users = new HashSet<I2b2Project>();
					users.add(new I2b2Project("i2b2metadata", server));
					users.add(new I2b2Project("i2b2demodata", server));
					userServer.put("i2b2demodata", server.getName());
					userServer.put("i2b2metadata", server.getName());
				}
				else if (server.getWhType().equalsIgnoreCase("i2b2") && server.getDatabaseType().equalsIgnoreCase("postgres")){
					System.out.println("POSTGRES");
					users = new HashSet<I2b2Project>();
					resultSet = statement
							.executeQuery("select schema_name from information_schema.schemata");
					users = getResultSet(resultSet, server);
				}
				else {
					users = new HashSet<I2b2Project>();
					users.add(new I2b2Project(server.getUser(), server));
				}
				connect.close();
				return users;
			}else {
				return null;
			}
		} catch (SQLException e) {
			MessageDialog.openError(Application.getShell(), "Error", e.getMessage());
			System.err.println(e.getMessage());
			e.printStackTrace();
			return null;
		}

	}

//	public static boolean isServer(String uniqueID) {
//
//			if (((servers.get(uniqueID) != null)
//					|| (importDBServers.get(uniqueID) != null))){
//				return true;
//			}
//			else 
//				return false;
////			else if (ServerList.getTargetServers().get(uniqueID).get
//	}

	public static void loadServersfromProps() throws BackingStoreException {
		File serverStorage = FileHandler.getBundleFile("/cfg/server");
		serverFile = serverStorage;

		servers = deserializeServer(serverFile);

		serverImportDBFile = FileHandler.getBundleFile("/cfg/serverImportDB");
		serverFile = serverStorage;
		importDBServers = deserializeServer(serverImportDBFile);
		if (importDBServers == null) {
			importDBServers = new HashMap<String, Server>();
		}
		if (servers == null) {
			servers = new HashMap<String, Server>();
		}
	}

	public static void removeAll() {
		File serverStorage = FileHandler.getBundleFile("/cfg/server");
		serverFile = serverStorage;

		String path = serverFile.getAbsolutePath();
		serverFile.delete();
		serverFile = new File(path);
	}

	public static void removeServer(Server server) {

		if (importDBServers.containsKey(server.getUniqueID())) {
			try {
				importDBServers.remove(server.getUniqueID());
				ObjectOutputStream os = new ObjectOutputStream(
						new FileOutputStream(serverImportDBFile));
				os.writeObject(importDBServers);
				os.flush();
				os.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (servers.containsKey(server.getUniqueID())) {
			try {
				servers.remove(server.getUniqueID());
				ObjectOutputStream os = new ObjectOutputStream(
						new FileOutputStream(serverFile));
				os.writeObject(servers);
				os.flush();
				os.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void removeServer(String serverUniqueID) {
		if (importDBServers.containsKey(serverUniqueID)) {
			try {
				importDBServers.remove(serverUniqueID);
				ObjectOutputStream os = new ObjectOutputStream(
						new FileOutputStream(serverImportDBFile));
				os.writeObject(importDBServers);
				os.flush();
				os.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (servers.containsKey(serverUniqueID)) {
			try {
				servers.remove(serverUniqueID);
				ObjectOutputStream os = new ObjectOutputStream(
						new FileOutputStream(serverFile));
				os.writeObject(servers);
				os.flush();
				os.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void removeUser(Server server, String username) {
		try {
			DriverManager.setLoginTimeout(2);
			connect = server.getConnection();

			statement = connect.createStatement();
			// Result set get the result of the SQL query

			resultSet = statement
					.executeQuery("delete from i2b2pm.pm_user_data where user_id='"
							+ username + "'");
			resultSet = statement
					.executeQuery("delete from i2b2pm.pm_user_session where user_id='"
							+ username + "'");
			resultSet = statement
					.executeQuery("delete from i2b2pm.pm_project_user_roles where user_id='"
							+ username + "'");

			connect.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void serializeServers() {
		try {
			ObjectOutputStream os = new ObjectOutputStream(
					new FileOutputStream(serverFile));

			os.writeObject(servers);
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setAdmin(String username, String project, Server server,
			boolean setAdmin) {
		try {
			DriverManager.setLoginTimeout(2);
			connect = server.getConnection();

			statement = connect.createStatement();
			connect.setAutoCommit(true);
			java.util.Date today = new java.util.Date();
			java.sql.Date date = new java.sql.Date(today.getTime());
			String sql;
			if (setAdmin) {
				sql = "insert into i2b2pm.pm_project_user_roles (project_id,user_id,user_role_cd,change_date,entry_date,changeby_char,status_cd) values ('"
						+ project
						+ "','"
						+ username
						+ "','ADMIN', TO_DATE('"
						+ date
						+ "','yyyy-mm-dd'), TO_DATE('"
						+ date
						+ "','yyyy-mm-dd'), 'IDRT', 'A')";
			} else {
				sql = "delete from i2b2pm.pm_project_user_roles where user_id='"
						+ username
						+ "' and project_id='"
						+ project
						+ "' and user_role_cd='ADMIN'";
			}
			resultSet = statement.executeQuery(sql);
			connect.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void setManager(String username, String project,
			Server server, boolean setManager) {
		try {
			DriverManager.setLoginTimeout(2);
			connect = server.getConnection();

			statement = connect.createStatement();
			connect.setAutoCommit(true);
			java.util.Date today = new java.util.Date();
			java.sql.Date date = new java.sql.Date(today.getTime());
			String sql;
			if (setManager) {
				sql = "insert into i2b2pm.pm_project_user_roles (project_id,user_id,user_role_cd,change_date,entry_date,changeby_char,status_cd) values ('"
						+ project
						+ "','"
						+ username
						+ "','MANAGER', TO_DATE('"
						+ date
						+ "','yyyy-mm-dd'), TO_DATE('"
						+ date
						+ "','yyyy-mm-dd'), 'IDRT', 'A')";
			} else {
				sql = "delete from i2b2pm.pm_project_user_roles where user_id='"
						+ username
						+ "' and project_id='"
						+ project
						+ "' and user_role_cd='MANAGER'";
			}
			resultSet = statement.executeQuery(sql);
			connect.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	//	private static ResultSet test(Server server) {
	//		try {
	//			DriverManager.setLoginTimeout(2);
	//			connect = server.getConnection();
	//			ResultSet resultSet = null;
	//			String sql = ("declare b varchar2(3500 byte); BEGIN   FOR emp IN  (    SELECT staging_path" +
	//					"    FROM I2B2IDRT.ioe_target_ontology where target_id = ?  )  LOOP " +
	//					"   select c_fullname into b from i2b2idrt.i2b2  where c_fullname = emp.staging_path;" +
	//					"    ? := b;  END LOOP;END;");
	//			CallableStatement  statement = connect.prepareCall(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);//prepareCall(sql);
	//
	//
	//			statement.setInt(1, 21);
	//			statement.setInt(2, 21);
	//			statement.registerOutParameter(1, Types.VARCHAR);
	//			statement.execute(sql);
	//
	//			System.out.println("RESULT: " + statement.getObject(1));
	//
	//			return resultSet;
	//		} catch (SQLException e) {
	//			e.printStackTrace();
	//		}
	//		return null;
	//	}

	public static void unAssignUserFromProject(String username, String project,
			Server server) {
		try {
			DriverManager.setLoginTimeout(2);
			connect = server.getConnection();

			statement = connect.createStatement();
			connect.setAutoCommit(true);
			String sql = "delete from i2b2pm.pm_project_user_roles where user_id='"
					+ username + "' and project_id='" + project + "'";
			resultSet = statement.executeQuery(sql);
			connect.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static HashSet<I2b2Project> getResultSet(ResultSet resultSet,
			Server server) throws SQLException {
		try {
			File properties = FileHandler.getBundleFile("/cfg/Default.properties");
			defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		HashSet<I2b2Project> users = new HashSet<I2b2Project>();
		if (server.getDatabaseType().equalsIgnoreCase("oracle")) {
			while (resultSet.next()) {
				String user = resultSet.getString("username");
				if (defaultProps.getProperty("filter").equals("true")) {
					if (user.startsWith("I2B2")
							&& !((user.equals("I2B2HIVE") || (user.equals("I2B2PM"))))) {
						users.add(new I2b2Project(user, server));
					}
				} else {
					users.add(new I2b2Project(user, server));
				}
			}
		}
		else if (server.getDatabaseType().equalsIgnoreCase("postgres") && server.getWhType().equalsIgnoreCase("i2b2")) {
			while (resultSet.next()) {
				String user = resultSet.getString("schema_name");
				if (defaultProps.getProperty("filter").equals("true")) {
					if (user.toLowerCase().startsWith("i2b2")
							&& !((user.toLowerCase().equals("i2b2hive") || (user.toLowerCase().equals("i2b2pm"))|| (user.toLowerCase().equals("i2b2imdata"))|| (user.toLowerCase().equals("i2b2workdata"))))) {
						users.add(new I2b2Project(user, server));
					}
				} else {
					users.add(new I2b2Project(user, server));
				}
			}
		}
		else if (server.getDatabaseType().equalsIgnoreCase("mysql")){
			while (resultSet.next()) {
				String user = resultSet.getString("Database");

				users.add(new I2b2Project(user,server));
			}
		}
		else {
			while (resultSet.next()) {
				String user = resultSet.getString("username");

				if (defaultProps.getProperty("filter").equals("true")) {
					if (user.startsWith("I2B2")
							&& !((user.equals("I2B2HIVE") || (user.equals("I2B2PM"))))) {
						users.add(new I2b2Project(user,server));
					}
				} else {
					users.add(new I2b2Project(user,server));
				}
			}	
		}
		for (I2b2Project user : users) {
			//			System.out.println("adding: " + user + " to " + server.getName());
			userServer.put(user.getName(), server.getName());
		}
		return users;
	}

}
