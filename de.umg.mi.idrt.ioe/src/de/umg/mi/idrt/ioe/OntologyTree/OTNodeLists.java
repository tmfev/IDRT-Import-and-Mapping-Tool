package de.umg.mi.idrt.ioe.OntologyTree;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Debug;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.Resource.OntologyTreeHelpers.PathAndID;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;


public class OTNodeLists {
		
		private Map<String, List<OntologyTreeNode>> iDtoNodes = new TreeMap<String, List<OntologyTreeNode>>();
		public Map<String, OntologyTreeNode> stringPathToNode	= new HashMap<String, OntologyTreeNode>();
		private Map<String, List<String>> iDtoStringPaths		= new TreeMap<String, List<String>>();
		private Map<String, ItemStatus> stringPathToItemStatus		= new TreeMap<String, ItemStatus>();
		
		public Map<String, ViewTreeNode> stringPathToViewTreeNode	= new TreeMap<String, ViewTreeNode>();
		
		public OTNodeLists(){
			
		}

		
		public void add( OntologyTreeNode node ){
			
			this.addIDtoPaths( node.getID(), node.getTreePath() );
			this.addNodeByID( node.getID(), node );
			
			this.addNodyByPath( node.getTreePath(), node );
			this.setNodeStatusByPath( node.getTreePath(), ItemStatus.UNCHECKED);
		}
		
		public void add( String itemID, String stringPath, OntologyTreeNode node ){
			
			this.addIDtoPaths( itemID, stringPath );
			this.addNodeByID( itemID, node );
			
			//this.addNodyByPath( stringPath, node );
			if ( !"\\".equals(stringPath))
				this.addNodyByPath( stringPath + "\\" + itemID, node );
			else
				this.addNodyByPath( "\\", node );
			
			this.setNodeStatusByPath( stringPath, ItemStatus.UNCHECKED);
		}
		
		public OntologyTreeNode addOTNode( String i2b2Path, OntologyTreeNode node ){
			OntologyTreeNode parentNode = null;
			PathAndID pathAndID = Resource.OntologyTreeHelpers.getParentPathAndIDFromI2B2Path( i2b2Path );

			
			if ( stringPathToNode.size() < 30 || stringPathToNode.size() % 400 == 0 ){
				System.out.println("addOTNode #" + stringPathToNode.size() +" " + node.getName() + "  -> " + i2b2Path + " || " + pathAndID.getParentPath() + " -> " + pathAndID.getID());
			}
		
			if ( pathAndID.getParentPath().isEmpty() ){
				Console.error("Could not add node \"" + node.getName() +"\", because no parent path was given.");
				return null;
				/*
			} else if ( pathAndID.getParentPath().equals("\\") ){
				Console.info("Node \"" + node.getName() +"\" has no real parent path, so it's parent is the root node and forever it shall be .");
				
				parentNode = Application.getEditorSourceView().getI2B2ImportTool().getOTCreator().getMyOT().getSourceRootNode();
			*/
			} else {
				parentNode = this.getNodeByPath( pathAndID.getParentPath() );
				if (parentNode == null){
					Console.info("The node \"" + node.getName() +"\" ( parentPath:" + pathAndID.getParentPath() +" ) could not be added, because the path for its parent node \"" + pathAndID.getParentPath() + "\" did not lead to a node.");
					return null;
				}
			}

			this.addIDtoPaths( pathAndID.getID(), i2b2Path );
			this.addNodeByID( pathAndID.getID(), node );
			this.addNodyByPath( i2b2Path, node );
			this.setNodeStatusByPath( i2b2Path, ItemStatus.UNCHECKED);
			
			
			node.setTreePath( i2b2Path );
			if ( parentNode != null )
				node.setTreePathLevel( parentNode.getTreePathLevel() + 1 );
			
			return parentNode;
		}
		


		public void addNodyByPath( String stringPath, OntologyTreeNode node ){
			this.stringPathToNode.put( stringPath, node );
		}

		public Iterator<Entry<String, OntologyTreeNode>> getPathToNodeIterator () {
			return this.stringPathToNode.entrySet().iterator();
		}

		public OntologyTreeNode getNodeByPath( String stringPath ) {
			return stringPathToNode.get( stringPath );
		}
		
		public void addNodeByID( String itemID, OntologyTreeNode node ) {
			if( !iDtoNodes.containsKey( itemID ) ) {
				iDtoNodes.put( itemID, new ArrayList<OntologyTreeNode>() );
			} 
			iDtoNodes.get( itemID ).add( node );
		}
		
		public List<OntologyTreeNode> getNodesByID( String itemID ) {
			return this.iDtoNodes.get( itemID );
		}
		
		public Iterator<Entry<String, List<OntologyTreeNode>>> getIDtoNodesIterator() {
			return this.iDtoNodes.entrySet().iterator();
		}
		
		public void addIDtoPaths( String itemID, String stringPath ) {
			if( !iDtoStringPaths.containsKey( itemID ) ) {
				iDtoStringPaths.put( itemID, new ArrayList<String>() );
			} 
			iDtoStringPaths.get( itemID ).add( stringPath );
		}
		
		public void setNodeStatusByPath( String stringPath, ItemStatus itemStauts ){
			this.stringPathToItemStatus.put( stringPath, itemStauts );
		}
		
		public List<String> getPathsByID( String itemID ) {
			
			if ( this.iDtoStringPaths.containsKey( itemID ) ) {
				return this.iDtoStringPaths.get( itemID );
			} else
				return null;
		}
		
		public int getNumberOfNodesByID( String itemID ) {
			
			List<String> itemList = this.getPathsByID( itemID );
			
			if( itemList!= null )
				return itemList.size();
			return 0;            
		}
		
		public int getNumberOfUncheckedItems(){
			
			Iterator<ItemStatus> iterator = this.stringPathToItemStatus.values().iterator();
			int counter = 0;
			
			while(iterator.hasNext()){
				ItemStatus status = iterator.next();
				if (status.equals(ItemStatus.UNCHECKED)){
					counter++;
				}
			}
			
			return counter;
		}
		
		public int getNumberOfItemNodes(){
			return stringPathToNode.size();
		}
		
		public ItemStatus getNodeStatusByPath(String stringPath){
			return stringPathToItemStatus.get(stringPath);
		}
		
		
		public void listNodeByPath(){
			System.out.println(" Path to Nodes:");
			for (String path : stringPathToNode.keySet()){
				System.out.println("  - " + path);
			}
			
		}
		
		public void addViewTreeNode(String path, ViewTreeNode node){
			this.stringPathToViewTreeNode.put(path, node);
		}
		
		public ViewTreeNode getViewTreeNode(String path){
			return this.stringPathToViewTreeNode.get(path);
		}
		
		public void removeNode(OntologyTreeNode node){
			iDtoNodes.remove(node.getID());
			stringPathToNode.remove(node.getTreePath());
			iDtoStringPaths.remove(node.getID());
			stringPathToItemStatus.remove(node.getTreePath());
			stringPathToViewTreeNode.remove(node.getTreePath());
			
		}
}
	

