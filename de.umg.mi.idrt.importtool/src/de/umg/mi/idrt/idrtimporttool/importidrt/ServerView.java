package de.umg.mi.idrt.idrtimporttool.importidrt;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Collections;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;

import de.goettingen.i2b2.importtool.idrt.StatusListener.StatusListener;
import de.umg.mi.idrt.idrtimporttool.ImportWizard.CSVImportWizard;
import de.umg.mi.idrt.idrtimporttool.ImportWizard.DBImportWizard;
import de.umg.mi.idrt.idrtimporttool.ImportWizard.ODMImportWizard;
import de.umg.mi.idrt.idrtimporttool.ImportWizard.P21ImportWizard;
import de.umg.mi.idrt.idrtimporttool.Log.Log;
import de.umg.mi.idrt.idrtimporttool.messages.Messages;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerDragSourceListener;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerDropTargetListener;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.ServerContentProvider;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.ServerImportDBLabelProvider;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.ServerImportDBModel;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.ServerLabelProvider;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.ServerModel;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.ServerSourceContentProvider;
import swing2swt.layout.BorderLayout;
import org.eclipse.wb.swt.ResourceManager;

/**
 * Main view for the IDRT-Import-Tool.
 * 
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 * 
 */
public class ServerView extends ViewPart {
	public ServerView() {
	}
	public static final String ID = "ImportIDRT.view";

	//	private Composite infoBoxComposite;
	private Composite labelNameComposite;
	private Composite sourceServerComposite;
	private Composite targetServercomposite;
	private Composite sourceTreeComp;
	private Composite logComposite;

	public static boolean stdImportStarted;
	private static Button btnStop;
	private static Composite parent;

	private static SashForm sourceAndTargetServercomposite;

	private static Label lblObservationsCurrent;
	private static Label lblPatientsCurrent;
	private static Label progressLabelTop;
	private static Label progressLabelBottom;

	private static Label subProgressLabel;
	private static Label labelName;
	private static Label labelNameCurrent;
	private static Label labelIp;
	private static Label labelIpCurrent;
	private static Label labelDBUser;
	private static Label labelDBUserCurrent;

	private static TreeViewer targetServerViewer;
	private static TreeViewer sourceServerViewer;

	//	private static TableColumn logTblClmTime;
	//	private static TableColumn logTblClmMessage;

	//	private static Table logTable;

	private static Menu importSourceServerMenu;
	private static Menu mainMenu;

	private static File properties;

	private static String csvPathSpecific;
	private static int selectedServerIndex;
	private static String[] filenames;
	private static boolean sourceServerDisposed;
	private static int[] weights;
	private static String fileName;

	private static ProgressBar progressBar;
	private static ProgressBar progressBar2;

	private static Properties defaultProps;
	private static Text text;

	public static void updateStatus() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				ServerView.setProgress((int) StatusListener.getPercentage());
				ServerView.setProgressTop(StatusListener.getFile());
				ServerView.setProgressBottom(""
						+ StatusListener.getStatus());
				ServerView.setSubProgressTitle(StatusListener.getSubStatus());
				ServerView.setSubProgress((int) StatusListener.getSubPerc());
			}
		});
	}
