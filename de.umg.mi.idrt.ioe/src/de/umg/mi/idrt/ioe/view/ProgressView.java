package de.umg.mi.idrt.ioe.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import swing2swt.layout.BorderLayout;


public class ProgressView extends ViewPart {
	private static Label lblTitle;
	private static Label lblCurrentAction;
	private static ProgressBar progressBar;
	private static int value;
	private static String title;
	private static String currentAction;
	private static Thread indeterminateThread;
	private static Composite composite;

	public ProgressView() {
	}

	//	public static Progress addProgress() {
	//
	//		final Composite composite_1 = new Composite(composite, SWT.NONE);
	//		composite_1.moveAbove(null);
	//		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	//		composite_1.setLayout(new GridLayout(2, false));
	//
	//		lblTitle = new Label(composite_1, SWT.NONE);
	//		lblTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
	//		lblTitle.setText("No operations to display at this time.");
	//
	//		progressBar = new ProgressBar(composite_1, SWT.NONE);
	//		//		gd_progressBar.heightHint = 25;
	//		progressBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	//		progressBar.setSelection(0);
	//		//		progressBar.setState(SWT.ERROR);
	//
	//
	//		Button btnCancel = new Button(composite_1, SWT.NO_BACKGROUND);
	//		btnCancel.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/terminate_co.gif"));
	//		btnCancel.setGrayed(true);
	//
	//		lblCurrentAction = new Label(composite_1, SWT.NONE);
	//		lblCurrentAction.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
	//		lblCurrentAction.setText("");
	//
	//		
	//		new Label(composite_1, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
	//		
	//		final Progress pro = new Progress(composite_1,progressBar,lblTitle,lblCurrentAction);
	//		getProgressList().add(pro);
	//
	//		btnCancel.addSelectionListener(new SelectionListener() {
	//
	//			@Override
	//			public void widgetSelected(SelectionEvent e) {
	//				composite_1.dispose();
	//				composite.layout();
	//				getProgressList().remove(pro);
	//			}
	//
	//			@Override
	//			public void widgetDefaultSelected(SelectionEvent e) {
	//			}
	//		});
	//		composite.redraw();
	//		composite.layout();	
	//	
	//		return pro;
	//	}

	public static void setProgress(boolean indeterminate, String title, String currentAction) {

		//		progressBar.dispose();
		progressBar = new ProgressBar(composite, SWT.INDETERMINATE);
		progressBar.setSelection(50);
		setProgressTitle(title);
		setCurrentAction(currentAction);
		composite.redraw();
		composite.update();
		updateProgressBar();

	}

	public static void clearProgress() {
		indeterminateThread.interrupt();
		setProgressTitle("No operations to display at this time.");
		setCurrentAction("");
		setProgressValue(0);
		updateProgressBar();
	}

	public static void updateProgressBar() {
		if (lblTitle!=null&&!lblTitle.isDisposed())
			lblTitle.setText(getProgressTitle());
		if (lblCurrentAction!=null&&!lblCurrentAction.isDisposed())
			lblCurrentAction.setText(getCurrentAction());
		if (progressBar!=null&&!progressBar.isDisposed())
			progressBar.setSelection(getProgressValue());
		if (composite!=null&&!composite.isDisposed()) {
			composite.update();
			composite.redraw();
		}
	}

	public static void setProgress(int value, String title, String currentAction) {
		setProgressValue(value);
		setProgressTitle(title);
		setCurrentAction(currentAction);

		updateProgressBar();
	}

	public static int getValue() {
		if (progressBar!=null) {
			return progressBar.getSelection();
		}
		else
			return 0;
	}

	public static void setProgress(int value) {
		setProgressValue(value);
		updateProgressBar();
	}

	public static void setProgress(int value, String currentAction) {
		setProgressValue(value);
		setCurrentAction(currentAction);
		updateProgressBar();
	}

	@Override
	public void createPartControl(Composite parent) {

		composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new BorderLayout(0, 0));
		final Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.moveAbove(null);
		composite_1.setLayout(new GridLayout(2, false));

		lblTitle = new Label(composite_1, SWT.NONE);
		lblTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		lblTitle.setText("No operations to display at this time.");

		progressBar = new ProgressBar(composite_1, SWT.NONE);
		//		gd_progressBar.heightHint = 25;
		progressBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		progressBar.setSelection(0);
		//		progressBar.setState(SWT.ERROR);


		//		Button btnCancel = new Button(composite_1, SWT.NO_BACKGROUND);
		//		btnCancel.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/terminate_co.gif"));
		//		btnCancel.addSelectionListener(new SelectionListener() {
		//
		//			@Override
		//			public void widgetSelected(SelectionEvent e) {
		//			}
		//
		//			@Override
		//			public void widgetDefaultSelected(SelectionEvent e) {
		//			}
		//		});
		lblCurrentAction = new Label(composite_1, SWT.NONE);
		lblCurrentAction.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		lblCurrentAction.setText("");


		composite.redraw();
		composite.layout();	
	}

	@Override
	public void setFocus() {
	}

	private static int getProgressValue() {
		return value;
	}

	private static void setProgressValue(int value) {
		ProgressView.value = value;
	}
	/**
	 * @return the title
	 */
	private static String getProgressTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public static void setProgressTitle(String title) {
		ProgressView.title = title;
	}

	/**
	 * @return the currentAction
	 */
	private static String getCurrentAction() {
		return currentAction;
	}

	/**
	 * @param currentAction the currentAction to set
	 */
	private static void setCurrentAction(String currentAction) {
		ProgressView.currentAction = currentAction;
	}

}
