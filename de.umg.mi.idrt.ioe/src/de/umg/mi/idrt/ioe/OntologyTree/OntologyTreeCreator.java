package de.umg.mi.idrt.ioe.OntologyTree;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.core.runtime.IProgressMonitor;

import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Debug;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;


/**
 * This class provides all the functions for creating an OT.
 * All   
 *  
 * @author	Christian Bauer	
 * @version 1.1 
 */
public class OntologyTreeCreator {
	
	public MyOntologyTrees _myOT	= null;
	public OntologyTree ontologyStagingTree		= null;
	public OntologyTreeNode ontologyStagingTreeRootNode = null;
	public int _numberOfItems				= 0;
	public int _numberOfPatients			= 0;
	int _counter							= 0;
	public IProgressMonitor _progressMonitor = null;
	public final static String FILE_EXTENSION	= "csv";
	public final String PATH_SEPARATOR			= Resource.I2B2.I2B2_PATH_SEPERATOR;
	public final String BASECODE_SEPARATOR		= Resource.I2B2.I2B2_CONCEPT_PATH_SEPERATOR;
	public final String BASECODE_VALUE_SEPARATOR	= Resource.I2B2.I2B2_CONCEPT_VALUE_SEPERATOR;

	public List<String> _basecode_list	= null;
	public String _patientID				= "";
	public String _visualattribute		= "";
	public String _value					= "";
	public String _importdate			= "";
	
	public OntologyTreeNode previous1ItemDefNode = null;
	public OntologyTreeNode previous2ItemDefNode = null;
	
	public int counterFullNumberOfItems = 0;
	public int counterFullNumberOfElements = 0;
	private IProgressMonitor _mainProgressMonitor;
	
		
	public OntologyTreeCreator (
				MyOntologyTrees myOntologyTree){
		
		Console.info( "Creating an OT." );

		//this._myOT = myOT;
//		this._myOT = Activator.getDefault().getResource().getEditorSourceView().getI2B2ImportTool().getMyOntologyTrees();
	_myOT = OntologyEditorView.getMyOntologyTree();

	
//	EditorSourceView.getI2B2ImportTool().setOTCreator(this);
	//OntologyEditorView.getI2b2ImportTool().setOTCreator(this);
		initiate(myOntologyTree.getOntologyTreeSource(), myOntologyTree.getTreeRoot());
	}
	
	public OntologyTreeCreator (){
		Console.info( "Creating an OT (2)." );

		initiate(null, null);
	}
	
	public void initiate(OntologyTree OT,
			OntologyTreeNode treeRoot){

		ontologyStagingTree = this._myOT.getOntologyTreeSource();
		ontologyStagingTreeRootNode = this.ontologyStagingTree.getRootNode();
		
		//_OT = OT;
		//_OTRoot = treeRoot;

		if ( ontologyStagingTree == null ){
			
			
			
		
			
			//this._myOT.createOT(Resource.OntologyTree.ONTOLOGYTREE_ROOTNODE_NAME, _OTRoot);
			
			//_OT = _myOT.getOT();
			//_OTRoot = _OT.getTreeRoot();
		}
		
		//this._myOT.createOT(Resource.OntologyTree.ONTOLOGYTREE_ROOTNODE_NAME, _myOT.getSourceRootNode());

		SimpleDateFormat sdf = new SimpleDateFormat( "dd. MMMM yyyy", new Locale("en"));
	    _importdate=sdf.format(new Date());
	}
	
	
	public void doIt(){
		createOntology();
		createPatientDimension();
		createConceptDimension();
		createObservationFact();
	}
	
	public void createOntology(){
		
	}
	
	public void createPatientDimension(){
	
	}
	
	public void createConceptDimension(){
	
	}
	
	public void createObservationFact(){
	
	}
	
	/**
	 * Sets the default parameters for a new node.
	 * 
	 * @param	*
	 * 
	 */
	/*
	private void setDefaultNodeParameters ( OTNode node, int level, String path, String visualattribute, String conceptCode, NodeType nodeType, String ID  ) {
		node.setImportPath( path );
		node.setTreePath( path );
		node.setI2B2Path( path );
		node.setI2B2Level( level );
		node.setVisualattribute( visualattribute );
		node.setConceptCode( conceptCode );
		node.setNodeType( nodeType );
		node.setID( ID.trim() );
		this.getOT().getNodeLists().addNodyByPath(path, node);
		return;
	}
	*/
	
	/*
	public OTNode addOTNode ( OTNode parentNode, String name){
		Console.info("Adding a node to the ontology tree.");
		
		OTNode node = _OT.addNode( parentNode, name );
		
		return node;
	}
	*/
	
	
	public OntologyTreeNode addItemGroupNodeByParentPath ( String parentPath, String name, String fullname, String basecode, String id, int order, boolean isMandatory, boolean isReferenceData ) {

		OntologyTreeNode parentNode = getOT().getItemLists().getItemNodeByPath(parentPath);

		return this.addItemGroupNode( parentNode, name, fullname, basecode, id, order, isMandatory, isReferenceData );
	}

	private OntologyTreeNode addItemGroupNode(OntologyTreeNode parentNode, String name,
			String fullname, String basecode, String id, int order,
			boolean isMandatory, boolean isReferenceData) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setItemDataID(  String id, String itemID, String basecode0 ){
		basecode0 += id;
		itemID = id.trim();
		return;
	}
	
	public XMLGregorianCalendar createXMLDate(int year, int month, int day) {
		XMLGregorianCalendar xmlDate = null;
		try {
			xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar();
			xmlDate.setYear(year);
			xmlDate.setMonth(month);
			xmlDate.setDay(day);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			Debug.e("xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar();", this);
		}
		return xmlDate;
	}
		

