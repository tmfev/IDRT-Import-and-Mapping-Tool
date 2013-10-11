package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.OntologyTree.TargetNodeAttributes;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class AddNodeCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("Adding Node");

		TreeViewer targetTreeViewer = OntologyEditorView.getTargetTreeViewer();
		IStructuredSelection selection = (IStructuredSelection) targetTreeViewer
				.getSelection();
		OntologyTreeNode firstElement = (OntologyTreeNode) selection
				.getFirstElement();
		if (firstElement==null) {
			System.out.println("firstElementnull null!");
		}
		MyOntologyTree myOT = OntologyEditorView.getI2b2ImportTool().getMyOntologyTrees();
		OntologyTreeNode targetNode = myOT.getOntologyTreeTarget()
				.getNodeLists().getNodeByPath(firstElement.getTreePath());

		TargetNodeAttributes attributes = targetNode.getTargetNodeAttributes();
		String visual = attributes.getVisualattribute();

			OntologyEditorView.setNotYetSaved(true);
			if (targetNode != null && !targetNode.getParent().isRoot()) {
				OntologyTreeNode node = new OntologyTreeNode("new Folder");
				node.setName("NAME");
				node.setID(targetNode.getID());
				node.setIsVisable(true);
				node.setTreePath(targetNode.getTreePath());
				myOT.getOntologyTreeTarget().getNodeLists().add(node);
				myOT.getOntologyTreeTarget().getNodeLists().addOTNode("\\path", node);
			}
		targetTreeViewer.refresh();
		return null;
	}
}