package de.umg.mi.idrt.idrtimporttool.server.Settings;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.umg.mi.idrt.idrtimporttool.importidrt.Application;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.PasswordDialog;
import de.umg.mi.idrt.importtool.views.ServerView;
import de.umi.mi.passwordcrypt.PasswordCrypt;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class Server implements Serializable {

//	private static String ORACLEDRIVER = "oracle.jdbc.driver.OracleDriver";
	private static String ORACLEDRIVER = "oracle.jdbc.OracleDriver";
	
	private static String MSSQLDRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static String MYSQLDRIVER = "com.mysql.jdbc.Driver";
	private static String POSTGRESDRIVER = "org.postgresql.Driver";
	
	//con=
	private static String error;
	private static String[] comboItems = {"Oracle","Postgres","MSSQL","MySQL"};
	private static final long serialVersionUID = 1L;

	public static String[] getComboItems() {
		return comboItems;
	}
	public static String getError() {
		return error;
	}
	public static void setError(String error) {
		Server.error = error;
	}
	private transient String notStoredPassword;
	private String uniqueID;
	private String ip;
	private String port;
	private String user;
	private String password;
	private String sid;
	private String schema;
	private String table;
	private String patients;

	private String concepts;

	private String projectName;

	private String databaseType;

	private boolean savePassword;

	private boolean useWinAuth;
	/**
	 * @param currentServer2
	 */
	public Server(Server currentServer2) {
		this.uniqueID = currentServer2.getUniqueID();
		this.ip = currentServer2.getIp();
		this.port = currentServer2.getPort();
		this.user = currentServer2.getUser();
		this.sid =currentServer2.getSID();
		this.schema = currentServer2.getSchema();
		this.table = currentServer2.getTable();
		this.databaseType = currentServer2.getDatabaseType();
		this.setUseWinAuth(currentServer2.isUseWinAuth());
		this.setSavePassword(currentServer2.isSavePassword());
		if (savePassword)
			setPassword(currentServer2.getPassword());
		else {
			setPassword("");
			setNotStoredPassword(currentServer2.getNotStoredPassword());
		}
	}

	public Server(String uniqueID, String ip, String port, String user,
			String passWord, String sid, String databaseType, boolean useWinAuth, boolean savePassword) {
		this.uniqueID = uniqueID;
		this.ip = ip;

		this.port = port;
		this.user = user;
		this.sid = sid;
		this.databaseType = databaseType;
		this.setUseWinAuth(useWinAuth);
		this.setSavePassword(savePassword);
		if (savePassword)
			setPassword(passWord);
		else {
			setPassword("");
			setNotStoredPassword(passWord);
		}
	}


	//	private Server(String uniqueID, String ip, String port, String user,
	//			String passWord, String sid, String databaseType,boolean useWinAuth, String schema) {
	//		this.uniqueID = uniqueID;
	//		this.ip = ip;
	//		setPassword(passWord);
	//		this.port = port;
	//		this.user = user;
	//		this.sid = sid;
	//		this.schema = schema;
	//		this.databaseType=databaseType;
	//		this.setUseWinAuth(useWinAuth);
	//	}

	//	public void getOntology(String user) {
	//		//TODO
	//		try {
	//			System.out.println("user: " + user);
	//			ontology = new LinkedHashSet<OntologyItem>();
	//			DriverManager.setLoginTimeout(2);
	//			Connection connect = getConnection();
	//			Statement statement = connect.createStatement();
	//			ResultSet resultSet = statement
	//					.executeQuery("select * from " + user
	//							+ ".i2b2 order by c_hlevel, c_fullname asc");
	//			
	//			long time = System.currentTimeMillis();
	//			while (resultSet.next()) {
	//				OntologyItem item = new OntologyItem(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),
	//						resultSet.getString(4),resultSet.getString(5),resultSet.getInt(6),resultSet.getString(7),
	//						resultSet.getString(8),resultSet.getString(9),resultSet.getString(10),resultSet.getString(11),
	//						resultSet.getString(12),resultSet.getString(13),resultSet.getString(14),resultSet.getString(15),
	//						resultSet.getString(16),resultSet.getString(17),resultSet.getDate(18),resultSet.getDate(19),
	//						resultSet.getDate(20),resultSet.getString(21),resultSet.getString(22),resultSet.getString(23),
	//						resultSet.getString(24),resultSet.getString(25));
	//				ontology.add(item);
	//				if (ontology.size()%1000==0) {
	//					System.out.println(System.currentTimeMillis()-time+ "ms "+ontology.size() + " items in array");
	//					time = System.currentTimeMillis();
	//				}
	//			}			
	//			connect.close();
	//		} catch (SQLException e) {
	//			e.printStackTrace();
	//			System.err.println("Not a valid i2b2 project: No Concepts found!");
	//		}
	//	}

	/**
	 * @return the ontology
	 */
	//	public static LinkedHashSet<OntologyItem> getOntology() {
	//		return ontology;
	//	}

	public Server(String uniqueID, String ip, String port, String user,
			String passWord, String sid,String databaseType, String schema, boolean useWinAuth, String table) {
		this.uniqueID = uniqueID;
		this.ip = ip;
		setPassword(passWord);
		this.port = port;
		this.user = user;
		this.sid = sid;
		System.out.println("NEW SERVER: " + schema);
		this.schema = schema;
		this.table = table;
		this.databaseType = databaseType;
		this.setUseWinAuth(useWinAuth);
	}

	public boolean checkDatabaseType() {

		if (this.databaseType.equals("oracle")) {
			return true;
		}
		else {
			return false;
		}
	}

	public String getConcepts(String user) {
		try {
			DriverManager.setLoginTimeout(2);
			Connection connect = getConnection();
			Statement statement = connect.createStatement();
			if (!user.toLowerCase().startsWith("i2b2")) {
				user = "i2b2" + user;
			}
			ResultSet resultSet = statement
					.executeQuery("select count(*) as concepts from " + user
							+ ".observation_fact");
			while (resultSet.next()) {
				concepts = resultSet.getString("concepts");
			}
			connect.close();
		} catch (SQLException e) {
			System.err.println("Not a valid i2b2 project: No Concepts found!");
			concepts = "";
			return concepts;
		}
		return concepts;
	}
	/**
	 * @return Connection for JDBC
	 */
	public Connection getConnection() {
		try {
			DriverManager.setLoginTimeout(2);
			if(this.getDatabaseType() == null) {
				Class.forName(ORACLEDRIVER);
				return DriverManager.getConnection("jdbc:oracle:thin:@" + this.getIp() + ":" + this.getPort() + ":"
						+ this.getSID(), this.getUser(), this.getPassword());
			}
			else if (this.getDatabaseType().equalsIgnoreCase("oracle")) {
				Class.forName(ORACLEDRIVER);
				return DriverManager.getConnection("jdbc:oracle:thin:@" + getIp() + ":" + getPort() + ":"
						+ getSID(), getUser(), getPassword());
			}
			else if (this.getDatabaseType().equalsIgnoreCase("mysql")) {
				Class.forName(MYSQLDRIVER);
				return DriverManager.getConnection("jdbc:mysql://" + getIp() + ":" + getPort() + "/"
						+ getSID(), getUser(), getPassword());	
			}
			else if (this.getDatabaseType().equalsIgnoreCase("mssql")) {
				Class.forName(MSSQLDRIVER);
				if (getPort().length()>0) {
					if (!isUseWinAuth()) {
						return DriverManager.getConnection("jdbc:sqlserver://" + getIp() + ":" 
								+ getPort(), getUser(), getPassword());
					}
					else {
						return DriverManager.getConnection("jdbc:sqlserver://"+getIp()+ ":" 
								+ getPort()+";databaseName="+getSID()+";integratedSecurity=true");

					}
				}
				else {
					if (!isUseWinAuth()) {
						return DriverManager.getConnection("jdbc:sqlserver://" + getIp(), getUser(), getPassword());
					}
					else {
						return DriverManager.getConnection("jdbc:sqlserver://"+getIp()
								+";databaseName="+this.getSID()+";integratedSecurity=true");

					}

				}
			}
			else if (this.getDatabaseType().equalsIgnoreCase("postgres")) {
				Class.forName(POSTGRESDRIVER);
				return DriverManager.getConnection("jdbc:postgresql://" + getIp() + ":" 
						+ getPort()+"/" + getSID(), getUser(), getPassword());
			}
			else {
				return null;
			}
		} catch (SQLException e) {
			setError(e.getMessage());
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getDatabaseType() {
		return databaseType;
	}

	/**
	 * @param ontology the ontology to set
	 */
	//	public static void setOntology(LinkedHashSet<OntologyItem> ontology) {
	//		Server.ontology = ontology;
	//	}

	public String getIp() {
		return ip;
	}

	public String getName() {
		return uniqueID;
	}

	public String getNotStoredPassword() {
		return notStoredPassword;
	}

	public String getPassword() {
		ServerList.serializeServers();
		if (this.isSavePassword())
			return PasswordCrypt.decrypt(password);
		else if (getNotStoredPassword()==null || getNotStoredPassword().isEmpty()) {
			PasswordDialog dialog = new PasswordDialog(Application.getShell());
			dialog.open();
			if (dialog.getSavePassword()) {
				setSavePassword(true);
				setPassword(dialog.getPassword());
				return dialog.getPassword();
			}
			else {
				setSavePassword(false);
				setNotStoredPassword(dialog.getPassword());
			}
		}
		if  (getNotStoredPassword()==null) {
			throw new RuntimeException("PASSWORD NULL");
		}
		return this.getNotStoredPassword();
	}

	public String getPatients(String user) {
		try {
			patients="0";
			Connection connect = getConnection();
			DriverManager.setLoginTimeout(2);
			Statement statement = connect.createStatement();
			if (!user.toLowerCase().startsWith("i2b2")) {
				user = "i2b2" + user;
			}
			ResultSet resultSet = statement
					.executeQuery("select count(*) as patients from " + user
							+ ".patient_dimension");
			while (resultSet.next()) {
				patients = resultSet.getString("patients");
			}
			connect.close();
		} catch (SQLException e) {
			//			e.printStackTrace();
			System.err.println("Not a valid i2b2 project: No Patients found!");
			patients = "";
			return patients;
		}
		return patients;
	}

	public String getPort() {
		return port;
	}

	public String getProjectName(String project) {
		try {
			Connection connect = getConnection();
			DriverManager.setLoginTimeout(2);
			Statement statement = connect.createStatement();
			ResultSet resultSet = statement
					.executeQuery("select project_name from i2b2pm.pm_project_data where project_id='"
							+ project + "'");
			while (resultSet.next()) {
				projectName = resultSet.getString("project_name");
			}
			connect.close();
			return projectName;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Not a valid i2b2 project: No Patients found!");
			projectName = "";
			return projectName;
		}
	}

	public String getSchema() {
		return ServerView.getCurrentSchema();
	}

	public String getSID() {
		return sid;
	}

	public String getTable() {
		return table;
	}

	public String getUniqueID() {
		return uniqueID;
	}

	public String getUser() {
		return user;
	}

	public boolean isSavePassword() {
		return savePassword;
	}

	public boolean isUseWinAuth() {
		return useWinAuth;
	}

	public void setConcecpts(String concecpts) {
		concepts = concecpts;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setNotStoredPassword(String notStoredPassword) {
		this.notStoredPassword = notStoredPassword;
	}

	public void setPassword(String password) {
		this.password = PasswordCrypt.encrypt(password);
	}

	public void setPatients(String patients) {
		this.patients = patients;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setSavePassword(boolean savePassword) {
		this.savePassword = savePassword;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public void setSID(String sid) {
		this.sid = sid;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}

	public void setUser(String user) {
		this.user = user;
	}
	public void setUseWinAuth(boolean useWinAuth) {
		this.useWinAuth = useWinAuth;
	}
	@Override
	public String toString() {
		return uniqueID + " " + ip;
	}
}
