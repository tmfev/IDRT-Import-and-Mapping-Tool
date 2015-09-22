package de.umg.mi.idrt.imt.transmart;


import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.umg.mi.idrt.ioe.misc.ProjectEmptyException;

public class TransmartConfigTreeContentProvider implements ITreeContentProvider {

	private TransmartConfigTreeModelProvider model;
	public TransmartConfigTreeContentProvider() {
		model = new TransmartConfigTreeModelProvider();
	}

	@Override
	public void dispose() {
		// unused
	}

	@Override
	public Object[] getChildren(Object element) {
		if (element instanceof TransmartConfigTreeItem) {
			return ((TransmartConfigTreeItem)element).getChildren().toArray();
		}
		else
			return null;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		try {
			if (model.getModel() != null)
				return model.getModel().toArray();
			else
			{
				throw new ProjectEmptyException();
			}
		}catch (ProjectEmptyException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	@Override
	public Object getParent(Object element) {
		return ((TransmartConfigTreeItem) element).getParent();
	}

	@Override
	public boolean hasChildren(Object element) {
		return ((TransmartConfigTreeItem) element).getChildCount() > 0;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// unused
	}
}