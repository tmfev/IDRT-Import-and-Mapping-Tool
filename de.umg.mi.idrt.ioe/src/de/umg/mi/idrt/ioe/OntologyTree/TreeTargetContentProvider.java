package de.umg.mi.idrt.ioe.OntologyTree;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class TreeTargetContentProvider implements ITreeContentProvider {

	private OntologyTreeContentProvider model;

	public TreeTargetContentProvider() {
		model = new OntologyTreeContentProvider();
	}

	@Override
	public void dispose() {
		// unused
	}

	@Override
	public Object[] getChildren(Object element) {
		if (element instanceof OntologyTreeNode) {
			// System.out.println("TREENODE");
			OntologyTreeNode node = (OntologyTreeNode) element;
			if (node.getTargetNodeAttributes().getVisualattribute().toLowerCase().startsWith("l")) {
				return node.getTargetNodeAttributes().getSubNodeList().toArray();
			} else
				return ((OntologyTreeNode) element).getChildren().toArray();
		} else
			return null;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return model.getTargetModel().toArray();
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof OntologyTreeNode)
			return ((OntologyTreeNode) element).getParent();
		else if (element instanceof OntologyTreeSubNode)
			return ((OntologyTreeSubNode) element).getParent();
		else
			return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof OntologyTreeNode) {
			return ((OntologyEditorView.isShowSubNodes() || !((OntologyTreeNode) element).isLeaf()) 
					&& (((OntologyTreeNode) element).getChildCount() > 0 || 
							((OntologyTreeNode) element).getTargetNodeAttributes().getSubNodeList().size() > 0));
		} else
			return false;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// unused
		System.out.println("changed");
	}

}