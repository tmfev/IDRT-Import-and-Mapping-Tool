package de.umg.mi.idrt.ioe.view;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.TreeViewerEditor;
import org.eclipse.jface.viewers.TreeViewerFocusCellManager;
import org.eclipse.jface.viewers.ViewerSorter;
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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.framework.Bundle;

import swing2swt.layout.BorderLayout;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.ioe.ActionCommand;
import de.umg.mi.idrt.ioe.Activator;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.SystemMessage;
import de.umg.mi.idrt.ioe.OntologyTree.FocusCellOwnerDrawHighlighterForMultiselection;
import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTrees;
import de.umg.mi.idrt.ioe.OntologyTree.NodeDragListener;
import de.umg.mi.idrt.ioe.OntologyTree.NodeDropListener;
import de.umg.mi.idrt.ioe.OntologyTree.NodeMoveDragListener;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeContentProvider;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNodeList;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeSubNode;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeTargetRootNode;
import de.umg.mi.idrt.ioe.OntologyTree.StyledViewTableLabelProvider;
import de.umg.mi.idrt.ioe.OntologyTree.TargetProject;
import de.umg.mi.idrt.ioe.OntologyTree.TargetProjects;
import de.umg.mi.idrt.ioe.OntologyTree.TreeStagingContentProvider;
import de.umg.mi.idrt.ioe.OntologyTree.TreeTargetContentProvider;

public class OntologyEditorView extends ViewPart {

	private static MyOntologyTrees myOntologyTree;

	private static boolean notYetSaved = true;

	private static OntologyTreeNode currentStagingNode;

	private static boolean showSubNodes = true;
	private static boolean init = false;

	private static TreeViewer stagingTreeViewer;

	private static TreeViewer targetTreeViewer;
	private static Composite stagingComposite;

	private static Composite targetComposite;

	private static long time;
	private static Composite mainComposite;
	private static SashForm sash;
	private static OntologyTreeNode currentTargetNode;
	private static HashSet<OntologyTreeNode> highlightedStagingNodes;
	private static String sourceSchema;
	private static Server currentStagingServer;
	private static Label lblSource;
	private static DropTargetListener dropListenerStaging;
	private static DropTargetListener dropListenerTarget;
	private static Composite composite;
	private static Composite composite_2;

	private static Button btnNewVersion;
	private static Composite composite_3;
	private static Label lblVersion;
	private static Button btnGo;
	private static Button btnCollapseAllTarget;
	private static Button btnExpandAllTarget;
	private static Composite composite_4;
	private static Composite composite_5;
	private static SashForm composite_1;
	private static Composite composite_6;
	private static TreeViewerColumn column;
	private static Button btnShowSubNodes;
	private static Combo versionCombo;
	private static Button btnEdit;
	private static Label lblProject;
	private static Text lblTargetName;
	private static Label lblInstance;
	private static Text instanceName;
	private static Composite composite_7;
	private static Button btnDeleteVersion;
	private static Composite composite_8;
	private static Text searchText;
	private static Label searchImage;
	private static Button btnClearSearch;
	private static Composite composite_9;
	private static Label label_1;
	private static Text searchTextTarget;
	private static Button searchClearButtonTarget;
	private static Text lblStagingName;



	private static void addItem() {
		Application.executeCommand("de.umg.mi.idrt.ioe.addItem");
	}
	private static void addNode() {
		Application.executeCommand("de.umg.mi.idrt.ioe.addNode");
	}
	public static void addVersionName(String name) {
		boolean contains = false;
		for (String item : versionCombo.getItems())
		{
			if (item.equals(name))
				contains = true;
		}
		if (!contains) {
			versionCombo.add(name);
			versionCombo.setText(name);
			//			System.out.println("added " + name);
		}

	}
	public static void clearTargetName(){
		lblTargetName.setText("Drop i2b2 target here!");
		lblTargetName.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		getTargetProjects().getSelectedTarget().setTargetDBSchema("");
		composite_2.layout();
	}
	private static void collapseAllLeafs(boolean state) {

		for (Object child : (targetTreeViewer.getExpandedElements())) {
			for (OntologyTreeNode node2 : ((OntologyTreeNode)child).getChildren())
				if (((OntologyTreeNode)node2).isLeaf())
					targetTreeViewer.setExpandedState((OntologyTreeNode)node2, state);
		}
	}
	private static void deleteNode() {
		Application.executeCommand("de.umg.mi.idrt.ioe.deletenode");
	}
	private static void expandStagingChildren(OntologyTreeNode node, boolean state) {

		for (OntologyTreeNode child : node.getChildren())
			expandStagingChildren(child,state);
		stagingTreeViewer.setExpandedState(node, state);
	}

	private static void expandTargetChildren(OntologyTreeNode node, boolean state) {

		for (OntologyTreeNode child : node.getChildren())
			expandTargetChildren(child,state);
		targetTreeViewer.setExpandedState(node, state);

	}

	/**
	 * @return the currentStagingNode
	 */
	public static OntologyTreeNode getCurrentStagingNode() {
		return currentStagingNode;
	}

	public static OntologyTreeNode getCurrentTargetNode() {
		return currentTargetNode;
	}
	/**
	 * @return the i2b2ImportTool
	 */
	/*
	public static I2B2ImportTool getI2B2ImportTool() {
		return i2b2ImportTool;
	}
	 */
	public static TargetProject getInstance() {
		return getTargetProjects().getSelectedTargetProject();
		//return OntologyEditorView.getTargetProjects().getSelectedTargetProject();
	}

