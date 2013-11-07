package de.umg.mi.idrt.ioe.OntologyTree;

import java.io.UnsupportedEncodingException;

import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.jface.viewers.TreeViewer;

import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Debug;


public class OntologyTree extends JTree {

	public static String convertAlphanumericPIDtoNumber(String pid) {
		String newString = "";
		String tmpString = "";

		byte[] bytes = null;
		try {
			bytes = pid.getBytes("UTF-8");
			for (int x = 0; x < bytes.length; x++) {
				tmpString = String.valueOf(bytes[x]);
				if (tmpString.length() == 3) {
					// nothing
				} else if (tmpString.length() == 2) {
					tmpString = "0" + tmpString;
				}
				newString = newString + tmpString;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
		return newString;
	}

	private OntologyTreeNode treeRoot = new OntologyTreeNode();
	private OntologyTreeNodeList nodeLists = new OntologyTreeNodeList();
	private OntologyTreeNode firstItemNode = null;
	private String importDate = "";
	private TreeViewer treeViewer = null;


	private OntologyTreeNode _lastActiveNode;

	OntologyTreeNode i2b2RootNode;

	// naked constructor for serialization
	public OntologyTree() {

	}

	public OntologyTree(OntologyTreeModel treeModel) {
		super();
		this.setModel(treeModel);
		this.treeRoot = (OntologyTreeNode) treeModel.getRoot();
	}

	public OntologyTree(OntologyTreeNode treeRoot) {
		super();

		this.treeRoot = treeRoot;

		treeRoot.setTreePath("\\");
		treeRoot.setTreePathLevel(-1);

		getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		setEditable(false);

	}

	/**
	 * @param item
	 * @param ontologySource
	 * @param object
	 */
	public void addModifierNodeByPath(OntologyItem item, String ontologySource, NodeType nodeType) {
		OntologyTreeNode node = new OntologyTreeNode(item.getC_NAME());
		String path = item.getM_APPLIED_PATH().substring(0, item.getM_APPLIED_PATH().length()-1)+item.getC_FULLNAME();
		node.setID( node.getIDFromPath( item.getC_FULLNAME() ) );
		try {
			this.getNodeLists().addOTNode(path, node).add(node);
		}catch (Exception e) {
			//			Console.error("Could not add node \"" + item.getC_NAME()
			//					+ "\" to the tree, because there is no parent node for it.");
		}
		node.setTreeAttributes();
		node.setType(ontologySource);
		node.setOntologyCellAttributes(item);
		if (nodeType != null) {
			setI2B2RootNode(node);
		}
	}


	/**
	 * addNode: creates a new childnode of the type OTNode
	 * 
	 * @param parentnode
	 *            the parentnode of the new child node
	 * @param name
	 *            the name of the new child node
	 * @return the new node
	 */
	/*
	public OTNode addNode(OTNode parentnode, String name) {
		System.out.print("Adding node:" + name);
		OTNode node = new OTNode(name);

		System.out.println(" ... adding to parentnode:" + parentnode.getName());
		parentnode.add(node);

		return node;
	}
	 */

	/**
	 * addNode: creates a new childnode of the type OTNode
	 * 
	 * @param parentnode
	 *            the parentnode of the new child node
	 * @param name
	 *            the name of the new child node
	 * @return the new node
	 */
	public OntologyTreeNode addNode(OntologyTreeNode parentnode, String id, String name) {
		System.out.print("Adding node:" + name);
		OntologyTreeNode node = new OntologyTreeNode(name);
		node.setID(id);

		node.setTreePath(parentnode.getTreePath() + "\\" + id);
		node.setTreePathLevel(parentnode.getTreePathLevel() + 1);

		parentnode.add(node);
		this.getNodeLists().add(id, parentnode.getTreePath(), node);
		return node;
	}

	public OntologyTreeNode addNode(OntologyTreeNode parentnode, String id, String name, String path, int level) {
		System.out.print("Adding node:" + name);
		OntologyTreeNode node = new OntologyTreeNode(name);
		node.setID(id);

		node.setTreePath( path );
		node.setTreePathLevel( level );

		parentnode.add(node);
		this.getNodeLists().add(id, parentnode.getTreePath(), node);
		return node;
	}

	public OntologyTreeNode addNodeByPath(String parentPath, String itemID, String name) {

		OntologyTreeNode node = new OntologyTreeNode(name);
		this.getNodeLists().add(itemID, parentPath, node);
		OntologyTreeNode parent = getNodeLists().getNodeByPath(parentPath);
		if (parent != null) {
			parent.add(node);
			node.setTreeAttributes();
			return parent;
		} else {
			Debug.e("Couldn't add Node because there was no parent node found with the path \""
					+ parentPath + "\"");
			return null;
		}
	}
	public void addNodeByPath(String i2b2Path, String name, String source, OntologyItem item, NodeType type) {

		OntologyTreeNode node = new OntologyTreeNode(name);
		node.setID( node.getIDFromPath( i2b2Path ) );
		try {
			this.getNodeLists().addOTNode(i2b2Path, node).add(node);
		}catch (Exception e) {
//						e.printStackTrace();
			Console.error("Could not add node \"" + name
					+ "\" to the tree, because there is no parent node for it.");
		}
		node.setTreeAttributes();
		node.setType(source);
		if (item != null)
			node.setOntologyCellAttributes(item);
		if (type != null) {
			setI2B2RootNode(node);
		}
		//		else {
		//			if (parentNode != null) {
		//				parentNode.add(node);
		//			} else {
		//				Console.error("Could not add node \"" + name
		//						+ "\" to the tree, because there is no parent node for it.");
		//			}
		//		}
	}

	public String createSQLDateFromXMLDate(XMLGregorianCalendar xmlDate) {
		return xmlDate.toString();
	}

	public String deconvertAlphanumericPIDfromNumber(String pidString) {


		if (pidString.length() % 3 != 0) {
			System.err
			.println("@deconvertAlphanumericPIDfromNumber length%3 != 0");
			return "";
		}

		String byteString = "";
		for (int x = 0; x < pidString.length(); x = x + 3) {
			byteString += new String(new byte[] { Byte.parseByte(pidString
					.substring(x, x + 3)) });
		}
		return byteString;
	}

	public void deleteNode( OntologyTreeNode node ){

		for ( int x = 0; x < node.getChildCount(); x ++ ){

			deleteNode( (OntologyTreeNode) node.getChildAt(x) );

		}

		this.getNodeLists().removeNode(node);


	}

	public void deleteNode(String i2b2Path){

		OntologyTreeNode node = getNodeLists().getNodeByPath(i2b2Path);

		if (node == null || node.getParent() == null ) {
			Console.error("Could not delete node because there is no node or no parent node for it.");
			return;
		}

		deleteNode(node);


	}

	/**
	 * @return the activeItemNode
	 */
	public OntologyTreeNode getActiveNode() {
		return _lastActiveNode;
	}

	public OntologyTreeNode getFirstItemNode() {
		return this.firstItemNode;
	}

	/**
	 * @param myOntologyTree TODO
	 * @return the subRootNode
	 */
	public OntologyTreeNode getI2B2RootNode() {
		return i2b2RootNode;
	}

	/**
	 * getImportDate: returns the local importDate variable
	 * 
	 * @return String
	 */
	public String getImportDate() {
		return this.importDate;
	}

	public OntologyTreeNodeList getNodeLists() {
		return this.nodeLists;
	}

	public OntologyTreeNode getRootNode() {

		if (this.getTreeRoot() != null) {
			return this.getTreeRoot();
		}
		return null;
	}

	public OntologyTreeNode getTreeRoot() {

		if (this.treeRoot != null) {
			return this.treeRoot;

		} else {
			Debug.e("treeRoot == null");
			return null;
		}
	}

	public TreeViewer getTreeViewer() {
		return this.treeViewer;
	}

	public void initilize() {

		OntologyTreeModel OTModel = new OntologyTreeModel(this.getTreeRoot());
		setModel(OTModel);
		setEditable(true);

	}

	public boolean isTrue(String bool) {
		if (bool.equals("true")) {
			return true;
		} else if (bool.equals("false")) {
			return false;
		} else {
			Console.error("Could not convert the string \"" + bool
					+ "\" to boolean.");
			return false;
		}
	}

	public void printNode(OntologyTreeNode node, int level) {

		String bridge = "";

		for (int x = 0; x <= level; x++) {

			if (x == level)
				bridge = "-";
			else
				bridge = " ";

		}

		System.out.println(bridge + " " + node.getName() + " ("
				+ node.getTreePath() + ")");

		for (int y = 0; y < node.getChildCount(); y++) {
			printNode((OntologyTreeNode) node.getChildAt(y), level++);
		}

	}

	public void printTree() {
		System.out.println("--------------");
		System.out.println(" printTree:   ");
		printNode(this.getRootNode(), 2);
	}

	/**
	 * @param activeItemNode
	 *            the activeItemNode to set
	 */
	public void setActiveNode(OntologyTreeNode lastActiveNode) {
		this._lastActiveNode = lastActiveNode;
	}

	public void setFirstItemNode(OntologyTreeNode firstItemNode) {
		this.firstItemNode = firstItemNode;
	}


	/**
	 * @param myOntologyTree TODO
	 * @param i2b2RootNode the subRootNode to set
	 */
	public void setI2B2RootNode(OntologyTreeNode tmpi2b2RootNode) {
		i2b2RootNode = tmpi2b2RootNode;
	}


	/**
	 * setImportDate: set the local importDate variable
	 * 
	 * @param importDate
	 *            the importDate as a string
	 */
	public void setImportDate(String importDate) {
		this.importDate = importDate;
	}

	public void setTreeViewer(TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
	}


}
