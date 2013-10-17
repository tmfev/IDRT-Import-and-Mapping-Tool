package de.umg.mi.idrt.ioe;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import javax.swing.JComponent;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.IHandlerService;

import de.umg.mi.idrt.ioe.view.EditorSourceInfoView;
import de.umg.mi.idrt.ioe.view.EditorSourceView;
import de.umg.mi.idrt.ioe.view.EditorTargetInfoView;
import de.umg.mi.idrt.ioe.view.EditorTargetView;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;
import de.umg.mi.idrt.ioe.view.OntologyView;
import de.umg.mi.idrt.ioe.view.StatusView;

public class Resource {

	public static final String ICON_PATH = "icons" + File.separator;
	public static final String IMAGE_PATH = "/images/";
	public static final String ICO_WORKING = ICON_PATH + "working.png";
	public static final String ICO_NOT_WORKING = ICON_PATH + "not_working.png";
	public StatusView _statusView = null;
	public OntologyEditorView _ontologyView = null;
	public I2B2ImportTool _i2b2ImportTool = null;
	public Display _display = null;
	public String TEXT_FILE = "cfg/english.lf";
	public Properties TEXT = new Properties();
	public Settings SETTINGS = new Settings();
	public String test = "test0";

	private ActionCommand _actionCommand;
	private EditorSourceView _editorSourceView;
	private EditorSourceInfoView _editorSourceInfoView;
	private EditorTargetView _editorTargetView;
	private EditorTargetInfoView _editorTargetInfoView;
	public final static String COLOR_INFO_HTML_LIGHT = "E4E4F6";
	public final static String COLOR_INFO_HTML = "C8C8E6";
	public final static Color COLOR_INFO = new Color(200, 200, 230);
	public final static String COLOR_ERROR_HTML_LIGHT = "FDD3D3";
	public final static String COLOR_ERROR_HTML = "FC4A4A";
	public final static Color COLOR_ERROR = new Color(252, 74, 74);
	public final static String COLOR_SUCCESS_HTML_LIGHT = "D3FDD2";
	public final static String COLOR_SUCCESS_HTML = "50FC4A";
	public final static Color COLOR_SUCCESS = new Color(80, 252, 74);

	public Resource() {
		Debug.c("Resource");
		URL i = Platform.getBundle("de.umg.mi.idrt.ioe").getEntry("/");
		URL url = null;
		try {
			url = FileLocator.toFileURL(i);
			// Debug.d("!HERE" + url.getPath());
		} catch (IOException e) {
			Console.error(e.toString() + " @Resource Code1");
		}

		try {
			TEXT.load(new FileInputStream(url.getPath() + this.TEXT_FILE));
		} catch (FileNotFoundException e) {
			Console.error(e.toString() + " @Resource Code2");
		} catch (IOException e) {
			Console.error(e.toString() + " @Resource Code3");
		}
	}

	public I2B2ImportTool getI2B2ImportTool() {
		return _i2b2ImportTool;
	}

	public void setI2B2ImportTool(I2B2ImportTool i2b2ImportTool) {
		this._i2b2ImportTool = i2b2ImportTool;
	}

	public StatusView getStatusView() {
		return _statusView;
	}

	public void setStatusView(StatusView statusView) {
		this._statusView = statusView;
	}

	public OntologyEditorView getOntologyView() {
		return _ontologyView;
	}

	public void setOntologyView(OntologyEditorView ontologyView) {
		this._ontologyView = ontologyView;
	}

	public EditorSourceView getEditorSourceView() {
		return _editorSourceView;
	}

	public void setEditorSourceView(EditorSourceView editorSourceView) {
		this._editorSourceView = editorSourceView;
	}

	public EditorSourceInfoView getEditorSourceInfoView() {
		return _editorSourceInfoView;
	}

	public void setEditorSourceInfoView(
			EditorSourceInfoView editorSourceInfoView) {
		this._editorSourceInfoView = editorSourceInfoView;
	}

	public EditorTargetView getEditorTargetView() {
		return _editorTargetView;
	}

	public void setEditorTargetView(EditorTargetView editorTargetView) {
		this._editorTargetView = editorTargetView;
	}

	public EditorTargetInfoView getEditorTargetInfoView() {
		return _editorTargetInfoView;
	}

	public void setEditorTargetInfoView(
			EditorTargetInfoView editorTargetInfoView) {
		this._editorTargetInfoView = editorTargetInfoView;
	}



	public static void addMessage(String message,
			SystemMessage.MessageType messageType,
			SystemMessage.MessageLocation messageLocation) {
		Application.getStatusView().addMessage(message, messageType,
				messageLocation);
	}
	
	public class Files {
		
		public static final String TEMP_FOLDER = "temp";
		public static final String TEMP_TOS_CONNECTOR_FILE = "temp_TOS_Connector_file.csv";
	
		
	}

	public class OntologyTree {

		public static final String ICON_ITEM = IMAGE_PATH + "i2b2_itemEdit.gif";
		public static final String ICON_ANSWER = IMAGE_PATH + "answer.gif";
		public static final String ICON_ANSWERGROUP = IMAGE_PATH
				+ "answergroup.gif";

