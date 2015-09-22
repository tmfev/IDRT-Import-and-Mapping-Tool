package de.umg.mi.idrt.imt.transmart;

import java.util.List;

import javax.swing.JTree;

public class TransmartConfigTree extends JTree{

	private static TransmartConfigTreeItem ROOT;

	public static TransmartConfigTreeItem getRoot(){
		return ROOT;
	}
	public TransmartConfigTree() {
		ROOT = new TransmartConfigTreeItem("tranSMART");
		ROOT.setHlevel(0);
		
		
		getRoot().addNode(new TransmartConfigTreeItem("Internal Studies"));
		getRoot().addNode(new TransmartConfigTreeItem("Public Studies"));
		
		
		for (int i = 0; i < 5; i++){
			TransmartConfigTreeItem item = new TransmartConfigTreeItem("item "+i);
			getRoot().addNode(item);
			for (int i2 = 0; i2 < 3; i2++){
				TransmartConfigTreeItem item2 = new TransmartConfigTreeItem("item "+i2);
				item2.setVisualAttribute("c");
				item.addNode(item2);
			}
		}
	}

	public boolean addNode(TransmartConfigTreeItem parent, TransmartConfigTreeItem child){
		return parent.addNode(child);
	}
	
	public boolean removeNode(TransmartConfigTreeItem child){
		TransmartConfigTreeItem parent = child.getParent();
		return parent.removeNode(child);
		
	}
	
	public void display(){
		System.out.println("DISPLAY");
		display(getRoot(),0);
	}
	public void display(TransmartConfigTreeItem nodeToDisplay, int depth) {
		if (nodeToDisplay!= null){
			List<TransmartConfigTreeItem> children = nodeToDisplay.getChildren();
			if (depth == ROOT.getHlevel()) {
				System.out.println(nodeToDisplay);
			} else {
				String tabs = String.format("%0" + (depth+1) + "d", 0).replace("0", "  "); // 4 spaces
				System.out.println(tabs + nodeToDisplay);
			}
			depth++;
			for (TransmartConfigTreeItem child : children) {
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
		return ROOT.toString();
	}
}
