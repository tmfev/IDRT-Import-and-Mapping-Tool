package de.umg.mi.idrt.ioe.view;

import java.util.ArrayList;
import java.util.List;

import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;

public class OTtoTreeContentProvider {
	
	List<String> list = new ArrayList<String>();
	List<OntologyTreeNode> list2 = new ArrayList<OntologyTreeNode>();
	
	public List<String> getModel(){
		list.add("BranchA1");
		list.add("BranchA2");
		return list;
	}
	
	public List<OntologyTreeNode> getNodeModel(){
		OntologyTreeNode rootNode = new OntologyTreeNode ("TopNode");
		list2.add(rootNode);
		
		return list2;
	}

	
}
