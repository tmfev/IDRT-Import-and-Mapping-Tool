package de.umg.mi.idrt.ioe.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.handlers.HandlerUtil;

import de.umg.mi.idrt.ioe.Debug;
import de.umg.mi.idrt.ioe.MessageDialog;


public class OpenDialog extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		
		
		String title = "";
		String message = "";
		int type = 0; // info, error, success
		String parameterType = "";
		
		if (event.getParameter("edu.goettingen.i2b2.importtool.commands.OpenDialog.Parameter.Title") != null){
			title = event.getParameter("edu.goettingen.i2b2.importtool.commands.OpenDialog.Parameter.Title");
		} else {
			title = "no title available";
		}
		
		if (event.getParameter("edu.goettingen.i2b2.importtool.commands.OpenDialog.Parameter.Message") != null){
			title = event.getParameter("edu.goettingen.i2b2.importtool.commands.OpenDialog.Parameter.Message");
		} else {
			message = "no message available";
		}
		
		parameterType = event.getParameter("edu.goettingen.i2b2.importtool.commands.OpenDialog.Parameter.Type");
		if (parameterType != null){
			if (parameterType.equals("error"))
				type = org.eclipse.jface.dialogs.MessageDialog.ERROR;
			else if (parameterType.equals("success"))
				type = org.eclipse.jface.dialogs.MessageDialog.WARNING;
			else
				type = org.eclipse.jface.dialogs.MessageDialog.INFORMATION;
		} else {
			type = org.eclipse.jface.dialogs.MessageDialog.INFORMATION;
		}
		
		
		org.eclipse.jface.dialogs.MessageDialog messageDialog = new org.eclipse.jface.dialogs.MessageDialog(HandlerUtil.getActiveWorkbenchWindow(
				event).getShell(), title, null,
				message, type,
		        new String[] { "Ok" }, 1);
		if (messageDialog.open() == 0) {
		    System.out.println("Ok klickt!");
		}
		
		
		//org.eclipse.jface.dialogs.MessageDialog dialog = MessageDialog(HandlerUtil.getActiveWorkbenchWindow(
		//		event).getShell());
		
		
		/*
		Application.getDisplay().asyncExec
	    (new Runnable() {
	        public void run() {
	            MessageDialog.openWarning(Application.getShell(),"wrong","no")
	        }
	    });
	}
		*/

		
		//dialog.setDialogTitle(title);
		/*
		if (type.equals("error")){
			dialog.setErrorMessage(message);
		} else {
			dialog.setMessage(message);
		}
		*/
		//dialog.setMessage("Hallo Test!");
		
		//dialog.create();
		
		/*
		if (dialog.open() == Window.OK) {
			Debug.d(dialog.getFirstName());
			Debug.d(dialog.getLastName());
		}
		*/
		
		
		return null;
	}




}