	public static MyOntologyTrees getMyOntologyTree(){
		return myOntologyTree;
	}

	public static OntologyTree getOntologyStagingTree(){
		return myOntologyTree.getOntologyTreeSource();
	}

	public static OntologyTree getOntologyTargetTree(){
		return myOntologyTree.getOntologyTreeTarget();
	}

	public static String getStagingSchemaName() {
		return sourceSchema;
	}
	public static Server getStagingServer() {
		return currentStagingServer;
	}

	public static TreeViewer getStagingTreeViewer() {
		return stagingTreeViewer;
	}
	/**
	 * 
	 */
	public static Composite getTargetComposite() {
		return targetComposite;
	}

	public static Point getTargetCompositePoint() {
		return targetComposite.getLocation();
	}

	public static TargetProjects getTargetProjects(){
		return ((OntologyTreeTargetRootNode)getOntologyTargetTree().getRootNode()).getTargetProjects();
	}

	public static String getTargetSchemaName() {
		if (lblTargetName!=null)
			return lblTargetName.getText();
		else 
			return "";
		//		return "";
	}

	public static TreeViewer getTargetTreeViewer() {
		return targetTreeViewer;
	}

	//	private static void markParent(OntologyTreeNode node) {
	//		System.out.println("marking " + node.getName());
	//		node.setSearchResult(true);
	//		stagingTreeViewer.setExpandedState(node, true);
	//		if (node.getParent()!=null)
	//		markParent(node.getParent());
	//	}

	/**
	 * @return the column
	 */
	public static TreeViewerColumn getTargetTreeViewerColumn() {
		return column;
	}

	private static void hideNode() {
		Application.executeCommand("de.umg.mi.idrt.ioe.hideNode");
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public static void init() {
		System.out.println("INIT! NEW NEW NEW");

		//TODO HERE

		//		Shell shell = new Shell();
		//		shell.setSize(844, 536);
		//		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		//		mainComposite = new Composite(shell, SWT.NONE);
		//		mainComposite.setLayout(new BorderLayout(0, 0));
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
			public void controlMoved(ControlEvent e) {
			}

			@Override
			public void controlResized(ControlEvent e) {
				composite_1.setWeights(sash.getWeights());
				composite_1.layout();
			}
		});

		targetComposite = new Composite(sash, SWT.NONE);
		targetComposite.setLayout(new BorderLayout(0, 0));
		targetTreeViewer = new TreeViewer(targetComposite, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);
		targetTreeViewer.setSorter(new ViewerSorter());
		TreeViewerFocusCellManager focusCellManager = new TreeViewerFocusCellManager(targetTreeViewer,new FocusCellOwnerDrawHighlighterForMultiselection(targetTreeViewer));
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

