package routines;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.prefs.Preferences;

import de.goettingen.i2b2.importtool.idrt.StatusListener.StatusListener;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**	Rotates a given CSV-File to an EAV-Format
 * @author Benjamin Baum
 * @author Christian Bauer
 * @version 1.0
 */
public class IDRTHelper {

	private static int ONT_HLEVEL = 0;
	private static int ONT_NAME = 1;
	private static int ONT_PATH = 2;
	private static int ONT_DATATYPE = 3;
	private static int ONT_UPDATEDATE= 4;
	private static int ONT_IMPORTDATE = 5;
	private static int ONT_DOWNLOADDATE = 6;
	private static int ONT_PATHID = 7;
	private static int ONT_VISUAL = 8;
	private static int ONT_ITEMCODE = 9;
	private static int ONT_SOURCE = 10;
	private static int ONT_XML = 11;
	private static int ONT_M_APPLIEDPATH = 12;
	private static int ONT_LENGTH = 13;

	private static int OBS_PATIENTID = 0;
	private static int OBS_ENCOUNTERID = 1;
	private static int OBS_IMPORTDATE = 2;
	private static int OBS_UPDATEDATE = 3;
	private static int OBS_DOWNLOADDATE = 4;
	private static int OBS_ITEM = 5;
	private static int OBS_VALUE = 6;
	private static int OBS_NICENAME = 7;
	private static int OBS_DATATYPE = 8;
	private static int OBS_UNIQUEID = 9;
	private static int OBS_BIRTHDAY = 10;
	private static int OBS_DEATHDAY = 11;
	private static int OBS_STARTDATE = 12;
	private static int OBS_ENDDATE = 13;
	private static int OBS_SOURCESYSTEM = 14;
	private static int OBS_INSTANCENUM = 15;
	private static int OBS_MODIFIERTYPE = 16;
	private static int OBS_SIC = 17;
	private static int OBS_LENGTH = 18;

	private static int PID = -1;
	private static int UpdateDate = -1;
	private static int ImportDate = -1;
	private static int DownloadDate = -1;
	private static int Sourcesystem = -1;
	private static int EncounterID = -1;
	private static int StartDate = -1;
	private static int EndDate = -1;
	private static int BirthDay = -1;

	private static int objectID = -1;

//	private static int discharge = -1;
//	private static int admission = -1;
	private static int DeathDay = -1;

	private static int headline;

//	private String sourcesystemName;
	//	private static String dbName = "I2B2IDRTDEMO.IDRTHelper";
	private HashMap<Integer, IDRTItem> IDRTItemMap;
	/**
	 * @return the iDRTItemMap
	 */
	public HashMap<Integer, IDRTItem> getIDRTItemMap() {
		return IDRTItemMap;
	}
	private static List<Integer> ignoreList;

	/**
	 * @param iDRTItemMap the iDRTItemMap to set
	 */
	public void setIDRTItemMap(HashMap<Integer, IDRTItem> iDRTItemMap) {
		IDRTItemMap = iDRTItemMap;
	}


	private static Connection con;
	private static char DEFAULTDELIM = ';';

