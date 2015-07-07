package de.umg.mi.idrt.ioe;

import java.awt.Color;
import java.io.File;
import java.util.Properties;

import org.eclipse.swt.widgets.Display;

import de.umg.mi.idrt.ioe.view.OntologyEditorView;
import de.umg.mi.idrt.ioe.view.StatusView;

/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> Department of
 *         Medical Informatics Goettingen www.mi.med.uni-goettingen.de
 * 
 * 
 */

public class Resource {

	public class DataType {

		public final static String STRING = "string";
		public final static String INTEGER = "integer";
		public final static String FLOAT = "float";
		public final static String DATE = "date";

	}
	public class Files {

		public static final String TEMP_FOLDER = "temp";
		public static final String TEMP_TOS_CONNECTOR_FILE = "temp_TOS_Connector_file.csv";

	}
	public class Global {



		public String TEXT_FILE = "cfg/english.lf";
		public Properties TEXT = new Properties();


		public final Color COLOR_SUCCESS = new Color(80, 252, 74);
		public final static String COLOR_SUCCESS_HTML = "50FC4A";
		public final static String COLOR_SUCCESS_HTML_LIGHT = "D3FDD2";
		public final Color COLOR_ERROR = new Color(252, 74, 74);
		public final static String COLOR_ERROR_HTML = "FC4A4A";
		public final static String COLOR_ERROR_HTML_LIGHT = "FDD3D3";
		public final Color COLOR_INFO = new Color(200, 200, 230);
		public final static String COLOR_INFO_HTML = "C8C8E6";
		public final static String COLOR_INFO_HTML_LIGHT = "E4E4F6";
		
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



	}
	public class GUI {

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

		public final String[] editorTargetInfoViewRows = { "*id", "*treePath",
				"*treePathLevel", Resource.I2B2.NODE.TARGET.C_NAME };
	}
	public class I2B2 {

		public class NODE {

			public class TARGET {

				public final static String TARGET_ID = "TARGET_ID";
				public final static String TREE_LEVEL = "TREE_LEVEL";
				public final static String TREE_PATH = "TREE_PATH";
				public final static String STAGING_PATH = "STAGING_PATH";
				public final static String STAGING_DIMENSION = "STAGING_DIMENSION";
//				public final static String NAME = "NAME";
				public final static String STARTDATE_STAGING_PATH = "STARTDATE_STAGING_PATH";
				public final static String ENDDATE_STAGING_PATH = "ENDDATE_STAGING_PATH";
				public final static String VISUALATTRIBUTES = "VISUALATTRIBUTES";
				
				// new table columns
				
				public final static String	C_HLEVEL = "C_HLEVEL";
				public final static String 	C_FULLNAME = "C_FULLNAME";
				public final static String	C_NAME = "C_NAME";
				public final static String	C_SYNONYM_CD = "C_SYNONYM_CD";
				public final static String	C_VISUALATTRIBUTES = "C_VISUALATTRIBUTES";
				public final static String	C_TOTALNUM = "C_TOTALNUM";
				public final static String	C_BASECODE = "C_BASECODE";
				public final static String	C_METADATAXML = "C_METADATAXML";
				public final static String	C_FACTTABLECOLUMN = "C_FACTTABLECOLUMN";
				public final static String	C_TABLENAME = "C_TABLENAME";
				public final static String	C_COLUMNNAME = "C_COLUMNNAME";
				public final static String	C_COLUMNDATATYPE = "C_COLUMNDATATYPE";
				public final static String	C_OPERATOR = "C_OPERATOR";
				public final static String	C_DIMCODE = "C_DIMCODE";
				public final static String	C_COMMENT = "C_COMMENT";
				public final static String	C_TOOLTIP = "C_TOOLTIP";
				public final static String	M_APPLIED_PATH = "M_APPLIED_PATH";
				public final static String	UPDATE_DATE = "UPDATE_DATE";
				public final static String	DOWNLOAD_DATE = "DOWNLOAD_DATE";
				public final static String	IMPORT_DATE = "IMPORT_DATE";
				public final static String	SOURCESYSTEM_CD = "SOURCESYSTEM_CD";
				public final static String	VALUETYPE_CD = "VALUETYPE_CD";
				public final static String	M_EXCLUSION_CD = "M_EXCLUSION_CD";
				public final static String	C_PATH = "C_PATH";
				public final static String	C_SYMBOL = "C_SYMBOL";
				public static final String STAGING_M_APPLIED_PATH = "STAGING_M_APPLIED_PATH";

			}

