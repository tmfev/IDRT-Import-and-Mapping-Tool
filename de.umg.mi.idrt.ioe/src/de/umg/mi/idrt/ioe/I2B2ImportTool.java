package de.umg.mi.idrt.ioe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardContainer;

import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeCreator;


public class I2B2ImportTool extends JFrame implements TreeSelectionListener,
		ActionListener {

	public String programmTitle = "";
	protected Properties properties = new Properties();
	private boolean _alreadyParsed = false;
	public MyOntologyTree myOT = null;
	private UserMessage userMessage = new UserMessage();
	private String importFile;
	private File importInputDir;
	private IWizardContainer _wizardContainer;
	final static private String FOLDERSEPERATOR = "\\"; 
	private OntologyTreeCreator otCreator = null;

	/**
	 * 
	 * @param args
	 *            [0] the default input directory is input, but there is also a
	 *            parameter for den input directory
	 */
	public I2B2ImportTool(String[] args) {
		Debug.f("I2B2ImportTool", this);
		
		
		/* startup test area */
		
		
		Debug.d("testing the TOSConnector ....");


		setMyOT( new MyOntologyTree() );

		Application.getStatusView().addMessage(new SystemMessage (Application.getResource().getText("I2B2IMPORTTOOL.INITIALIZED"),
				SystemMessage.MessageType.SUCCESS,
				SystemMessage.MessageLocation.MAIN));
	}

	public void setMyOT (MyOntologyTree myOT){
		this.myOT = myOT;
	}
	
	public MyOntologyTree getMyOntologyTrees(){
		return this.myOT;
	}


    public void showImportStatusbar(){
    	Debug.f("showImportStatusbar", this);

		ProgressMonitorDialog dialog = new ProgressMonitorDialog(Application.getShell());    
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
			         //return monitor;
			         
			         

			         
			         monitor.beginTask("Second time ...", 5);
			         for (int i = 0; i < 5; i++) {
			        	 try {
					        	Thread.sleep(500);
							} catch (InterruptedException e) {
								Console.error(e);
								e.printStackTrace();
							}
							monitor.worked(1);
			         }
			         
			         monitor.done();
			     }
			 });
		} catch (InvocationTargetException e) {
			Console.error(e);
			e.printStackTrace();
		} catch (InterruptedException e) {
			Console.error(e);
			e.printStackTrace();
		} 
		
		return;
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
					Console.error(e);
					e.printStackTrace();
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

    public void getOntologyTab(){
    	return ;
    }
    
    public void setWizardContainer(IWizardContainer wizardContainer){
    	this._wizardContainer = wizardContainer;
    }
    
    public void executeExport(){
    	/*
		if (getMyOntologyTree() == null
				|| getMyOntologyTree().getOntologyTree() == null
				|| getMyOntologyTree().getOntologyTree().isValid() == false) {
			Application.getStatusView().addMessage(Application.getResource().getText("EXPORT.ERROR.NO_ONTOLOGY"),
					SystemMessage.MessageType.ERROR);
		} else if (getMyOntologyTree().getOntologyTree().isValid() == false) {
			Application.getStatusView().addMessage(Application.getResource().getText("EXPORT.ERROR.NO_VALID_ONTOLOGY"),
					SystemMessage.MessageType.ERROR);
			
		} else {
			if (getMyOntologyTree().export()){
				Application.getStatusView().addMessage(Application.getResource().getText("EXPORT.SUCCESS"),
						SystemMessage.MessageType.SUCCESS);
			} else {
				Application.getStatusView().addMessage(Application.getResource().getText("EXPORT.ERROR"),
						SystemMessage.MessageType.ERROR);
			}
		}
		*/
    }

    public void setOTCreator (OntologyTreeCreator otCreator){
    	this.otCreator = otCreator;
    }

    public OntologyTreeCreator getOTCreator(){
    	return this.otCreator;
    }






	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}






	@Override
	public void valueChanged(TreeSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}
