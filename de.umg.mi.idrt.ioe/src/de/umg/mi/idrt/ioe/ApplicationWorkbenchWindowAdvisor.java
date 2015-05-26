package de.umg.mi.idrt.ioe;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void postWindowCreate(){

		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.getWindow().getShell().setMaximized( true );
	}
	public void preWindowOpen() {

		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		//configurer.setInitialSize(new Point(100, 100));
		//configurer.getWindow().getShell().setMaximized( true ); 
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(false);
		configurer.setShowPerspectiveBar(false);
		configurer.setShowProgressIndicator(false);
		//configurer.setShowPerspectiveBar(true);
		configurer.setTitle("IDRT Import and Mapping Tool V"
				+ Activator.getDefault().getBundle().getVersion());
		
		//TODO
		 configurer.setShowStatusLine(true);
	        configurer.setShowProgressIndicator(true);
//		IPreferenceStore apiStore = PlatformUI.getPreferenceStore();
//		apiStore.setValue(IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR,
//				"TOP_RIGHT");

	}


	@Override
	public boolean preWindowShellClose(){
		try {
			// save the full workspace before quit
			ResourcesPlugin.getWorkspace().save(true, null);
		} catch (final CoreException e) {
			// log exception, if required
		}
		return true;
	}

}
