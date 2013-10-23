package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import de.umg.mi.idrt.ioe.I2B2ImportTool;
import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeModel;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeSubNode;
import de.umg.mi.idrt.ioe.view.EditorTargetInfoView;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class DeleteNodeCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		MyOntologyTree myOT = OntologyEditorView.getI2b2ImportTool().getMyOntologyTrees();
		TreeViewer targetTreeViewer = OntologyEditorView.getTargetTreeViewer();
		IStructuredSelection selection = (IStructuredSelection) targetTreeViewer
				.getSelection();

		if (selection.getFirstElement() instanceof OntologyTreeNode) {
			OntologyTreeNode firstElement = (OntologyTreeNode) selection
					.getFirstElement();
			OntologyEditorView.setNotYetSaved(true);
			if (!(firstElement ==myOT.getSubRootNode())) {

				firstElement.removeFromParent();
			}
			else {
				firstElement.removeFromParent();
				firstElement.getChildren().clear();
				myOT.getOntologyTreeTarget()
				.getNodeLists().add(firstElement);
				//			editorTargetView.setComposite(OTTarget);
				//TODO Ontology Editor
				//			firstElement.removeFromParent();
				for (OntologyTreeNode node : firstElement.getChildren()) {
					firstElement.remove(node);
				}
			}
		}
		else if (selection.getFirstElement() instanceof OntologyTreeSubNode) {
			OntologyTreeSubNode subNode = (OntologyTreeSubNode) selection.getFirstElement();
			System.err.println("NYI");
		}
		targetTreeViewer.refresh();
		return null;

	}

}