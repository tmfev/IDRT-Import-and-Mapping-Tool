package de.umg.mi.idrt.idrtimporttool.importidrt;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.prefs.BackingStoreException;

import org.eclipse.core.commands.Command;
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
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;
import org.osgi.framework.Bundle;

import swing2swt.layout.BorderLayout;
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
import de.umg.mi.idrt.importtool.misc.FileHandler;

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
	private Composite logButtonAndTextComposite;

	private static File log;
	private static String logString;
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
	private static Text logText;
	/**
	 * @param mainPath
	 */
	private void createDirs(File mainPath) {
		File misc = new File(mainPath.getAbsolutePath()+"/misc/");

		if (!misc.exists()) 
			misc.mkdir();

		File cfg = new File(mainPath.getAbsolutePath()+"/cfg/");
		if (!cfg.exists())
			cfg.mkdir();

		File miscInput = new File(mainPath.getAbsolutePath()+"/misc/input/");
		if (!miscInput.exists()) 
			miscInput.mkdir();

		File miscOutput = new File(mainPath.getAbsolutePath()+"/misc/output/");
		if (!miscOutput.exists())
			miscOutput.mkdir();

		File miscTmp = new File(mainPath.getAbsolutePath()+"/misc/tmp/");
		if (!miscTmp.exists())
			miscTmp.mkdir();

		File log = new File(mainPath.getAbsolutePath()+"/log/");
		if (!log.exists())
			log.mkdir();

		File logLog = new File(mainPath.getAbsolutePath()+"/log/log.log");
		if (!logLog.exists())
			try {
				logLog.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		File server = new File(mainPath.getAbsolutePath()+"/cfg/server");
		if (!server.exists()) {
			File defServer = new File(mainPath.getAbsolutePath()+"/COPY_DEFAULT_CFG/server");
			System.out.println(defServer.toPath().toString() + " " + server.toPath().toString());
			copyFile(defServer, server);
		}

		File serverImportDB = new File(mainPath.getAbsolutePath()+"/cfg/serverImportDB");
		if (!serverImportDB.exists()) {
			File defserverImportDB = new File(mainPath.getAbsolutePath()+"/COPY_DEFAULT_CFG/serverImportDB");
			copyFile(defserverImportDB, serverImportDB);
		}

		File properties = new File(mainPath.getAbsolutePath()+"/cfg/Default.properties");
		if (!properties.exists()) {
			File defproperties = new File(mainPath.getAbsolutePath()+"/COPY_DEFAULT_CFG/Default.properties");
			copyFile(defproperties,properties);
		}
	}
	public static void copyFile(File inputFile, File outputFile) {
		try {
			FileReader in = new FileReader(inputFile);

			FileWriter out = new FileWriter(outputFile);
			int c;

			while ((c = in.read()) != -1) {
				out.write(c);
			}
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void createPartControl(final Composite parentComp) {
		try {
			Bundle bundle = Activator.getDefault().getBundle();
			URL mainURL = FileLocator.find(bundle, new Path(""), null);
			URL mainFileUrl = FileLocator.toFileURL(mainURL);
			File mainPath = new File(mainFileUrl.getPath());	
			System.out.println("creating paths");
			createDirs(mainPath);
			System.out.println("done");


			log = new File(mainPath.getAbsolutePath()+"/log/log.log");	
			logString = "";

			logString=readLogFromDisc(25);

			parent = new Composite(parentComp, SWT.NONE);
			parent.getShell().setMinimumSize(500, 200);
			System.out.println("Current Version: "
					+ Activator.getDefault().getBundle().getVersion()
					.toString());

			// Remove tmp files

			File folder = new File(mainPath.getAbsolutePath()+"/misc/tmp/");
			File[] listOfFiles = folder.listFiles();

			for (File listOfFile : listOfFiles) {
				if (listOfFile.getName().endsWith(".tmp") && !listOfFile.getName().equals("ph") ) { 
					listOfFile.delete();
				}
			}
			File inputFolder = new File(mainPath.getAbsolutePath()+"/misc/input/");
			File[] listOfInputFiles = inputFolder.listFiles();

			for (File listOfInputFile : listOfInputFiles) {
				if (listOfInputFile.getName().endsWith(".csv")) { 
					listOfInputFile.delete();
				}
			}
			File outputFolder = new File(mainPath.getAbsolutePath()+"/misc/output/");
			File[] listOfOutputFiles = outputFolder.listFiles();

			for (File listOfOutputFile : listOfOutputFiles) {
				if (listOfOutputFile.getName().endsWith(".csv")||listOfOutputFile.getName().endsWith(".xml")) { 
					listOfOutputFile.delete();
				}
			}
			properties = new File(mainPath.getAbsolutePath()+"/cfg/Default.properties");
			System.out.println(properties.getAbsolutePath());

			defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));
			boolean sysLog = ((defaultProps.getProperty("sysoLog") 
					.equals("true")) ? true : false); 
			String logdir = defaultProps.getProperty("sysoLogLoc"); 
			if (sysLog && (logdir != null)) {

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
			sourceServerViewer.getTree().setData("org.eclipse.swtbot.widget.key","sourceServerTree");
			/**
			 * Menu for the source servers
			 */

			layoutSourceServerContextMenu();

			/**
			 * Import Button Composite
			 */
			Label targetServerLabel = new Label(targetServercomposite, SWT.NONE);
			targetServerLabel.setLayoutData(BorderLayout.NORTH);
			targetServerLabel.setText(Messages.ServerView_TargetServer);
			targetServerViewer = new TreeViewer(targetServercomposite, SWT.MULTI); // parent
			targetServerViewer.getTree().setData("org.eclipse.swtbot.widget.key","targetServerTree");
			
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
					final String selectedItemString = selectedItem.getText();
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
						final Server server = ServerList.getTargetServers().get(
								parentServer);
						labelIpCurrent.setText(server.getIp());
						labelDBUserCurrent.setText(selectedItemString);
						labelNameCurrent.setText(server.getName());
						lblObservationsCurrent.setText("..."); 
						lblPatientsCurrent.setText("..."); 


						//Displays "Loading..." while DB loads.

						new Thread(new Runnable() {

							@Override
							public void run() {
								File imgLoadingFile = FileHandler.getBundleFile("/images/loading.png");
								Image imgLoading = new Image(parent.getDisplay(),
										imgLoadingFile.getAbsolutePath());
								final Shell loadingShell = new Shell(SWT.NO_TRIM ); //SWT.ON_TOP
								loadingShell.setSize(imgLoading.getBounds().width,imgLoading.getBounds().height);
								loadingShell.setLocation(Display.getCurrent().getCursorLocation().x,Display.getCurrent().getCursorLocation().y);
								loadingShell.setBackgroundImage(imgLoading);
								loadingShell.open();
								lblObservationsCurrent.setText(server
										.getConcepts(selectedItemString));
								lblPatientsCurrent.setText(server
										.getPatients(selectedItemString));
								loadingShell.close();
							}
						}).run();


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

			targetServerViewer.refresh();

			targetServerViewer.getTree().setMenu(mainMenu);
			targetServerViewer.addDragSupport(operations, transferTypes,
					new ServerDragSourceListener(targetServerViewer));

			/**
			 * Check if Load Ontology Command is enabled
			 */
			ICommandService commandService = (ICommandService) PlatformUI
					.getWorkbench().getActiveWorkbenchWindow().getService(
							ICommandService.class);
			final Command command = commandService
					.getCommand("edu.goettingen.i2b2.importtool.OntologyEditorLoad");


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

			if (!command.isEnabled()) {
				loadOntologyMenuItem.setEnabled(false);
				loadOntologyMenuItem.setText("Load Ontology (IOE only)"); 
			}

			new MenuItem(mainMenu, SWT.SEPARATOR);
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
					boolean result = MessageDialog.openConfirm(Application.getShell(),
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
			new MenuItem(importMenu, SWT.SEPARATOR);
			final MenuItem importTermsMenuItem = new MenuItem(importMenu, SWT.PUSH);
			importTermsMenuItem.setText(Messages.ServerView_ImportST);
			importTermsMenuItem.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					IDRTImport.runImportST_NoMap(ServerList.getTargetServers().get(labelNameCurrent.getText()),labelDBUserCurrent.getText());
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {

				}
			});
			MenuItem removeLocksMenuItem = new MenuItem(mainMenu, SWT.NONE);
			removeLocksMenuItem.setText("Remove Locks");
			removeLocksMenuItem.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					boolean result = MessageDialog.openConfirm(Application.getShell(),
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

			//			new MenuItem(mainMenu, SWT.SEPARATOR);
			//			MenuItem ontServerMenuItem = new MenuItem(mainMenu, SWT.PUSH);
			//			ontServerMenuItem.setText("loadont(beta)");
			//			ontServerMenuItem.addSelectionListener(new SelectionListener() {
			//				@Override
			//				public void widgetDefaultSelected(SelectionEvent e) {
			//				}
			//
			//				@Override
			//				public void widgetSelected(SelectionEvent e) {
			//					Server server = ServerList.getTargetServers().get(labelNameCurrent.getText());
			////					server.getOntology(labelDBUserCurrent.getText());
			//				}
			//			});


			//			 TODO REMOVE COMMENTATION FOR ADMINISTRATION
//			new MenuItem(mainMenu, SWT.SEPARATOR);
//			MenuItem adminMenuItem = new MenuItem(mainMenu, SWT.PUSH);
//			adminMenuItem.setText("Administration");
//			adminMenuItem.addSelectionListener(new SelectionListener() {
//				@Override
//				public void widgetSelected(SelectionEvent e) {
//					adminTargetServer();
//				}
//
//				@Override
//				public void widgetDefaultSelected(SelectionEvent e) {
//
//				}
//			});

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
							 loadOntologyMenuItem.setEnabled(false);
							 importTermsMenuItem.setEnabled(false);
							 deleteServerMenuItem.setEnabled(true);
							 editServerMenuItem.setEnabled(true);
							 exportServerMenuItem.setEnabled(true);
						 } else {
							 deleteServerMenuItem.setEnabled(false);
							 exportServerMenuItem.setEnabled(false);
							 importMenuItem.setEnabled(true);
							 truncateMenuItem.setEnabled(true);

							 if (!command.isEnabled()) {
								 loadOntologyMenuItem.setEnabled(false);
							 }
							 else {
								 loadOntologyMenuItem.setEnabled(true);
							 }
							 importTermsMenuItem.setEnabled(true);
						 }
					 } else {
						 editServerMenuItem.setEnabled(false);
						 deleteServerMenuItem.setEnabled(false);
						 importMenuItem.setEnabled(false);
						 truncateMenuItem.setEnabled(false);
						 loadOntologyMenuItem.setEnabled(false);
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
			 addSourceServerMenuItem.setText(Messages.ServerView_AddDatasourceServer);
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
			 editSourceServerMenuItem.setText(Messages.ServerView_EditDatasourceServer);
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
			 deleteSourceServerMenuItem.setText(Messages.ServerView_DeleteDatasourceServer);
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
			 exportImportDBServerMenuItem.setText(Messages.ServerView_ExportDatasourceServer);
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
			 importSourceServerMenuItem.setText(Messages.ServerView_ImportDatasourceServer);
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
			 else {
				 sourceServerDisposed=false;
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
								 SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
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

			 SashForm infoBoxSash = new SashForm(sashForm, SWT.VERTICAL);
			 infoBoxSash.setLayout(new FillLayout(SWT.VERTICAL));

			 labelNameComposite = new Composite(infoBoxSash, SWT.NONE);
			 labelNameComposite.setLayout(new GridLayout(2, false));
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
			 Composite sashForm_1 = new Composite(infoBoxSash, SWT.NONE);
			 sashForm_1.setLayout(new BorderLayout(0, 0));

			 Composite progressBarComp = new Composite(sashForm_1, SWT.NONE);
			 progressBarComp.setLayoutData(BorderLayout.NORTH);
			 GridLayout gl_progressComp = new GridLayout(2, false);
			 gl_progressComp.marginWidth = 0;
			 progressBarComp.setLayout(gl_progressComp);

			 progressLabelTop = new Label(progressBarComp, SWT.NONE);
			 progressLabelTop.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
			 progressLabelTop.setText("");
			 progressLabelBottom = new Label(progressBarComp, SWT.NONE);
			 progressLabelBottom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
			 progressLabelBottom.setText("");

			 progressBar = new ProgressBar(progressBarComp, SWT.SMOOTH);
			 progressBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					 true, 1, 1));

			 btnStop = new Button(progressBarComp, SWT.PUSH);
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
					 } 
				 }
			 });
			 new Label(progressBarComp, SWT.NONE);
			 new Label(progressBarComp, SWT.NONE);

			 subProgressLabel = new Label(progressBarComp, SWT.NONE);
			 subProgressLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
			 subProgressLabel.setText("");
			 progressBar2 = new ProgressBar(progressBarComp, SWT.SMOOTH);

			 GridData gd_progressBar2 = new GridData(SWT.FILL, SWT.FILL, true,
					 true, 1, 1);
			 gd_progressBar2.heightHint = 20;
			 progressBar2.setLayoutData(gd_progressBar2);
			 new Label(progressBarComp, SWT.NONE);
			 Composite logComposite = new Composite(sashForm_1, SWT.NONE);
			 logComposite.setLayoutData(BorderLayout.CENTER);
			 logComposite.setLayout(new BorderLayout(0, 0));

			 Label lblLog = new Label(logComposite, SWT.NONE);
			 lblLog.setLayoutData(BorderLayout.NORTH);
			 lblLog.setText(Messages.ServerView_Log);

			 logButtonAndTextComposite = new Composite(logComposite, SWT.NONE);
			 logButtonAndTextComposite.setLayoutData(BorderLayout.CENTER);
			 logButtonAndTextComposite.setBounds(0, 0, 64, 64);
			 logButtonAndTextComposite.setLayout(new BorderLayout(0, 0));

			 Composite logButtonComposite = new Composite(logButtonAndTextComposite, SWT.NONE);
			 logButtonComposite.setLayoutData(BorderLayout.SOUTH);
			 logButtonComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

			 Button btnClearLog = new Button(logButtonComposite, SWT.NONE);
			 btnClearLog.setText(Messages.ServerView_ClearLog);
			 btnClearLog.addSelectionListener(new SelectionListener() {
				 @Override
				 public void widgetDefaultSelected(SelectionEvent e) {
				 }

				 @Override
				 public void widgetSelected(SelectionEvent e) {
					 logString="";
					 writeLog();
					 logText.setText("");
				 }
			 });
			 Button btnExportLog = new Button(logButtonComposite, SWT.CENTER);
			 btnExportLog.setText(Messages.ServerView_ExportLog);

			 Composite logTextComposite = new Composite(logButtonAndTextComposite,  SWT.BORDER);
			 logTextComposite.setLayoutData(BorderLayout.CENTER);
			 logTextComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

			 logText = new Text(logTextComposite, SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
			 logText.addListener(SWT.Modify, new Listener(){
				 @Override
				 public void handleEvent(Event e){
					 logText.setTopIndex(0);
					 logText.getParent().layout();
				 }
			 });
			 logText.setText(logString);
			 infoBoxSash.setWeights(new int[] {2, 7});
			 btnExportLog.addSelectionListener(new SelectionListener() {
				 @Override
				 public void widgetDefaultSelected(SelectionEvent e) {
				 }

				 @Override
				 public void widgetSelected(SelectionEvent e) {
					 try {
						 File properties = FileHandler.getBundleFile("/cfg/Default.properties");
						 defaultProps.load(new FileReader(properties));

						 String fileName = defaultProps.getProperty("log"); 
						 File log = new File(fileName);

						 Log.addLog(0,Messages.ServerView_ExportLogTo + log.getAbsolutePath());

						 FileWriter outFile = new FileWriter(log);
						 PrintWriter out = new PrintWriter(outFile);
						 out.print(logText.getText());
						 out.close();
					 } catch (IOException e1) {
						 e1.printStackTrace();
					 }
				 }
			 });
			 //			Log.addLog(0, Messages.ServerView_IDRTImportStarted);
			 sashForm.setWeights(new int[] { 2, 2 });
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}


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

	public static Server getSelectedServer() {
		TreeViewer viewer = null;
		viewer = ServerView.getTargetServersViewer();
		if (viewer != null) {
			if (viewer.getTree().getSelectionCount() > 0) {
				if (viewer.getTree().getSelection()[0].getParentItem() != null) {
					String serverUniqueName = viewer.getTree().getSelection()[0]
							.getParentItem().getText();
					return ServerList.getTargetServers().get(serverUniqueName);
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

	public static void setLog(String newLog) {
		String DATE_FORMAT_NOW = "dd.MM.yyyy - HH:mm:ss";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		String time = sdf.format(cal.getTime());
		String old = logString;
		if (!old.isEmpty()) {
			logString =  time+"\t"+newLog + "\n" + old; 
			logText.setText("");
			logText.setText(logString);
		}
		else {
			logString = time+"\t"+newLog;
			logText.setText("");
			logText.setText(logString);
		}
		writeLog();
	}

	private static void writeLog() {
		BufferedWriter writer = null;
		try{
			writer = new BufferedWriter( new FileWriter(log));
			writer.write(logString);

		}
		catch ( IOException e){
		}
		finally{
			try {
				if ( writer != null)
					writer.close( );
			}
			catch ( IOException e) {
			}
		}
	}

	private static String readLogFromDisc(int lines){
		try {
			BufferedReader reader = new BufferedReader( new FileReader (log));

			String line = null;
			StringBuilder stringBuilder = new StringBuilder();
			String ls = System.getProperty("line.separator");
			int counter=0;
			while(counter<lines && (line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
				counter++;
			}
			return stringBuilder.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	//	public static void setLog(String time, String log) {
	//		String old = logText.getText();
	//		String newText;
	//		if (!old.isEmpty()) {
	//			 newText = time+"\t"+log + "\n"+ old;
	//			logText.setText(newText);
	//			logText.update();
	//			logText.getParent().layout();
	//			logText.getParent().redraw();
	//		}
	//		else {
	//			 newText = time+"\t"+log;
	//			logText.setText(newText);
	//			logText.update();
	//			logText.getParent().layout();
	//			logText.getParent().redraw();
	//		}
	//	}

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
			//			throw new RuntimeException("addServer.command not found"); 
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
			//			throw new RuntimeException("editServer.command not found"); 
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
			//			throw new RuntimeException("deleteServer.command not found"); 
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
			//			throw new RuntimeException("importServer.command not found"); 
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
			//			throw new RuntimeException("de.goettingen.i2b2.importtool.idrt.exportServer.command not found"); 
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
			//			throw new RuntimeException("de.goettingen.i2b2.importtool.idrt.addSourceServer not found"); 
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
			//			throw new RuntimeException("de.goettingen.i2b2.importtool.idrt.editSourceServer.command not found"); 
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
			//			throw new RuntimeException("de.goettingen.i2b2.importtool.idrt.deleteSourceServer.command not found"); 
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
			//			throw new RuntimeException("de.goettingen.i2b2.importtool.idrt.importSourceServer.command not found"); 
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
			//			throw new RuntimeException("de.goettingen.i2b2.importtool.idrt.exportSourceServer.command not found"); 
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
			//			throw new RuntimeException("adminServer.command not found"); 
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
			//			throw new RuntimeException("CSVImport.command not found"); 
		}
	}

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
			//			throw new RuntimeException("ODMImport.command not found"); 
		}
	}

	/**
	 * Starts the DB-Import.
	 */
	private void importDB() {
		IHandlerService handlerService = (IHandlerService) getSite()
				.getService(IHandlerService.class);
		try {
			handlerService.executeCommand(
					"de.goettingen.i2b2.importtool.DBImport", null); 

		} catch (Exception ex) {
			ex.printStackTrace();
			//			throw new RuntimeException("DBImport.command not found"); 
		}
	}

	/**
	 * Starts the P21-Import.
	 */
	private void importP21() {
		IHandlerService handlerService = (IHandlerService) getSite()
				.getService(IHandlerService.class);
		try {
			handlerService.executeCommand(
					"de.goettingen.i2b2.importtool.P21Import", null); 
		} catch (Exception ex) {
			ex.printStackTrace();
			//			throw new RuntimeException("DBImport.command not found"); 
		}
	}
	private void loadOntology() {
		IHandlerService handlerService = (IHandlerService) getSite()
				.getService(IHandlerService.class);
		try {

			//TODO 
			handlerService.executeCommand(
					"edu.goettingen.i2b2.importtool.OntologyEditorLoad", null); 
		} catch (Exception ex) {
			ex.printStackTrace();
			//			throw new RuntimeException("edu.goettingen.i2b2.importtool.OntologyEditorLoad.command not found"); 
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
			File properties = FileHandler.getBundleFile("/cfg/Default.properties");
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
		addSourceServerMenuItem.setText(Messages.ServerView_AddDatasourceServer);
		addSourceServerMenuItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				addSourceServer();
			}
		});

		MenuItem editSourceServerMenuItem = new MenuItem(importSourceServerMenu,
				SWT.PUSH);
		editSourceServerMenuItem.setText(Messages.ServerView_EditDatasourceServer);
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

		MenuItem deleteSourceServerMenuItem = new MenuItem(importSourceServerMenu,
				SWT.PUSH);
		deleteSourceServerMenuItem.setText(Messages.ServerView_DeleteDatasourceServer);
		deleteSourceServerMenuItem
		.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteSourceServer();
				if (targetServerViewer.getTree().getChildren() == null) {
				}
			}
		});
		new MenuItem(importSourceServerMenu, SWT.SEPARATOR);

		MenuItem exportSourceServerMenuItem = new MenuItem(importSourceServerMenu,
				SWT.PUSH);
		exportSourceServerMenuItem.setText(Messages.ServerView_ExportDatasourceServer);
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
		importSourceServerMenuItem.setText(Messages.ServerView_ImportDatasourceServer);
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