package de.umg.mi.idrt.ioe.commands;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.handlers.HandlerUtil;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Debug;


public class ShowImportProgressBar extends AbstractHandler {

	public ShowImportProgressBar(){
		super();
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Debug.f("execute", this);
		
		
		
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(HandlerUtil.getActiveWorkbenchWindow(event).getShell());    
		try {
			
			dialog.run(true, true, new IRunnableWithProgress(){
			     public void run(IProgressMonitor monitor) {
			         monitor.beginTask("Some nice progress message here ...", 100);
			         
			         for (int i = 0; i < 5; i++) {
			        	   monitor.subTask("Subtask # " + i + " running.");
			        	   
			        	   
			        	   
			        	   runSubTask(new SubProgressMonitor(monitor, 20, SubProgressMonitor.PREPEND_MAIN_LABEL_TO_SUBTASK), i);
			        	   
			        	   
			        	   //monitor.worked(20);
			         }
 			         
			         
			         // execute the task ...

			         monitor.done();
			     }
			 });
		} catch (InvocationTargetException e) {
			Console.error(e.toString() + " @OntologyTreePatientAnswer Code1");
		} catch (InterruptedException e) {
			Console.error(e.toString() + " @OntologyTreePatientAnswer Code2");
		} 
		
		return null;
	}
	
	private void runSubTask(IProgressMonitor monitor, int subTaskId) {

		int chance = 0;
		
		int counter = 0;
		
		monitor.beginTask("Sub task running", 20);
		
		for (int i = 0; i < 10; i++) {
			monitor.subTask("Inside subtask, " + i + " out of 10");
			// do something here ...
			
			

			chance = (int) (Math.random()*3+1);
			
			
			if (chance == 3){
				//Application.getStatusView().addMessage(i + " is invalid");
				Debug.d(i + " is invalid");
			} else {
			
		        try {
		        	Thread.sleep(500);
				} catch (InterruptedException e) {
					Console.error(e.toString() + " @OntologyTreePatientAnswer Code3");
				}
				monitor.worked(2);
				counter++;
				//monitor.worked(IProgressMonitor.UNKNOWN);

			}
			if (monitor.isCanceled())
				throw new OperationCanceledException();
		}
		
		
		if (counter < 10){
			// something is missing
			Debug.d("counter < 10", counter);
			for (; counter < 10; counter++){
				monitor.worked(2);
			}
		}
		
		monitor.done();
	}	

}
