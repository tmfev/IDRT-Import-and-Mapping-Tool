package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import de.umg.mi.idrt.ioe.GUITools;
import de.umg.mi.idrt.ioe.Resource;
/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 * @author Christian Bauer <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> 
 * 			Department of Medical Informatics Goettingen 
 * 			www.mi.med.uni-goettingen.de
 */
public class TransmartStyledViewTableLabelProvider extends StyledCellLabelProvider  {

	

	public TransmartStyledViewTableLabelProvider() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.StyledCellLabelProvider#update(org.eclipse.jface.viewers.ViewerCell)
	 */
	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		Color hiddenColor = SWTResourceManager.getColor(SWT.COLOR_GRAY);
		Color activeColor = SWTResourceManager.getColor(SWT.COLOR_BLACK);
		if (element instanceof TransmartOntologyTreeItem) {
			TransmartOntologyTreeItem otNode = ((TransmartOntologyTreeItem) element);

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
				cell.setBackground(null);	
			}
				cell.setText(otNode.getName());

				String visualAttributeFull = otNode.getOntologyCellAttributes().getC_VISUALATTRIBUTES();

				//			System.out.println("visualAttributeFull: " + visualAttributeFull);
				if ( visualAttributeFull != null && !visualAttributeFull.isEmpty()) {

					String visualAttribute = visualAttributeFull.substring(0, 1);

					if ("f".equals(visualAttribute.toLowerCase()))
						cell.setImage(GUITools
								.getImage(Resource.OntologyTree.VISIBILITY_ICON_FA));// GUITools.createImage(Resource.OntologyTree.ICON_ANSWERGROUP);
					else if ("m".equals(visualAttribute.toLowerCase()))
						cell.setImage(GUITools
								.getImage(Resource.OntologyTree.VISIBILITY_ICON_CA));// GUITools.createImage(Resource.OntologyTree.ICON_ANSWERGROUP);
					else if ("m".equals(visualAttribute.toLowerCase()))
						cell.setImage(GUITools
								.getImage(Resource.OntologyTree.VISIBILITY_ICON_MA));// GUITools.createImage(Resource.OntologyTree.ICON_ANSWERGROUP);
					else if ("l".equals(visualAttribute.toLowerCase()))
						cell.setImage(GUITools
								.getImage(Resource.OntologyTree.VISIBILITY_ICON_LA));// GUITools.createImage(Resource.OntologyTree.ICON_ANSWERGROUP);
					else if ("d".equals(visualAttribute.toLowerCase()))
						cell.setImage(GUITools
								.getImage(Resource.OntologyTree.VISIBILITY_ICON_DA));
					else if ("r".equals(visualAttribute.toLowerCase()))
						cell.setImage(GUITools
								.getImage(Resource.OntologyTree.VISIBILITY_ICON_RA));
					//TODO replace image
					//ResourceManager.getPluginImage("de.umg.mi.idrt.importtool", "images/searchtool16.png")
					else if ("c".equals(visualAttribute.toLowerCase()))
						cell.setImage(GUITools
								.getImage(Resource.OntologyTree.VISIBILITY_ICON_FA));
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

		super.update(cell);
	}
}