		public static final String VISIBILITY_ICON_FA = IMAGE_PATH
				+ "i2b2_fa.gif";
		public static final String VISIBILITY_ICON_FI = IMAGE_PATH
				+ "i2b2_fi.gif";
		public static final String VISIBILITY_ICON_CA = IMAGE_PATH
				+ "i2b2_ca.gif";
		public static final String VISIBILITY_ICON_CI = IMAGE_PATH
				+ "i2b2_ci.gif";
		public static final String VISIBILITY_ICON_MA = IMAGE_PATH
				+ "i2b2_ma.gif";
		public static final String VISIBILITY_ICON_MI = IMAGE_PATH
				+ "i2b2_mi.gif";
		public static final String VISIBILITY_ICON_LA = IMAGE_PATH
				+ "i2b2_la.jpg";
		public static final String VISIBILITY_ICON_LI = IMAGE_PATH
				+ "i2b2_li.gif";

		public static final String ITEMSTATUS_ICON_UNCHECKED = IMAGE_PATH
				+ "itemstatus-unchecked.png";
		public static final String ITEMSTATUS_ICON_CHECKED_BY_AUTOMATION_UNSURE = IMAGE_PATH
				+ "itemstatus-checkedbyautomationunsure.png";
		public static final String ITEMSTATUS_ICON_CHECKED_BY_AUTOMATION_SURE = IMAGE_PATH
				+ "itemstatus-checkedbyautomationsure.png";
		public static final String ITEMSTATUS_ICON_CHECKED_BY_AUTOMATION_DEFINITELY = IMAGE_PATH
				+ "itemstatus-checkedbyautomationdefinitly.png";
		public static final String ITEMSTATUS_ICON_CHECKED_BY_USER = IMAGE_PATH
				+ "itemstatus-checkedbyuser.png";
		public static final String ITEMSTATUS_ICON_UNKNOWN = IMAGE_PATH
				+ "itemstatus-unchecked2.png";

		public static final String ONTOLOGYTREE_ROOTNODE_NAME = "Source";
	}

	public class ODM {

		public static final String CONCEPTCODE_PATH_SEPARATOR = "|";
		public static final String CONCEPTCODE_CODE_SEPARATOR = ":";
		public static final String PATH_SEPARATOR = "\\";

	}

	public class I2B2OracleDB {

		public static final String SQL_OPERATOR_LIKE = "LIKE";
		public static final String SQL_OPERATOR_EQUAL = "=";

		public static final String TABLE_I2B2 = "I2B2";
		public static final String TABLE_CONCEPT_DIMENSION = "CONCEPT_DIMENSION";
		public static final String TABLE_PATIENT_DIMENSION = "PATIENT_DIMENSION";
		public static final String TABLE_OBSERVATION_FACT = "OBSERVATION_FACT";

		/* i2b2 */
		public static final String COLUMN_C_HLEVEL = "C_HLEVEL";
		public static final String COLUMN_C_FULLNAME = "C_FULLNAME";
		public static final String COLUMN_C_NAME = "C_NAME";
		public static final String COLUMN_C_SYNONYM_CD = "C_SYNONYM_CD";
		public static final String COLUMN_C_VISUALATTRIBUTES = "C_VISUALATTRIBUTES";
		public static final String COLUMN_C_TOTALNUM = "C_TOTALNUM";
		public static final String COLUMN_C_BASECODE = "C_BASECODE";
		public static final String COLUMN_C_FACTTABLECOLUMN = "C_FACTTABLECOLUMN";
		public static final String COLUMN_C_TABLENAME = "C_TABLENAME";
		public static final String COLUMN_C_COLUMNNAME = "C_COLUMNNAME";
		public static final String COLUMN_C_COLUMNDATATYPE = "C_COLUMNDATATYPE";
		public static final String COLUMN_C_OPERATOR = "C_OPERATOR";
		public static final String COLUMN_C_DIMCODE = "C_DIMCODE";
		public static final String COLUMN_C_TOOLTIP = "C_TOOLTIP";
		public static final String COLUMN_UPDATE_DATE = "UPDATE_DATE";
		public static final String COLUMN_DOWNLOAD_DATE = "DOWNLOAD_DATE";
		public static final String COLUMN_IMPORT_DATE = "IMPORT_DATE";
		public static final String COLUMN_SOURCESYSTEM_CD = "SOURCESYSTEM_CD";
		public static final String COLUMN_VALUETYPE_CD = "VALUETYPE_CD";

	}

	public class TOSConnector {

		public class Command {

			public static final String CHECK_ONTOLOGY_EMPTY = "check_ontology_empty";

		}

	}

	public class ID {

		public class View {

			public final static String MAIN_VIEW = "edu.goettingen.i2b2.importtool.view.MainView";
			public final static String STATUS_VIEW = "edu.goettingen.i2b2.importtool.view.StatusView";
			public final static String SERVER_VIEW = "de.umg.mi.idrt.importtool.ServerView";

