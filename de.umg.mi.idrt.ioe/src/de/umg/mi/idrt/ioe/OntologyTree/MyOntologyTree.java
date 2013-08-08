package de.umg.mi.idrt.ioe.OntologyTree;

import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.ResourceManager;
import de.umg.mi.idrt.ioe.ActionCommand;
import de.umg.mi.idrt.ioe.Activator;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.Resource.I2B2.NODE.TYPE;
import de.umg.mi.idrt.ioe.SystemMessage;
import de.umg.mi.idrt.ioe.view.ViewTree;

/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> Department of
 *         Medical Informatics Goettingen www.mi.med.uni-goettingen.de
 * 
 *         main class managing and giving access to the source and target trees
 */

public class MyOntologyTree extends JPanel {

	private static final long serialVersionUID = -7345913467371670611L;

	// jTree components
	private OntologyTree _ontologyTreeSource = null;
	private OntologyTree _ontologyTreeTarget = null;
	private ViewTree viewTree = null;
	private ViewTree viewTreeTarget = null;

	JPopupMenu popup = null;

	// private tree components
	private OntologyTreeNode sourceRootNode = null;
	private OntologyTreeNode targetRootNode = null;

	/**
	 * The MyontolgoyTree class imports data, creates and manages a tree and
	 * exports this tree to I2B2.
	 */
	public MyOntologyTree() {
		super(new GridLayout(1, 0));
		initiate();

		Application.getStatusView().addMessage(
				new SystemMessage(Application.getResource().getText(
						"MyOT.INITIALIZED"), SystemMessage.MessageType.SUCCESS,
						SystemMessage.MessageLocation.MAIN));
	}

	/**
	 * Sets and/or resets the basic variables for this object.
	 * 
	 */
	public void initiate() {
		Console.info("Initiating source and target Tree.");
		this.createOntologyTreeSource();
		this.createOntologyTreeTarget();
	}

	/**
	 * Does things when something in the jTree has been selected.
	 * 
	 * @param event
	 *            the event
	 * @return void
	 */
	public void valueChanged(TreeSelectionEvent event) {
		String message = "";

		OntologyTreeNode node = (OntologyTreeNode) this._ontologyTreeSource
				.getLastSelectedPathComponent();

		// DEL set active node for value changed
		// Application.getResource().setActiveNode(node);
		// Activator.getDefault().getResource().setActiveNode(node);

		if (node != null) {

			Activator.getDefault().getResource().getEditorSourceInfoView()
					.setNode(node);

			message = "Selection in source tree changed to \'" + node.getName() + "\'.";

			Application.getStatusView().addMessage(
					new SystemMessage(message,
							SystemMessage.MessageType.SUCCESS));

		} else {
			// do nothing if the node is null or no NodeType is set
			message = "Selection in source tree changed, but the the new selection isn't a node.";
		}
		
		Console.info(message);
	}

	public void createOntologyTreeSource() {
		Console.info("Creating the source tree ...");

		OntologyTreeNode rootNode = new OntologyTreeNode(
				"OntologyTreeSourceRootNode");
		rootNode.setID("root");
		rootNode.setTreePath("\\");
		rootNode.setTreePathLevel(-1);
		rootNode.setNodeType(NodeType.ROOT);

		_ontologyTreeSource = new OntologyTree(rootNode);
		
	
		_ontologyTreeSource.getNodeLists().add(rootNode.getID(), rootNode.getTreePath(), rootNode);
		
		setSourceRootNode(rootNode);

		_ontologyTreeSource.setDragEnabled(true);
		_ontologyTreeSource.setAutoscrolls(true);
		_ontologyTreeSource.setRootVisible(false);

		_ontologyTreeSource.setTransferHandler(new OTTransferHandler());

		getOntologyTreeSource().updateUI();

		expandTreePaths(new TreePath(
				((OntologyTreeNode) _ontologyTreeSource.getTreeRoot())));

		// adding mouse adapter
		MouseAdapter ma = new MouseAdapter() {
			private void myPopupEvent(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				OntologyTree OT = (OntologyTree) e.getSource();
				TreePath path = OT.getPathForLocation(x, y);
				if (path == null)
					return;

				OT.setSelectionPath(path);

			}

			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger())
					myPopupEvent(e);
			}

