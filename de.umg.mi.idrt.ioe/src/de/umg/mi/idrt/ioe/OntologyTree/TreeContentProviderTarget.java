package de.umg.mi.idrt.ioe.OntologyTree;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TreeContentProviderTarget implements ITreeContentProvider {

	private ViewTreeNode treeRoot = null;
	private OntologyTree _ot;

	@Override
	public Object[] getChildren(Object element) {
		return ((ViewTreeNode) element).getChildren().toArray();
	}

	@Override
	public Object getParent(Object element) {
		return ((ViewTreeNode) element).getParent();
	}

	@Override
	public boolean hasChildren(Object element) {
		return ((ViewTreeNode) element).hasChildren();
	}

	@Override
	public void dispose() {
		// unused
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// unused
	}

	@Override
	public Object[] getElements(Object inputElement) {

		System.out.println("----");
		System.out.println("getElemts (Target)");
		
		System.out.println("treeRoot set: "
				+ (treeRoot != null ? "true" : "false"));
		System.out.println("ot set: " + (_ot != null ? "true" : "false"));
		

		System.out.println("getELements already! (Target)");
		if (inputElement instanceof ViewTreeNode) {
			ViewTreeNode parentNode = ((ViewTreeNode) inputElement)
					.getParent();
			if (parentNode != null) {
				return getChildren(parentNode);
			} else {
				System.out
						.println("TreeContentProvider Error at getElements: has no parent");
			}
		} else {
			System.out
					.println("TreeContentProvider Error at getElements: inputElement is NOT instanceof ViewTableNode");
		}
		/*
		ViewTableNode returnNode = new ViewTableNode(null, new OTNode(
				"Source-Root-Node"), "Source-Root-Node");
		*/
		ViewTreeNode returnNode = new ViewTreeNode(null, (OntologyTreeNode)_ot.getRootNode().getFirstChild(),
				"Source");
		
		treeRoot = returnNode;

		convertNode(returnNode);
		
		return new Object[] { returnNode };
	}

	public ViewTreeNode getRoot() {
		return treeRoot;
	}

	public void setOT(OntologyTree ot) {
		this._ot = ot;
	}


	public void convertNode(ViewTreeNode parentNode) {

		OntologyTreeNode otParentNode = parentNode.getOTNode();

		if (otParentNode.getChildCount() > 0) {
			Iterator<OntologyTreeNode> it = otParentNode.getChildrenIterator();

			if (it == null) {
				System.out.println("Iterator is Null or something!");
				return;
			}

			while (it.hasNext()) {
				OntologyTreeNode tempOTNode = it.next();
				ViewTreeNode tmpNode = new ViewTreeNode(parentNode,
						tempOTNode, tempOTNode.getName());
				convertNode(tmpNode);
			}
		} else {
			// nothing, because there are no children
			//System.out.println("convertNode: has no children");
		}

	}
}