			public final static String ONTOLOGY_VIEW = "de.umg.mi.idrt.ioe.OntologyEditor";
			public final static String ONTOLOGY_NODEEDITOR_VIEW = "edu.goettingen.i2b2.importtool.view.OntologyNodeEditorView";
			public final static String ONTOLOGY_ANSWERSPREVIEW_VIEW = "edu.goettingen.i2b2.importtool.view.OntologyAnswersPreviewView";
			public final static String ONTOLOGY_ANSWERSTEMPLATES_VIEW = "edu.goettingen.i2b2.importtool.view.OntologyAnswersTemplatesView";

			public final static String EDITOR_SOURCE_VIEW = "edu.goettingen.i2b2.importtool.view.EditorSourceView";
			public final static String EDITOR_SOURCE_INFO_VIEW = "edu.goettingen.i2b2.importtool.view.EditorSourceInfoView";
			public final static String EDITOR_TARGET_VIEW = "edu.goettingen.i2b2.importtool.view.EditorTargetView";
			public final static String EDITOR_TARGET_INFO_VIEW = "edu.goettingen.i2b2.importtool.view.EditorTargetInfoView";

		}

		public class Command {
			public final static String OPEN_IMPORT_WIARD = "edu.goettingen.i2b2.importtool.commands.systemimport";
			public final static String IMPORT = "edu.goettingen.i2b2.importtool.commands.Import";
			public final static String IMPORT_FILE = "edu.goettingen.i2b2.importtool.commands.ImportFile";
			public final static String IMPORT_FILENAME = "edu.goettingen.i2b2.importtool.commands.Import.Filename";
			public final static String IMPORT_VISIBILITY_METADATAVERSION = "edu.goettingen.i2b2.importtool.commands.Import.VisibilityMetaDataVersion";
			public final static String IMPORT_VISIBILITY_STUDYEVENT = "edu.goettingen.i2b2.importtool.commands.Import.VisibilityStudyEvent";
			public final static String IMPORT_VISIBILITY_FORM = "edu.goettingen.i2b2.importtool.commands.Import.VisibilityForm";
			public final static String EXPORT = "edu.goettingen.i2b2.importtool.commands.Export";
			public final static String EXPORT_ATTRIBUTE_RESTRICT_NUMBER_OF_PATIENTS = "edu.goettingen.i2b2.importtool.commands.Export.Parameter.RestrictToNumberOfPatients";
			public final static String EXPORT_ATTRIBUTE_NUMBER_OF_PATIENTS = "edu.goettingen.i2b2.importtool.commands.Export.Parameter.NumberOfPatients";
			public final static String EXPORT_ATTRIBUTE_SCRAMBLE = "edu.goettingen.i2b2.importtool.commands.Export.Scramble";
			public final static String EXPORT_ATTRIBUTE_SECOND_IDENT = "edu.goettingen.i2b2.importtool.commands.Export.SecondIdent";
			public final static String EXPORT_ATTRIBUTE_SECOND_IDENT_LAB = "edu.goettingen.i2b2.importtool.commands.Export.SecondIdent.Lab";
			public final static String EXPORT_ATTRIBUTE_WINDOWS_FILE = "edu.goettingen.i2b2.importtool.commands.Export.WindowsFile";
			public final static String EXPORT_ATTRIBUTE_LINUX_FILE = "edu.goettingen.i2b2.importtool.commands.Export.LinuxFile";
			public final static String EXPORT_ATTRIBUTE_DB_NAME = "edu.goettingen.i2b2.importtool.commands.Export.DatabaseName";
			public final static String EXPORT_ONTOLOGY = "edu.goettingen.i2b2.importtool.commands.Export.Ontology";

			public final static String OTCOPY = "edu.goettingen.i2b2.importtool.command.OTCopy";
			public final static String OTCOPY_ATTRIBUTE_SOURCE_NODE_PATH = "edu.goettingen.i2b2.importtool.command.OTCopy.Source";
			public final static String OTCOPY_ATTRIBUTE_TARGET_NODE_PATH = "edu.goettingen.i2b2.importtool.command.OTCopy.Target";

			public final static String OTSETTARGETATTRIBUTE = "edu.goettingen.i2b2.importtool.command.OTSetTargetAttribute";
			public final static String OTSETTARGETATTRIBUTE_ATTRIBUTE_SOURCE_NODE_PATH = "edu.goettingen.i2b2.importtool.command.OTSetTargetAttribute.Source";
			public final static String OTSETTARGETATTRIBUTEY_ATTRIBUTE_TARGET_NODE_PATH = "edu.goettingen.i2b2.importtool.command.OTSetTargetAttribute.Target";
			public final static String OTSETTARGETATTRIBUTEY_ATTRIBUTE_ATTRIBUTE = "edu.goettingen.i2b2.importtool.command.OTSetTargetAttribute.Attribute";
			public final static String DELETENODE = "de.umg.mi.idrt.ioe.deletenode";

			public class IEO {
				public final static String ETLSTAGINGI2B2TOTARGETI2B2 = "de.umg.mi.idrt.ioe.command.etlStagingI2B2ToTargetI2B2";
				public final static String LOADTARGETPROJECTS = "de.umg.mi.idrt.ioe.LoadTargetProjects";
				
			}

		}

