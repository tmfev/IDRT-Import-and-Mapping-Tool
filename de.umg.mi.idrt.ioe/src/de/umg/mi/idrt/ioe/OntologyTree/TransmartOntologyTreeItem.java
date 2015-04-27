package de.umg.mi.idrt.ioe.OntologyTree;

import java.util.LinkedList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import de.umg.mi.idrt.ioe.Resource;


public class TransmartOntologyTreeItem extends DefaultMutableTreeNode{

	private String name;
	private List<TransmartOntologyTreeItem> children;
	private int hlevel;
	private TransmartOntologyTreeItem parent;
	private String path;
	private OntologyCellAttributes ontologyCellAttributes;
	private boolean highlighted = false;
	private boolean searchResult = false;
	private boolean isModifier;
	@Override
	public String toString() {
		return(this.name + "--> "+this.getOntologyCellAttributes().getC_FULLNAME());
	}

	public TransmartOntologyTreeItem() {
		new TransmartOntologyTreeItem("default",0,"default",null);
	}
	public TransmartOntologyTreeItem(String name, int hlevel, String path){
		this.name = name;
		this.hlevel = hlevel;
		this.path=path;
		children = new LinkedList<TransmartOntologyTreeItem>();
		ontologyCellAttributes = new OntologyCellAttributes();
//		setOntologyCellAttributes(item);
	}

	public TransmartOntologyTreeItem(String name, int hlevel, String path, OntologyItem item){
		this.name = name;
		this.hlevel = hlevel;
		this.path=path;
		children = new LinkedList<TransmartOntologyTreeItem>();
		ontologyCellAttributes = new OntologyCellAttributes();
		setOntologyCellAttributes(item);
	}

	public void setOntologyCellAttributes(OntologyItem item) {
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
	
	public boolean addNode(TransmartOntologyTreeItem item){
//		System.out.println("ADDING: " + item + " TO " +this + " LEVEL:" + item.getHlevel());
		item.setParent(this);
		return this.getChildren().add(item);
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public List<TransmartOntologyTreeItem> getChildren() {
		return children;
	}


	public void setChildren(List<TransmartOntologyTreeItem> children) {
		this.children = children;
	}


	public int getHlevel() {
		return hlevel;
	}


	public void setHlevel(int hlevel) {
		this.hlevel = hlevel;
	}

	public String getNodePath() {
		return path;
	}

	public void setNodePath(String path) {
		if (!path.startsWith("\\"))
			path = "\\"+path;
		if (!path.endsWith("\\"))
			path = path + "\\";
		this.path = path;
	}

	@Override
	public TransmartOntologyTreeItem getParent() {
		return parent;
	}

	public void setParent(TransmartOntologyTreeItem parent) {
		this.parent = parent;
	}

	public OntologyCellAttributes getOntologyCellAttributes() {
		return ontologyCellAttributes;
	}

	public void setOntologyCellAttributes(OntologyCellAttributes ontologyCellAttributes) {
		this.ontologyCellAttributes = ontologyCellAttributes;
	}

	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	public boolean isSearchResult() {
		return searchResult;
	}

	@Override
	public int getChildCount() {
		return getChildren().size();
	}
	@Override
	public boolean isLeaf() {
		return getChildren().isEmpty();
	}

	public boolean isModifier() {
		return !getOntologyCellAttributes().getM_APPLIED_PATH().equals("@");
	}


}
