package de.umg.mi.idrt.ioe;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Display;

public class Global {

	
	public static String variable = "";
	public static HashMap<String,JComponent> fieldComponent = new HashMap<String,JComponent>();
	
	public static String BASECODE_SEPARATOR = "|";
	public final static String BASECODE_VALUE_SEPARATOR = ":";
	public static String PATH_SEPARATOR = "\\";
	
	// i2b2 variables
	public final static int maxPatientNumLength = 38;
	
	public final static String CONFIGURATION_FILE
		= "config.ini";
	public static Properties PROPERTIES = new Properties();
	public final static String TEXT_FILE
		= "cfg/english.lf";
	public static Properties TEXT = new Properties();
	
	public static int counter = 0;

	public final static Color COLOR_SUCCESS = new Color(80, 252, 74);
	public final static String COLOR_SUCCESS_HTML = "50FC4A";
	public final static String COLOR_SUCCESS_HTML_LIGHT = "D3FDD2";
	public final static Color COLOR_ERROR = new Color(252, 74, 74);
	public final static String COLOR_ERROR_HTML = "FC4A4A";
	public final static String COLOR_ERROR_HTML_LIGHT = "FDD3D3";
	public final static Color COLOR_INFO = new Color(200, 200, 230);
	public final static String COLOR_INFO_HTML = "C8C8E6";
	public final static String COLOR_INFO_HTML_LIGHT = "E4E4F6";

	// final variables for the look of the programm
	public static final Color MAIN_WINDOWS_BGCOLOR =
		Color.WHITE;
	public static final Dimension DIMENSION_PROGRAM_MAX =
		new Dimension(1000, 800);
	public static final Dimension DIMENSION_PROGRAM_MIN =
		new Dimension(1000,	800);
	public static final Dimension DIMENSION_PROGRAM_PREF = 
		new Dimension(1000,	800);
	public static final Dimension ONTOLOGYEDITOR_DIMENSION_MAX =
		new Dimension(1200, 800);
	public static final Dimension ONTOLOGYEDITOR_DIMENSION_MIN =
		new Dimension(800, 400);
	public static final Dimension ONTOLOGYEDITOR_DIMENSION_PREF =
		new Dimension(1200, 500);

	public static final Dimension NODEEDITOR_DIMENSION_MAX =
		new Dimension(1100, 3000);
	public static final Dimension NODEEDITOR_DIMENSION_MIN =
		new Dimension(800, 200);
	public static final Dimension NODEEDITOR_DIMENSION_PREF =
		new Dimension(1100, 200);
	public static final Color NODEEDITOR_BACKGROUND_COLOR =
		Color.WHITE;

	
	// the active component to talk to the user
	public Component activeComponent = null;
	private I2B2ImportTool I2B2ImportTool;
	
	public static JMenu menuSettings = null;

	private static Display display;

	public Global(I2B2ImportTool i2b2ImportTool, JFrame jFrame){
		this.I2B2ImportTool = i2b2ImportTool;

	}
	
	public Global(I2B2ImportTool i2b2ImportTool, Display display){
		this.I2B2ImportTool = i2b2ImportTool;
		this.display = display;
	}
	
	public static void setDisplay(Display display){
		//this.display = display;
	}


