package de.umg.mi.idrt.ioe.OntologyTree;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map.Entry;

import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Debug;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.Resource.DataType;
import de.umg.mi.idrt.ioe.Resource.I2B2;


/**
 * This is the filecreator class which generates the output files. Each parser methods has for each method the specific filecreator
 * method. We build SQL-statements in thes files.
 * generated jaxb classes.
 * @author	Steffen Zeiss, Christian Bauer	
 * @version 0.9
 */

public class FileCreator {
	public PrintWriter out = null;
	boolean isGlobalExport = false;
	String i2b2Meta = "";
	String i2b2User = "";
	
	private final static String I2B2_HEADPATH = Resource.I2B2.I2B2_PATH_SEPERATOR + Resource.I2B2.I2B2_HEADPATH;
	private final static String I2B2_PATH_SEPERATOR = Resource.I2B2.I2B2_PATH_SEPERATOR;
	
	final String CSV_SEPERATOR = ";";
	final String CSV_NEWLINE = "\n";
	String _basecodeValueSeperator = ":";
	//StringEscapeUtils escape = new StringEscapeUtils();
	String _importDate = "";
	private File file;
	private String date = "";
	int counter = 0;
	final int rowcutter = 500;
	
	/**
	 * The constructor creates the files in the output directory. if this directory does not exits, the constructor will create
	 * the output directory and in den next step all directoreis for the four kinds of i2b2 import files.
	 * @param	filename 	name of the new generated file
	 * @param 	outputDirL2	name of the second level directory (in output) where the files are stored.
	 */
	public FileCreator(String filename, String outputDirL2, boolean isSQL){

		//i2b2User = Global.I2B2USER;
		i2b2Meta = i2b2User;
		//ontologyHeadPath = Global.ONTOLOGYHEADPATH;
		//isGlobalExport = Global.DELETE_OLD_ENTRIES;
		
		File outputDir = new File( "temp\\" );
		File l2outputDir = new File( outputDir, outputDirL2 );
		
		if (!outputDir.exists()) {
			try {
				outputDir.mkdir();
				l2outputDir.mkdir();
			}catch (SecurityException se){
				System.err.println(se.toString());
			}
		}
		if (!l2outputDir.exists()) {
			try {
				l2outputDir.mkdir();
			}catch (SecurityException se){
				System.err.println(se.toString());
			}
		}
		try{
			file = new File (l2outputDir,filename);
			//FileWriter f = new FileWriter(file);
			PrintWriter f = new PrintWriter(file);
			// true = autoflush on
			out = new PrintWriter(f, true);
			
			
		}
		catch(IOException e){
			System.err.println(e.toString());
		}


		// set global escape character
		//out.append( "Set Escape '\';" );
	}
	



	public FileCreator(String filename,String outputDirL2){
		new FileCreator(filename,outputDirL2,true);
	}
	
	public void setImportDate(String importDate){
		
		this._importDate = getSQLDate(importDate);
		
	
	}
	
	public String getImportDate(){
		if ( _importDate.isEmpty() ){
			
			_importDate = this.getCurrentDate();
			
		}
		return _importDate;
	}
	
