package de.umg.mi.idrt.imt.transmart;


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
public class TransmartConfigStyledLabelProvider extends StyledCellLabelProvider  {

	private Color hiddenColor;
	private Color activeColor;
	private Color nodeHighlightedColor;	
	private Color nodeSearchResultColor;
	
	public TransmartConfigStyledLabelProvider() {
		hiddenColor = SWTResourceManager.getColor(SWT.COLOR_GRAY);
		activeColor = SWTResourceManager.getColor(SWT.COLOR_BLACK);
		nodeHighlightedColor = SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
		nodeSearchResultColor = SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION);
	}

	@Override
	public void update(ViewerCell cell) {
		
		
		Object element = cell.getElement();
		
		if (element instanceof TransmartConfigTreeItem) {
			TransmartConfigTreeItem otNode = ((TransmartConfigTreeItem) element);

			//		Color color = SWTResourceManager.getColor(SWT.COLOR_BLUE);
			//		cell.setBackground(color);
				String visualAttribute = otNode.getVisualAttribute();
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
					
					cell.setText(((TransmartConfigTreeItem) element).getName());

			}

		super.update(cell);
	}
}