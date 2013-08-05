package de.umg.mi.idrt.ioe.OntologyTree;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;

class ViewTableLabelProvider extends LabelProvider {

	private final TreeViewer viewer;

	ViewTableLabelProvider (TreeViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public String getText(Object element) {
		ViewTreeNode node = (ViewTreeNode) element;
		return node.getName();
	}
}