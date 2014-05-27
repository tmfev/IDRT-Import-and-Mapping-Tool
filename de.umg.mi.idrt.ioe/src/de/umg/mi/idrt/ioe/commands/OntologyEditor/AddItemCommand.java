package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;

import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.Resource.I2B2.NODE.TYPE;
import de.umg.mi.idrt.ioe.OntologyTree.Dimension;
import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTrees;
import de.umg.mi.idrt.ioe.OntologyTree.NodeType;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class AddItemCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("Adding Item");
		OntologyTreeNode currentNode = OntologyEditorView.getCurrentTargetNode();

		OntologyTreeNode subRootNode = new OntologyTreeNode("New Node");

		subRootNode.setID("customItem");
		//		subRootNode.setTreePath("\\i2b2\\customNode\\");
		//		subRootNode.setTreeAttributes();
		//		subRootNode.setTreePathLevel(1);
		subRootNode.setType(TYPE.ONTOLOGY_TARGET);
//		subRootNode.getTargetNodeAttributes().addStagingPath("");
		subRootNode.getTargetNodeAttributes().setDimension(Dimension.CONCEPT_DIMENSION);
		subRootNode.getTargetNodeAttributes().setVisualattributes("LAE");
		subRootNode.getTargetNodeAttributes().getTargetNodeMap().put(Resource.I2B2.NODE.TARGET.M_APPLIED_PATH, "@");
		currentNode.add(subRootNode);
		subRootNode.setTreeAttributes();
		subRootNode.getTargetNodeAttributes().getTargetNodeMap().put(Resource.I2B2.NODE.TARGET.C_BASECODE, subRootNode.getTreePath().replaceAll("\\\\", "|").substring(0, subRootNode.getTreePath().length()-1));
//		for (OntologyTreeNode n : OntologyEditorView.getOntologyTargetTree().getNodeLists().getStringPathToNode().values()) {
//			System.out.println(n.getTreePathLevel() + " " + n.getTreePath());
//		}
		int counter = 1;
		while (OntologyEditorView.getOntologyTargetTree().getNodeLists().getNodeByPath(subRootNode.getTreePath())!=null) {
			String oldPath = subRootNode.getID();
			if (oldPath.contains("_"))
				oldPath = oldPath.substring(0,oldPath.lastIndexOf("_"));
			oldPath = oldPath+"_"+counter;
			subRootNode.setID(oldPath);
			subRootNode.setTreeAttributes();
			counter++;
		}


		OntologyEditorView.getOntologyTargetTree().getNodeLists().add(subRootNode);
		subRootNode.setTreeAttributes();
		//			currentNode.add(subRootNode);
		//			OntologyEditorView.getOntologyTargetTree().getNodeLists().add(subRootNode);





		OntologyEditorView.setCurrentTargetNode(subRootNode);


		TreeViewer targetTreeViewer = OntologyEditorView.getTargetTreeViewer();
		//		targetTreeViewer.expandToLevel(subRootNode, 10);
		targetTreeViewer.setSelection(new StructuredSelection(subRootNode), true);
		targetTreeViewer.editElement(subRootNode, 0);



		targetTreeViewer.refresh();
		//		OntologyTreeNode test = (OntologyTreeNode) OntologyEditorView.getOntologyTargetTree().getRootNode().getNextNode();

		TreeViewerColumn column = OntologyEditorView.getTargetTreeViewerColumn();
		column.getViewer().editElement(subRootNode, 0);
		//		subRootNode.setID(subRootNode.getName());
		//		subRootNode.setTreeAttributes();
		//		System.out.println(test.getName());		
		return null;
	}
}