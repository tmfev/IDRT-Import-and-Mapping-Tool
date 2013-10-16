package de.umg.mi.idrt.ioe.view;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.TreeViewerEditor;
import org.eclipse.jface.viewers.TreeViewerFocusCellManager;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;

import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.ioe.Activator;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.I2B2ImportTool;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.SystemMessage;
import de.umg.mi.idrt.ioe.OntologyTree.NodeDragListener;
import de.umg.mi.idrt.ioe.OntologyTree.NodeDropListener;
import de.umg.mi.idrt.ioe.OntologyTree.NodeMoveDragListener;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.OntologyTree.TOSConnector;
import de.umg.mi.idrt.ioe.OntologyTree.TargetProject;
import de.umg.mi.idrt.ioe.OntologyTree.TargetProjects;
import de.umg.mi.idrt.ioe.OntologyTree.TreeContentProvider;
import de.umg.mi.idrt.ioe.commands.OntologyEditor.ReadTarget;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;

public class OntologyEditorView extends ViewPart {
	private static I2B2ImportTool i2b2ImportTool;
	/**
	 * @return the i2b2ImportTool
	 */
	public static I2B2ImportTool getI2b2ImportTool() {
		return i2b2ImportTool;
	}

	/**
	 * @param i2b2ImportTool the i2b2ImportTool to set
	 */
	public static void setI2b2ImportTool(I2B2ImportTool i2b2ImportTool) {
		OntologyEditorView.i2b2ImportTool = i2b2ImportTool;
	}

	private static boolean notYetSaved = true;
	/**
	 * @return the notYetSaved
	 */
	public static boolean isNotYetSaved() {
		return notYetSaved;
	}

	/**
	 * @param notYetSaved the notYetSaved to set
	 */
	public static void setNotYetSaved(boolean notYetSaved) {
		OntologyEditorView.notYetSaved = notYetSaved;
	}

	private static boolean init = false;
	private static TreeViewer stagingTreeViewer;
	private static TreeViewer targetTreeViewer;
	private static Composite stagingComposite;
	private static Composite targetComposite;
	private static Composite mainComposite;
	private static SashForm sash;
	private static OntologyTreeNode currentTargetNode;

	private static String sourceSchema;

	private static Server currentStagingServer;
	private static Label lblSource;
	private static Label lblTarget;
	private static DropTargetListener dropListenerStaging;
	private static DropTargetListener dropListenerTarget;
	private static Composite composite;
	private static Composite composite_2;
	private static Button btnNewVersion;
	private static Composite composite_3;
	private static Label lblVersion;
	private static Text lblNameText;
	private static Label lblVersionText;
	private static Button btnGo;
	private static Button btnCollapseAllTarget;
	private static Button btnExpandAllSource;
	private static Composite composite_4;
	private static Composite composite_5;
	private static SashForm composite_1;
	private static Composite composite_6;
	private static Button btnCancel;
	private static TreeViewerColumn column;
	/**
	 * @return the column
	 */
	public static TreeViewerColumn getTargetTreeViewerColumn() {
		return column;
	}

	/**
	 * @param column the column to set
	 */
	public static void setColumn(TreeViewerColumn column) {
		OntologyEditorView.column = column;
	}

	public OntologyEditorView() {
	}

