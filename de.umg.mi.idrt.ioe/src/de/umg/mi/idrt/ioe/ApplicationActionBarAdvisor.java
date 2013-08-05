package de.umg.mi.idrt.ioe;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

	protected void makeActions(IWorkbenchWindow window) {
    
    }

    
}