	public int getNumberOfItems() {
		return _numberOfItems;
	}

	public void setNumberOfItems(int numberOfItems) {
		this._numberOfItems = numberOfItems;
	}

	public int getNumberOfEstimatedPatients() {
		return _numberOfPatients;
	}

	public void setNumberOfEstimatedPatients(int numberOfPatients) {
		this._numberOfPatients = numberOfPatients;
	}
	
	public void patientAdded(){
		//getProgressMonitor().worked(1);
		_counter++;
		getProgressMonitor().subTask("finding patients:  trying possible candidate " + _counter + " out of "+this.getNumberOfEstimatedPatients()+"");
		if (getProgressMonitor().isCanceled()){
			Debug.e("patientAdded isCanceled");
		}
	}
	
	public void noPatientAdded(){
		//getProgressMonitor().worked(1);
		_counter++;
		getProgressMonitor().subTask("finding patients:  trying possible candidate " + _counter + " out of "+this.getNumberOfEstimatedPatients()+" (†)");
		if (getProgressMonitor().isCanceled()){
			Debug.e("patientAdded isCanceled");
		}
	}
	
	public void itemNodeAdded(){
		//getProgressMonitor().worked(1);
		_counter++;
		if (getProgressMonitor() != null){
			getProgressMonitor().subTask("Adding ItemNode " + _counter + " out of ~"+this.getNumberOfItems()+".");
			if (getProgressMonitor().isCanceled()){
				Debug.e("itemNodeAdded isCanceled");
			}
		}
		
	}
	
	public void addConceptDimensionProgress(){
		_counter++;
		getProgressMonitor().subTask("Adding ConceptDimensionElement " + _counter + " out of "+this.getAccurateNumberOfElements()+".");
		if (getProgressMonitor().isCanceled()){
			Debug.e("addConceptDimensionProgress isCanceled");
		}
	}
	
	public void addObservationFactProgress(){
		_counter++;
		getProgressMonitor().subTask("Adding ObservationFactElement " + _counter + " out of "+this.getNumberOfEstimatedPatients()*this.getAccurateNumberOfItems()+".");

		// console output 
		if (_counter%50000 == 0){
			Console.infoLine(String.valueOf(_counter));
		} else if (_counter%500 == 0){
			Console.infoNoLine(".");
		}

		if (getProgressMonitor().isCanceled()){
			Debug.e("addObservationFactProgress isCanceled");
		}
		getMainProgressMonitor().worked(1);
	}

	public IProgressMonitor getProgressMonitor() {
		return _progressMonitor;
	}

	public void setProgressMonitor(IProgressMonitor _progressMonitor) {
		this._counter = 0;
		this._progressMonitor = _progressMonitor;
	}
	
	public void addCounterItem(){
		Console.infoLine(".");
		this.counterFullNumberOfItems++;
		if(this._mainProgressMonitor.isCanceled()){
			Debug.d("isCanceled! @addCounterItem");
		}
		
	}
	
	public void addCounterElement(){
		this.counterFullNumberOfElements++;
	}
	
	public int getAccurateNumberOfItems(){
		return this.counterFullNumberOfItems;
	}
	
	public int getAccurateNumberOfElements(){
		return this.counterFullNumberOfItems;
	}
	
	public void setMainProgressMonitor(IProgressMonitor mainProgressMonitor){
		this._mainProgressMonitor = mainProgressMonitor;
	}
	
	public IProgressMonitor getMainProgressMonitor(){
		return this._mainProgressMonitor;
	}

	
	
	/*
	 * simple OT output for debuging
	 * 
	 */
	
	public void printTree(OntologyTreeNode startNode){
		Console.info("------ printing tree ------");
		printNode(startNode, 1);
	}
	
	public void printTree(){
		printTree(ontologyStagingTree.getRootNode());
	}
	
	public void printNode(OntologyTreeNode node, int depth){
		String depth_sign = " ";
		String depth_string = "";
		if (node.getID() == null){
			Console.info("- ERROR: ID == NULL");
		}
		
		for (int d = 0; d < depth; d ++){
			depth_string += depth_sign;
		}
		
		Console.info("" + depth_string + " " + node.getName() + "/id:" + node.getID() + " (" + node.getChildCount() + ")");
		
		for (int i = 0; i < node.getChildCount(); i++){
			printNode((OntologyTreeNode)node.getChildAt(i), depth + 1);
		}
		

		

		
	}
	
	
	
	public OntologyTree getOT(){
		return this.ontologyStagingTree;
	}
	
	public MyOntologyTrees getMyOT(){
		
		
		return this._myOT;
	}

	
	/* 
	 * The loading functions 
	 * 
	 * 
	 */
	
	
	
	/**
	 * Convert a Path to an ConceptCode
	 * 
	 * @param	path	the Path
	 * 
	 * return	the ConceptCode
	 * 
	 */
	protected String convertPathToConceptCode(String path){
		return path.replace("\\", "|");
	}
	

	
	protected int getIntFromString(String integer){
		
		try {
			return Integer.valueOf(integer);
		} catch (NumberFormatException e) {
			Console.error( "Coudn't convert String (\"" + integer + "\") to Integer.", e );
		}
		
		return 0;
	}

	protected boolean searchForSpecialItem ( String specialName ){
			
		if ( !specialName.isEmpty() && specialName.equals( Resource.Import.ODM_SDSVAR_PATIENTID ) ){
			return true;
		} 
		return false;
	}
}
