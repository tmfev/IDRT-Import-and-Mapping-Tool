package de.umg.mi.idrt.ioe.OntologyTree;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

public class OTtoJFaceTreeConvert {

	private OntologyTree _ontologyTree;
	private Tree _viewTree;

	public OTtoJFaceTreeConvert(OntologyTree ontologyTree, Composite composite){
		
		_ontologyTree = ontologyTree;
		_viewTree = new Tree(composite, 0);

	}
	
	
	
}