		public class OntologyTreeEditor {

			public class Command {

				public final static String EXPORT = "edu.goettingen.i2b2.importtool.OntologyTreeEditor.command.Export";

				public final static String INPUT_LOW = "low";
				public final static String INPUT_HIGH = "high";
				public final static String CREATE_ANSWERS_INTEGER = "edu.goettingen.i2b2.importtool.OntologyTreeEditor.command.CreateAnswersInteger";

				public final static String IMPORT = "edu.goettingen.i2b2.importtool.OntologyTreeEditor.command.Import";

			}

		}

		public class Import {
			public final static String SECOND_IDENT_GROUP = "Second_Ident";
			public final static String SECOND_IDENT_ID = "Second_Ident_ID";
			public final static String SECOND_IDENT_TYPE = "Second_Ident_Type";
			public final static String SECOND_IDENT_PATH = "Second_Ident_Path";

			public class Mode {
				public final static String ODM13 = "ODM 1.3";
				public final static String SECUTRIAL_KFO = "Secutrial KFO";
				public final static String TOS = "Talend Open Studio";
			}

		}

		public class Export {
			public final static String DATABASEB_STRING = "Database_String";
			public final static String CREATE_WINDOWS_ORACLE_BATCH = "Create_Windows_Oracle_Batch";
			public final static String CREATE_Linux_ORACLE_BATCH = "Create_Linux_Oracle_Batch";
			public final static String SCRAMBLE_PATIENT_DATA = "Scramble_Patient_Data";
			public final static String SECOND_IDENT_PATH = "Second_Ident_Path";
		}

		public class Variables {

			public class Preferences {

				public final static String I2B2_DATABASE_NAME = "edu.goettingen.i2b2.importtool.keyword.I2B2.DATABASE_NAME";
				public final static String I2B2_ONTOLOGY_HEADPATH = "edu.goettingen.i2b2.importtool.keyword.I2B2.ONTOLOGYHEADPATH";
				public final static String LAST_IMPORTED_FILE = "edu.goettingen.i2b2.importtool.keyword.LAST_IMPORTED_FILE";
				public final static String LAST_IMPORTED_FILE1 = "edu.goettingen.i2b2.importtool.keyword.LAST_IMPORTED_FILE1";
				public final static String LAST_IMPORTED_FILE2 = "edu.goettingen.i2b2.importtool.keyword.LAST_IMPORTED_FILE2";
				public final static String LAST_IMPORTED_FILE3 = "edu.goettingen.i2b2.importtool.keyword.LAST_IMPORTED_FILE3";
				public final static String LAST_IMPORTED_FILE4 = "edu.goettingen.i2b2.importtool.keyword.LAST_IMPORTED_FILE4";
				public final static String LAST_IMPORTED_FILE5 = "edu.goettingen.i2b2.importtool.keyword.LAST_IMPORTED_FILE5";

				public final static String ORACLE_HOST = "edu.goettingen.i2b2.importtool.keyword.ORACLE_HOST";
				public final static String ORACLE_PORT = "edu.goettingen.i2b2.importtool.keyword.ORACLE_PORT";
				public final static String ORACLE_SID = "edu.goettingen.i2b2.importtool.keyword.ORACLE_SID";
				public final static String ORACLE_USERNAME = "edu.goettingen.i2b2.importtool.keyword.ORACLE_USERNAME";
				public final static String ORACLE_PASSWORD = "edu.goettingen.i2b2.importtool.keyword.ORACLE_PASSWORD";

			}

		}

	}

	public class I2B2 {

		public final static String I2B2_HEADPATH = "i2b2";
		public final static String I2B2_PATH_SEPERATOR = "\\";
		public final static String I2B2_CONCEPT_PATH_SEPERATOR = "|";
		public final static String I2B2_CONCEPT_VALUE_SEPERATOR = ":";

		public final static int DATABASE_MAX_TVAL_CHAR_LENGTH = 255;

		public class VisualAttributes {

			public final static String FOLDER = "F";
			public final static String CONTAINER = "C";
			public final static String MULTIPLE = "M";
			public final static String LEAF = "L";

			public final static String MODIFIER_LEAF = "R";
			public final static String MODIFIER_FOLDER = "D";
			public final static String MODIFIER_MULTIPLE = "";

			public final static String ACTIVE = "A";
			public final static String INACTIVE = "I";
			public final static String HIDDEN = "H";

			public final static String EDITABLE = "E";

		}

		public class TABLE {

			public class ONTOLOGY {