//	public static void closeBar(String msg, String file, int status) {
//		setProgress(0);
//		Color color = SWTResourceManager.getColor(SWT.COLOR_BLACK);
//		;
//		if (status == 1) {
//			color = SWTResourceManager.getColor(SWT.COLOR_RED);
//		}
//		if (status == 0) {
//			color = SWTResourceManager.getColor(SWT.COLOR_GREEN);
//		}
//		progressLabelTop.setText(file);
//		progressLabelTop.setForeground(color);
//		progressLabelBottom.setText(msg);
//		progressLabelBottom.setForeground(color);
//	}

	public static String getCsvPathSpecific() {
		return csvPathSpecific;
	}

	public static String getCurrentSchema() {
		if (targetServerViewer.getTree().getSelectionCount()<=0)
			return targetServerViewer.getTree().getItem(0).getText();
		else
			return targetServerViewer.getTree().getSelection()[0].getText();
	}

	public static int getCurrentServerIndex() {
		return selectedServerIndex;
	}

	public static String[] getCurrentServers() {
		selectedServerIndex = 0;
		TreeItem[] treeItems = targetServerViewer.getTree().getItems();
		String[] serverNames = new String[treeItems.length];
		for (int i = 0; i < treeItems.length; i++) {
			serverNames[i] = treeItems[i].getText();
			if (targetServerViewer.getTree().getSelectionCount() > 0) {
				if (targetServerViewer.getTree().getSelection()[0].getParentItem() != null) {
					if (treeItems[i].getText().equals(
							targetServerViewer.getTree().getSelection()[0].getParentItem()
							.getText())) {
						selectedServerIndex = i;
					}
				} else if (targetServerViewer.getTree().getSelection()[0] != null) {
					if (treeItems[i].getText().equals(
							targetServerViewer.getTree().getSelection()[0].getText())) {
						selectedServerIndex = i;
					}
				}
			} else {
				selectedServerIndex = 0;
			}
		}
		return serverNames;
	}

	/**
	 * @return the fileName
	 */
	public static String getFileName() {
		return fileName;
	}

	public static TreeViewer getSourceServerViewer() {
		return sourceServerViewer;
	}

	public static String getLogText() {
		return null;
	}

	public static Composite getMainComposite() {
		return parent;
	}

	public static String getSelectedServer() {
		TreeViewer viewer = null;
		viewer = ServerView.getTargetServersViewer();
		if (viewer != null) {
			if (viewer.getTree().getSelectionCount() > 0) {
				if (viewer.getTree().getSelection()[0].getParentItem() != null) {
					String serverUniqueName = viewer.getTree().getSelection()[0]
							.getParentItem().getText();
					return serverUniqueName;
				}
			}
		}
		return null;
	}

	public static TreeViewer getTargetServersViewer() {
		return targetServerViewer;
	}

	private static boolean importStarted() {
		return (ODMImportWizard.started || DBImportWizard.started || CSVImportWizard.started || P21ImportWizard.started || stdImportStarted);
	}

	/**
	 * commands for target server
	 */
	public static void refresh() {
		targetServerViewer.setContentProvider(new ServerContentProvider());
		targetServerViewer.refresh();

		if (!sourceServerViewer.getControl().isDisposed()) {
			sourceServerViewer
			.setContentProvider(new ServerSourceContentProvider());
			sourceServerViewer.refresh();
		}
	}

	public static void setLog(String time, String log) {
		System.out.println("adding new log: " + log + " " + time); 
		//		final TableItem tableItem = new TableItem(logTable, SWT.NONE, 0);
		//		tableItem.setText(new String[] { time, log });
		String old = text.getText();
		if (!old.isEmpty()) {
			String newText = time+"\t"+log + "\n"+ old;
			text.setText(newText);
			text.update();
			text.getParent().layout();
			text.getParent().redraw();
		}
		else {
			String newText = time+"\t"+log;
			text.setText(newText);
			text.update();
			text.getParent().layout();
			text.getParent().redraw();
		}
		//		org.eclipse.swt.graphics.Color color = SWTResourceManager
		//				.getColor(SWT.COLOR_BLACK);
		//		switch (code) {
		//		case 0:
		//			color = SWTResourceManager.getColor(SWT.COLOR_BLACK);
		//			break;
		//		case 1:
		//			color = SWTResourceManager.getColor(SWT.COLOR_RED);
		//			break;
		//		default:
		//			break;
		//		}
		//		tableItem.setForeground(color);
		//		logTblClmTime.pack();
		//		logTblClmMessage.pack();
		//		logTable.update();
	}

	public static void setSubProgress(final int percentage) {
		progressBar2.setSelection(percentage);
	}

	public static void setProgress(final int percentage) {
		progressBar.setSelection(percentage);
	}

	public static void setSubProgressTitle(final String title) {
		subProgressLabel.setText(title);
	}

	public static void setProgressBottom(final String string) {
		if (progressBar.getSelection()>0) {
		progressLabelBottom.setText("" + progressBar.getSelection() + "% " 
				+ string);
		Color color = SWTResourceManager.getColor(SWT.COLOR_BLACK);
		progressLabelBottom.setForeground(color);
		}
		else
			progressLabelBottom.setText("");
	}

	public static void setProgressTop(final String file) {
		progressLabelTop.setText(file);
		Color color = SWTResourceManager.getColor(SWT.COLOR_BLACK);
		progressLabelTop.setForeground(color);
	}

	public static String[] getFilenames() {
		return filenames;
	}

	/**
	 * Adds a TARGET server to the server list.
	 */
	private void addTargetServer() {
		IHandlerService handlerService = (IHandlerService) getSite()
				.getService(IHandlerService.class);
		try {
			targetServerViewer = (TreeViewer) handlerService.executeCommand(
					"de.goettingen.i2b2.importtool.idrt.addServer",

					null);
			targetServerViewer.refresh();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("addServer.command not found"); 
		}
	}
	/**
	 * Edits a TARGET server.
	 */
	private void editTargetServer() {
		IHandlerService handlerService = (IHandlerService) getSite()
				.getService(IHandlerService.class);
		try {
			targetServerViewer = (TreeViewer) handlerService.executeCommand(
					"de.goettingen.i2b2.importtool.idrt.editServer", null); 
			targetServerViewer.refresh();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("editServer.command not found"); 
		}
	}
	/**
	 * Deletes a TARGET server from the server list. 
	 * @see ServerList.
	 */
	private void deleteTargetServer() {
		IHandlerService handlerService = (IHandlerService) getSite()
				.getService(IHandlerService.class);
		try {
			targetServerViewer = (TreeViewer) handlerService.executeCommand(
					"de.goettingen.i2b2.importtool.idrt.deleteServer", null); 
			targetServerViewer.refresh();
		} catch (Exception ex) {
			throw new RuntimeException("deleteServer.command not found"); 
		}
	}
	/**
	 * Imports a TARGET server from disc.
	 */
	private void importTargetServer() {
		IHandlerService handlerService = (IHandlerService) getSite()
				.getService(IHandlerService.class);
		try {
			handlerService.executeCommand(
					"de.goettingen.i2b2.importtool.idrt.importServer", null); 

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("importServer.command not found"); 
		}
	}
	/**
	 * Exports a TARGET server to disc.
	 */
	private void exportTargetServer() {
		IHandlerService handlerService = (IHandlerService) getSite()
				.getService(IHandlerService.class);
		try {
			handlerService.executeCommand(
					"de.goettingen.i2b2.importtool.idrt.exportServer", null); 
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("de.goettingen.i2b2.importtool.idrt.exportServer.command not found"); 
		}
	}

	/**
	 * Imports a SOURCE server.
	 */
	private void addSourceServer() {
		IHandlerService handlerService = (IHandlerService) getSite()
				.getService(IHandlerService.class);
		try {
			sourceServerViewer = (TreeViewer) handlerService.executeCommand(
					"de.goettingen.i2b2.importtool.idrt.addSourceServer", 
					null);
			sourceServerViewer.refresh();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("de.goettingen.i2b2.importtool.idrt.addSourceServer not found"); 
		}
	}
	/**
	 * Edits a SOURCE server.
	 */
	private void editSourceServer() {
		IHandlerService handlerService = (IHandlerService) getSite()
				.getService(IHandlerService.class);
		try {
			sourceServerViewer = (TreeViewer) handlerService.executeCommand(
					"de.goettingen.i2b2.importtool.idrt.editSourceServer", 
					null);
			sourceServerViewer.refresh();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("de.goettingen.i2b2.importtool.idrt.editSourceServer.command not found"); 
		}
	}

	/**
	 * Deletes a SOURCE server from the source server list.
	 */
	private void deleteSourceServer() {
		IHandlerService handlerService = (IHandlerService) getSite()
				.getService(IHandlerService.class);
		try {
			sourceServerViewer = (TreeViewer) handlerService.executeCommand(
					"de.goettingen.i2b2.importtool.idrt.deleteSourceServer", 
					null);
			sourceServerViewer.refresh();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("de.goettingen.i2b2.importtool.idrt.deleteSourceServer.command not found"); 
		}
	}
	/**
	 * Imports a SOURCE server from disc.
	 */
	private void importSourceServer() {
		IHandlerService handlerService = (IHandlerService) getSite()
				.getService(IHandlerService.class);
		try {
			handlerService.executeCommand(
					"de.goettingen.i2b2.importtool.idrt.importSourceServer", 
					null);
			sourceServerViewer.refresh();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("de.goettingen.i2b2.importtool.idrt.importSourceServer.command not found"); 
		}
	}
	/**
	 * Exports a SOURCE server to disc.
	 */
	private void exportSourceServer() {
		IHandlerService handlerService = (IHandlerService) getSite()
				.getService(IHandlerService.class);
		try {
			handlerService.executeCommand(
					"de.goettingen.i2b2.importtool.idrt.exportSourceServer",
					null);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("de.goettingen.i2b2.importtool.idrt.exportSourceServer.command not found"); 
		}
	}

	/**
	 * Admin Control for the TARGET server/project.
	 */
	@SuppressWarnings("unused")
	private void adminTargetServer() {
		IHandlerService handlerService = (IHandlerService) getSite()
				.getService(IHandlerService.class);
		try {
			handlerService.executeCommand(
					"de.goettingen.i2b2.importtool.idrt.adminServer", null); 
			// viewer.refresh();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("adminServer.command not found"); 
		}
	}

	/**
	 * Starts the CSV-Import.
	 */
	private void importCSV() {
		IHandlerService handlerService = (IHandlerService) getSite()
				.getService(IHandlerService.class);
		try {
			handlerService.executeCommand(
					"de.goettingen.i2b2.importtool.CSVImport", null); 
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("CSVImport.command not found"); 
		}
	}

	//	/**
	//	 * Starts the CSV-Specific-Import.
	//	 */
	//	private void importCSVSpecific() {
	//		IHandlerService handlerService = (IHandlerService) getSite()
	//				.getService(IHandlerService.class);
	//		try {
	//			handlerService.executeCommand(
	//					"de.goettingen.i2b2.importtool.CSVImport", null); 
	//		} catch (Exception ex) {
	//			throw new RuntimeException("CSVImport.command not found"); 
	//		}
	//	}

	/**
	 * Starts the ODM-Import.
	 */
	private void importODM() {
		IHandlerService handlerService = (IHandlerService) getSite()
				.getService(IHandlerService.class);
		try {
			handlerService.executeCommand(
					"de.goettingen.i2b2.importtool.ODMImport", null); 

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("ODMImport.command not found"); 
		}
	}
	//	/**
	//	 * Starts the ODM-Specific-Import.
	//	 */
	//	private void importODMSpecific() {
	//		IHandlerService handlerService = (IHandlerService) getSite()
	//				.getService(IHandlerService.class);
	//		try {
	//			handlerService.executeCommand(
	//					"de.goettingen.i2b2.importtool.ODMImportSpecific", null); 
	//
	//		} catch (Exception ex) {
	//			ex.printStackTrace();
	//			throw new RuntimeException("ODMImport.command not found"); 
	//		}
	//	}

	/**
	 * Starts the DB-Import.
	 */
	private void importDB() {
		IHandlerService handlerService = (IHandlerService) getSite()
				.getService(IHandlerService.class);
		try {
			// System.out.println("NYI");
			handlerService.executeCommand(
					"de.goettingen.i2b2.importtool.DBImport", null); 

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("DBImport.command not found"); 
		}
	}

	/**
	 * Starts the P21-Import.
	 */
	private void importP21() {
		IHandlerService handlerService = (IHandlerService) getSite()
				.getService(IHandlerService.class);
		try {
			// System.out.println("NYI");
			handlerService.executeCommand(
					"de.goettingen.i2b2.importtool.P21Import", null); 
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("DBImport.command not found"); 
		}
	}
	private void loadOntology() {
		IHandlerService handlerService = (IHandlerService) getSite()
				.getService(IHandlerService.class);
		try {
			// System.out.println("NYI");
			handlerService.executeCommand(
					"edu.goettingen.i2b2.importtool.OntologyEditorLoad", null); 
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("edu.goettingen.i2b2.importtool.OntologyEditorLoad.command not found"); 
		}
	}
	
	//edu.goettingen.i2b2.importtool.OntologyEditorLoad
	//	/**
	//	 * Removes possible database locks. Only works with Oracle + sys/system user.
	//	 */
	//	private void removeLocks() {
	//		IHandlerService handlerService = (IHandlerService) getSite()
	//				.getService(IHandlerService.class);
	//		try {
	//			handlerService.executeCommand(
	//					"de.goettingen.i2b2.importtool.RemoveLocks", null); 
	//		} catch (Exception ex) {
	//			ex.printStackTrace();
	//			throw new RuntimeException("RemoveLocks.command not found"); 
	//		}
	//	}

	@Override
	public void createPartControl(final Composite parentComp) {
		try {

			parent = new Composite(parentComp, SWT.NONE);
			parent.getShell().setMinimumSize(500, 200);
			//			parent.setLayout(new FillLayout());
			System.out.println("Current Version: "
					+ Activator.getDefault().getBundle().getVersion()
					.toString());

			Bundle bundle = Activator.getDefault().getBundle();

			// Remove tmp files

			Path tmpPath = new Path("/misc/tmp/"); 
			URL tmpURL = FileLocator.find(bundle, tmpPath,
					Collections.EMPTY_MAP);
			
			if (tmpURL==null) {
				Path miscPath = new Path("/misc/"); 
				URL miscURL = FileLocator.find(bundle, miscPath,
						Collections.EMPTY_MAP);
				URL miscURL2 = FileLocator.toFileURL(miscURL);
				File file = new File(miscURL2.getPath()+"/tmp/");
				file.mkdir();
				tmpURL = FileLocator.find(bundle, tmpPath,
						Collections.EMPTY_MAP);
			}
			URL tmpURL2 = FileLocator.toFileURL(tmpURL);
			File folder = new File(tmpURL2.getPath());
			File[] listOfFiles = folder.listFiles();

			
			
			
			for (File listOfFile : listOfFiles) {
				if (listOfFile.getName().endsWith(".tmp") && !listOfFile.getName().equals("ph") ) { 
					listOfFile.delete();
				}
			}
			Path inputPath = new Path("/misc/input/"); 
			URL inputURL = FileLocator.find(bundle, inputPath,
					Collections.EMPTY_MAP);
			if (inputURL==null) {
				Path miscPath = new Path("/misc/"); 
				URL miscURL = FileLocator.find(bundle, miscPath,
						Collections.EMPTY_MAP);
				URL miscURL2 = FileLocator.toFileURL(miscURL);
				File file = new File(miscURL2.getPath()+"/input/");
				file.mkdir();
				inputURL = FileLocator.find(bundle, inputPath,
						Collections.EMPTY_MAP);
			}
			URL inputURL2 = FileLocator.toFileURL(inputURL);
			File inputFolder = new File(inputURL2.getPath());
			File[] listOfInputFiles = inputFolder.listFiles();

			for (File listOfInputFile : listOfInputFiles) {
				if (listOfInputFile.getName().endsWith(".csv")) { 
					listOfInputFile.delete();
				}
			}
			Path outputPath = new Path("/misc/output/"); 
			URL outputURL = FileLocator.find(bundle, outputPath,
					Collections.EMPTY_MAP);
			if (outputURL==null) {
				Path miscPath = new Path("/misc/"); 
				URL miscURL = FileLocator.find(bundle, miscPath,
						Collections.EMPTY_MAP);
				URL miscURL2 = FileLocator.toFileURL(miscURL);
				File file = new File(miscURL2.getPath()+"/output/");
				file.mkdir();
				outputURL = FileLocator.find(bundle, outputPath,
						Collections.EMPTY_MAP);
			}
			URL outputURL2 = FileLocator.toFileURL(outputURL);
			File outputFolder = new File(outputURL2.getPath());
			File[] listOfOutputFiles = outputFolder.listFiles();

			for (File listOfOutputFile : listOfOutputFiles) {
				if (listOfOutputFile.getName().endsWith(".csv")||listOfOutputFile.getName().endsWith(".xml")) { 
					listOfOutputFile.delete();
				}
			}
			// Bundle bundle = Activator.getDefault().getBundle();
			Path propPath = new Path("/cfg/Default.properties"); 
			URL url = FileLocator.find(bundle, propPath, Collections.EMPTY_MAP);
			URL fileUrl = FileLocator.toFileURL(url);
			properties = new File(fileUrl.getPath());
			defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));
			boolean sysLog = ((defaultProps.getProperty("sysoLog") 
					.equals("true")) ? true : false); 
			String logdir = defaultProps.getProperty("sysoLogLoc"); 
			if (sysLog && (logdir != null)) {

				System.out.println(logdir);
				PrintStream ps = new PrintStream(new BufferedOutputStream(
						new FileOutputStream(new File(logdir))), true);
				System.setOut(ps);
				System.setErr(ps);
			}
			try {
				ServerList.loadServersfromProps();
			} catch (BackingStoreException e1) {
				e1.printStackTrace();
			}

			parent.pack();
			parent.setLayout(new GridLayout(1, false));

			SashForm sashForm = new SashForm(parent, SWT.NONE);
			sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

			final int operations = DND.DROP_COPY | DND.DROP_MOVE; //
			final Transfer[] transferTypes = new Transfer[] { TextTransfer
					.getInstance() };

			Composite leftSideComposite = new Composite(sashForm, SWT.NONE);
			leftSideComposite.setLayout(new BorderLayout(0, 0));

			sourceAndTargetServercomposite = new SashForm(leftSideComposite, SWT.SMOOTH | SWT.VERTICAL);
			sourceAndTargetServercomposite.setLayout(new FillLayout(SWT.VERTICAL));
			// treeComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
			// true, 1, 1));
			// treeComp.setLayout(new GridLayout(1, false));
			sourceAndTargetServercomposite.addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent e) {
					weights = sourceAndTargetServercomposite.getWeights();
				}
			});

			targetServercomposite = new Composite(sourceAndTargetServercomposite, SWT.NONE);
			targetServercomposite.setLayout(new BorderLayout(0, 0));

			sourceServerComposite = new Composite(sourceAndTargetServercomposite, SWT.NONE);
			sourceServerComposite.setLayout(new BorderLayout(0, 0));
			sourceServerComposite.addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent e) {
					sourceServerDisposed = true;
				}
			});
			sourceTreeComp = new Composite(sourceServerComposite, SWT.NONE);
			sourceTreeComp.setLayoutData(BorderLayout.CENTER);
			sourceTreeComp.setLayout(new BorderLayout(0, 0));
			Label lblNewLabel_1_1_1 = new Label(sourceTreeComp, SWT.NONE);
			lblNewLabel_1_1_1.setLayoutData(BorderLayout.NORTH);
			lblNewLabel_1_1_1.setText(Messages.ServerView_SourceServer);

			sourceServerViewer = new TreeViewer(sourceTreeComp, SWT.MULTI
					| SWT.H_SCROLL | SWT.V_SCROLL); // parent
			sourceServerViewer.addDropSupport(operations, transferTypes,
					new ServerDropTargetListener(sourceServerViewer));

			sourceServerViewer.getTree().addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					importDB();
				}

				@Override
				public void mouseDown(MouseEvent e) {
				}

				@Override
				public void mouseUp(MouseEvent e) {
				}
			});
			sourceServerViewer
			.setContentProvider(new ServerSourceContentProvider());
			sourceServerViewer.setLabelProvider(new ServerImportDBLabelProvider());
			sourceServerViewer.setInput(new ServerImportDBModel());
			sourceServerViewer.setAutoExpandLevel(1);
			sourceServerViewer.setSorter(new ViewerSorter());
			
			/**
			 * Menu for the source servers
			 */

			layoutSourceServerContextMenu();

			/**
			 * Import Button Composite
			 */
			Label targetServerLabel = new Label(targetServercomposite, SWT.NONE);
			targetServerLabel.setLayoutData(BorderLayout.NORTH);
