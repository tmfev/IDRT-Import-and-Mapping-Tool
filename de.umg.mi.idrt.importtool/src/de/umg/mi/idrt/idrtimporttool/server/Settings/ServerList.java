package de.umg.mi.idrt.idrtimporttool.server.Settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.prefs.BackingStoreException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;

import de.umg.mi.idrt.idrtimporttool.importidrt.Activator;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class ServerList {

	// final private transient static String password =
	// "jeLaengerJeBesserPasswort";
	// final private transient static byte [] salt = { (byte) 0xc9, (byte)
	// 0xc9,(byte) 0xc9,(byte) 0xc9,(byte) 0xc9,(byte) 0xc9,(byte) 0xc9,(byte)
	// 0xc9};
	final static int iterations = 3;
	// private static sun.misc.BASE64Encoder encoder = new
	// sun.misc.BASE64Encoder();
	// private static sun.misc.BASE64Decoder decoder = new
	// sun.misc.BASE64Decoder();
	private static Connection connect = null;
	private static HashMap<String, Server> servers = new HashMap<String, Server>();
	private static HashMap<String, Server> importDBServers = new HashMap<String, Server>();
	private static HashMap<String, ServerTable> tableMap;
	// private static Preferences prefsRoot = Preferences.userRoot();
	private static Statement statement = null;
	private static ResultSet resultSet = null;
	private static File serverFile;
	private static File serverImportDBFile;
	private static Properties defaultProps;
	private static String[] USERROLES = { "USER", "EDITOR", "DATA_PROT",
		"DATA_OBFSC", "DATA_LDS", "DATA_DEID", "DATA_AGG" };

	public static void addSourceServer(Server loadServer) {
		if (!servers.containsKey(loadServer.getUniqueID())) {
			//			System.out.println("put: " + loadServer.getUniqueID());
			importDBServers.put(loadServer.getUniqueID(), loadServer);
			try {
				// System.out.println("adding server: " +
				// serverFile.getAbsolutePath());

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

	public static void addServer(Server server) {
		try {

			if (!importDBServers.containsKey(server.getUniqueID())) {
				// System.out.println("adding server: " +
				// serverFile.getAbsolutePath());

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

		// Preferences myPrefs = prefsRoot.node(server.getUniqueID());
		//
		// myPrefs.put("ip", server.getIp());
		// myPrefs.put("port",server.getPort());
		// myPrefs.put("user",server.getUser());
		// myPrefs.put("password",server.getPassWord());
		// myPrefs.put("sid", server.getSID());

	}

	/**
	 * Initialisiert den Verschlüsselungsmechanismus
	 * 
	 * @param pass
	 *            char[]
	 * @param salt
	 *            byte[]
	 * @param iterations
	 *            int
	 * @throws SecurityException
	 */
	// public static void init (final char[] pass, final byte[] salt, final int
	// iterations) throws SecurityException {
	// try {
	// final PBEParameterSpec ps = new PBEParameterSpec(salt, 20);
	// final SecretKeyFactory kf =
	// SecretKeyFactory.getInstance("PBEWithMD5AndDES");
	// final SecretKey k = kf.generateSecret(new PBEKeySpec(pass));
	// encryptCipher = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");
	// encryptCipher.init (Cipher.ENCRYPT_MODE, k, ps);
	// decryptCipher = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");
	// decryptCipher.init (Cipher.DECRYPT_MODE, k, ps);
	// }
	// catch (Exception e) {
	// throw new SecurityException("Could not initialize CryptoLibrary: " +
	// e.getMessage());
	// }
	// }
	//
	// /**
	// * Verschlüsselt eine Zeichenkette
	// *
	// * @param str Description of the Parameter
	// * @return String the encrypted string.
	// * @exception SecurityException Description of the Exception
	// */
	// public synchronized static String encrypt(String str) throws
	// SecurityException {
	// try {
	// byte[] b = str.getBytes(charset);
	// byte[] enc = encryptCipher.doFinal(b);
	// return encoder.encode(enc);
	// }
	// catch (Exception e){
	// throw new SecurityException("Could not encrypt: " + e.getMessage());
	// }
	//
	// }

	// /**
	// * Entschlüsselt eine Zeichenkette, welche mit der Methode encrypt
	// * verschlüsselt wurde.
	// *
	// * @param str Description of the Parameter
	// * @return String the encrypted string.
	// * @exception SecurityException Description of the Exception
	// */
	// public synchronized static String decrypt(String str) throws
	// SecurityException {
	// try {
	// byte[] dec = decoder.decodeBuffer(str);
	// byte[] b = decryptCipher.doFinal(dec);
	// return new String(b, charset);
	// }
	// catch (Exception e) {
	// throw new SecurityException("Could not decrypt: " + e.getMessage());
	// }
	// }
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
			@SuppressWarnings("unchecked")
			HashMap<String, Server> list = (HashMap<String, Server>) is
			.readObject();
			is.close();
			return list;

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HashMap<String, Server> getSourceServers() {
		return importDBServers;
	}

	public static ResultSet getPreview(String table, String schema,
			Server server) {
		ResultSet resultSet = null;
		//		System.out.println("preview");
		try {
			DriverManager.setLoginTimeout(2);
			connect = server.getConnection();

			statement = connect.createStatement();
			
			if (server.getDatabaseType().equalsIgnoreCase("oracle")) {
				resultSet = statement.executeQuery("select * from " + schema + "."
						+ table + " where rownum <= 5");
			}
			else if (server.getDatabaseType().equalsIgnoreCase("mysql")) {
//				statement.executeQuery("use " + schema);
				System.out.println("mysql " + table + " " + schema);
				resultSet = statement.executeQuery("select * from " + schema+"."+table);// + " where rownum <= 5");
				
			}
			else if (server.getDatabaseType().equalsIgnoreCase("mssql")){
				ServerTable currentTable = getTableMap().get(table);
				System.out.println("select * from " + currentTable.getDatabaseUser()+"."+currentTable.getDatabaseSchema()+"."+currentTable.getName());
				resultSet = statement.executeQuery("select * from " + currentTable.getDatabaseUser()+"."+currentTable.getDatabaseSchema()+"."+currentTable.getName());// + " where rownum <= 5");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
		 * Returns all Tables or Views from a User. Usable for ImportDB schemas.
		 * 
		 * @param user
		 * @return ServerTables
		 */
		public static List<ServerTable> getTables(User user) {
			try {
				tableMap = new HashMap<String, ServerTable>();
				Bundle bundle = Activator.getDefault().getBundle();
				Path propPath = new Path("/cfg/Default.properties"); //$NON-NLS-1$
				URL url = FileLocator.find(bundle, propPath, Collections.EMPTY_MAP);
	
				URL fileUrl = FileLocator.toFileURL(url);
				File properties = new File(fileUrl.getPath());
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
	
							// System.out.println("temp? + " + temp + " name: " + table
							// + " -- " + defaultProps.getProperty("hideTemp"));
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
						System.out.println("MYSQL TABLE: " + table);
						ServerTable newTable = new ServerTable(server,
								user.getName(), table);
						tables.add(newTable);
					}
				}
				else if(user.getServer().getDatabaseType().equalsIgnoreCase("mssql")) {
					System.out.println("MSSQL: " + user.getName());
	//				resultSet = statement.executeQuery("use " + user.getName());
					statement.execute("use " + user.getName());
	//				connect = server.getConnection();
	//				statement = connect.createStatement();
						resultSet = statement
								.executeQuery("select * from information_schema.tables;");
	//					System.out.println(resultSet.getWarnings().toString());
						while (resultSet.next()) {
							String table = resultSet.getString("table_name");
							String schema = resultSet.getString("table_schema");
							// System.out.println("temp? + " + temp + " name: " + table
							// + " -- " + defaultProps.getProperty("hideTemp"));
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
	 * @return the tableMap
	 */
	public static HashMap<String, ServerTable> getTableMap() {
		return tableMap;
	}

	/**
		 * Returns DBImport Users
		 * 
		 * @param server
		 * @return users form DBImport
		 */
		public static List<User> getUsersSourceServer(Server server) {
			try {
				DriverManager.setLoginTimeout(1);
				connect = server.getConnection();
	
				statement = connect.createStatement();
				List<User> userList = new LinkedList<User>();
	
				if (server.getDatabaseType().equalsIgnoreCase("oracle")) {
					if (server.getUser().toLowerCase().equals("system")) {
						// Result set get the result of the SQL query
						resultSet = statement
								.executeQuery("select username from all_users");
	
						while (resultSet.next()) {
							String user = resultSet.getString("username");
							User newUser = new User(user, server);
							userList.add(newUser);
						}
					} else {
						User newUser = new User(server.getUser(), server);
						userList.add(newUser);
					}
				}
				else if (server.getDatabaseType().equalsIgnoreCase("mysql")){
	
					resultSet = statement
							.executeQuery("show databases");
	
					while (resultSet.next()) {
						String user = resultSet.getString("Database");
						User newUser = new User(user, server);
						userList.add(newUser);
					}
				}
				else if (server.getDatabaseType().equalsIgnoreCase("mssql")){
	
						//SELECT * FROM sys.databases
						resultSet = statement
								.executeQuery("SELECT * FROM sys.databases");
	
						while (resultSet.next()) {
							String user = resultSet.getString("name");
							User newUser = new User(user, server);
							userList.add(newUser);
						}
	//					User newUser = new User(server.getUser(), server);
	//					userList.add(newUser);
				}
	
				connect.close();
				return userList;
			} catch (SQLException e) {
				MessageDialog.openError(Display.getDefault()
						.getActiveShell(), "Error", e.getMessage());
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
	public static HashSet<String> getUsersTargetServer(Server server) {
		try {
			DriverManager.setLoginTimeout(2);
			connect = server.getConnection();
	
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			HashSet<String> users;
			if (server.getDatabaseType().equalsIgnoreCase("oracle")) {
				if (server.getUser().toLowerCase().equals("system")) {
					resultSet = statement
							.executeQuery("select username from all_users");
	
					users = getResultSet(resultSet, server);
				} else {
					users = new HashSet<String>();
					users.add(server.getUser());
				}
			}
			else if (server.getDatabaseType().equalsIgnoreCase("mysql")) {
				resultSet = statement
						.executeQuery("show databases");
	
				users = getResultSet(resultSet, server);
			}
			else {
				users = new HashSet<String>();
				users.add(server.getUser());
			}
			connect.close();
			return users;
		} catch (SQLException e) {
			MessageDialog.openError(Display.getDefault()
					.getActiveShell(), "Error", e.getMessage());
			System.err.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private static HashSet<String> getResultSet(ResultSet resultSet,
			Server server) throws SQLException {
		try {
			Bundle bundle = Activator.getDefault().getBundle();
			Path propPath = new Path("/cfg/Default.properties"); //$NON-NLS-1$
			URL url = FileLocator.find(bundle, propPath, Collections.EMPTY_MAP);

			URL fileUrl = FileLocator.toFileURL(url);
			File properties = new File(fileUrl.getPath());
			defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		HashSet<String> users = new HashSet<String>();
		if (server.getDatabaseType().equalsIgnoreCase("oracle")) {
			while (resultSet.next()) {
				String user = resultSet.getString("username");

				if (defaultProps.getProperty("filter").equals("true")) {
					if (user.startsWith("I2B2")
							&& !((user.equals("I2B2HIVE") || (user.equals("I2B2PM"))))) {
						users.add(user);
					}
				} else {
					users.add(user);
				}
			}
		}
		else if (server.getDatabaseType().equalsIgnoreCase("mysql")){
			while (resultSet.next()) {
				String user = resultSet.getString("Database");

				//				if (defaultProps.getProperty("filter").equals("true")) {
				//					if (user.startsWith("I2B2")
				//							&& !((user.equals("I2B2HIVE") || (user.equals("I2B2PM"))))) {
				users.add(user);
				//					}
				//				} else {
				//					users.add(user);
				//				}
			}
		}
		else {
			while (resultSet.next()) {
				String user = resultSet.getString("username");

				if (defaultProps.getProperty("filter").equals("true")) {
					if (user.startsWith("I2B2")
							&& !((user.equals("I2B2HIVE") || (user.equals("I2B2PM"))))) {
						users.add(user);
					}
				} else {
					users.add(user);
				}
			}	
		}
		return users;
	}

	/**
	 * Returns all Servers stored
	 * 
	 * @return
	 */
	public static HashMap<String, Server> getTargetServers() {
		return servers;
	}

	public static boolean getManager(Server server, String username,
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
					+ "' and user_role_cd='MANAGER'";
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

	public static LinkedList<String> getAssignedUsersFromProject(Server server,
			String project) {
		try {
			System.out.println("PROJECT: " + project);
			LinkedList<String> users = new LinkedList<String>();
			if (!project.isEmpty()) {

				DriverManager.setLoginTimeout(2);
				connect = server.getConnection();

				statement = connect.createStatement();
				// Result set get the result of the SQL query

				resultSet = statement
						.executeQuery("select distinct user_id from i2b2pm.pm_project_user_roles where project_id='"
								+ project + "' order by user_id asc");

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
			MessageDialog.openError(Display.getDefault()
					.getActiveShell(), "Error", e.getMessage());
			System.err.println(e.getMessage());
			e.printStackTrace();
			return null;
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
						users.add(user);
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
			MessageDialog.openError(Display.getDefault()
					.getActiveShell(), "Error", e.getMessage());
			System.err.println(e.getMessage());
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openError(Display.getDefault()
					.getActiveShell(), "Error", e.getMessage());
			System.err.println(e.getMessage());
			LinkedList<String> users = new LinkedList<String>();
			return users;
		}
		return null;
	}

	public static I2B2User getI2B2UserData(String user, String project,
			Server server) {

		try {
			I2B2User i2b2User = new I2B2User(user);
			DriverManager.setLoginTimeout(2);
			connect = server.getConnection();

			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement
					.executeQuery("select * from (select entry_date from i2b2pm.pm_user_session where user_id='"
							+ user
							+ "' order by entry_date desc) where rownum <=1");
			i2b2User.setLastLogin(null);
			while (resultSet.next()) {
				i2b2User.setLastLogin(resultSet.getTimestamp("entry_date"));
			}
			i2b2User.setLastQuery(null);
//			if (project != null) {
//				if (!project.toLowerCase().startsWith("i2b2")) {
//					project = "i2b2" + project;
//				}
//				resultSet = statement
//						.executeQuery("select * from (select create_date from "
//								+ project
//								+ ".qt_query_master where user_id='"
//								+ user
//								+ "' order by create_date desc) where rownum <=1");
//
//				while (resultSet.next()) {
//					i2b2User.setLastQuery(resultSet.getTimestamp("create_date"));
//				}
//			}
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
			MessageDialog.openError(Display.getDefault()
					.getActiveShell(), "Error", e.getMessage());
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
				System.out.println("not system");
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
			MessageDialog.openError(Display.getDefault()
					.getActiveShell(), "Error", e.getMessage());
			System.err.println(e.getMessage());
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			MessageDialog.openError(Display.getDefault()
					.getActiveShell(), "Error", e.getMessage());
			e.printStackTrace();
			HashSet<String> users = new HashSet<String>();
			return users;
		}
		return null;
	}



	// public static void storePreferences(String fileName){
	// try {
	// File file = new File(fileName);
	// prefsRoot.exportSubtree(new FileOutputStream(file));
	// // prefsRoot.exportNode(new FileOutputStream(file));
	// System.out.println(file.getAbsolutePath());
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (BackingStoreException e) {
	// e.printStackTrace();
	// }
	// }

	public static boolean isServer(String uniqueID) {
		if ((servers.get(uniqueID) != null)
				|| (importDBServers.get(uniqueID) != null)) {
			return true;
		} else {
			return false;
		}
	}

	public static void loadServersfromProps() throws BackingStoreException {
		try {
			// init(password.toCharArray(), salt, iterations);

			Bundle bundle = Activator.getDefault().getBundle();
			Path propPath = new Path("/cfg/server"); //$NON-NLS-1$
			URL url = FileLocator.find(bundle, propPath, Collections.EMPTY_MAP);
			URL fileUrl = null;
			if (url != null) {
				fileUrl = FileLocator.toFileURL(url);
			}
			File serverStorage = null;
			if (fileUrl != null) {
				serverStorage = new File(fileUrl.getPath());
				//				System.out.println("if1");
			} else {
				//				System.out.println("else1");
				serverStorage = new File(System.getProperty("user.home")
						+ "/idrt/cfg/server");
			}
			serverFile = serverStorage;

			//			System.out.println("bla " + serverFile.getAbsolutePath());
			servers = deserializeServer(serverFile);

			propPath = new Path("/cfg/serverImportDB"); //$NON-NLS-1$
			url = FileLocator.find(bundle, propPath, Collections.EMPTY_MAP);
			fileUrl = null;
			if (url != null) {
				fileUrl = FileLocator.toFileURL(url);
			}
			serverImportDBFile = null;
			if (fileUrl != null) {
				serverImportDBFile = new File(fileUrl.getPath());
				//				System.out.println("if importdb");
			} else {
				//				System.out.println("else importdb");
				serverImportDBFile = new File(System.getProperty("user.home")
						+ "/idrt/cfg/serverImportDB");
			}
			serverFile = serverStorage;
			importDBServers = deserializeServer(serverImportDBFile);
			if (importDBServers == null) {
				importDBServers = new HashMap<String, Server>();
			}
			if (servers == null) {
				servers = new HashMap<String, Server>();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * String [] childrenNames = prefsRoot.childrenNames(); boolean isServer
		 * = false; for (int i = 0; i < childrenNames.length; i++) {
		 * System.out.println("loading: " + childrenNames[i]);
		 * 
		 * Preferences serverPrefs = prefsRoot.node(childrenNames[i]);
		 * 
		 * 
		 * if (childrenNames[i].startsWith(Server.getIMPORTDBPREFIX())){
		 * System.out.println("importdb server loaded"); Server loadServer = new
		 * Server(childrenNames[i], serverPrefs.get("ip", null),
		 * serverPrefs.get("port", null), serverPrefs.get("user",
		 * null),serverPrefs.get("password", null), serverPrefs.get("sid",
		 * null)); addImportDBServer(loadServer); } else { String [] serverKeys
		 * = serverPrefs.keys(); for (int j = 0; j<serverKeys.length;j++){
		 * System.out.println(serverKeys[j]); if (serverKeys[j].equals("ip")){
		 * isServer=true; break; } }
		 * 
		 * if(isServer){ Server loadServer = new Server(childrenNames[i],
		 * serverPrefs.get("ip", null), serverPrefs.get("port", null),
		 * serverPrefs.get("user", null),serverPrefs.get("password", null),
		 * serverPrefs.get("sid", null)); addServer(loadServer); } else
		 * System.out.println("no server!"); isServer = false; } }
		 */
	}

	public static void removeAll() {
		try {
			Bundle bundle = Activator.getDefault().getBundle();
			Path propPath = new Path("/cfg/server"); //$NON-NLS-1$
			URL url = FileLocator.find(bundle, propPath, Collections.EMPTY_MAP);
			URL fileUrl = null;
			if (url != null) {
				fileUrl = FileLocator.toFileURL(url);
			}
			File serverStorage = null;
			if (fileUrl != null) {
				serverStorage = new File(fileUrl.getPath());
				//				System.out.println("if");
			} else {
				//				System.out.println("else");
				serverStorage = new File(System.getProperty("user.home")
						+ "/idrt/cfg/server");
			}
			serverFile = serverStorage;

			String path = serverFile.getAbsolutePath();
			//			System.out.println("serverpath: " + path);
			serverFile.delete();
			serverFile = new File(path);

			// prefsRoot.clear();
			// String [] childrenNames = prefsRoot.childrenNames();
			//
			// for (int i = 0; i < childrenNames.length; i++) {
			// Preferences removePrefs = prefsRoot.node(childrenNames[i]);
			// removePrefs.removeNode();
			// }
			// servers.clear();
			// importDBServers.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void removeServer(Server server) {

		if (importDBServers.containsKey(server.getUniqueID())) {
			//			System.out.println("startswith removed");
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
				//				System.out.println("removing importdb: " + serverUniqueID);
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
				//				System.out.println("removing: " + serverUniqueID);
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

}
