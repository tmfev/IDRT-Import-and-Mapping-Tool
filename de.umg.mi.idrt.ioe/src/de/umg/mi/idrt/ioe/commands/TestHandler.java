package de.umg.mi.idrt.ioe.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Debug;
import de.umg.mi.idrt.ioe.SystemMessage;
import de.umg.mi.idrt.ioe.SystemMessage.MessageType;
import de.umg.mi.idrt.ioe.view.StatusView;

public class TestHandler extends AbstractHandler implements IHandler {

	public TestHandler(){
		super();
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Debug.f("TestHandler");
		//IWorkbenchWindow window = actionBarAdvisor.getActionBarConfigurer().getWindowConfigurer().getWindow();

		
		//StatusView statusView = Application.getStatusView();
		/*
		PlatformUI.getWorkbench();
		PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findViewReference("edu.goettingen.i2b2.importtool.view.StatusView");
		StatusView statusView = (StatusView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findViewReference("edu.goettingen.i2b2.importtool.view.StatusView").getView(true);
		*/
		
		
		StatusView statusView = Application.getStatusView();
		
		if (statusView == null){
			Debug.d("* statusView == null");
			return null;
		}
		
		
		SystemMessage message = null;
		
		MessageType messageType = SystemMessage.MessageType.INFO;
		
		for (int x = 0; x < 4; x++){
			
			if (x % 2 == 0){
				
				if (x % 4 == 0){
					messageType = SystemMessage.MessageType.SUCCESS;
				} else {
					messageType = SystemMessage.MessageType.ERROR;
				}
				
			} else {
				messageType = SystemMessage.MessageType.INFO;
			}
			
			message = new SystemMessage( "testLoop" + x, messageType, SystemMessage.MessageLocation.MAIN );
			
			statusView.addMessage(message);
			
			//Global.messages.addMessage(message);
			
			/*
			stutuslineView.addMessage(message);
			
			*/
			//output.append("TestX="+x+"\n");
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Console.error(e);
			}
		}
		
		
		
		
		
		return null;
	}

}
