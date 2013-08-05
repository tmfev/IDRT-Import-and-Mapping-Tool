package de.umg.mi.idrt.ioe.view;

import java.util.Map;
import java.util.TreeMap;

import de.umg.mi.idrt.ioe.OntologyTree.ViewTreeNode;


public class ViewTree {

	public Map<String, ViewTreeNode> stringPathToViewTreeNode	= new TreeMap<String, ViewTreeNode>();
	
	
	public void addViewTreeNode(String path, ViewTreeNode node){
		this.stringPathToViewTreeNode.put(path, node);
	}
	
	public ViewTreeNode getViewTreeNode(String path){
		return this.stringPathToViewTreeNode.get(path);
	}
	
}
