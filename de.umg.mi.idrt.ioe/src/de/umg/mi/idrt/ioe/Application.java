package de.umg.mi.idrt.ioe;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Properties;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

import au.com.bytecode.opencsv.CSVReader;

import de.umg.mi.idrt.ioe.OntologyTree.FileHandling;
import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTrees;
import de.umg.mi.idrt.ioe.commands.OntologyEditor.CombineNodesCommand;
import de.umg.mi.idrt.ioe.misc.Regex;
import de.umg.mi.idrt.ioe.view.EditorSourceInfoView;
import de.umg.mi.idrt.ioe.view.EditorTargetInfoView;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;
import de.umg.mi.idrt.ioe.view.StatusView;

/**
 * This class controls all aspects of the application's execution
 * 
 * @author Christian Bauer
 * @verion 0.9
 */
public class Application implements IApplication {

	protected Properties properties = new Properties();

	HashMap<String, javax.swing.JComponent> exportDialogComponents = new HashMap<String, javax.swing.JComponent>();

	private Display _display;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
	 * IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception {
		this._display = PlatformUI.createDisplay();

		OntologyEditorView.setMyOntologyTree(new MyOntologyTrees());

		Activator.getDefault().createResource();

		File file = new File(FileHandling.getCFGFilePath("regex.csv"));
		
		CSVReader reader = new CSVReader(new FileReader(file), ';');
		
		String[] line = reader.readNext();
		
		while ((line = reader.readNext()) != null) {
			Regex regex = new Regex(line[0], line[1]);
			CombineNodesCommand.addRegEx(regex);
		}
		
		reader.close();
		// Activator.getDefault().getResource().setDisplay(this._display);
		// _global = new Global(_i2b2ImportTool, _display);

		try {
			int returnCode = PlatformUI.createAndRunWorkbench(_display,
					new ApplicationWorkbenchAdvisor());

			if (returnCode == PlatformUI.RETURN_RESTART)
				return IApplication.EXIT_RESTART;
			else
				return IApplication.EXIT_OK;

		} finally {
			_display.dispose();
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

	// doc
	public void setResource(Resource resource) {
		// this._resource = resource;
	}

	// doc
	public static Resource getResource() {
		return Activator.getDefault().getResource();
	}

	// doc
	public static Display getDisplay() {
		return PlatformUI.getWorkbench().getDisplay();
	}

	// doc
	public static Shell getShell() {
		return PlatformUI.getWorkbench().getDisplay().getActiveShell();
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

	public static void executeCommand(ActionCommand command) {

		if (command.hasParameters()) {
			Application.executeCommand(command.getParameterizedCommand());
		} else {
			Application.executeCommand(command.getCommandID());
		}
	}

}
