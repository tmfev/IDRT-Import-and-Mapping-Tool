package de.umg.mi.idrt.ioe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.commands.ICommandService;

/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> Department of
 *         Medical Informatics Goettingen www.mi.med.uni-goettingen.de
 * 
 *         
 */

public class ActionCommand {

	private String _commandID = "";
	private Map<String, Object> _params = new HashMap<String, Object>();

	public ActionCommand(String commandID) {
		_commandID = commandID;
	}

	public ActionCommand(ExecutionEvent event) {
		Map<String, String> parameters = event.getParameters();
		Iterator<String> parametersIterator = parameters.keySet().iterator();
		String parameterKey = "";
		while (parametersIterator.hasNext()) {
			parameterKey = parametersIterator.next();
			_params.put(parameterKey,
					checkParameter(parameters.get(parameterKey)));
		}
	}

	public void addParameter(String id, Object value) {
		System.out
				.println("addingParamter " + id + ":" + String.valueOf(value));
		_params.put(id, value);
	}

	public void addParameter(String id, boolean value) {

		_params.put(id, String.valueOf(value));
	}

	public void addParameter(String id, int value) {
		_params.put(id, String.valueOf(value));
	}

	public ParameterizedCommand getParameterizedCommand() {
		IWorkbenchWindow activeWorkbenchWindow = Activator.getDefault()
				.getWorkbench().getActiveWorkbenchWindow();

		if (activeWorkbenchWindow != null) {
			ICommandService commandService = (ICommandService) activeWorkbenchWindow
					.getService(ICommandService.class);
			if (commandService != null) {
				ParameterizedCommand parameterizedCommand = ParameterizedCommand
						.generateCommand(commandService.getCommand(_commandID),
								_params);
				return parameterizedCommand;
			}
		}
		return null;
	}

	public ParameterizedCommand getCommand() {
		IWorkbenchWindow activeWorkbenchWindow = Activator.getDefault()
				.getWorkbench().getActiveWorkbenchWindow();

		
		if (activeWorkbenchWindow != null) {

			ICommandService commandService = (ICommandService) activeWorkbenchWindow
					.getService(ICommandService.class);


			if (commandService != null) {


				// commandService.
				ParameterizedCommand parameterizedCommand = ParameterizedCommand
						.generateCommand(commandService.getCommand(_commandID),
								_params);
				return parameterizedCommand;
			}
		}
		return null;
	}

	public Object checkParameter(String value) {
		// TODO delete this
		if (true)
			return value;

		if (value.equals(String.valueOf(Boolean.TRUE))
				|| value.equals(String.valueOf(Boolean.FALSE))) {
			return Boolean.parseBoolean(value);
		}

		try {
			Integer.valueOf(value);
			return Integer.valueOf(value);
		} catch (NumberFormatException e) {
			Console.error(e);
			e.printStackTrace();
		}

		return value;
	}

	public Map<String, Object> getParameters() {
		return _params;
	}

	public Object getParameter(String key) {

		return _params.get(key);
	}

	public Boolean getBoolean(String key) {

		// check if parameter is really there
		if (isNull(_params.get(key)))
			return false;

		// check if parameter is really a boolean
		if (_params.get(key).getClass().equals(Boolean.class)) {
			return (Boolean) _params.get(key);
		} else {
			Console.error("A boolean parameter wasn't really a boolean but a "
					+ _params.get(key).getClass().getName()
					+ ". Trying to convert to now ...");

			if ("true".equals(_params.get(key))) {
				return true;
			} else if ("false".equals(_params.get(key))) {
				return false;
			} else {
				Console.error("Could not convert a boolean action parameter.");
				return false;
			}
		}

	}

	public int getInteger(String key) {

		// check if parameter is really there
		if (isNull(_params.get(key)))
			return 0;

		try {
			String val = (String) _params.get(key);
			if (val.isEmpty())
				return 0;
			Integer.valueOf((String) _params.get(key));
			return Integer.valueOf((String) _params.get(key));
		} catch (NumberFormatException e) {
			Console.error("Could not convert an integer action parameter.");
			Console.error(e);
			e.printStackTrace();
		}
		return 0;
	}

	public String getString(String key) {

		// check if parameter is really there
		if (isNull(_params.get(key)))
			return "";

		try {
			String returnValue = String.valueOf(_params.get(key));
			return returnValue;
		} catch (NumberFormatException e) {
			Console.error("Could not convert a string action parameter.");
			Console.error(e);
			e.printStackTrace();
		}
		return "";
	}

	public String getCommandID() {
		return _commandID;
	}

	private boolean isNull(Object object) {
		if (object == null) {
			Console.error("action parameter is null");
			return true;
		}
		return false;
	}

	public boolean hasParameters() {
		return _params.size() > 0 ? true : false;
	}

}
