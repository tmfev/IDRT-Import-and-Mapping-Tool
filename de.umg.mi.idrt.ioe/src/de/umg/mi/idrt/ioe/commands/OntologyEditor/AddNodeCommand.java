package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;

import de.umg.mi.idrt.ioe.Resource.I2B2.NODE.TYPE;
import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.NodeType;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class AddNodeCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("Adding Node");
		OntologyTreeNode currentNode = OntologyEditorView.getCurrentTargetNode();
		
		OntologyTreeNode subRootNode = new OntologyTreeNode("New Node");

		subRootNode.setID("new Node");
		subRootNode.setTreePath("\\i2b2\\newNode\\");
		subRootNode.setTreePathLevel(1);
		subRootNode.setType(TYPE.ONTOLOGY_TARGET);
		subRootNode.setNodeType(NodeType.ITEMGROUP);
		subRootNode.getTargetNodeAttributes().addStagingPath("");
		subRootNode.getTargetNodeAttributes().setChanged(true);
		subRootNode.getTargetNodeAttributes().setVisualattributes("FAE");
		subRootNode.setName("New Node");
		currentNode.add(subRootNode);
		MyOntologyTree myOT = OntologyEditorView.getI2b2ImportTool().getMyOntologyTrees();
		myOT.getOntologyTreeTarget().getNodeLists().add(subRootNode);
		OntologyEditorView.setCurrentTargetNode(subRootNode);
		TreeViewer targetTreeViewer = OntologyEditorView.getTargetTreeViewer();
//		targetTreeViewer.expandToLevel(subRootNode, 10);
		targetTreeViewer.setSelection(new StructuredSelection(subRootNode), true);
		targetTreeViewer.editElement(subRootNode, 0);
		
	
		
		targetTreeViewer.refresh();
		OntologyTreeNode test = (OntologyTreeNode) myOT.getTargetTreeRoot().getNextNode();

		TreeViewerColumn column = OntologyEditorView.getTargetTreeViewerColumn();
		column.getViewer().editElement(subRootNode, 0);
		
		System.out.println(test.getName());
		
		return null;
	}
}