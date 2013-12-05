package de.umg.mi.idrt.ioe.OntologyTree;

import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;
import org.eclipse.jface.viewers.TreeViewer;

import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> Department of
 *         Medical Informatics Goettingen www.mi.med.uni-goettingen.de
 * 
 * 
 */

@SuppressWarnings("serial")
public class OntologyTree extends JTree {

	private OntologyTreeNode treeRoot = new OntologyTreeNode();
	private OntologyTreeNodeList nodeLists = new OntologyTreeNodeList();

	OntologyTreeNode i2b2RootNode;
	private TreeViewer treeViewer = null;

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
	public void addModifierNodeByPath(OntologyItem item, String ontologySource,
			NodeType nodeType) {
		OntologyTreeNode node = new OntologyTreeNode(item.getC_NAME());
		String path = item.getM_APPLIED_PATH().substring(0,
				item.getM_APPLIED_PATH().length() - 1)
				+ item.getC_FULLNAME();
		node.setID(node.getIDFromPath(item.getC_FULLNAME()));
		try {
			this.getNodeLists().addOTNode(path, node).add(node);
		} catch (Exception e) {
			System.err.println("no parent");
			Console.error("Could not add node \"" + item.getC_NAME()
					+ "\" to the tree, because there is no parent node for it.");
			
			
		}
		node.setTreeAttributes();
		node.setType(ontologySource);
		node.setOntologyCellAttributes(item);
		if (nodeType != null) {
			setI2B2RootNode(node);
		}
	}

	public OntologyTreeNode addNode(OntologyTreeNode parentnode, String id,
			String name, String path, int level) {
		System.out.print("Adding node:" + name);
		OntologyTreeNode node = new OntologyTreeNode(name);
		node.setID(id);

		node.setTreePath(path);
		node.setTreePathLevel(level);

		parentnode.add(node);
		this.getNodeLists().add(id, parentnode.getTreePath(), node);
		return node;
	}

	public OntologyTreeNode addNodeByPath(String parentPath, String itemID,
			String name) {

		OntologyTreeNode node = new OntologyTreeNode(name);
		this.getNodeLists().add(itemID, parentPath, node);
		OntologyTreeNode parent = getNodeLists().getNodeByPath(parentPath);
		if (parent != null) {
			parent.add(node);
			node.setTreeAttributes();
			return parent;
		} else {
			Console.error("Couldn't add Node because there was no parent node found with the path \""
					+ parentPath + "\"");
			return null;
		}
	}

	public void addNodeByPath(String i2b2Path, String name, String source,
			OntologyItem item, NodeType type) {

		OntologyTreeNode node = new OntologyTreeNode(name);
		node.setID(node.getIDFromPath(i2b2Path));
		try {
			this.getNodeLists().addOTNode(i2b2Path, node).add(node);
		} catch (Exception e) {
			// e.printStackTrace();
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
		// else {
		// if (parentNode != null) {
		// parentNode.add(node);
		// } else {
		// Console.error("Could not add node \"" + name
		// + "\" to the tree, because there is no parent node for it.");
		// }
		// }
	}

	public void addNodeByPath(String i2b2Path, String name, String source,
			OntologyItemTarget item, NodeType type) {

		OntologyTreeNode node = new OntologyTreeNode(name);
		node.setID(node.getIDFromPath(i2b2Path));
		try {
			this.getNodeLists().addOTNode(i2b2Path, node).add(node);
		} catch (Exception e) {
			// e.printStackTrace();
			Console.error("Could not add node \"" + name
					+ "\" to the tree, because there is no parent node for it.");
		}
		node.setTreeAttributes();
		node.setType(source);
		node.getTargetNodeAttributes().setVisualattributes(
				item.getVisualattributes());
		node.getTargetNodeAttributes().addStagingPath(item.getStagingPath());
		node.getTargetNodeAttributes().setEndDateSourcePath(
				item.getEnddateStagingPath());
		node.getTargetNodeAttributes().setStartDateSourcePath(
				item.getStartdateStagingPath());
		node.getTargetNodeAttributes().setName(item.getName());
		if (type != null) {
			setI2B2RootNode(node);
		}
		OntologyEditorView.getOntologyTargetTree().getNodeLists().add(node);
	}

	public void deleteNode(OntologyTreeNode node) {

		for (int x = 0; x < node.getChildCount(); x++) {

			deleteNode((OntologyTreeNode) node.getChildAt(x));

		}

		this.getNodeLists().removeNode(node);

	}

	public void deleteNode(String i2b2Path) {

		OntologyTreeNode node = getNodeLists().getNodeByPath(i2b2Path);

		if (node == null || node.getParent() == null) {
			Console.error("Could not delete node because there is no node or no parent node for it.");
			return;
		}

		deleteNode(node);

	}

	public OntologyTreeNode getI2B2RootNode() {
		return i2b2RootNode;
	}

	public OntologyTreeNodeList getNodeLists() {
		return this.nodeLists;
	}

	public OntologyTreeNode getRootNode() {

		return treeRoot;
	}

	public TreeViewer getTreeViewer() {
		return this.treeViewer;
	}

	public void setTreeViewer(TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
	}

	public void initilize() {

		OntologyTreeModel OTModel = new OntologyTreeModel(this.getRootNode());
		setModel(OTModel);
		setEditable(true);
	}

	public void setI2B2RootNode(OntologyTreeNode tmpi2b2RootNode) {
		i2b2RootNode = tmpi2b2RootNode;
	}

}