//			targetServerLabel.setText(Messages.ServerView_TargetServer + " " + " NEW NEW NEW");
			targetServerLabel.setText(Messages.ServerView_TargetServer);
			targetServerViewer = new TreeViewer(targetServercomposite, SWT.MULTI); // parent
			Tree targetServerTree = targetServerViewer.getTree();
			targetServerTree.setLayoutData(BorderLayout.CENTER);
			targetServerViewer.setContentProvider(new ServerContentProvider());
			targetServerViewer.setLabelProvider(new ServerLabelProvider());
			targetServerViewer.setInput(new ServerModel());
//			targetServerViewer.setAutoExpandLevel(2);
			
			targetServerViewer.setSorter(new ViewerSorter());

			targetServerViewer.getTree().addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
				@Override
				public void widgetSelected(SelectionEvent e) {
					
					TreeItem selectedItem = (TreeItem) e.item;
					String selectedItemString = selectedItem.getText();
					if (ServerList.isServer(selectedItemString)) {
						String currentServerID = selectedItem.getText();
						Server server = ServerList.getTargetServers().get(
								currentServerID);

						labelIpCurrent.setText(server.getIp());
						labelDBUserCurrent.setText(""); 
						labelNameCurrent.setText(server.getName());
						lblObservationsCurrent.setText(""); 
						lblPatientsCurrent.setText(""); 
					} else {
						String parentServer = selectedItem.getParentItem()
								.getText();
						Server server = ServerList.getTargetServers().get(
								parentServer);
						labelIpCurrent.setText(server.getIp());
						labelDBUserCurrent.setText(selectedItemString);
						labelNameCurrent.setText(server.getName());
						lblObservationsCurrent.setText("..."); 
						lblPatientsCurrent.setText("..."); 
						Bundle bundle = Activator.getDefault().getBundle();
						Path imgLoadingPath = new Path("/images/loading.png"); 
						URL url = FileLocator.find(bundle, imgLoadingPath,
								Collections.EMPTY_MAP);

						try {
							URL fileUrl = FileLocator.toFileURL(url);
							File imgLoadingFile = new File(fileUrl.getPath());
							Image imgLoading = new Image(parent.getDisplay(),
									imgLoadingFile.getAbsolutePath());

							//Displays "Loading..." while DB loads.
							final Shell loadingShell = new Shell(SWT.ON_TOP);
							loadingShell.setSize(imgLoading.getBounds().width,imgLoading.getBounds().height);
							loadingShell.setLocation(Display.getCurrent().getCursorLocation().x,Display.getCurrent().getCursorLocation().y);
							loadingShell.setBackgroundImage(imgLoading);
							loadingShell.open();
							lblObservationsCurrent.setText(server
									.getConcepts(selectedItemString));
							lblPatientsCurrent.setText(server
									.getPatients(selectedItemString));
							loadingShell.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						
					}
				}
			});

			targetServerViewer.getTree().addMouseListener(new MouseListener() {
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					if (targetServerViewer.getTree().getSelectionCount() > 0) {
						if (targetServerViewer.getTree().getSelection()[0].getExpanded()) {
							targetServerViewer.getTree().getSelection()[0]
									.setExpanded(false);
						} else {
							targetServerViewer.getTree().getSelection()[0]
									.setExpanded(true);
						}
					}
					targetServerViewer.refresh();
				}

				@Override
				public void mouseDown(MouseEvent e) {
				}

				@Override
				public void mouseUp(MouseEvent e) {
				}
			});
			mainMenu = new Menu(targetServerViewer.getTree());