			public class TYPE {

				public final static String ONTOLOGY_SOURCE = "ONTOLOGY_SOURCE";
				public final static String ONTOLOGY_TARGET = "ONTOLOGY_TARGET";
				public final static String UNSPECIFIC = "UNSPECIFIC";
				public final static String ROOT = "ROOT";

			}

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
				public final static String SEC_OBJ = "SECURE_OBJ_TOKEN";

			}

		}
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
		public final static String I2B2_HEADPATH = "i2b2";

		public final static String I2B2_PATH_SEPERATOR = "\\";

		public final static String I2B2_CONCEPT_PATH_SEPERATOR = "|";

		public final static String I2B2_CONCEPT_VALUE_SEPERATOR = ":";

		public final static int DATABASE_MAX_TVAL_CHAR_LENGTH = 255;

	}
	public class ID {

		public class Command {

			public class IOE {

				public final static String STAGINGTOTARGET = "de.umg.mi.idrt.ioe.command.etlStagingI2B2ToTargetI2B2";
				public final static String LOADEVERYTHING = "de.umg.mi.idrt.ioe.LoadEverything";
				public final static String LOADSTAGINGONTOLOGY = "de.umg.mi.idrt.ioe.LoadStagingtOntology";
				public final static String LOADTARGETONTOLOGY = "de.umg.mi.idrt.ioe.LoadTargetOntology";
				public final static String LOADTARGETONTOLOGY_ATTRIBUTE_VERSION = "de.umg.mi.idrt.ioe.LoadTargetOntologyAttribute.Version";
				public final static String LOADTARGETPROJECTS = "de.umg.mi.idrt.ioe.LoadTargetProjects";
				public final static String SAVETARGETPROJECT = "de.umg.mi.idrt.ioe.SaveTargetProject";
				public final static String SAVETARGET = "de.umg.mi.idrt.ioe.SaveTarget";
				public final static String DELETETARGET = "de.umg.mi.idrt.ioe.DeleteTarget";
				public final static String DELETETARGET_ATTRIBUTE_TARGETID = "de.umg.mi.idrt.ioe.DeleteTargetAttribute.TargetID";
				public final static String INCREMENTTARGETVERSION = "de.umg.mi.idrt.ioe.IncrementTargetVersion";

			}
			public class TOS {

				public final static String WRITE_TARGET_ONTOLOGY = "WriteTargetOntology";
				public final static String DELETE_TARGET = "DeleteTarget";
				public final static String READ_STAGING_ONTOLOGY = "ReadStagingOntology";

			}
			public final static String OTCOPY = "edu.goettingen.i2b2.importtool.command.OTCopy";

			public final static String OTCOPY_ATTRIBUTE_SOURCE_NODE_PATH = "edu.goettingen.i2b2.importtool.command.OTCopy.Source";
			public final static String OTCOPY_ATTRIBUTE_TARGET_NODE_PATH = "edu.goettingen.i2b2.importtool.command.OTCopy.Target";
			public final static String OTSETTARGETATTRIBUTE = "edu.goettingen.i2b2.importtool.command.OTSetTargetAttribute";
			public final static String OTSETTARGETATTRIBUTE_ATTRIBUTE_SOURCE_NODE_PATH = "edu.goettingen.i2b2.importtool.command.OTSetTargetAttribute.Source";

			public final static String OTSETTARGETATTRIBUTEY_ATTRIBUTE_TARGET_NODE_PATH = "edu.goettingen.i2b2.importtool.command.OTSetTargetAttribute.Target";
			public final static String OTSETTARGETATTRIBUTEY_ATTRIBUTE_ATTRIBUTE = "edu.goettingen.i2b2.importtool.command.OTSetTargetAttribute.Attribute";

			public final static String DELETENODE = "de.umg.mi.idrt.ioe.deletenode";

			public final static String COMBINENODE = "de.umg.mi.idrt.ioe.combineNodes";

		}

		public class TOS {

			public class ContextVariable {
				public final static String JOB = "Job";
				public final static String DATA_FILE = "DataFile";
				public final static String TARGETID = "TargetID";
			}

		}

		public class View {

			public final static String MAIN_VIEW = "edu.goettingen.i2b2.importtool.view.MainView";
			public final static String STATUS_VIEW = "edu.goettingen.i2b2.importtool.view.StatusView";
			public final static String SERVER_VIEW = "de.umg.mi.idrt.importtool.ServerView";

			public final static String ONTOLOGY_VIEW = "de.umg.mi.idrt.ioe.OntologyEditor";
			public final static String ONTOLOGY_NODEEDITOR_VIEW = "edu.goettingen.i2b2.importtool.view.OntologyNodeEditorView";
			public final static String ONTOLOGY_ANSWERSPREVIEW_VIEW = "edu.goettingen.i2b2.importtool.view.OntologyAnswersPreviewView";
			public final static String ONTOLOGY_ANSWERSTEMPLATES_VIEW = "edu.goettingen.i2b2.importtool.view.OntologyAnswersTemplatesView";


		}

	}

	public class ODM {

		public static final String CONCEPTCODE_PATH_SEPARATOR = "|";
		public static final String CONCEPTCODE_CODE_SEPARATOR = ":";
		public static final String PATH_SEPARATOR = "\\";

	}
	public class OntologyTree {

		public static final String HAS_START_DATE = IMAGE_PATH
				+ "hasStartDate16.gif";
		public static final String HAS_END_DATE = IMAGE_PATH
				+ "hasEndDate16.gif";
		public static final String HAS_START_END_DATE = IMAGE_PATH
				+ "hasSEDate16.gif";
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
		public static final String VISIBILITY_ICON_DA = IMAGE_PATH
				+ "i2b2_da.gif";
		public static final String VISIBILITY_ICON_RA = IMAGE_PATH
				+ "i2b2_ra.gif";
		public static final String SHOW_SUB_NODES = IMAGE_PATH
				+ "showSubNodes.gif";

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
		public static final String LINK_ICON = IMAGE_PATH + "arrow.gif";
	}
	public static class OntologyTreeHelpers {

		public static class PathAndID {

			public PathAndID(String path, String id) {
				_id = id;
				_path = path;
			}
			String _id = "";

			String _path = "";

			public String getID() {
				return _id;
			}

			public String getParentPath() {
				return _path;
			}

		}

		// i2b2 paths already have the last backslash
		public static String addToI2B2Path(String path, String newID) {
			if (path.endsWith("\\"))
				return path + newID;
			else
				return path + Resource.ODM.PATH_SEPARATOR + newID;
		}

		public static String addToPath(String path, String newID) {
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


	}

	public class Options {

		public final static int EDITOR_SOURCE_TREE_OPENING_LEVEL = 8;

	}
//	public class TOSConnector {

//		public class Command {

//			public static final String CHECK_ONTOLOGY_EMPTY = "check_ontology_empty";

//		}

//		public static void killThread() {
			// TODO Auto-generated method stub
			
//		}

//	}
	public static final String ICON_PATH = "icons" + File.separator;
	public static final String IMAGE_PATH = "/images/";
	public static final String ICO_WORKING = ICON_PATH + "working.png";
	public static final String ICO_NOT_WORKING = ICON_PATH + "not_working.png";
	public StatusView _statusView = null;
	public OntologyEditorView _ontologyView = null;
	public Display _display = null;

	public String TEXT_FILE = "cfg/english.lf";

	public Properties TEXT = new Properties();

	public final static String COLOR_INFO_HTML_LIGHT = "E4E4F6";

	public final static String COLOR_INFO_HTML = "C8C8E6";

	public final static Color COLOR_INFO = new Color(200, 200, 230);

	public final static String COLOR_ERROR_HTML_LIGHT = "FDD3D3";

	public final static String COLOR_ERROR_HTML = "FC4A4A";

	public final static Color COLOR_ERROR = new Color(252, 74, 74);

	public final static String COLOR_SUCCESS_HTML_LIGHT = "D3FDD2";

	public final static String COLOR_SUCCESS_HTML = "50FC4A";

	public final static Color COLOR_SUCCESS = new Color(80, 252, 74);

	public OntologyEditorView getOntologyView() {
		return _ontologyView;
	}


	public StatusView getStatusView() {
		return _statusView;
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

	public void setOntologyView(OntologyEditorView ontologyView) {
		this._ontologyView = ontologyView;
	}

	public void setStatusView(StatusView statusView) {
		this._statusView = statusView;
	}

}
