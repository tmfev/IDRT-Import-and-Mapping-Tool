package de.umg.mi.idrt.ioe.view;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.handlers.IHandlerService;
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
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.ioe.Activator;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.I2B2ImportTool;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.SystemMessage;
import de.umg.mi.idrt.ioe.OntologyTree.NodeDragListener;
import de.umg.mi.idrt.ioe.OntologyTree.NodeDropListener;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.OntologyTree.TreeContentProvider;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;

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

	private static boolean init = false;
	private static TreeViewer sourceTreeViewer;
	private static TreeViewer targetTreeViewer;
	private static Composite sourceComposite;
	private static Composite targetComposite;
	private static Composite mainComposite;
	private static SashForm sash;

	
	private static Label lblSource;
	private static Label lblTarget;
	private static DropTargetListener dropListener;
	private static Composite composite;
	private static Composite composite_1;
	private static Composite composite_2;
	private static Button btnNewVersion;
	private static Composite composite_3;
	private static Label lblVersion;
	private static Label lblNameText;
	private static Label lblVersionText;

	public OntologyEditorView() {
	}

	@Override
	public void createPartControl(final Composite parent) {
		mainComposite = parent;
		Label _label = new Label(mainComposite, SWT.LEFT);
		_label.setBackground(SWTResourceManager.getColor(255, 255, 255));
		_label.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe",
				"images/IDRT_ME.gif"));
		final int operations = DND.DROP_COPY | DND.DROP_MOVE; //
		final Transfer[] transferTypes = new Transfer[] { TextTransfer
				.getInstance() };

		DropTarget dropTarget = new DropTarget(_label, operations);
		dropTarget.setTransfer(transferTypes);

		dropListener = new DropTargetListener() {

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

					IHandlerService handlerService = (IHandlerService) getSite()
							.getService(IHandlerService.class);
					try {
						handlerService.executeCommand(
								"edu.goettingen.i2b2.importtool.OntologyEditorLoad", null); 
						setTargetNameVersion((String)event.data, "0");
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
		dropTarget.addDropListener(dropListener);




	}
	/**
	 * @wbp.parser.entryPoint
	 */
	public static void init() {
		System.out.println("INIT!");


//		Shell shell = new Shell();
//		shell.setSize(844, 536);
//		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
//		mainComposite = new Composite(shell, SWT.NONE);
//		mainComposite.setLayout(new BorderLayout(0, 0));

		mainComposite.getChildren()[0].dispose();
		composite = new Composite(mainComposite, SWT.NONE);
		composite.setLayout(new BorderLayout(0, 0));

		sash = new SashForm(composite, SWT.NONE);
		sourceComposite = new Composite(sash, SWT.NONE);
		sourceComposite.setLayout(new BorderLayout(0, 0));
		targetComposite = new Composite(sash, SWT.NONE);
		targetComposite.setLayout(new BorderLayout(0, 0));
		targetTreeViewer = new TreeViewer(targetComposite, SWT.MULTI
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		int operations = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };


		NodeDropListener nodeDropListener = new NodeDropListener(targetTreeViewer);

		composite_2 = new Composite(targetComposite, SWT.NONE);
		composite_2.setLayoutData(BorderLayout.NORTH);
		composite_2.setLayout(new GridLayout(5, false));

		lblTarget = new Label(composite_2, SWT.NONE);
		lblTarget.setText("Target i2b2 Project:");
		System.out.println("lblTarget: " + lblTarget.getBounds());

		lblNameText = new Label(composite_2, SWT.NONE);
		lblNameText.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblVersion = new Label(composite_2, SWT.NONE);
		lblVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		lblVersion.setText("Version:");

		lblVersionText = new Label(composite_2, SWT.NONE);
		lblVersionText.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnNewVersion = new Button(composite_2, SWT.NONE);
		btnNewVersion.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/plus16.png"));
		btnNewVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
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

		sourceTreeViewer = new TreeViewer(sourceComposite, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);
		sourceTreeViewer.addDragSupport(operations, transferTypes, new NodeDragListener(
				sourceTreeViewer));

		composite_3 = new Composite(sourceComposite, SWT.NONE);
		composite_3.setLayoutData(BorderLayout.NORTH);
		lblSource = new Label(composite_3, SWT.NONE);
		lblSource.setSize(215, 15);
		lblSource.setText("Staging i2b2");
		sash.setWeights(new int[] {331, 333});

		composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayoutData(BorderLayout.NORTH);
		composite_1.setLayout(new GridLayout(1, false));
		sourceTreeViewer.addDropSupport(operations, transferTypes, dropListener);


		sourceTreeViewer.getTree().addSelectionListener(new SelectionAdapter() {
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

		targetTreeViewer.expandToLevel(8);

		i2b2ImportTool.getMyOntologyTrees().getOntologyTreeTarget()
		.setTreeViewer(targetTreeViewer);

		Menu menu = new Menu(targetTreeViewer.getTree());

		MenuItem mntmInsert = new MenuItem(menu, SWT.PUSH);
		mntmInsert.setImage(ResourceManager.getPluginImage(
				"edu.goettingen.i2b2.importtool", "images/edit-copy.png"));
		mntmInsert.setText("Insert");

		MenuItem mntmCombine = new MenuItem(menu, SWT.PUSH);
		mntmCombine.setImage(ResourceManager.getPluginImage(
				"edu.goettingen.i2b2.importtool",
				"images/format-indent-more.png"));
		mntmCombine.setText("Combine");

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
		targetTreeViewer.getTree().setMenu(menu);
		menu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
			}
		});


		targetComposite.layout();
		mainComposite.layout();
	}

	public static void setSourceContent() {

		if (!init) {
			init();
		}
		sourceTreeViewer.getTree().removeAll();
		TreeContentProvider treeContentProvider = new TreeContentProvider();

		treeContentProvider.setOT(i2b2ImportTool.getMyOntologyTrees()
				.getOntologyTreeSource());

		sourceTreeViewer.setContentProvider(treeContentProvider);		
		sourceTreeViewer.setLabelProvider(new ViewTableLabelProvider(sourceTreeViewer));

		OTtoTreeContentProvider oTreeContent = new OTtoTreeContentProvider();

		sourceTreeViewer.setInput(oTreeContent.getModel());
		sourceTreeViewer.expandToLevel(Resource.Options.EDITOR_SOURCE_TREE_OPENING_LEVEL);

		i2b2ImportTool.getMyOntologyTrees().getOntologyTreeSource()
		.setTreeViewer(sourceTreeViewer);


		sourceComposite.layout();
		mainComposite.layout();
		System.out.println("LENGTH: " + sourceTreeViewer.getTree().getItems().length);
	}


	public static TreeViewer getTargetTreeViewer() {
		return targetTreeViewer;
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
}
