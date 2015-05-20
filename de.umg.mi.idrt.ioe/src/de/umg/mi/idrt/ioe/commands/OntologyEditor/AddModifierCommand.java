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
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class AddModifierCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("Adding Modifier");
		OntologyTreeNode currentNode = OntologyEditorView.getCurrentTargetNode();

		OntologyTreeNode modifierNode = new OntologyTreeNode("New Modifier",true);

		modifierNode.setID("customModifier");
		//		subRootNode.setTreePath("\\i2b2\\customNode\\");
		//		subRootNode.setTreeAttributes();
		//		subRootNode.setTreePathLevel(1);
		modifierNode.setType(TYPE.ONTOLOGY_TARGET);
//		modifierNode.getTargetNodeAttributes().addStagingPath("");
		modifierNode.getTargetNodeAttributes().setDimension(Dimension.MODIFIER_DIMENSION);
		modifierNode.getTargetNodeAttributes().setVisualattributes("RA ");
		modifierNode.getTargetNodeAttributes().getTargetNodeMap().put(Resource.I2B2.NODE.TARGET.M_APPLIED_PATH, currentNode.getTreePath()+"%");
		currentNode.add(modifierNode);
		modifierNode.setTreeAttributes();
//		for (OntologyTreeNode n : OntologyEditorView.getOntologyTargetTree().getNodeLists().getStringPathToNode().values()) {
//			System.out.println(n.getTreePathLevel() + " " + n.getTreePath());
//		}
		int counter = 1;
		while (OntologyEditorView.getOntologyTargetTree().getNodeLists().getNodeByPath(modifierNode.getTreePath())!=null) {
			String oldPath = modifierNode.getID();
			if (oldPath.contains("_"))
				oldPath = oldPath.substring(0,oldPath.lastIndexOf("_"));
			oldPath = oldPath+"_"+counter;
			modifierNode.setID(oldPath);
			modifierNode.setTreeAttributes();
			counter++;
		}


		OntologyEditorView.getOntologyTargetTree().getNodeLists().add(modifierNode);
		modifierNode.setTreeAttributes();
		//			currentNode.add(subRootNode);
		//			OntologyEditorView.getOntologyTargetTree().getNodeLists().add(subRootNode);





		OntologyEditorView.setCurrentTargetNode(modifierNode);


		TreeViewer targetTreeViewer = OntologyEditorView.getTargetTreeViewer();
		//		targetTreeViewer.expandToLevel(subRootNode, 10);
		targetTreeViewer.setSelection(new StructuredSelection(modifierNode), true);
		targetTreeViewer.editElement(modifierNode, 0);



		targetTreeViewer.refresh();
		//		OntologyTreeNode test = (OntologyTreeNode) OntologyEditorView.getOntologyTargetTree().getRootNode().getNextNode();

		TreeViewerColumn column = OntologyEditorView.getTargetTreeViewerColumn();
		column.getViewer().editElement(modifierNode, 0);
		//		subRootNode.setID(subRootNode.getName());
		//		subRootNode.setTreeAttributes();
		//		System.out.println(test.getName());		
		return null;
	}
}