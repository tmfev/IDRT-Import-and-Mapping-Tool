package de.umg.mi.idrt.ioe.OntologyTree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Debug;

/**
 * This is the generic node class for the ontology tree. It holds some basic
 * informations like name, stringpath, nodetype and the visual attribute for
 * I2B2.
 * 
 * @author Christian Bauer
 * @version 0.9
 */
public class OTTargetNode extends DefaultMutableTreeNode implements
		Serializable {

	/**
	 * the name of the node
	 */
	private transient String _name = "";

	/**
	 * the path to this node at the importing stage
	 */
	private transient String _sourcePath = "";

	/**
	 * the path to this node in a jTree
	 */
	private transient String _treePath = "";

	/**
	 * the node type of this node
	 */
	private transient NodeType _nodeType;

	/**
	 * the ID of this node
	 */
	private transient String _id = "";

	// i2b2 specific data

	/**
	 * the path to this node in an i2b2 export
	 */
	private transient String _i2b2Path = "";

	/**
	 * the level corresponding to the string path to this node in a jTree
	 */
	private transient int _i2b2Level = 0;

	/**
	 * the ConceptCode of this node for I2B2
	 */
	private transient String _conceptCode = "";

	/**
	 * the visual attribute of this node for I2B2
	 */
	private transient String _visualattribute = "";

	/**
	 * does this studyevent occours more than once
	 */
	private boolean _repeating = false;

	private boolean _isReferenceData = false;

	private int _additionalDataType = 0; // 0 = nothing, 1 = biomaterial, 2 =
											// imagedata

	private boolean _isVisitNode = false;

	private OntologyCellAttributes _ontologyCellAttributes = new OntologyCellAttributes();

	// patientID itemStringPath, extraID
	private HashMap<String, HashMap<String, String>> _additionalData = null;

	private boolean _isAdditionDataParent = false;

	private List<ConceptDimensionAttributes> _conceptDimensions = new ArrayList();
	private List<ModifierDimensionAttributes> _modifierDimensions = new ArrayList();

	/**
	 * Creates a generic ontology tree node.
	 * 
	 * @param name
	 *            the name of the new node
	 */
	public OTTargetNode(String name) {
		setName(name);
	}

	public OTTargetNode() {
		setName("No_Name");
	}

	/**
	 * Sets the name attribute for this node.
	 * 
	 * @param name
	 *            the name of the node
	 */
	public void setName(String name) {

		this._name = name;

	}

	/**
	 * Returns the name of this node.
	 * 
	 * @return the name of the node
	 */
	public String getName() {
		return this._name;
	}

	/**
	 * Sets the import path attribute for this node. Which is defined by
	 * original import files structure. *
	 * 
	 * @param sourcePath
	 *            the path to this node in the originial document
	 */
	public void setsourcePath(String sourcePath) {
		this._sourcePath = sourcePath;
	}

	/**
	 * Returns the import path of this node
	 * 
	 * @return the path
	 */
	public String getsourcePath() {
		return this._sourcePath;
	}

	/**
	 * Sets the stringpath attribute for this node. Which will define the
	 * hierarchy in I2B2 and is also used by MyOntologyTree to quickly finde
	 * nodes in the ontologyTree.
	 * 
	 * @param stringpath
	 *            the path to this node in a tree
	 */
	public void setTreePath(String treePath) {
		this._treePath = treePath;
	}

	/**
	 * Returns the path to this node as a string.
	 * 
	 * @return the path
	 */
	public String getI2B2Path() {
		return this._i2b2Path;
	}

	/**
	 * Sets the stringpath attribute for this node. Which will define the
	 * hierarchy in I2B2 and is also used by MyOntologyTree to quickly finde
	 * nodes in the ontologyTree.
	 * 
	 * @param stringpath
	 *            the path to this node in a tree
	 */
	public void setI2B2Path(String i2b2Path) {
		this._i2b2Path = i2b2Path;
	}

	/**
	 * Returns the path to this node as a string.
	 * 
	 * @return the path
	 */
	public String getTreePath() {
		return this._treePath;
	}

	/**
	 * Sets the level attribute for this node.
	 * 
	 * @param i2b2Level
	 *            the level of this node in a tree
	 */
	public void setI2B2Level(int i2b2Level) {
		this._i2b2Level = i2b2Level;
	}

	/**
	 * Sets the level attribute for this node by converting a string to int
	 * first.
	 * 
	 * @param i2b2Level
	 *            the level of this node in a tree
	 */
	public void setI2B2Level(String i2b2LevelString) {
		try {
			setI2B2Level(Integer.valueOf(i2b2LevelString));
		} catch (NumberFormatException e) {
			Console.error(
					"Couldn't convert an i2b2-level-string to an an integer.",
					e);
			setI2B2Level(99);
		}
	}

	/**
	 * Returns the path to this node as a string.
	 * 
	 * @return the path
	 */
	public int getLevel() {
		return this._i2b2Level;
	}

	/**
	 * Sets the ConceptCode (key to connect items and user data) for I2B2.
	 * 
	 * @param conceptCode
	 *            the conceptCode
	 */
	public void setConceptCode(String conceptCode) {
		this._conceptCode = conceptCode;
	}

	/**
	 * Returns the ConceptCode.
	 * 
	 * @return the conceptCode
	 */
	public String getConceptCode() {
		return this._conceptCode;
	}

	/**
	 * Sets the visual attribute (key to connect items and user data) for I2B2.
	 * 
	 * @param visualattribute
	 *            the basecode
	 */
	public void setVisualattribute(String visualattribute) {
		this._visualattribute = visualattribute;
	}

	/**
	 * Returns visual attribute.
	 * 
	 * @return the visual attribute
	 */
	public String getVisualattribute() {
		return this._visualattribute;
	}

	/**
	 * Sets node type.
	 * 
	 * @param nodeType
	 *            the node type
	 */
	public void setNodeType(NodeType nodeType) {
		this._nodeType = nodeType;
	}

	/**
	 * Returns the node type.
	 * 
	 * @return the node type
	 */
	public NodeType getNodeType() {
		return this._nodeType;
	}

	/**
	 * Sets the ID of this node.
	 * 
	 * @param id
	 *            the ID
	 */
	public void setID(String id) {
		this._id = id;
	}

	/**
	 * Returns the ID of this node.
	 * 
	 * @return the ID
	 */
	public String getID() {
		return this._id;
	}

	/**
	 * Returns the name of the node (for use by the JTree render functions
	 * only).
	 * 
	 * @return the name
	 */
	public String toString() {
		return this._name;
	}

	public ListIterator getChild() {
		return this.children.listIterator();
	}

	public Iterator<OTTargetNode> getChildren() {
		if (this.children != null) {
			return (Iterator<OTTargetNode>) this.children.iterator();
		}
		return null;
	}

	/**
	 * @param _repeating
	 *            the _repeating to set
	 */
	public void setRepeating(boolean _repeating) {
		this._repeating = _repeating;
	}

	/**
	 * @return the _repeating
	 */
	public boolean isRepeating() {
		return _repeating;
	}

	/**
	 * @return the _repeating
	 */
	public String isRepeatingYesOrNo() {
		return (_repeating == true) ? "Yes" : "No";
	}

	/**
	 * @param _repeating
	 *            the _repeating to set
	 */
	public void setIsReferenceData(boolean _isReferenceData) {
		this._isReferenceData = _isReferenceData;
	}

	/**
	 * @return the _isReferenceData
	 */
	public boolean isReferenceData() {
		return _isReferenceData;
	}

	/**
	 * @return the _isReferenceData as a 'Yes' or 'No' string
	 */
	public String isReferenceDataYesOrNo() {
		return (_isReferenceData == true) ? "Yes" : "No";
	}

	/**
	 * @param _additionalData
	 *            the _additionalData to set
	 */
	public void addAdditionalData(String patientID, String _id,
			String _additionalData) {
		if (this._additionalData == null) {
			this._additionalData = new HashMap<String, HashMap<String, String>>();
		}
		HashMap<String, String> idToData = this._additionalData.get(patientID);
		if (idToData == null) {
			idToData = new HashMap<String, String>();
			this._additionalData.put(patientID, idToData);
		}
		idToData.put(_id, _additionalData);
	}

	/**
	 * @return the _additionalData
	 */
	public String getAdditionalData(String patientID, String _id) {

		if (_additionalData != null && _additionalData.get(patientID) != null
				&& _additionalData.get(patientID).get(_id) != null) {
			return _additionalData.get(patientID).get(_id);
		} else {
			return "noBarcodeID";
		}

	}

	public void deleteI2B2ValuesFromChildren() {

		if (this.getChildCount() > 0) {
			// Debug.d(">>>>childCount = " + this.getChildCount());
			for (int x = 0; x < this.getChildCount(); x++) {
				((OTTargetNode) this.getChildAt(x)).setI2B2Level(-1);
				((OTTargetNode) this.getChildAt(x)).setI2B2Path("");
				((OTTargetNode) this.getChildAt(x))
						.deleteI2B2ValuesFromChildren();
			}
		}

	}

	public void updateI2B2Values() {

		// delete all the childrens i2b2values
		// deleteI2B2ValuesFromChildren();

		Debug.d("updateStringPath for " + this.getName());

		String path = "";
		String additionalPath = "";
		String parentPath = "";
		int level = 0;
		int parentLevel = 0;

		Path<Integer, String> parentValues = this
				.getI2B2ValuesFromParents(this);
		parentPath = parentValues.path;
		parentLevel = parentValues.level;

		additionalPath = this.getID();

		if (additionalPath == null || additionalPath.isEmpty()) {
			additionalPath = this.getName();
		}

		path = parentPath + "\\" + additionalPath;
		level = parentLevel + 1;

		this.setI2B2Path(path);
		this.setI2B2Level(level);

		Debug.d(" .. stringPath isVisable with level:" + level + " stringPath:"
				+ path);

		if (this.getChildCount() > 0) {
			Debug.d(">>>>childCount = " + this.getChildCount());
			for (int x = 0; x < this.getChildCount(); x++) {
				((OTTargetNode) this.getChildAt(x)).updateI2B2Values();
			}
		}
	}

	public Path<Integer, String> getI2B2ValuesFromParents(OTTargetNode oNode) {

		OTTargetNode parentNode = (OTTargetNode) oNode.getParent();

		Debug.d("getStringPathFromParents:");

		if (parentNode.getLevel() != -1) {
			Debug.d(" ... isVisable");
			return new Path<Integer, String>(parentNode.getLevel(),
					parentNode.getI2B2Path());

		} else {

			// end the search at the study node
			if (parentNode.getNodeType().equals(NodeType.STUDY)) {
				Debug.d(" ... is NOT! Visable and study");
				return new Path<Integer, String>(1, parentNode.getID());
			}
			Debug.d(" ... is NOT! Visable");
			// parentNode.setI2B2Path("");
			// parentNode.setI2B2Level(-1);

			return getI2B2ValuesFromParents(parentNode);
		}

	}

	/*
	 * public Path getStringPathFromParents(OntologyTreeNode node){
	 * 
	 * String path = ""; int level = 0;
	 * 
	 * if (node.isVisable()){
	 * 
	 * path = node.getID() ; level = node.getLevel(); if(path == null ||
	 * path.isEmpty()){ // there is no id because this node is probalby an
	 * answer without an id path = node.getName(); }
	 * 
	 * return new Path(level, path); } else { return
	 * getStringPathFromParents((OntologyTreeNode)node.getParent()); }
	 * 
	 * }
	 */
	public Path getStringPathFromParents(int oldLevel, String oldPath) {

		String path = "";
		int level = 0;

		path = this.getID();
		level = this.getLevel();
		if (path == null || path.isEmpty()) {
			// there is no id because this node is probalby an answer without an
			// id
			path = this.getName();
		}
		Debug.d("---##");
		Debug.d("path1:" + path);

		if (!oldPath.isEmpty()) {
			path = path + "\\" + oldPath;
		}

		level = this.getLevel();

		if (oldLevel > 0) {
			level = oldLevel + level;
		}
		Debug.d("path2:" + path);

		TreeNode parentTreeNode = this.getParent();
		OTTargetNode parentNode = (OTTargetNode) this.getParent();
		if (parentNode != null
				&& parentTreeNode.getClass().getName() != "OntologyTreeODMNode") {
			// parentValues = parentNode.getStringPathFromParents(level, path);
			return parentNode.getStringPathFromParents(level, path);
		}

		Debug.d(2, "getPath for FatherNode\"" + this.getClass().getName()
				+ "\"  oldLevel:" + oldLevel + " & newLevel:" + level
				+ " oldPath:" + this.getTreePath() + " &newPath:" + path);

		return new Path(level, path);

	}

	public Path getStringPathFromParents() {

		HashMap<Integer, String> parentValues = new HashMap<Integer, String>();
		OTTargetNode parentNode = (OTTargetNode) this.getParent();
		if (parentNode != null
				&& parentNode.getClass().getName() != "OntologyTreeODMNode") {
			// parentValues.put(parentNode.getLevel(),
			// parentNode.getStringPath());
			// return parentValues;
			return new Path(parentNode.getLevel(), parentNode.getTreePath());
		}

		return null;
	}

	public void setStringPathForChildren(int fatherLevel,
			String fatherStringPath) {

		// HashMap<Integer, String> values = new HashMap<Integer,String>();

		String path = "";
		int level = 0;

		path = fatherStringPath + "\\"
				+ (!this.getID().isEmpty() ? this.getID() : this.getName());
		level = fatherLevel + 1;

		this.setTreePath(path);
		this.setI2B2Level(level);

		// this.setStringPath(fatherStringPath);
		// this.setLevel(fatherLevel);

		Debug.d(2, "ChangePath for Node\"" + this.getClass().getName()
				+ "\"  oldPath:" + this.getTreePath() + " &newPath:" + path);

		return;
	}

	public String escapeForSql(String string) {
		// Debug.d("* export:isString");
		if (string == null) {
			return "";
		} else {
			// replaces ' with ''
			// string = StringEscapeUtils.escapeSql( string );
			string = string.replaceAll("&", "'||chr(38)||'");
			string = string.replaceAll("Ä", "'||chr(196)||'");
			string = string.replaceAll("ä", "'||chr(228)||'");
			string = string.replaceAll("Ö", "'||chr(214)||'");
			string = string.replaceAll("ö", "'||chr(246)||'");
			string = string.replaceAll("Ü", "'||chr(220)||'");
			string = string.replaceAll("ü", "'||chr(252)||'");
			string = string.replaceAll("ß", "'||chr(252)||'");
			string = string.replaceAll("ÃŒ", "'||chr(252)||'");

			// string.replaceAll("ö", "'ö");
			// string.replaceAll("ü", "'ü");
			// string.replaceAll("ß", "'ß");
			return string;
		}
	}

	public OTTargetNode getChildByID(String id) {

		for (int x = 0; x < this.getChildCount(); x++) {
			OTTargetNode childNode = (OTTargetNode) this.getChildAt(x);
			if (childNode.getID().equals(id)) {
				return childNode;
			}
		}
		return null;
	}

	public String getItemNodeChildValueByID(String id) {
		OTTargetNode itemNode = (OTTargetNode) this.getChildByID(id);
		// TODO MyOntologyNode
		/*
		 * if (itemNode != null && itemNode.getAnswerAt(0) != null){ return
		 * itemNode.getAnswerAt(0).toString(); }
		 */
		return "";
	}

	/**
	 * 155: * Adds a new child node to this node and sets this node as the
	 * parent of 156: * the child node. The child node must not be an ancestor
	 * of this node. 157: * If the tree uses the {@link DefaultTreeModel}, you
	 * must subsequently 158: * call {@link DefaultTreeModel#reload(TreeNode)}.
	 * 159: * 160: * @param child the child node (<code>null</code> not
	 * permitted). 161: * 162: * @throws IllegalStateException if
	 * {@link #getAllowsChildren()} returns 163: * <code>false</code>. 164: * @throws
	 * IllegalArgumentException if {@link #isNodeAncestor} returns 165: *
	 * <code>true</code>. 166: * @throws IllegalArgumentException if
	 * <code>child</code> is 167: * <code>null</code>. 168:
	 */
	public void add(OTTargetNode child) {
		super.add(child);

	}

	public class Path<Integer, String> {

		public final Integer level;
		public final String path;

		public Path(Integer paramLevel, String paramPath) {

			this.level = paramLevel;
			this.path = paramPath;

		}
	}

	public void setAdditionalData(int additionalDataType) {
		_additionalDataType = additionalDataType;
	}

	public int getAdditionalDataType() {
		return _additionalDataType;
	}

	public boolean isAdditionalData() {
		return _additionalDataType == 0 ? false : true;
	}

	public boolean isAdditionDataChild() {
		return isAdditionalData() && !isAdditionDataParent();
	}

	/*
	 * public OntologyTreeNode getAdditionalDataParentNode (){ return
	 * this._additionalDataParentNode; }
	 * 
	 * public void setAdditionalDataParentNode (OntologyTreeNode
	 * additionalDataParentNode){
	 * 
	 * this._additionalDataParentNode = additionalDataParentNode; }
	 * 
	 * public boolean hasAdditionalDataParentNode(){ return
	 * !_additionalDataParentNodesourcePath.isEmpty() ? true : false; //return
	 * _additionalDataParentNode != null ? true : false; }
	 * 
	 * public String getAdditionalDataParentNodesourcePath (){ return
	 * _additionalDataParentNodesourcePath; }
	 * 
	 * public void setAdditionalDataParentNodesourcePath (String
	 * additionalDataParentNodesourcePath){ _additionalDataParentNodesourcePath
	 * = additionalDataParentNodesourcePath; }
	 * 
	 * public boolean hasAdditionalDataParentNodesourcePath(){ return
	 * !_additionalDataParentNodesourcePath.isEmpty() ? true : false; }
	 */

	public void setIsAdditionDataParent(boolean isAdditionDataParent) {
		_isAdditionDataParent = isAdditionDataParent;
	}

	public boolean isAdditionDataParent() {
		return _isAdditionDataParent;
	}

	public OTTargetNode getAdditionalDataParentNode() {

		if (this.isAdditionDataParent()) {
			return this;
		} else if (this.isAdditionalData() == false) {
			return null;
		} else {
			return ((OTTargetNode) getParent()).getAdditionalDataParentNode();
		}

	}

	public boolean hastAdditionalDataParentNode() {
		return (getAdditionalDataParentNode() != null) ? true : false;
	}

	public void convertVisualAttributesToModifier() {

		String visualAttribute = getVisualattribute();
		visualAttribute = visualAttribute.replaceAll("F", "D");
		visualAttribute = visualAttribute.replaceAll("L", "R");
		setVisualattribute(visualAttribute);
		return;

	}

	/**
	 * @return the isVisitNode
	 */
	public boolean isVisitNode() {
		return _isVisitNode;
	}

	/**
	 * @param isVisitNode
	 *            the isVisitNode to set
	 */
	public void setIsVisitNode(boolean isVisitNode) {
		this._isVisitNode = isVisitNode;
	}

	public OTTargetNode getVisitNode() {

		if (this.isVisitNode()) {
			return this;
			// return (OntologyTreeStudyEventNode) this;
		} else if (this.isRoot()) {
			return null;
		} else {
			return this.getParent().getVisitNode();
		}

	}

	public OTTargetNode getParent() {
		return (OTTargetNode) super.getParent();
	}

	public int getI2B2Level() {
		return this._i2b2Level;
	}

	public void setI2B2Ontology(int C_HLEVEL, String C_FULLNAME, String C_NAME,
			String C_SYNONYM_CD, String C_VISUALATTRIBUTES, int C_TOTALNUM,
			String C_BASECODE, Object C_METADATAXML, String C_FACTTABLECOLUMN,
			String C_TABLENAME, String C_COLUMNNAME, String C_COLUMNDATATYPE,
			String C_OPERATOR, String C_DIMCODE, String C_COMMENT,
			String C_TOOLTIP, String M_APPLIED_PATH, String UPDATE_DATE,
			String DOWNLOAD_DATE, String IMPORT_DATE, String SOURCESYSTEM_CD,
			String VALUETYPE_CD, String M_EXCLUSION_CD, String C_PATH,
			String C_SYMBOL) {

		// toDO add i2b2 ontology to default node
	}


	public String getDataType() {
		// TODO OTupdate needs implementation
		return "";
	}

	public boolean hasCodeList() {
		// TODO OTupdate needs implementation
		return false;
	}

	public int getAnswerCount() {
		// TODO OTupdate needs implementation
		return 0;
	}

	public Object getAnswerAt(int x) {
		// TODO OTupdate needs implementation
		return "";
	}

	public Object setDataSourceLow(Object x) {
		// TODO OTupdate needs implementation
		return "";
	}

	public Object setDataSourceHigh(Object x) {
		// TODO OTupdate needs implementation
		return "";
	}

	public Object getDataSourceLow() {
		// TODO OTupdate needs implementation
		return "";
	}

	public Object getDataSourceHigh() {
		// TODO OTupdate needs implementation
		return "";
	}

	public boolean isChecked() {
		// TODO OTupdate needs implementation
		return false;
	}

	public OTTargetNode addAnswerNode(String name) {
		// TODO OTupdate needs implementation
		return null;
	}

	public Object getItemValue() {
		// TODO OTupdate needs implementation
		return null;
	}

	public void setItemValue(Object itemValue) {
		// TODO OTupdate needs implementation
	}

	public boolean isAnswerGroup() {
		// TODO OTupdate needs implementation
		return false;
	}

	public void setIsAnswerGroup(boolean b) {
		// TODO OTupdate needs implementation
	}

	public void setChecked(boolean b, OTTargetNode treeRoot) {
		// TODO OTupdate needs implementation
	}

	public String getQuestion() {
		// TODO OTupdate needs implementation
		return null;
	}

	public int getTreeRow() {
		// TODO OTupdate needs implementation
		return 0;
	}

	public OTTargetNode getNextItemNode() {
		// TODO OTupdate needs implementation
		return null;
	}

	public OTTargetNode getNextUncheckedItemNode() {
		// TODO OTupdate needs implementation
		return null;
	}

	public OTTargetNode getPreviousItemNode() {
		// TODO OTupdate needs implementation
		return null;
	}

	public void setText(String trim) {
		// TODO Auto-generated method stub

	}

	public void setCodeListID(String codeListRef) {
		// TODO Auto-generated method stub
	}

	public OntologyCellAttributes getOntologyCellAttributes() {
		return _ontologyCellAttributes;
	}

	public void setOntologyCellAttributes(int C_HLEVEL, String C_FULLNAME,
			String C_NAME, String C_SYNONYM_CD, String C_VISUALATTRIBUTES,
			int C_TOTALNUM, String C_BASECODE, Object C_METADATAXML,
			String C_FACTTABLECOLUMN, String C_TABLENAME, String C_COLUMNNAME,
			String C_COLUMNDATATYPE, String C_OPERATOR, String C_DIMCODE,
			Object C_COMMENT, String C_TOOLTIP, String M_APPLIED_PATH,
			Date UPDATE_DATE, Date DOWNLOAD_DATE, Date IMPORT_DATE,
			String SOURCESYSTEM_CD, String VALUETYPE_CD, String M_EXCLUSION_CD,
			String C_PATH, String C_SYMBOL) {
		_ontologyCellAttributes.setC_HLEVEL(C_HLEVEL);
		_ontologyCellAttributes.setC_FULLNAME(C_FULLNAME);
		_ontologyCellAttributes.setC_NAME(C_NAME);
		_ontologyCellAttributes.setC_SYNONYM_CD(C_SYNONYM_CD);
		_ontologyCellAttributes.setC_VISUALATTRIBUTES(C_VISUALATTRIBUTES);
		_ontologyCellAttributes.setC_TOTALNUM(C_TOTALNUM);
		_ontologyCellAttributes.setC_BASECODE(C_BASECODE);
		_ontologyCellAttributes.setC_METADATAXML(C_METADATAXML);
		_ontologyCellAttributes.setC_FACTTABLECOLUMN(C_FACTTABLECOLUMN);
		_ontologyCellAttributes.setC_TABLENAME(C_TABLENAME);
		_ontologyCellAttributes.setC_COLUMNNAME(C_COLUMNNAME);
		_ontologyCellAttributes.setC_COLUMNDATATYPE(C_COLUMNDATATYPE);
		_ontologyCellAttributes.setC_OPERATOR(C_OPERATOR);
		_ontologyCellAttributes.setC_DIMCODE(C_DIMCODE);
		_ontologyCellAttributes.setC_COMMENT(C_COMMENT);
		_ontologyCellAttributes.setC_TOOLTIP(C_TOOLTIP);
		_ontologyCellAttributes.setM_APPLIED_PATH(M_APPLIED_PATH);
		_ontologyCellAttributes.setUPDATE_DATE(UPDATE_DATE);
		_ontologyCellAttributes.setDOWNLOAD_DATE(DOWNLOAD_DATE);
		_ontologyCellAttributes.setIMPORT_DATE(IMPORT_DATE);
		_ontologyCellAttributes.setSOURCESYSTEM_CD(SOURCESYSTEM_CD);
		_ontologyCellAttributes.setVALUETYPE_CD(VALUETYPE_CD);
		_ontologyCellAttributes.setM_EXCLUSION_CD(M_EXCLUSION_CD);
		_ontologyCellAttributes.setC_PATH(C_PATH);
		_ontologyCellAttributes.setC_SYMBOL(C_SYMBOL);
	}

	public boolean hasConceptDimension() {
		if (getNumberOfConceptDimensions() > 0)
			return true;
		else
			return false;
	}

	public boolean hasModifierDimension() {
		if (getNumberOfModifierDimensions() > 0)
			return true;
		else
			return false;
	}

	public boolean hasDimensions() {
		if (hasConceptDimension())
			return true;
		else if (hasModifierDimension())
			return true;
		else
			return false;
	}

	public ConceptDimensionAttributes addConceptDimension() {

		ConceptDimensionAttributes tmpConecptCell = new ConceptDimensionAttributes();
		_conceptDimensions.add(tmpConecptCell);
		return tmpConecptCell;
	}

	public int getNumberOfConceptDimensions() {
		return _conceptDimensions.size();
	}

	public Object getConceptDimensionAt(int x) {
		if (getNumberOfConceptDimensions() < x) {
			Console.error("Error while getting concept dimension attributes for a node: requestet list element is greater than list size.");
			return null;
		}
		return x;
	}

	public ModifierDimensionAttributes addModifierDimension() {

		ModifierDimensionAttributes tmpModifierCell = new ModifierDimensionAttributes();
		_modifierDimensions.add(tmpModifierCell);
		return tmpModifierCell;
	}

	public int getNumberOfModifierDimensions() {
		return _modifierDimensions.size();
	}

	public Object getModifierDimensionAt(int x) {
		if (getNumberOfModifierDimensions() < x) {
			Console.error("Error while getting modifier dimension attributes for a node: requestet list element is greater than list size.");
			return null;
		}
		return x;
	}
}