				public final static String C_HLEVEL = "C_HLEVEL";
				public final static String C_FULLNAME = "C_FULLNAME";
				public final static String C_NAME = "C_NAME";
				public final static String C_SYNONYM_CD = "C_SYNONYM_CD";
				public final static String C_VISUALATTRIBUTES = "C_VISUALATTRIBUTES";
				public final static String C_TOTALNUM = "C_TOTALNUM";
				public final static String C_BASECODE = "C_BASECODE";
				public final static String C_METADATAXML = "C_METADATAXML";
				public final static String C_FACTTABLECOLUMN = "C_FACTTABLECOLUMN";
				public final static String C_TABLENAME = "C_TABLENAME";
				public final static String C_COLUMNNAME = "C_COLUMNNAME";
				public final static String C_COLUMNDATATYPE = "C_COLUMNDATATYPE";
				public final static String C_OPERATOR = "C_OPERATOR";
				public final static String C_DIMCODE = "C_DIMCODE";
				public final static String C_COMMENT = "C_COMMENT";
				public final static String C_TOOLTIP = "C_TOOLTIP";
				public final static String M_APPLIED_PATH = "M_APPLIED_PATH";
				public final static String UPDATE_DATE = "UPDATE_DATE";
				public final static String DOWNLOAD_DATE = "DOWNLOAD_DATE";
				public final static String IMPORT_DATE = "IMPORT_DATE";
				public final static String SOURCESYSTEM_CD = "SOURCESYSTEM_CD";
				public final static String VALUETYPE_CD = "VALUETYPE_CD";
				public final static String M_EXCLUSION_CD = "M_EXCLUSION_CD";
				public final static String C_PATH = "C_PATH";
				public final static String C_SYMBOL = "C_SYMBOL";

			}

		}

		public class NODE {

			public class TARGET {

				public final static String TARGET_ID = "TARGET_ID";
				public final static String TREE_LEVEL = "TREE_LEVEL";
				public final static String TREE_PATH = "TREE_PATH";
				public final static String SOURCE_PATH = "SOURCE_PATH";
				public final static String NAME = "NAME";
				public final static String STARTDATE_SOURCE_PATH = "STARTDATE_SOURCE_PATH";
				public final static String ENDDATE_SOURCE_PATH = "ENDDATE_SOURCE_PATH";
				public final static String CHANGED = "CHANGED";
				public final static String VISUALATTRIBUTE = "VISUALATTRIBUTE";

			}

			public class TYPE {

				public final static String ONTOLOGY_SOURCE = "ONTOLOGY_SOURCE";
				public final static String ONTOLOGY_TARGET = "ONTOLOGY_TARGET";
				public final static String UNSPECIFIC = "UNSPECIFIC";
				public final static String ROOT = "ROOT";

			}

		}

	}

	public class Options {

		public final static int EDITOR_SOURCE_TREE_OPENING_LEVEL = 8;

	}

	public class GUI {

		public final String[] editorTargetInfoViewRows = { "*id", "*treePath",
				"*treePathLevel", Resource.I2B2.NODE.TARGET.NAME };

		public class EditorInfoViewTable {

			public final static String ID = "*id";
			public final static String TREEPATH = "*treePath";
			public final static String TREEPATHLEVEL = "*treePathLevel";
			public final static String TYPE = "*type";
			public final static String NODETYPE = "*nodetype";
			public final static String SOURCE_PATH = "SOURCE_PATH";
			public final static String NAME = "NAME";
			public final static String CHANGED = "CHANGED";
			public final static String STARTDATE_PATH = "STARTDATE_PATH";
			public final static String ENDDATE_PATH = "ENDDATE_PATH";
			public final static String VISUALATTRIBUTE = "VISUALATTRIBUTE";

		}
	}

	public class Import {

		public final static String SECUTRIAL_ODM_PATIENTID_ITEMGROUP = "FG.SUBJID";
		public final static String SECUTRIAL_ODM_PATIENTID_ITEM = "FF.SUBJID";
		public final static String ODM_SDSVAR_PATIENTID = "SUBJID";

	}

	public class DataType {

		public final static String STRING = "string";
		public final static String INTEGER = "integer";
		public final static String FLOAT = "float";
		public final static String DATE = "date";

	}

	public static class OntologyTreeHelpers {

		public static String addToPath(String path, String newID) {
			return path + Resource.ODM.PATH_SEPARATOR + newID;
		}

		// i2b2 paths already have the last backslash
		public static String addToI2B2Path(String path, String newID) {
			// Debug.d("addAnswerForParentPath:" + path + "   -- " + newID);
			if (path.endsWith("\\"))
				return path + newID;
			else
				return path + Resource.ODM.PATH_SEPARATOR + newID;
		}

		public static String constructPath(String[] ids) {

			String path = "";

			for (int x = 0; x < ids.length; x++) {
				path += ids[x];

				if (x < ids.length - 1)
					path += Resource.ODM.PATH_SEPARATOR;
			}

			return path;
		}

		/**
		 * Convert a Path to an ConceptCode and add a Concept at the end of it
		 * 
		 * @param path
		 *            the Path
		 * @param concept
		 *            the Concept
		 * 
		 *            return the ConceptCode
		 * 
		 */
		public static String convertPathToConceptCode(String path,
				String concept) {
			if (path != null)
				return path.replace(Resource.ODM.PATH_SEPARATOR,
						Resource.ODM.CONCEPTCODE_PATH_SEPARATOR)
						+ (!concept.isEmpty() ? Resource.ODM.CONCEPTCODE_CODE_SEPARATOR
								+ concept
								: "");
			else
				return "";
		}

