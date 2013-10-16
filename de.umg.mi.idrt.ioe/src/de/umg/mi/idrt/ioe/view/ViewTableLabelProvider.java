package de.umg.mi.idrt.ioe.view;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import de.umg.mi.idrt.ioe.GUITools;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;

class ViewTableLabelProvider extends LabelProvider {

	private final TreeViewer viewer;

	ViewTableLabelProvider(TreeViewer viewer) {

		this.viewer = viewer;
	}

	@Override
	public String getText(Object element) {
		OntologyTreeNode node = (OntologyTreeNode) element;
		return node.getName();
	}

	@Override
	public Image getImage(Object element) {

		OntologyTreeNode otNode = ((OntologyTreeNode) element);

		if (otNode == null) {
			return null;
		} else if ( Resource.I2B2.NODE.TYPE.ONTOLOGY_SOURCE.equals( otNode.getType() ) || 
				Resource.I2B2.NODE.TYPE.ONTOLOGY_TARGET.equals( otNode.getType() ) ) {

			String visualAttributeFull = "";

			if ( Resource.I2B2.NODE.TYPE.ONTOLOGY_SOURCE.equals( otNode.getType() ) && otNode.getOntologyCellAttributes() != null) {
				visualAttributeFull = otNode.getOntologyCellAttributes()
						.getC_VISUALATTRIBUTES();
			} else if ( Resource.I2B2.NODE.TYPE.ONTOLOGY_TARGET.equals( otNode.getType() ) && otNode.getTargetNodeAttributes() != null ) {
				visualAttributeFull = otNode.getTargetNodeAttributes()
						.getVisualattribute();
				
			}
			
			if ( visualAttributeFull != null && !visualAttributeFull.isEmpty()) {

				String visualAttribute = visualAttributeFull.substring(0, 1);

				if ("F".equals(visualAttribute))
					return GUITools
							.getImage(Resource.OntologyTree.VISIBILITY_ICON_FA);// GUITools.createImage(Resource.OntologyTree.ICON_ANSWERGROUP);
				else if ("C".equals(visualAttribute))
					return GUITools
							.getImage(Resource.OntologyTree.VISIBILITY_ICON_CA);// GUITools.createImage(Resource.OntologyTree.ICON_ANSWERGROUP);
				else if ("M".equals(visualAttribute))
					return GUITools
							.getImage(Resource.OntologyTree.VISIBILITY_ICON_MA);// GUITools.createImage(Resource.OntologyTree.ICON_ANSWERGROUP);
				else if ("L".equals(visualAttribute))
					return GUITools
							.getImage(Resource.OntologyTree.VISIBILITY_ICON_LA);// GUITools.createImage(Resource.OntologyTree.ICON_ANSWERGROUP);

				System.out.println("nothing?!?");

			}
			// System.out.println("null!");
			return GUITools
					.getImage(Resource.OntologyTree.ITEMSTATUS_ICON_UNCHECKED);// GUITools.createImage(Resource.OntologyTree.ICON_ANSWERGROUP);
		}

		if (element instanceof OntologyTreeNode) {
			return GUITools.getImage(Resource.OntologyTree.VISIBILITY_ICON_FA);// GUITools.createImage(Resource.OntologyTree.ICON_ANSWERGROUP);
		}
		return PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_OBJ_FILE);
	}
	
}