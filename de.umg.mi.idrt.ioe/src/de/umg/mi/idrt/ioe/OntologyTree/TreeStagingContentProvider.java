package de.umg.mi.idrt.ioe.OntologyTree;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.umg.mi.idrt.ioe.view.OTtoTreeContentProvider;

public class TreeStagingContentProvider implements ITreeContentProvider {

	private OntologyTreeNode _treeRoot = null;
	private OntologyTree _ot;
	private OTtoTreeContentProvider model;
	public TreeStagingContentProvider() {
		// TODO Auto-generated constructor stub
		model = new OTtoTreeContentProvider();
	}

	@Override
	public Object[] getChildren(Object element) {
		if (element instanceof OntologyTreeNode) {
//			System.out.println("TREENODE");
			OntologyTreeNode node = (OntologyTreeNode)element;
//			System.out.println(node.getName());
			return ((OntologyTreeNode)element).getChildren().toArray();
		}
		else
			return null;
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
		return model.getStagingModel().toArray();
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

	public void setOT(OntologyTree ot) {
		this._ot = ot;
		this._treeRoot = ot.getTreeRoot();
	}

}