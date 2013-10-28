package de.umg.mi.idrt.ioe.view;

import java.util.ArrayList;
import java.util.List;

import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;

public class OTtoTreeContentProvider {
	
	public List<OntologyTreeNode> getTargetModel(){
		List<OntologyTreeNode> list2 = new ArrayList<OntologyTreeNode>();
		list2.add(OntologyEditorView.getI2b2ImportTool().getMyOntologyTrees().getSubRootNode());
		return list2;
	}
	
	public List<OntologyTreeNode> getStagingModel(){
		List<OntologyTreeNode> list2 = new ArrayList<OntologyTreeNode>();
		list2.add(OntologyEditorView.getI2b2ImportTool().getMyOntologyTrees().getOntologyTreeSource().getStagingRootNode());
		return list2;
	}
	
	
}
