package de.umg.mi.idrt.ioe.OntologyTree;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class TreeTargetContentProvider implements ITreeContentProvider {

	private OntologyTreeNode _treeRoot = null;
	private OntologyTree _ot;
	private OntologyTreeContentProvider model;
	public TreeTargetContentProvider() {
		// TODO Auto-generated constructor stub
		model = new OntologyTreeContentProvider();
	}

	@Override
	public Object[] getChildren(Object element) {
		if (element instanceof OntologyTreeNode) {
			//			System.out.println("TREENODE");
			OntologyTreeNode node = (OntologyTreeNode)element;
			if (node.getTargetNodeAttributes().getVisualattribute().toLowerCase().startsWith("l")) {
				return node.getTargetNodeAttributes().getSubNodeList().toArray();
			}
			else
				return ((OntologyTreeNode)element).getChildren().toArray();
		}
		else
			return null;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof OntologyTreeNode)
		return ((OntologyTreeNode) element).getParent();
		else if (element instanceof OntologyTreeSubNode)
			return ((OntologyTreeSubNode)element).getParent();
		else
			return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof OntologyTreeNode)
		{
			return ((OntologyEditorView.isShowSubNodes()||!((OntologyTreeNode) element).isLeaf())&&(((OntologyTreeNode) element).getChildCount() > 0 || ((OntologyTreeNode) element).getTargetNodeAttributes().getSubNodeList().size()>0));
		}
			else
				return false;
	}

	@Override
	public void dispose() {
		// unused
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// unused
		System.out.println("changed");
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return model.getTargetModel().toArray();
	}
	//		if (inputElement instanceof ArrayList) {
	//			//			System.out.println("Content provider ");
	//		} else {
	//			System.out
	//			.println("TreeContentProvider Error at getElements: inputElement is NOT instanceof OntologyTreeNode "
	//					+ inputElement.getClass().getSimpleName());
	//		}

	//		if (((OntologyTreeNode) _ot.getRootNode()).getChildCount() > 0) {
	//			System.out.println("IF?");
	//			// return new Object[]{
	//			// ((OntologyTreeNode)_ot.getRootNode().getFirstChild()) };
	//			return new Object[] { ((OntologyTreeNode) _ot.getRootNode()
	//					.getChildAt(0)) };
	//		} else {
	//			System.out
	//			.println("Error in TreeContentProvider: RootNode does not have child nodes. (ChildCount: "+((OntologyTreeNode) _ot.getRootNode()).getChildCount()+")")	;
	//			System.out.println("RooteNod:" + _ot.getRootNode().getTreePath() + " // " + _ot.getRootNode().getName() );
	//			return null;
	//		}
	//
	//	}

	public OntologyTreeNode getRoot() {
		return _treeRoot;
	}

	public void setOntologyTree(OntologyTree ot) {
		this._ot = ot;
		this._treeRoot = ot.getTreeRoot();
	}

}