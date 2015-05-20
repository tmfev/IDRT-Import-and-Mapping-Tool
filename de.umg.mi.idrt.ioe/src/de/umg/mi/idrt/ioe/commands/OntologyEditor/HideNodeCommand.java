package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.view.EditorTargetInfoView;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class HideNodeCommand extends AbstractHandler {

	/**
	 * @param firstElement
	 */
	private void changeVisual(OntologyTreeNode firstElement, String visual) {
		for (OntologyTreeNode child : firstElement.getChildren())
			changeVisual(child,visual);

		if (visual.equals("hidden")) {
			firstElement.getTargetNodeAttributes().setVisualattributes(firstElement.getTargetNodeAttributes().getVisualattribute().replace("h", "a"));
			firstElement.getTargetNodeAttributes().setVisualattributes(firstElement.getTargetNodeAttributes().getVisualattribute().replace("H", "A"));
		}
		else if (visual.equals("active")) {
			firstElement.getTargetNodeAttributes().setVisualattributes(firstElement.getTargetNodeAttributes().getVisualattribute().replace("a", "h"));
			firstElement.getTargetNodeAttributes().setVisualattributes(firstElement.getTargetNodeAttributes().getVisualattribute().replace("A", "H"));
		}
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		TreeViewer targetTreeViewer = OntologyEditorView.getTargetTreeViewer();
		IStructuredSelection selection = (IStructuredSelection) targetTreeViewer
				.getSelection();
		OntologyTreeNode firstElement = (OntologyTreeNode) selection
				.getFirstElement();
		OntologyEditorView.setNotYetSaved(true);

		String newVisual;
		if (firstElement.getTargetNodeAttributes().getVisualattribute().toLowerCase().contains("a"))
			newVisual = "active";
		else
			newVisual = "hidden";

		changeVisual(firstElement, newVisual);


		targetTreeViewer.refresh();
		EditorTargetInfoView.refresh();
		return null;
	}

}