package de.umg.mi.idrt.ioe;


import de.umg.mi.idrt.ioe.OntologyTree.ServerPreferences;
import de.umg.mi.idrt.ioe.view.EditorSourceInfoView;
import de.umg.mi.idrt.ioe.view.EditorSourceView;
import de.umg.mi.idrt.ioe.view.EditorTargetInfoView;
import de.umg.mi.idrt.ioe.view.EditorTargetView;
import de.umg.mi.idrt.ioe.view.MainView;
import de.umg.mi.idrt.ioe.view.OntologyView;
import de.umg.mi.idrt.ioe.view.StatusView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;


/**
 * This class controls all aspects  of the application's execution
 * 
 * @author Christian Bauer
 * @verion 0.9
 */
public class Application implements IApplication {
	
	private I2B2ImportTool _i2b2ImportTool = null;
		
	protected Properties properties = new Properties();

	HashMap<String,javax.swing.JComponent> exportDialogComponents = new HashMap<String,javax.swing.JComponent>();

	//TODO check if needed
	private Display _display;
	

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception {
		this._display = PlatformUI.createDisplay();
		
		Activator.getDefault().createResource();
		
		//Activator.getDefault().getResource().setDisplay(this._display);
		//_global = new Global(_i2b2ImportTool, _display);
		
		
		try {
			int returnCode = PlatformUI.createAndRunWorkbench(_display, new ApplicationWorkbenchAdvisor());
			initilize();
			
			if (returnCode == PlatformUI.RETURN_RESTART)
				return IApplication.EXIT_RESTART;
			else
				return IApplication.EXIT_OK;
			
		} finally {
			_display.dispose();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		if (!PlatformUI.isWorkbenchRunning())
			return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
	
	//doc
	private void initilize(){
		Debug.f("initilize",this);
			
		//this.alreadyParsed = false;

	}
	
	//doc
	public I2B2ImportTool getMain(){
		return this._i2b2ImportTool;
	}
	
	//doc
	public static StatusView getStatusView(){
		StatusView view = null;
	
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null
				&& PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() != null){
			view = (de.umg.mi.idrt.ioe.view.StatusView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("edu.goettingen.i2b2.importtool.view.StatusView");
		}
    	
    	if (view == null){
    		view = Activator.getDefault().getResource().getStatusView();
    	}

    	return view;
    }
	
	//doc
	public static MainView getMainView(){
		MainView view = null;
	
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null
				&& PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() != null){
			view = (de.umg.mi.idrt.ioe.view.MainView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(Resource.ID.View.MAIN_VIEW);
		}

    	if (view == null){
    		//Debug.dn("MainView",view);
    		view = Activator.getDefault().getResource().getMainView();
    	}
    	
    	return view;
    }
	
	//doc
	public static OntologyView getOntologyView(){
		OntologyView view = null;
	
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null
				&& PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() != null){
			view = (OntologyView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(Resource.ID.View.ONTOLOGY_VIEW);
		}

    	if (view == null){
    		view = Activator.getDefault().getResource().getOntologyView();
    	}
    	
    	return view;
    }

	public static EditorSourceView getEditorSourceView(){
		EditorSourceView view = null;
	
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null
				&& PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() != null){
		
			view = (EditorSourceView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(Resource.ID.View.EDITOR_SOURCE_VIEW);
		}

    	if (view == null){
    		view = Activator.getDefault().getResource().getEditorSourceView();
    	}
    	
    	return view;
    }
	
	public static EditorTargetView getEditorTargetView(){
		EditorTargetView view = null;
	
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null
				&& PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() != null){
		
			view = (EditorTargetView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(Resource.ID.View.EDITOR_TARGET_VIEW);
		}

    	if (view == null){
    		view = Activator.getDefault().getResource().getEditorTargetView();
    	}
    	
    	return view;
    }
	
	public static EditorSourceInfoView getEditorSourceInfoView(){
		EditorSourceInfoView view = null;
	
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null
				&& PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() != null){
		
			view = (EditorSourceInfoView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(Resource.ID.View.EDITOR_SOURCE_INFO_VIEW);
		}

    	if (view == null){
    		view = Activator.getDefault().getResource().getEditorSourceInfoView();
    	}
    	
    	return view;
    }
	
	public static EditorTargetInfoView getEditorTargetInfoView(){
		EditorTargetInfoView view = null;
	
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null
				&& PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() != null){
		
			view = (EditorTargetInfoView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(Resource.ID.View.EDITOR_TARGET_INFO_VIEW);
		}
		
    	if (view == null){
    		view = Activator.getDefault().getResource().getEditorTargetInfoView();
    	}
    	
    	
    	return view;
    }

	/**
	 * Sets the main program link.
	 * 
	 * @param i2b2ImportTool the i2b2ImportTool
	 */
 	public void setI2B2ImportTool(I2B2ImportTool i2b2ImportTool){
 		this._i2b2ImportTool = i2b2ImportTool;
 	}
 	
 	/**
	 * Gets the main program link.
	 * 
	 * @return returns the i2b2ImportTool
	 */
 	public I2B2ImportTool getI2B2ImportTool(){
 		return this._i2b2ImportTool;
 	}
 	
 	//doc
	public void setResource(Resource resource){
		//this._resource = resource;
	}
	
	//doc
	public static Resource getResource(){
		return (getMainView() != null) ? getMainView().getResource() : null;
	}

	//doc
	public static Display getDisplay(){
		return PlatformUI.getWorkbench().getDisplay();
	}

	//doc
	public static Shell getShell(){
		return PlatformUI.getWorkbench().getDisplay().getActiveShell();
	}

	//doc
	public static void executeCommand(String commandID){
		IWorkbenchWindow activeWorkbenchWindow = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow();
		System.out.println("ExecuteCommand: " + commandID);
		if (activeWorkbenchWindow != null) {
			IHandlerService handlerService = (IHandlerService)
				activeWorkbenchWindow.getService(IHandlerService.class);
			try {
				handlerService.executeCommand(commandID, null);
			} catch(NotHandledException ne){
				Debug.d("NotHandledException while executing command \"" + commandID + "\"");
			} catch (Exception e) {
				Console.error(e, true);
			}
		}
	}
	
	//doc
	public static void executeCommand(ParameterizedCommand parameterizedCommand){
		IWorkbenchWindow activeWorkbenchWindow = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow();
		
		if (activeWorkbenchWindow != null) {
			IHandlerService handlerService = (IHandlerService)
				activeWorkbenchWindow.getService(IHandlerService.class);
			if (handlerService != null){
				try {
					handlerService.executeCommand(parameterizedCommand, null);
				} catch (Exception e) {
					Console.error(e);
				}
			} else
				Debug.e("handlerService == null");
		} else
			Debug.e("activeWorkbenchWindow == null");
	}
	
	public static void executeCommand(ActionCommand command){

			Application.getResource().setActionCommand(command);

			if ( command.hasParameters() ) {
				System.out.println("Command hasParameters");
				Application.executeCommand(command.getParameterizedCommand());
			} else {
				System.out.println("Command hasNOOOOParameters");
				Application.executeCommand(command.getCommandID());
			}
	}
	
	//debug testing the plugin interface
	private void runGreeterExtension() {
		
		Debug.d("trying to find some plugins already ...");
		
		/*
		
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor("edu.goettingen.i2b2.importtool.ExtensionPoint.ImportInterface");
		try {
			for (IConfigurationElement e : config) {
				System.out.println("Evaluating extension");
				final Object o = e.createExecutableExtension("class");
				if (o instanceof edu.goettingen.i2b2.importtool.ExtensionPoint.definition.ImportInterface) {
					ISafeRunnable runnable = new ISafeRunnable() {
						@Override
						public void handleException(Throwable exception) {
							System.out.println("Exception in client");
						}

						@Override
						public void run() throws Exception {
							((ImportInterface) o).test();
						}
					};
					SafeRunner.run(runnable);
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}
		
		*/
	}
	
}
