package de.umg.mi.idrt.ioe.view;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import de.umg.mi.idrt.ioe.Debug;


public class OntologyPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		Debug.f("createInitialLayout",this);

	    IFolderLayout folder = layout.createFolder("edu.goettingen.i2b2.importtool",
	    IPageLayout.TOP, 0.8f, layout.getEditorArea());
	    folder.addPlaceholder("edu.goettingen.i2b2.importtool.view.OntologyView:*");
	    folder.addView("edu.goettingen.i2b2.importtool.view.OntologyView");
	    folder.addPlaceholder("edu.goettingen.i2b2.importtool.view.OntologyNodeEditorView:*");
	    folder.addView("edu.goettingen.i2b2.importtool.view.OntologyNodeEditorView");

	}

}
