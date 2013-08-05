package de.umg.mi.idrt.ioe;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "edu.goettingen.i2b2.importtool.view.MainPerspective"; //$NON-NLS-1$

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}

	public void createWindowContents(IWorkbenchWindowConfigurer configurer,
			Shell shell) {
		
		// super.createWindowContents(configurer, shell);
		shell.setMaximized(true);
	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);
		configurer.setSaveAndRestore(true);
	}

	public boolean preWindowShellClose(IWorkbenchWindowConfigurer configurer) {
		// Ensure that the editor area is clear before closing, otherwise there
		// can be
		// "ghost" editor areas upon re-opening the workbench.
		IWorkbenchPage page = configurer.getWindow().getActivePage();
		page.close();
		return true;
	}
	 @Override
	    public void preStartup() {
//	    	 P2Util.checkForUpdates();
	    }
	public void postShutdown() {
		// TODO lazy exit
		System.exit(0);
	}

}