	public String getSQLDate(String date){
		
		int month = 0;
		
		if (date == null || date.isEmpty()){
			date = getCurrentDate();
		} else if (date.split("-").length == 3){
			// seems to be in the right format already, or is it ?
			
		} else if (date.split(" ").length == 3 || date.split(".").length == 3) {
			// this is proboably a date in the format "DD Mon YYY" or "DD.MM.YYYY"
			
			String[] splitDate = null;
			

			if (date.split(" ").length == 3){
				// split accordingly and get month by string comparison 
				
				splitDate = date.split(" ");
				String monthString = splitDate[1];

				if (monthString.equalsIgnoreCase("January")
						|| monthString.equalsIgnoreCase("Januar")){
					month = 1;
				} else if (monthString.equalsIgnoreCase("February")
						|| monthString.equalsIgnoreCase("Februar")){
					month = 2;
				} else if (monthString.equalsIgnoreCase("March")
						|| monthString.equalsIgnoreCase("März")){
					month = 3;
				} else if (monthString.equalsIgnoreCase("April")
						|| monthString.equalsIgnoreCase("April")){
					month = 4;
				} else if (monthString.equalsIgnoreCase("May")
						|| monthString.equalsIgnoreCase("Mai")){
					month = 5;
				} else if (monthString.equalsIgnoreCase("June")
						|| monthString.equalsIgnoreCase("Juni")){
					month = 6;
				} else if (monthString.equalsIgnoreCase("July")
						|| monthString.equalsIgnoreCase("Juli")){
					month = 7;
				} else if (monthString.equalsIgnoreCase("August")
						|| monthString.equalsIgnoreCase("August")){
					month = 8;
				} else if (monthString.equalsIgnoreCase("September")
						|| monthString.equalsIgnoreCase("September")){
					month = 9;
				} else if (monthString.equalsIgnoreCase("October")
						|| monthString.equalsIgnoreCase("Oktober")){
					month = 10;
				} else if (monthString.equalsIgnoreCase("November")
						|| monthString.equalsIgnoreCase("November")){
					month = 11;
				} else if (monthString.equalsIgnoreCase("December")
						|| monthString.equalsIgnoreCase("Dezember")){
					month = 12;
				}
			} else {
				
				splitDate = date.split(".");
				month = Integer.valueOf(splitDate[2]);
				
			}
			
			
			
			String day = splitDate[0];
			
			if (day.length() > 2){
				day = day.substring(0,2);
			}

			String year = splitDate[2];
		
			date = year + "-" + new DecimalFormat("00").format(month) + "-"  + day;

		} else {
			Console.error("@Export: importdate not set (\""+date+"\")");
		}
		return date;//"to_date('" + date + "','YYYY-MM-DD')";
		
	}
	
	private String getSQLDateFunction(String date){
		return "to_date('" + date + "','YYYY-MM-DD')";
	}
	