	public IDRTHelper(){
		IDRTItemMap = new HashMap<Integer, IDRTItem>();
	}

	
	//	public IDRTHelper(String host, String port, String sid, String username, String password) {
	//		try{
	//			HOST = host;
	//			PORT = port;
	//			SID = sid;
	//			USERNAME = username;
	//			PWD = password;
	//			getConnection();
	//		} catch (SQLException e) {
	//			e.printStackTrace();
	//		}
	//	}
	public static String getEncoding(String fileString, String contextEncoding){
		try {
			File file = new File (fileString);
			CSVReader reader = new CSVReader(new FileReader(file),'>');
			String [] next = reader.readNext();
			reader.close();
			String test = next[0].substring(next[0].indexOf("?")+1, next[0].lastIndexOf("?"));
			String[] split1 = test.split(" ");

			for (int i = 0; i <split1.length; i++){
				if (split1[i].startsWith("encoding")){
					String encoding = split1[i].substring(split1[i].indexOf("=")+1, split1[i].length());
					if (encoding.contains("\"")){
						encoding = encoding.replaceAll("\"", "");
					}
					if (encoding.startsWith("UTF") || encoding.startsWith("ISO"))
						return encoding;	
				}
			}
			System.out.println("NO encoding found, using context.encoding!");

			return contextEncoding;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public String replaceIDRT(String toReplace){

		return toReplace.replace("Ä„", "AE").replace("ä", "ae").replace("Ö", "OE").replace("ö", "oe").replace("ü", "ue").replace("-", "_");

	}
	public void readConfig(File config, char quoteChar) {
		try {
			FileReader fr = new FileReader(config);

			System.out.println("ENCODING " + fr.getEncoding());
			fr.close();
			PID = -1;
			UpdateDate = -1;
			ImportDate = -1;
			DownloadDate = -1;
			Sourcesystem = -1;
			EncounterID = -1;
			StartDate = -1;
			EndDate = -1;
			objectID = -1;

			char inputDelim = '\t';
			ignoreList = new LinkedList<Integer>();
			CSVReader CSVInput = new CSVReader(new FileReader(config), inputDelim, quoteChar);
			String []line1 = CSVInput.readNext();

			if (line1.length==1){
				System.err.println("Wrong delimiter?");
				if (inputDelim==DEFAULTDELIM){
					inputDelim='\t';
					System.err.println("Delimiter set to: \\t");
				}
				else{
					inputDelim=DEFAULTDELIM;
					System.err.println("Delimiter set to: " + DEFAULTDELIM);
				}
			}
			CSVInput.close();
			CSVInput = new CSVReader(new FileReader(config), inputDelim);
			line1 = CSVInput.readNext();
			String []dataType = CSVInput.readNext();
			String []niceName = CSVInput.readNext();
			String []configRow = CSVInput.readNext();
			CSVInput.readNext(); //pidrow
			String []headerRow = CSVInput.readNext();
			
			headline = Integer.parseInt(headerRow[1]);
			CSVInput.close();

			for (int k = 0; k< configRow.length; k++){

				if (configRow[k].toLowerCase().equals("patientid")){
					PID = k-1;
					System.out.println("PID@" + k);
				}
				if (configRow[k].toLowerCase().equals("updatedate")){
					UpdateDate = k-1;
					System.out.println("UpdateDate@" + k);
				}
				if (configRow[k].toLowerCase().equals("importdate")){
					ImportDate = k-1;
					System.out.println("ImportDate@" + k);
				}
				if (configRow[k].toLowerCase().equals("downloaddate")){
					DownloadDate = k-1;
					System.out.println("DownloadDate@" + k);
				}
				if (configRow[k].toLowerCase().equals("sourcesystem")){
					Sourcesystem = k-1;
//					SourcesystemName = configRow[k];
					System.out.println("Sourcesystem@" + k);
				}
				if (configRow[k].toLowerCase().equals("encounterid")){
					EncounterID = k-1;
					System.out.println("EncounterID@" + k);
				}
				if (configRow[k].toLowerCase().equals("startdate")){
					StartDate = k-1;
					System.out.println("StartDate@" + k);
				}
				if (configRow[k].toLowerCase().equals("enddate")){
					EndDate = k-1;
					System.out.println("EndDate@" + k);
				}

				/**
				 * Modifier
				 */
				if (configRow[k].equalsIgnoreCase("objectid")){
					objectID = k-1;
					System.out.println("objectid@" + k);
				}

				if (configRow[k].toLowerCase().equals("birthday")){
					BirthDay = k-1;
					System.out.println("BirthDay@" + k);
				}
				if (configRow[k].toLowerCase().equals("deathday")){
					DeathDay = k-1;
					System.out.println("DeathDay@" + k);
				}
				if (configRow[k].toLowerCase().equals("admission")){
//					admission = k-1;
					System.out.println("admission@" + k);
				}
				if (configRow[k].toLowerCase().equals("discharge")){
//					discharge = k-1;
					System.out.println("discharge@" + k);
				}
				if (configRow[k].toLowerCase().equals("ignore")){
					ignoreList.add(k-1);
					System.out.println("ignoring: " + (k-1));
				}
			}

			List<String> columnNames = new LinkedList<String>();

			for (int i = 0; i<line1.length;i++){
				line1[i]=replaceIDRT(line1[i]);
				if (columnNames.contains(line1[i])){
					while (columnNames.contains(line1[i])){
						System.err.println("Duplicate entry found, renamed " + line1[i] + " to " + line1[i]+"_");
						line1[i]+="_";
					}
				}
				if (line1[i].length()>0)
					columnNames.add(line1[i]);
				else {
					line1[i] = "emptyColumn";
					columnNames.add(line1[i]);
				}
				//i != bioSic+1 && i != imageSic+1 && i != otherSic+1 &&
				if ( i != PID+1 && i != UpdateDate+1 && i != ImportDate+1 && i != DownloadDate+1 && i != Sourcesystem+1 && i!=EncounterID+1){
					IDRTItem idrtItem;

					if (i<niceName.length && niceName[i].length()>0){
						System.out.println("nicename erkannt");
						idrtItem = new IDRTItem(line1[i], dataType[i], niceName[i]);

					}
					else{
						idrtItem = new IDRTItem(line1[i], dataType[i], line1[i]);
						System.out.println("kein nicename@" + line1[i] + " " + dataType[i]);
					}
					//					System.out.println("Put: " + (i-1) + ":"+idrtItem.getColumnName()+"-"+idrtItem.getNiceName());
					IDRTItemMap.put(i-1, idrtItem);
				}
			}

		} catch (Exception e) {
			//	StatusListener.error("aaa","aaa");
			e.printStackTrace();
		}
	}

	//NO pidgen
	//input,output,ont,context.i2b2HeadNode,context.MDPDName,context.MDPD,context.fileName,context.quoteChar, context.datePattern
	public void rotateData(File input, File output, File ont, String headnode, String pdName, String pd, String tableName, char quoteChar, String pattern) throws SQLException {
		try {
			//			File ont = new File("C:/Desktop/ont.csv");

			//			String tableName = "DATATABLE";
			//			String pdName="Patientendaten";
			//			String pd = "PD";

			long start = System.currentTimeMillis();
			char inputDelim = '\t';
			char outputDelim = '\t';
			CSVReader CSVInput = new CSVReader(new FileReader(input), inputDelim, quoteChar, headline);
			int allRows = 1;
			String []nextLine = CSVInput.readNext();
			while((nextLine = CSVInput.readNext()) != null){
				allRows++;
			}
			System.out.println("ROWS: " + allRows);
			CSVInput.close();
			CSVInput = new CSVReader(new FileReader(input), inputDelim, quoteChar, headline);
			nextLine = CSVInput.readNext();

			if (nextLine.length==1){
				System.err.println("Wrong delimiter?");
				if (inputDelim==DEFAULTDELIM){
					inputDelim='\t';
					System.err.println("Delimiter set to: \\t");
				}
				else{
					inputDelim=DEFAULTDELIM;
					System.err.println("Delimiter set to: " + DEFAULTDELIM);
				}
			}
			System.out.println("quote: " + quoteChar);
			CSVInput.close();
			CSVInput = new CSVReader(new FileReader(input), inputDelim, quoteChar, headline); //

			CSVWriter rotatedOutput = new CSVWriter(new FileWriter(output), outputDelim);

			nextLine = CSVInput.readNext();

			List<String> columnNames = new LinkedList<String>();

			for (int a = 0; a < nextLine.length;a++){
				nextLine[a]=replaceIDRT(nextLine[a]);
				if (columnNames.contains(nextLine[a])){
					while (columnNames.contains(nextLine[a])){
						System.err.println("Duplicate entry found, renamed " + nextLine[a] + " to " + nextLine[a]+"_");
						nextLine[a]+="_";
					}
				}
				columnNames.add(nextLine[a]);
			}

			System.out.println("delim: " + outputDelim);
			CSVWriter ontOutput = new CSVWriter(new FileWriter(ont), outputDelim);
			String[] ontLine = new String[ONT_LENGTH];
			ontLine[ONT_HLEVEL] = "HLEVEL";
			ontLine[ONT_NAME] = "Name";
			ontLine[ONT_PATH] = "Path";
			ontLine[ONT_DATATYPE] = "DataType";
			ontLine[ONT_UPDATEDATE] = "Update_Date";
			ontLine[ONT_IMPORTDATE] = "Import_Date";
			ontLine[ONT_DOWNLOADDATE] = "Download_Date";
			ontLine[ONT_PATHID] = "PathID";
			ontLine[ONT_VISUAL] = "visual";
			ontLine[ONT_ITEMCODE] = "itemCode";
			ontLine[ONT_SOURCE] = "source";
			ontLine[ONT_XML] = "xml";
			ontLine[ONT_M_APPLIEDPATH] = "m_applied_path";
			ontOutput.writeNext(ontLine);

			//
			ontLine[ONT_HLEVEL] = "0";
			ontLine[ONT_NAME] = "Ontology";
			ontLine[ONT_PATH] = "\\"+headnode+"\\";
			ontLine[ONT_DATATYPE] = "";
			ontLine[ONT_UPDATEDATE] = "";
			ontLine[ONT_IMPORTDATE] = "";
			ontLine[ONT_DOWNLOADDATE] = "";
			ontLine[ONT_PATHID] = "";
			ontLine[ONT_VISUAL] = "FAE";
			ontLine[ONT_ITEMCODE] = "";
			ontLine[ONT_SOURCE] = "";
			ontLine[ONT_XML] = "";
			ontLine[ONT_M_APPLIEDPATH] = "@";
			ontOutput.writeNext(ontLine);

			/**
			 * MODIFIERS
			 */
			if (objectID>=0) { 
				System.out.println("GOING FOR MODIFIERS");
				ontLine[ONT_HLEVEL] = "1";
				if (objectID>=0) {
					ontLine[ONT_NAME] = "Objects";	//TODO context variable
					ontLine[ONT_PATH] = "\\"+headnode+"\\"+"OID"+"\\"; //TODO context variable
				}

				ontLine[ONT_DATATYPE] = "";
				ontLine[ONT_UPDATEDATE] = "";
				ontLine[ONT_IMPORTDATE] = "";
				ontLine[ONT_DOWNLOADDATE] = "";
				ontLine[ONT_PATHID] = "";
				ontLine[ONT_VISUAL] = "FAE";
				ontLine[ONT_ITEMCODE] = "";
				ontLine[ONT_SOURCE] = "";
				ontLine[ONT_XML] = "";
				ontLine[ONT_M_APPLIEDPATH] = "@";

				ontOutput.writeNext(ontLine);

				ontLine[ONT_HLEVEL] = "1";
				ontLine[ONT_NAME] = "OBJECTID";
				ontLine[ONT_PATH] = "\\i2b2\\"+"OBJECTID"+"\\"; //"\\"+headnode+"\\"+pd+"\\"+tableName+"\\";
				ontLine[ONT_DATATYPE] = "";
				ontLine[ONT_UPDATEDATE] = "";
				ontLine[ONT_IMPORTDATE] = "";
				ontLine[ONT_DOWNLOADDATE] = "";
				ontLine[ONT_PATHID] = "";
				ontLine[ONT_VISUAL] = "DHE";
				ontLine[ONT_ITEMCODE] = "";
				ontLine[ONT_SOURCE] = "";
				ontLine[ONT_XML] = "";
				ontLine[ONT_M_APPLIEDPATH] = "@";  //applied path

				ontOutput.writeNext(ontLine);


				ontLine[ONT_HLEVEL] = "1";
				ontLine[ONT_NAME] = tableName;
				ontLine[ONT_PATH] = "\\"+tableName+"\\"; //"\\"+headnode+"\\"+pd+"\\"+tableName+"\\";
				ontLine[ONT_DATATYPE] = "";
				ontLine[ONT_UPDATEDATE] = "";
				ontLine[ONT_IMPORTDATE] = "";
				ontLine[ONT_DOWNLOADDATE] = "";
				ontLine[ONT_PATHID] = "";
				ontLine[ONT_VISUAL] = "DAE";
				ontLine[ONT_ITEMCODE] = "";
				ontLine[ONT_SOURCE] = "";
				ontLine[ONT_XML] = "";
				if (objectID>=0)
					ontLine[ONT_M_APPLIEDPATH] = "\\"+headnode+"\\"+"OID"+"\\";  //applied path

				ontOutput.writeNext(ontLine);



				int columnNum = columnNames.size();
				int colCounter = 0;
				//				System.out.println(columnNumber);
				//				while(coulumnNameIterator.hasNext()){
				for (int i= 0; i<columnNum;i++){
					//					currentColumn = coulumnNameIterator.next();

					if ( !ignoreList.contains(colCounter) && colCounter!=PID && 
							colCounter!= Sourcesystem && colCounter != DownloadDate && 
							colCounter != ImportDate && colCounter != UpdateDate && 
							colCounter != EncounterID && colCounter != DeathDay && 
							colCounter != BirthDay && colCounter != StartDate && colCounter != EndDate
					){ //&& colCounter != bioSic && colCounter != imageSic && colCounter != otherSic

						String string = columnNames.get(i);	
						ontLine[ONT_HLEVEL] = "2";
						ontLine[ONT_NAME] = IDRTItemMap.get(colCounter).getNiceName();
						ontLine[ONT_PATH] = "\\"+tableName+"\\"+string+"\\";
						ontLine[ONT_DATATYPE] = IDRTItemMap.get(colCounter).getDataType();
						ontLine[ONT_UPDATEDATE] = "";
						ontLine[ONT_IMPORTDATE] = "";
						ontLine[ONT_DOWNLOADDATE] = "";
						ontLine[ONT_PATHID] = input.getName().substring(0, input.getName().lastIndexOf("."))+"|"+string;	
						ontLine[ONT_VISUAL] = "DAE";
						ontLine[ONT_ITEMCODE] = "";
						ontLine[ONT_SOURCE] = "";
						ontLine[ONT_XML] = "";
						if (objectID>=0)
							ontLine[ONT_M_APPLIEDPATH] = "\\"+headnode+"\\"+"OID"+"\\";

						ontOutput.writeNext(ontLine);
					}
					colCounter++;
				}
				ontOutput.flush();
				ontOutput.close();

			}
			else {
				//No Modifiers
				ontLine[ONT_HLEVEL] = "1";
				ontLine[ONT_NAME] = pdName;
				ontLine[ONT_PATH] = "\\"+headnode+"\\"+pd+"\\";
				ontLine[ONT_DATATYPE] = "";
				ontLine[ONT_UPDATEDATE] = "";
				ontLine[ONT_IMPORTDATE] = "";
				ontLine[ONT_DOWNLOADDATE] = "";
				ontLine[ONT_PATHID] = "";
				ontLine[ONT_VISUAL] = "FAE";
				ontLine[ONT_ITEMCODE] = "";
				ontLine[ONT_SOURCE] = "";
				ontLine[ONT_XML] = "";
				ontLine[ONT_M_APPLIEDPATH] = "@";
				ontOutput.writeNext(ontLine);

				ontLine[ONT_HLEVEL] = "2";
				ontLine[ONT_NAME] = tableName;
				ontLine[ONT_PATH] = "\\"+headnode+"\\"+pd+"\\"+tableName+"\\";
				ontLine[ONT_DATATYPE] = "";
				ontLine[ONT_UPDATEDATE] = "";
				ontLine[ONT_IMPORTDATE] = "";
				ontLine[ONT_DOWNLOADDATE] = "";
				ontLine[ONT_PATHID] = "";
				ontLine[ONT_VISUAL] = "FAE";
				ontLine[ONT_ITEMCODE] = "";
				ontLine[ONT_SOURCE] = "";
				ontLine[ONT_XML] = "";
				ontLine[ONT_M_APPLIEDPATH] = "@";
				ontOutput.writeNext(ontLine);

				int columnNum = columnNames.size();
				int colCounter = 0;
				//				System.out.println(columnNumber);
				//				while(coulumnNameIterator.hasNext()){
				for (int i= 0; i<columnNum;i++){
					//					currentColumn = coulumnNameIterator.next();

					if ( !ignoreList.contains(colCounter) && colCounter!=PID && 
							colCounter!= Sourcesystem && colCounter != DownloadDate && 
							colCounter != ImportDate && colCounter != UpdateDate && 
							colCounter != EncounterID && colCounter != DeathDay && 
							colCounter != BirthDay && colCounter != StartDate && colCounter != EndDate
					){ //&& colCounter != bioSic && colCounter != imageSic && colCounter != otherSic

						String string = columnNames.get(i);	
						ontLine[ONT_HLEVEL] = "3";
						ontLine[ONT_NAME] = IDRTItemMap.get(colCounter).getNiceName();
						ontLine[ONT_PATH] = "\\i2b2\\"+pd+"\\"+tableName+"\\"+string+"\\";
						ontLine[ONT_DATATYPE] = IDRTItemMap.get(colCounter).getDataType();
						ontLine[ONT_UPDATEDATE] = "";
						ontLine[ONT_IMPORTDATE] = "";
						ontLine[ONT_DOWNLOADDATE] = "";
						ontLine[ONT_PATHID] = input.getName().substring(0, input.getName().lastIndexOf("."))+"|"+string;	
						ontLine[ONT_VISUAL] = "FAE";
						ontLine[ONT_ITEMCODE] = "";
						ontLine[ONT_SOURCE] = "";
						ontLine[ONT_XML] = "";
						ontLine[ONT_M_APPLIEDPATH] = "@";
						ontOutput.writeNext(ontLine);
					}
					colCounter++;
				}
				System.out.println("flushing");
				ontOutput.flush();
				ontOutput.close();
			}



			System.out.println(ont.getAbsolutePath());
			System.out.println("closing done");
			//

			/**
			 * writing header
			 */
			String [] header = new String[OBS_LENGTH];
			header[OBS_PATIENTID] = "PatientID";
			header[OBS_ENCOUNTERID] = "EncounterID"; 
			header[OBS_IMPORTDATE] = "ImportDate"; 
			header[OBS_UPDATEDATE] = "UpdateDate"; 
			header[OBS_DOWNLOADDATE] = "DownloadDate";
			header[OBS_ITEM] = "Item";
			header[OBS_VALUE] = "Value";
			header[OBS_NICENAME] = "NiceName";
			header[OBS_DATATYPE] = "DataType";
			header[OBS_UNIQUEID] = "uniqueID";
			header[OBS_BIRTHDAY] = "BirthDay";
			header[OBS_DEATHDAY] = "DeathDay";
			header[OBS_STARTDATE] = "StartDate";
			header[OBS_ENDDATE] = "EndDate";
			header[OBS_SOURCESYSTEM] = "SourceSystem";
			header[OBS_INSTANCENUM] = "instanceNum";
			header[OBS_MODIFIERTYPE] = "modifierType";
			header[OBS_SIC] = "sic";
			rotatedOutput.writeNext(header);
			String [] nextOutputLine = new String[OBS_LENGTH];
			int rows = 1;
			int mod = 1;
			if(allRows/100 > 0)
				mod=allRows/100;
			HashMap<String,HashMap<String,Integer>>patientInstanceNumMap = new HashMap<String, HashMap<String,Integer>>();
			
			HashMap<String,Integer> instancePerObjectID = new HashMap<String, Integer>();
			while((nextLine = CSVInput.readNext()) != null){
				rows++;

				if (rows%(mod)==0 || rows==allRows){
					//					System.out.println("Row: " + rows + "/"+allRows);
					StatusListener.setSubStatus((float)rows/allRows*100, (int)((float)rows/allRows*100)+"% " + "Row: " + rows + "/"+allRows);
				}
				int columnCounter = 0;
				int instanceNum = 0;

				//				if(bioSic>=0) {
				//					instanceNum++;
				//				}
				String encounterIDString;
				if (PID>=0  && patientInstanceNumMap.containsKey(nextLine[PID])){
					if (EncounterID>=0){
						encounterIDString = nextLine[EncounterID];
						if (patientInstanceNumMap.get(nextLine[PID]).containsKey(encounterIDString)){
							instanceNum = patientInstanceNumMap.get(nextLine[PID]).get(encounterIDString)+1;
							
							patientInstanceNumMap.get(nextLine[PID]).remove(encounterIDString);
							patientInstanceNumMap.get(nextLine[PID]).put(encounterIDString,instanceNum);
						}
						else {
							patientInstanceNumMap.get(nextLine[PID]).put(nextLine[EncounterID], instanceNum);
						}
					}
					else {
						encounterIDString = "0";
						if (patientInstanceNumMap.get(nextLine[PID]).containsKey(encounterIDString)){
							instanceNum = patientInstanceNumMap.get(nextLine[PID]).get(encounterIDString)+1;
							patientInstanceNumMap.get(nextLine[PID]).remove(encounterIDString);
							patientInstanceNumMap.get(nextLine[PID]).put(encounterIDString,instanceNum);
						}
						else {
							patientInstanceNumMap.get(nextLine[PID]).put(encounterIDString, instanceNum);
						}
					}
				
				}
				else if (PID>=0 && !patientInstanceNumMap.containsKey(nextLine[PID])) {
					if (EncounterID>=0){
						HashMap<String,Integer> encTempMap = new HashMap<String, Integer>();
						encTempMap.put(nextLine[EncounterID], instanceNum);
						patientInstanceNumMap.put(nextLine[PID], encTempMap);
					}
					else {
						HashMap<String,Integer> encTempMap = new HashMap<String, Integer>();
						encTempMap.put("0", instanceNum);
						patientInstanceNumMap.put(nextLine[PID], encTempMap);
					}
				}
				
				if (objectID>=0) {
					if (instancePerObjectID.containsKey(nextLine[objectID])){
						instanceNum = instancePerObjectID.get(nextLine[objectID]);
						System.out.println(instanceNum + "==" + nextLine[objectID]);
					}
					else{
						instancePerObjectID.put(nextLine[objectID], instanceNum);
					}
				}
				//				else if (EncounterID<0 || PID < 0){
				//					instanceNum=0;
				//				}
				//				Iterator<String> coulumnNameIterator = columnNames.iterator();
				String currentColumn = "";
				//skip 1st column

				//				while(coulumnNameIterator.hasNext()){
				//
				//					currentColumn = coulumnNameIterator.next();
				int columnNumber = columnNames.size();
				//				System.out.println(columnNumber);
				//				while(coulumnNameIterator.hasNext()){
				for (int i= 0; i<columnNumber;i++){
					//					currentColumn = coulumnNameIterator.next();
					currentColumn = columnNames.get(i);				
					//					System.out.println("currentColumn: " + currentColumn); 

					/**
					 * empty columnName
					 */
					if (currentColumn.isEmpty()){
						currentColumn = currentColumn+"_empty";
					}
					/**
					 * skip PID Column
					 */

					if ( !nextLine[columnCounter].isEmpty() && !nextLine[columnCounter].equals("\"") &&!ignoreList.contains(columnCounter) && columnCounter!=PID && 
							columnCounter!= Sourcesystem && columnCounter != DownloadDate && 
							columnCounter != ImportDate && columnCounter != UpdateDate && 
							columnCounter != EncounterID && columnCounter != DeathDay && 
							columnCounter != BirthDay && columnCounter != StartDate && columnCounter != EndDate 
					){ //&& columnCounter != bioSic && columnCounter != imageSic && columnCounter != otherSic


						if (PID>=0)
							nextOutputLine[OBS_PATIENTID] = nextLine[PID];
						else 
							nextOutputLine[OBS_PATIENTID] = "";

						if (EncounterID>=0)
							nextOutputLine[OBS_ENCOUNTERID] = nextLine[EncounterID];
						else
							nextOutputLine[OBS_ENCOUNTERID] = "";

						if (ImportDate>=0){
							if (nextLine[ImportDate].length()>0){
								Date date2 = TalendDate.parseDate(pattern, nextLine[ImportDate]);
								nextOutputLine[OBS_IMPORTDATE] = TalendDate.formatDate("dd.MM.yyyy", date2);
							}
						}
						else
							nextOutputLine[OBS_IMPORTDATE] = "";

						if (UpdateDate>=0){
							if (nextLine[UpdateDate].length()>0){
								Date date2 = TalendDate.parseDate(pattern, nextLine[UpdateDate]);
								nextOutputLine[OBS_UPDATEDATE] = TalendDate.formatDate("dd.MM.yyyy", date2);
							}
						}
						else
							nextOutputLine[OBS_UPDATEDATE] = "";

						if (DownloadDate>=0){
							if (nextLine[DownloadDate].length()>0){
								Date date2 = TalendDate.parseDate(pattern, nextLine[DownloadDate]);
								nextOutputLine[OBS_DOWNLOADDATE] = TalendDate.formatDate("dd.MM.yyyy", date2);
							}
						}
						else
							nextOutputLine[OBS_DOWNLOADDATE] = "";

						nextOutputLine[OBS_ITEM] = currentColumn;
						if (nextLine[columnCounter].length()>180)
							nextOutputLine[OBS_VALUE] = nextLine[columnCounter].substring(0, 179);
						else
							nextOutputLine[OBS_VALUE] = nextLine[columnCounter];
						//						System.out.println("COUNTER: " + columnCounter);
						nextOutputLine[OBS_NICENAME] = IDRTItemMap.get(columnCounter).getNiceName();
						nextOutputLine[OBS_DATATYPE] = IDRTItemMap.get(columnCounter).getDataType();
						nextOutputLine[OBS_UNIQUEID] = input.getName().substring(0, input.getName().lastIndexOf("."));	

						if (BirthDay>=0){
							if (nextLine[BirthDay].length()>0){
								Date date2 = TalendDate.parseDate(pattern, nextLine[BirthDay]);
								nextOutputLine[OBS_BIRTHDAY] = TalendDate.formatDate("dd.MM.yyyy", date2);
							}
						}
						else
							nextOutputLine[OBS_BIRTHDAY] = "";
						if (DeathDay>=0){
							if (nextLine[DeathDay].length()>0){
								Date date2 = TalendDate.parseDate(pattern, nextLine[DeathDay]);
								nextOutputLine[OBS_DEATHDAY] = TalendDate.formatDate("dd.MM.yyyy", date2);
							}
						}
						else
							nextOutputLine[OBS_DEATHDAY] = "";

						if(StartDate>=0){
							if (nextLine[StartDate].length()>0){
								Date date3 = TalendDate.parseDate(pattern, nextLine[StartDate]);
								nextOutputLine[OBS_STARTDATE] = TalendDate.formatDate("dd.MM.yyyy", date3);
							}
						}
						else
							nextOutputLine[OBS_STARTDATE] = "";

						if(EndDate>=0){
							if (nextLine[EndDate].length()>0){
								Date date3 = TalendDate.parseDate(pattern, nextLine[EndDate]);
								nextOutputLine[OBS_ENDDATE] = TalendDate.formatDate("dd.MM.yyyy", date3);
							}
						}
						else
							nextOutputLine[OBS_ENDDATE] = "";

						if (Sourcesystem>=0)
							nextOutputLine[OBS_SOURCESYSTEM] = nextLine[Sourcesystem]; //+"_"+global;
						else{
							nextOutputLine[OBS_SOURCESYSTEM] = "";	
						}

						nextOutputLine[OBS_INSTANCENUM] = ""+instanceNum;
						
						if (objectID >=0) {
							nextOutputLine[OBS_MODIFIERTYPE] = "oid";
							nextOutputLine[OBS_SIC] =  nextLine[objectID];
						}

						rotatedOutput.writeNext(nextOutputLine);
						rotatedOutput.flush();
					} 
					columnCounter++;
				}
			}
			//			StatusListener.setSubStatus(0, "");
			rotatedOutput.close();
			CSVInput.close();
			long end = System.currentTimeMillis() - start;
			System.out.println("dura: " + end/1000 + "."+end%1000);
			long abc = 1;
			if (end/1000 >0)
				abc=end/1000;
			long speed = end/1000;
			if (speed <=0)
				speed = 1;
			System.out.println("Speed: " + rows/abc + " rows/s");
			System.out.println("Rotate done.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}}

	// pidgen
	public void rotateData(File input, File output, File ont, String headnode, String pdName, String pd, String tableName, char quoteChar, HashMap<String,String>pidMap, String pattern) throws SQLException {
		try {

			long start = System.currentTimeMillis();
			HashMap<String,Integer>patientInstanceNumMap = new HashMap<String, Integer>();
			System.out.println("PIDGENROTATE");
			char inputDelim = '\t';
			char outputDelim = '\t';
			CSVReader CSVInput = new CSVReader(new FileReader(input), inputDelim, quoteChar, headline);
			int allRows = 1;
			String []nextLine = CSVInput.readNext();
			while((nextLine = CSVInput.readNext()) != null){
				allRows++;
			}
			System.out.println("ROWS: " + allRows);
			CSVInput.close();
			CSVInput = new CSVReader(new FileReader(input), inputDelim, quoteChar, headline);
			nextLine = CSVInput.readNext();

			if (nextLine.length==1){
				System.err.println("Wrong delimiter?");
				if (inputDelim==DEFAULTDELIM){
					inputDelim='\t';
					System.err.println("Delimiter set to: \\t");
				}
				else{
					inputDelim=DEFAULTDELIM;
					System.err.println("Delimiter set to: " + DEFAULTDELIM);
				}
			}
			System.out.println("quote: " + quoteChar);


			CSVInput.close();
			CSVInput = new CSVReader(new FileReader(input), inputDelim, quoteChar, headline);

			CSVWriter rotatedOutput = new CSVWriter(new FileWriter(output), outputDelim);

			nextLine = CSVInput.readNext();

			List<String> columnNames = new LinkedList<String>();

			for (int a = 0; a < nextLine.length;a++){
				nextLine[a] = replaceIDRT(nextLine[a]);
				if (columnNames.contains(nextLine[a])){
					while (columnNames.contains(nextLine[a])){
						System.err.println("Duplicate entry found, renamed " + nextLine[a] + " to " + nextLine[a]+"_");
						nextLine[a]+="_";
					}
				}
				columnNames.add(nextLine[a]);

			}

			CSVWriter ontOutput = new CSVWriter(new FileWriter(ont), outputDelim);
			String[] ontLine = new String[ONT_LENGTH];
			ontLine[ONT_HLEVEL] = "HLEVEL";
			ontLine[ONT_NAME] = "Name";
			ontLine[ONT_PATH] = "Path";
			ontLine[ONT_DATATYPE] = "DataType";
			ontLine[ONT_UPDATEDATE] = "Update_Date";
			ontLine[ONT_IMPORTDATE] = "Import_Date";
			ontLine[ONT_DOWNLOADDATE] = "Download_Date";
			ontLine[ONT_PATHID] = "PathID";
			ontLine[ONT_VISUAL] = "visual";
			ontLine[ONT_ITEMCODE] = "itemCode";
			ontLine[ONT_SOURCE] = "source";
			ontLine[ONT_XML] = "xml";
			ontLine[ONT_M_APPLIEDPATH] = "m_applied_path";
			ontOutput.writeNext(ontLine);

			//
			ontLine[ONT_HLEVEL] = "0";
			ontLine[ONT_NAME] = "Ontology";
			ontLine[ONT_PATH] = "\\"+headnode+"\\";
			ontLine[ONT_DATATYPE] = "";
			ontLine[ONT_UPDATEDATE] = "";
			ontLine[ONT_IMPORTDATE] = "";
			ontLine[ONT_DOWNLOADDATE] = "";
			ontLine[ONT_PATHID] = "";
			ontLine[ONT_VISUAL] = "FAE";
			ontLine[ONT_ITEMCODE] = "";
			ontLine[ONT_SOURCE] = "";
			ontLine[ONT_XML] = "";
			ontLine[ONT_M_APPLIEDPATH] = "@";
			ontOutput.writeNext(ontLine);

			ontLine[ONT_HLEVEL] = "1";
			ontLine[ONT_NAME] = pdName;
			ontLine[ONT_PATH] = "\\"+headnode+"\\"+pd+"\\";
			ontLine[ONT_DATATYPE] = "";
			ontLine[ONT_UPDATEDATE] = "";
			ontLine[ONT_IMPORTDATE] = "";
			ontLine[ONT_DOWNLOADDATE] = "";
			ontLine[ONT_PATHID] = "";
			ontLine[ONT_VISUAL] = "FAE";
			ontLine[ONT_ITEMCODE] = "";
			ontLine[ONT_SOURCE] = "";
			ontLine[ONT_XML] = "";
			ontLine[ONT_M_APPLIEDPATH] = "@";

			ontOutput.writeNext(ontLine);

			ontLine[ONT_HLEVEL] = "2";
			ontLine[ONT_NAME] = tableName;
			ontLine[ONT_PATH] = "\\"+headnode+"\\"+pd+"\\"+tableName+"\\";
			ontLine[ONT_DATATYPE] = "";
			ontLine[ONT_UPDATEDATE] = "";
			ontLine[ONT_IMPORTDATE] = "";
			ontLine[ONT_DOWNLOADDATE] = "";
			ontLine[ONT_PATHID] = "";
			ontLine[ONT_VISUAL] = "FAE";
			ontLine[ONT_ITEMCODE] = "";
			ontLine[ONT_SOURCE] = "";
			ontLine[ONT_XML] = "";
			ontLine[ONT_M_APPLIEDPATH] = "@";
			ontOutput.writeNext(ontLine);

			int columnNum = columnNames.size();
			int colCounter = 0;
			//				System.out.println(columnNumber);
			//				while(coulumnNameIterator.hasNext()){
			for (int i= 0; i<columnNum;i++){
				//					currentColumn = coulumnNameIterator.next();

				if ( !ignoreList.contains(colCounter) && colCounter!=PID && 
						colCounter!= Sourcesystem && colCounter != DownloadDate && 
						colCounter != ImportDate && colCounter != UpdateDate && 
						colCounter != EncounterID && colCounter != DeathDay && 
						colCounter != BirthDay && colCounter != StartDate && colCounter != EndDate ){

					String string = columnNames.get(i);	
					ontLine[ONT_HLEVEL] = "3";
					ontLine[ONT_NAME] = IDRTItemMap.get(colCounter).getNiceName();
					ontLine[ONT_PATH] = "\\i2b2\\"+pd+"\\"+tableName+"\\"+string+"\\";
					ontLine[ONT_DATATYPE] = IDRTItemMap.get(colCounter).getDataType();
					ontLine[ONT_UPDATEDATE] = "";
					ontLine[ONT_IMPORTDATE] = "";
					ontLine[ONT_DOWNLOADDATE] = "";
					ontLine[ONT_PATHID] = input.getName().substring(0, input.getName().lastIndexOf("."))+"|"+string;	
					ontLine[ONT_VISUAL] = "FAE";
					ontLine[ONT_ITEMCODE] = "";
					ontLine[ONT_SOURCE] = "";
					ontLine[ONT_XML] = "";
					ontLine[ONT_M_APPLIEDPATH] = "@";
					ontOutput.writeNext(ontLine);
				}
				colCounter++;
			}
			ontOutput.flush();
			ontOutput.close();
			//			for (String string : nextLine) {
			//
			//				if (columnNames.contains(string)){
			//					while (columnNames.contains(string)){
			//						System.err.println("Duplicate entry found, renamed " + string + " to " + string+"_");
			//						string+="_";
			//					}
			//				}
			//				columnNames.add(string);
			//			}

			//int global = getGlobalFromDB(SourcesystemName);

			/**
			 * writing header
			 */
			String [] header = new String[OBS_LENGTH];
			header[OBS_PATIENTID] = "PatientID";
			header[OBS_ENCOUNTERID] = "EncounterID"; 
			header[OBS_IMPORTDATE] = "ImportDate"; 
			header[OBS_UPDATEDATE] = "UpdateDate"; 
			header[OBS_DOWNLOADDATE] = "DownloadDate";
			header[OBS_ITEM] = "Item";
			header[OBS_VALUE] = "Value";
			header[OBS_NICENAME] = "NiceName";
			header[OBS_DATATYPE] = "DataType";
			header[OBS_UNIQUEID] = "uniqueID";
			header[OBS_BIRTHDAY] = "BirthDay";
			header[OBS_DEATHDAY] = "DeathDay";
			header[OBS_STARTDATE] = "StartDate";
			header[OBS_ENDDATE] = "EndDate";
			header[OBS_SOURCESYSTEM] = "SourceSystem";
			header[OBS_INSTANCENUM] = "instanceNum";
			header[OBS_MODIFIERTYPE] = "modifierType";
			header[OBS_SIC] = "sic";
			rotatedOutput.writeNext(header);
			int k = 0;
			int rows = 1;
			int mod = 1;
			if(allRows/100 > 0)
				mod=allRows/100;
			while((nextLine = CSVInput.readNext()) != null && nextLine.length>1){
				rows++;
				if (rows%(mod)==0 || rows==allRows){
					//					System.out.println("Row: " + rows + "/"+allRows);
					StatusListener.setSubStatus((float)rows/allRows*100, (int)((float)rows/allRows*100)+"% " + "Row: " + rows + "/"+allRows);
				}
				int columnCounter = 0;
				int instanceNum = 0;

				if (PID>=0 && patientInstanceNumMap.containsKey(nextLine[PID])){
					instanceNum = patientInstanceNumMap.get(nextLine[PID])+1;
					patientInstanceNumMap.remove(nextLine[PID]);
					patientInstanceNumMap.put(nextLine[PID],instanceNum);
				}
				else {
					if(PID>=0)
						patientInstanceNumMap.put(nextLine[PID],instanceNum);
				}

				//				Iterator<String> coulumnNameIterator = columnNames.iterator();
				String currentColumn = "";
				//skip 1st column
				int columnNumber = columnNames.size();
				//				System.out.println(columnNumber);
				//				while(coulumnNameIterator.hasNext()){
				for (int i= 0; i<columnNumber;i++){
					//					currentColumn = coulumnNameIterator.next();
					currentColumn = columnNames.get(i);				
					//					System.out.println("currentColumn: " + currentColumn); 
					/**
					 * empty columnName
					 */
					if (currentColumn.isEmpty()){
						currentColumn = currentColumn+"_empty";
					}
					/**
					 * skip PID Column
					 */

					if ( !ignoreList.contains(columnCounter) && columnCounter!=PID && 
							columnCounter!= Sourcesystem && columnCounter != DownloadDate && 
							columnCounter != ImportDate && columnCounter != UpdateDate && 
							columnCounter != EncounterID && columnCounter != DeathDay && 
							columnCounter != BirthDay && columnCounter != StartDate && columnCounter != EndDate){
						String [] nextOutputLine = new String[OBS_LENGTH];

						//						if (PID>=0){
						//							System.out.println("pid: " + PID);
						//							nextOutputLine[0] = pidMap.get(nextLine[PID]);
						//							//							nextOutputLine[0] = pidMap.get(k);
						//						}


						if (pidMap.get(""+nextLine[PID])!=null){

							if (pidMap.get(""+nextLine[PID]).equals("FAILED")){
								break;
							}
							else {
								nextOutputLine[OBS_PATIENTID] = pidMap.get(""+nextLine[PID]);
							}
						}
						//WORKING
						//						if (pidMap.get(""+k)!=null){
						//
						//							if (pidMap.get(""+k).equals("FAILED")){
						//								break;
						//							}
						//							else {
						//								nextOutputLine[0] = pidMap.get(""+k);
						//							}
						//						}

						if (EncounterID>=0)
							nextOutputLine[OBS_ENCOUNTERID] = nextLine[EncounterID];
						else
							nextOutputLine[OBS_ENCOUNTERID] = "";

						if (ImportDate>=0){
							if (nextLine[ImportDate].length()>0){
								Date date2 = TalendDate.parseDate(pattern, nextLine[ImportDate]);
								nextOutputLine[OBS_IMPORTDATE] = TalendDate.formatDate("dd.MM.yyyy", date2);
							}
						}
						else
							nextOutputLine[OBS_IMPORTDATE] = "";

						if (UpdateDate>=0){
							if (nextLine[UpdateDate].length()>0){
								Date date2 = TalendDate.parseDate(pattern, nextLine[UpdateDate]);
								nextOutputLine[OBS_UPDATEDATE] = TalendDate.formatDate("dd.MM.yyyy", date2);
							}
						}
						else
							nextOutputLine[OBS_UPDATEDATE] = "";

						if (DownloadDate>=0){
							if (nextLine[DownloadDate].length()>0){
								Date date2 = TalendDate.parseDate(pattern, nextLine[DownloadDate]);
								nextOutputLine[OBS_DOWNLOADDATE] = TalendDate.formatDate("dd.MM.yyyy", date2);
							}
						}
						else
							nextOutputLine[OBS_DOWNLOADDATE] = "";

						nextOutputLine[OBS_ITEM] = currentColumn;
						if (nextLine[columnCounter].length()>180)
							nextOutputLine[OBS_VALUE] = nextLine[columnCounter].substring(0, 179);
						else
							nextOutputLine[OBS_VALUE] = nextLine[columnCounter];
						nextOutputLine[OBS_NICENAME] = IDRTItemMap.get(columnCounter).getNiceName();
						nextOutputLine[OBS_DATATYPE] = IDRTItemMap.get(columnCounter).getDataType();
						//						if (Sourcesystem>=0)
						//							nextOutputLine[9] = nextLine[Sourcesystem]; //+"_"+global;
						//						else
						nextOutputLine[OBS_UNIQUEID] = input.getName().substring(0, input.getName().lastIndexOf("."));	

						if (BirthDay>=0){
							if (nextLine[BirthDay].length()>0){
								Date date2 = TalendDate.parseDate(pattern, nextLine[BirthDay]);
								nextOutputLine[OBS_BIRTHDAY] = TalendDate.formatDate("dd.MM.yyyy", date2);
							}
						}
						else
							nextOutputLine[OBS_BIRTHDAY] = "";
						if (DeathDay>=0){
							if (nextLine[DeathDay].length()>0){
								Date date2 = TalendDate.parseDate(pattern, nextLine[DeathDay]);
								nextOutputLine[OBS_DEATHDAY] = TalendDate.formatDate("dd.MM.yyyy", date2);
							}
						}
						else
							nextOutputLine[OBS_DEATHDAY] = "";
						if(StartDate>=0){
							if (nextLine[StartDate].length()>0){
								Date date3 = TalendDate.parseDate(pattern, nextLine[StartDate]);
								nextOutputLine[OBS_STARTDATE] = TalendDate.formatDate("dd.MM.yyyy", date3);
							}
						}
						else
							nextOutputLine[OBS_STARTDATE] = "";

						if (EndDate>=0){
							if (nextLine[EndDate].length()>0){
								Date date3 = TalendDate.parseDate(pattern, nextLine[EndDate]);
								nextOutputLine[OBS_ENDDATE] = TalendDate.formatDate("dd.MM.yyyy", date3);
							}
						}
						else {
							nextOutputLine[OBS_ENDDATE] = "";
						}

						if (Sourcesystem>=0)
							nextOutputLine[OBS_SOURCESYSTEM] = nextLine[Sourcesystem]; //+"_"+global;
						else{
							nextOutputLine[OBS_SOURCESYSTEM] = "";	
						}
						nextOutputLine[OBS_INSTANCENUM] = ""+instanceNum;
						rotatedOutput.writeNext(nextOutputLine);

						nextOutputLine[OBS_MODIFIERTYPE] = "";
						nextOutputLine[OBS_SIC] =  "";
					} 
					columnCounter++;
				}
				k++;
			}
			//			StatusListener.setSubStatus(0, "");
			rotatedOutput.close();
			CSVInput.close();

			long end = System.currentTimeMillis() - start;
			System.out.println("dura: " + end/1000 + "."+end%1000);
			long abc = 1;
			if (end/1000 >0)
				abc=end/1000;
			long speed = end/1000;
			if (speed <=0)
				speed = 1;
			System.out.println("Speed: " + rows/abc + " rows/s");
			System.out.println("Rotate done.");

			System.out.println("Rotate done.");
			//global++;
			//setGlobalToDB(global, SourcesystemName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}}

	/**
	 * 
	 * @param input
	 * @param output
	 * @param inputDelim
	 * @param outputDelim
	 * @param PIDCol
	 * @param timeStampCol
	 * @param encounterIDCol
	 * @param replacePID
	 * @param sourceSystem
	 */
	//	public void rotateData (File input, File output, char inputDelim, char outputDelim, String PIDCol, String timeStampCol, String encounterIDCol, String updateDate, String importDate, String downloadDate, boolean replacePID,
	//			String sourceSystem){
	//		try {
	//			int PID = -1;
	//			int timeStamp = -1;
	//			int encounterID = -1;
	//			CSVReader CSVInput = new CSVReader(new FileReader(input), inputDelim);
	//			String []nextLine = CSVInput.readNext();
	//
	//			for (int i = 0; i < nextLine.length;i++) {
	//				if (nextLine[i].equals(PIDCol)){
	//					PID = i;
	////					System.out.println(i);
	//				}
	//				else if (nextLine[i].equals(timeStampCol)){
	//					timeStamp = i;
	//				}
	//				else if (nextLine[i].equals(encounterIDCol)){
	//					encounterID = i;
	//				}
	//			}
	//
	//			if (PID < 0 || timeStamp < 0 || encounterID < 0){
	//				System.out.println("not found");
	//			}
	//			else{
	//				rotateData(input, output, inputDelim, outputDelim, PID, timeStamp, encounterID, replacePID, sourceSystem);
	//			}
	//		} catch (Throwable e) {
	//			e.printStackTrace();
	//		} 
	//	}

	/**
	 * 
	 * @param input Input File
	 * @param output Output File
	 * @param inputDelim Delimiter of Input File, Default: ;
	 * @param outputDelim Delimiter of Output File (your choice)
	 * @param PID Column with Patient Identifier, Default: 0
	 * @param replacePID replace the PID?
	 */
	//	public void rotateData(File input, File output, char inputDelim, char outputDelim, int PID, int timeStamp, int encounterID, boolean replacePID,
	//			String sourceSystem){
	//		try {
	//			//int global = getGlobalFromDB(sourceSystem);
	//			HashSet<String> testSet = new HashSet<String>();
	//			CSVReader CSVInput = new CSVReader(new FileReader(input), inputDelim);
	//			CSVWriter rotatedOutput = new CSVWriter(new FileWriter(output), outputDelim);
	//
	//			String []nextLine = CSVInput.readNext();
	//
	//			if (nextLine.length<PID || nextLine.length < timeStamp)
	//			{
	//				throw new Exception("PID-Column or timestamp Column out of bounds, check PID again");
	//			}
	//			/**
	//			 * Wrong delimiter?
	//			 */
	//			if (nextLine.length==1){
	//				System.err.println("Wrong delimiter?");
	//				if (inputDelim==DEFAULTDELIM){
	//					inputDelim='\t';
	//					System.err.println("Delimiter set to: \\t");
	//				}
	//				else{
	//					inputDelim=DEFAULTDELIM;
	//					System.err.println("Delimiter set to: " + DEFAULTDELIM);
	//				}
	//				CSVInput.close();
	//				CSVInput = new CSVReader(new FileReader(input), inputDelim);
	//				nextLine = CSVInput.readNext();
	//
	//			}
	//			/**
	//			 * get ColumnNames
	//			 */
	//			List<String> columnNames = new LinkedList<String>();
	//			for (String string : nextLine) {
	//
	//				if (columnNames.contains(string)){
	//					while (columnNames.contains(string)){
	//						System.err.println("Duplicate entry found, renamed " + string + " to " + string+"_");
	//						string+="_";
	//					}
	//				}
	//				columnNames.add(string);
	//			}
	//			/**
	//			 * writing header
	//			 */
	//			String [] header = new String[6];
	//			header[0] = "PatientID";
	//			header[1] = "itemID";
	//			header[2] = "itemValue";
	//			header[3] = "timestamp";
	//			header[4] = "encounterID";
	//			header[5] = "uniqueID";
	//			rotatedOutput.writeNext(header);
	//			/**
	//			 * writing content
	//			 * 
	//			 */
	//			while((nextLine = CSVInput.readNext()) != null){
	//				int columnCounter = 0;
	//				Iterator<String> coulumnNameIterator = columnNames.iterator();
	//				String currentColumn = "";
	//				while(coulumnNameIterator.hasNext()){
	//
	//					currentColumn = coulumnNameIterator.next();
	//					/**
	//					 * empty columnName
	//					 */
	//					if (currentColumn.isEmpty()){
	//						currentColumn = columnCounter+"_empty";
	//						emptyColumn = true;
	//					}
	//					/**
	//					 * skip PID Column
	//					 */
	//					if (columnCounter!=PID && columnCounter != timeStamp && columnCounter != encounterID){
	//						/**
	//						 * writing output line
	//						 */
	//						String [] nextOutputLine = new String[6];
	//						if (replacePID) {
	//							nextOutputLine[0] = convertAlphanumericPIDtoNumber(nextLine[PID]);
	//						} else {
	//							nextOutputLine[0] = nextLine[PID];
	//						}
	//						nextOutputLine[1] = currentColumn;
	//						nextOutputLine[2] = nextLine[columnCounter];
	//						nextOutputLine[3] = nextLine[timeStamp]	;
	//						nextOutputLine[4] = nextLine[encounterID];
	//						nextOutputLine[5] = sourceSystem; //+"_"+global;
	//						rotatedOutput.writeNext(nextOutputLine);
	//					} 
	//					columnCounter++;
	//				}
	//				/**
	//				 * check for duplicate pids
	//				 */
	//				if (replacePID) {
	//					if (!testSet.add(convertAlphanumericPIDtoNumber(nextLine[PID]))){
	//						throw new Exception("Duplicate PID found: " + nextLine[0] + " - " + convertAlphanumericPIDtoNumber(nextLine[PID]));
	//					}
	//				}
	//				else{
	//					if (!testSet.add(nextLine[PID])){
	//						throw new Exception("Duplicate PID found: " + nextLine[0]);
	//					}
	//				}
	//			}
	//
	//			if (emptyColumn)
	//				System.err.println("ERROR: One or more columns have no name.");
	//
	//			rotatedOutput.close();
	//			CSVInput.close();	
	//			System.out.println("Rotate done.");
	//			//global++;
	//			//setGlobalToDB(global, sourceSystem);
	//		} catch (Throwable e) {
	//			e.printStackTrace();
	//		} 
	//	}

	/**
	 * Converts an alphanumeric pid to a number.
	 * @param pid PID to convert.
	 * @return
	 */
	public static String convertAlphanumericPIDtoNumber(String pid){
		String newString = "";
		String tmpString = "";
		byte[] bytes = null;
		try {
			bytes = pid.getBytes("UTF-8");
			for(int x = 0; x < bytes.length; x++){
				tmpString = String.valueOf(bytes[x]);
				if (tmpString.length() == 3){
					// nothing
				} else if (tmpString.length() == 2){
					tmpString = "0" + tmpString;
				} 
				newString = newString + tmpString;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
		return newString;
	}

	/**
	 * Reconverts a string to number
	 * @param pid String to reconvert.
	 * @return
	 */
	public static String reconvertAlphanumericPIDfromNumber(String pid){
		if (pid.length() % 3 != 0){
			//			System.err.println("length%3 != 0");
			//			return "";
			pid = "0"+pid;
		}

		String byteString = "";
		for (int x = 0; x < pid.length(); x = x + 3){
			byteString += new String(new byte[] {Byte.parseByte(pid.substring(x, x+3))});
		}
		return byteString;
	}

	protected static void closeConnection(){
		try {
			System.out.println("closing");
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//	protected static void getConnection() throws SQLException {
	//		if (con == null || con.isClosed()){
	//			try {
	//				System.out.println("jdbc:oracle:thin:@" + HOST
	//						+ ":" + PORT + ":" + SID + " " +  USERNAME+ " " + PWD);
	//				Class.forName("oracle.jdbc.driver.OracleDriver");
	//				con = DriverManager.getConnection("jdbc:oracle:thin:@" + HOST
	//						+ ":" + PORT + ":" + SID, USERNAME, PWD);
	//
	//			} catch (Throwable e) {
	//				e.printStackTrace();
	//			} 
	//		}
	//	}

	//	public static boolean getSourceFromDB(String source) throws SQLException{
	//		//String dbName = "I2B2IDRTDEMO.IDRTHelper";
	//
	//		Statement stmt = null;
	//		boolean exists = false;
	//		try {
	//			String query = "select SOURCESYSTEM "+
	//			"from " + dbName + " where SOURCESYSTEM ='" + source + "'";
	//			stmt = con.createStatement();
	//			ResultSet rs = stmt.executeQuery(query);
	//			while (rs.next()) {
	//				exists=true;
	//				System.out.println("exists!");
	//			}
	//		} catch (SQLException e ) {
	//			//			global = getGLOBALCOUNTER();
	//			e.printStackTrace();
	//		} finally {
	//			if (stmt != null) { stmt.close(); }
	//		}
	//
	//		//		con.close();
	//		return exists;
	//	}


	//	public static int getGlobalFromDB(String source)
	//	throws SQLException {
	//		int global = 0;
	//		System.out.println("source: " + source);
	//		//String dbName = "I2B2IDRTDEMO.IDRTHelper";
	//		getConnection();
	//		if (getSourceFromDB(source)){
	//			Statement stmt = null;
	//			String query = "select GLOBALCOUNTER "+
	//			"from " + dbName + " where SOURCESYSTEM = '" + source + "'";
	//			try {
	//				stmt = con.createStatement();
	//				ResultSet rs = stmt.executeQuery(query);
	//				while (rs.next()) {
	//					global = rs.getInt("GLOBALCOUNTER");
	//					System.out.println("global: " + global);
	//				}
	//			} catch (SQLException e ) {
	//				e.printStackTrace();
	//			} finally {
	//				if (stmt != null) { stmt.close(); }
	//			}
	//		}
	//		else{
	//			System.out.println("New source: " + source);
	//			Statement stmt = null;
	//			String query = "INSERT INTO "+ dbName + "(GLOBALCOUNTER,SOURCESYSTEM) VALUES (0,'"+source+"')";
	//			try {
	//				stmt = con.createStatement();
	//				stmt.executeQuery(query);
	//			} catch (SQLException e ) {
	//				e.printStackTrace();
	//			} finally {
	//				if (stmt != null) { stmt.close(); }
	//			}
	//		}
	//		return global;
	//	}

	//	public int setGlobalToDB(int global, String source) {
	//		try {	
	//			System.out.println("sourceto: " + source);
	//
	//
	//			//		try {
	//			getConnection();
	//			//		} catch (SQLException e1) {
	//			//			e1.printStackTrace();
	//			//		}
	//			Statement stmt = null;
	//			String query = "UPDATE " + dbName + " SET GLOBALCOUNTER='" + global + "' WHERE SOURCESYSTEM='"+source+"'";
	//			try {
	//				stmt = con.createStatement();
	//				stmt.executeQuery(query);
	//			} catch (SQLException e ) {
	//				e.printStackTrace();
	//			} finally {
	//				if (stmt != null)
	//				{stmt.close();} 
	//			}
	//		}catch (SQLException e) {
	//			e.printStackTrace();
	//		}
	//		System.out.println("newglobal: " + global);
	//		setGLOBALCOUNTER(global);
	//		return global;
	//	}

	/**
	 * @return the gLOBALCOUNTER
	 */
	public static int getGLOBALCOUNTER() {
		Preferences prefsRoot = Preferences.userRoot();
		Preferences myPrefs = prefsRoot.node("IDRT");

		return myPrefs.getInt("GLOBALCOUNTER", 0);
	}
	/**
	 * @param gLOBALCOUNTER the gLOBALCOUNTER to set
	 */
	public static void setGLOBALCOUNTER(int gLOBALCOUNTER) {
		Preferences prefsRoot = Preferences.userRoot();
		Preferences myPrefs = prefsRoot.node("IDRT");
		myPrefs.putInt("GLOBALCOUNTER", gLOBALCOUNTER);
	}
}
class IDRTItem {

	private String columnName;
	private String dataType;
	private String niceName;

	public IDRTItem(String columnName, String dataType, String niceName) {
		this.setColumnName(columnName);
		this.setDataType(dataType);
		this.setNiceName(niceName);
	}

	public String getNiceName() {
		return niceName;
	}

	public void setNiceName(String niceName) {
		this.niceName = niceName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	@Override
	public String toString() {
		return columnName + " " + niceName + " " + dataType;
	}



}

