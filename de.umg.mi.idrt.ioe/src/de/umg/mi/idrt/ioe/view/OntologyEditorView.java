package de.umg.mi.idrt.ioe.view;

import java.awt.MouseInfo;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.swing.tree.MutableTreeNode;

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
import org.eclipse.swt.dnd.ByteArrayTransfer;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.framework.Bundle;

import swing2swt.layout.BorderLayout;
import de.umg.mi.idrt.idrtimporttool.server.Settings.I2b2Project;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.idrtimporttool.server.Settings.I2b2ProjectTransferType;
import de.umg.mi.idrt.importtool.views.ServerView;
import de.umg.mi.idrt.ioe.ActionCommand;
import de.umg.mi.idrt.ioe.Activator;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.GUITools;
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
import de.umg.mi.idrt.ioe.OntologyTree.TOSConnector;
import de.umg.mi.idrt.ioe.OntologyTree.TargetInstance;
import de.umg.mi.idrt.ioe.OntologyTree.TargetInstances;
import de.umg.mi.idrt.ioe.OntologyTree.TransmartOntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.TransmartOntologyTreeContentProvider;
import de.umg.mi.idrt.ioe.OntologyTree.TransmartStyledViewTableLabelProvider;
import de.umg.mi.idrt.ioe.OntologyTree.TransmartTreeStagingContentProvider;
import de.umg.mi.idrt.ioe.OntologyTree.TreeStagingContentProvider;
import de.umg.mi.idrt.ioe.OntologyTree.TreeTargetContentProvider;
import de.umg.mi.idrt.ioe.misc.FileHandler;
import de.umg.mi.idrt.ioe.tos.TOSHandler;

public class OntologyEditorView extends ViewPart {

	private static TransmartOntologyTree transmartTree;
	private static MyOntologyTrees myOntologyTree;

	private static boolean notYetSaved = false;

	private static OntologyTreeNode currentStagingNode;

	private static boolean showSubNodes = true;
	private static boolean init = false;

	private static TreeViewer stagingTreeViewer;

	private static TreeViewer targetTreeViewer;
	private static Composite stagingComposite;

	private static Composite targetComposite;

	private static Composite mainComposite;
	private static SashForm sash;
	private static OntologyTreeNode currentTargetNode;
	private static HashSet<OntologyTreeNode> highlightedStagingNodes;
	private static HashSet<OntologyTreeNode> highlightedTargetNodes;
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
	private static TreeViewerColumn targetColumn;
	private static TreeViewerColumn stagingColumn;
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
	private static Composite composite_12;
	private static Composite composite_11;

	public OntologyEditorView() {
		// TODO Auto-generated constructor stub
//		transmartTree = new TransmartOntologyTree();
	}

	private static void createDirs(File mainPath) {
		File misc = new File(mainPath.getAbsolutePath()+"/temp/");

		if (!misc.exists()) 
			misc.mkdir();

		File miscInput = new File(mainPath.getAbsolutePath()+"/temp/output/");
		if (!miscInput.exists()) 
			miscInput.mkdir();
	}

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
		getTargetInstance().getSelectedTarget().setTargetDBSchema("");
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

	/**
	 * @return the currentStagingNode
	 */
	public static OntologyTreeNode getCurrentStagingNode() {
		return currentStagingNode;
	}

	public static OntologyTreeNode getCurrentTargetNode() {
		return currentTargetNode;
	}
	public static TargetInstance getInstance() {
		return getTargetInstance().getSelectedTargetInstance();
	}

	public static MyOntologyTrees getMyOntologyTree(){
		return myOntologyTree;
	}

	public static OntologyTree getOntologyStagingTree(){
		return myOntologyTree.getOntologyTreeSource();
	}