	@Override
	public void createPartControl(final Composite parent) {
		mainComposite = parent;
		Label label = new Label(mainComposite, SWT.LEFT);
		label.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe",
				"images/IDRT_ME.gif"));
		
		System.out.println("################# Debug #######");
		TargetProjects targetProjects = new TargetProjects();

		TargetProject targetProject1 = new TargetProject();
		targetProject1.setTargetProjectID(1);
		targetProject1.setName("TargetProject1Name");
		targetProject1.setDescription("TargetProject1Desc");
		
		TargetProject targetProject2 = new TargetProject();
		targetProject2.setTargetProjectID(3);
		targetProject2.setName("TargetProject2Name");
		targetProject2.setDescription("TargetProject2Desc");
		
		targetProjects.add(targetProject1);
		targetProjects.add(targetProject2);
		
		System.out.println("TargetProjects:");
		List<TargetProject> list = targetProjects.getTargetProjectsList();
		for ( TargetProject tmpTargetProject : list ){
			System.out.println("TargetProject: " + tmpTargetProject.getTargetProjectID() + "||" + tmpTargetProject.getName() + "|" + tmpTargetProject.getDescription() );
		}
		System.out.println("---");
		System.out.println("getTargetProject with ID 3:" + targetProjects.getTargetProjectByID(3).getName());
		
	
		
		
		//targetProjects.getSelectedTargetProject()
		
		final int operations = DND.DROP_COPY | DND.DROP_MOVE; //
		final Transfer[] transferTypes = new Transfer[] { TextTransfer
				.getInstance() };

		DropTarget dropTarget = new DropTarget(label, operations);
		dropTarget.setTransfer(transferTypes);

		dropListenerStaging = new DropTargetListener() {

			@Override
			public void dropAccept(DropTargetEvent event) {
			}

			@Override
			public void drop(DropTargetEvent event) {
				System.out.println("DROPPED! " + event.data);
				if (ServerList.getTargetServers().containsKey(event.data) ) {
					System.out.println("SERVER!");
					MessageDialog.openError(Application.getShell(), "Error", "You cannot drop this item here!");
				}
				else if (((String)(event.data)).startsWith("\\i2b2")) {
					//do nothing
				}
				else {
					try {
						String schema = (String)event.data;
						Server stagingServer = ServerList.getTargetServers().get(ServerList.getUserServer().get(schema));
						stagingServer.setSchema(schema);
						setStagingServer(stagingServer);
						setStagingSchemaName((String)event.data);
						Application.executeCommand("edu.goettingen.i2b2.importtool.OntologyEditorLoad");
						setTargetNameVersion(getLatestVersion((String)(event.data)));
					} catch (Exception ex) {
						ex.printStackTrace();
						throw new RuntimeException("edu.goettingen.i2b2.importtool.OntologyEditorLoad.command not found"); 
					}
				}
			}

			@Override
			public void dragOver(DropTargetEvent event) {
			}
			@Override
			public void dragOperationChanged(DropTargetEvent event) {
			}
			@Override
			public void dragLeave(DropTargetEvent event) {
			}
			@Override
			public void dragEnter(DropTargetEvent event) {
			}
		};
		dropTarget.addDropListener(dropListenerStaging);

		dropListenerTarget = new DropTargetListener() {

			@Override
			public void dropAccept(DropTargetEvent event) {
			}

			@Override
			public void drop(DropTargetEvent event) {
				System.out.println("DROPPED! " + event.data);
				if (ServerList.getTargetServers().containsKey(event.data) ) {
					System.out.println("SERVER!");
					MessageDialog.openError(Application.getShell(), "Error", "You cannot drop this item here!");
				}
				else if (((String)(event.data)).startsWith("\\i2b2")) {
					//do nothing
				}
				else {
					setTargetNameVersion((String)event.data, getLatestVersion((String)(event.data)));
				}
			}

			@Override
			public void dragOver(DropTargetEvent event) {
			}
			@Override
			public void dragOperationChanged(DropTargetEvent event) {
			}
			@Override
			public void dragLeave(DropTargetEvent event) {
			}
			@Override
			public void dragEnter(DropTargetEvent event) {
			}
		};
	}
	/**
	 * @wbp.parser.entryPoint
	 */
	public static void init() {
		System.out.println("INIT!");
		
		
		
		//TODO HERE

//								Shell shell = new Shell();
//								shell.setSize(844, 536);
//								shell.setLayout(new FillLayout(SWT.HORIZONTAL));
//								mainComposite = new Composite(shell, SWT.NONE);
//								mainComposite.setLayout(new BorderLayout(0, 0));
		
		
		try {
		Bundle bundle = Activator.getDefault().getBundle();
		Path tmpPath = new Path("/temp/output/");
		URL tmpURL = FileLocator.find(bundle, tmpPath,
				Collections.EMPTY_MAP);

		if (tmpURL==null) {
			Path miscPath = new Path("/temp/"); 
			URL miscURL = FileLocator.find(bundle, miscPath,
					Collections.EMPTY_MAP);
			URL miscURL2 = FileLocator.toFileURL(miscURL);
			File file = new File(miscURL2.getPath()+"/output/");
			file.mkdir();
			tmpURL = FileLocator.find(bundle, tmpPath,
					Collections.EMPTY_MAP);
		}

		URL tmpURL2 = FileLocator.toFileURL(tmpURL);
		File folder = new File(tmpURL2.getPath());
		File[] listOfFiles = folder.listFiles();

		for (File listOfFile : listOfFiles) {
			if (listOfFile.getName().endsWith(".tmp") && !listOfFile.getName().equals("ph") ) { 
				System.out.println(listOfFile.getName() + " deleted");
				listOfFile.delete();
			}
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		mainComposite.getChildren()[0].dispose();
		composite = new Composite(mainComposite, SWT.NONE);
		composite.setLayout(new BorderLayout(0, 0));

		sash = new SashForm(composite, SWT.NONE);
		stagingComposite = new Composite(sash, SWT.NONE);
		stagingComposite.setLayout(new BorderLayout(0, 0));
		stagingComposite.addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {
				composite_1.setWeights(sash.getWeights());
				composite_1.layout();
			}

			@Override
			public void controlMoved(ControlEvent e) {
			}
		});

		//TODO
		targetComposite = new Composite(sash, SWT.NONE);
		targetComposite.setLayout(new BorderLayout(0, 0));
		targetTreeViewer = new TreeViewer(targetComposite, SWT.MULTI
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		TreeViewerFocusCellManager focusCellManager = new TreeViewerFocusCellManager(targetTreeViewer,new FocusCellOwnerDrawHighlighter(targetTreeViewer));
		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(targetTreeViewer) {
			protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
				return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
//						|| event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION
						|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.CR)
						|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC
						|| (event.keyCode == SWT.F2);
			}
		};

		TreeViewerEditor.create(targetTreeViewer, focusCellManager, actSupport, ColumnViewerEditor.TABBING_HORIZONTAL
				| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
				| ColumnViewerEditor.TABBING_VERTICAL | ColumnViewerEditor.KEYBOARD_ACTIVATION);

		final TextCellEditor textCellEditor = new TextCellEditor(targetTreeViewer.getTree());
		targetComposite.layout();
		//		targetComposite.pack();
		column = new TreeViewerColumn(targetTreeViewer, SWT.NONE);
		System.out.println(targetTreeViewer.getControl().getSize().x);
		column.getColumn().setMoveable(true);
		column.getColumn().setText("Column 1");
		column.setLabelProvider(new ColumnLabelProvider() {

			public String getText(Object element) {
				return "- " + element.toString();
			}

		});
		column.setEditingSupport(new EditingSupport(targetTreeViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return textCellEditor;
			}

			protected Object getValue(Object element) {
				return ((OntologyTreeNode) element).getName();
			}

			protected void setValue(Object element, Object value) {
				((OntologyTreeNode) element).getTargetNodeAttributes().setName((String)value);
				((OntologyTreeNode) element).setName((String)value);
				targetTreeViewer.update(element, null);
			}
		});
		int operations = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };

		
		
		targetTreeViewer.addDragSupport(operations, transferTypes, new NodeMoveDragListener(
				targetTreeViewer));
		
		
		NodeDropListener nodeDropListener = new NodeDropListener(targetTreeViewer);

		targetTreeViewer.addDropSupport(operations, transferTypes, nodeDropListener);
		targetTreeViewer.getTree().addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.DEL) {
					System.out.println("DELETE");
					deleteNode();
				}
			}
		});

		targetTreeViewer.getTree().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (e.item == null || e.item.getData() == null) {
					System.out.println("WidgetSelected but no known node found!");
					return;
				}
				OntologyTreeNode node = (OntologyTreeNode) e.item.getData();
				setCurrentTargetNode(node);
				if (node != null) {
					Activator.getDefault().getResource()
					.getEditorTargetInfoView().setNode(node);

					Application.getStatusView().addMessage(
							new SystemMessage("Target selection changed to \'"
									+ node.getName() + "\'.",
									SystemMessage.MessageType.SUCCESS,
									SystemMessage.MessageLocation.MAIN));
				} else {
					Application
					.getStatusView()
					.addMessage(
							new SystemMessage(
									"Target selection changed but new selection isnt' any know kind of node..",
									SystemMessage.MessageType.ERROR,
									SystemMessage.MessageLocation.MAIN));
				}
			}
		});
		IDoubleClickListener doubleClickListener = new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				TreeViewer viewer = (TreeViewer) event.getViewer();
				IStructuredSelection thisSelection = (IStructuredSelection) event.getSelection(); 
				Object selectedNode = thisSelection.getFirstElement(); 
				viewer.setExpandedState(selectedNode,
						!viewer.getExpandedState(selectedNode));
			}
		};

				targetTreeViewer.addDoubleClickListener(doubleClickListener); 


		stagingTreeViewer = new TreeViewer(stagingComposite, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);
		stagingTreeViewer.addDragSupport(operations, transferTypes, new NodeDragListener(
				stagingTreeViewer,1));
		stagingTreeViewer.setSorter(new ViewerSorter());
		composite_1 = new SashForm(composite, SWT.SMOOTH);
		composite_1.setLayoutData(BorderLayout.NORTH);
		composite_1.setLayout(new GridLayout(2, false));


		composite_3 = new Composite(composite_1, SWT.NONE);
		composite_3.setLayout(new GridLayout(3, false));
		composite_3.setSize(828,66);
		composite_3.addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {
				sash.setWeights(composite_1.getWeights());
				sash.layout();
			}

			@Override
			public void controlMoved(ControlEvent e) {
			}
		});
		DropTarget dropTarget5 = new DropTarget(composite_3, operations);
		dropTarget5.setTransfer(transferTypes);
		dropTarget5.addDropListener(dropListenerStaging);

		composite_6 = new Composite(composite_3, SWT.NONE);
		composite_6.setLayout(new GridLayout(1, false));
		lblSource = new Label(composite_6, SWT.NONE);
		lblSource.setText("Staging i2b2");
		new Label(composite_3, SWT.NONE);

		btnCancel = new Button(composite_3, SWT.NONE);
		btnCancel.setText("CANCEL");
		btnCancel.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ReadTarget.killImport();	
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		//		new Label(composite_3, SWT.NONE);
		composite_5 = new Composite(composite_3, SWT.NONE);
		composite_5.setLayout(new GridLayout(2, false));
		composite_5.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));

		Button btnExpandAll = new Button(composite_5, SWT.FLAT);
		btnExpandAll.setSize(28, 26);
		btnExpandAll.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/expandall.gif"));
		btnExpandAll.setToolTipText("Expand All");

		Button btnMinimizeAll = new Button(composite_5, SWT.NONE);
		btnMinimizeAll.setSize(28, 26);
		btnMinimizeAll.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/collapseall.gif"));
		btnMinimizeAll.setToolTipText("Collapse All");
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);

		composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		composite_2.setLayout(new GridLayout(4, false));

		DropTarget dropTarget4 = new DropTarget(composite_2, operations);
		dropTarget4.setTransfer(transferTypes);
		dropTarget4.addDropListener(dropListenerTarget);

		lblVersion = new Label(composite_2, SWT.NONE);
		lblVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblVersion.setText("Version:");
		lblVersionText = new Label(composite_2, SWT.NONE);
		lblVersionText.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnNewVersion = new Button(composite_2, SWT.NONE);
		btnNewVersion.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/plus16.png"));
		btnNewVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnNewVersion.setToolTipText("New Version");
		btnNewVersion.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				incrementVersion();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		btnGo = new Button(composite_2, SWT.NONE);
		btnGo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		btnGo.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/go-next.png"));
		btnGo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				OntologyTree ontologyTreeTarget = OntologyEditorView.getI2b2ImportTool()
						.getMyOntologyTrees().getOntologyTreeTarget();
				OntologyTreeNode bla = ((OntologyTreeNode) ontologyTreeTarget
						.getTreeRoot().getFirstChild());
				System.out.println("childCount: " + bla.getChildCount());
				if (bla.getChildCount()>0 && isNotYetSaved()) {
					boolean confirm = MessageDialog.openConfirm(Application.getShell(), "Target not saved!","The target tree has not been saved,\n" +
							"do you want to save it first?");
					if (confirm)
					{
						//save target
						Application.executeCommand("edu.goettingen.i2b2.importtool.OTWriteTarget");
					}
				}
				Application.executeCommand("de.umg.mi.idrt.ioe.uploadProject");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		composite_4 = new Composite(composite_2, SWT.NONE);
		composite_4.setLayout(new GridLayout(4, false));
		composite_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));


		DropTarget dropTarget3 = new DropTarget(composite_4, operations);
		dropTarget3.setTransfer(transferTypes);
		dropTarget3.addDropListener(dropListenerTarget);

		btnExpandAllSource = new Button(composite_4, SWT.NONE);
		btnExpandAllSource.setSize(28, 26);
		btnExpandAllSource.setToolTipText("Expand All");
		btnExpandAllSource.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/expandall.gif"));

		btnCollapseAllTarget = new Button(composite_4, SWT.NONE);
		btnCollapseAllTarget.setSize(28, 26);
		btnCollapseAllTarget.setToolTipText("Collapse All");
		btnCollapseAllTarget.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/collapseall.gif"));

		lblTarget = new Label(composite_4, SWT.NONE);
		lblTarget.setText("Target i2b2 Project:");
		lblNameText = new Text(composite_4, SWT.NONE);
		lblNameText.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
		lblNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblNameText.setText("Drop Target i2b2 here!");
		lblNameText.setEditable(false);

		DropTarget dropTarget = new DropTarget(lblNameText, operations);
		dropTarget.setTransfer(transferTypes);
		dropTarget.addDropListener(dropListenerTarget);


		DropTarget dropTarget2 = new DropTarget(lblTarget, operations);
		dropTarget2.setTransfer(transferTypes);
		dropTarget2.addDropListener(dropListenerTarget);

		btnCollapseAllTarget.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				targetTreeViewer.collapseAll();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		btnExpandAllSource.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				targetTreeViewer.expandAll();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		btnMinimizeAll.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				stagingTreeViewer.collapseAll();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		btnExpandAll.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				stagingTreeViewer.expandAll();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		stagingTreeViewer.addDoubleClickListener(doubleClickListener);

		sash.setWeights(new int[] {1, 1});
		stagingTreeViewer.addDropSupport(operations, transferTypes, dropListenerStaging);

		stagingTreeViewer.getTree().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (e.item == null || e.item.getData() == null) {
					Console.error("WidgetSelected but no known node found!");
					return;
				}
				OntologyTreeNode node = (OntologyTreeNode) e.item.getData();
				if (node != null) {
					Activator.getDefault().getResource()
					.getEditorSourceInfoView().setNode(node);
					Application.getStatusView().addMessage(
							new SystemMessage("Selection changed to \'"
									+ node.getName() + "\'.",
									SystemMessage.MessageType.SUCCESS,
									SystemMessage.MessageLocation.MAIN));
				} else {
					Application
					.getStatusView()
					.addMessage(
							new SystemMessage(
									"Selection changed but new selection isn't any know kind of node.",
									SystemMessage.MessageType.ERROR,
									SystemMessage.MessageLocation.MAIN));
				}
			}
		});
		init = true;
		initDataBindings();
	}

	/**
	 * @return the init
	 */
	public static boolean isInit() {
		return init;
	}

	public static void incrementVersion() {
		int version = Integer.parseInt(lblVersionText.getText());
		version++;
		lblVersionText.setText(""+version);
		composite_2.layout();
	}

	public static void setTargetNameVersion(String name, String version) {
		System.out.println("SETTING: " + name + " " + version);
		lblNameText.setText(name);
		lblNameText.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lblVersionText.setText(version);
		composite_2.layout();
	}
	public static void setTargetNameVersion(String version) {
		lblVersionText.setText(version);
		composite_2.layout();
	}

	public static void setI2B2ImportTool(I2B2ImportTool i2b2ImportTool) {
		OntologyEditorView.i2b2ImportTool = i2b2ImportTool;
	}
	@Override
	public void setFocus() {
	}

	public static void setTargetContent(OntologyTree oTTarget) {
		targetTreeViewer.getTree().removeAll();

		TreeContentProvider treeContentProvider = new TreeContentProvider();
		treeContentProvider.setOT(i2b2ImportTool.getMyOntologyTrees()
				.getOntologyTreeTarget());

		targetTreeViewer.setContentProvider(treeContentProvider);
		targetTreeViewer.setLabelProvider(new ViewTableLabelProvider(targetTreeViewer));
		targetTreeViewer.setInput(new OTtoTreeContentProvider().getModel());

		targetTreeViewer.expandToLevel(Resource.Options.EDITOR_SOURCE_TREE_OPENING_LEVEL);

		i2b2ImportTool.getMyOntologyTrees().getOntologyTreeTarget()
		.setTreeViewer(targetTreeViewer);

		Menu menu = new Menu(targetTreeViewer.getTree());

		//		MenuItem mntmInsert = new MenuItem(menu, SWT.PUSH);
		//		mntmInsert.setImage(ResourceManager.getPluginImage(
		//				"edu.goettingen.i2b2.importtool", "images/edit-copy.png"));
		//		mntmInsert.setText("Insert");

		//		MenuItem mntmCombine = new MenuItem(menu, SWT.PUSH);
		//		mntmCombine.setImage(ResourceManager.getPluginImage(
		//				"edu.goettingen.i2b2.importtool",
		//				"images/format-indent-more.png"));
		//		mntmCombine.setText("Combine");

	

		MenuItem mntmAddNode = new MenuItem(menu, SWT.PUSH);
		mntmAddNode.setText("Add Folder");
		mntmAddNode.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				addNode();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		
		MenuItem mntmAddItem = new MenuItem(menu, SWT.PUSH);
		mntmAddItem.setText("Add Item");
		mntmAddItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				addItem();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		
		MenuItem mntmDelete = new MenuItem(menu, SWT.PUSH);
		mntmDelete.setText("Delete Item");
		mntmDelete.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				deleteNode();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		
		MenuItem mntmRenameNode = new MenuItem(menu, SWT.PUSH);
		mntmRenameNode.setText("Rename");
		mntmRenameNode.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				renameNode();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		
		targetTreeViewer.getTree().setMenu(menu);
		menu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
			}
		});

		targetTreeViewer.getTree().setMenu(menu);
		menu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
			}
		});
		targetComposite.layout();
		mainComposite.layout();
		column.getColumn().setWidth(targetComposite.getBounds().width-5);
	}

	public static void setSourceContent() {

		if (!init) {
			init();
		}
		stagingTreeViewer.getTree().removeAll();
		TreeContentProvider treeContentProvider = new TreeContentProvider();

		treeContentProvider.setOT(i2b2ImportTool.getMyOntologyTrees()
				.getOntologyTreeSource());

		stagingTreeViewer.setContentProvider(treeContentProvider);		
//		stagingTreeViewer.setLabelProvider(new ViewTableLabelProvider(stagingTreeViewer));
		stagingTreeViewer.setLabelProvider(new ViewTableLabelProvider(stagingTreeViewer));
		OTtoTreeContentProvider oTreeContent = new OTtoTreeContentProvider();

		stagingTreeViewer.setInput(oTreeContent.getModel());
		stagingTreeViewer.expandToLevel(Resource.Options.EDITOR_SOURCE_TREE_OPENING_LEVEL);

		i2b2ImportTool.getMyOntologyTrees().getOntologyTreeSource()
		.setTreeViewer(stagingTreeViewer);


		stagingComposite.layout();
		mainComposite.layout();
	}

	public static TreeViewer getTargetTreeViewer() {
		return targetTreeViewer;
	}
	public static TreeViewer getStagingTreeViewer() {
		return stagingTreeViewer;
	}
	
	private static void addNode() {
		Application.executeCommand("de.umg.mi.idrt.ioe.addNode");
	}
	private static void addItem() {
		Application.executeCommand("de.umg.mi.idrt.ioe.addItem");
	}
	private static void renameNode() {
		Application.executeCommand("de.umg.mi.idrt.ioe.renameNode");

	}


	private static void deleteNode() {
		Application.executeCommand("de.umg.mi.idrt.ioe.deletenode");
	}

	public static void setSelection(OntologyTreeNode node) {
		System.out.println("setSelection Target");
		getTargetTreeViewer().expandToLevel(node, node.getTreePathLevel());
		getTargetTreeViewer().setSelection(new StructuredSelection(node), true);
		getTargetTreeViewer().refresh();
		Application.getEditorTargetInfoView().setNode(node);
	}
	protected static DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		return bindingContext;
	}
	private String getLatestVersion(String string) {
		//TODO GET VERSION
		return "0";
	}

	public static String getTargetSchemaName() {
		return lblNameText.getText();
	}

	public static void setStagingSchemaName(String schema) {
		sourceSchema=schema;
	}

	public static String getStagingSchemaName() {
		return sourceSchema;
	}

	/**
	 * @param currentStagingServer
	 */
	public static void setStagingServer(Server currentServer2) {
		System.out.println("setting staging server to: " + currentServer2.toString());
		currentStagingServer=currentServer2;
	}
	public static Server getStagingServer() {
		return currentStagingServer;
	}

	public static void setCurrentTargetNode(OntologyTreeNode node) {
		currentTargetNode = node;
	}

	public static OntologyTreeNode getCurrentTargetNode() {
		return currentTargetNode;
	}
	public static Point getTargetCompositePoint() {
		return targetComposite.getLocation();
	}

}
