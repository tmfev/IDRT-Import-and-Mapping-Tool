package de.umg.mi.idrt.ioe;

import org.eclipse.equinox.p2.examples.cloud.p2.CloudPolicy;
import org.eclipse.equinox.p2.ui.Policy;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.umg.mi.idrt.ioe"; //$NON-NLS-1$
    public static CloudPolicy policy;
    ServiceRegistration policyRegistration;
	IPropertyChangeListener preferenceListener;
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
		registerP2Policy(context);
		getPreferenceStore().addPropertyChangeListener(getPreferenceListener());
	}
	private IPropertyChangeListener getPreferenceListener() {
		if (preferenceListener == null) {
			preferenceListener = new IPropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent event) {
					policy.updateForPreferences();
				}
			};
		}
		return preferenceListener;
	}
	private void registerP2Policy(BundleContext context) {
		policy = new CloudPolicy();
		policy.updateForPreferences();
		policyRegistration = context.registerService(Policy.class.getName(), policy, null);
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
		policyRegistration.unregister();
		policyRegistration = null;
		getPreferenceStore().removePropertyChangeListener(preferenceListener);
		preferenceListener = null;
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
