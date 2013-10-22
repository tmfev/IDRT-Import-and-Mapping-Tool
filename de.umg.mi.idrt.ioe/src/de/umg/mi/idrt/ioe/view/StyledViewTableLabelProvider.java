package de.umg.mi.idrt.ioe.view;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
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
/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 * @author Christian Bauer <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> 
 * 			Department of Medical Informatics Goettingen 
 * 			www.mi.med.uni-goettingen.de
 */
class StyledViewTableLabelProvider extends StyledCellLabelProvider  {


	StyledViewTableLabelProvider() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.StyledCellLabelProvider#update(org.eclipse.jface.viewers.ViewerCell)
	 */
	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		OntologyTreeNode otNode = ((OntologyTreeNode) element);
		Color hiddenColor = SWTResourceManager.getColor(SWT.COLOR_GRAY);
		Color activeColor = SWTResourceManager.getColor(SWT.COLOR_BLACK);
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
		String visual = otNode.getTargetNodeAttributes().getVisualattribute();
		if (visual.toLowerCase().contains("h")) {

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
			//			System.out.println("visualAttributeFull: " + visualAttributeFull);
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
				String hidden =  visualAttributeFull.substring(1, 2);

				if (hidden.toLowerCase().equals("h"))
					cell.setForeground(hiddenColor);
				else
					cell.setForeground(activeColor);
			}
		}
		super.update(cell);
	}
}