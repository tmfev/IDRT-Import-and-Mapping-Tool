package de.umg.mi.idrt.ioe.view;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.tree.TreeNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.umg.mi.idrt.ioe.Activator;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Debug;
import de.umg.mi.idrt.ioe.GUITools;
import de.umg.mi.idrt.ioe.I2B2ImportTool;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyCellAttributes;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;


import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class EditorSourceInfoView extends ViewPart {

	private I2B2ImportTool _i2b2ImportTool = null;
	private Resource _resource = null;
	private String _text = ""; 
	private Composite parentPane;
	//private OntologyTreeItemNode _itemNode;
	OntologyTreeNode _node = null;
	private Composite _editorComposite;
	private Composite _parent;
	private Table _infoTable;
	private TableColumn infoTableDBColumn;
	private TableColumn infoTableValue;
	
	public EditorSourceInfoView() {
		
	}

	@Override
	public void createPartControl(Composite parent) {

		Debug.f("createPartControl",this);
		_parent = parent;
		parent.setLayout(new GridLayout(1, false));

		//createInfoGroup();

		Label lblNodeInfosDeluxe = new Label(parent, SWT.NONE);
		lblNodeInfosDeluxe.setText("node infos deluxe");

		_editorComposite = new Composite(parent, SWT.NONE);
		_editorComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		_editorComposite.setLayout(new FillLayout(SWT.VERTICAL));
		

		createTable();
		
		//item.setImage (image);

		parentPane = parent.getParent();
	}
	
	private TableItem addColumItem( String text ){
		TableItem item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] { text, "" });
		return item;
	}

	private void createTable() {
		
		if (_infoTable != null)
			return;
		
		_infoTable = new Table(_parent, SWT.BORDER | SWT.MULTI);
		_infoTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		_infoTable.setHeaderVisible(true);
		_infoTable.setLinesVisible(true);

		infoTableDBColumn = new TableColumn(_infoTable, SWT.NONE);
		infoTableDBColumn.setWidth(170);
		infoTableDBColumn.setText("column");

		infoTableValue = new TableColumn(_infoTable, SWT.NONE);
		infoTableValue.setWidth(550);
		infoTableValue.setText("value");

		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.C_HLEVEL);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.C_FULLNAME);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.C_NAME);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.C_SYNONYM_CD);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.C_VISUALATTRIBUTES);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.C_TOTALNUM);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.C_BASECODE);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.C_METADATAXML);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.C_FACTTABLECOLUMN);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.C_TABLENAME);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.C_COLUMNNAME);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.C_COLUMNDATATYPE);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.C_OPERATOR);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.C_DIMCODE);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.C_COMMENT);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.C_TOOLTIP);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.M_APPLIED_PATH);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.UPDATE_DATE);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.DOWNLOAD_DATE);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.IMPORT_DATE);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.SOURCESYSTEM_CD);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.VALUETYPE_CD);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.M_EXCLUSION_CD);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.C_PATH);
		addColumItem(Resource.I2B2.TABLE.ONTOLOGY.C_SYMBOL);
		addColumItem("DebugTreeInfo");

	}
	


	private boolean hasNode() {
		if (_node != null)
			return true;
		else
			return false;
	}

	@Override
	public void setFocus() {

	}
	
	public void setComposite(String text){
		Debug.f("setComposite",this);
		
		this._text = text;
		refresh();
	}
	
	public void setComposite(Composite pane){
		Debug.f("setComposite",this);
		
		refresh();
	}
	
	
	public void setNode(OntologyTreeNode node){//, List<String> answersList, MyOntologyTreeItemLists itemLists){
		//Debug.f("setNode",this);
		//Console.info("setting node");
		System.out.println("setting node ("+node.getName()+")");
		_node = node;
		refresh();
	}
	
	
	
	public void setI2B2ImportTool(I2B2ImportTool i2b2ImportTool){
		this._i2b2ImportTool = i2b2ImportTool;
	}
	
	public I2B2ImportTool getI2B2ImportTool(){
		return this._i2b2ImportTool;
	}
	
	public void setResource(Resource resource){
		this._resource = resource;
	}
	
	public Resource getResource(){
		return this._resource;
	}
	
	public void refresh(){
		Debug.f("refresh",this);
		System.out.println("refreseh!");
		Display display = Display.getCurrent();
		
		if(display == null) {
		    // Bad, no display for this thread => we are not in (a) UI thread
		    //display.syncExec(new Runnable() {void run() { gc = new GC(display);}});
			Debug.e("no display");

			if (PlatformUI.getWorkbench().getDisplay() != null){
				Debug.d("display by PlatformUI");
				System.out.println("display by PlatformUI");
				PlatformUI.getWorkbench().getDisplay().syncExec(
					  new Runnable() {
					    public void run(){
					    	executeRefresh();
					    }
					  });
			} else if (Application.getDisplay() != null){
				Debug.d("display by Acitvator");
				System.out.println("display by Acitvator");
				Application.getDisplay().syncExec(
						  new Runnable() {
						    public void run(){
						    	executeRefresh();
						    }
						  });
			} else {
				Debug.e("no Display (final)");
				System.out.println("no Display (final)");
			}
		} else {
			System.out.println("display else");
			  new Runnable() {
				    public void run(){
				    	System.out.println("... execute");
				    	executeRefresh();
				    }
				  };
			executeRefresh();
		}
	}
	
	public TableItem addValueItem( TableItem[] items, int row, String value ){
		if (items[row] == null){
			Debug.e("Could not add an item to a table in EditorSourceInfoView, because there was no row #"+row+".");
			return null;
		}
		TableItem item = items[row];
		item.setText (1, value != null && !value.equals("null") ? value : "" );

		return item;
	}
	
	public void executeRefresh(){
		Debug.f("executeRefresh for text:\""+this._text+"\"",this);
		
		if(parentPane == null){
			Debug.e("no pane avaible @OntologyNodeEditorView");
			return;
		}

		createTable();
		
		OntologyCellAttributes attributes = _node.getOntologyCellAttributes();
		
		TableItem[] items = _infoTable.getItems();

		int row = 0;

		addValueItem(items, row++,  String.valueOf( attributes.getC_HLEVEL() ) );
		addValueItem(items, row++,  attributes.getC_FULLNAME() );
		addValueItem(items, row++,  attributes.getC_NAME() );
		addValueItem(items, row++,  attributes.getC_SYNONYM_CD() );
		addValueItem(items, row++,  attributes.getC_VISUALATTRIBUTES() );
		addValueItem(items, row++,  String.valueOf( attributes.getC_TOTALNUM() ) );
		addValueItem(items, row++,  attributes.getC_BASECODE() );
		addValueItem(items, row++,  String.valueOf( attributes.getC_METADATAXML() ) );
		addValueItem(items, row++,  attributes.getC_FACTTABLECOLUMN() );
		addValueItem(items, row++,  attributes.getC_TABLENAME() );
		addValueItem(items, row++,  attributes.getC_COLUMNNAME() );
		addValueItem(items, row++,  attributes.getC_COLUMNDATATYPE() );
		addValueItem(items, row++,  attributes.getC_OPERATOR() );
		addValueItem(items, row++,  attributes.getC_DIMCODE() );
		addValueItem(items, row++,  String.valueOf( attributes.getC_COMMENT() ) );
		addValueItem(items, row++,  attributes.getC_TOOLTIP() );
		addValueItem(items, row++,  attributes.getM_APPLIED_PATH() );
		addValueItem(items, row++,  String.valueOf( attributes.getUPDATE_DATE() ) );
		addValueItem(items, row++,  String.valueOf( attributes.getDOWNLOAD_DATE() ) );
		addValueItem(items, row++,  String.valueOf( attributes.getIMPORT_DATE() ) );
		addValueItem(items, row++,  attributes.getSOURCESYSTEM_CD() );
		addValueItem(items, row++,  attributes.getVALUETYPE_CD() );
		addValueItem(items, row++,  attributes.getM_EXCLUSION_CD() );
		addValueItem(items, row++,  attributes.getC_PATH() );
		addValueItem(items, row++,  attributes.getC_SYMBOL() );
		
		
		
		TreeNode[] path = _node.getPath();
		
		String stringPath = "";
		if (path != null && path.length < 1 ){
			
			for ( int x = 0; x < path.length; x ++ ) {
				System.out.println("");
				stringPath += "\\" + path[x].toString();
			}
			
		}
		
		addValueItem(items, row++, stringPath + " (" + _node.getDepth() + ")" );
		
		
		
		
		/*
		TableItem item = new TableItem (_infoTable, SWT.NONE);
		item.setText (2, String.valueOf( attributes.getC_HLEVEL() ) );
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"C_FULLNAME", attributes.getC_FULLNAME()});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"C_NAME", attributes.getC_NAME()});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"C_SYNONYM_CD", attributes.getC_SYNONYM_CD()});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"C_VISUALATTRIBUTES", attributes.getC_VISUALATTRIBUTES()});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"C_TOTALNUM", String.valueOf( attributes.getC_TOTALNUM() )});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"C_BASECODE", attributes.getC_BASECODE()});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"C_METADATAXML", String.valueOf( attributes.getC_METADATAXML() )});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"C_FACTTABLECOLUMN", attributes.getC_FACTTABLECOLUMN()});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"C_TABLENAME", attributes.getC_TABLENAME()});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"C_COLUMNNAME", attributes.getC_COLUMNNAME()});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"C_COLUMNDATATYPE", attributes.getC_COLUMNDATATYPE()});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"C_OPERATOR", attributes.getC_OPERATOR()});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"C_DIMCODE", attributes.getC_DIMCODE()});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"C_COMMENT", String.valueOf( attributes.getC_COMMENT() )});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"C_TOOLTIP", attributes.getC_TOOLTIP()});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"M_APPLIED_PATH", attributes.getM_APPLIED_PATH()});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"UPDATE_DATE", String.valueOf( attributes.getUPDATE_DATE() )});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"DOWNLOAD_DATE", String.valueOf( attributes.getDOWNLOAD_DATE() )});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"IMPORT_DATE", String.valueOf( attributes.getIMPORT_DATE() )});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"SOURCESYSTEM_CD", attributes.getSOURCESYSTEM_CD()});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"VALUETYPE_CD", attributes.getVALUETYPE_CD()});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"M_EXCLUSIVE_CD", attributes.getM_EXCLUSION_CD()});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"C_PATH", attributes.getC_PATH()});
		item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] {"C_SYMBOL", attributes.getC_SYMBOL()});
		*/
		
		//_infoTable.getColumn(1)
		
		
		/*
		
		// recreate bottom-label
		if ( _itemNode == null ){
			
			disposeChildren(_editorComposite);
			
			Group grpLabel = new Group(_editorComposite, SWT.NONE);
			grpLabel.setText("label");
			grpLabel.setLayout(new GridLayout(2, false));
			
			//_bottomLabel = new Label(grpLabel, SWT.NONE);
			//_bottomLabel.setText("New Label");
		}

		if ( _itemNode != null ){
			
			path = _itemNode.getTreePath();
			name = _itemNode.getName();
			nodeType = _itemNode.getNodeType().toString();
			
			

			bottomLabel = "ItemNode";

			disposeChildren(_editorComposite);

			_editorComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			_editorComposite.setLayout(new FillLayout(SWT.VERTICAL));
			
			NodeEditorCompositeInteger bottomComposite = new NodeEditorCompositeInteger(this._editorComposite, SWT.NONE);
			
		} 
		
		if ( _node != null ) {
			
			//if ( _node.getNodeType().equals(NodeType.ANSWER) ){
			//	_node = (OTNode)_node.getParent();
			//	if ( _node.getNodeType().equals(NodeType.ANSWER) ){
			//		_node = (OTNode)_node.getParent();
			//		if ( _node.getNodeType().equals(NodeType.ANSWER) ){
			//			_node = (OTNode)_node.getParent();
			//		}
			//	}
			//}
			
			path = _node.getTreePath();
			name = _node.getName();
			nodeType = _node.getNodeType().toString();
			importPath = _node.getImportPath();
			treePath = _node.getTreePath();
			i2b2Path = _node.getI2B2Path();
			i2b2Level = String.valueOf(_node.getLevel());
			if ( _node.isAdditionalData() ){
				OTNode additionalDataParent = _node.getAdditionalDataParentNode();
				
				if ( _node.isAdditionDataParent() ) {
					additionalDataParentNodePath = "*";
				} else if ( additionalDataParent != null ){
					additionalDataParentNodePath = additionalDataParent.getImportPath();
				}
			}
			additionalDataParentNodePath = ( _node != null && _node.hastAdditionalDataParentNode() ) ? _node.getAdditionalDataParentNode().getImportPath() : "-";
			
			bottomLabel = "Node";
			
		} else {
			path = ">";
			name = "Name";
			nodeType = "Type";
			
			bottomLabel = "nothing";
		}
		
		if (_node != null){
			i2b2Info = _node.getLevel() + "|" + _node.getI2B2Path();
			
			//Console.info("checkID2");
			//_checkboxIsVisible.setSelection(_node.isVisable());
			//_checkboxIsVisible.update();
		}
		
		*/
		
		/*
		_infoLabelPath.setText(path);
		_infoLabelPath.update();
		_infoLabelName.setText(name);
		_infoLabelName.update();
		//_infoLabelI2B2InfoData.setText(i2b2Info);
		//_infoLabelI2B2InfoData.update();
		if (nodeType != null)
			_infoLabelNodeType.setText(nodeType + ( ( ( (nodeType.equals(NodeType.ITEM.toString() ) && _itemNode != null )  ) ?  " (" + _itemNode.getDataType() + ")" : "") ));
		
		_infoLabelNodeType.update();
		_infoLabelImportPathData.setText(importPath);
		_infoLabelImportPathData.update();
		_infoLabelTreePathData.setText(treePath);
		_infoLabelTreePathData.update();
		_infoLabelI2B2PathData.setText(i2b2Path);
		_infoLabelI2B2PathData.update();
		_infoLabelI2B2LevelData.setText(i2b2Level);
		_infoLabelI2B2LevelData.update();
		_infoLabelAdditionalDataParentPathData.setText(additionalDataParentNodePath);
		_infoLabelAdditionalDataParentPathData.update();
		
		_infoLabelNodeType.getParent().layout();
		
		// only edit if there is a active instance
		if (!_bottomLabel.isDisposed()){
			_bottomLabel.setText(bottomLabel);
			_bottomLabel.update();
			
		}
		
		*/
		
		
		//_editorComposite.dispose();
		//_editorComposite = new NodeEditorCompositeInteger(this._editorComposite, SWT.NONE);
		
		/*
		Label _bottomLabel2 = new Label(_editorComposite, SWT.NONE);
		_bottomLabel2.setText("New Label2 (XX)");
		_bottomLabel2.update();
		*/
		
		//_editorComposite.update();
		//_editorComposite.layout();
		
		
		
		_editorComposite.update();
		_editorComposite.layout();
		
		_parent.layout();
		
	}
	
	/*
	
	private JPanel createCreateLeafsNumberPanel(OntologyTreeItemNode node) {

		JPanel buttonPanel = null;
		UserInput userInput = node.getUserInput();
		String low = "";
		String high = "";

		if ("integer".equals(node.getDataType())) {
			userInput = (UserInputInteger) node.getUserInput();
			buttonPanel = createOneButtonPanel("", "erstellen (int)", "createLeafs",
					Color.LIGHT_GRAY);

			low = node.hasUserInput() ? String
					.valueOf(((UserInputInteger) userInput).getLow()) : ((node
					.getDataSourceLow() != null) ? node.getDataSourceLow().toString() : "");
			high = node.hasUserInput() ? String
					.valueOf(((UserInputInteger) userInput).getHigh()) : ((node
					.getDataSourceHigh() != null) ? node.getDataSourceHigh().toString() : "");
		} else if ("float".equals(node.getDataType())) {
			userInput = (UserInputDataFloat) node.getUserInput();
			buttonPanel = createOneButtonPanel("", "erstellen (float)", "createLeafs",
					Color.LIGHT_GRAY);
			low = node.hasUserInput() ? String
					.valueOf(((UserInputDataFloat) userInput).getLow())
					: ((node.getDataSourceLow() != null) ? node.getDataSourceLow().toString()
							: "0");
			high = node.hasUserInput() ? String
					.valueOf(((UserInputDataFloat) userInput).getHigh())
					: ((node.getDataSourceHigh() != null) ? node.getDataSourceHigh()
							.toString() : "0");

		} else {
			userInput = node.getUserInput();
			buttonPanel = createOneButtonPanel("", "erstellen",
					"createLeafsInteger", Color.LIGHT_GRAY);
		}

		JPanel suggestionAddPanel = createItemPanel();
		suggestionAddPanel.add(createJLabel("Leafs von "));
		suggestionAddPanel.add(createJTextField("indexLow", low, 4));
		suggestionAddPanel.add(createJLabel(" bis "));
		suggestionAddPanel.add(createJTextField("indexHigh", high, 4));
		suggestionAddPanel.add(createJLabel(". "));
		createGroupingEditor(suggestionAddPanel, node);
		suggestionAddPanel.add(buttonPanel);

		return suggestionAddPanel;
	}
	
	*/
	
	private JPanel createOneButtonPanel(String text, String label,
			String actionCommand, Color bgColor) {

		JPanel jPanel = new JPanel();
		jPanel = this.createItemPanel();

		JButton suggestionCreateLeafs = createButton(label, actionCommand);

		jPanel.add(createJLabel(text));
		jPanel.add(suggestionCreateLeafs);

		return jPanel;
	}
	
	private JPanel createItemPanel() {

		JPanel itemPanel = new JPanel();
		itemPanel.setBackground(Color.LIGHT_GRAY);
		//itemPanel.setLayout(getStandardGridBagLayout());
		itemPanel.setSize(new Dimension(1000, 500));
		// itemPanel.setPreferredSize( new Dimension ( 900, 200 ) );

		return itemPanel;
	}
	
	private JLabel createJLabel(String text) {

		return new JLabel(text);
	}
	
	private JButton createButton(String label, String command) {

		JButton button = new JButton();
		if (!label.isEmpty())
			button.setText(label);
		button.setVerticalAlignment(AbstractButton.BOTTOM);
		button.setActionCommand(command);
		//button.addActionListener(this.myOntologyTree);
		// button.setLabel( label );

		// button.setPreferredSize( button.getPreferredSize() );
		// button.setMaximumSize( new Dimension(10,3) );
		button.setVisible(true);

		return button;
	}


	private JComboBox createJComboBox(String name, String value, int type) {

		String[] pattern = null;
		String selected = "";
		int size = 0;

		if (type == 1) // month
			size = 12;
		else if (type == 2) // days
			size = 31;
		else
			size = 1;

		if (type < 3) {
			pattern = new String[size];
			for (int x = 0; x < size; x++) {
				pattern[x] = String.valueOf(x + 1);
			}
		} else if (type == 3) {
			// groups

			// add value to pattern
			if (value != null) {
				if (value != "10" && value != "50" && value != "100"
						&& value != "1000" && value != "10" && value != "10"
						&& value != "10") {
					pattern = new String[] { value, "10", "50", "100", "1000" };
				}

			}

			// set selected value to 10
			if (value == null)
				value = "10";
		}

		JComboBox jComboBox = new JComboBox(pattern);
		jComboBox.setName(name);
		jComboBox.setSelectedItem(value);
		jComboBox.setPrototypeDisplayValue("XXXX");
		jComboBox.setMaximumSize(jComboBox.getPreferredSize());

		if (type == 3)
			jComboBox.setEditable(true);

		// add this object to the session array
		//((Object) this.editorFields).put(name, jComboBox);

		return jComboBox;
	}
	
	private JTextField createJTextField(String name, String value, int size) {

		JTextField jTextField = new JTextField(size);
		jTextField.setName(name);
		jTextField.setText(value);

		// add this object to the session array
		//this.editorFields.put(name, jTextField);

		return jTextField;
	}
	
	private JButton createIconButton(String label, String command, String icon) {

		JButton button = createButton(label, command);
		button.setIcon(GUITools.createImageIcon(icon));
		button.setBorderPainted(false);
		button.setIconTextGap(0);

		return button;
	}
	
	private void disposeChildren(Composite composite){
		Control[] children = composite.getChildren();
		
		for(int x = 0; x < children.length; x++){
			children[x].dispose();
		}
		
	}
}
