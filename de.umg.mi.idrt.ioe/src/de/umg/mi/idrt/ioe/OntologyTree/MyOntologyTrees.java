package de.umg.mi.idrt.ioe.OntologyTree;

import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.ResourceManager;

import de.umg.mi.idrt.ioe.ActionCommand;
import de.umg.mi.idrt.ioe.Activator;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.Resource.I2B2.NODE.TYPE;
import de.umg.mi.idrt.ioe.SystemMessage;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

/**
 * @author Christian Bauer <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> 
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 * 			Department of Medical Informatics Goettingen 
 * 			www.mi.med.uni-goettingen.de
 *         main class managing and giving access to the source and target trees
 */

public class MyOntologyTrees{

	// jTree components
	private OntologyTree ontologyTreeSource = null;
	private OntologyTree ontologyTargetTree = null;

	// private tree components
	private OntologyTreeNode sourceRootNode = null;
	private OntologyTreeNode targetRootNode = null;

	/**
	 * The MyontolgoyTree class imports data, creates and manages a tree and
	 * exports this tree to I2B2.
	 */
	public MyOntologyTrees() {
//		super(new GridLayout(1, 0));
		
		initiate();

		
	}

	/* OT Commands */
	public void copySourceNodeToTarget(final OntologyTreeNode sourceNode , final OntologyTreeNode targetNode) {
		IStructuredSelection selection = (IStructuredSelection) OntologyEditorView.getStagingTreeViewer()
				.getSelection();
		Iterator<OntologyTreeNode> nodeIterator = selection.iterator();


		if (!targetNode.isLeaf()) {
			int minLevel = Integer.MAX_VALUE;

			while (nodeIterator.hasNext()) {
				final OntologyTreeNode sourceNode1 = nodeIterator.next();

				if (minLevel > sourceNode1.getTreePathLevel())
					minLevel = sourceNode1.getTreePathLevel();
			}
			nodeIterator = selection.iterator();

			while (nodeIterator.hasNext()) {
				final OntologyTreeNode sourceNode1 = nodeIterator.next();
				if (sourceNode1.getTreePathLevel() == minLevel){
					deepCopy(sourceNode1, targetNode);
				}
			}

		}
		else {
			//TODO
			while (nodeIterator.hasNext()) {
				final OntologyTreeNode sourceNode1 = nodeIterator.next();
				targetNode.getTargetNodeAttributes().addStagingPath(sourceNode1.getOntologyCellAttributes().getC_FULLNAME());
			}

		}
		OntologyEditorView.setSelection(targetNode);
		OntologyEditorView.getTargetTreeViewer().setExpandedState(targetNode,
				true);
	
	}

	public String createLongStringID(int id) {
		return String.format("%08d", id);
	}