		public static String addItemToConecptCode(String oldConceptCode,
				String newItem) {

			// Debug.d("addItemToConecptCode:" + oldConceptCode + "#" + newItem
			// + " -->  " + oldConceptCode.replace(
			// Resource.ODM.CONCEPTCODE_CODE_SEPARATOR,
			// Resource.ODM.CONCEPTCODE_PATH_SEPARATOR) + ( !newItem.isEmpty() ?
			// Resource.ODM.CONCEPTCODE_CODE_SEPARATOR + newItem : "" ));

			return oldConceptCode.replace(
					Resource.ODM.CONCEPTCODE_CODE_SEPARATOR,
					Resource.ODM.CONCEPTCODE_PATH_SEPARATOR)
					+ (!newItem.isEmpty() ? Resource.ODM.CONCEPTCODE_CODE_SEPARATOR
							+ newItem
							: "");
		}

		public static String replaceValueFromConecptCode(String oldConceptCode,
				String newItem) {

			// Debug.d("addItemToConecptCode:" + oldConceptCode + "#" + newItem
			// + " -->  " + oldConceptCode.replace(
			// Resource.ODM.CONCEPTCODE_CODE_SEPARATOR,
			// Resource.ODM.CONCEPTCODE_PATH_SEPARATOR) + ( !newItem.isEmpty() ?
			// Resource.ODM.CONCEPTCODE_CODE_SEPARATOR + newItem : "" ));

			int cutIndex = oldConceptCode
					.lastIndexOf(Resource.ODM.CONCEPTCODE_CODE_SEPARATOR);

			return oldConceptCode.substring(0, cutIndex)
					+ Resource.ODM.CONCEPTCODE_CODE_SEPARATOR + newItem;
		}

		public static int getLevelFromLevelPathString(String levelPathString) {
			if (!levelPathString.isEmpty())
				return Integer.valueOf(levelPathString.split(":")[0]);
			else
				return 0;
		}

		public static String getPathFromLevelPathString(String levelPathString) {
			if (!levelPathString.isEmpty()) {

				String[] splitString = levelPathString.split(":");

				if (splitString.length > 0)
					return splitString[1];

			}
			return "";
		}

		public static class LevelAndPath {

			int _level = 0;
			String _path = "";

			public LevelAndPath(int level, String path) {
				_level = level;
				_path = path;
			}

			public int getLevel() {
				return _level;
			}

			public String getPath() {
				return _path;
			}

		}

		public static class PathAndID {

			String _id = "";
			String _path = "";

			public PathAndID(String path, String id) {
				_id = id;
				_path = path;
			}

			public String getID() {
				return _id;
			}

			public String getParentPath() {
				return _path;
			}

		}

		public static PathAndID getParentPathAndIDFromI2B2Path(String path) {

			String parentPath = "";
			String id = "";

			if (path.length() < 0) {
				Console.error("Could not find a parentpath and nodeID in the given path \""
						+ path + "\", because it's not long enough.");
				return new PathAndID("", "");
			}

			path = path.substring(0, path.length() - 1);

			int separator = path.lastIndexOf("\\") + 1;

			if (separator >= path.length()) {
				Console.error("Could not find a parentpath and nodeID in the given path \""
						+ path
						+ "\", because there is nothing left right from the seperator.");
				return new PathAndID("", "");
			}

			parentPath = path.substring(0, separator);
			id = path.substring(separator, path.length());

			return new PathAndID(parentPath, id);
		}

		public static String getParentPathFromPath(String path) {
			if (path.length() < 0)
				return "";
			path = path.substring(0, path.length() - 1);

			String parentPath = path.substring(0, path.lastIndexOf("\\") + 1);

			return parentPath;
		}

		public static String getItemIDFromPath(String path) {

			if (path.length() < 1)
				return "";

			path = path.substring(0, path.length() - 1);

			if (path.lastIndexOf("\\") + 1 >= path.length())
				return "";

			String parentPath = path.substring(path.lastIndexOf("\\") + 1,
					path.length());

			return parentPath;
		}

	}

	public class Global {

		public String variable = "";

		public Debug debug = new Debug();

		public HashMap<String, JComponent> fieldComponent = new HashMap<String, JComponent>();

		public static final boolean pluginSecondIdent = false;
		public static final boolean pluginLabIDisLabID = false;
		public static final boolean convertPatientIDs = false;
		public static final boolean noI2B0Level = false;

		public String PROGRAMM_TITLE = "";
		public String FILE_EXTENSION = "csv";
		public String SQL_STUDYID = "KFO3";

		// i2b2 variables
		public final static int maxPatientNumLength = 38;

		// global variables
		/*
		 * public static SystemMessages messages = null; public static
		 * JSplitPane messagePane = null; public static JPanel messagesPanel =
		 * null; static JFrame frame = null;
		 */

		public final static String CONFIGURATION_FILE = "config.ini";
		public Properties PROPERTIES = new Properties();
		public String TEXT_FILE = "cfg/english.lf";
		public Properties TEXT = new Properties();

		public int counter = 0;

