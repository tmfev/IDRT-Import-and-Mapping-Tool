package de.umg.mi.idrt.ioe;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.umg.mi.idrt.ioe"; //$NON-NLS-1$
    
	
	// The shared instance
	private static Activator plugin;

	private Resource _resource;
	
//	private ILogListener listener;	
	
	/**
	 * The constructor
	 */
	public Activator() {
		// create new resource for this program
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		// adding local log file listener
		
//		listener = new LogListener();
//		Platform.addLogListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		
		// removing local log file listener
//		Platform.removeLogListener(listener);
//	    listener = null;
		
		plugin = null;
		super.stop(context);
		
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	public void createResource(){
		this.setResource(new Resource());
	}
	
	public void setResource(Resource resource){
		this._resource = resource;
	}
	
	public Resource getResource(){
		return this._resource;
	}
	
}
