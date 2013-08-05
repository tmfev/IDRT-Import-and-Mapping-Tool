package de.umg.mi.idrt.ioe.commands.OntologyEditor;


import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.umg.mi.idrt.ioe.Activator;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.I2B2ImportTool;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.OTCreatorTOS;
import de.umg.mi.idrt.ioe.OntologyTree.OTRenderer;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeModel;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.OntologyTree.TOSConnector;
import de.umg.mi.idrt.ioe.view.EditorSourceInfoView;
import de.umg.mi.idrt.ioe.view.EditorSourceView;
import de.umg.mi.idrt.ioe.view.EditorTargetInfoView;
import de.umg.mi.idrt.ioe.view.EditorTargetView;



public class ReadTarget extends AbstractHandler {

	
	public ReadTarget(){
		super();
	}

	private ExecutionEvent _event;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		System.out.println("OEReadTarget -> execute");
		
		I2B2ImportTool i2b2ImportTool = new I2B2ImportTool(null);

		OntologyTreeNode treeRoot = i2b2ImportTool.getMyOntologyTrees().getOntologyTreeTarget().getNodeLists().getNodeByPath("\\i2b2\\");
		
		
		if ( treeRoot == null ){
			System.out.println("getOTTarget treeRoot is null!");
		} 
		
		System.out.println("I2B2ImportTool isNull1?:" + (i2b2ImportTool == null ? "true" : "false") );
		
			// create the main ontology view
			EditorTargetView editorSourceView;
			EditorTargetView editorTargetView;
			
			
			ProgressMonitorDialog dialog = new ProgressMonitorDialog(Application.getShell());
			// move variables to fields to be used in the dialog
			_event = event;
			
			
			try {

				dialog.run(true, true, new IRunnableWithProgress(){
				     public void run(IProgressMonitor monitor) {

				    	 

						monitor.done();
				     }
				 });
			} catch (InvocationTargetException e) {
				Console.error(e);
			} catch (InterruptedException e) {
				Console.error(e);
			} 

			
			try {
				editorTargetView = (EditorTargetView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(Resource.ID.View.EDITOR_TARGET_VIEW);
					
								
				TOSConnector tos = new TOSConnector();
				tos.readTargetOntology("1");

				
				//editorTargetView.getTreeViewer().getTree().clearAll(true);
					
				//i2b2ImportTool.getMyOT().getOTTarget().setModel(newTreeModel2);
				//i2b2ImportTool.getMyOntologyTrees().getOntologyTreeTarget().updateUI();
				Application.getEditorTargetView().setSelection(editorTargetView.getI2B2ImportTool().getMyOntologyTrees().getOntologyTreeTarget().getRootNode());
				Application.getEditorTargetView().getTreeViewer().expandAll();
				/*
				if (OTTarget != null) {
					OTTarget.setCellRenderer( new OTRenderer( i2b2ImportTool.getMyOT() ));
				}
				*/
				

				
				
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
			//return null;
		//}
		
			
		return null;
	}
}
