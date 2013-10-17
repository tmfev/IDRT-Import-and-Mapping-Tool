package de.umg.mi.idrt.ioe.OntologyTree;


/**
 * This is the special node class for the ontology tree. It holds some basic
 * informations like name, stringpath, nodetype and the visual attribute for
 * I2B2 from its OntologyTreeNode parent and some specific information about
 * the target.
 * 
 * @author Christian Bauer
 * @version 0.9
 */
public class OntologyTreeTargetRootNode extends OntologyTreeNode {


	
	private TargetProjects targetProjects = new TargetProjects();

	
	public OntologyTreeTargetRootNode(String name) {
		super(name);
		
		
	}
	
	public TargetProjects getTargetProjects(){
		return this.targetProjects;
	}

}
