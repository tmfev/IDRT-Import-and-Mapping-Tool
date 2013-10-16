package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.OntologyTree.TargetNodeAttributes;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class DeleteNodeCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("Deleting Node");

		TreeViewer targetTreeViewer = OntologyEditorView.getTargetTreeViewer();
		IStructuredSelection selection = (IStructuredSelection) targetTreeViewer
				.getSelection();
		OntologyTreeNode firstElement = (OntologyTreeNode) selection
				.getFirstElement();
		MyOntologyTree myOT = OntologyEditorView.getI2b2ImportTool().getMyOntologyTrees();

		OntologyEditorView.setNotYetSaved(true);

		if (firstElement != null && !firstElement.getParent().isRoot()) {

			Iterator<OntologyTreeNode> it = firstElement.getChildrenIterator();
			while(it.hasNext()) {
				OntologyTreeNode tn = it.next();
				System.out.println("REMOVUING: " + tn.getName());
				myOT.getOntologyTreeTarget()
				.getNodeLists().removeNode(tn);
				tn.removeFromParent();
			}
			firstElement.removeFromParent();
			myOT.getOntologyTreeTarget()
			.getNodeLists().removeNode(firstElement);
		}
		else {
			firstElement.removeAllChildren();
			System.err.println("Cannot remove root node.\n Removing all children instead.");
		}
		targetTreeViewer.refresh();
		return null;
	}
}