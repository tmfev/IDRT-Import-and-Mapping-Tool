package de.umg.mi.idrt.ioe.view;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import de.umg.mi.idrt.ioe.Debug;


public class MainPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		Debug.f("createInitialLayout for MainPerspective",this);
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);


	    layout.addView("edu.goettingen.i2b2.importtool.view.EditorTargetView", IPageLayout.LEFT, 1.0f, editorArea);
	    
	    
	    
	    

//		    layout.addView("edu.goettingen.i2b2.importtool.view.ServerView", IPageLayout.LEFT, 1.0f, editorArea);
		
		//layout.addStandaloneView(View.ID,  false, IPageLayout.LEFT, 1.0f, editorArea);
		/*
	    IFolderLayout folder = layout.createFolder("edu.goettingen.i2b2.importtool",
	    IPageLayout.TOP, 0.8f, layout.getEditorArea());
	    folder.addView("edu.goettingen.i2b2.importtool.view.MainView");
	    folder.addView("edu.goettingen.i2b2.importtool.view.StatusView");
	    folder.addView("edu.goettingen.i2b2.importtool.view.OntologyView");
		*/
	}

}