	public String getCurrentDate(){
		
		String date = "";
		DecimalFormat df = new DecimalFormat("00");
		Calendar cal = Calendar.getInstance();
		cal.get(Calendar.MONTH);
		
		date = cal.get(Calendar.YEAR) + "-" + df.format(cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
		
        return date;
	}
	
	public void setDBName(String dbName){
		this.i2b2User = dbName;
		this.i2b2Meta = dbName;
	}
	
	

	/**
	 * This method returns true if this is a global export.
	 * 
	 * @return	true if it is a global export
	 */
	public String escapeForSql(Object object){
		if ( object == null ){
			Debug.d("* export:isNullObject");
			return "";
		} else {
			Debug.d("* export:isObject");
			// escape if the object gives some string
			if ( !object.toString().isEmpty() ){
				return escapeForSql( object.toString() );
			} else {
				return "";
			}
		}
	}

	/**
	 * Escapes a String for use in an SQL statment.
	 * (' -> '', & -> '&)
	 * 
	 * @return	true if it is a global export
	 */
	public String escapeForSql(String string){
		
		String stringOld = string;
		
		/*
		Debug.d("ESCAP:" + string + ":->" + StringEscapeUtils.escapeSql(string) +":");
		
		return StringEscapeUtils.escapeSql(string);
		*/
		if ( string == null ){
			return "";
		} else {
			// replaces ' with ''
			//string = StringEscapeUtils.escapeSql( string );
			string = string.replaceAll("'", "'||chr(39)||'");
			string = string.replaceAll(";", "'||chr(59)||'");
			string = string.replaceAll("&", "'||chr(38)||'");
			string = string.replaceAll("Ä", "'||chr(196)||'");
			string = string.replaceAll("ä", "'||chr(228)||'");
			string = string.replaceAll("Ö", "'||chr(214)||'");
			string = string.replaceAll("ö", "'||chr(246)||'");
			string = string.replaceAll("Ü", "'||chr(220)||'");
			string = string.replaceAll("ü", "'||chr(252)||'");
			string = string.replaceAll("ß", "'||chr(223)||'");
			
			string = string.replaceAll("°", "'||chr(176)||'");
			string = string.replaceAll("²", "'||chr(178)||'");
			
			
			
			string = string.replaceAll("ÃŒ", "'||chr(252)||'");
			
			//string.replaceAll("ö", "'ö");
			//string.replaceAll("ü", "'ü");
			//string.replaceAll("ß", "'ß");
			//Debug.d("ESCAP:" + stringOld + ":->" + string +":");
			
			return string;
		}
		
		
	}

	
    public String getVisitFromStringPath(String stringPath){
    	String[] split = stringPath.split("/");
    	if (split[2] != null){
    		return split[2].toString();
    	}
    	
    	return "";
    }
    
    public File getFile(){
    	return file;
    }
    
    private void addCounter(){
    	this.counter++;
    	if (this.counter % this.rowcutter == 0){
    		out.append("COMMIT;\n");
    	}
    }
    
    public void append(String append){
    	Debug.dn("out", out);
    	out.append( append );
    }
    
    public void close(){
    	out.close();
    }
    
    private boolean isNummeric(String dataType){
    	if (dataType.equals("float") || dataType.equals("Float"))
    		return true;
    	else if (dataType.equals("integer") || dataType.equals("Integer"))
    		return true;
    	else
    		return false;
    	
    	
    }
    
    public void saveOntologyHeader(){
    	out.append(
    			/*  0 */ "nodetype" + CSV_SEPERATOR +
    			/*  1 */ "id" + CSV_SEPERATOR + 
    			/*  2 */ "treePath"  + CSV_SEPERATOR + 
    			/*  3 */ "name" + CSV_SEPERATOR + 
    			/*  4 */ "importPath" + CSV_SEPERATOR +
        		/*  5 */ "i2b2Level" + CSV_SEPERATOR + 
        		/*  6 */ "i2b2Path" + CSV_SEPERATOR + 	 
        		/*  7 */ "conceptCode" + CSV_SEPERATOR + 
        		/*  8 */ "visualAttribute" + CSV_SEPERATOR + 
        		/*  9 */ "isVisable" + CSV_SEPERATOR + 
        		/* 10 */ "orderNumber" + CSV_SEPERATOR + 
        		/* 11 */ "isMandatory" + CSV_SEPERATOR + 
        		/* 12 */ "isRepeating" + CSV_SEPERATOR + 
        		/* 13 */ "isReferenceData" + CSV_SEPERATOR + 
        		/* 14 */ "type" + CSV_SEPERATOR + 
        		/* 15 */ "category" + CSV_SEPERATOR + 
        		/* 16 */ "itemDataType" + CSV_SEPERATOR + 
        		/* 17 */ "question" + CSV_SEPERATOR + 
        		/* 18 */ "codelistID" + CSV_SEPERATOR + 
        		/* 19 */ "isChecked" + CSV_SEPERATOR + 
        		/* 20 */ "answerValue" + CSV_SEPERATOR + CSV_NEWLINE
    			);
    }
    
    public void saveOntology(
    		String nodeType,
    		String id,
    		String treePath,
    		String name,
    		String importPath,
    		String i2b2Level,
    		String i2b2Path,
    		String conceptCode,
    		String visualAttribute,
    		String isVisable,
    		String orderNumber,
    		String isMandatory,
    		String isRepeating,
    		String isReferenceData,
    		String type,
    		String category,
    		String dataType,
    		String question,
    		String codelistID,
    		String isChecked,
    		String answerValue
    		){
    	out.append(
    			nodeType + CSV_SEPERATOR + 
        		id + CSV_SEPERATOR + 
    			treePath  + CSV_SEPERATOR + 
    			name + CSV_SEPERATOR + 
    			importPath + CSV_SEPERATOR +
    			i2b2Level + CSV_SEPERATOR + 
        		i2b2Path + CSV_SEPERATOR + 
        		conceptCode + CSV_SEPERATOR + 
        		visualAttribute + CSV_SEPERATOR + 
        		isVisable + CSV_SEPERATOR + 
        		orderNumber + CSV_SEPERATOR + 
        		isMandatory + CSV_SEPERATOR + 
        		isRepeating + CSV_SEPERATOR + 
        		isReferenceData + CSV_SEPERATOR + 
        		type + CSV_SEPERATOR + 
        		category + CSV_SEPERATOR + 
        		dataType + CSV_SEPERATOR + 
        		question + CSV_SEPERATOR + 
        		codelistID + CSV_SEPERATOR +
        		isChecked + CSV_SEPERATOR +
        		answerValue + CSV_SEPERATOR + CSV_NEWLINE
    			);
    }
    
    public void saveMeta(String meta){
    	out.append(meta);
    }
    
    public void savePatientDataHeader(){
    	
		out.append(
				  /*  0 patientid */			"patientid" + CSV_SEPERATOR
				+ /*  1 patienti2b2id */		"patienti2b2id" + CSV_SEPERATOR
				+ /*  2 patientImportDate */	"patientImportDate" + CSV_SEPERATOR
				+ /*  3 itemStringPath */		"itemStringPath" + CSV_SEPERATOR
				+ /*  4 conecpt_cd */			"conecpt_cd" + CSV_SEPERATOR
				+ /*  5 provider_id	*/			"provider_id" + CSV_SEPERATOR
				+ /*  6 start_date */			"start_date" + CSV_SEPERATOR
				+ /*  7 value */				"value" + CSV_SEPERATOR
				+ /*  8 instance_num */			"instance_num" + CSV_SEPERATOR
				+ /*  9 observation_blob */		"observation_blob" + CSV_SEPERATOR
				+ /* 10 update_date */			"update_date" + CSV_SEPERATOR
				+ /* 11 download_date */		"download_date" + CSV_SEPERATOR
				+ /* 12 import_date */			"import_date" + CSV_SEPERATOR
				+ /* 13 sourcesystem_cd */		"sourcesystem_cd" + CSV_SEPERATOR
				+ /* 14 upload_id */			"upload_id" + CSV_SEPERATOR + CSV_NEWLINE
				);
    }	
    
    public void savePatientData(String patientID, String patientI2B2ID, String patientImportDate, String itemStringPath, String conceptCD, String provider_id, String start_date, String value, String instancenum, String observation_blob, String update_date, String download_date, String import_date, String sourcesystem_cd, String upload_id){
    	
		out.append(
				  /* patientid */			patientID + CSV_SEPERATOR
				+ /* patienti2b2id */		patientI2B2ID + CSV_SEPERATOR
				+ /* patientImportDate */	patientImportDate + CSV_SEPERATOR
				+ /* itemStringPath */		itemStringPath + CSV_SEPERATOR
				+ /* conecpt_cd */			conceptCD + CSV_SEPERATOR
				+ /* provider_id	*/		provider_id + CSV_SEPERATOR
				+ /* start_date */			start_date + CSV_SEPERATOR
				+ /* value */				value + CSV_SEPERATOR
				+ /* instance */			instancenum + CSV_SEPERATOR
				+ /* observation_blob */	observation_blob + CSV_SEPERATOR
				+ /* update_date */			update_date + CSV_SEPERATOR
				+ /* download_date */		download_date + CSV_SEPERATOR
				+ /* import_date */			import_date + CSV_SEPERATOR
				+ /* sourcesystem_cd */		sourcesystem_cd + CSV_SEPERATOR
				+ /* upload_id */			upload_id + CSV_SEPERATOR + CSV_NEWLINE
				);
    }
    
	
	
}
