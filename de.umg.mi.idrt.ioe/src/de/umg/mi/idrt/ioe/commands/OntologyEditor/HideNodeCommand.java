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
		System.out.println(firstElement.getName() + " " +visual);
		for (OntologyTreeNode child : firstElement.getChildren())
			changeVisual(child,visual);

		//		String visual = firstElement.getTargetNodeAttributes().getVisualattribute();
		if (visual.equals("hidden")) {
			if (firstElement.getTargetNodeAttributes().getVisualattribute().equalsIgnoreCase("LAE")||firstElement.getTargetNodeAttributes().getVisualattribute().equalsIgnoreCase("LA"))
				firstElement.getTargetNodeAttributes().setVisualattributes("LHE");
			else if (firstElement.getTargetNodeAttributes().getVisualattribute().equalsIgnoreCase("FAE")||firstElement.getTargetNodeAttributes().getVisualattribute().equalsIgnoreCase("FA"))
				firstElement.getTargetNodeAttributes().setVisualattributes("FHE");

		}
		else if (visual.equals("active")) {
			if (firstElement.getTargetNodeAttributes().getVisualattribute().equalsIgnoreCase("FHE")||firstElement.getTargetNodeAttributes().getVisualattribute().equalsIgnoreCase("FH"))
				firstElement.getTargetNodeAttributes().setVisualattributes("FAE");
			else if (firstElement.getTargetNodeAttributes().getVisualattribute().equalsIgnoreCase("LHE")||firstElement.getTargetNodeAttributes().getVisualattribute().equalsIgnoreCase("LH"))
				firstElement.getTargetNodeAttributes().setVisualattributes("LAE");
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

		String visual = firstElement.getTargetNodeAttributes().getVisualattribute();
		if (visual.equalsIgnoreCase("LAE")||visual.equalsIgnoreCase("LA"))
			firstElement.getTargetNodeAttributes().setVisualattributes("LHE");
		else if (visual.equalsIgnoreCase("FAE")||visual.equalsIgnoreCase("FA"))
			firstElement.getTargetNodeAttributes().setVisualattributes("FHE");
		else if (visual.equalsIgnoreCase("FHE")||visual.equalsIgnoreCase("FH"))
			firstElement.getTargetNodeAttributes().setVisualattributes("FAE");
		else if (visual.equalsIgnoreCase("LHE")||visual.equalsIgnoreCase("LH"))
			firstElement.getTargetNodeAttributes().setVisualattributes("LAE");

		String newVisual;
		if (firstElement.getTargetNodeAttributes().getVisualattribute().toLowerCase().contains("a"))
			newVisual = "active";
		else
			newVisual = "hidden";

System.out.println(newVisual);
		changeVisual(firstElement, newVisual);


		targetTreeViewer.refresh();
		EditorTargetInfoView.refresh();
		return null;
	}

}