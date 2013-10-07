package de.umg.mi.idrt.ioe.OntologyTree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Debug;
import de.umg.mi.idrt.ioe.Resource;

/**
 * This is the generic node class for the ontology tree. It holds some basic
 * informations like name, stringpath, nodetype and the visual attribute for
 * I2B2.
 * 
 * @author Christian Bauer
 * @version 0.9
 */
public class OntologyTreeNode extends DefaultMutableTreeNode {

	/**
	 * a list of this nodes children for the use in a swt tree viewer
	 */
	private List<OntologyTreeNode> _children = new ArrayList<OntologyTreeNode>();
	
	/**
	 * the name of the node
	 */
	private String _type = Resource.I2B2.NODE.TYPE.UNSPECIFIC;

	/**
	 * the name of the node
	 */
	private transient String _name = "";
	
	/**
	 * the path to this node in the jTree and treeviewer
	 */
	private transient String _treePath = "";

	/**
	 * the path to this node in a jTree
	 */
	private transient int _treePathLevel = 0;

	/**
	 * the node type of this node
	 */
	private transient NodeType _nodeType;

	/**
	 * the ID of this node
	 */
	private transient String _id = "";

	/**
	 * the visual attribute of this node for I2B2
	 */
	private transient String _visualattribute = "";

	/**
	 * the attribute of the visibility of this node
	 */
	private transient boolean _isVisible = true;

	/**
	 * specific attributes for different kind of nodes
	 */
	private OntologyCellAttributes _ontologyCellAttributes = new OntologyCellAttributes();
	private TargetNodeAttributes _targetNodeAttributes = new TargetNodeAttributes();

	/**
	 * Creates a generic ontology tree node.
	 * 
	 * @param name
	 *            the name of the new node
	 */
	public OntologyTreeNode(String name) {
		setName(name);
	}