	public static OntologyTree getOntologyTargetTree(){
		if (myOntologyTree != null)
			return myOntologyTree.getOntologyTreeTarget();
		else 
			return null;
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

	public static TargetInstances getTargetInstance(){
		return ((OntologyTreeTargetRootNode)getOntologyTargetTree().getRootNode()).getTargetProjects();
	}

	public static String getTargetSchemaName() {
		return lblTargetName!=null?lblTargetName.getText():"";
	}

	public static TreeViewer getTargetTreeViewer() {
		return targetTreeViewer;
	}

	/**
	 * @return the column
	 */
	public static TreeViewerColumn getTargetTreeViewerColumn() {
		return targetColumn;
	}

	private static void hideNode() {
		Application.executeCommand("de.umg.mi.idrt.ioe.hideNode");
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
			File folder = FileHandler.getBundleFile("/temp/output/");
			File[] listOfFiles = folder.listFiles();

			for (File listOfFile : listOfFiles) {
				if (!listOfFile.getName().equals("ph") ) { 
					listOfFile.delete();
				}
			}

			folder = FileHandler.getBundleFile("/temp/");
			listOfFiles = folder.listFiles();

			for (File listOfFile : listOfFiles) {
				if (!listOfFile.getName().equals("ph") ) { 
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

		final Listener targetTreeLabelListener = new Listener () {
			public void handleEvent (Event event) {
				Label label = (Label)event.widget;
				Shell shell = label.getShell ();
				switch (event.type) {
				case SWT.MouseDown:
					Event e = new Event ();
					e.item = (TreeItem) label.getData ("_TABLEITEM");
					// Assuming table is single select, set the selection as if
					// the mouse down event went through to the table
					targetTreeViewer.getTree().setSelection (new TreeItem [] {(TreeItem) e.item});
					targetTreeViewer.getTree().notifyListeners (SWT.Selection, e);
					shell.dispose ();
					targetTreeViewer.getTree().setFocus();
					break;
				case SWT.MouseExit:
					shell.dispose ();
					break;
				}
			}
		};

		Listener targetTreeListener = new Listener () {
			Shell tip = null;
			Label label = null;
			public void handleEvent (Event event) {
				switch (event.type) {
				case SWT.Dispose:
				case SWT.KeyDown:
				case SWT.MouseMove: {
					if (tip == null) break;
					tip.dispose ();
					tip = null;
					label = null;
					break;
				}
				case SWT.MouseHover: {
					Point coords = new Point(event.x, event.y);
					TreeItem item = targetTreeViewer.getTree().getItem(coords);
					MutableTreeNode node = null;
					String tooltip ="";
					if (item != null) {
						if (item.getData() instanceof OntologyTreeNode) {
							node = (OntologyTreeNode) item.getData();
							if (((OntologyTreeNode) node).getTargetNodeAttributes().getTargetNodeMap().
									get(Resource.I2B2.NODE.TARGET.C_TOOLTIP) != null)
								tooltip=((OntologyTreeNode) node).getTargetNodeAttributes().getTargetNodeMap().
								get(Resource.I2B2.NODE.TARGET.C_TOOLTIP);
						}
						else if (item.getData() instanceof OntologyTreeSubNode) {
							node = (OntologyTreeSubNode) item.getData();
							if (((OntologyTreeSubNode) node).getParent().getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_TOOLTIP)!=null) {
								tooltip = (((OntologyTreeSubNode) node).getParent().getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_TOOLTIP));
							}
						}
					}
					if (item != null && !tooltip.isEmpty()) {
						int columns = targetTreeViewer.getTree().getColumnCount();

						for (int i = 0; i < columns || i == 0; i++) {
							if (item.getBounds(i).contains(coords)) {
								if (tip != null  && !tip.isDisposed ())
									tip.dispose ();
								tip = new Shell (targetTreeViewer.getTree().getShell(), SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
								tip.setBackground (targetTreeViewer.getTree().getDisplay().getSystemColor (SWT.COLOR_INFO_BACKGROUND));
								FillLayout layout = new FillLayout ();
								layout.marginWidth = 2;
								tip.setLayout (layout);
								label = new Label (tip, SWT.NONE);
								label.setForeground (targetTreeViewer.getTree().getDisplay().getSystemColor (SWT.COLOR_INFO_FOREGROUND));
								label.setBackground (targetTreeViewer.getTree().getDisplay().getSystemColor (SWT.COLOR_INFO_BACKGROUND));
								label.setData ("_TABLEITEM", item);
								label.setText(tooltip);
								label.addListener (SWT.MouseExit, targetTreeLabelListener);
								label.addListener (SWT.MouseDown, targetTreeLabelListener);
								Point size = tip.computeSize (SWT.DEFAULT, SWT.DEFAULT);
								tip.setBounds (MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y-size.y, size.x, size.y);
								tip.setVisible (true);
								break;
							}
						}
					}
				}
				}
			}
		};
		targetComposite.layout();

		int operations = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance(), I2b2ProjectTransferType.getInstance() };
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

		stagingTreeViewer = new TreeViewer(stagingComposite, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);

		final Listener stagingTreeLabelListener = new Listener () {
			public void handleEvent (Event event) {
				Label label = (Label)event.widget;
				Shell shell = label.getShell ();
				switch (event.type) {
				case SWT.MouseDown:
					Event e = new Event ();
					e.item = (TreeItem) label.getData ("_TABLEITEM");
					// Assuming table is single select, set the selection as if
					// the mouse down event went through to the table
					stagingTreeViewer.getTree().setSelection (new TreeItem [] {(TreeItem) e.item});
					stagingTreeViewer.getTree().notifyListeners (SWT.Selection, e);
					shell.dispose ();
					stagingTreeViewer.getTree().setFocus();
					break;
				case SWT.MouseExit:
					shell.dispose ();
					break;
				}
			}
		};

		Listener stagingTreeListener = new Listener () {
			Shell tip = null;
			Label label = null;
			public void handleEvent (Event event) {
				switch (event.type) {
				case SWT.Dispose:
				case SWT.KeyDown:
				case SWT.MouseMove: {
					if (tip == null) break;
					tip.dispose ();
					tip = null;
					label = null;
					break;
				}
				case SWT.MouseHover: {
					Point coords = new Point(event.x, event.y);
					TreeItem item = stagingTreeViewer.getTree().getItem(coords);
					MutableTreeNode nodse = null;
					String tooltip ="";
					if (item!=null) {
						if (item.getData() instanceof OntologyTreeNode) {
							nodse = (OntologyTreeNode) item.getData();
							if (((OntologyTreeNode) nodse).getOntologyCellAttributes().getC_TOOLTIP() != null)
								tooltip=((OntologyTreeNode) nodse).getOntologyCellAttributes().getC_TOOLTIP();
						}

						if (!tooltip.isEmpty()) {
							int columns = stagingTreeViewer.getTree().getColumnCount();

							for (int i = 0; i < columns || i == 0; i++) {
								if (item.getBounds(i).contains(coords)) {
									if (tip != null  && !tip.isDisposed ())
										tip.dispose ();
									tip = new Shell (stagingTreeViewer.getTree().getShell(), SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
									tip.setBackground (stagingTreeViewer.getTree().getDisplay().getSystemColor (SWT.COLOR_INFO_BACKGROUND));
									FillLayout layout = new FillLayout ();
									layout.marginWidth = 2;
									tip.setLayout (layout);
									label = new Label (tip, SWT.NONE);
									label.setForeground (stagingTreeViewer.getTree().getDisplay().getSystemColor (SWT.COLOR_INFO_FOREGROUND));
									label.setBackground (stagingTreeViewer.getTree().getDisplay().getSystemColor (SWT.COLOR_INFO_BACKGROUND));
									label.setData ("_TABLEITEM", item);
									label.setText(tooltip);
									label.addListener (SWT.MouseExit, stagingTreeLabelListener);
									label.addListener (SWT.MouseDown, stagingTreeLabelListener);
									Point size = tip.computeSize (SWT.DEFAULT, SWT.DEFAULT);
									tip.setBounds (MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y-size.y, size.x, size.y);
									tip.setVisible (true);
									break;
								}
							}
						}
					}
				}
				}
			}
		};
		stagingTreeViewer.getTree().addListener (SWT.Dispose, stagingTreeListener);
		stagingTreeViewer.getTree().addListener (SWT.KeyDown, stagingTreeListener);
		stagingTreeViewer.getTree().addListener (SWT.MouseMove, stagingTreeListener);
		stagingTreeViewer.getTree().addListener (SWT.MouseHover, stagingTreeListener);


		stagingColumn = new TreeViewerColumn(stagingTreeViewer, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);
		stagingColumn.getColumn().setMoveable(false);
		stagingColumn.setLabelProvider(new ColumnLabelProvider() {

			public String getText(Object element) {
				return element.toString();
			}
		});

		composite_8 = new Composite(stagingComposite, SWT.NONE);
		composite_8.setLayoutData(BorderLayout.NORTH);
		composite_8.setLayout(new GridLayout(3, false));

		searchImage = new Label(composite_8, SWT.NONE);
		searchImage.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/tsearch_obj.gif"));
		searchImage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		searchText = new Text(composite_8, SWT.BORDER);
		searchText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		searchText.setToolTipText("Search");
		searchText.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR){
					Display.getCurrent().asyncExec(new Runnable() {
						public void run() {
							clearSearch(getOntologyStagingTree().getNodeLists(),stagingTreeViewer);
							searchNode(getOntologyStagingTree().getNodeLists(),searchText.getText(), getOntologyStagingTree().getI2B2RootNode(),stagingTreeViewer);
							stagingTreeViewer.refresh();
							stagingComposite.redraw();
							sash.redraw();
						}
					});
				}
			}
		});
		searchText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				Display.getCurrent().syncExec(new Runnable() {
					public void run() {
						if (searchText.getText().length()>3){
							clearSearch(getOntologyStagingTree().getNodeLists(),stagingTreeViewer);
							searchNode(getOntologyStagingTree().getNodeLists(),searchText.getText(), getOntologyStagingTree().getI2B2RootNode(),stagingTreeViewer);
							stagingTreeViewer.refresh();
							stagingComposite.redraw();
							sash.redraw();
						}
					}
				});
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
				clearSearch(getOntologyStagingTree().getNodeLists(),stagingTreeViewer);
				stagingTreeViewer.refresh();
				stagingComposite.redraw();
				sash.redraw();
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

		searchTextTarget.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR){
					Display.getCurrent().asyncExec(new Runnable() {
						public void run() {
							clearSearch(getOntologyTargetTree().getNodeLists(),targetTreeViewer);
							searchNode(getOntologyTargetTree().getNodeLists(),searchTextTarget.getText(), getOntologyTargetTree().getI2B2RootNode(),targetTreeViewer);
							targetTreeViewer.refresh();
							targetComposite.redraw();
							sash.redraw();
						}
					});
				}
			}
		});
		searchTextTarget.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				Display.getCurrent().syncExec(new Runnable() {
					public void run() {
						if (searchTextTarget.getText().length()>3){
							clearSearch(getOntologyTargetTree().getNodeLists(),targetTreeViewer);
							searchNode(getOntologyTargetTree().getNodeLists(),searchTextTarget.getText(), getOntologyTargetTree().getI2B2RootNode(),targetTreeViewer);
							targetTreeViewer.refresh();
							targetComposite.redraw();
							sash.redraw();
						}
					}
				});
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
				clearSearch(getOntologyTargetTree().getNodeLists(),targetTreeViewer);
				targetTreeViewer.refresh();
				targetComposite.redraw();
				sash.redraw();
			}
		});

		composite_12 = new Composite(targetComposite, SWT.NONE);
		composite_12.setLayoutData(BorderLayout.CENTER);
		composite_12.setLayout(new BorderLayout(0, 0));

		Composite composite_10 = new Composite(composite_12, SWT.NONE);
		composite_10.setLayoutData(BorderLayout.EAST);
		composite_10.setLayout(new GridLayout(1, false));

		Button btnMoveNodeUp = new Button(composite_10, SWT.NONE);
		btnMoveNodeUp.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 1));
		btnMoveNodeUp.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/go-up.png"));
		btnMoveNodeUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				moveNodeUp();
			}
		});
		Button btnMoveNodeDown = new Button(composite_10, SWT.NONE);
		btnMoveNodeDown.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1));
		btnMoveNodeDown.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/go-down.png"));
		btnMoveNodeDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				moveNodeDown();
			}
		});
		composite_11 = new Composite(composite_12, SWT.NONE);
		composite_11.setLayoutData(BorderLayout.CENTER);
		composite_11.setLayout(new FillLayout(SWT.HORIZONTAL));
		targetTreeViewer = new TreeViewer(composite_11, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);
		targetTreeViewer.setSorter(new ViewerSorter());
		targetTreeViewer.getTree().addListener (SWT.Dispose, targetTreeListener);
		targetTreeViewer.getTree().addListener (SWT.KeyDown, targetTreeListener);
		targetTreeViewer.getTree().addListener (SWT.MouseMove, targetTreeListener);
		targetTreeViewer.getTree().addListener (SWT.MouseHover, targetTreeListener);

		TreeViewerFocusCellManager focusCellManager = new TreeViewerFocusCellManager(targetTreeViewer,new FocusCellOwnerDrawHighlighterForMultiselection(targetTreeViewer));
		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(targetTreeViewer) {
			protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
				return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
						|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.CR)
						|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC
						|| (event.keyCode == SWT.F2);
			}
		};

		TreeViewerEditor.create(targetTreeViewer, focusCellManager, actSupport, ColumnViewerEditor.TABBING_HORIZONTAL
				| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
				| ColumnViewerEditor.TABBING_VERTICAL | ColumnViewerEditor.KEYBOARD_ACTIVATION);

		/**
		 * RENAME NODE
		 */
		final TextCellEditor textCellEditor = new TextCellEditor(targetTreeViewer.getTree(),SWT.NONE);
		targetColumn = new TreeViewerColumn(targetTreeViewer, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);
		targetColumn.setEditingSupport(new EditingSupport(targetTreeViewer) {
			protected boolean canEdit(Object element) {
				OntologyTreeNode treeNode = ((OntologyTreeNode) element);
				System.out.println(treeNode.getName());
				if ((OntologyTreeNode)element == OntologyEditorView.getOntologyTargetTree().getI2B2RootNode()){
					System.err.println("You cannot rename the root node!");
					return false;
				}
				else			
					return true;
			}

			protected CellEditor getCellEditor(Object element) {
				return textCellEditor;
			}

			protected Object getValue(Object element) {
				return ((OntologyTreeNode) element).getName();
			}
			protected void setValue(Object element, Object value) {
				String newName = (String)value;
				OntologyTreeNode treeNode = ((OntologyTreeNode) element);
				if (!treeNode.isModifier())
					setPath(treeNode, newName, treeNode.getTreePathLevel());
				treeNode.getTargetNodeAttributes().setName(newName);
				treeNode.setName(newName);
				treeNode.getTargetNodeAttributes().getTargetNodeMap().put(Resource.I2B2.NODE.TARGET.C_TOOLTIP,newName);
				targetTreeViewer.update(element, null);
				setNotYetSaved(true);
				EditorTargetInfoView.refresh();
			}
			protected void setPath(OntologyTreeNode treeNode, String value, int hlevel){
				if (treeNode.isCustomNode()){
					String[] baseSplit = treeNode.getTargetNodeAttributes().getC_Basecode().split("\\|");
					baseSplit[hlevel+1] =value;
					String newBase = "";
					for (int i = 0; i < baseSplit.length; i++){
						newBase += baseSplit[i]+"|";
					}
					newBase = newBase.substring(0, newBase.length()-1);
					treeNode.getTargetNodeAttributes().setC_Basecode(newBase);
					treeNode.setID(value);
				}

				String treePath = treeNode.getTreePath();

				if (treeNode.isModifier()){
					System.out.println("MAPP: " + treeNode.getTargetNodeAttributes().getM_applied_path());
				}

				String [] pathSplit = treePath.split("\\\\");
				pathSplit[hlevel+1]=value;
				String newPath = "";
				for (int i = 0; i<pathSplit.length; i++){
					newPath+=pathSplit[i]+"\\";
				}
				treeNode.setTreePath(newPath);
				for (OntologyTreeNode child : treeNode.getChildren()){
					setPathChildren(child, value, hlevel, treeNode.getTreePath());
				}
			}

			protected void setPathChildren(OntologyTreeNode treeNode, String value, int hlevel, String applied_path){
				if (treeNode.isCustomNode()){
					String[] baseSplit = treeNode.getTargetNodeAttributes().getC_Basecode().split("\\|");
					baseSplit[hlevel+1] =value;
					String newBase = "";
					for (int i = 0; i < baseSplit.length; i++){
						newBase += baseSplit[i]+"|";
					}
					newBase = newBase.substring(0, newBase.length()-1);
					treeNode.getTargetNodeAttributes().setC_Basecode(newBase);
					treeNode.setID(value);
				}

				String treePath = treeNode.getTreePath();

				if (treeNode.isModifier()){
					treeNode.getTargetNodeAttributes().setM_applied_path(applied_path);
				}

				String [] pathSplit = treePath.split("\\\\");
				pathSplit[hlevel+1]=value;
				String newPath = "";
				for (int i = 0; i<pathSplit.length; i++){
					newPath+=pathSplit[i]+"\\";
				}
				treeNode.setTreePath(newPath);
				for (OntologyTreeNode child : treeNode.getChildren()){
					setPathChildren(child, value, hlevel, applied_path);
				}
			}
		});

		targetTreeViewer.addDragSupport(operations, transferTypes, new NodeMoveDragListener(
				targetTreeViewer));
		NodeDropListener nodeDropListener = new NodeDropListener(targetTreeViewer);

		targetTreeViewer.addDropSupport(operations, transferTypes, nodeDropListener);
		targetTreeViewer.getTree().addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.DEL) {
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

		targetTreeViewer.addDoubleClickListener(doubleClickListener); 
		searchClearButtonTarget.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				searchTextTarget.setText("");
			}
		});

		stagingTreeViewer.addDragSupport(operations, transferTypes, new NodeDragListener(stagingTreeViewer));
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
						System.out.println("node treepath: "+ node.getTreePath());
						EditorStagingInfoView.setNode(node);
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
				ActionCommand command  = new ActionCommand(Resource.ID.Command.IOE.DELETETARGET);
				command.addParameter(Resource.ID.Command.IOE.DELETETARGET_ATTRIBUTE_TARGETID, versionCombo.getText());
				Application.executeCommand(command);

				versionCombo.setText(""+getTargetInstance().getSelectedTarget().getVersion());
				versionCombo.getParent().setFocus();
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
				ProgressView.setProgress(0, "Saving...", "");
				Application.executeCommand(Resource.ID.Command.IOE.SAVETARGET);
				ProgressView.setProgress(100, "Saving...(done)", "");
			}
		});
		btnNewVersion.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
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
				if (Integer.parseInt(versionCombo.getText()) != getTargetInstance().getSelectedTarget().getVersion()){
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
							System.out.println("(Del) Version selected:" + versionCombo.getText());
							System.out.println("(Del) Previous Selected Verions:" + getTargetInstance().getPreviousSelectedVersion().getVersion());

							ActionCommand command  = new ActionCommand(Resource.ID.Command.IOE.DELETETARGET);
							command.addParameter(Resource.ID.Command.IOE.DELETETARGET_ATTRIBUTE_TARGETID, versionCombo.getText());
							Application.executeCommand(command);

							versionCombo.setText(""+getTargetInstance().getSelectedTarget().getVersion());
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
							versionCombo.setText(""+getTargetInstance().getSelectedTarget().getVersion());
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
				OntologyTreeNode node = OntologyEditorView.getOntologyTargetTree().getI2B2RootNode();
				System.out.println(isNotYetSaved());
				if (node.getChildCount()>0 && isNotYetSaved()) {
					boolean confirm = MessageDialog.openConfirm(Application.getShell(), "Target not saved!","The target tree has not been saved,\n" +
							"do you want to save it first?");
					if (confirm)
					{
						/**
						 * SaveTarget
						 */
						Application.executeCommand("de.umg.mi.idrt.ioe.SaveTarget");
					}
				}
				/**
				 * UploadProjectCommand
				 */
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

		if (  OntologyEditorView.getTargetInstance().getSelectedTarget() != null ){ 
			setTargetNameVersion( getTargetInstance().getSelectedTarget().getTargetDBSchema(), ""+ getTargetInstance().getSelectedTarget().getVersion() );
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
					EditorStagingInfoView.setNode(node);
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
	}

	public static boolean isInit() {
		return init;
	}

	public static boolean isNotYetSaved() {
		return notYetSaved;
	}

	public static boolean isShowSubNodes() {
		return showSubNodes;
	}

	private static void markNodes(OntologyTreeNode node, String text, TreeViewer viewer) {
		for (OntologyTreeNode child : node.getChildren()) {
			markNodes(child, text,viewer);
		}
		if (node.getName().toLowerCase().contains(text.toLowerCase())) {
			node.setSearchResult(true);
			while (node.getParent()!=null) {
				node = node.getParent();
				viewer.setExpandedState(node, true);
			}
		}
		else {
			node.setSearchResult(false);
		}
	}

	public static void refreshTargetName() {
		if(lblTargetName != null) {
			if (getTargetInstance().getSelectedTarget() != null && !getTargetInstance().getSelectedTarget().getTargetDBSchema().isEmpty()) {
				lblTargetName.setText(getTargetInstance().getSelectedTarget().getTargetDBSchema());
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
			for (int i = 0; i<getTargetInstance().getSelectedTargetInstance().getTargetsList().size();i++) {
				list.add(getTargetInstance().getSelectedTargetInstance().getTargetsList().get(i).getVersion());
			}
			Collections.sort(list);
			for (Integer i : list) {
				addVersionName(""+i);
			}

			if (getTargetInstance().getSelectedTarget() != null){
				versionCombo.setText(String.valueOf(getTargetInstance().getSelectedTarget().getVersion()));
			}

			composite_2.layout();
		} else {
			System.err.println("versionCombo null");
			Console.error("versionCombo is null, so don't refresh the version number");
		}
	}

	public static void removeVersionFromCombo(String version) {
		if ( versionCombo != null ){
			System.out.println("VERSION: " + version + " / oldSelectedVersion:" + getTargetInstance().getPreviousSelectedVersion().getVersion());
			versionCombo.remove(version);

		} else {
			Console.error("versionCombo is null, so don't remove an entry from it");
		}
	}

	private static void renameNode() {
		Application.executeCommand("de.umg.mi.idrt.ioe.renameNode");
	}

	private static void clearSearch(OntologyTreeNodeList nodeLists, TreeViewer treeViewer) {
		for (OntologyTreeNode node : nodeLists.getStringPathToNode().values()){
			node.setSearchResult(false);
		}
	}

	private static void searchNode(OntologyTreeNodeList nodeLists, String text, OntologyTreeNode rootNode, TreeViewer treeViewer) {
		if (text.isEmpty()) {
			System.out.println("TEXT EMPTY");
			for (OntologyTreeNode node : nodeLists.getStringPathToNode().values()){
				node.setSearchResult(false);
			}
		}
		else {
			for (OntologyTreeNode node : nodeLists.getStringPathToNode().values()){
				if (node.getName().toLowerCase().contains(text.toLowerCase())){
					node.setSearchResult(true);
					while (node.getParent()!=null) {
						node = node.getParent();
						treeViewer.setExpandedState(node, true);
					}
				}
			}
		}
	}
	/**
	 * @param column the column to set
	 */
	public static void setColumn(TreeViewerColumn column) {
		OntologyEditorView.targetColumn = column;
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

	public static void setTargetInstance(String name, String description) {
		instanceName.setText(name);
		OntologyEditorView.getTargetInstance().getSelectedTargetInstance().setName(name);
		OntologyEditorView.getTargetInstance().getSelectedTargetInstance().setDescription(description);
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
		getTargetTreeViewer().setSelection(new StructuredSelection(node), true);
		getTargetTreeViewer().refresh();
	}

	/**
	 * @param showSubNodes the showSubNodes to set
	 */
	public static void setShowSubNodes(boolean showSubNodes) {
		OntologyEditorView.showSubNodes = showSubNodes;
	}

	public static void setStagingContent() {
		long time =System.currentTimeMillis();
		if (!init) {
			init();
		}
		
		//TODO
		stagingTreeViewer.getTree().removeAll();
//		stagingTreeViewer.setContentProvider(new TransmartTreeStagingContentProvider());		
//		stagingTreeViewer.setLabelProvider(new TransmartStyledViewTableLabelProvider());
//		stagingTreeViewer.setInput(new TransmartOntologyTreeContentProvider().getStagingModel());
		stagingTreeViewer.setContentProvider(new TreeStagingContentProvider());		
		stagingTreeViewer.setLabelProvider(new StyledViewTableLabelProvider());
		stagingTreeViewer.setInput(new OntologyTreeContentProvider().getStagingModel());
		stagingTreeViewer.expandToLevel(2);
		OntologyEditorView.getOntologyStagingTree().setTreeViewer(stagingTreeViewer);

		Menu menu = new Menu(stagingTreeViewer.getTree());
		MenuItem deleteStudy = new MenuItem(menu, SWT.PUSH);
		deleteStudy.setText("Delete Study");
		deleteStudy.addSelectionListener(new SelectionListener() {
			//TODO
			@Override
			public void widgetSelected(SelectionEvent e) {
				String studyID = currentStagingNode.getOntologyCellAttributes().getSEC_OBJ();
				System.out.println("DELETING: " + studyID);

				boolean confirm = MessageDialog.openConfirm(Application.getShell(), "Delete "+ studyID + "?", "Do you really want to delete the complete study: " + studyID+"?");
				if (confirm){
					TOSConnector.deleteStudy(currentStagingServer,studyID);
					OntologyTreeNode bla = OntologyEditorView.getOntologyTargetTree().getI2B2RootNode();
					//				System.out.println("childCount: " + bla.getChildCount());
					System.out.println(isNotYetSaved());
					if (bla.getChildCount()>0 && isNotYetSaved()) {
						confirm = MessageDialog.openConfirm(Application.getShell(), "Target not saved!","The target tree has not been saved,\n" +
								"do you want to save it?");
						if (confirm)
						{
							//save target
							Application.executeCommand("de.umg.mi.idrt.ioe.SaveTarget");
						}
					}
					Application.executeCommand("edu.goettingen.i2b2.importtool.OntologyEditorLoad");
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
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

				OntologyTreeNode node = OntologyEditorView.getCurrentStagingNode();
				stagingComposite.setRedraw(false);
				stagingTreeViewer.expandToLevel(node, TreeViewer.ALL_LEVELS);
				//				expandStagingChildren(node, true);
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
				stagingTreeViewer.collapseToLevel(node, TreeViewer.ALL_LEVELS);
				//				expandStagingChildren(node, false);
				stagingComposite.setRedraw(true);
			}
		});

		stagingTreeViewer.getTree().addMouseTrackListener(new MouseTrackListener() {

			@Override
			public void mouseEnter(MouseEvent arg0) {
				for (OntologyTreeNode node : highlightedTargetNodes) {
					node.setHighlighted(false);
					highlightedTargetNodes.remove(node);
				}
			}

			@Override
			public void mouseExit(MouseEvent arg0) {
				for (OntologyTreeNode node : highlightedTargetNodes) {
					node.setHighlighted(false);
				}
				highlightedTargetNodes.clear();
				targetTreeViewer.refresh();
			}

			@Override
			public void mouseHover(MouseEvent arg0) {
				long time = System.currentTimeMillis();
				if (highlightedTargetNodes!=null) {
					for (OntologyTreeNode node : highlightedTargetNodes) {
						node.setHighlighted(false);
					}
					highlightedTargetNodes.clear();
				}

				Point p = new Point(arg0.x,arg0.y);
				final TreeItem a = stagingTreeViewer.getTree().getItem(p);
				if (a!=null) {
					if (a.getData() instanceof OntologyTreeNode) {
						OntologyTreeNode node = (OntologyTreeNode) a.getData();
						String fullname = node.getOntologyCellAttributes().getC_FULLNAME();
						for (OntologyTreeNode target : myOntologyTree.getOntologyTreeTarget().getNodeLists().getStringPathToNode().values()) {
							if (System.currentTimeMillis()-time>500) {
								//Timeout, if it takes to long.
								break;
							}
							if (fullname.equals(target.getTargetNodeAttributes().getSourcePath())){
								if (target.isModifier()){
									if (node.getOntologyCellAttributes().getM_APPLIED_PATH().equals(target.getStagingModifierPath()))
										target.setHighlighted(true);
								}
								else{
									target.setHighlighted(true);
								}

								if (target.isHighlighted()){
									highlightedTargetNodes.add(target);
								}
							}
						}
					}
				}
				targetTreeViewer.refresh();
			}
		});
//		transmartTree.tryToAddNodes();
//		transmartTree.display();
		stagingTreeViewer.getTree().setMenu(menu);
		stagingComposite.layout();
		mainComposite.layout();
		stagingColumn.getColumn().setWidth(stagingComposite.getBounds().width);
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
	}

	public static void setTargetContent(OntologyTree oTTarget) {
		targetTreeViewer.getTree().removeAll();
		final int IMAGE_MARGIN = 20;
		TreeTargetContentProvider treeContentProvider = new TreeTargetContentProvider();

		targetTreeViewer.setContentProvider(treeContentProvider);
		targetTreeViewer.setLabelProvider(new StyledViewTableLabelProvider());
		targetTreeViewer.setInput(new OntologyTreeContentProvider().getTargetModel());
		//		if (targetTreeViewer.getTree().getItems().length>=2)
		targetTreeViewer.expandToLevel(3);

		final Tree tree = targetTreeViewer.getTree();
		tree.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				TreeItem item = (TreeItem)event.item;
				if (item.getData() instanceof OntologyTreeNode) {
					OntologyTreeNode node = (OntologyTreeNode) item.getData();
					Image trailingImage;
					boolean start = node.getTargetNodeAttributes().getStartDateSource()!= null && !node.getTargetNodeAttributes().getStartDateSource().isEmpty();
					boolean end = node.getTargetNodeAttributes().getEndDateSource()!= null && !node.getTargetNodeAttributes().getEndDateSource().isEmpty();
					boolean startEnd = node.getTargetNodeAttributes().getEndDateSource()!= null && 
							!node.getTargetNodeAttributes().getEndDateSource().isEmpty() && 
							node.getTargetNodeAttributes().getStartDateSource()!= null && 
							!node.getTargetNodeAttributes().getStartDateSource().isEmpty();

					//					boolean name = !node.getTargetNodeAttributes().getName().isEmpty(); 

					if (start && !end) {
						trailingImage = GUITools
								.getImage(Resource.OntologyTree.HAS_START_DATE);
						if (trailingImage != null) {
							event.width += trailingImage.getBounds().width + IMAGE_MARGIN;
						}
					}
					else if (end && !start) {
						trailingImage = GUITools
								.getImage(Resource.OntologyTree.HAS_START_DATE);
						if (trailingImage != null) {
							event.width += trailingImage.getBounds().width + IMAGE_MARGIN;
						}
					}
					else if (startEnd) {
						trailingImage = GUITools
								.getImage(Resource.OntologyTree.HAS_START_END_DATE);
						if (trailingImage != null) {
							event.width += trailingImage.getBounds().width + IMAGE_MARGIN;
						}
					}
				}
			}
		});
		tree.addListener(SWT.PaintItem, new Listener() {
			public void handleEvent(Event event) {
				TreeItem item = (TreeItem)event.item;
				if (item.getData() instanceof OntologyTreeNode) {
					OntologyTreeNode node = (OntologyTreeNode) item.getData();
					Image trailingImage = null;
					boolean start = node.getTargetNodeAttributes().getStartDateSource()!= null && !node.getTargetNodeAttributes().getStartDateSource().isEmpty();
					boolean end = node.getTargetNodeAttributes().getEndDateSource()!= null && !node.getTargetNodeAttributes().getEndDateSource().isEmpty();
					boolean startEnd = node.getTargetNodeAttributes().getEndDateSource()!= null && 
							!node.getTargetNodeAttributes().getEndDateSource().isEmpty() && 
							node.getTargetNodeAttributes().getStartDateSource()!= null && 
							!node.getTargetNodeAttributes().getStartDateSource().isEmpty();

					if (start && !end) {
						trailingImage = GUITools
								.getImage(Resource.OntologyTree.HAS_START_DATE);
					}
					else if (end && !start) {
						trailingImage = GUITools
								.getImage(Resource.OntologyTree.HAS_END_DATE);
					}
					else if (startEnd) {
						trailingImage = GUITools
								.getImage(Resource.OntologyTree.HAS_START_END_DATE);
					}
					if (trailingImage != null) {
						int x = event.x + event.width + IMAGE_MARGIN;
						int itemHeight = tree.getItemHeight();
						int imageHeight = trailingImage.getBounds().height;
						int y = event.y + (itemHeight - imageHeight) / 2;
						event.gc.drawImage(trailingImage, x, y);
					}
				}
			}
		});
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
				if (stagingTreeViewer.getTree().getItemCount()>0)
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
				final TreeItem a = targetTreeViewer.getTree().getItem(p);
				if (a!=null) {
					if (a.getData() instanceof OntologyTreeNode) {
						OntologyTreeNode node = (OntologyTreeNode) a.getData();
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

		getOntologyTargetTree()
		.setTreeViewer(targetTreeViewer);

		Menu menu = new Menu(targetTreeViewer.getTree());

		MenuItem mntmSortChildren = new MenuItem(menu, SWT.PUSH);
		//TODO
		mntmSortChildren.setText("Sort Children");
		mntmSortChildren.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			@Override
			public void widgetSelected(SelectionEvent event) {
				OntologyTreeNode node = OntologyEditorView.getCurrentTargetNode();
				sortNode(node);

			}
		});

		//TODO
		//		new MenuItem(menu, SWT.SEPARATOR);
		//		MenuItem mntmgetChildren = new MenuItem(menu, SWT.PUSH);
		//		mntmgetChildren.setText("TEST");
		//		mntmgetChildren.addSelectionListener(new SelectionListener() {
		//
		//			@Override
		//			public void widgetDefaultSelected(SelectionEvent e) {
		//
		//			}
		//
		//			@Override
		//			public void widgetSelected(SelectionEvent event) {
		//
		//				System.out.println(getMyOntologyTree().getOntologyTreeSource().getNodeLists().getStringPathToNode().size());

		//				System.out.println("**** STAGING: ****");
		//				for (OntologyTreeNode child : getOntologyStagingTree().getRootNode().getChildren()) {
		//					System.out.println(child.getName());
		//					for (OntologyTreeNode cchild : child.getChildren()) {
		//						System.out.println("\t"+cchild.getName());
		//					}
		//				}
		//				System.out.println("*****************");
		//				System.out.println("**** TARGET: ****");
		//				for (OntologyTreeNode child : getOntologyTargetTree().getRootNode().getChildren()) {
		//					System.out.println(child.getName());
		//					for (OntologyTreeNode cchild : child.getChildren()) {
		//						System.out.println("\t"+cchild.getName());
		//					}
		//				}
		//				System.out.println("*****************");
		//				System.out.println("TARGET: " +getOntologyTargetTree().getNodeLists().getNumberOfItemNodes());
		//				System.out.println("STAGING:" +getOntologyStagingTree().getNodeLists().getNumberOfItemNodes());

		//				System.gc();
		//			}
		//		});
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
				//				expandTargetChildren(node, true);
				targetTreeViewer.expandToLevel(node, TreeViewer.ALL_LEVELS);
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
				//				expandTargetChildren(node, false);
				targetTreeViewer.collapseToLevel(node, TreeViewer.ALL_LEVELS);
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
		setTargetInstance(getTargetInstance().getSelectedTargetInstance().getName(),getTargetInstance().getSelectedTargetInstance().getDescription());

		//		versionCombo.removeAll();
		targetComposite.layout();
		mainComposite.layout();
		targetColumn.getColumn().setWidth(targetComposite.getBounds().width);
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
		//		System.out.println("Setting selectedTarget.setTargetDBSchema:" + name + "!");
		getTargetInstance().getSelectedTarget().setTargetDBSchema(name);

		composite_2.layout();
	}

	//	private static void unmarkAllNodes(OntologyTreeNode node) {
	//
	//		for (OntologyTreeNode child : node.getChildren()) 
	//			unmarkAllNodes(child);
	//		node.setSearchResult(false);
	//	}

	@Override
	public void createPartControl(final Composite parent) {

		Bundle bundle = Activator.getDefault().getBundle();
		URL mainURL = FileLocator.find(bundle, new Path(""), null);
		try {
			URL mainFileUrl = FileLocator.toFileURL(mainURL);
			File mainPath = new File(mainFileUrl.getPath());	
			createDirs(mainPath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			File folder = FileHandler.getBundleFile("/temp/output/");
			File[] listOfFiles = folder.listFiles();

			for (File listOfFile : listOfFiles) {
				if (!listOfFile.getName().equals("ph") ) { 
					listOfFile.delete();
				}
			}

			folder = FileHandler.getBundleFile("/temp/");
			listOfFiles = folder.listFiles();

			for (File listOfFile : listOfFiles) {
				if (!listOfFile.getName().equals("ph") ) { 
					listOfFile.delete();
				}
			}

		}catch (Exception e) {
			e.printStackTrace();
		}

		mainComposite = parent;
		highlightedStagingNodes = new HashSet<OntologyTreeNode>();
		highlightedTargetNodes = new HashSet<OntologyTreeNode>();
		Label label = new Label(mainComposite, SWT.CENTER);
		label.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe",
				"images/idrt_01.png"));

		TargetInstances targetProjects = new TargetInstances();

		TargetInstance targetProject1 = new TargetInstance();
		targetProject1.setTargetProjectID(1);
		targetProject1.setName("TargetProject1Name");
		targetProject1.setDescription("TargetProject1Desc");

		TargetInstance targetProject2 = new TargetInstance();
		targetProject2.setTargetProjectID(3);
		targetProject2.setName("TargetProject2Name");
		targetProject2.setDescription("TargetProject2Desc");

		targetProjects.add(targetProject1);
		targetProjects.add(targetProject2);
		final int operations = DND.DROP_COPY | DND.DROP_MOVE; //
		final Transfer[] transferTypes = new Transfer[] { TextTransfer
				.getInstance(), I2b2ProjectTransferType.getInstance()};

		DropTarget dropTarget = new DropTarget(mainComposite, operations);
		dropTarget.setTransfer(transferTypes);

		dropListenerStaging = new DropTargetListener() {

			@Override
			public void dragEnter(DropTargetEvent event) {
				System.out.println("drag enter");
				System.out.println(event.toString());
				System.out.println(event.data);
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
			public void drop(final DropTargetEvent event) {
				TOSHandler.setCounter(0);
				System.out.println("???DROPPED! " + event.data);

				if (event.data instanceof Server){
					MessageDialog.openError(Application.getShell(), "Error", "You cannot drop this item here!");
				}
				else if (event.data instanceof I2b2Project){
					I2b2Project project = (I2b2Project) event.data;
					if (OntologyEditorView.getOntologyTargetTree()!=null) {
						OntologyTreeNode bla = OntologyEditorView.getOntologyTargetTree().getI2B2RootNode();
						if (bla.getChildCount()>0 && isNotYetSaved()) {
							boolean confirm = MessageDialog.openQuestion(Application.getShell(), "Target not saved!","The target tree has not been saved,\n" +
									"do you want to save it first?");
							if (confirm)
							{
								Application.executeCommand("de.umg.mi.idrt.ioe.SaveTarget");
							}
						}
					}
					Application.executeCommand("edu.goettingen.i2b2.importtool.OntologyEditorLoad");
					setTargetNameVersion(getLatestVersion(project.getName()));
			}

			//				TreeViewer targetTreeViewer = ServerView.getSourceServerViewer();
			//				IStructuredSelection selection = (IStructuredSelection) targetTreeViewer
			//						.getSelection();
			//				System.out.println(event.data);
			//				
			////				if (ServerList.isServer((String)event.data) ) {
			//				if (selection.getFirstElement() instanceof Server){
			//					MessageDialog.openError(Application.getShell(), "Error", "You cannot drop this item here!");
			//				}
			//				else if (((String)(event.data)).startsWith("\\i2b2")) {
			//					//do nothing
			//				}
			//				else if (((String)(event.data)).equals("stagingTreeViewer")) {
			//					System.err.println("staging node dropped");
			//				}
			//				else {
			//					if (OntologyEditorView.getOntologyTargetTree()!=null) {
			//						OntologyTreeNode bla = OntologyEditorView.getOntologyTargetTree().getI2B2RootNode();
			//						if (bla.getChildCount()>0 && isNotYetSaved()) {
			//							boolean confirm = MessageDialog.openQuestion(Application.getShell(), "Target not saved!","The target tree has not been saved,\n" +
			//									"do you want to save it first?");
			//							if (confirm)
			//							{
			//								Application.executeCommand("de.umg.mi.idrt.ioe.SaveTarget");
			//							}
			//						}
			//					}
			//					Application.executeCommand("edu.goettingen.i2b2.importtool.OntologyEditorLoad");
			//					setTargetNameVersion(getLatestVersion((String)(event.data)));
			//				}
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
			System.out.println("!!!DROPPED! " + event.data);
			if (event.data instanceof I2b2Project){
				I2b2Project project = (I2b2Project)event.data;
				setTargetNameVersion(project.getName(), getLatestVersion(project.getName()));
				
			}
			
//			if (ServerList.getTargetServers().containsKey(event.data) ) {
//				MessageDialog.openError(Application.getShell(), "Error", "You cannot drop this item here!");
//			}
//			else if (((String)(event.data)).startsWith("\\i2b2")) {
//				//do nothing
//			}
//			else {
//				setTargetNameVersion((String)event.data, getLatestVersion((String)(event.data)));
//			}
		}
		@Override
		public void dropAccept(DropTargetEvent event) {
		}
	};
}

private String getLatestVersion(String string) {
	if (  getTargetInstance().getSelectedTarget() != null ){ 
		return String.valueOf( getTargetInstance().getSelectedTarget().getVersion() ) ;
	} else {
		return "0";
	}
}
@Override
public void setFocus() {
}
private static void moveNodeDown(){
	TreeViewer targetTreeViewer = OntologyEditorView.getTargetTreeViewer();
	IStructuredSelection selection = (IStructuredSelection) targetTreeViewer
			.getSelection();

	List<MutableTreeNode> nodeList = selection.toList();

	for (int i = nodeList.size()-1;i>=0;i--){
		MutableTreeNode mNode = nodeList.get(i);
		if ( mNode instanceof OntologyTreeNode) {
			OntologyTreeNode node = (OntologyTreeNode) mNode;
			OntologyTreeNode parent = node.getParent();

			int max = parent.getChildren().size();
			int leading = 2;
			int zeros = max/10;
			while(zeros>1){
				zeros=zeros/10;
				leading++;
			}
			boolean isSorted = false;
			if (!node.getName().substring(0,leading).matches("[0-9]{"+leading+"}")){
				boolean cont = MessageDialog.openConfirm(Application.getShell(), "Sort Items?", "You have to sort the items first!\nContinue?");
				if (cont){
					sortNode(parent);
					isSorted=true;
				}
			}
			else 
				isSorted=true;

			if (isSorted){
				//find next item
				String nodeNumber = node.getName().substring(0,leading);
				int nextNumber = Integer.parseInt(nodeNumber)+1;
				if (nextNumber<=parent.getChildren().size()){
					for (OntologyTreeNode neighbors : parent.getChildren()){
						String name = neighbors.getName();

						if (name.startsWith(String.format("%0"+leading+"d", nextNumber))){
							//got previous item
							String newName;
							newName = name.substring(leading, name.length()).trim();
							newName=""+String.format("%0"+leading+"d", nextNumber-1)+" " +newName;
							neighbors.setName(newName);
							neighbors.getTargetNodeAttributes().setName(newName);
							newName = node.getName().substring(leading, node.getName().length()).trim();
							newName=""+String.format("%0"+leading+"d", nextNumber)+" " +newName;
							node.setName(newName);
							node.getTargetNodeAttributes().setName(newName);
							break;
						}
					}
					targetTreeViewer.refresh();
				}
				else 
					break;
			}
		}
	}
}

private static void moveNodeUp(){
	TreeViewer targetTreeViewer = OntologyEditorView.getTargetTreeViewer();
	IStructuredSelection selection = (IStructuredSelection) targetTreeViewer
			.getSelection();

	List<MutableTreeNode> nodeList = selection.toList();
	for (int i = 0; i<nodeList.size();i++){
		MutableTreeNode mNode = nodeList.get(i);
		if ( mNode instanceof OntologyTreeNode) {
			OntologyTreeNode node = (OntologyTreeNode) mNode;
			OntologyTreeNode parent = node.getParent();

			int max = parent.getChildren().size();
			int leading = 2;
			int zeros = max/10;
			while(zeros>1){
				zeros=zeros/10;
				leading++;
			}
			boolean isSorted = false;
			if (!node.getName().substring(0,leading).matches("[0-9]{"+leading+"}")){
				boolean cont = MessageDialog.openConfirm(Application.getShell(), "Sort Items?", "You have to sort the items first!\nContinue?");
				if (cont){
					sortNode(parent);
					isSorted=true;
				}
			}
			else 
				isSorted=true;

			if (isSorted){
				//find next item
				String nodeNumber = node.getName().substring(0,leading);
				int prevNumber = Integer.parseInt(nodeNumber)-1;
				if (prevNumber>0){
					for (OntologyTreeNode neighbors : parent.getChildren()){
						String name = neighbors.getName();

						if (name.startsWith(String.format("%0"+leading+"d", prevNumber))){
							//got previous item
							String newName;
							newName = name.substring(leading, name.length()).trim();
							newName=""+String.format("%0"+leading+"d", prevNumber+1)+" " +newName;
							neighbors.setName(newName);
							neighbors.getTargetNodeAttributes().setName(newName);
							newName = node.getName().substring(leading, node.getName().length()).trim();
							newName=""+String.format("%0"+leading+"d", prevNumber)+" " +newName;
							node.setName(newName);
							node.getTargetNodeAttributes().setName(newName);
							break;
						}
					}
					targetTreeViewer.refresh();
				}
				else 
					break;

			}
		}
	}
}

private static void sortNode(OntologyTreeNode node){
	int counter = 1;
	int max = node.getChildren().size();
	int leading = 2;
	int zeros = max/10;
	while(zeros>1){
		zeros=zeros/10;
		leading++;
	}
	Collections.sort(node.getChildren());
	Collections.reverse(node.getChildren());
	for (OntologyTreeNode child:node.getChildren()){
		String name = child.getName();
		String sortNumber;
		if (name.length()>leading){
			sortNumber = name.substring(0, leading);
		}
		else 
			sortNumber = name;

		String newName;
		if (sortNumber.matches("[0-9]{"+leading+"}")){
			newName = name.substring(leading, name.length()).trim();
			newName=""+String.format("%0"+leading+"d", counter)+" " +newName;
		}
		else {
			newName = name.trim();
			newName=""+String.format("%0"+leading+"d", counter)+" " +newName;
		}
		child.setName(newName);
		child.getTargetNodeAttributes().setName(newName);
		counter++;
	}
	targetTreeViewer.refresh();
}

public static TransmartOntologyTree getTransmartTree() {
	return transmartTree;
}

}
