package de.umg.mi.idrt.ioe;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class PerspectiveExpert implements IPerspectiveFactory {

	/**
	 * Add fast views to the perspective.
	 */
	private void addFastViews(IPageLayout layout) {
	}

	/**
	 * Add perspective shortcuts to the perspective.
	 */
	private void addPerspectiveShortcuts(IPageLayout layout) {
	}

	/**
	 * Add view shortcuts to the perspective.
	 */
	private void addViewShortcuts(IPageLayout layout) {
	}

	/**
	 * Creates the initial layout for a page.
	 */
	public void createInitialLayout(IPageLayout layout) {
		addPerspectiveShortcuts(layout);
		layout.setEditorAreaVisible(false);
		layout.addView("de.umg.mi.idrt.ioe.EditorStagingInfoView", IPageLayout.BOTTOM, 0.71f, IPageLayout.ID_EDITOR_AREA);
		layout.addView("de.umg.mi.idrt.ioe.OntologyEditor", IPageLayout.RIGHT, 0.5f, IPageLayout.ID_EDITOR_AREA);
		layout.addView("de.umg.mi.idrt.importtool.ServerView", IPageLayout.LEFT, 0.3f, "de.umg.mi.idrt.ioe.OntologyEditor");
		layout.addView("edu.goettingen.i2b2.importtool.view.StatusView", IPageLayout.BOTTOM, 0.65f, "de.umg.mi.idrt.importtool.ServerView");
		layout.addView("de.umg.mi.idrt.ioe.ProgressView", IPageLayout.BOTTOM, 0.5f, "edu.goettingen.i2b2.importtool.view.StatusView");
		layout.addView("de.umg.mi.idrt.ioe.EditorTargetInfoView", IPageLayout.RIGHT, 0.5f, "de.umg.mi.idrt.ioe.EditorStagingInfoView");
		addFastViews(layout);
		addViewShortcuts(layout);
		addPerspectiveShortcuts(layout);
		System.out.println("READY");
	}
	
	
}
