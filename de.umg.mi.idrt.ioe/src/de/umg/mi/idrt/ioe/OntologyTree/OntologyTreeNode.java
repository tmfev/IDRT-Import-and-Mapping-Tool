package de.umg.mi.idrt.ioe.OntologyTree;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;


/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de>
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 * 
 *         This is the generic node class for the ontology tree. It holds some
 *         basic informations like name, stringpath, nodetype and the visual
 *         attribute for I2B2.
 */
public class OntologyTreeNode extends DefaultMutableTreeNode implements Comparable {

	/**
	 * 
	 */
	
	private String stagingModifierPath;
	
	private boolean isCustomNode = false;
	private static final long serialVersionUID = -874444767245977081L;
	private boolean searchResult = false;
	private boolean highlighted = false;
	private boolean merged = false;
	private boolean isModifier = false;
	/**
	 * a list of this nodes children for the use in a swt tree viewer
	 */
	private List<OntologyTreeNode> children;

	/**
	 * the name of the node
	 */
	private String type = Resource.I2B2.NODE.TYPE.UNSPECIFIC;

	/**
	 * the name of the node
	 */
	private String name;

	/**
	 * the path to this node in the jTree and treeviewer
	 */
	private String treePath;

	/**
	 * the path to this node in a jTree
	 */
	private int treePathLevel = 0;

	/**
	 * the node type of this node
	 */
	private NodeType nodeType;

	/**
	 * the ID of this node
	 */
	private String id;

	/**
	 * the attribute of the visibility of this node
	 */
	private boolean isVisible = true;

	/**
	 * specific attributes for different kind of nodes
	 */
	private OntologyCellAttributes ontologyCellAttributes;

	private TargetNodeAttributes targetNodeAttributes;

	/**
	 * Copy Constructor
	 * 
	 * @param node
	 */
	public OntologyTreeNode(OntologyTreeNode node) {
		this.children = new ArrayList<OntologyTreeNode>();
		this.ontologyCellAttributes = node.getOntologyCellAttributes();
		this.setName(node.getName());
		this.setID(node.getID());
		//		counter++;
		//		if(counter%2000==0||counter<500)
		//			System.out.println("Creating OTNODE: "+ counter+ " " + this.getName()+ " " );
	}
	/**
	 * Creates a generic ontology tree node.
	 * 
	 * @param name
	 *            the name of the new node
	 * @param isCustomNode 
	 */
	public OntologyTreeNode(String name, boolean isCustomNode) {
		this.children = new ArrayList<OntologyTreeNode>();
		this.isCustomNode = isCustomNode;
		this.ontologyCellAttributes = new OntologyCellAttributes();
		this.setName(name);
		//		counter++;
		//		if(counter%2000==0||counter<500)
		//			System.out.println("Creating OTNODE: "+ counter+ " " + this.getName()+ " " );
	}

	//	private static int counter=0;