//			if (targetServerViewer.getTree().getItemCount()>0) {
//			targetServerViewer.getTree().getItem(0)
//					.setExpanded(true);
//			}
			targetServerViewer.refresh();

			targetServerViewer.getTree().setMenu(mainMenu);
			targetServerViewer.addDragSupport(operations, transferTypes,
					new ServerDragSourceListener(targetServerViewer));
			final MenuItem loadOntologyMenuItem = new MenuItem(mainMenu, SWT.PUSH);
			loadOntologyMenuItem.setText("Load Ontology");
			loadOntologyMenuItem.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					loadOntology();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {

				}
			});
			Menu importMenu = new Menu(mainMenu);

			final MenuItem importMenuItem = new MenuItem(mainMenu, SWT.CASCADE);
			importMenuItem.setMenu(importMenu);

			importMenuItem.setText(Messages.ServerView_ImportData);
			new MenuItem(mainMenu, SWT.SEPARATOR);

			MenuItem importODMMenuItem = new MenuItem(importMenu, SWT.PUSH);
			importODMMenuItem.setText(Messages.ServerView_ImportODM);
			importODMMenuItem.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					importODM();
				}
			});

			final MenuItem truncateMenuItem = new MenuItem(mainMenu, SWT.PUSH);
			truncateMenuItem.setText(Messages.ServerView_TruncateProject);
			truncateMenuItem.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					boolean result = MessageDialog.openConfirm(parent.getShell(),
							Messages.ServerView_TruncateProjectQuestion,
							Messages.ServerView_TruncateProjectConfirmText);
					if(result) {
						IDRTImport.runTruncateProject(ServerList.getTargetServers().get(labelNameCurrent.getText()),labelDBUserCurrent.getText());
					}
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {

				}
			});
			
			

			final MenuItem importTermsMenuItem = new MenuItem(mainMenu, SWT.PUSH);
			importTermsMenuItem.setText(Messages.ServerView_ImportAndMapST);
			importTermsMenuItem.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					boolean result = MessageDialog.openConfirm(parent.getShell(),
							Messages.ServerView_ImportAndMapSTConfirm,
							Messages.ServerView_ImportAndMapSTConfirmText);
					if(result) {
						IDRTImport.runImportST(ServerList.getTargetServers().get(labelNameCurrent.getText()),labelDBUserCurrent.getText());
					}
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {

				}
			});

			MenuItem importCSVMenuItem = new MenuItem(importMenu, SWT.PUSH);
			importCSVMenuItem.setText(Messages.ServerView_ImportCSV);
			importCSVMenuItem.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					importCSV();
				}
			});

			MenuItem importDBMenuItem = new MenuItem(importMenu, SWT.PUSH);
			importDBMenuItem.setText(Messages.ServerView_ImportDB);
			importDBMenuItem.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					importDB();
				}
			});

			MenuItem importP21MenuItem = new MenuItem(importMenu, SWT.PUSH);
			importP21MenuItem.setText(Messages.ServerView_ImportP21);
			importP21MenuItem.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					importP21();
				}
			});
			
			
			MenuItem removeLocksMenuItem = new MenuItem(mainMenu, SWT.NONE);
			removeLocksMenuItem.setText("Remove Locks");
			removeLocksMenuItem.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					boolean result = MessageDialog.openConfirm(parent.getShell(),
							"Remove Locks?",
							"Do you really want to remove ALL locks from the database?\nSYSDBA REQUIRED!");
					if(result) {
						IDRTImport.runRemoveLocks(ServerList.getTargetServers().get(labelNameCurrent.getText()),labelDBUserCurrent.getText());
					}
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {

				}
			});

			new MenuItem(mainMenu, SWT.SEPARATOR);
			MenuItem addServerMenuItem = new MenuItem(mainMenu, SWT.PUSH);
			addServerMenuItem.setText(Messages.ServerView_AddServer);
			addServerMenuItem.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					addTargetServer();
				}
			});

			final MenuItem editServerMenuItem = new MenuItem(mainMenu, SWT.PUSH);
			editServerMenuItem.setText(Messages.ServerView_EditServer);
			editServerMenuItem.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					editTargetServer();
				}
			});

			final MenuItem deleteServerMenuItem = new MenuItem(mainMenu, SWT.PUSH);
			deleteServerMenuItem.setText(Messages.ServerView_DeleteServer);
			deleteServerMenuItem.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					deleteTargetServer();
				}
			});
			new MenuItem(mainMenu, SWT.SEPARATOR);

			final MenuItem exportServerMenuItem = new MenuItem(mainMenu, SWT.PUSH);
			exportServerMenuItem.setText(Messages.ServerView_ExportServer);
			exportServerMenuItem.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					exportTargetServer();
				}
			});
			MenuItem importServerMenuItem = new MenuItem(mainMenu, SWT.PUSH);
			importServerMenuItem.setText(Messages.ServerView_ImportServer);
			importServerMenuItem.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					importTargetServer();
				}
			});

			new MenuItem(mainMenu, SWT.SEPARATOR);
			MenuItem refreshServerMenuItem = new MenuItem(mainMenu, SWT.PUSH);
			refreshServerMenuItem.setText(Messages.ServerView_Refresh);
			refreshServerMenuItem.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					refresh();
				}
			});

			
			// TODO REMOVE COMMENTATION FOR ADMINISTRATION
