package de.umg.mi.idrt.ioe.view;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;

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

	private static Composite sourceComposite;
	private static Composite targetComposite;
	private static Composite mainComposite;
	private static SashForm sash;
	
	private static TreeViewer _treeViewer = null;
	
	public OntologyEditorView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		
		mainComposite = parent;
		sash = new SashForm(mainComposite, SWT.NONE);
		sourceComposite = new Composite(sash, SWT.NONE);
		sourceComposite.setLayout(new FillLayout());
		targetComposite = new Composite(sash, SWT.NONE);
		targetComposite.setLayout(new FillLayout());
		
	}
	public static void setI2B2ImportTool(I2B2ImportTool i2b2ImportTool) {
		OntologyEditorView.i2b2ImportTool = i2b2ImportTool;
	}
	@Override
	public void setFocus() {
	}
	
	public static void setTargetContent(OntologyTree oTTarget) {
		_treeViewer = new TreeViewer(targetComposite, SWT.MULTI
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		int operations = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
		NodeDropListener nodeDropListener = new NodeDropListener(_treeViewer);
		nodeDropListener.setMyOT(i2b2ImportTool.getMyOntologyTrees());
		
		try {
			_treeViewer.addDropSupport(operations, transferTypes, nodeDropListener);
		} catch (Exception e2) {
			Console.error("Could not initialize Drop in the editor target view.");
		}
		
		
		TreeContentProvider treeContentProvider = new TreeContentProvider();
		treeContentProvider.setOT(i2b2ImportTool.getMyOntologyTrees()
				.getOntologyTreeTarget());

		_treeViewer.setContentProvider(treeContentProvider);
		// viewer.setContentProvider(new TreeContentProvider());
		_treeViewer.setLabelProvider(new ViewTableLabelProvider(_treeViewer));
		// viewer.setInput(ContentProviderTree.INSTANCE.getModel());
		// ContentProviderTree contentProviderTree = new ContentProviderTree(
		// _i2b2ImportTool.getMyOT().getOT() );
		// viewer.setInput(ContentProviderTree.INSTANCE.getModel());
		_treeViewer.setInput(new OTtoTreeContentProvider().getModel());

		_treeViewer.expandToLevel(8);

		i2b2ImportTool.getMyOntologyTrees().getOntologyTreeTarget()
				.setTreeViewer(_treeViewer);

		// final Menu menu = new Menu(viewer.getTree())
		final Menu menu = getContextMenu(_treeViewer.getTree());
		_treeViewer.getTree().setMenu(menu);
		menu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
			}
		});

		_treeViewer.getTree().addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {

				if (e.item == null || e.item.getData() == null) {
					System.out
							.println("WidgetSelected but no known node found!");
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
		targetComposite.layout();
		mainComposite.layout();
	}

	public static void setSourceContent() {
		TreeViewer sourceTreeViewer = new TreeViewer(sourceComposite, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);
		int operations = DND.DROP_COPY | DND.DROP_MOVE;

		Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
		sourceTreeViewer.addDragSupport(operations, transferTypes, new NodeDragListener(
				sourceTreeViewer));
		
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

		sourceTreeViewer.getTree().addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {

				// ViewTableNode viewNode = (ViewTableNode) e.item.item;
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
		sourceComposite.layout();
		mainComposite.layout();
		System.out.println("LENGTH: " + sourceTreeViewer.getTree().getItems().length);
	}
	private static Menu getContextMenu(Composite composite) {

		Menu menu = new Menu(composite);
		// editorTargetView.setMenu(menu);

		MenuItem mntmInsert = new MenuItem(menu, SWT.PUSH);
		mntmInsert.setImage(ResourceManager.getPluginImage(
				"edu.goettingen.i2b2.importtool", "images/edit-copy.png"));
		mntmInsert.setText("Insert");

		MenuItem mntmCombine = new MenuItem(menu, SWT.PUSH);
		mntmCombine.setImage(ResourceManager.getPluginImage(
				"edu.goettingen.i2b2.importtool",
				"images/format-indent-more.png"));
		mntmCombine.setText("Combine");

		return menu;
	}
	public static TreeViewer getTreeViewer() {
		return _treeViewer;
	}
	
	public static void setSelection(OntologyTreeNode node) {
		System.out.println("setSelection Target");
		getTreeViewer().expandToLevel(node, node.getTreePathLevel());
		getTreeViewer().setSelection(new StructuredSelection(node), true);
		getTreeViewer().refresh();
		Application.getEditorTargetInfoView().setNode(node);
	}
}
