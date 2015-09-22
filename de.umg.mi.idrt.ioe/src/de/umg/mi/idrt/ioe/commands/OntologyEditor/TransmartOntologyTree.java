package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTree;



public class TransmartOntologyTree extends JTree{
	private final static int ROOT = -1;
	private TransmartOntologyTreeItem root;
	private HashMap<String, TransmartOntologyTreeItem> nodes;
	private List<TransmartOntologyTreeItem> tempNodes;
		public TransmartOntologyTree() {
			nodes = new HashMap<String, TransmartOntologyTreeItem>();
			tempNodes = new LinkedList<TransmartOntologyTreeItem>();
			setRoot(new TransmartOntologyTreeItem("i2b2",-1,"\\i2b2\\"));
			nodes.put("\\i2b2\\", getRoot());
		}

	public TransmartOntologyTree(String rootName){
		nodes = new HashMap<String, TransmartOntologyTreeItem>();
		tempNodes = new LinkedList<TransmartOntologyTreeItem>();
		setRoot(new TransmartOntologyTreeItem(rootName,-1,"\\i2b2\\"));
		nodes.put("\\i2b2\\", getRoot());
	}


	public TransmartOntologyTreeItem getParentNode(TransmartOntologyTreeItem item){
		String [] split = item.getNodePath().split("\\\\");
		String parentPath = "";
		for (int i = 0; i < split.length-1;i++){
			parentPath += split[i]+"\\";
		}
		return nodes.get(parentPath);
	}

	public boolean addNodeByPath (TransmartOntologyTreeItem item){
		//if item is already added, do not add it again
		if (!nodes.containsKey(item.getNodePath())){
			//if node has a parent, add it. if not, add to the rootnode
			if (getParentNode(item) != null) {
				nodes.put(item.getNodePath(), item);
				return getParentNode(item).addNode(item);
			}
			else if (item.getHlevel() == 0) {
				nodes.put(item.getNodePath(), item);
				return root.addNode(item);
			}
			else {
				//saving item for later
				tempNodes.add(item);
				return false;
			}
		}
		else {
			System.err.println(item + " already added!");
			return false;
		}

	}

	public boolean removeNode (TransmartOntologyTreeItem item){
		if (nodes.containsKey(item.getNodePath())){
			nodes.remove(item.getNodePath());
			return item.getParent().getChildren().remove(item);
		}
		else
			return false;
	}

	public void display(TransmartOntologyTreeItem identifier) {
		this.display(identifier, ROOT);
	}

	public void display(TransmartOntologyTreeItem nodeToDisplay, int depth) {
		this.display(nodeToDisplay.getNodePath(),depth);
	}
	public void display(String nodeToDisplay) {
		this.display(nodeToDisplay, ROOT);
	}
	public void display(){
		System.out.println(getRoot().getPath());
		this.display(getRoot().getNodePath(),ROOT);
	}

	public void display(String nodeToDisplay, int depth) {
		if (nodes!= null && nodes.get(nodeToDisplay) != null){
			List<TransmartOntologyTreeItem> children = nodes.get(nodeToDisplay).getChildren();
			if (depth == ROOT) {
				System.out.println(nodes.get(nodeToDisplay));
			} else {
				String tabs = String.format("%0" + (depth+1) + "d", 0).replace("0", "  "); // 4 spaces
				System.out.println(tabs + nodes.get(nodeToDisplay));
			}
			depth++;
			for (TransmartOntologyTreeItem child : children) {
				// Recursive call
				this.display(child, depth);
			}
		}
		else {
			//			System.out.println(nodes);
			System.out.println("TREE EMPTY?");
		}
	}

	@Override
	public String toString() {
		return root.toString();
	}

	public TransmartOntologyTreeItem getRoot() {
		return root;
	}

	public void setRoot(TransmartOntologyTreeItem root) {
		nodes.put(root.getNodePath(), root);
		this.root = root;
	}

	public void move(TransmartOntologyTreeItem item, TransmartOntologyTreeItem newParent) {
		removeNode(item);
		newParent.addNode(item);
		nodes.put(item.getNodePath(), item);
	}


	public HashMap<String, TransmartOntologyTreeItem> getNodes() {
		return nodes;
	}


	public void setNodes(HashMap<String, TransmartOntologyTreeItem> nodes) {
		this.nodes = nodes;
	}

	
	
	public void tryToAddNodes() {
		LinkedList<TransmartOntologyTreeItem> tmp = new LinkedList<TransmartOntologyTreeItem>();
		tmp.addAll(tempNodes);
		tempNodes.clear();
		Collections.sort(tmp, new Comparator<TransmartOntologyTreeItem>() {
	        @Override
	        public int compare(TransmartOntologyTreeItem  item1, TransmartOntologyTreeItem  item2)
	        {
	            return  item1.getNodePath().compareTo(item2.getNodePath());
	        }
	    });
		
		LinkedList<TransmartOntologyTreeItem> itemsAdded = new LinkedList<TransmartOntologyTreeItem>();
		for (TransmartOntologyTreeItem item : tmp){			
			if (this.addNodeByPath(item))
				itemsAdded.add(item);
		}
	}


	public List<TransmartOntologyTreeItem> getTempNodes() {
		return tempNodes;
	}


	public void setTempNodes(List<TransmartOntologyTreeItem> tempNodes) {
		this.tempNodes = tempNodes;
	}
}