		public final Color COLOR_SUCCESS = new Color(80, 252, 74);
		public final static String COLOR_SUCCESS_HTML = "50FC4A";
		public final static String COLOR_SUCCESS_HTML_LIGHT = "D3FDD2";
		public final Color COLOR_ERROR = new Color(252, 74, 74);
		public final static String COLOR_ERROR_HTML = "FC4A4A";
		public final static String COLOR_ERROR_HTML_LIGHT = "FDD3D3";
		public final Color COLOR_INFO = new Color(200, 200, 230);
		public final static String COLOR_INFO_HTML = "C8C8E6";
		public final static String COLOR_INFO_HTML_LIGHT = "E4E4F6";

		// properties of CONFIGURATION_FILE

		public String I2B2USER = "i2b2i2b2thesis";
		public String ONTOLOGYHEADPATH = "\\i2b2";
		public boolean DELETE_OLD_ENTRIES = true;
		public boolean CREATE_ONE_SQL_FILE = true;
		public I2B2ImportTool I2B2ImportTool;
		public String DATABASE_URL = "";
		public String DATABASE_USERNAME = "";

		// not implemented
		// public static boolean romanTool = false;

		// final variables for the look of the programm
		public final Color MAIN_WINDOWS_BGCOLOR = Color.WHITE;
		public final Dimension DIMENSION_PROGRAM_MAX = new Dimension(1000, 800);
		public final Dimension DIMENSION_PROGRAM_MIN = new Dimension(1000, 800);
		public final Dimension DIMENSION_PROGRAM_PREF = new Dimension(1000, 800);
		public final Dimension ONTOLOGYEDITOR_DIMENSION_MAX = new Dimension(
				1200, 800);
		public final Dimension ONTOLOGYEDITOR_DIMENSION_MIN = new Dimension(
				800, 400);
		public final Dimension ONTOLOGYEDITOR_DIMENSION_PREF = new Dimension(
				1200, 500);

		public final Dimension NODEEDITOR_DIMENSION_MAX = new Dimension(1100,
				3000);
		public final Dimension NODEEDITOR_DIMENSION_MIN = new Dimension(800,
				200);
		public final Dimension NODEEDITOR_DIMENSION_PREF = new Dimension(1100,
				200);
		public final Color NODEEDITOR_BACKGROUND_COLOR = Color.WHITE;

		// the active component to talk to the user
		public Component activeComponent = null;

		// public static JMenu menuSettings = null;

		private Display display;

		/*
		 * public Global(I2B2ImportTool i2b2ImportTool, JFrame jFrame){
		 * this.I2B2ImportTool = i2b2ImportTool; this.frame = jFrame; messages =
		 * new SystemMessages(this.I2B2ImportTool, this.frame); }
		 * 
		 * public Global(I2B2ImportTool i2b2ImportTool, Display display){
		 * this.I2B2ImportTool = i2b2ImportTool; this.display = display;
		 * //messages = new SystemMessages(this.I2B2ImportTool, this.frame); }
		 */

		public Global() {

		}

		public void setDisplay(Display display) {
			// this.display = display;
		}

		/**
		 * Writes all changable properties to a file.
		 * 
		 */
		public void writeChangableProperties() {

			this.PROPERTIES.setProperty("I2B2.USER", this.I2B2USER);
			this.PROPERTIES.setProperty("I2B2.ONTOLOGYHEADPATH",
					this.ONTOLOGYHEADPATH);
			this.PROPERTIES.setProperty("I2B2.DELETE_OLD_ENTRIES",
					String.valueOf(this.DELETE_OLD_ENTRIES));
			this.PROPERTIES.setProperty("I2B2.CREATE_ONE_SQL_FILE",
					String.valueOf(this.CREATE_ONE_SQL_FILE));
			this.PROPERTIES.setProperty("I2B2.DATABASE_URL",
					String.valueOf(this.DATABASE_URL));
			this.PROPERTIES.setProperty("I2B2.DATABASE_USERNAME",
					String.valueOf(this.DATABASE_USERNAME));
			try {
				this.PROPERTIES.store(new FileOutputStream(
						Global.CONFIGURATION_FILE), null);
				Resource.addMessage("Properties saved.",
						SystemMessage.MessageType.SUCCESS,
						SystemMessage.MessageLocation.MAIN);
			} catch (IOException e) {
				Resource.addMessage("Error in writing properties to file. ("
						+ e.toString() + ")", SystemMessage.MessageType.ERROR,
						SystemMessage.MessageLocation.MAIN);
			}
		}

		/**
		 * Sets all the changable vars in this class.
		 * 
		 * @param userName
		 * @param ontologyHeadPath
		 * @param deleteOldEntries
		 * @param createOneSqlFile
		 * @param databaseURL
		 * @param databaseUsername
		 */
		public void setChangeableGlobal(String userName,
				String ontologyHeadPath, boolean deleteOldEntries,
				boolean createOneSqlFile, String databaseURL,
				String databaseUsername) {

			this.I2B2USER = userName;
			this.ONTOLOGYHEADPATH = ontologyHeadPath;
			this.DELETE_OLD_ENTRIES = deleteOldEntries;
			this.CREATE_ONE_SQL_FILE = createOneSqlFile;
			this.DATABASE_URL = databaseURL;
			this.DATABASE_USERNAME = databaseUsername;

		}