		final TextCellEditor textCellEditor = new TextCellEditor(targetTreeViewer.getTree(),SWT.NONE);
		targetComposite.layout();
		column = new TreeViewerColumn(targetTreeViewer, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);
		column.getColumn().setMoveable(false);
		//		column.getColumn().setText("");
		column.setLabelProvider(new ColumnLabelProvider() {

			public String getText(Object element) {
				return element.toString();
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
				EditorTargetInfoView.refresh();
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
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.DEL) {
					System.out.println("DELETE");
					deleteNode();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});

		targetTreeViewer.getTree().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				if (e.item == null || e.item.getData() == null) {
					System.out.println("WidgetSelected but no known node found!");
					return;
				}
				if (e.item.getData() instanceof OntologyTreeNode) {
					OntologyTreeNode node = (OntologyTreeNode) e.item.getData();
					setCurrentTargetNode(node);
					if (node != null) {
						EditorTargetInfoView.setNode(node);
					}
				}
				else if (e.item.getData() instanceof OntologyTreeSubNode) {
					OntologyTreeSubNode subNode = (OntologyTreeSubNode) e.item.getData();
					setCurrentTargetNode(subNode.getParent());
					EditorTargetInfoView.setNode(subNode);
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

		composite_8 = new Composite(stagingComposite, SWT.NONE);
		composite_8.setLayoutData(BorderLayout.NORTH);
		composite_8.setLayout(new GridLayout(3, false));

		searchImage = new Label(composite_8, SWT.NONE);
		searchImage.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/tsearch_obj.gif"));
		searchImage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		searchText = new Text(composite_8, SWT.BORDER);
		searchText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		searchText.setToolTipText("Search");
		searchText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						searchNode(getOntologyStagingTree().getNodeLists(),searchText.getText(), getOntologyStagingTree().getI2B2RootNode(),stagingTreeViewer);
						stagingTreeViewer.refresh();
						stagingComposite.redraw();
						sash.redraw();
					}
				}).run();
			}
		});
		btnClearSearch = new Button(composite_8, SWT.NO_BACKGROUND);
		btnClearSearch.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/remove-grouping.png"));
		btnClearSearch.setToolTipText("Clear Search");
		btnClearSearch.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				searchText.setText("");
			}
		});
		composite_9 = new Composite(targetComposite, SWT.NONE);
		composite_9.setLayoutData(BorderLayout.NORTH);
		composite_9.setLayout(new GridLayout(3, false));

		label_1 = new Label(composite_9, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/tsearch_obj.gif"));

		searchTextTarget = new Text(composite_9, SWT.BORDER);
		searchTextTarget.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		searchTextTarget.setToolTipText("Search");
		searchTextTarget.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						searchNode(getOntologyTargetTree().getNodeLists(), searchTextTarget.getText(), getOntologyTargetTree().getI2B2RootNode(), targetTreeViewer);
						targetTreeViewer.refresh();
						targetComposite.redraw();
						sash.redraw();
					}
				}).run();
			}
		});
		searchClearButtonTarget = new Button(composite_9, SWT.NONE);
		searchClearButtonTarget.setToolTipText("Clear Search");
		searchClearButtonTarget.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/remove-grouping.png"));
		searchClearButtonTarget.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				searchTextTarget.setText("");
			}
		});

		stagingTreeViewer.addDragSupport(operations, transferTypes, new NodeDragListener());
		stagingTreeViewer.setSorter(new ViewerSorter());
		stagingTreeViewer.getTree().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				if (e.item == null || e.item.getData() == null) {
					System.out.println("WidgetSelected but no known node found!");
					return;
				}
				if (e.item.getData() instanceof OntologyTreeNode) {
					OntologyTreeNode node = (OntologyTreeNode) e.item.getData();
					setCurrentStagingNode(node);
					if (node != null) {
						EditorSourceInfoView.setNode(node);
					} else {
						StatusView
						.addMessage(
								new SystemMessage(
										"Target selection changed but new selection isnt' any know kind of node..",
										SystemMessage.MessageType.ERROR,
										SystemMessage.MessageLocation.MAIN));
					}
				}
			}
		});

		composite_1 = new SashForm(composite, SWT.SMOOTH);
		composite_1.setLayoutData(BorderLayout.NORTH);
		composite_1.setLayout(new GridLayout(2, false));


		composite_3 = new Composite(composite_1, SWT.NONE);
		composite_3.setLayout(new GridLayout(3, false));
		composite_3.setSize(828,66);
		composite_3.addControlListener(new ControlListener() {

			@Override
			public void controlMoved(ControlEvent e) {
			}

			@Override
			public void controlResized(ControlEvent e) {
				sash.setWeights(composite_1.getWeights());
				sash.layout();
			}
		});
		DropTarget dropTarget5 = new DropTarget(composite_3, operations);
		dropTarget5.setTransfer(transferTypes);
		dropTarget5.addDropListener(dropListenerStaging);

		composite_6 = new Composite(composite_3, SWT.NONE);
		composite_6.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		composite_6.setLayout(new GridLayout(2, false));
		lblSource = new Label(composite_6, SWT.NONE);
		lblSource.setText("Staging i2b2:");

		lblStagingName = new Text(composite_6, SWT.NONE);
		lblStagingName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		lblStagingName.setEditable(false);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		//		btnCancel = new Button(composite_3, SWT.NONE);
		//		btnCancel.setText("CANCEL");
		//		btnCancel.addSelectionListener(new SelectionListener() {
		//
		//			@Override
		//			public void widgetSelected(SelectionEvent arg0) {
		//				ReadTarget.killImport();	
		//			}
		//
		//			@Override
		//			public void widgetDefaultSelected(SelectionEvent arg0) {
		//
		//			}
		//		});
		//		new Label(composite_3, SWT.NONE);
		composite_5 = new Composite(composite_3, SWT.NONE);
		composite_5.setLayout(new GridLayout(2, false));
		composite_5.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));

		Button btnExpandAllStaging = new Button(composite_5, SWT.FLAT);
		btnExpandAllStaging.setSize(28, 26);
		btnExpandAllStaging.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/expandall.gif"));
		btnExpandAllStaging.setToolTipText("Expand All");

		Button btnMinimizeAll = new Button(composite_5, SWT.NONE);
		btnMinimizeAll.setSize(28, 26);
		btnMinimizeAll.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/collapseall.gif"));
		btnMinimizeAll.setToolTipText("Collapse All");

		composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setLayout(new GridLayout(1, false));
		//		composite_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		composite_7 = new Composite(composite_2, SWT.NONE);
		composite_7.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_7.setLayout(new GridLayout(8, false));

		btnEdit = new Button(composite_7, SWT.NONE);
		btnEdit.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/format-justify-fill.png"));
		btnEdit.setText("edit");
		btnEdit.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Application.executeCommand("de.umg.mi.idrt.ioe.editInstance");
			}
		});


		lblInstance = new Label(composite_7, SWT.NONE);
		lblInstance.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		lblInstance.setText("Instance:");

		instanceName = new Text(composite_7, SWT.NONE);
		instanceName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		instanceName.setEditable(false);

		lblVersion = new Label(composite_7, SWT.NONE);
		lblVersion.setText("Version:");

		versionCombo = new Combo(composite_7, SWT.NONE);
		GridData gd_versionCombo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_versionCombo.widthHint = 20;
		versionCombo.setLayoutData(gd_versionCombo);

		btnNewVersion = new Button(composite_7, SWT.NONE);
		btnNewVersion.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/add.gif"));
		btnNewVersion.setToolTipText("New Version");

		btnDeleteVersion = new Button(composite_7, SWT.NONE);
		btnDeleteVersion.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/remove.gif"));
		btnDeleteVersion.setToolTipText("Delete Version");
		btnDeleteVersion.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//				clearTargetName();
				ActionCommand command  = new ActionCommand(Resource.ID.Command.IOE.DELETETARGET);
				command.addParameter(Resource.ID.Command.IOE.DELETETARGET_ATTRIBUTE_TARGETID, versionCombo.getText());
				Application.executeCommand(command);

				//versionCombo.setText(""+getTargetProjects().getPreviousSelectedVersion().getVersion());
				versionCombo.setText(""+getTargetProjects().getSelectedTarget().getVersion());
				versionCombo.getParent().setFocus();
				//TODO
			}
		});
		Button saveProject = new Button(composite_7, SWT.PUSH);
		saveProject.setToolTipText("Save Version");
		saveProject.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "icons/save_edit.gif"));
		saveProject.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				Application.executeCommand(Resource.ID.Command.IOE.SAVETARGET);
			}
		});
		btnNewVersion.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				//incrementVersion();
				ActionCommand command  = new ActionCommand(Resource.ID.Command.IOE.INCREMENTTARGETVERSION);
				Application.executeCommand(command);

			}
		});



		versionCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO IMPLEMENT
				if (Integer.parseInt(versionCombo.getText()) != getTargetProjects().getSelectedTarget().getVersion()){
					final Shell dialog = new Shell(Application.getDisplay(), SWT.ON_TOP //SWT.APPLICATION_MODAL
							| SWT.TOOL);

					dialog.setSize(250, 75);
					dialog.setLocation(versionCombo.toDisplay(1, 1).x-(2*versionCombo.getSize().x),versionCombo.toDisplay(1, 1).y+versionCombo.getSize().y);
					final Composite actionMenu = new Composite(dialog, SWT.NONE);
					actionMenu.setLayout(new org.eclipse.swt.layout.GridLayout(3, false));

					/**
					 * Menu for Combine Actions
					 */
					final ToolBar toolBar = new ToolBar(actionMenu, SWT.FLAT | SWT.RIGHT);
					toolBar.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));

					ToolItem tltmInsertNodes = new ToolItem(toolBar, SWT.NONE);
					tltmInsertNodes.setText("Load Version");
					tltmInsertNodes.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent e) {
							dialog.close();
							System.out.println("Load Version clicked (Version:"+versionCombo.getText()+")");

							ActionCommand command  = new ActionCommand(Resource.ID.Command.IOE.LOADTARGETONTOLOGY);
							command.addParameter(Resource.ID.Command.IOE.LOADTARGETONTOLOGY_ATTRIBUTE_VERSION, versionCombo.getText());
							Application.executeCommand(command);

						}
					});
					final ToolItem tltmCombine = new ToolItem(toolBar, SWT.PUSH);
					tltmCombine.setText("Delete Version");

					tltmCombine.addListener(SWT.Selection, new Listener() {
						public void handleEvent(Event event) {

							dialog.close();
							//TODO
							System.out.println("(Del) Version selected:" + versionCombo.getText());
							System.out.println("(Del) Previous Selected Verions:" + getTargetProjects().getPreviousSelectedVersion().getVersion());
							
							ActionCommand command  = new ActionCommand(Resource.ID.Command.IOE.DELETETARGET);
							command.addParameter(Resource.ID.Command.IOE.DELETETARGET_ATTRIBUTE_TARGETID, versionCombo.getText());
							Application.executeCommand(command);

							versionCombo.setText(""+getTargetProjects().getSelectedTarget().getVersion());
							//							versionCombo.setText(versionCombo.getItem(versionCombo.getItems().length-1));

							versionCombo.getParent().setFocus();
						}
					});

					ToolItem tltmCancel = new ToolItem(toolBar, SWT.NONE);
					tltmCancel.setText("Cancel");
					tltmCancel.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/terminate_co.gif"));
					tltmCancel.addSelectionListener(new SelectionListener() {

						@Override
						public void widgetDefaultSelected(SelectionEvent e) {

						}

						@Override
						public void widgetSelected(SelectionEvent e) {
							versionCombo.setText(""+getTargetProjects().getSelectedTarget().getVersion());
							dialog.close();
						}
					});

					actionMenu.pack();
					dialog.pack();
					dialog.open();

				}
			}
		});
		DropTarget dropTarget4 = new DropTarget(composite_2, operations);
		dropTarget4.setTransfer(transferTypes);
		dropTarget4.addDropListener(dropListenerTarget);
		composite_4 = new Composite(composite_2, SWT.NONE);
		composite_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite_4.setLayout(new GridLayout(6, false));


		DropTarget dropTarget3 = new DropTarget(composite_4, operations);
		dropTarget3.setTransfer(transferTypes);
		dropTarget3.addDropListener(dropListenerTarget);

		btnShowSubNodes = new Button(composite_4, SWT.NONE);
		btnShowSubNodes.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		btnShowSubNodes.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/showSubNodes.gif"));
		btnShowSubNodes.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				showSubNodes = !showSubNodes;
				if (showSubNodes)
					btnShowSubNodes.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/showSubNodes.gif"));
				else
					btnShowSubNodes.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/hideSubNodes.gif"));
				targetComposite.setRedraw(false);
				collapseAllLeafs(showSubNodes);
				targetTreeViewer.refresh();
				targetComposite.setRedraw(true);
			}
		});

		btnExpandAllTarget = new Button(composite_4, SWT.NONE);
		btnExpandAllTarget.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		btnExpandAllTarget.setSize(28, 26);
		btnExpandAllTarget.setToolTipText("Expand All");
		btnExpandAllTarget.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/expandall.gif"));

		btnCollapseAllTarget = new Button(composite_4, SWT.NONE);
		btnCollapseAllTarget.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		btnCollapseAllTarget.setSize(28, 26);
		btnCollapseAllTarget.setToolTipText("Collapse All");
		btnCollapseAllTarget.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/collapseall.gif"));

		lblProject = new Label(composite_4, SWT.NONE);
		lblProject.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 1));
		lblProject.setText("Target Project:");

		lblTargetName = new Text(composite_4, SWT.NONE);
		lblTargetName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		lblTargetName.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
		lblTargetName.setEditable(false);
		btnGo = new Button(composite_4, SWT.NONE);
		btnGo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 1));
		btnGo.setText("Upload");
		btnGo.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/go-next.png"));
		btnGo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			@Override
			public void widgetSelected(SelectionEvent e) {


				OntologyTree ontologyTreeTarget = OntologyEditorView.getOntologyTargetTree();
				System.out.println(((OntologyTreeNode) ontologyTreeTarget.getRootNode()).getName());
				OntologyTreeNode bla = OntologyEditorView.getOntologyTargetTree().getI2B2RootNode();
				System.out.println("childCount: " + bla.getChildCount());
				if (bla.getChildCount()>0 && isNotYetSaved()) {
					boolean confirm = MessageDialog.openConfirm(Application.getShell(), "Target not saved!","The target tree has not been saved,\n" +
							"do you want to save it first?");
					if (confirm)
					{
						//save target
						Application.executeCommand("de.umg.mi.idrt.ioe.SaveTarget");
					}
				}
				Application.executeCommand("de.umg.mi.idrt.ioe.uploadProject");
			}
		});

		btnCollapseAllTarget.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				targetTreeViewer.collapseAll();
			}
		});

		btnExpandAllTarget.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				targetComposite.setRedraw(false);
				int items = getOntologyTargetTree().getNodeLists().getNumberOfItemNodes();
				if (items>5000) {
					boolean confirm = MessageDialog.openConfirm(mainComposite.getShell(), "Continue?", "The Target Tree contains " + items
							+ " items.\nExpanding the Tree will take some time.");
					if (confirm) {
						targetTreeViewer.expandAll();
					}
				}
				else {
					targetTreeViewer.expandAll();
				}

				targetComposite.setRedraw(true);
			}
		});

		if (  OntologyEditorView.getTargetProjects().getSelectedTarget() != null ){ 
			setTargetNameVersion( getTargetProjects().getSelectedTarget().getTargetDBSchema(), ""+ getTargetProjects().getSelectedTarget().getVersion() );
		} else {
			lblTargetName.setText("Drop Target i2b2 here!");
			//			lblN
			//			lblNameText.
			//			lblNameText.setText("Drop Target i2b2 here!");
		}
		btnMinimizeAll.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				stagingTreeViewer.collapseAll();
			}
		});
		btnExpandAllStaging.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				stagingComposite.setRedraw(false);
				int items = getOntologyStagingTree().getNodeLists().getNumberOfItemNodes();
				if (items>5000) {
					boolean confirm = MessageDialog.openConfirm(mainComposite.getShell(), "Continue?", "The Staging Tree contains " + items
							+ " items.\nExpanding the Tree will take some time.");
					if (confirm)
						stagingTreeViewer.expandAll();
				}
				else {
					stagingTreeViewer.expandAll();
				}
				stagingComposite.setRedraw(true);
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
				if (node != null ) {
					EditorSourceInfoView.setNode(node);
				} else {
					StatusView
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


	protected static DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		return bindingContext;
	}

	/**
	 * @return the init
	 */
	public static boolean isInit() {
		return init;
	}

	/**
	 * @return the notYetSaved
	 */
	public static boolean isNotYetSaved() {
		return notYetSaved;
	}
	/**
	 * @return the showSubNodes
	 */
	public static boolean isShowSubNodes() {
		return showSubNodes;
	}
	private static void markNodes(OntologyTreeNode node, String text, TreeViewer viewer) {

		for (OntologyTreeNode child : node.getChildren()) {
			markNodes(child, text,viewer);
		}
		if (node.getName().toLowerCase().contains(text.toLowerCase())) {
			//			markParent(node);
			node.setSearchResult(true);
			//			stagingTreeViewer.
			while (node.getParent()!=null) {
				node = node.getParent();
				viewer.setExpandedState(node, true);
			}
		}
		else {
			node.setSearchResult(false);
			//			stagingTreeViewer.setExpandedState(node, false);
		}
	}

	public static void refreshTargetName() {

		if(lblTargetName != null) {
			if (getTargetProjects().getSelectedTarget() != null && !getTargetProjects().getSelectedTarget().getTargetDBSchema().isEmpty()) {
				lblTargetName.setText(getTargetProjects().getSelectedTarget().getTargetDBSchema());
				lblTargetName.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
			}
			else {
				lblTargetName.setText("Drop i2b2 target here!");
				lblTargetName.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
			}
			composite_2.layout();
		}
	}

	public static void refreshTargetVersionGUI(){

		refreshVersionCombo();
		refreshTargetName();
	}

	public static void refreshVersionCombo() {
		if ( versionCombo != null ){
			versionCombo.removeAll();
			versionCombo.setText("");
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i<getTargetProjects().getSelectedTargetProject().getTargetsList().size();i++) {
				list.add(getTargetProjects().getSelectedTargetProject().getTargetsList().get(i).getVersion());
			}
			Collections.sort(list);
			for (Integer i : list) {
				addVersionName(""+i);
			}

			if (getTargetProjects().getSelectedTarget() != null){
				versionCombo.setText(String.valueOf(getTargetProjects().getSelectedTarget().getVersion()));
			}

			composite_2.layout();
		} else {
			System.err.println("versionCombo null");
			Console.error("versionCombo is null, so don't refresh the version number");
		}
	}
	public static void removeVersionFromCombo(String version) {

		if ( versionCombo != null ){
			System.out.println("VERSION: " + version + " / oldSelectedVersion:" + getTargetProjects().getPreviousSelectedVersion().getVersion());
			versionCombo.remove(version);
			
//			
//			boolean found = false;
//			for (String items : versionCombo.getItems()) {
//				if (items.equals(""+getTargetProjects().getPreviousSelectedVersion().getVersion())) {
//					versionCombo.setText(""+getTargetProjects().getPreviousSelectedVersion().getVersion());
//					found = true;
//					break;
//				}
//			}
//			if (!found) {
//				versionCombo.setText(""+versionCombo.getItem(versionCombo.getItemCount()-1));
//				System.out.println(versionCombo.getItem(versionCombo.getItemCount()-1));
//			}
//			composite_2.layout();

		} else {
			Console.error("versionCombo is null, so don't remove an entry from it");
		}
	}
	private static void renameNode() {
		Application.executeCommand("de.umg.mi.idrt.ioe.renameNode");

	}

	private static void searchNode(OntologyTreeNodeList nodeLists, String text, OntologyTreeNode rootNode, TreeViewer treeViewer) {
		int size = nodeLists.getNumberOfItemNodes();
		if (text.isEmpty()) {
			unmarkAllNodes(rootNode);	
		}
		else {
			if (size<1000 || text.length()>=3) {
				markNodes(rootNode,text,treeViewer);
			}
		}
	}
	/**
	 * @param column the column to set
	 */
	public static void setColumn(TreeViewerColumn column) {
		OntologyEditorView.column = column;
	}
	/**
	 * @param currentStagingNode the currentStagingNode to set
	 */
	public static void setCurrentStagingNode(OntologyTreeNode currentStagingNode) {
		OntologyEditorView.currentStagingNode = currentStagingNode;
	}

	public static void setCurrentTargetNode(OntologyTreeNode node) {
		currentTargetNode = node;
	}

	public static void setInstance(String name, String description) {
		instanceName.setText(name);
		OntologyEditorView.getTargetProjects().getSelectedTargetProject().setName(name);
		OntologyEditorView.getTargetProjects().getSelectedTargetProject().setDescription(description);
	}

	public static void setMyOntologyTree (MyOntologyTrees myOT){
		myOntologyTree = myOT;
	}

	/**
	 * @param notYetSaved the notYetSaved to set
	 */
	public static void setNotYetSaved(boolean notYetSaved) {
		OntologyEditorView.notYetSaved = notYetSaved;
	}

	public static void setSelection(OntologyTreeNode node) {
		getTargetTreeViewer().expandToLevel(node, node.getTreePathLevel());
		getTargetTreeViewer().setSelection(new StructuredSelection(node), true);
		getTargetTreeViewer().refresh();
	}

	/**
	 * @param showSubNodes the showSubNodes to set
	 */
	public static void setShowSubNodes(boolean showSubNodes) {
		OntologyEditorView.showSubNodes = showSubNodes;
	}

	public static void setSourceContent() {
		long time =System.currentTimeMillis();
		if (!init) {
			init();
		}
		stagingTreeViewer.getTree().removeAll();

		stagingTreeViewer.setContentProvider(new TreeStagingContentProvider());		
		stagingTreeViewer.setLabelProvider(new StyledViewTableLabelProvider());

		stagingTreeViewer.setInput(new OntologyTreeContentProvider().getStagingModel());
		stagingTreeViewer.expandToLevel(2);
		OntologyEditorView.getOntologyStagingTree()
		.setTreeViewer(stagingTreeViewer);

		Menu menu = new Menu(stagingTreeViewer.getTree());

		MenuItem mntmExpandChildren = new MenuItem(menu, SWT.PUSH);
		mntmExpandChildren.setText("Expand All Children");
		mntmExpandChildren.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			@Override
			public void widgetSelected(SelectionEvent event) {

				new Thread(new Runnable() {

					@Override
					public void run() {
						OntologyTreeNode node = OntologyEditorView.getCurrentStagingNode();
						stagingComposite.setRedraw(false);

						expandStagingChildren(node, true);
						stagingComposite.setRedraw(true);
					}
				}).run();
				OntologyTreeNode node = OntologyEditorView.getCurrentStagingNode();
				stagingComposite.setRedraw(false);
				expandStagingChildren(node, true);
				stagingComposite.setRedraw(true);
			}
		});
		MenuItem mntmCollapseChildren = new MenuItem(menu, SWT.PUSH);
		mntmCollapseChildren.setText("Collapse All Children");
		mntmCollapseChildren.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			@Override
			public void widgetSelected(SelectionEvent event) {
				OntologyTreeNode node = OntologyEditorView.getCurrentStagingNode();
				stagingComposite.setRedraw(false);
				expandStagingChildren(node, false);
				stagingComposite.setRedraw(true);
			}
		});

		stagingTreeViewer.getTree().setMenu(menu);
		stagingComposite.layout();
		mainComposite.layout();
		refreshTargetVersionGUI();
		System.out.println(System.currentTimeMillis()-time +"ms");
	}



	public static void setStagingName(String stagingName) {
		lblStagingName.setText(stagingName);
	}

	public static void setStagingSchemaName(String schema) {
		sourceSchema=schema;
	}

	/**
	 * @param currentStagingServer
	 */
	public static void setStagingServer(Server currentServer2) {
		currentStagingServer = new Server(currentServer2);
		System.out.println("setting staging server to: " + currentServer2.toString() + " " + currentServer2.getSchema());
	}



	public static void setTargetContent(OntologyTree oTTarget) {
		targetTreeViewer.getTree().removeAll();

		TreeTargetContentProvider treeContentProvider = new TreeTargetContentProvider();

		targetTreeViewer.setContentProvider(treeContentProvider);
		targetTreeViewer.setLabelProvider(new StyledViewTableLabelProvider());
		targetTreeViewer.setInput(new OntologyTreeContentProvider().getTargetModel());
		targetTreeViewer.getTree().addMouseTrackListener(new MouseTrackListener() {

			@Override
			public void mouseEnter(MouseEvent arg0) {
				for (OntologyTreeNode node : highlightedStagingNodes) {
					node.setHighlighted(false);
					highlightedStagingNodes.remove(node);
				}

			}

			@Override
			public void mouseExit(MouseEvent arg0) {
				for (OntologyTreeNode node : highlightedStagingNodes) {
					node.setHighlighted(false);
				}
				highlightedStagingNodes.clear();
				stagingTreeViewer.refresh();
			}

			@Override
			public void mouseHover(MouseEvent arg0) {
				if (highlightedStagingNodes!=null) {
					for (OntologyTreeNode node : highlightedStagingNodes) {
						node.setHighlighted(false);
					}
					highlightedStagingNodes.clear();
				}

				Point p = new Point(arg0.x,arg0.y);
				TreeItem a = targetTreeViewer.getTree().getItem(p);
				if (a!=null) {
					if (a.getData() instanceof OntologyTreeNode) {
						OntologyTreeNode node = (OntologyTreeNode) a.getData();

						//				System.out.println("T "+node.getName());
						for (OntologyTreeSubNode subNode: node.getTargetNodeAttributes().getSubNodeList()) {
							if (subNode.getStagingParentNode()!=null) {
								highlightedStagingNodes.add(subNode.getStagingParentNode());
								subNode.getStagingParentNode().setHighlighted(true);
							}
						}
					}
					else if (a.getData() instanceof OntologyTreeSubNode) {
						OntologyTreeSubNode subNode = (OntologyTreeSubNode) a.getData();
						if (subNode.getStagingParentNode()!=null) {
							highlightedStagingNodes.add(subNode.getStagingParentNode());
							subNode.getStagingParentNode().setHighlighted(true);
						}
					}
				}
				stagingTreeViewer.refresh();
			}
		});

		//		targetTreeViewer.expandToLevel(Resource.Options.EDITOR_SOURCE_TREE_OPENING_LEVEL);

		getOntologyTargetTree()
		.setTreeViewer(targetTreeViewer);

		Menu menu = new Menu(targetTreeViewer.getTree());

		MenuItem mntmgetChildren = new MenuItem(menu, SWT.PUSH);
		mntmgetChildren.setText("getChildren");
		mntmgetChildren.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				//				targetTreeViewer.getSelection()

			}

			@Override
			public void widgetSelected(SelectionEvent event) {
				IStructuredSelection selection = (IStructuredSelection) targetTreeViewer.getSelection();
				OntologyTreeNode node = (OntologyTreeNode) selection.iterator().next();
				if (node == OntologyEditorView.getOntologyTargetTree().getI2B2RootNode())
					System.out.println("TRUE");

				for (OntologyTreeSubNode child : node.getTargetNodeAttributes().getSubNodeList()) {
					System.out.println(child.getStagingName() + ":" + child.getStagingPath());
				}
				System.out.println(node.getTreePath());
			}
		});
		new MenuItem(menu, SWT.SEPARATOR);
		MenuItem mntmAddNode = new MenuItem(menu, SWT.PUSH);
		mntmAddNode.setText("Add Folder");
		mntmAddNode.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			@Override
			public void widgetSelected(SelectionEvent event) {
				addNode();
			}
		});

		MenuItem mntmAddItem = new MenuItem(menu, SWT.PUSH);
		mntmAddItem.setText("Add Item");
		mntmAddItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			@Override
			public void widgetSelected(SelectionEvent event) {
				addItem();
			}
		});

		new MenuItem(menu, SWT.SEPARATOR);
		MenuItem mntmExpandChildren = new MenuItem(menu, SWT.PUSH);
		mntmExpandChildren.setText("Expand All Children");
		mntmExpandChildren.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			@Override
			public void widgetSelected(SelectionEvent event) {


				OntologyTreeNode node = OntologyEditorView.getCurrentTargetNode();
				targetComposite.setRedraw(false);
				expandTargetChildren(node, true);
				targetComposite.setRedraw(true);
			}
		});
		MenuItem mntmCollapseChildren = new MenuItem(menu, SWT.PUSH);
		mntmCollapseChildren.setText("Collapse All Children");
		mntmCollapseChildren.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			@Override
			public void widgetSelected(SelectionEvent event) {
				OntologyTreeNode node = OntologyEditorView.getCurrentTargetNode();
				targetComposite.setRedraw(false);
				expandTargetChildren(node, false);
				targetComposite.setRedraw(true);
			}
		});
		new MenuItem(menu, SWT.SEPARATOR);
		MenuItem mntmHideItem = new MenuItem(menu, SWT.PUSH);
		mntmHideItem.setText("Hide/Activate");

		mntmHideItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			@Override
			public void widgetSelected(SelectionEvent event) {
				hideNode();
			}
		});

		MenuItem mntmDelete = new MenuItem(menu, SWT.PUSH);
		mntmDelete.setText("Delete");
		mntmDelete.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			@Override
			public void widgetSelected(SelectionEvent event) {
				deleteNode();
			}
		});
		new MenuItem(menu, SWT.SEPARATOR);
		MenuItem mntmRenameNode = new MenuItem(menu, SWT.PUSH);
		mntmRenameNode.setText("Rename");
		mntmRenameNode.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			@Override
			public void widgetSelected(SelectionEvent event) {
				renameNode();
			}
		});

		//		targetTreeViewer.getTree().setMenu(menu);
		//		menu.addMenuListener(new MenuAdapter() {
		//			public void menuShown(MenuEvent e) {
		//			}
		//		});

		targetTreeViewer.getTree().setMenu(menu);
		menu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
			}
		});
		//TODO
		setInstance(getTargetProjects().getSelectedTargetProject().getName(),getTargetProjects().getSelectedTargetProject().getDescription());

		//		versionCombo.removeAll();
		targetComposite.layout();
		mainComposite.layout();
		column.getColumn().setWidth(targetComposite.getBounds().width-5);
		refreshTargetVersionGUI();
	}

	public static void setTargetNameVersion(String version) {
		addVersionName(version);
		versionCombo.setText(version);
		composite_2.layout();
	}	


	public static void setTargetNameVersion(String name, String version) {
		System.out.println("SETTING: " + name + " " + version);
		if (name != null) {
			lblTargetName.setText(name);
			lblTargetName.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		}
		else {
			lblTargetName.setText("Drop i2b2 target here!");
			lblTargetName.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		}

		//		addVersionName(version);
		//		versionCombo.setText(version);
		System.out.println("Setting selectedTarget.setTargetDBSchema:" + name + "!");
		getTargetProjects().getSelectedTarget().setTargetDBSchema(name);

		composite_2.layout();
	}

	private static void unmarkAllNodes(OntologyTreeNode node) {

		for (OntologyTreeNode child : node.getChildren()) 
			unmarkAllNodes(child);
		node.setSearchResult(false);
	}

	@Override
	public void createPartControl(final Composite parent) {
		mainComposite = parent;
		highlightedStagingNodes = new HashSet<OntologyTreeNode>();
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
			public void dragEnter(DropTargetEvent event) {
			}

			@Override
			public void dragLeave(DropTargetEvent event) {
			}

			@Override
			public void dragOperationChanged(DropTargetEvent event) {
			}
			@Override
			public void dragOver(DropTargetEvent event) {
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
				else if (((String)(event.data)).equals("stagingTreeViewer")) {
					System.err.println("staging node dropped");
				}
				else {
					try {
						//						String schema = (String)event.data;
						//						Server stagingServer = ServerList.getTargetServers().get(ServerList.getUserServer().get(schema));
						//						stagingServer.setSchema(schema);
						//						setStagingServer(stagingServer);
						//						setStagingSchemaName(schema);

						Application.executeCommand("edu.goettingen.i2b2.importtool.OntologyEditorLoad");
						setTargetNameVersion(getLatestVersion((String)(event.data)));
						// ((OntologyTreeTargetRootNode)OntologyEditorView.getI2b2ImportTool().getMyOntologyTrees().getOntologyTargetTree().getTreeRoot()).getTargetProjects().getSelectedTarget().setVersion(Integer.valueOf( (String)event.data ));
					} catch (Exception ex) {
						ex.printStackTrace();
						throw new RuntimeException("edu.goettingen.i2b2.importtool.OntologyEditorLoad.command not found"); 
					}
				}
			}
			@Override
			public void dropAccept(DropTargetEvent event) {
			}
		};
		dropTarget.addDropListener(dropListenerStaging);

		dropListenerTarget = new DropTargetListener() {

			@Override
			public void dragEnter(DropTargetEvent event) {
			}

			@Override
			public void dragLeave(DropTargetEvent event) {
			}

			@Override
			public void dragOperationChanged(DropTargetEvent event) {
			}
			@Override
			public void dragOver(DropTargetEvent event) {
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
			public void dropAccept(DropTargetEvent event) {
			}
		};
	}

	private String getLatestVersion(String string) {

		if (  getTargetProjects().getSelectedTarget() != null ){ 
			return String.valueOf( getTargetProjects().getSelectedTarget().getVersion() ) ;

		} else {
			return "0";
		}
	}
	@Override
	public void setFocus() {
	}
}
