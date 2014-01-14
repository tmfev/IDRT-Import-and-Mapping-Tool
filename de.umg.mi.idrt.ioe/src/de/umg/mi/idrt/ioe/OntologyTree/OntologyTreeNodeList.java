package de.umg.mi.idrt.ioe.OntologyTree;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
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
	private HashMap<String, OntologyTreeNode> stringPathToNode;
	private long time = 0;
	public OntologyTreeNodeList(){
		stringPathToNode = new HashMap<String, OntologyTreeNode>();
	}

	public void add( OntologyTreeNode node ){

		this.addNodyByPath( node.getTreePath(), node );
	}

	public void add( String itemID, String stringPath, OntologyTreeNode node ){
		if ( !"\\".equals(stringPath))
			this.addNodyByPath( stringPath + "\\" + itemID, node );
		else
			this.addNodyByPath( "\\", node );
	}

	public void addNodyByPath( String stringPath, OntologyTreeNode node ){
//		if ( stringPathToNode.size() < 30 || stringPathToNode.size() % 1000 == 0 ) {
//			long newTime = System.currentTimeMillis();
//
//			System.out.println((newTime-time)+ "ms: addOTNode # " + stringPathToNode.size() +" " + node.getName() + "  -> " + node.getTreePath());
//			time = newTime;
//		}
		this.stringPathToNode.put( stringPath, node );
	}

	public OntologyTreeNode addOTNode( String i2b2Path, OntologyTreeNode node ){
		OntologyTreeNode parentNode = null;
		PathAndID pathAndID = Resource.OntologyTreeHelpers.getParentPathAndIDFromI2B2Path( i2b2Path );

		if ( pathAndID.getParentPath().isEmpty() ){
			System.err.println("Could not add node \"" + node.getName() +"\", because no parent path was given.");
			return null;
		} else {
			parentNode = this.getNodeByPath( pathAndID.getParentPath() );
			if (parentNode == null){
				String path = pathAndID.getParentPath().substring(0, pathAndID.getParentPath().length()-1);
				node.setID(path.substring(path.lastIndexOf("\\")+1,path.length()));
				path = path.substring(0,path.lastIndexOf("\\"))+"\\";
				parentNode =  this.getNodeByPath(path);
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
		for (String path : stringPathToNode.keySet()){
			System.out.println("  - " + path);
		}
	}

	public void removeAll() {
		stringPathToNode.clear();
		System.gc();
	}

	public void removeNode(OntologyTreeNode node){
		stringPathToNode.remove(node.getTreePath());
	}

	/**
	 * @return the stringPathToNode
	 */
	public HashMap<String, OntologyTreeNode> getStringPathToNode() {
		return stringPathToNode;
	}
}