//						new MenuItem(mainMenu, SWT.SEPARATOR);
//						MenuItem adminMenuItem = new MenuItem(mainMenu, SWT.PUSH);
//						adminMenuItem.setText("Administration");
//						adminMenuItem.addSelectionListener(new SelectionListener() {
//							@Override
//							public void widgetSelected(SelectionEvent e) {
//								adminTargetServer();
//							}
//			
//							@Override
//							public void widgetDefaultSelected(SelectionEvent e) {
//			
//							}
//						});

			/*
			 * Dis-/Enables the mainMenu items.
			 */
			mainMenu.addListener(SWT.Show, new Listener() {
				@Override
				public void handleEvent(Event event) {
					if (targetServerViewer.getTree().getSelectionCount() > 0) {
						if (targetServerViewer.getTree().getSelection()[0].getParentItem() == null) {
							importMenuItem.setEnabled(false);
							truncateMenuItem.setEnabled(false);
							importTermsMenuItem.setEnabled(false);
							deleteServerMenuItem.setEnabled(true);
							editServerMenuItem.setEnabled(true);
							exportServerMenuItem.setEnabled(true);
						} else {
							deleteServerMenuItem.setEnabled(false);
							exportServerMenuItem.setEnabled(false);
							importMenuItem.setEnabled(true);
							truncateMenuItem.setEnabled(true);
							importTermsMenuItem.setEnabled(true);
						}
					} else {
						editServerMenuItem.setEnabled(false);
						deleteServerMenuItem.setEnabled(false);
						importMenuItem.setEnabled(false);
						truncateMenuItem.setEnabled(false);
						importTermsMenuItem.setEnabled(false);
						exportServerMenuItem.setEnabled(false);
					}
				}
			});

			sourceServerViewer = new TreeViewer(sourceTreeComp, SWT.MULTI
					| SWT.H_SCROLL | SWT.V_SCROLL); // parent
			sourceServerViewer.addDropSupport(operations, transferTypes,
					new ServerDropTargetListener(sourceServerViewer));
			sourceServerViewer.getTree().addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					importDB();
				}

				@Override
				public void mouseDown(MouseEvent e) {
				}

				@Override
				public void mouseUp(MouseEvent e) {
				}
			});
			sourceServerViewer.setContentProvider(new ServerSourceContentProvider());
			sourceServerViewer.setLabelProvider(new ServerImportDBLabelProvider());
			sourceServerViewer.setInput(new ServerImportDBModel());
			sourceServerViewer.setAutoExpandLevel(1);
			sourceServerViewer.setSorter(new ViewerSorter());
			importSourceServerMenu = new Menu(sourceServerViewer.getTree());

			/*
			 * SOURCE server menu
			 */
			MenuItem addSourceServerMenuItem = new MenuItem(importSourceServerMenu,
					SWT.PUSH);
			addSourceServerMenuItem.setText(Messages.ServerView_AddServer);
			addSourceServerMenuItem
			.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					addSourceServer();
				}
			});

			MenuItem editSourceServerMenuItem = new MenuItem(
					importSourceServerMenu, SWT.PUSH);
			editSourceServerMenuItem.setText(Messages.ServerView_EditServer);
			editSourceServerMenuItem
			.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					editSourceServer();
				}
			});

			MenuItem deleteSourceServerMenuItem = new MenuItem(
					importSourceServerMenu, SWT.PUSH);
			deleteSourceServerMenuItem.setText(Messages.ServerView_DeleteServer);
			deleteSourceServerMenuItem
			.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					deleteSourceServer();
				}
			});
			new MenuItem(importSourceServerMenu, SWT.SEPARATOR);

			MenuItem exportImportDBServerMenuItem = new MenuItem(
					importSourceServerMenu, SWT.PUSH);
			exportImportDBServerMenuItem.setText(Messages.ServerView_ExportServer);
			exportImportDBServerMenuItem
			.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					exportSourceServer();
				}
			});
			MenuItem importSourceServerMenuItem = new MenuItem(
					importSourceServerMenu, SWT.PUSH);
			importSourceServerMenuItem.setText(Messages.ServerView_ImportServer);
			importSourceServerMenuItem
			.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					importSourceServer();
				}
			});
			new MenuItem(importSourceServerMenu, SWT.SEPARATOR);
			MenuItem refreshSourceServerMenuItem = new MenuItem(
					importSourceServerMenu, SWT.PUSH);
			refreshSourceServerMenuItem.setText(Messages.ServerView_Refresh);
			refreshSourceServerMenuItem
			.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					refresh();
				}
			});

			sourceServerViewer.getTree().setMenu(importSourceServerMenu);

			//Load weights from disc
			int x = Integer.parseInt(defaultProps.getProperty("sashx")); 
			int y = Integer.parseInt(defaultProps.getProperty("sashy")); 
			sourceAndTargetServercomposite.setWeights(new int[] { x, y });
			weights = sourceAndTargetServercomposite.getWeights();

			if (defaultProps.getProperty("showSourceServer").equals("false")) {  
				sourceServerDisposed = true;
				sourceServerComposite.dispose();
			}

			Composite showHideSourceServerButtonComp = new Composite(leftSideComposite, SWT.NONE);
			showHideSourceServerButtonComp.setLayoutData(BorderLayout.SOUTH);
			showHideSourceServerButtonComp.setLayout(new FillLayout(SWT.HORIZONTAL));
			final Button btnShowAndHideSourceServer = new Button(showHideSourceServerButtonComp, SWT.NONE);
			btnShowAndHideSourceServer.setText(Messages.ServerView_ShowSourceServer);
			btnShowAndHideSourceServer.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					if (sourceServerComposite.isDisposed()) {
						sourceServerDisposed = false;
						sourceServerComposite = new Composite(sourceAndTargetServercomposite, SWT.NONE);
						sourceServerComposite.setLayout(new BorderLayout(0, 0));
						sourceTreeComp = new Composite(sourceServerComposite,
								SWT.NONE);
						sourceTreeComp.setLayoutData(BorderLayout.CENTER);
						sourceTreeComp.setLayout(new BorderLayout(0, 0));
						Label labelSourceServer = new Label(sourceTreeComp,
								SWT.NONE);
						labelSourceServer.setLayoutData(BorderLayout.NORTH);
						labelSourceServer.setText(Messages.ServerView_SourceServer);

						sourceServerViewer = new TreeViewer(sourceTreeComp,
								SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL); // parent
						sourceServerViewer.addDropSupport(operations,
								transferTypes, new ServerDropTargetListener(
										sourceServerViewer));
						sourceServerViewer.getTree().addMouseListener(
								new MouseListener() {

									@Override
									public void mouseDoubleClick(MouseEvent e) {
										importDB();
									}

									@Override
									public void mouseDown(MouseEvent e) {
									}

									@Override
									public void mouseUp(MouseEvent e) {
									}
								});
						sourceServerViewer
						.setContentProvider(new ServerSourceContentProvider());
						sourceServerViewer
						.setLabelProvider(new ServerImportDBLabelProvider());
						sourceServerViewer.setInput(new ServerImportDBModel());
						sourceServerViewer.setAutoExpandLevel(1);
						sourceServerViewer.setSorter(new ViewerSorter());

						layoutSourceServerContextMenu();
						sourceServerViewer.getTree().setMenu(importSourceServerMenu);
						sourceAndTargetServercomposite.setWeights(weights);
						btnShowAndHideSourceServer.setText(Messages.ServerView_HideSourceServer);
					} else {
						sourceServerDisposed = true;
						btnShowAndHideSourceServer.setText(Messages.ServerView_ShowSourceServer);
						weights = sourceAndTargetServercomposite.getWeights();
						sourceServerComposite.dispose();
					}
					sourceAndTargetServercomposite.layout();
				}
			});

			SashForm infoboxSash = new SashForm(sashForm, SWT.VERTICAL);
			infoboxSash.setLayout(new FillLayout(SWT.VERTICAL));
			//			 infoBoxComposite = new Composite(infoboxSash, SWT.NONE);
			//			 infoBoxComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
			labelNameComposite = new Composite(infoboxSash, SWT.NONE);
			labelNameComposite.setLayout(new GridLayout(2, false));
			//			 labelNameComposite.setLayout(new GridData(2, false));
			labelNameComposite.pack();
			labelName = new Label(labelNameComposite, SWT.SHADOW_IN);
			labelName.setText(Messages.ServerView_Name);

			labelNameCurrent = new Label(labelNameComposite, SWT.SHADOW_IN);
			labelNameCurrent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
			labelNameCurrent.setText("");

			labelIp = new Label(labelNameComposite, SWT.SHADOW_IN);
			labelIp.setText(Messages.ServerView_IP);

			labelIpCurrent = new Label(labelNameComposite, SWT.SHADOW_IN);
			labelIpCurrent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
			labelIpCurrent.setText("");

			labelDBUser = new Label(labelNameComposite, SWT.SHADOW_IN);
			labelDBUser.setAlignment(SWT.LEFT);
			labelDBUser.setText(Messages.ServerView_Project);

			labelDBUserCurrent = new Label(labelNameComposite, SWT.SHADOW_IN);
			labelDBUserCurrent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
			labelDBUserCurrent.setText("");

			Label lblPatients = new Label(labelNameComposite, SWT.SHADOW_IN);
			lblPatients.setText(Messages.ServerView_Patients);

			lblPatientsCurrent = new Label(labelNameComposite, SWT.SHADOW_IN);
			lblPatientsCurrent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
			lblPatientsCurrent.setText("");

			Label lblObservations = new Label(labelNameComposite, SWT.SHADOW_IN);
			lblObservations.setText(Messages.ServerView_Observations);

			lblObservationsCurrent = new Label(labelNameComposite,
					SWT.SHADOW_IN);
			lblObservationsCurrent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
			lblObservationsCurrent.setText("");
			Composite sashForm_1 = new Composite(infoboxSash, SWT.NONE);
			sashForm_1.setLayout(new BorderLayout(0, 0));

			Composite progressComp = new Composite(sashForm_1, SWT.NONE);
			progressComp.setLayoutData(BorderLayout.NORTH);
			GridLayout gl_progressComp = new GridLayout(2, false);
			gl_progressComp.marginWidth = 0;
			progressComp.setLayout(gl_progressComp);

			//			Composite composite_11 = new Composite(progressComp, SWT.NONE);
			//			GridData gd_composite_11 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
			//			gd_composite_11.widthHint = 304;
			//			composite_11.setLayoutData(gd_composite_11);
			//			composite_11.setLayout(new GridLayout(2, false));
			//			GridData gd_composite_11 = new GridData(SWT.LEFT, SWT.FILL, true,
			//					false, 1, 1);
			//			gd_composite_11.minimumHeight = 100;
			//			gd_composite_11.minimumWidth = 200;
			//			composite_11.setLayoutData(gd_composite_11);
			//			composite_11.setLayout(new GridLayout(2, false));

			progressLabelTop = new Label(progressComp, SWT.NONE);
			//			gd_progressLabelTop.widthHint = 213;
			progressLabelTop.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
			//			progressLabelTop.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
			//					true, true, 1, 1));
			progressLabelTop.setText("");
			progressLabelBottom = new Label(progressComp, SWT.NONE);
			progressLabelBottom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
			//						progressLabelBottom.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
			//								true, true, 1, 1));
			progressLabelBottom.setText("");

			//			Composite composite_2 = new Composite(progressComp, SWT.NONE);
			//			composite_2.setLayout(new GridLayout(1, false));
			//			composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
			//					true, 1, 1));

			progressBar = new ProgressBar(progressComp, SWT.SMOOTH);
			//			gd_progressBar.heightHint = 30;
			progressBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					true, 1, 1));

			//			composite_11.addControlListener(new ControlListener() {
			//				
			//				@Override
			//				public void controlResized(ControlEvent e) {
			//					System.out.println("sash "+sashForm_1.getBounds().toString());
			//					gd_btnStop.widthHint = sashForm_1.getBounds().width/3;
			//					gd_btnStop.heightHint = sashForm_1.getBounds().height/3;
			//					btnStop.getParent().layout();
			////					btnStop.setBounds(sashForm_1.getBounds());
			//					btnStop.update();
			//					btnStop.redraw();
			//					btnStop.pack();
			//				
			//				
			//					
			//					System.out.println("btn: " + btnStop.getBounds().toString());
			//				}
			//				
			//				@Override
			//				public void controlMoved(ControlEvent e) {
			//					
			//				}
			//			});
			btnStop = new Button(progressComp, SWT.PUSH);
			btnStop.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.IDRTImportTool", "images/terminate_co.gif"));
			btnStop.setToolTipText("Terminate Import!");
			btnStop.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
			btnStop.setEnabled(false);
			btnStop.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					if (importStarted()) {
						progressBar.setSelection(0);
						progressBar2.setSelection(0);
						subProgressLabel.setText("");
						progressLabelTop.setText(""); 
						progressLabelBottom.setText(Messages.ServerView_ImportStopped);
						progressLabelBottom.setForeground(SWTResourceManager
								.getColor(SWT.COLOR_RED));
						StatusListener.interrupt();
						btnStop.setEnabled(false);
					} else {
						System.out.println("nothing started"); 
					}
				}
			});
			new Label(progressComp, SWT.NONE);
			new Label(progressComp, SWT.NONE);

			subProgressLabel = new Label(progressComp, SWT.NONE);
			subProgressLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
			subProgressLabel.setText("");
			progressBar2 = new ProgressBar(progressComp, SWT.SMOOTH);

			GridData gd_progressBar2 = new GridData(SWT.FILL, SWT.FILL, true,
					true, 1, 1);
			gd_progressBar2.heightHint = 20;
			progressBar2.setLayoutData(gd_progressBar2);
			new Label(progressComp, SWT.NONE);
			Composite composite = new Composite(sashForm_1, SWT.NONE);
			composite.setLayoutData(BorderLayout.CENTER);
			composite.setLayout(new BorderLayout(0, 0));

			Label lblLog = new Label(composite, SWT.NONE);
			lblLog.setLayoutData(BorderLayout.NORTH);
			lblLog.setText(Messages.ServerView_Log);

			logComposite = new Composite(composite, SWT.NONE);
			logComposite.setLayoutData(BorderLayout.CENTER);
			logComposite.setBounds(0, 0, 64, 64);
			logComposite.setLayout(new BorderLayout(0, 0));

			//			logTable = new Table(logComposite, SWT.FULL_SELECTION
			//					| SWT.HIDE_SELECTION);
			//			logTable.setLayoutData(BorderLayout.CENTER);
			//			logTable.setHeaderVisible(false);
			//			logTable.setLinesVisible(false);
			//
			//			logTblClmTime = new TableColumn(logTable, SWT.NONE);
			//			logTblClmTime.setWidth(100);
			//			logTblClmTime.setText(Messages.ServerView_Time);
			//
			//			logTblClmMessage = new TableColumn(logTable, SWT.LEFT);
			//			logTblClmMessage.setWidth(100);
			//			logTblClmMessage.setText(Messages.ServerView_Message);

			Composite composite_1 = new Composite(logComposite, SWT.NONE);
			composite_1.setLayoutData(BorderLayout.SOUTH);
			//			composite_1.setLayoutData(BorderLayout.SOUTH);
			composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));

			Button btnClearLog = new Button(composite_1, SWT.NONE);
			btnClearLog.setText(Messages.ServerView_ClearLog);
			btnClearLog.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					text.setText("");
				}
			});
			Button btnExportLog = new Button(composite_1, SWT.CENTER);
			btnExportLog.setText(Messages.ServerView_ExportLog);

			Composite composite_3 = new Composite(logComposite,  SWT.BORDER);
			composite_3.setLayoutData(BorderLayout.CENTER);
			composite_3.setLayout(new FillLayout(SWT.HORIZONTAL));

			text = new Text(composite_3, SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
			text.addListener(SWT.Modify, new Listener(){
				public void handleEvent(Event e){
					text.setTopIndex(0);
					text.getParent().layout();
				}
			});
			text.setText("");
			infoboxSash.setWeights(new int[] {2, 7});
			btnExportLog.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						Bundle bundle = Activator.getDefault().getBundle();
						Path propPath = new Path("/cfg/Default.properties"); 
						URL url = FileLocator.find(bundle, propPath,
								Collections.EMPTY_MAP);
						URL fileUrl = FileLocator.toFileURL(url);
						File properties = new File(fileUrl.getPath());
						defaultProps.load(new FileReader(properties));

						String fileName = defaultProps.getProperty("log"); 
						File log = new File(fileName);

						Log.addLog(0,
								Messages.ServerView_ExportLogTo + log.getAbsolutePath());

						FileWriter outFile = new FileWriter(log);
						PrintWriter out = new PrintWriter(outFile);
						out.print(text.getText());
						//						for (int i = 0; i < logTable.getItemCount(); i++) {
						//							out.println(logTable.getItems()[i].getText(0) + "|" 
						//									+ logTable.getItems()[i].getText(1));
						//						}
						out.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
			});
			Log.addLog(0, Messages.ServerView_IDRTImportStarted);
			sashForm.setWeights(new int[] { 2, 2 });
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
	public static void btnStopSetEnabled(final boolean enabled) {
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				btnStop.setEnabled(enabled);  
			}
		});

	}

	@Override
	public void dispose() {
		try {
			Bundle bundle = Activator.getDefault().getBundle();
			Path propPath = new Path("/cfg/Default.properties"); 
			URL url = FileLocator.find(bundle, propPath, Collections.EMPTY_MAP);

			URL fileUrl = FileLocator.toFileURL(url);
			File properties = new File(fileUrl.getPath());
			defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));

			if (sourceServerDisposed) {
				defaultProps.setProperty("showSourceServer", "false");  
			} else {
				defaultProps.setProperty("showSourceServer", "true");  
			}

			if (weights != null) {
				if (weights.length > 1) {
					defaultProps.setProperty("sashx", "" + weights[0]);  
					defaultProps.setProperty("sashy", "" + weights[1]);  
				} else {
					defaultProps.setProperty("sashx", "" + weights[0]);  
				}
			}
			defaultProps.store(new FileOutputStream(properties), ""); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.dispose();
	}

	private void layoutSourceServerContextMenu() {
		importSourceServerMenu = new Menu(sourceServerViewer.getTree());

		MenuItem addSourceServerMenuItem = new MenuItem(importSourceServerMenu,
				SWT.PUSH);
		addSourceServerMenuItem.setText(Messages.ServerView_AddServer);
		addSourceServerMenuItem.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("add server clicked"); 
				addSourceServer();
			}
		});

		MenuItem editSourceServerMenuItem = new MenuItem(importSourceServerMenu,
				SWT.PUSH);
		editSourceServerMenuItem.setText(Messages.ServerView_EditServer);
		editSourceServerMenuItem
		.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("edit server clicked"); 
				editSourceServer();
			}
		});

		MenuItem deleteSourceServerMenuItem = new MenuItem(importSourceServerMenu,
				SWT.PUSH);
		deleteSourceServerMenuItem.setText(Messages.ServerView_DeleteServer);
		deleteSourceServerMenuItem
		.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("delete server clicked"); 
				deleteSourceServer();
				if (targetServerViewer.getTree().getChildren() == null) {
					System.out.println("empty tree"); 
				}
			}
		});
		new MenuItem(importSourceServerMenu, SWT.SEPARATOR);

		MenuItem exportSourceServerMenuItem = new MenuItem(importSourceServerMenu,
				SWT.PUSH);
		exportSourceServerMenuItem.setText(Messages.ServerView_ExportServer);
		exportSourceServerMenuItem
		.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				exportSourceServer();
			}
		});
		MenuItem importSourceServerMenuItem = new MenuItem(importSourceServerMenu,
				SWT.PUSH);
		importSourceServerMenuItem.setText(Messages.ServerView_ImportServer);
		importSourceServerMenuItem
		.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				importSourceServer();
			}
		});
		new MenuItem(importSourceServerMenu, SWT.SEPARATOR);
		MenuItem refreshSourceServerMenuItem = new MenuItem(importSourceServerMenu,
				SWT.PUSH);
		refreshSourceServerMenuItem.setText(Messages.ServerView_Refresh);
		refreshSourceServerMenuItem
		.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				refresh();
			}
		});
	}

	@Override
	public void setFocus() {
	}
	/**
	 * 
	 */
	/**
	 * 
	 */
	
}

class ViewContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public Object[] getElements(Object parent) {
		if (parent instanceof Object[]) {
			return (Object[]) parent;
		}
		return new Object[0];
	}

	@Override
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}
}

class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object obj, int index) {
		return getImage(obj);
	}

	@Override
	public String getColumnText(Object obj, int index) {
		return getText(obj);
	}

	@Override
	public Image getImage(Object obj) {
		return PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_OBJ_ELEMENT);
	}
}