	public void createOntologyTreeSource() {
		Console.info("Creating the source tree ...");

		OntologyTreeNode rootNode = new OntologyTreeNode(
				"OntologyTreeSourceRootNode");
		rootNode.setID("root");
		rootNode.setTreePath("\\");
		rootNode.setNodeType(NodeType.TREEROOT);
		rootNode.setTreePathLevel(-1);

		ontologyTreeSource = new OntologyTree(rootNode);

		ontologyTreeSource.getNodeLists().add(rootNode.getID(),
				rootNode.getTreePath(), rootNode);

		setSourceRootNode(rootNode);

		ontologyTreeSource.setDragEnabled(true);
		ontologyTreeSource.setAutoscrolls(true);
		ontologyTreeSource.setRootVisible(false);

		getOntologyTreeSource().updateUI();

		//		expandTreePaths(new TreePath(
		//				((OntologyTreeNode) _ontologyTreeSource.getTreeRoot())));

		// adding mouse adapter
		MouseAdapter ma = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger())
					myPopupEvent(e);
			}

			public void mouseReleased(MouseEvent e) {
				// TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
				if (e.isPopupTrigger())
					myPopupEvent(e);
			}

			private void myPopupEvent(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				OntologyTree OT = (OntologyTree) e.getSource();
				TreePath path = OT.getPathForLocation(x, y);
				if (path == null)
					return;

				OT.setSelectionPath(path);

			}
		};

		// set custom listener
		if (this.ontologyTreeSource != null) {
			// this.OT.addTreeSelectionListener(this);
			this.ontologyTreeSource.addMouseListener(ma);
		}


	}

	public void createOntologyTreeTarget() {

		OntologyTreeTargetRootNode rootNode = new OntologyTreeTargetRootNode(
				"OntologyTreeTargetRootNode");

		rootNode.setID("root");
		rootNode.setTreePath("\\");
		rootNode.setTreePathLevel(-1);
		rootNode.setNodeType(NodeType.TREEROOT);

		ontologyTargetTree = new OntologyTree(rootNode);

		ontologyTargetTree.getNodeLists().addNodyByPath(
				rootNode.getTreePath(), rootNode);

		this.setTargetRootNode(rootNode);

		OntologyTreeNode targetOntologyi2b2RootNode = new OntologyTreeNode("Target-Ontology");
		targetOntologyi2b2RootNode.setID("i2b2");
		targetOntologyi2b2RootNode.setTreePath("\\i2b2\\");
		targetOntologyi2b2RootNode.setTreePathLevel(0);
		targetOntologyi2b2RootNode.setType(TYPE.ONTOLOGY_TARGET);
		targetOntologyi2b2RootNode.setNodeType(NodeType.I2B2ROOT);
		targetOntologyi2b2RootNode.getTargetNodeAttributes().addStagingPath("\\i2b2\\");
		targetOntologyi2b2RootNode.getTargetNodeAttributes().setDimension(Dimension.CONCEPT_DIMENSION);
		targetOntologyi2b2RootNode.getTargetNodeAttributes().setVisualattributes("FAE");
		targetOntologyi2b2RootNode.getTargetNodeAttributes().setName("i2b2");

		rootNode.add(targetOntologyi2b2RootNode);

		ontologyTargetTree.setI2B2RootNode(targetOntologyi2b2RootNode);
		
		
		ontologyTargetTree.getNodeLists().addNodyByPath(
				targetOntologyi2b2RootNode.getTreePath(), targetOntologyi2b2RootNode);

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
				Console.info("... importing data via the TransferHandler. Crazy.");
				Console.error("This is just plain wrong.");
				Console.subinfo("Subinfo");
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
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger())
					myPopupEvent(e);
			}

			public void mouseReleased(MouseEvent e) {
				// TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
				if (e.isPopupTrigger())
					myPopupEvent(e);
			}

			private void myPopupEvent(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				OntologyTree OTTarget = (OntologyTree) e.getSource();
				TreePath path = OTTarget.getPathForLocation(x, y);
				if (path == null)
					return;

				OTTarget.setSelectionPath(path);

			}
		};

		// set custom renderer and listener
		if (this.ontologyTargetTree != null) {

			// this.OTTarget.addTreeSelectionListener(this);
			this.ontologyTargetTree.addMouseListener(ma);
		}

	}

	/******************************************************
	 * 
	 * Node operations
	 * 
	 * ****************************************************
	 */

	public void deepCopy(final OntologyTreeNode source, final OntologyTreeNode target) {
		//TODO

		final OntologyTreeNode node = new OntologyTreeNode(source);
		node.getTargetNodeAttributes().setVisualattributes(source.getOntologyCellAttributes().getC_VISUALATTRIBUTES());
		node.getTargetNodeAttributes().addStagingPath(source.getTreePath());
		node.setType(Resource.I2B2.NODE.TYPE.ONTOLOGY_TARGET);

		node.setTreePath(target.getTreePath() + node.getID() + "\\");
		node.setTreePathLevel(target.getTreePathLevel() + 1);
		node.getTargetNodeAttributes().setName(node.getName());

		OntologyTreeNode testNode =	OntologyEditorView.getOntologyTargetTree().getNodeLists().getNodeByPath(node.getTreePath());
		if (testNode==null) {
			target.add(node);
			this.getOntologyTreeTarget().getNodeLists().add(node);
			for (OntologyTreeNode child : source.getChildren()) {
				deepCopy(child, node);
			}
		}
		else {
			boolean confirm =MessageDialog.openConfirm(Application.getShell(), "Node already exists!", "This Node "+ node.getID() +" already exists in the target ontology!\nDo you want to rename the Path?");
			if (confirm) {
				Display display = Application.getDisplay().getActiveShell().getDisplay();
				Application.getOntologyView().getSite().getShell();

				Point pt = display.getCursorLocation();

				Monitor [] monitors = display.getMonitors();
				for (int i= 0; i<monitors.length; i++) {
					if (monitors [i].getBounds().contains(pt)) {
						Rectangle rect = monitors [i].getClientArea();
						pt.x=rect.x;
						pt.y=rect.y;
						break;
					}
				}

				final Shell dialog = new Shell(display, SWT.ON_TOP //SWT.APPLICATION_MODAL
						| SWT.TOOL);
				dialog.setLocation(pt.x+Application.getOntologyView().getViewSite().getShell().getSize().x/2,Application.getOntologyView().getViewSite().getShell().getSize().y/2+pt.y);
				final Composite actionMenu = new Composite(dialog, SWT.NONE);
				actionMenu.setLayout(new org.eclipse.swt.layout.GridLayout(3, false));

				final Text text = new Text(actionMenu, SWT.NONE);
				final String oldID = node.getID();
				text.setText(oldID+"_2");

				Button btnOK = new Button(actionMenu,SWT.PUSH);
				btnOK.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/itemstatus-checkmark16.png"));
				btnOK.setToolTipText("OK");
				btnOK.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {

					}

					@Override
					public void widgetSelected(SelectionEvent e) {
						String newID = text.getText();
						node.setID(newID);

						String oldTreePath = node.getTreePath();
						String newTreePath = oldTreePath.replace(oldID, newID);
						node.setTreePath(newTreePath);
						OntologyTreeNode testNode =	OntologyEditorView.getOntologyTargetTree().getNodeLists().getNodeByPath(node.getTreePath());
						if (testNode==null) {
							dialog.close();
							target.add(node);
							getOntologyTreeTarget().getNodeLists().add(node);
							for (OntologyTreeNode child : source.getChildren()) {
								deepCopy(child, node);
							}
						}
						else {
							dialog.close();
							MessageDialog.openError(Application.getShell(), "Cannot Add Node!", "This Node is already present!\nPlease choose an unique name!");
						}

					}
				});

				actionMenu.pack();
				dialog.pack();
				dialog.open();

				while (!dialog.isDisposed ()) {
					if (!display.readAndDispatch ()) display.sleep ();
				}
			}
		}
	}

	public void dropCommandCopyNodes(int dropOperation) {
		if (dropOperation == 1) {
			ActionCommand command = new ActionCommand(
					Resource.ID.Command.OTCOPY);
			command.addParameter(
					Resource.ID.Command.OTCOPY_ATTRIBUTE_SOURCE_NODE_PATH,
					"");
			//			command.addParameter(
			//					Resource.ID.Command.OTCOPY_ATTRIBUTE_TARGET_NODE_PATH,
			//					targetPath);
			Application.executeCommand(command);

			OntologyEditorView.getTargetTreeViewer().refresh();
		}
		else {
			final Shell dialog = new Shell(Application.getShell(), SWT.ON_TOP //SWT.APPLICATION_MODAL
					| SWT.TOOL);
			dialog.setLocation(PlatformUI.getWorkbench().getDisplay()
					.getCursorLocation());

			final Composite actionMenu = new Composite(dialog, SWT.NONE);
			actionMenu.setLayout(new org.eclipse.swt.layout.GridLayout(3, false));


			/**
			 * Menu for Combine Actions
			 */
			final Menu menu = new Menu(dialog, SWT.POP_UP);

			//Set StartDate
			MenuItem startMenuItem = new MenuItem(menu, SWT.PUSH);
			startMenuItem.setText("Add as Start Date");
			startMenuItem.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
				}

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					dialog.close();
					System.out.println("STARTDATE CLICKED");

					IStructuredSelection selection = (IStructuredSelection) OntologyEditorView.getStagingTreeViewer()
							.getSelection();
					OntologyTreeNode node = (OntologyTreeNode) selection.iterator().next();
					ActionCommand command = new ActionCommand(
							Resource.ID.Command.OTSETTARGETATTRIBUTE);
					command.addParameter(
							Resource.ID.Command.OTSETTARGETATTRIBUTE_ATTRIBUTE_SOURCE_NODE_PATH,
							node.getOntologyCellAttributes().getC_FULLNAME());
					//					command.addParameter(
					//							Resource.ID.Command.OTSETTARGETATTRIBUTEY_ATTRIBUTE_TARGET_NODE_PATH,
					//							targetPath);
					command.addParameter(
							Resource.ID.Command.OTSETTARGETATTRIBUTEY_ATTRIBUTE_ATTRIBUTE,
							"startDateSource");
					Application.executeCommand(command);
				}
			});
			//Set EndDate
			MenuItem endMenuItem =  new MenuItem(menu, SWT.PUSH);
			endMenuItem.setText("Add as End Date");
			endMenuItem.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {

				}

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					dialog.close();
					System.out.println("ENDDATE CLICKED");
					IStructuredSelection selection = (IStructuredSelection) OntologyEditorView.getStagingTreeViewer()
							.getSelection();
					OntologyTreeNode node = (OntologyTreeNode) selection.iterator().next();
					ActionCommand command = new ActionCommand(
							Resource.ID.Command.OTSETTARGETATTRIBUTE);
					command.addParameter(
							Resource.ID.Command.OTSETTARGETATTRIBUTE_ATTRIBUTE_SOURCE_NODE_PATH,
							node.getOntologyCellAttributes().getC_FULLNAME());
					//					command.addParameter(
					//							Resource.ID.Command.OTSETTARGETATTRIBUTEY_ATTRIBUTE_TARGET_NODE_PATH,
					//							targetPath);
					command.addParameter(
							Resource.ID.Command.OTSETTARGETATTRIBUTEY_ATTRIBUTE_ATTRIBUTE,
							"endDateSource");
					Application.executeCommand(command);
				}
			});
			new MenuItem(menu, SWT.SEPARATOR);

			//Merge Ontologies (ICD10 etc)
			MenuItem mergeOntMenuItem = new MenuItem(menu, SWT.PUSH);
			mergeOntMenuItem.setText("Merge with Standard Ontology");
			mergeOntMenuItem.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
				}

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					dialog.close();
					//					ActionCommand command = new ActionCommand(
					//							Resource.ID.Command.OTCOPY);
					//					command.addParameter(
					//							Resource.ID.Command.OTCOPY_ATTRIBUTE_SOURCE_NODE_PATH,
					//							"");
					//					command.addParameter(
					//							Resource.ID.Command.OTCOPY_ATTRIBUTE_TARGET_NODE_PATH,
					//							targetPath);
					//					OntologyEditorView.getTargetTreeViewer().getTree().setRedraw(false);
					//					Application.executeCommand(command);
					//					OntologyEditorView.getTargetTreeViewer().getTree().setRedraw(true);

					System.out.println("COPY DONE");

					ActionCommand command2 = new ActionCommand(
							Resource.ID.Command.COMBINENODE);