	/**
	 * Creates a generic ontology tree node.
	 * 
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
		//		System.out.println("ADD " + this.getName());
		this.getChildren().add(child);
		//		if (!this.isModifier()){
		//		child.setTreePath(this.getTreePath() + child.getID() + "\\");
		//		child.setTreePathLevel(this.getTreePathLevel() + 1);
		//		}
		super.add(child);
		/* adding stuff the child now */

	}

	public int getAllChildrenCount() {
		int sum = this.getChildren().size();
		for (OntologyTreeNode child : getChildren()) {
			sum+=child.getAllChildrenCount();
		}
		return sum;
	}

	/* ******************************
	 * /* functions for the swt tree /* *****************************
	 */
	public ListIterator<OntologyTreeNode> getChild() {
		return this.children.listIterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.tree.DefaultMutableTreeNode#getChildAfter(javax.swing.tree
	 * .TreeNode)
	 */
	@Override
	public OntologyTreeNode getChildAfter(TreeNode aChild) {
		return (OntologyTreeNode) super.getChildAfter(aChild);
	}

	public List<OntologyTreeNode> getChildren() {
		return this.children;
	}

	/**
	 * Returns the ID of this node.
	 * 
	 * @return the ID
	 */
	public String getID() {
		return this.id;
	}

	public String getIDFromPath(String path) {

		if (path.length() > 2) {
			path = path.substring(0, path.length() - 1);
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

	/**
	 * Returns the name of this node.
	 * 
	 * @return the name of the node
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the node type.
	 * 
	 * @return the node type
	 */
	public NodeType getNodeType() {
		return this.nodeType;
	}

	public OntologyCellAttributes getOntologyCellAttributes() {
		return ontologyCellAttributes;
	}

	public OntologyTreeNode getParent() {
		return (OntologyTreeNode) super.getParent();
	}

	public TargetNodeAttributes getTargetNodeAttributes() {
		if (this.targetNodeAttributes == null)
			this.targetNodeAttributes = new TargetNodeAttributes(this);
		return this.targetNodeAttributes;
	}

	/**
	 * Returns the path to this node as a string.
	 * 
	 * @return the path
	 */
	public String getTreePath() {
		return this.treePath;
	}

	/**
	 * Returns the path to this node as a string.
	 * 
	 * @return the path
	 */
	public int getTreePathLevel() {
		return treePathLevel;
	}

	public String getType() {
		return this.type;
	}

	public boolean hasChildren() {
		return this.getChildren().size() > 0;
	}

	public boolean isHighlighted() {
		return highlighted;
	}

	@Override
	public boolean isLeaf() {
		return this.getTargetNodeAttributes().getVisualattribute()
				.toLowerCase().startsWith("l")||this.getTargetNodeAttributes().getVisualattribute()
				.toLowerCase().startsWith("r");
	}

	public boolean isMerged() {
		return merged;
	}

	public boolean isModifier() {

		isModifier = this.getTargetNodeAttributes().getVisualattribute().toLowerCase().startsWith("r") 
				|| this.getTargetNodeAttributes().getVisualattribute().toLowerCase().startsWith("d");
		//|| 	(this.getOntologyCellAttributes().getC_VISUALATTRIBUTES()!=null)?this.getOntologyCellAttributes().getC_VISUALATTRIBUTES().toLowerCase().startsWith("d"):false 
		//|| (this.getOntologyCellAttributes().getC_VISUALATTRIBUTES()!=null)?this.getOntologyCellAttributes().getC_VISUALATTRIBUTES().toLowerCase().startsWith("r"):false;
		boolean isModifier2 = false;
		if (this.getOntologyCellAttributes().getC_VISUALATTRIBUTES()!=null){
			isModifier2 = this.getOntologyCellAttributes().getC_VISUALATTRIBUTES().toLowerCase().startsWith("d")
					|| this.getOntologyCellAttributes().getC_VISUALATTRIBUTES().toLowerCase().startsWith("r");
		}



		isModifier = isModifier||isModifier2;
		return isModifier;

	}

	public boolean isSearchResult() {
		return searchResult;
	}

	/**
	 * Returns the status of the visibility of this node.
	 * @return the visibility
	 */
	public boolean isVisable() {
		return this.isVisible;
	}

	public void remove(OntologyTreeNode aChild) {
		getChildren().remove(aChild);
		super.remove(aChild);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.DefaultMutableTreeNode#removeAllChildren()
	 */
	@Override
	public void removeAllChildren() {
		//		System.out.println("REMOVEALL\tCHILDREN "+this.getName());
		for (OntologyTreeNode node : getChildren()) {
			MyOntologyTrees myOT = OntologyEditorView.getMyOntologyTree();
			myOT.getOntologyTreeTarget().getNodeLists().removeNode(node);
			node.removeAllChildren();
			node=null;

		}
		this.children=new ArrayList<OntologyTreeNode>();
		super.removeAllChildren();
	}

	public void removeAllChildren(OntologyTreeNodeList nodeList) {
		for (OntologyTreeNode node : getChildren()) {
			nodeList.removeNode(node);
			node.removeAllChildren();
			node=null;

		}
		this.children=new ArrayList<OntologyTreeNode>();
		super.removeAllChildren();
	}

	@Override
	public void removeFromParent() {
		MyOntologyTrees myOT = OntologyEditorView.getMyOntologyTree();
		myOT.getOntologyTreeTarget().getNodeLists().removeNode(this);
		this.removeAllChildren();
		if (this != OntologyEditorView.getOntologyTargetTree()
				.getI2B2RootNode())
			this.getParent().getChildren().remove(this);
		super.removeFromParent();
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	/**
	 * Sets the ID of this node.
	 * 
	 * @param id
	 *            the ID
	 */
	public void setID(String id) {
		this.id = id;
	}

	public void setIsVisable(boolean isVisable) {
		this.isVisible = isVisable;
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
			for (int x = 0; x < this.getChildCount(); x++) {
				((OntologyTreeNode) this.getChildAt(x)).setIsVisable(isVisable,
						setChilden);
			}
		}
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

	/**
	 * @param merged the merged to set
	 */
	public void setMerged(boolean merged) {
		this.merged = merged;
	}


	public void setModifier(boolean isModifier) {
		//		System.out.println("setting " + isModifier + " for " + getName());
		this.isModifier = isModifier;
	}

	/**
	 * Sets the name attribute for this node.
	 * 
	 * @param name
	 *            the name of the node
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets node type.
	 * 
	 * @param nodeType
	 *            the node type
	 */
	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	public void setOntologyCellAttributes(int C_HLEVEL, String C_FULLNAME,
			String C_NAME, String C_SYNONYM_CD, String C_VISUALATTRIBUTES,
			int C_TOTALNUM, String C_BASECODE, String C_METADATAXML,
			String C_FACTTABLECOLUMN, String C_TABLENAME, String C_COLUMNNAME,
			String C_COLUMNDATATYPE, String C_OPERATOR, String C_DIMCODE,
			Object C_COMMENT, String C_TOOLTIP, String M_APPLIED_PATH,
			Date UPDATE_DATE, Date DOWNLOAD_DATE, Date IMPORT_DATE,
			String SOURCESYSTEM_CD, String VALUETYPE_CD, String M_EXCLUSION_CD,
			String C_PATH, String C_SYMBOL, String sec_obj) {
		this.setType(Resource.I2B2.NODE.TYPE.ONTOLOGY_SOURCE);
		ontologyCellAttributes.setC_HLEVEL(C_HLEVEL);
		ontologyCellAttributes.setC_FULLNAME(C_FULLNAME);
		ontologyCellAttributes.setC_NAME(C_NAME);
		ontologyCellAttributes.setC_SYNONYM_CD(C_SYNONYM_CD);
		ontologyCellAttributes.setC_VISUALATTRIBUTES(C_VISUALATTRIBUTES);
		ontologyCellAttributes.setC_TOTALNUM(C_TOTALNUM);
		ontologyCellAttributes.setC_BASECODE(C_BASECODE);
		ontologyCellAttributes.setC_METADATAXML(C_METADATAXML);
		ontologyCellAttributes.setC_FACTTABLECOLUMN(C_FACTTABLECOLUMN);
		ontologyCellAttributes.setC_TABLENAME(C_TABLENAME);
		ontologyCellAttributes.setC_COLUMNNAME(C_COLUMNNAME);
		ontologyCellAttributes.setC_COLUMNDATATYPE(C_COLUMNDATATYPE);
		ontologyCellAttributes.setC_OPERATOR(C_OPERATOR);
		ontologyCellAttributes.setC_DIMCODE(C_DIMCODE);
		ontologyCellAttributes.setC_COMMENT(C_COMMENT);
		ontologyCellAttributes.setC_TOOLTIP(C_TOOLTIP);
		ontologyCellAttributes.setM_APPLIED_PATH(M_APPLIED_PATH);
		ontologyCellAttributes.setUPDATE_DATE(UPDATE_DATE);
		ontologyCellAttributes.setDOWNLOAD_DATE(DOWNLOAD_DATE);
		ontologyCellAttributes.setIMPORT_DATE(IMPORT_DATE);
		ontologyCellAttributes.setSOURCESYSTEM_CD(SOURCESYSTEM_CD);
		ontologyCellAttributes.setVALUETYPE_CD(VALUETYPE_CD);
		ontologyCellAttributes.setM_EXCLUSION_CD(M_EXCLUSION_CD);
		ontologyCellAttributes.setC_PATH(C_PATH);
		ontologyCellAttributes.setC_SYMBOL(C_SYMBOL);
		ontologyCellAttributes.setSEC_OBJ(sec_obj);
	}

	public void setOntologyCellAttributes(OntologyItem item) {
		this.setType(Resource.I2B2.NODE.TYPE.ONTOLOGY_SOURCE);
		ontologyCellAttributes.setC_HLEVEL(item.getC_HLEVEL());
		ontologyCellAttributes.setC_FULLNAME(item.getC_FULLNAME());
		ontologyCellAttributes.setC_NAME(item.getC_NAME());
		ontologyCellAttributes.setC_SYNONYM_CD(item.getC_SYNONYM_CD());
		ontologyCellAttributes.setC_VISUALATTRIBUTES(item
				.getC_VISUALATTRIBUTES());
		ontologyCellAttributes.setC_TOTALNUM(item.getC_TOTALNUM());
		ontologyCellAttributes.setC_BASECODE(item.getC_BASECODE());
		ontologyCellAttributes.setC_METADATAXML(item.getC_METADATAXML());
		ontologyCellAttributes
		.setC_FACTTABLECOLUMN(item.getC_FACTTABLECOLUMN());
		ontologyCellAttributes.setC_TABLENAME(item.getC_TABLENAME());
		ontologyCellAttributes.setC_COLUMNNAME(item.getC_COLUMNNAME());
		ontologyCellAttributes.setC_COLUMNDATATYPE(item.getC_COLUMNDATATYPE());
		ontologyCellAttributes.setC_OPERATOR(item.getC_OPERATOR());
		ontologyCellAttributes.setC_DIMCODE(item.getC_DIMCODE());
		ontologyCellAttributes.setC_COMMENT(item.getC_COMMENT());
		ontologyCellAttributes.setC_TOOLTIP(item.getC_TOOLTIP());
		ontologyCellAttributes.setM_APPLIED_PATH(item.getM_APPLIED_PATH());
		ontologyCellAttributes.setUPDATE_DATE(item.getUPDATE_DATE());
		ontologyCellAttributes.setDOWNLOAD_DATE(item.getDOWNLOAD_DATE());
		ontologyCellAttributes.setIMPORT_DATE(item.getIMPORT_DATE());
		ontologyCellAttributes.setSOURCESYSTEM_CD(item.getSOURCESYSTEM_CD());
		ontologyCellAttributes.setVALUETYPE_CD(item.getVALUETYPE_CD());
		ontologyCellAttributes.setM_EXCLUSION_CD(item.getM_EXCLUSION_CD());
		ontologyCellAttributes.setC_PATH(item.getC_PATH());
		ontologyCellAttributes.setC_SYMBOL(item.getC_SYMBOL());
		ontologyCellAttributes.setSEC_OBJ(item.getSEC_OBJ());
	}

	public void setSearchResult(boolean searchResult) {
		this.searchResult = searchResult;
	}

	public void setTreeAttributes() {
		if (this.parent == null) {
			Console.error("Could not set tree attributes for node \""
					+ this.getName()
					+ "\", because he has no parents (ErrorCode: BruceWayne01).");

			//		} else if (getTreePath() == null) {
		} else {
			if (!this.isModifier()){
//				System.out.println("ADDING I2b2?");
				this.setTreePath(((OntologyTreeNode) parent).getTreePath()
						+ this.id + "\\");
				this.setTreePathLevel(((OntologyTreeNode) parent)
						.getTreePathLevel() + 1);
			}
		}
	}

	/**
	 * Returns the path to this node as a string.
	 * 
	 * @return the path
	 */
	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	/**
	 * Sets the level attribute for this node.
	 * 
	 * @param treePathLevel
	 *            the level of this node in a tree
	 */
	public void setTreePathLevel(int treePathLevel) {
		//		System.out.println("SETTING TREEPATHLEVEL: " + treePathLevel + " FOR " + this.getName());
		this.treePathLevel = treePathLevel;
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

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Returns the name of the node (for use by the JTree render functions
	 * only).
	 * 
	 * @return the name
	 */
	public String toString() {
		return this.name + " " + this.treePath;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {

		//		counter--;
		//		if(counter%500==0)
		//		System.err.println("FINALIZE OT: " + counter + " "+ this.getName() );
		//		System.err.println("FINALIZE OT: " + this.getName());
		// TODO Auto-generated method stub
		super.finalize();
	}
	@Override
	public int compareTo(Object o) {
		return ((OntologyTreeNode) o).getName().compareTo(this.getName());
		//		return 0;
	}
	public boolean isCustomNode() {
		return isCustomNode;
	}
	public void setCustomNode(boolean isCustomNode) {
		this.isCustomNode = isCustomNode;
	}
	public String getStagingModifierPath() {
		return stagingModifierPath;
	}
	public void setStagingModifierPath(String stagingModifierPath) {
		this.stagingModifierPath = stagingModifierPath;
	}

}