			public void mouseReleased(MouseEvent e) {
				// TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
				if (e.isPopupTrigger())
					myPopupEvent(e);
			}
		};

		// set custom listener
		if (this._ontologyTreeSource != null) {
			// this.OT.addTreeSelectionListener(this);
			this._ontologyTreeSource.addMouseListener(ma);
			// Debug.d("# calling: setActive");
		}

		this.viewTree = new ViewTree();

	}

	public void createOntologyTreeTarget() {

		OntologyTreeNode rootNode = new OntologyTreeNode(
				"OntologyTreeTargetRootNode");

		rootNode.setID("root");
		rootNode.setTreePath("\\");
		rootNode.setTreePathLevel(-1);
		rootNode.setNodeType(NodeType.ROOT);

		this._ontologyTreeTarget = new OntologyTree(rootNode);

		_ontologyTreeTarget.getNodeLists().addIDtoPaths(rootNode.getID(),
				rootNode.getTreePath());
		_ontologyTreeTarget.getNodeLists().addNodeByID(rootNode.getID(),
				rootNode);
		_ontologyTreeTarget.getNodeLists().addNodyByPath(
				rootNode.getTreePath(), rootNode);
		_ontologyTreeTarget.getNodeLists().setNodeStatusByPath(
				rootNode.getTreePath(), ItemStatus.UNCHECKED);

		this.setTargetRootNode(rootNode);

		// OTNode targetNode = new OTNode( "TargetOntology" );

		OntologyTreeNode subRootNode = new OntologyTreeNode("Target-Ontology");

		subRootNode.setID("i2b2");
		subRootNode.setTreePath("\\i2b2\\");
		subRootNode.setTreePathLevel(0);
		subRootNode.setType(TYPE.ONTOLOGY_TARGET);
		subRootNode.setNodeType(NodeType.I2B2ROOT);

		rootNode.add(subRootNode);

		_ontologyTreeTarget.getNodeLists().addIDtoPaths(subRootNode.getID(),
				subRootNode.getTreePath());
		_ontologyTreeTarget.getNodeLists().addNodeByID(subRootNode.getID(),
				subRootNode);
		_ontologyTreeTarget.getNodeLists().addNodyByPath(
				subRootNode.getTreePath(), subRootNode);
		_ontologyTreeTarget.getNodeLists().setNodeStatusByPath(
				subRootNode.getTreePath(), ItemStatus.UNCHECKED);

		// set some options
		OntologyTreeModel OTModel = new OntologyTreeModel(
				this.getTargetTreeRoot());
		getOntologyTreeTarget().setModel(OTModel);
		getOntologyTreeTarget().setEditable(true);

		getOntologyTreeTarget().setTransferHandler(new TransferHandler() {

			public boolean canImport(TransferHandler.TransferSupport support) {
				if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)
						|| !support.isDrop()) {
					return false;
				}

				JTree.DropLocation dropLocation = (JTree.DropLocation) support
						.getDropLocation();

				return dropLocation.getPath() != null;
			}

			public boolean importData(TransferHandler.TransferSupport support) {
				if (!canImport(support)) {
					return false;
				}

				JTree.DropLocation dropLocation = (JTree.DropLocation) support
						.getDropLocation();

				TreePath path = dropLocation.getPath();

				Transferable transferable = support.getTransferable();

				String transferData;
				try {
					transferData = (String) transferable
							.getTransferData(DataFlavor.stringFlavor);
				} catch (IOException e) {
					return false;
				} catch (UnsupportedFlavorException e) {
					return false;
				}

				int childIndex = dropLocation.getChildIndex();
				if (childIndex == -1) {
					childIndex = getOntologyTreeTarget().getModel()
							.getChildCount(path.getLastPathComponent());
				}

				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(
						transferData);
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) path
						.getLastPathComponent();
				((DefaultTreeModel) getOntologyTreeTarget().getModel())
						.insertNodeInto(newNode, parentNode, childIndex);

				TreePath newPath = path.pathByAddingChild(newNode);
				getOntologyTreeTarget().makeVisible(newPath);
				getOntologyTreeTarget().scrollRectToVisible(
						getOntologyTreeTarget().getPathBounds(newPath));

				return true;
			}
		});

		this.setTargetRootNode(getOntologyTreeTarget().getRootNode());

		// adding mouse adapter
		MouseAdapter ma = new MouseAdapter() {
			private void myPopupEvent(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				OntologyTree OTTarget = (OntologyTree) e.getSource();
				TreePath path = OTTarget.getPathForLocation(x, y);
				if (path == null)
					return;

				OTTarget.setSelectionPath(path);

			}

			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger())
					myPopupEvent(e);
			}

			public void mouseReleased(MouseEvent e) {
				// TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
				if (e.isPopupTrigger())
					myPopupEvent(e);
			}
		};

		// set custom renderer and listener
		if (this._ontologyTreeTarget != null) {

			// this.OTTarget.addTreeSelectionListener(this);
			this._ontologyTreeTarget.addMouseListener(ma);
		}

		this.viewTreeTarget = new ViewTree();

	}

	/******************************************************
	 * 
	 * Node operations
	 * 
	 * ****************************************************
	 */

	/**
	 * getTreeRoot: return the tree root object for this instance
	 * 
	 * @return this tree root
	 */

	public OntologyTreeNode getTreeRoot() {

		if (this._ontologyTreeSource == null)
			Console.error("OT is null while trying to get the tree root!");

		return this._ontologyTreeSource.getTreeRoot();
	}

	public OntologyTreeNode getTargetTreeRoot() {

		if (this._ontologyTreeTarget == null) {
			Console.error("OTTarget is null while trying to get the tree root!");
			return null;
		}
		return this._ontologyTreeTarget.getTreeRoot();
	}

	public OntologyTreeNode getStudyNode() {
		return this._ontologyTreeSource.getRootNode();
	}

	/**
	 * Returns the instance of the MyOTItemLists object.
	 * 
	 * @return the MyOTItemLists object
	 */

	public OTItemLists getItemLists() {
		return this._ontologyTreeSource.getItemLists();
	}

	protected void expandTreePaths(TreePath path) {
		this._ontologyTreeSource.expandPath(path);
		final Object node = path.getLastPathComponent();
		final int n = this._ontologyTreeSource.getModel().getChildCount(node);

		for (int index = 0; index < n; index++) {
			final Object child = this._ontologyTreeSource.getModel().getChild(
					node, index);
			OntologyTreeNode treeNode = (OntologyTreeNode) child;
			/*
			 * if (treeNode.getNodeType().equals(NodeType.ITEM)) { // if its an
			 * item, leaf it closed and return return; }
			 */
			if (this._ontologyTreeSource.getModel().getChildCount(child) > 0) {
				expandTreePaths(path.pathByAddingChild(child));
			}
		}
	}

	public OntologyTree getOntologyTreeSource() {
		return this._ontologyTreeSource;
	}

	public OntologyTree getOntologyTreeTarget() {
		return this._ontologyTreeTarget;
	}

	public String createLongStringID(int id) {
		return String.format("%08d", id);
	}

	public String getVisitFromStringPath(String stringPath) {
		String[] split = stringPath.split("/");
		if (split[2] != null) {
			return split[2].toString();
		}

		return "";
	}

	/**
	 * @param _lastActiveNode
	 *            the activeItemNode to set
	 */
	public void setSourceRootNode(OntologyTreeNode sourceRootNode) {
		this.sourceRootNode = sourceRootNode;
	}

	/**
	 * @return the activeItemNode
	 */
	public OntologyTreeNode getSourceRootNode() {
		return sourceRootNode;
	}

	/**
	 * @param _lastActiveNode
	 *            the activeItemNode to set
	 */
	public void setTargetRootNode(OntologyTreeNode targetRootNode) {
		this.targetRootNode = targetRootNode;
	}

	/**
	 * @return the activeItemNode
	 */
	public OntologyTreeNode getTargetRootNode() {
		return targetRootNode;
	}

	/* OT Commands */
	public OntologyTreeNode copySourceNodeToTarget(OntologyTreeNode sourceNode,
			OntologyTreeNode targetNode) {

		if (sourceNode != null) {
			System.out.println("- do:" + sourceNode.getName() + " -> "
					+ targetNode.getName() + "!");

			OntologyTreeNode newNode = new OntologyTreeNode(
					sourceNode.getName());

			newNode.setID(sourceNode.getID());
			newNode.setType(Resource.I2B2.NODE.TYPE.ONTOLOGY_TARGET);

			/*
			 * copy attriubutes
			 */

			if (sourceNode.getOntologyCellAttributes() != null) {

				newNode.getTargetNodeAttributes().setSourcePath(
						sourceNode.getOntologyCellAttributes().getC_FULLNAME());

				newNode.getTargetNodeAttributes().setVisualattributes(
						sourceNode.getOntologyCellAttributes()
								.getC_VISUALATTRIBUTES());
				newNode.getTargetNodeAttributes().setName(sourceNode.getName());
				newNode.getTargetNodeAttributes().setChanged(true);
			}

			targetNode.add(newNode);

			this.getOntologyTreeTarget().getNodeLists().add(newNode);

			if (sourceNode.hasChildren()) {
				for (int x = 0; x < sourceNode.getChildCount(); x++) {
					OntologyTreeNode child = (OntologyTreeNode) sourceNode
							.getChildAt(x);
					copySourceNodeToTarget(child, newNode);
				}
			}

			return newNode;
		}
		return null;
	}

	/* OT Commands */
	public void setTargetAttributesAsSourcePath(OntologyTreeNode sourceNode,
			OntologyTreeNode targetNode, String attribute) {

		System.out.println("------");
		System.out.println("setTargetAttributesAsSourcePath:");

		if (sourceNode != null) {
			System.out.println(" - do:" + sourceNode.getName() + " -> "
					+ targetNode.getName() + "!");

			System.out.println(" - attribute: " + attribute);
			System.out
					.println(" - sourceNodePath: " + sourceNode.getTreePath());
			System.out
					.println(" - targetNode.startDateSource: "
							+ targetNode.getTargetNodeAttributes()
									.getStartDateSource());
			setTargetAttribute(targetNode, "startDateSource",
					sourceNode.getTreePath());
			System.out
					.println(" - targetNode.startDateSource (after): "
							+ targetNode.getTargetNodeAttributes()
									.getStartDateSource());
		}
	}

	public void setTargetAttribute(OntologyTreeNode targetNode,
			String attribute, String value) {

		targetNode.getTargetNodeAttributes().setStartDateSourcePath(value);

		if (targetNode.hasChildren()) {

			for (int x = 0; x < targetNode.getChildCount(); x++) {
				OntologyTreeNode child = (OntologyTreeNode) targetNode
						.getChildAt(x);
				setTargetAttribute(child, attribute, value);
			}

		}

	}

	public void dropCommandCopyNodes(final String sourcePath,
			final String targetPath) {

		// createPopupMenu();

		System.out.println("createPopupMenu():");
		Shell shell = Application.getShell();
		System.out.println(" - shell is null? "
				+ (shell == null ? "true" : "false"));

		final Shell dialog = new Shell(shell, SWT.APPLICATION_MODAL | SWT.NONE
				| SWT.TOOL);

		dialog.setLocation(PlatformUI.getWorkbench().getDisplay()
				.getCursorLocation());

		final Composite actionMenu = new Composite(dialog, SWT.NONE);

		actionMenu.setLayout(new org.eclipse.swt.layout.GridLayout(2, false));

		Button btnInsert = new Button(actionMenu, SWT.NONE);
		btnInsert.setImage(ResourceManager.getPluginImage(
				"edu.goettingen.i2b2.importtool", "images/edit-copy.png"));
		btnInsert.setBounds(0, 0, 75, 25);
		btnInsert.setText("insert nodes here");
		btnInsert.addSelectionListener(new SelectionAdapter() {
			// SelectionAdapter Methode
			public void widgetSelected(SelectionEvent e) {
				// appletString = "Button eins geklickt"; //später wird dieser
				// String in einem Label dargestellt
				ActionCommand command = new ActionCommand(
						Resource.ID.Command.OTCOPY);
				command.addParameter(
						Resource.ID.Command.OTCOPY_ATTRIBUTE_SOURCE_NODE_PATH,
						sourcePath);
				command.addParameter(
						Resource.ID.Command.OTCOPY_ATTRIBUTE_TARGET_NODE_PATH,
						targetPath);
				Application.executeCommand(command);
				dialog.close();
				Application.getEditorTargetView().getTreeViewer().expandAll();
				Application.getEditorTargetView().getTreeViewer().refresh();
			}
		});

		btnInsert.addMouseTrackListener(new MouseTrackAdapter() {

			@Override
			public void mouseHover(org.eclipse.swt.events.MouseEvent e) {
				// TODO Auto-generated method stub

				System.out.println(" ... hovering ...");

				/*
				 * Composite composite = new Composite(actionMenu, SWT.NONE);
				 * composite.setBackground(SWTResourceManager.getColor(SWT.
				 * COLOR_TITLE_BACKGROUND_GRADIENT));
				 * 
				 * Label lblInsertTest = new Label(composite, SWT.NONE);
				 * lblInsertTest.setBounds(0, 0, 55, 15);
				 * lblInsertTest.setText("insert Test"); new Label(actionMenu,
				 * SWT.NONE);
				 * 
				 * actionMenu.pack();
				 */

			}

		});

		Button btnCombine = new Button(actionMenu, SWT.NONE);
		btnCombine.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1));
		btnCombine.setImage(ResourceManager.getPluginImage(
				"edu.goettingen.i2b2.importtool",
				"images/format-indent-more.png"));
		btnCombine.setBounds(0, 0, 75, 25);
		btnCombine.setText("combine nodes here");
		btnCombine.addSelectionListener(new SelectionAdapter() {
			// SelectionAdapter Methode
			public void widgetSelected(SelectionEvent e) {
				ActionCommand command = new ActionCommand(
						Resource.ID.Command.OTSETTARGETATTRIBUTE);
				command.addParameter(
						Resource.ID.Command.OTSETTARGETATTRIBUTE_ATTRIBUTE_SOURCE_NODE_PATH,
						sourcePath);
				command.addParameter(
						Resource.ID.Command.OTSETTARGETATTRIBUTEY_ATTRIBUTE_TARGET_NODE_PATH,
						targetPath);
				command.addParameter(
						Resource.ID.Command.OTSETTARGETATTRIBUTEY_ATTRIBUTE_ATTRIBUTE,
						"startDateSource");
				Application.executeCommand(command);
				dialog.close();

			}
		});

		actionMenu.pack();
		dialog.pack();
		dialog.open();

	}

	public ViewTree getViewTree() {
		return this.viewTree;
	}

	public ViewTree getViewTreeTarget() {
		return this.viewTreeTarget;
	}

	public void createPopupMenu() {
		System.out.println("createPopupMenu():");
		Shell shell = Application.getShell();
		System.out.println(" - shell is null? "
				+ (shell == null ? "true" : "false"));

		final Shell dialog = new Shell(shell, SWT.APPLICATION_MODAL | SWT.NONE
				| SWT.ON_TOP);

		dialog.setLocation(PlatformUI.getWorkbench().getDisplay()
				.getCursorLocation());

		Composite actionMenu = new Composite(dialog, SWT.NONE);

		actionMenu.setLayout(new org.eclipse.swt.layout.GridLayout(2, false));

		Button btnInsert = new Button(actionMenu, SWT.NONE);
		btnInsert.setImage(ResourceManager.getPluginImage(
				"edu.goettingen.i2b2.importtool", "images/edit-copy.png"));
		btnInsert.setBounds(0, 0, 75, 25);
		btnInsert.setText("insert nodes here");

		Button btnCombine = new Button(actionMenu, SWT.NONE);
		btnCombine.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1));
		btnCombine.setImage(ResourceManager.getPluginImage(
				"edu.goettingen.i2b2.importtool",
				"images/format-indent-more.png"));
		btnCombine.setBounds(0, 0, 75, 25);
		btnCombine.setText("combine nodes here");

		actionMenu.pack();
		dialog.pack();
		dialog.open();

		Application.getEditorTargetView().getTreeViewer().expandAll();
		Application.getEditorTargetView().getTreeViewer().refresh();

	}
}
