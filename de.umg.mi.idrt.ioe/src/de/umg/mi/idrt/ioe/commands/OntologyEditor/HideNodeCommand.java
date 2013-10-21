package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.view.EditorTargetInfoView;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class HideNodeCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		TreeViewer targetTreeViewer = OntologyEditorView.getTargetTreeViewer();
		IStructuredSelection selection = (IStructuredSelection) targetTreeViewer
				.getSelection();
		OntologyTreeNode firstElement = (OntologyTreeNode) selection
				.getFirstElement();
		OntologyEditorView.setNotYetSaved(true);
		String visual = firstElement.getTargetNodeAttributes().getVisualattribute();

		if (visual.equalsIgnoreCase("LAE"))
			firstElement.getTargetNodeAttributes().setVisualattributes("LHE");
		else if (visual.equalsIgnoreCase("FAE"))
			firstElement.getTargetNodeAttributes().setVisualattributes("FHE");
		else if (visual.equalsIgnoreCase("FHE"))
			firstElement.getTargetNodeAttributes().setVisualattributes("FAE");
		else if (visual.equalsIgnoreCase("LHE"))
			firstElement.getTargetNodeAttributes().setVisualattributes("LAE");
		targetTreeViewer.refresh();
		EditorTargetInfoView.refresh();
		return null;
	}

}