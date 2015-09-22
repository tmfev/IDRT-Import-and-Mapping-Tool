package de.umg.mi.idrt.imt.transmart;

import java.util.LinkedList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;


public class TransmartConfigTreeItem extends DefaultMutableTreeNode{
	
	private String name;
	private List<TransmartConfigTreeItem> children;
	private int hlevel;
	private TransmartConfigTreeItem parent;
	private String variableType;
	private String validationRules;
	private String dataLabel;
	private String fileName;
	private String visualAttribute;
	
	public TransmartConfigTreeItem() {
		children = new LinkedList<TransmartConfigTreeItem>();
	}
	
	@Override
	public int getChildCount() {
		return getChildren().size();
	}
	
	public boolean addNode(TransmartConfigTreeItem newChild){
		newChild.setParent(this);
		return getChildren().add(newChild);
	}
	public boolean removeNode(TransmartConfigTreeItem oldChild){
		return getChildren().remove(oldChild);
	}
	public TransmartConfigTreeItem(String name){
		this.name=name;
		children = new LinkedList<TransmartConfigTreeItem>();
		visualAttribute="f";
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getHlevel() {
		return hlevel;
	}
	public void setHlevel(int hlevel) {
		this.hlevel = hlevel;
	}
	public List<TransmartConfigTreeItem> getChildren() {
		return children;
	}
	public void setChildren(List<TransmartConfigTreeItem> children) {
		this.children = children;
	}
	public TransmartConfigTreeItem getParent() {
		return parent;
	}
	public void setParent(TransmartConfigTreeItem parent) {
		this.parent = parent;
	}
	public String getVariableType() {
		return variableType;
	}
	public void setVariableType(String variableType) {
		this.variableType = variableType;
	}
	public String getValidationRules() {
		return validationRules;
	}
	public void setValidationRules(String validationRules) {
		this.validationRules = validationRules;
	}
	public String getDataLabel() {
		return dataLabel;
	}
	public void setDataLabel(String dataLabel) {
		this.dataLabel = dataLabel;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Override
	public String toString() {
		return hlevel + " " + name;
	}

	public String getVisualAttribute() {
		return visualAttribute;
	}

	public void setVisualAttribute(String visualAttribute) {
		this.visualAttribute = visualAttribute;
	}

}
