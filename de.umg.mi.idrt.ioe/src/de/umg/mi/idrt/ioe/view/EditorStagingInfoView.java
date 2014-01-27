package de.umg.mi.idrt.ioe.view;

import javax.swing.tree.TreeNode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyCellAttributes;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
/**
 * @author Christian Bauer <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> 
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 * 			Department of Medical Informatics Goettingen 
 * 			www.mi.med.uni-goettingen.de
 *         main class managing and giving access to the source and target trees
 */
public class EditorStagingInfoView extends ViewPart {

	private static Resource _resource = null;
	private static Composite parentPane;
	static OntologyTreeNode _node = null;
	private static Composite _editorComposite;
	private static Composite _parent;
	private static Table _infoTable;
	private static TableColumn infoTableDBColumn;
	private static TableColumn infoTableValue;
	
	private static TableItem addColumItem( String text ){
		TableItem item = new TableItem (_infoTable, SWT.NONE);
		item.setText (new String[] { text, "" });
		item.setForeground(Application.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		return item;
	}
	
	public static TableItem addValueItem( TableItem[] items, int row, String value ){
		if (items[row] == null){
			Console.error("Could not add an item to a table in EditorSourceInfoView, because there was no row #"+row+".");
			return null;
		}
		TableItem item = items[row];
		item.setText (1, value != null && !value.equals("null") ? value : "" );
		
		return item;
	}


	@Override
	public void createPartControl(Composite parent) {

		_parent = parent;
		parent.setLayout(new GridLayout(1, false));

		//createInfoGroup();

		_editorComposite = new Composite(parent, SWT.NONE);
		_editorComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		_editorComposite.setLayout(new FillLayout(SWT.VERTICAL));
		

		createTable();
		
		//item.setImage (image);

		parentPane = parent.getParent();
	}
	
	
	
	private static void createTable() {
		
		if (_infoTable != null)
			return;
		
		_infoTable = new Table(_parent, SWT.BORDER | SWT.MULTI);
		_infoTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		_infoTable.setHeaderVisible(true);
		_infoTable.setLinesVisible(true);

		infoTableDBColumn = new TableColumn(_infoTable, SWT.NONE);
		infoTableDBColumn.setWidth(170);
		infoTableDBColumn.setText("Attribute");

		infoTableValue = new TableColumn(_infoTable, SWT.NONE);
		infoTableValue.setWidth(550);
		infoTableValue.setText("Value");

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

	}
	
	public static void executeRefresh(){
		
		if(parentPane == null){
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
		
		_editorComposite.update();
		_editorComposite.layout();
		_parent.layout();
	}
	
	public Resource getResource(){
		return this._resource;
	}
	
	public static void refresh(){
		new Thread(new Runnable() {
		    public void run(){
		    	executeRefresh();
		    }
		  }).run();
	}
	
	public void setComposite(Composite pane){
		refresh();
	}
	
	public void setComposite(String text){
		refresh();
	}
	
	@Override
	public void setFocus() {

	}
	
	public static void setNode(OntologyTreeNode node){
		_node = node;
		refresh();
	}

	public void setResource(Resource resource){
		EditorStagingInfoView._resource = resource;
	}
	
}
