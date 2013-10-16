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
 * This is the special node class for the ontology tree. It holds some basic
 * informations like name, stringpath, nodetype and the visual attribute for
 * I2B2 from its OntologyTreeNode parent and some specific information about
 * the target.
 * 
 * @author Christian Bauer
 * @version 0.9
 */
public class OntologyTreeTargetRootNode extends OntologyTreeNode {

	private Target target = null;
	private TargetProject targetProject = null;
	
	private TargetProjects targetProjects = new TargetProjects();

	
	public OntologyTreeTargetRootNode(String name) {
		super(name);
		
		
	}
	
	public TargetProjects getTargetProjects(){
		return this.targetProjects;
	}

}
