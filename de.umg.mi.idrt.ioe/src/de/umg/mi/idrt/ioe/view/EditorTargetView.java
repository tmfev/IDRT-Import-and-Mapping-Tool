package de.umg.mi.idrt.ioe.view;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;

import de.umg.mi.idrt.ioe.Activator;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Debug;
import de.umg.mi.idrt.ioe.I2B2ImportTool;
import de.umg.mi.idrt.ioe.SystemMessage;
import de.umg.mi.idrt.ioe.OntologyTree.NodeDropListener;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.OntologyTree.TreeContentProvider;

import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.widgets.MenuItem;

public class EditorTargetView extends ViewPart {

	private I2B2ImportTool _i2b2ImportTool;
	private Composite _composite;
	private TreeViewer _treeViewer = null;
	private OntologyTree _tree;

	public EditorTargetView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		Debug.f("createPartControl", this);
		this._composite = parent;
		// Text text = new Text(this._composite, SWT.BORDER);
		// text.setText("EditorTargetView2");
		// setComposite();
	
		
		
	
	}

	@Override
	public void setFocus() {

	}

	/*
	 * public I2B2ImportTool initI2B2ImportTool(){ return _i2b2ImportTool = new
	 * I2B2ImportTool(null); }
	 */

	public I2B2ImportTool setI2B2ImportTool(I2B2ImportTool i2b2ImportTool) {
		return _i2b2ImportTool = i2b2ImportTool;
	}

	public I2B2ImportTool getI2B2ImportTool() {
		return _i2b2ImportTool;
	}

	public void setComposite(OntologyTree tree) {
		System.out.println("setCompostie!!");

		_tree = tree;

		if (_treeViewer == null) {

			TreeViewer viewer = new TreeViewer(_composite, SWT.MULTI
					| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

			_treeViewer = viewer;

		} else {
			// = viewer;
		}

		int operations = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
		NodeDropListener nodeDropListener = new NodeDropListener(_treeViewer);
		nodeDropListener.setMyOT(_i2b2ImportTool.getMyOntologyTrees());
		_treeViewer.addDropSupport(operations, transferTypes, nodeDropListener);
		TreeContentProvider treeContentProvider = new TreeContentProvider();
		treeContentProvider.setOT(this._i2b2ImportTool.getMyOntologyTrees()
				.getOntologyTreeTarget());
		// treeContentProvider.setViewTree(
		// this._i2b2ImportTool.getMyOntologyTrees().getViewTreeTarget() );

		_treeViewer.setContentProvider(treeContentProvider);
		// viewer.setContentProvider(new TreeContentProvider());
		_treeViewer.setLabelProvider(new ViewTableLabelProvider(_treeViewer));
		// viewer.setInput(ContentProviderTree.INSTANCE.getModel());
		// ContentProviderTree contentProviderTree = new ContentProviderTree(
		// _i2b2ImportTool.getMyOT().getOT() );
		// viewer.setInput(ContentProviderTree.INSTANCE.getModel());
		_treeViewer.setInput(new OTtoTreeContentProvider().getModel());

		_treeViewer.expandToLevel(8);

		this._i2b2ImportTool.getMyOntologyTrees().getOntologyTreeTarget()
				.setTreeViewer(_treeViewer);

		// final Menu menu = new Menu(viewer.getTree())
		final Menu menu = this.getContextMenu(_treeViewer.getTree());
		_treeViewer.getTree().setMenu(menu);
		menu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
				// Get rid of existing menu items
				// MenuItem[] items = menu.getItems();
				/*
				 * for (int i = 0; i < items.length; i++) { ((MenuItem)
				 * items[i]).dispose(); }
				 */
				// Add menu items for current selection
				// MenuItem newItem = new MenuItem(menu, SWT.NONE);
				// newItem.setText("Menu for " +
				// viewer2.getTree().getSelection()[0].getText());
			}
		});

		_treeViewer.getTree().addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {

				// ViewTableNode viewNode = (ViewTableNode) e.item.item;
				if (e.item == null || e.item.getData() == null) {
					System.out
							.println("WidgetSelected but no known node found!");
					return;
				}
				OntologyTreeNode node = (OntologyTreeNode) e.item.getData();
				// OTNode node = viewNode.getOTNode();

				if (node != null) {
					/*
					 * if (!node.getNodeType().equals( NodeType.ITEM )){
					 * 
					 * // //Activator.getDefault().getResource().
					 * getOntologyAnswersPreviewView().setItemNode(null); }
					 */

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

		this._composite.layout();
	}

	@Override
	public void dispose() {
		super.dispose();

		/*
		 * if (_i2b2ImportTool != null && _i2b2ImportTool.getMyOntologyTree() !=
		 * null){ _i2b2ImportTool.getMyOntologyTree().initiate(); }
		 */

	}

	public void clear() {

		if (_tree != null && _composite != null) {
			// _composite.r.remove( _tree );
		}

		if (_tree != null) {

		}

		// _composite.dispose();
	}

	public Composite getComposite() {
		return this._composite;
	}

	public Menu getContextMenu(Composite composite) {

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

	public TreeViewer getTreeViewer() {
		return _treeViewer;
	}

	public void setSelection(OntologyTreeNode node) {
		System.out.println("setSelection Target");
		getTreeViewer().expandToLevel(node, node.getTreePathLevel());
		getTreeViewer().setSelection(new StructuredSelection(node), true);
		getTreeViewer().refresh();
		Application.getEditorTargetInfoView().setNode(node);
	}
}
