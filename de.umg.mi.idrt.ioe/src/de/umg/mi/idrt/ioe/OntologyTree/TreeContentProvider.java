package de.umg.mi.idrt.ioe.OntologyTree;

import java.util.ArrayList;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TreeContentProvider implements ITreeContentProvider {

	private OntologyTreeNode _treeRoot = null;
	private OntologyTree _ot;

	@Override
	public Object[] getChildren(Object element) {
		return ((OntologyTreeNode) element).getChildrenArray();
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
	public void dispose() {
		// unused
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// unused
	}

	@Override
	public Object[] getElements(Object inputElement) {

		if (inputElement instanceof OntologyTreeNode) {
			OntologyTreeNode parentNode = ((OntologyTreeNode) inputElement)
					.getParent();
			if (parentNode != null) {
				System.out
						.println("is Instance of OntologyTreeNode so getChildren()");
				return getChildren(parentNode);
			} else {
				System.out
						.println("TreeContentProvider Error at getElements: has no parent");
			}
		} else if (inputElement instanceof ArrayList) {
			System.out.println("Content provider ");
			OntologyTreeNode node = new OntologyTreeNode("empty");
			OntologyTreeNode node2 = new OntologyTreeNode("empty2");
			// OntologyTreeNode[] nodes = new OntologyTreeNode[1]();
			// nodes[0] = node;
			ArrayList<OntologyTreeNode> list = new ArrayList<OntologyTreeNode>();
			list.add(node);
			// return (Object[])list;
		} else {
			System.out
					.println("TreeContentProvider Error at getElements: inputElement is NOT instanceof OntologyTreeNode "
							+ inputElement.getClass().getSimpleName());
		}

		if (((OntologyTreeNode) _ot.getRootNode()).getChildCount() > 0) {
			// return new Object[]{
			// ((OntologyTreeNode)_ot.getRootNode().getFirstChild()) };
			return new Object[] { ((OntologyTreeNode) _ot.getRootNode()
					.getChildAt(0)) };
		} else {
			System.out
					.println("Error in TreeContentProvider: RootNode does not have child nodes. (ChildCount: "+((OntologyTreeNode) _ot.getRootNode()).getChildCount()+")")	;
			System.out.println("RooteNod:" + _ot.getRootNode().getTreePath() + " // " + _ot.getRootNode().getName() );
			return null;
		}

	}

	public OntologyTreeNode getRoot() {
		return _treeRoot;
	}

	public void setOT(OntologyTree ot) {
		this._ot = ot;
		this._treeRoot = ot.getTreeRoot();
	}

	public void convertNode(OntologyTreeNode parentNode) {
		/*
		 * OTNode otParentNode = parentNode.getOTNode();
		 * 
		 * if (otParentNode.getChildCount() > 0) { Iterator<OTNode> it =
		 * otParentNode.getChildren();
		 * 
		 * if (it == null) {
		 * System.out.println("Iterator is Null or something!"); return; }
		 * 
		 * while (it.hasNext()) { OTNode tempOTNode = it.next(); OTNode tmpNode
		 * = new OTNode(parentNode, tempOTNode, tempOTNode.getName());
		 * System.out.println("addNode: " +
		 * tempOTNode.getOntologyCellAttributes().getC_FULLNAME());
		 * this._viewTree
		 * .addOTNode(tempOTNode.getOntologyCellAttributes().getC_FULLNAME(),
		 * tmpNode); convertNode(tmpNode); } } else { // nothing, because there
		 * are no children //System.out.println("convertNode: has no children");
		 * }
		 */
	}

}