//										command2.addParameter(
//												Resource.ID.Command.OTCOPY_ATTRIBUTE_TARGET_NODE_PATH,
//												targetPath);
					Application.executeCommand(command2);
				}
			});

			final ToolBar toolBar = new ToolBar(actionMenu, SWT.FLAT | SWT.RIGHT);
			toolBar.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));

			ToolItem tltmInsertNodes = new ToolItem(toolBar, SWT.NONE);
			tltmInsertNodes.setText("Insert Nodes");
			tltmInsertNodes.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					dialog.close();
					ActionCommand command = new ActionCommand(
							Resource.ID.Command.OTCOPY);
					//					command.addParameter(
					//							Resource.ID.Command.OTCOPY_ATTRIBUTE_SOURCE_NODE_PATH,
					//							"");
					//					command.addParameter(
					//							Resource.ID.Command.OTCOPY_ATTRIBUTE_TARGET_NODE_PATH,
					//							targetPath);
					OntologyEditorView.getTargetTreeViewer().getTree().setRedraw(false);
					Application.executeCommand(command);
					OntologyEditorView.getTargetTreeViewer().getTree().setRedraw(true);
					System.out.println("INSERT CLICKED");
					OntologyEditorView.getTargetTreeViewer().refresh();
				}
			});
			final ToolItem tltmCombine = new ToolItem(toolBar, SWT.PUSH);
			tltmCombine.setText("Combine Nodes");

			tltmCombine.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					Rectangle bounds = tltmCombine.getBounds();
					Point point = toolBar.toDisplay(bounds.x, bounds.y + bounds.height);
					menu.setLocation(point);
					menu.setVisible(true);
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
					dialog.close();
				}
			});

			actionMenu.pack();
			dialog.pack();
			dialog.open();
		}
	}

	/**
	 * Returns the instance of the MyOTItemLists object.
	 * 
	 * @return the MyOTItemLists object
	 */

	public OntologyTree getOntologyTreeSource() {
		return this.ontologyTreeSource;
	}

	//	protected void expandTreePaths(TreePath path) {
	//		this._ontologyTreeSource.expandPath(path);
	//		final Object node = path.getLastPathComponent();
	//		final int n = this._ontologyTreeSource.getModel().getChildCount(node);
	//
	//		for (int index = 0; index < n; index++) {
	//			final Object child = this._ontologyTreeSource.getModel().getChild(
	//					node, index);
	//			OntologyTreeNode treeNode = (OntologyTreeNode) child;
	//			/*
	//			 * if (treeNode.getNodeType().equals(NodeType.ITEM)) { // if its an
	//			 * item, leaf it closed and return return; }
	//			 */
	//			if (this._ontologyTreeSource.getModel().getChildCount(child) > 0) {
	//				expandTreePaths(path.pathByAddingChild(child));
	//			}
	//		}
	//	}

	public OntologyTree getOntologyTreeTarget() {
		return this.ontologyTargetTree;
	}

	/**
	 * @return the activeItemNode
	 */
	public OntologyTreeNode getSourceRootNode() {
		return sourceRootNode;
	}

	public OntologyTreeNode getStudyNode() {
		return this.ontologyTreeSource.getRootNode();
	}

	/**
	 * @return the activeItemNode
	 */
	public OntologyTreeNode getTargetRootNode() {
		return targetRootNode;
	}

	public OntologyTreeNode getTargetTreeRoot() {

		if (this.ontologyTargetTree == null) {
			Console.error("OTTarget is null while trying to get the tree root!");
			return null;
		}
		return this.ontologyTargetTree.getTreeRoot();
	}

	/**
	 * getTreeRoot: return the tree root object for this instance
	 * 
	 * @return this tree root
	 */

	public OntologyTreeNode getTreeRoot() {

		if (this.ontologyTreeSource == null)
			Console.error("OT is null while trying to get the tree root!");

		return this.ontologyTreeSource.getTreeRoot();
	}

	public String getVisitFromStringPath(String stringPath) {
		String[] split = stringPath.split("/");
		if (split[2] != null) {
			return split[2].toString();
		}

		return "";
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

	public OntologyTreeNode moveTargetNode(OntologyTreeNode sourceNode,
			OntologyTreeNode targetNode) {
		OntologyTreeNode newNode = new OntologyTreeNode(
				sourceNode.getName());

		newNode.setID(sourceNode.getID());
		newNode.setType(Resource.I2B2.NODE.TYPE.ONTOLOGY_TARGET);

		/*
		 * copy attriubutes
		 */

		if (sourceNode.getOntologyCellAttributes() != null) {
			newNode.getTargetNodeAttributes().setSubNodeList((
					sourceNode.getTargetNodeAttributes().getSubNodeList()));

			newNode.getTargetNodeAttributes().setVisualattributes(
					sourceNode.getTargetNodeAttributes().getVisualattribute());
			newNode.getTargetNodeAttributes().setName(sourceNode.getName());
			newNode.getTargetNodeAttributes().setDimension(sourceNode.getTargetNodeAttributes().getDimension());
		}
		else {
			System.out.println("sourceNode.getOntologyCellAttributes() == null");
		}


		targetNode.add(newNode);
		OntologyEditorView.getTargetTreeViewer().setExpandedState(newNode, true);
		OntologyEditorView.getTargetTreeViewer().refresh();
		this.getOntologyTreeTarget().getNodeLists().add(newNode);

		if (sourceNode.hasChildren()) {
			for (int x = 0; x < sourceNode.getChildCount(); x++) {
				OntologyTreeNode child = (OntologyTreeNode) sourceNode
						.getChildAt(x);
				moveTargetNode(child, newNode);
			}
		}
		return newNode;
	}

	/**
	 * @param _lastActiveNode
	 *            the activeItemNode to set
	 */
	public void setSourceRootNode(OntologyTreeNode sourceRootNode) {
		this.sourceRootNode = sourceRootNode;
	}

	public void setTargetAttribute(OntologyTreeNode targetNode,
			String attribute, String value) {

		if (attribute.equals("startDateSource")) {
			targetNode.getTargetNodeAttributes().setStartDateSourcePath(value);
			for (OntologyTreeSubNode sub : targetNode.getTargetNodeAttributes().getSubNodeList()) {
				sub.getTargetSubNodeAttributes().setStartDateSourcePath(value);
			}
		}
		else if (attribute.equals("endDateSource")) {
			targetNode.getTargetNodeAttributes().setEndDateSourcePath(value);
			for (OntologyTreeSubNode sub : targetNode.getTargetNodeAttributes().getSubNodeList()) {
				sub.getTargetSubNodeAttributes().setEndDateSourcePath(value);
			}
		}
		if (targetNode.hasChildren()) {

			for (int x = 0; x < targetNode.getChildCount(); x++) {
				OntologyTreeNode child = (OntologyTreeNode) targetNode
						.getChildAt(x);
				setTargetAttribute(child, attribute, value);
			}
		}
	}

	/* OT Commands */
	public void setTargetAttributesAsSourcePath(OntologyTreeNode sourceNode,
			OntologyTreeNode targetNode, String attribute) {
		if (sourceNode != null) {
			setTargetAttribute(targetNode, attribute,
					sourceNode.getOntologyCellAttributes().getC_FULLNAME());
		}
	}

	/**
	 * @param _lastActiveNode
	 *            the activeItemNode to set
	 */
	public void setTargetRootNode(OntologyTreeNode targetRootNode) {
		this.targetRootNode = targetRootNode;
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

		OntologyTreeNode node = (OntologyTreeNode) this.ontologyTreeSource
				.getLastSelectedPathComponent();

		// DEL set active node for value changed
		// Application.getResource().setActiveNode(node);
		// Activator.getDefault().getResource().setActiveNode(node);

		if (node != null) {

			Activator.getDefault().getResource().getEditorSourceInfoView()
			.setNode(node);

			message = "Selection in source tree changed to \'" + node.getName()
					+ "\'.";

			Application.getStatusView().addMessage(
					new SystemMessage(message,
							SystemMessage.MessageType.SUCCESS));

		} else {
			// do nothing if the node is null or no NodeType is set
			message = "Selection in source tree changed, but the the new selection isn't a node.";
		}

		Console.info(message);
	}

}
