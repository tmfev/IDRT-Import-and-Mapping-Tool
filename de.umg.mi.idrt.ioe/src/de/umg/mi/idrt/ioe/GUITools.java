package de.umg.mi.idrt.ioe;
import javax.swing.ImageIcon;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wb.swt.ResourceManager;

import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTree;


/**
 * Provides basic GUI functions like creating buttons, etc
 * 
 * @author	Christian Bauer	
 * @version 0.9
 */
public class GUITools {
	

	
	/**
	 * Get an image by image name.
	 *
	 * @param image the filename of the image
	 * @return an Image
	 */
	public static Image getImage (String image){
		return ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", image);
	}
	
	
}