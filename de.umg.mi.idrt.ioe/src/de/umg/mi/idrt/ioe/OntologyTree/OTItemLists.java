package de.umg.mi.idrt.ioe.OntologyTree;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


public class OTItemLists {
		
		public Map<String, List<OntologyTreeNode>> iDtoNodes	= new TreeMap<String, List<OntologyTreeNode>>();
		public Map<String, OntologyTreeNode> stringPathToNode	= new HashMap<String, OntologyTreeNode>();
		private Map<String, List<String>> iDtoStringPaths			= new TreeMap<String, List<String>>();
		private Map<String, ItemStatus> stringPathToItemStatus		= new TreeMap<String, ItemStatus>();
		
		public OTItemLists(){
			
		}

		public void add( String itemID, String stringPath, OntologyTreeNode node ){
			this.addIDtoPaths( itemID, stringPath );
			this.addNodeByID( itemID, node );
			this.addNodyByPath( stringPath, node );
			this.setItemStatusByPath( stringPath, ItemStatus.UNCHECKED);
		}
		
		public void addNodyByPath( String stringPath, OntologyTreeNode node ){
			this.stringPathToNode.put( stringPath, node );
		}
		
		public Iterator<Entry<String, OntologyTreeNode>> getPathToNodeIterator () {
			return this.stringPathToNode.entrySet().iterator();
		}
		
		public OntologyTreeNode getItemNodeByPath( String stringPath ) {
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
		
		public int getNumberOfIDtoNodes() {
			return this.iDtoNodes.size();
		}
		
		public void addIDtoPaths( String itemID, String stringPath ) {
			if( !iDtoStringPaths.containsKey( itemID ) ) {
				iDtoStringPaths.put( itemID, new ArrayList<String>() );
			} 
			iDtoStringPaths.get( itemID ).add( stringPath );
		}
		
		public void setItemStatusByPath( String stringPath, ItemStatus itemStauts ){
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
		
		public ItemStatus getItemStatusByPath(String stringPath){
			return stringPathToItemStatus.get(stringPath);
		}
		

}
	

