package de.umg.mi.idrt.ioe.misc;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 * Department of Medical Informatics Goettingen
 * www.mi.med.uni-goettingen.de
 */
public class Progress {


	private static int value;
	private static String title;
	private static String currentAction;
	private ProgressBar progressBar; 
	private Label lblTitle;
	private Label lblCurrentAction;
	private Composite comp;
	
	
	public Progress() {
		
		
	}

	/**
	 * @param progressBar
	 * @param lblTitle
	 * @param lblCurrentAction
	 */
	public Progress(Composite comp, ProgressBar progressBar, Label lblTitle, Label lblCurrentAction) {
		this.progressBar = progressBar;
		this.lblTitle = lblTitle;
		this.lblCurrentAction = lblCurrentAction;
		this.comp = comp;
	}

	public static int getValue() {
		return value;
	}

	public static void setValue(int value) {
		Progress.value = value;
	}

	public static String getCurrentAction() {
		return currentAction;
	}

	public static void setCurrentAction(String currentAction) {
		Progress.currentAction = currentAction;
	}

	public static String getTitle() {
		return title;
	}

	public static void setTitle(String title) {
		Progress.title = title;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public Label getLblTitle() {
		return lblTitle;
	}

	public void setLblTitle(Label lblTitle) {
		this.lblTitle = lblTitle;
	}

	public Label getLblCurrentAction() {
		return lblCurrentAction;
	}

	public void setLblCurrentAction(Label lblCurrentAction) {
		this.lblCurrentAction = lblCurrentAction;
	}

	/**
	 * @param value
	 * @param title
	 * @param currentAction
	 */
	public void setProgress(int value, String title, String currentAction) {
		
		getProgressBar().setSelection(value);
		getLblTitle().setText(title);
		getLblCurrentAction().setText(currentAction);
	}
	
	public void finish(String title, String currentAction) {
		getProgressBar().dispose();
		getLblTitle().setText(title);
		getLblCurrentAction().setText(currentAction);
		this.comp.redraw();
		this.comp.update();
	}
	
}
