package de.umg.mi.idrt.ioe.view;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.SWTResourceManager;

import de.umg.mi.idrt.ioe.GUITools;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;

class StyledViewTableLabelProvider extends StyledCellLabelProvider  {

	private final TreeViewer viewer;

	StyledViewTableLabelProvider(TreeViewer viewer) {

		this.viewer = viewer;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.StyledCellLabelProvider#update(org.eclipse.jface.viewers.ViewerCell)
	 */
	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		OntologyTreeNode otNode = ((OntologyTreeNode) element);
//		Color color = SWTResourceManager.getColor(SWT.COLOR_BLUE);
//		cell.setBackground(color);
		if (otNode.isHighlighted()) {
			Color color = SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
			cell.setBackground(color);	
		}
		else {
//			Color color = SWTResourceManager.getColor(SWT.COLOR_BLUE);
			cell.setBackground(null);	
		}
		cell.setText(otNode.getName());

		if ( Resource.I2B2.NODE.TYPE.ONTOLOGY_SOURCE.equals( otNode.getType() ) || 
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
					cell.setImage(GUITools
							.getImage(Resource.OntologyTree.VISIBILITY_ICON_FA));// GUITools.createImage(Resource.OntologyTree.ICON_ANSWERGROUP);
				else if ("C".equals(visualAttribute))
					cell.setImage(GUITools
							.getImage(Resource.OntologyTree.VISIBILITY_ICON_CA));// GUITools.createImage(Resource.OntologyTree.ICON_ANSWERGROUP);
				else if ("M".equals(visualAttribute))
					cell.setImage(GUITools
							.getImage(Resource.OntologyTree.VISIBILITY_ICON_MA));// GUITools.createImage(Resource.OntologyTree.ICON_ANSWERGROUP);
				else if ("L".equals(visualAttribute))
					cell.setImage(GUITools
							.getImage(Resource.OntologyTree.VISIBILITY_ICON_LA));// GUITools.createImage(Resource.OntologyTree.ICON_ANSWERGROUP);


			}
		}
		super.update(cell);
	}

	public String getText(Object element) {
		OntologyTreeNode node = (OntologyTreeNode) element;
		return node.getName();
	}

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

//				System.out.println("nothing?!?");

			}
			// System.out.println("null!");
			return GUITools
					.getImage(Resource.OntologyTree.ITEMSTATUS_ICON_UNCHECKED);// GUITools.createImage(Resource.OntologyTree.ICON_ANSWERGROUP);
		}

		if (element instanceof OntologyTreeNode) {

			/*
			 * return
			 * PlatformUI.getWorkbench().getSharedImages()//.getImage(Resource
			 * .OntologyTree.ICON_ANSWERGROUP);
			 * .getImage(ISharedImages.IMG_OBJ_FOLDER);
			 */

			return GUITools.getImage(Resource.OntologyTree.VISIBILITY_ICON_FA);// GUITools.createImage(Resource.OntologyTree.ICON_ANSWERGROUP);

		}
		return PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_OBJ_FILE);
	}

}