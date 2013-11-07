package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;

import de.umg.mi.idrt.ioe.Resource.I2B2.NODE.TYPE;
import de.umg.mi.idrt.ioe.OntologyTree.Dimension;
import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTrees;
import de.umg.mi.idrt.ioe.OntologyTree.NodeType;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class AddFolderCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("Adding Node");
		OntologyTreeNode currentNode = OntologyEditorView.getCurrentTargetNode();
		
		OntologyTreeNode subRootNode = new OntologyTreeNode("New Node");

		subRootNode.setID("new Node");
		subRootNode.setTreePath("\\i2b2\\newNode\\");
		subRootNode.setTreePathLevel(1);
		subRootNode.setType(TYPE.ONTOLOGY_TARGET);
		subRootNode.getTargetNodeAttributes().addStagingPath("");
		subRootNode.getTargetNodeAttributes().setDimension(Dimension.CONCEPT_DIMENSION);
		subRootNode.getTargetNodeAttributes().setVisualattributes("FAE");
		subRootNode.setName("New Node");
		currentNode.add(subRootNode);
		OntologyEditorView.getOntologyTargetTree().getNodeLists().add(subRootNode);
		OntologyEditorView.setCurrentTargetNode(subRootNode);
		TreeViewer targetTreeViewer = OntologyEditorView.getTargetTreeViewer();
//		targetTreeViewer.expandToLevel(subRootNode, 10);
		targetTreeViewer.setSelection(new StructuredSelection(subRootNode), true);
		targetTreeViewer.editElement(subRootNode, 0);
		
	
		
		targetTreeViewer.refresh();
		OntologyTreeNode test = (OntologyTreeNode) OntologyEditorView.getOntologyTargetTree().getRootNode().getNextNode();

		TreeViewerColumn column = OntologyEditorView.getTargetTreeViewerColumn();
		column.getViewer().editElement(subRootNode, 0);
		
		System.out.println(test.getName());
		
		return null;
	}
}