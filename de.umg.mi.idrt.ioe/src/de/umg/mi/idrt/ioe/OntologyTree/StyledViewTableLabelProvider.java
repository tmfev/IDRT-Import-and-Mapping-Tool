package de.umg.mi.idrt.ioe.OntologyTree;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

import de.umg.mi.idrt.ioe.GUITools;
import de.umg.mi.idrt.ioe.Resource;
/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 * @author Christian Bauer <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> 
 * 			Department of Medical Informatics Goettingen 
 * 			www.mi.med.uni-goettingen.de
 */
public class StyledViewTableLabelProvider extends StyledCellLabelProvider  {

	

	public StyledViewTableLabelProvider() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.StyledCellLabelProvider#update(org.eclipse.jface.viewers.ViewerCell)
	 */
	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		Color hiddenColor = SWTResourceManager.getColor(SWT.COLOR_GRAY);
		Color activeColor = SWTResourceManager.getColor(SWT.COLOR_BLACK);
		if (element instanceof OntologyTreeNode) {
			OntologyTreeNode otNode = ((OntologyTreeNode) element);

			//		Color color = SWTResourceManager.getColor(SWT.COLOR_BLUE);
			//		cell.setBackground(color);
			if (otNode.isHighlighted()) {
				Color color = SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
				cell.setBackground(color);	
			}
			else if (otNode.isSearchResult()) {
				Color color = SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION);
				cell.setBackground(color);	
			}
			else {
				//			Color color = SWTResourceManager.getColor(SWT.COLOR_BLUE);
				cell.setBackground(null);	
			}
			String visual = otNode.getTargetNodeAttributes().getVisualattribute();
			if (visual.toLowerCase().contains("h")) {

			}
//			if (otNode.getOntologyCellAttributes().getC_TOTALNUM()>0)
//			cell.setText(otNode.getName() + " - ["+otNode.getOntologyCellAttributes().getC_TOTALNUM()+"]");
//			else
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
					else if ("D".equals(visualAttribute))
						cell.setImage(GUITools
								.getImage(Resource.OntologyTree.VISIBILITY_ICON_DA));
					else if ("R".equals(visualAttribute))
						cell.setImage(GUITools
								.getImage(Resource.OntologyTree.VISIBILITY_ICON_RA));
					else
						cell.setImage(GUITools
								.getImage(Resource.OntologyTree.VISIBILITY_ICON_LA));
					String hidden =  visualAttributeFull.substring(1, 2);

					if (hidden.toLowerCase().equals("h"))
						cell.setForeground(hiddenColor);
					else
						cell.setForeground(activeColor);
				}
			}
		}

		else if (element instanceof OntologyTreeSubNode) {
			OntologyTreeSubNode subNode = (OntologyTreeSubNode)element;
			if (subNode.getStagingName()!=null)
				cell.setText("(" + (subNode.getStagingName()+")"));
			else {
//				System.out.println(subNode.getParent().getName());
				cell.setText("{no mapping found}");
			}
			cell.setImage(GUITools
					.getImage(Resource.OntologyTree.LINK_ICON));
			cell.setForeground(hiddenColor);
			if (subNode.isHighlighted()) {
				Color color = SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
				cell.setBackground(color);	
			}
			else {
				//			Color color = SWTResourceManager.getColor(SWT.COLOR_BLUE);
				cell.setBackground(null);	
			}
		}
		super.update(cell);
	}
}