package de.umg.mi.idrt.ioe;

import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

import de.umg.mi.idrt.ioe.misc.Regex;

/**
 * This class controls all aspects of the application's execution
 * 
 * @author Christian Bauer
 * @verion 0.9
 */
public class Application implements IApplication {

	
	public static void executeCommand(ActionCommand command) {

		if (command.hasParameters()) {
			Application.executeCommand(command.getParameterizedCommand());
		} else {
			Application.executeCommand(command.getCommandID());
		}
	}

	// doc
	public static void executeCommand(ParameterizedCommand parameterizedCommand) {
		IWorkbenchWindow activeWorkbenchWindow = Activator.getDefault()
				.getWorkbench().getActiveWorkbenchWindow();

		if (activeWorkbenchWindow != null) {
			IHandlerService handlerService = (IHandlerService) activeWorkbenchWindow
					.getService(IHandlerService.class);
			if (handlerService != null) {
				try {
					handlerService.executeCommand(parameterizedCommand, null);
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (NotDefinedException e) {
					e.printStackTrace();
				} catch (NotEnabledException e) {
					e.printStackTrace();
				} catch (NotHandledException e) {
					e.printStackTrace();
				}

			}
		}
	}

	// doc
	public static void executeCommand(String commandID) {
		IWorkbenchWindow activeWorkbenchWindow = Activator.getDefault()
				.getWorkbench().getActiveWorkbenchWindow();
		System.out.println("ExecuteCommand: " + commandID);
		if (activeWorkbenchWindow != null) {
			IHandlerService handlerService = (IHandlerService) activeWorkbenchWindow
					.getService(IHandlerService.class);
			try {
				handlerService.executeCommand(commandID, null);
			} catch (NotHandledException ne) {
				ne.printStackTrace();
				Console.error("NotHandledException while executing command \""
						+ commandID + "\"");
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (NotDefinedException e) {
				e.printStackTrace();
			} catch (NotEnabledException e) {
				e.printStackTrace();
			} 
		}
	}

	// doc
	public static Display getDisplay() {
		return PlatformUI.getWorkbench().getDisplay();
	}

	// doc
	public static Resource getResource() {
		return Activator.getDefault().getResource();
	}

	// doc
	public static Shell getShell() {
		return PlatformUI.getWorkbench().getDisplay().getActiveShell();
	}

	// doc
	public void setResource(Resource resource) {
		// this._resource = resource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
	 * IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception {
		Display display = PlatformUI.createDisplay();

//		OntologyEditorView.setMyOntologyTree(new MyOntologyTrees());
		 
		Activator.getDefault().createResource();

		Regex.loadRegex();
		// Activator.getDefault().getResource().setDisplay(this._display);
		// _global = new Global(_i2b2ImportTool, _display);

		try {
			int returnCode = PlatformUI.createAndRunWorkbench(display,
					new ApplicationWorkbenchAdvisor());

			if (returnCode == PlatformUI.RETURN_RESTART)
				return IApplication.EXIT_RESTART;
			else
				return IApplication.EXIT_OK;

		} finally {
			display.dispose();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
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
	
	

}