	/**
	 * Creates a generic ontology tree node.
	 * 
	 */
	public OntologyTreeNode() {
		setName("No_Name");
	}

	
	@Override
	public boolean isRoot() {
		return super.isRoot();
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
	 * Returns the path to this node as a string.
	 * 
	 * @return the path
	 */
	public void setTreePath(String treePath) {
		_treePath = treePath;
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
	 * @param treePathLevel
	 *            the level of this node in a tree
	 */
	public void setTreePathLevel(int treePathLevel) {
		this._treePathLevel = treePathLevel;
	}

	/**
	 * Sets the level attribute for this node by converting a string to int
	 * first.
	 * 
	 * @param treePathLevel
	 *            the level of this node in a tree
	 */
	public void setTreePathLevel(String treePathLevel) {
		try {
			setTreePathLevel(Integer.valueOf(treePathLevel));
		} catch (NumberFormatException e) {
			Console.error(
					"Couldn't convert an i2b2-level-string to an an integer.",
					e);
			setTreePathLevel(99);
		}
	}

	/**
	 * Returns the path to this node as a string.
	 * 
	 * @return the path
	 */
	public int getTreePathLevel() {
		return _treePathLevel;
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
	 * Sets the visibility option for this node and possible children
	 * 
	 * @param isVisable
	 *            the value of the visibility
	 * @param setChilden
	 *            true if also all children should change the visibility value
	 */
	public void setIsVisable(boolean isVisable, boolean setChilden) {
		setIsVisable(isVisable);
		if (setChilden == true && this.getChildCount() > 0) {
			Debug.d("* setVisibility ("
					+ (isVisable == true ? "true" : "false") + ") for "
					+ this.getChildCount() + " children");
			for (int x = 0; x < this.getChildCount(); x++) {
				((OntologyTreeNode) this.getChildAt(x)).setIsVisable(isVisable,
						setChilden);
			}
		}
		// this.updateI2B2Values();
	}

	public void setIsVisable(String isVisableString, boolean setChilden) {
		try {
			setIsVisable(Boolean.valueOf(isVisableString), setChilden);
		} catch (Exception e) {
			Console.error("Couldn't convert an isVisable-string to a boolean.",
					e);
			setIsVisable(true, setChilden);
		}
	}

	public void setIsVisable(boolean isVisable) {
		this._isVisible = isVisable;
	}

	/**
	 * Returns the status of the visibility of this node.
	 * 
	 * @return the visibility
	 */
	public boolean isVisable() {
		return this._isVisible;
	}

	/**
	 * Returns the name of the node (for use by the JTree render functions
	 * only).
	 * 
	 * @return the name
	 */
	public String toString() {
		return this._name + " " + this._treePath;
	}

	/* ******************************
	 * /* functions for the swt tree /* *****************************
	 */
	public ListIterator getChild() {
		return this._children.listIterator();
	}

	public Iterator<OntologyTreeNode> getChildrenIterator() {
		if (this._children != null) {
			return (Iterator<OntologyTreeNode>) this._children.iterator();
		}
		return null;
	}

	public List<OntologyTreeNode> getChildren() {
		return this._children;
	}

	/*
	 * 
	 * public void updateI2B2Values(){
	 * 
	 * // delete all the childrens i2b2values //deleteI2B2ValuesFromChildren();
	 * 
	 * Debug.d("updateStringPath for " + this.getName());
	 * 
	 * if (this.isVisable()){
	 * 
	 * String path = ""; String additionalPath = ""; String parentPath = ""; int
	 * level = 0; int parentLevel = 0;
	 * 
	 * Path<Integer,String> parentValues = this.getI2B2ValuesFromParents(this);
	 * parentPath = parentValues.path; parentLevel = parentValues.level;
	 * 
	 * additionalPath = this.getID();
	 * 
	 * if (additionalPath == null || additionalPath.isEmpty()){ additionalPath =
	 * this.getName(); }
	 * 
	 * path = parentPath + "\\" + additionalPath; level = parentLevel + 1;
	 * 
	 * this.setI2B2Path(path); this.setI2B2Level(level);
	 * 
	 * Debug.d(" .. stringPath isVisable with level:" + level + " stringPath:" +
	 * path);
	 * 
	 * } else {
	 * 
	 * Debug.d(" .. stringPath is NOT!!! Visable"); this.setI2B2Path("");
	 * this.setI2B2Level(-1);
	 * 
	 * }
	 * 
	 * if (this.getChildCount() > 0){ Debug.d(">>>>childCount = " +
	 * this.getChildCount()); for (int x = 0; x < this.getChildCount(); x++){
	 * ((OTNode)this.getChildAt(x)).updateI2B2Values(); } } }
	 * 
	 * public Path<Integer,String> getI2B2ValuesFromParents(OTNode oNode){
	 * 
	 * OTNode parentNode = (OTNode)oNode.getParent();
	 * 
	 * Debug.d("getStringPathFromParents:");
	 * 
	 * if (parentNode.isVisable() && parentNode.getLevel() != -1 ){
	 * Debug.d(" ... isVisable"); return new Path<Integer,
	 * String>(parentNode.getLevel(), parentNode.getI2B2Path());
	 * 
	 * } else {
	 * 
	 * // end the search at the study node if
	 * (parentNode.getNodeType().equals(NodeType.STUDY) ){
	 * Debug.d(" ... is NOT! Visable and study"); return new Path<Integer,
	 * String>(1,parentNode.getID()); } Debug.d(" ... is NOT! Visable");
	 * //parentNode.setI2B2Path(""); //parentNode.setI2B2Level(-1);
	 * 
	 * return getI2B2ValuesFromParents(parentNode); }
	 * 
	 * }
	 */

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

	/*
	 * 
	 * public Path getStringPathFromParents(int oldLevel, String oldPath){
	 * 
	 * String path = ""; int level = 0; if (this.isVisable()){
	 * 
	 * path = this.getID() ; level = this.getLevel(); if(path == null ||
	 * path.isEmpty()){ // there is no id because this node is probalby an
	 * answer without an id path = this.getName(); } Debug.d("---##");
	 * Debug.d("path1:" + path);
	 * 
	 * 
	 * 
	 * if (!oldPath.isEmpty()){ path = path + "\\" + oldPath; }
	 * 
	 * level = this.getLevel();
	 * 
	 * if (oldLevel > 0){ level = oldLevel + level; } Debug.d("path2:" + path);
	 * 
	 * } else { path = oldPath; level = oldLevel; Debug.d("path1B:" + path);
	 * return getI2B2ValuesFromParents((OTNode)this.getParent()); }
	 * 
	 * 
	 * TreeNode parentTreeNode = this.getParent(); OTNode parentNode = (OTNode)
	 * this.getParent(); if (parentNode != null &&
	 * parentTreeNode.getClass().getName() != "OntologyTreeODMNode"){
	 * //parentValues = parentNode.getStringPathFromParents(level, path); return
	 * parentNode.getStringPathFromParents(level, path); }
	 * 
	 * Debug.d(2,
	 * "getPath for FatherNode\""+this.getClass().getName()+"\"  oldLevel:"
	 * +oldLevel
	 * +" & newLevel:"+level+" oldPath:"+this.getTreePath()+" &newPath:"+path);
	 * if (this.isVisable()) return new Path(level, path); else return new
	 * Path(-1,""); }
	 */

	/*
	 * public Path getStringPathFromParents(){
	 * 
	 * HashMap<Integer, String> parentValues = new HashMap<Integer,String>();
	 * OTNode parentNode = (OTNode) this.getParent(); if (parentNode != null &&
	 * parentNode.getClass().getName() != "OntologyTreeODMNode"){
	 * //parentValues.put(parentNode.getLevel(), parentNode.getStringPath());
	 * //return parentValues; return new Path(parentNode.getLevel(),
	 * parentNode.getTreePath()); }
	 * 
	 * return null; }
	 */

	/*
	 * 
	 * public void setStringPathForChildren(int fatherLevel, String
	 * fatherStringPath){
	 * 
	 * //HashMap<Integer, String> values = new HashMap<Integer,String>();
	 * 
	 * String path = ""; int level = 0;
	 * 
	 * if (this.isVisable()){ path = fatherStringPath + "\\" +
	 * (!this.getID().isEmpty() ? this.getID() : this.getName() ); level =
	 * fatherLevel + 1; } else { path = fatherStringPath; level = fatherLevel; }
	 * 
	 * this.setTreePath(path); this.setI2B2Level(level);
	 * 
	 * //this.setStringPath(fatherStringPath); //this.setLevel(fatherLevel);
	 * 
	 * Debug.d(2,
	 * "ChangePath for Node\""+this.getClass().getName()+"\"  oldPath:"
	 * +this.getTreePath()+" &newPath:"+path);
	 * 
	 * 
	 * 
	 * return; }
	 * 
	 * public String escapeForSql(String string){
	 * //Debug.d("* export:isString"); if ( string == null ){ return ""; } else
	 * { // replaces ' with '' //string = StringEscapeUtils.escapeSql( string );
	 * string = string.replaceAll("&", "'||chr(38)||'"); string =
	 * string.replaceAll("Ä", "'||chr(196)||'"); string = string.replaceAll("ä",
	 * "'||chr(228)||'"); string = string.replaceAll("Ö", "'||chr(214)||'");
	 * string = string.replaceAll("ö", "'||chr(246)||'"); string =
	 * string.replaceAll("Ü", "'||chr(220)||'"); string = string.replaceAll("ü",
	 * "'||chr(252)||'"); string = string.replaceAll("ß", "'||chr(252)||'");
	 * string = string.replaceAll("ÃŒ", "'||chr(252)||'");
	 * 
	 * //string.replaceAll("ö", "'ö"); //string.replaceAll("ü", "'ü");
	 * //string.replaceAll("ß", "'ß"); return string; } }
	 * 
	 * public OTNode getChildByID(String id){
	 * 
	 * for(int x = 0; x < this.getChildCount(); x++){ OTNode childNode =
	 * (OTNode) this.getChildAt(x); if (childNode.getID().equals(id)){ return
	 * childNode; } } return null; }
	 * 
	 * public String getItemNodeChildValueByID(String id){ OTNode itemNode =
	 * (OTNode)this.getChildByID(id); //TODO MyOntologyNode
	 * 
	 * if (itemNode != null && itemNode.getAnswerAt(0) != null){ return
	 * itemNode.getAnswerAt(0).toString(); }
	 * 
	 * return ""; }
	 */

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
	public void add(OntologyTreeNode child) {
		super.add(child);
		/* adding stuff the child now */
		
		child.setTreePath( this.getTreePath() + child.getID() + "\\"  );
		child.setTreePathLevel( this.getTreePathLevel() + 1 );
		
		
	}

	public class Path<Integer, String> {

		public final Integer level;
		public final String path;

		public Path(Integer paramLevel, String paramPath) {

			this.level = paramLevel;
			this.path = paramPath;

		}
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
	 * !_additionalDataParentNodeImportPath.isEmpty() ? true : false; //return
	 * _additionalDataParentNode != null ? true : false; }
	 * 
	 * public String getAdditionalDataParentNodeImportPath (){ return
	 * _additionalDataParentNodeImportPath; }
	 * 
	 * public void setAdditionalDataParentNodeImportPath (String
	 * additionalDataParentNodeImportPath){ _additionalDataParentNodeImportPath
	 * = additionalDataParentNodeImportPath; }
	 * 
	 * public boolean hasAdditionalDataParentNodeImportPath(){ return
	 * !_additionalDataParentNodeImportPath.isEmpty() ? true : false; }
	 */

	public void convertVisualAttributesToModifier() {

		String visualAttribute = getVisualattribute();
		visualAttribute = visualAttribute.replaceAll("F", "D");
		visualAttribute = visualAttribute.replaceAll("L", "R");
		setVisualattribute(visualAttribute);
		return;

	}

	public OntologyTreeNode getParent() {
		return (OntologyTreeNode) super.getParent();
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
		this.setType(Resource.I2B2.NODE.TYPE.ONTOLOGY_SOURCE);
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

	/*
	 * public boolean hasConceptDimension() { if (getNumberOfConceptDimensions()
	 * > 0) return true; else return false; }
	 * 
	 * public boolean hasModifierDimension() { if
	 * (getNumberOfModifierDimensions() > 0) return true; else return false; }
	 * 
	 * public boolean hasDimensions() { if (hasConceptDimension()) return true;
	 * else if (hasModifierDimension()) return true; else return false; }
	 * 
	 * /* public ConceptDimensionAttributes addConceptDimension(){
	 * 
	 * ConceptDimensionAttributes tmpConecptCell = new
	 * ConceptDimensionAttributes(); _conceptDimensions.add(tmpConecptCell);
	 * return tmpConecptCell; }
	 * 
	 * public int getNumberOfConceptDimensions(){ return
	 * _conceptDimensions.size(); }
	 * 
	 * public Object getConceptDimensionAt(int x){ if (
	 * getNumberOfConceptDimensions() < x ){ Console.error(
	 * "Error while getting concept dimension attributes for a node: requestet list element is greater than list size."
	 * ); return null; } return x; }
	 * 
	 * public ModifierDimensionAttributes addModifierDimension(){
	 * 
	 * ModifierDimensionAttributes tmpModifierCell = new
	 * ModifierDimensionAttributes(); _modifierDimensions.add(tmpModifierCell);
	 * return tmpModifierCell; }
	 * 
	 * public int getNumberOfModifierDimensions(){ return
	 * _modifierDimensions.size(); }
	 * 
	 * public Object getModifierDimensionAt(int x){ if (
	 * getNumberOfModifierDimensions() < x ){ Console.error(
	 * "Error while getting modifier dimension attributes for a node: requestet list element is greater than list size."
	 * ); return null; } return x; }
	 */

	public Object[] getChildrenArray() {

		List<OntologyTreeNode> children = new ArrayList<OntologyTreeNode>();

		// System.out.println("getChildrenArray for \"" +
		// this.getOntologyCellAttributes().getC_FULLNAME() + "\" ChildCount \""
		// + getChildCount() + "\"");

		if (this.getChildCount() > 0) {

			// System.out.println(" .. do children");

			Iterator<OntologyTreeNode> it = this.getChildrenIterator();

			int i = 0;

			for (int x = 0; x < this.getChildCount(); x++) {
				// System.out.println(" ... while " + i++);
				OntologyTreeNode tmpChild = (OntologyTreeNode) this.getChildAt(x);
				children.add(tmpChild);
				tmpChild.getChildrenArray();
			}

			return children.toArray();
		} else
			return null;

	}

	public boolean hasChildren() {
		if (this.getChildCount() > 0)
			return true;
		else
			return false;

	}

	public TargetNodeAttributes getTargetNodeAttributes() {
		return this._targetNodeAttributes;
	}

	public void setType(String type) {
		this._type = type;
	}

	public String getType() {
		return this._type;
	}

	public void setTreeAttributes() {

		if (this.parent == null) {
			Console.error("Could not set tree attributes for node \""
					+ this.getName()
					+ "\", because he has no parents (ErrorCode: BruceWayne01).");

		} else {

			this.setTreePath(((OntologyTreeNode) parent).getTreePath() + "\\" + this._id);
			this.setTreePathLevel(((OntologyTreeNode) parent).getTreePathLevel() + 1);

		}

	}

	public String getIDFromPath(String path) {

		if (path.length() > 2) {
			// System.out.print("path1: " + path);
			path = path.substring(0, path.length() - 1);
			// System.out.print(" | path 2: " + path );
			if (path.lastIndexOf("\\") + 1 < path.length()) {
				return path
						.substring(path.lastIndexOf("\\") + 1, path.length());
			} else {
				Console.error("Could not get ID from Path, because shortend path is too damn short already.");
				return path;
			}
			
		} else {
			Console.error("Could not get ID from Path, because path is too damn short.");
			return "";
		}
	}
}
