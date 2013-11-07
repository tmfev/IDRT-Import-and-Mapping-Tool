package de.umg.mi.idrt.ioe.OntologyTree;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.Resource.OntologyTreeHelpers.PathAndID;

/**
 * @author Christian Bauer <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> 
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 * 			Department of Medical Informatics Goettingen 
 * 			www.mi.med.uni-goettingen.de
 */
public class OntologyTreeNodeList {
	private long time;
	private HashMap<String, OntologyTreeNode> stringPathToNode	= new HashMap<String, OntologyTreeNode>();

	public OntologyTreeNodeList(){

	}


	public void add( OntologyTreeNode node ){
//		if ( stringPathToNode.size() < 30 || stringPathToNode.size() % 1000 == 0 ){
			long newTime = System.currentTimeMillis();

			System.out.println((newTime-time)+ "ms: addOTNode # " + stringPathToNode.size() +" " + node.getName() + "  -> " + node.getTreePath());
			time = newTime;
//		}
		this.addNodyByPath( node.getTreePath(), node );
	}

	public void add( String itemID, String stringPath, OntologyTreeNode node ){

		//this.addNodyByPath( stringPath, node );
		if ( !"\\".equals(stringPath))
			this.addNodyByPath( stringPath + "\\" + itemID, node );
		else
			this.addNodyByPath( "\\", node );
	}

	public void addNodyByPath( String stringPath, OntologyTreeNode node ){
		this.stringPathToNode.put( stringPath, node );
	}

	public OntologyTreeNode addOTNode( String i2b2Path, OntologyTreeNode node ){
		OntologyTreeNode parentNode = null;
		PathAndID pathAndID = Resource.OntologyTreeHelpers.getParentPathAndIDFromI2B2Path( i2b2Path );

//		if ( stringPathToNode.size() < 30 || stringPathToNode.size() % 1000 == 0 ){
//			long newTime = System.currentTimeMillis();
//
//			System.out.println((newTime-time)+ "ms: addOTNode # " + stringPathToNode.size() +" " + node.getName() + "  -> " + i2b2Path + " || " + pathAndID.getParentPath() + " -> " + pathAndID.getID());
//			time = newTime;
//		}

		if ( pathAndID.getParentPath().isEmpty() ){
System.err.println("Could not add node \"" + node.getName() +"\", because no parent path was given.");
			return null;
			/*
			} else if ( pathAndID.getParentPath().equals("\\") ){
				Console.info("Node \"" + node.getName() +"\" has no real parent path, so it's parent is the root node and forever it shall be .");

				parentNode = Application.getEditorSourceView().getI2B2ImportTool().getOTCreator().getMyOT().getSourceRootNode();
			 */
		} else {
			parentNode = this.getNodeByPath( pathAndID.getParentPath() );
			System.out.println(" pathAndID.getParentPath()" +  pathAndID.getParentPath());
			if (parentNode == null){
				String path = pathAndID.getParentPath().substring(0, pathAndID.getParentPath().length()-1);
				node.setID(path.substring(path.lastIndexOf("\\")+1,path.length()));
				path = path.substring(0,path.lastIndexOf("\\"))+"\\";
				parentNode =  this.getNodeByPath(path);
				System.out.println("!!! " +parentNode.getTreePath() + " " + parentNode.getName());
				if (parentNode == null) {
					Console.info("The node \"" + node.getName() +"\" ( parentPath:" + pathAndID.getParentPath() +" ) could not be added, because the path for its parent node \"" + pathAndID.getParentPath() + "\" did not lead to a node.");
					return null;
				}
			}
		}

		this.addNodyByPath( i2b2Path, node );

		node.setTreePath( i2b2Path );
		if ( parentNode != null )
			node.setTreePathLevel( parentNode.getTreePathLevel() + 1 );

		return parentNode;
	}


	public OntologyTreeNode getNodeByPath( String stringPath ) {
		return stringPathToNode.get( stringPath );
	}

	public int getNumberOfItemNodes(){
		return stringPathToNode.size();
	}

	public Iterator<Entry<String, OntologyTreeNode>> getPathToNodeIterator () {
		return this.stringPathToNode.entrySet().iterator();
	}

	public void listNodeByPath(){
		System.out.println(" Path to Nodes:");
		for (String path : stringPathToNode.keySet()){
			System.out.println("  - " + path);
		}
	}

	public void removeAll() {
		stringPathToNode.clear();
	}

	public void removeNode(OntologyTreeNode node){
		stringPathToNode.remove(node.getTreePath());
	}
}
