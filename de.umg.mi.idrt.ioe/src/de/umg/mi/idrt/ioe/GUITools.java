package de.umg.mi.idrt.ioe;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Device;
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
	 * Creates an image icon.
	 *
	 * @param image the name of the image file
	 * @return the new image icon
	 */	
	public static ImageIcon createImageIcon(String image) {
		
		
		
		java.net.URL imgURL = MyOntologyTree.class.getResource(//"images/" +
				image);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + image);
			return null;
		}
		
	}
	
	public static Image createImage(String imageName) {
		
		ImageDescriptor imageDesc = AbstractUIPlugin.imageDescriptorFromPlugin(
				"de.umg.mi.idrt.ioe",
				imageName);
		
		
		
		Image image = null;
		
		if (imageDesc != null){
			image = imageDesc.createImage();
		}
		
				

		if (image != null) {
			return image;
		} else {
			System.err.println("Couldn't find file: " + image + " (" + imageName + ")");
			return null;
		}
		
	}
	
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