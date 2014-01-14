package de.umg.mi.idrt.ioe.commands.OntologyEditor;


import javax.swing.tree.DefaultTreeModel;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.umg.mi.idrt.ioe.Activator;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.OntologyTree.TOSConnector;
import de.umg.mi.idrt.ioe.view.EditorTargetInfoView;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;



public class ReadTarget extends AbstractHandler {

	public static Thread readTargetOntThread;
	
	public static Thread getReadTargetOntThread() {
		return readTargetOntThread;
	}
	
	public static void killImport() {
		System.out.println("killing");
		readTargetOntThread.stop();
		System.out.println("killed");
	}
	
	private ExecutionEvent _event;

	public ReadTarget(){
		super();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		System.out.println("OEReadTarget -> execute");

		OntologyTreeNode treeRoot = OntologyEditorView.getOntologyTargetTree().getNodeLists().getNodeByPath("\\i2b2\\");


		if ( treeRoot == null ){
			System.out.println("getOTTarget treeRoot is null!");
		} 



		// create the main ontology view
//		EditorTargetView editorSourceView;


//		ProgressMonitorDialog dialog = new ProgressMonitorDialog(Application.getShell());
//		// move variables to fields to be used in the dialog
//		_event = event;
//
//
//		try {
//
//			dialog.run(true, true, new IRunnableWithProgress(){
//				public void run(IProgressMonitor monitor) {
//
//
//
//					monitor.done();
//				}
//			});
//		} catch (InvocationTargetException e) {
//			Console.error(e);
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//			Console.error(e);
//		} 


		OntologyEditorView.getOntologyTargetTree().removeAll();


		readTargetOntThread = new Thread(new Runnable() {

			@Override
			public void run() {
				TOSConnector tos = new TOSConnector();
				tos.setContextVariable("Job", "read_target_ontology");
				tos.setContextVariable("Var1", "1");
				tos.runJob();						
			}
		});

		readTargetOntThread.run();
		OntologyEditorView.getTargetTreeViewer().getTree().update();

		//editorTargetView.getTreeViewer().getTree().clearAll(true);

		//i2b2ImportTool.getMyOT().getOTTarget().setModel(newTreeModel2);
		//i2b2ImportTool.getMyOntologyTrees().getOntologyTreeTarget().updateUI();
		/*
			Application.getEditorTargetView().setSelection(editorTargetView.getI2B2ImportTool().getMyOntologyTrees().getOntologyTreeTarget().getRootNode());
			Application.getEditorTargetView().getTreeViewer().expandAll();

			Application.getEditorTargetView().getTreeViewer().refresh();
		 */

		// add Target views as well
//			editorTargetView = (EditorTargetView) PlatformUI.getWorkbench()
//					.getActiveWorkbenchWindow().getActivePage()
//					.showView(Resource.ID.View.EDITOR_TARGET_VIEW);
//			Activator.getDefault().getResource()
//			.setEditorTargetView(editorTargetView);


//			Composite comp = editorTargetView.getComposite();

//			comp.dispose();
//
//			editorTargetView.clear();
//
//			editorTargetView.setI2B2ImportTool(i2b2ImportTool);

		DefaultTreeModel newTreeModel2 = new DefaultTreeModel(
				OntologyEditorView.getOntologyTargetTree().getRootNode(),true);

		OntologyEditorView.getOntologyTargetTree()
		.setModel(newTreeModel2);
		OntologyEditorView.getOntologyTargetTree()
		.updateUI();

		OntologyTree ontologyTargetTree = OntologyEditorView.getOntologyTargetTree();


		//return null;
		//}


		return null;
	}
}
