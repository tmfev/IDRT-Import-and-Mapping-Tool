package de.umg.mi.idrt.ioe.OntologyTree;

public class ServerPreferences {

	public final String TRENNER = ";";
	public String _serverName = "";
	public String _serverIP = "";
	public String _dbServerIP = "";
	public String _dbServerPort = "";
	public String _dbServerSIG = "";
	
	public void ServerPreferences2(){
		
	}
	
	public ServerPreferences(String serverName, String serverIP, String dbServerIP, String dbServerPort, String dbServerSIG){
		_serverName = serverName;
		_serverIP = serverIP;
		_dbServerIP = dbServerIP;
		_dbServerPort = dbServerPort;
		_dbServerSIG = dbServerSIG;
	}
	
	public ServerPreferences(String string){
		String[] splitString = string.split(TRENNER);
		_serverName		= splitString[0];
		_serverIP		= splitString[1];
		_dbServerIP		= splitString[2];
		_dbServerPort	= splitString[3];
		_dbServerSIG	= splitString[4];

	}
	
	public String getString(){
		return _serverName + TRENNER
				+ _serverIP  + TRENNER
				+ _dbServerIP  + TRENNER
				+ _dbServerPort  + TRENNER
				+ _dbServerSIG;
		
	}
	
}
