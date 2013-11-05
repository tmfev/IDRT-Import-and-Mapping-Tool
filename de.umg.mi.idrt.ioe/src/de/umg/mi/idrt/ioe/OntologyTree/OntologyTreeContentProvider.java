package de.umg.mi.idrt.ioe.OntologyTree;

import java.util.ArrayList;
import java.util.List;

import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class OntologyTreeContentProvider {
	
	public List<OntologyTreeNode> getStagingModel(){
		List<OntologyTreeNode> list2 = new ArrayList<OntologyTreeNode>();
		list2.add(OntologyEditorView.getOntologyStagingTree().getI2B2RootNode());
		return list2;
	}
	
	public List<OntologyTreeNode> getTargetModel(){
		List<OntologyTreeNode> list2 = new ArrayList<OntologyTreeNode>();
		list2.add(OntologyEditorView.getOntologyTargetTree().getI2B2RootNode());
		return list2;
	}
	
	
}
