package de.umg.mi.idrt.ioe;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
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

	public void preWindowOpen() {
		Debug.f("preWindowOpen",this);

		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		//configurer.setInitialSize(new Point(100, 100));
		//configurer.getWindow().getShell().setMaximized( true ); 
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(false);
		configurer.setShowPerspectiveBar(false);
		configurer.setShowProgressIndicator(true);
		//configurer.setShowPerspectiveBar(true);

		IPreferenceStore apiStore = PlatformUI.getPreferenceStore();
		apiStore.setValue(IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR,
				"TOP_RIGHT");

	}
	public void postWindowCreate(){
		Debug.f("postWindowCreate",this);

		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.getWindow().getShell().setMaximized( true );

		// close views from previous session 
		IViewReference[] views = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();//.showView("edu.goettingen.i2b2.importtool.view.OntologyView");
		for (int x = 0; x < views.length; x++){
			IViewReference view = views[x];
			if (!view.getId().equals(Resource.ID.View.STATUS_VIEW) 
					&& !view.getId().equals(Resource.ID.View.SERVER_VIEW))
			{
				Debug.d("closed View from previous session (ViewID:"+view.getId()+")");
				//TODO HIDE VIEWS
//				view.getPage().hideView(view);
			}
		}

		Application.getStatusView().addMessage("RCP initialized.");
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
