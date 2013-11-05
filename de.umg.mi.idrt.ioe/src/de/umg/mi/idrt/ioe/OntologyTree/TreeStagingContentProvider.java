package de.umg.mi.idrt.ioe.OntologyTree;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TreeStagingContentProvider implements ITreeContentProvider {

	private OntologyTreeContentProvider model;
	public TreeStagingContentProvider() {
		model = new OntologyTreeContentProvider();
	}

	@Override
	public void dispose() {
		// unused
	}

	@Override
	public Object[] getChildren(Object element) {
		if (element instanceof OntologyTreeNode) {
			return ((OntologyTreeNode)element).getChildren().toArray();
		}
		else
			return null;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return model.getStagingModel().toArray();
	}

	@Override
	public Object getParent(Object element) {
		return ((OntologyTreeNode) element).getParent();
	}

	@Override
	public boolean hasChildren(Object element) {
		return ((OntologyTreeNode) element).getChildCount() > 0;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// unused
	}
}