		public String getTextFromProperties(String textID) {
			return (String) this.PROPERTIES.get(textID);
		}

		public String getText(String textID) {
			String text = (String) this.TEXT.get(textID);
			if (text == null || text.isEmpty()) {
				return "No text found in the configuration file. (TextID="
						+ textID + ")";
			} else {
				return text;
			}
		}

		public String getText(String textID, Object value) {
			String text = getText(textID);
			text = text.replaceFirst("#", value.toString());
			return text;
		}

		public String getText(String textID, Object value1, Object value2) {
			String text = getText(textID);
			text = text.replaceFirst("#", value1.toString());
			text = text.replaceFirst("#", value2.toString());
			return text;
		}

		public String getText(String textID, Object value1, Object value2,
				Object value3) {
			String text = getText(textID);
			text = text.replaceFirst("#", value1.toString());
			text = text.replaceFirst("#", value2.toString());
			text = text.replaceFirst("#", value3.toString());
			return text;
		}

		public void registerField(String name, JComponent component) {
			fieldComponent.put(name, component);
		}

		public JComponent getField(String name) {
			JComponent component = fieldComponent.get(name);
			fieldComponent.remove(name);
			return component;
		}

	}

	public String getText(String textID) {
		String text = (String) this.TEXT.get(textID);
		if (text == null || text.isEmpty()) {
			return "No text found in the configuration file. (TextID=" + textID
					+ ")";
		} else {
			return text;
		}
	}

	public String getText(String textID, Object value) {
		String text = getText(textID);
		text = text.replaceFirst("#", value.toString());
		return text;
	}

	public String getText(String textID, Object value1, Object value2) {
		String text = getText(textID);
		text = text.replaceFirst("#", value1.toString());
		text = text.replaceFirst("#", value2.toString());
		return text;
	}

	public String getText(String textID, Object value1, Object value2,
			Object value3) {
		String text = getText(textID);
		text = text.replaceFirst("#", value1.toString());
		text = text.replaceFirst("#", value2.toString());
		text = text.replaceFirst("#", value3.toString());
		return text;
	}

	public void setActionCommand(ActionCommand actionCommand) {
		_actionCommand = actionCommand;
	}

	public ActionCommand getActionCommand() {
		return _actionCommand;
	}

	public ActionCommand getActionCommand(String commandID) {

		if (!hasActionCommand()) {
			Debug.e("No active ActionCommand '" + commandID + "' found.");
			return null;
		}

		if (!commandID.equals(_actionCommand.getCommandID())) {
			Debug.e("Active ActionCommand isn't '" + commandID + "' but '"
					+ _actionCommand.getCommandID() + "'.");
		}

		return _actionCommand;
	}

	public boolean hasActionCommand() {
		return (_actionCommand != null);
	}

	public void removeActionCommand() {
		_actionCommand = null;
	}

	public Settings getSettings() {
		return SETTINGS;
	}

	public static class Settings {

		public String DB_NAME = "db_name_string";
		private LinkedList<String> lastImportedFiles = new LinkedList<String>();

		public void setDatabaseName(String dbName) {
			DB_NAME = dbName;
		}

		public String getDatabaseName() {
			return DB_NAME;
		}

		public void addLastOpenedFile(String filename) {
			Debug.d("addingFile", filename);
			// remove filename if already in the list
			if (lastImportedFiles.contains(filename)) {
				lastImportedFiles.remove(filename);
				Debug.d("- removed", filename);
			}

			lastImportedFiles.addFirst(filename);

			// delete
			if (lastImportedFiles.size() > 5) {
				Debug.d("- removed>5", filename);
				lastImportedFiles.removeLast();
			}

			try {
				IPreferenceStore store = Activator.getDefault()
						.getPreferenceStore();
				int x = 0;
				Activator
						.getDefault()
						.getPreferenceStore()
						.setValue(
								Resource.ID.Variables.Preferences.LAST_IMPORTED_FILE,
								filename);
				// TODO add support for different savefiles
				lastImportedFiles.size();
				((IPersistentPreferenceStore) store).save();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public String getLastOpenedFile() {
			return Activator
					.getDefault()
					.getPreferenceStore()
					.getString(
							Resource.ID.Variables.Preferences.LAST_IMPORTED_FILE);
		}

		public String getLastOpenedFilesAt(int i) {
			if (lastImportedFiles.size() < i)
				return "";
			return lastImportedFiles.get(i);
		}

		public static String get(String id) {
			return Activator.getDefault().getPreferenceStore().getString(id);
		}

		public void set(String id, String value) {
			try {
				IPreferenceStore store = Activator.getDefault()
						.getPreferenceStore();
				Activator.getDefault().getPreferenceStore().setValue(id, value);
				((IPersistentPreferenceStore) store).save();
			} catch (IOException e) {
				Console.error(e);
				e.printStackTrace();
			}
		}

	}

	public static IHandlerService getHandlerService() {
		return (IHandlerService) Activator.getDefault().getWorkbench()
				.getService(IHandlerService.class);
	}

}