	/*
	public static void setGlobalFromPropertiesFiles(Display display) {
		Debug.f("setGlobalFromPropertiesFiles @I2B2ImportTool");
		String tmpProperty = "";
		File tmpFile = new File("hans.txt");
		
		//Shell shell = new Shell (display);
	    //shell.open ();
		

	    
	    URL url = Platform.getInstanceLocation().getURL();
	    Debug.d("FileDialog:" + url.getPath());

		String tmpString2 = "";
		try {
			tmpString2 = tmpFile.getCanonicalPath();
		} catch (IOException e1) {
			Console.error(e1.toString() + " @Global Code1");
		}
		
		Debug.d("TMPFILE:" + tmpFile.getAbsolutePath() + " :" + tmpString2);
		Global.PROPERTIES = new Properties();
		try {
			// loading properties file
			

			String propertiesFileNameFQ = "properties/hibernate.properties";
	
			Global.PROGRAMM_TITLE = (Global.PROPERTIES
					.getProperty("TOOL.PROGRAMM_TITLE") != null ? Global.PROPERTIES
					.getProperty("TOOL.PROGRAMM_TITLE")
					: "OntologyDataTool 0.01");
			Global.FILE_EXTENSION = Global.PROPERTIES
					.getProperty("TOOL.FILE_EXTENSION") != null ? Global.PROPERTIES
					.getProperty("TOOL.FILE_EXTENSION")
					: "csv";
			Global.SQL_STUDYID = "";
			Global.BASECODE_SEPARATOR = Global.PROPERTIES
					.getProperty("I2B2.BASECODE_SEPARATOR") != null ? Global.PROPERTIES
					.getProperty("I2B2.BASECODE_SEPARATOR")
					: ":";
			Global.PATH_SEPARATOR = Global.PROPERTIES
					.getProperty("I2B2.PATH_SEPARATOR") != null ? Global.PROPERTIES
					.getProperty("I2B2.PATH_SEPARATOR")
					: "\\";
			Global.I2B2USER = Global.PROPERTIES.getProperty("I2B2.USER") != null ? Global.PROPERTIES
					.getProperty("I2B2.USER")
					: "i2b2i2b2thesis";
			Global.ONTOLOGYHEADPATH = Global.PROPERTIES
					.getProperty("I2B2.ONTOLOGYHEADPATH") != null ? Global.PROPERTIES
					.getProperty("I2B2.ONTOLOGYHEADPATH")
					: "\\i2b2";
			Global.DELETE_OLD_ENTRIES = Global.PROPERTIES
					.getProperty("I2B2.DELETE_OLD_ENTRIES") != null ? (Global.PROPERTIES
					.getProperty("I2B2.DELETE_OLD_ENTRIES").equals("true") ? true
					: false)
					: true;
			Global.CREATE_ONE_SQL_FILE = Global.PROPERTIES
					.getProperty("I2B2.CREATE_ONE_SQL_FILE") != null ? (Global.PROPERTIES
					.getProperty("I2B2.CREATE_ONE_SQL_FILE").equals("true") ? true
					: false)
					: true;
			tmpProperty = Global.PROPERTIES.getProperty("I2B2.DATABASE_URL");
			Global.DATABASE_URL = tmpProperty != null && !tmpProperty.isEmpty() ? Global.PROPERTIES
					.getProperty("I2B2.DATABASE_URL") : "";
			tmpProperty = Global.PROPERTIES.getProperty("I2B2.DATABASE_USERNAME");
			Global.DATABASE_USERNAME = tmpProperty != null && !tmpProperty.isEmpty() ? Global.PROPERTIES
					.getProperty("I2B2.DATABASE_USERNAME") : "";
		} catch (Exception e) {
			System.err.println("Error in reading properties from file. ("+e.toString()+")");

		}
	}
*/

/*
	public static void setChangeableGlobal( String userName, String ontologyHeadPath, boolean deleteOldEntries, boolean createOneSqlFile, String databaseURL, String databaseUsername) {
		
		Global.I2B2USER = userName;
		Global.ONTOLOGYHEADPATH = ontologyHeadPath;
		Global.DELETE_OLD_ENTRIES = deleteOldEntries;
		Global.CREATE_ONE_SQL_FILE = createOneSqlFile;
		Global.DATABASE_URL = databaseURL;
		Global.DATABASE_USERNAME = databaseUsername;
		
	}
	
*/
	
	public static String getTextFromProperties(String textID){
		return (String) Global.PROPERTIES.get(textID);
	}
	
	public static String getText(String textID){
		String text = (String) Global.TEXT.get(textID);
		if (text == null
				|| text.isEmpty()){
			return "No text found in the configuration file. (TextID="+textID+")";
		} else {
			return text;
		}
	}
	
	public static String getText(String textID, Object value){
		String text = getText(textID);
		text = text.replaceFirst("#", value.toString());
		return text;
	}
	
	public static String getText(String textID, Object value1,  Object value2){
		String text = getText(textID);
		text = text.replaceFirst("#", value1.toString());
		text = text.replaceFirst("#", value2.toString());
		return text;
	}
	
	public static String getText(String textID, Object value1,  Object value2, Object value3){
		String text = getText(textID);
		text = text.replaceFirst("#", value1.toString());
		text = text.replaceFirst("#", value2.toString());
		text = text.replaceFirst("#", value3.toString());
		return text;
	}
	
	public static void registerField(String name, JComponent component){
		fieldComponent.put(name, component);
	}
	
	public static JComponent getField(String name){
		JComponent component = fieldComponent.get(name);
		fieldComponent.remove(name);
		return component;
	}

	
	
}
