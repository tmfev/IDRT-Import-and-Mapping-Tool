package de.umg.mi.idrt.ioe.OntologyTree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class OTRootNode extends OntologyTreeNode  {
	
	private String xmlns = "";
	private String xmlnsXsi = "";
	private String xsiSchemaLocation = "";
	private String xmlnsIas = "";
	private String description = "";
	private String fileType = "";
	private String fileOID = "";
	private String creationDateTime = "";
	private String asOfDateTime = "";
	private String odmVersion = "";
	private String originator = "";
	private String sourceSystem = "";
	private String sourceSystemVersion = "";
	private String _originalFileName = "";
	
	
	/**
	 * counter of unchecked children of this node
	 */
	private transient long _uncheckedChildren = 0;
	
	private transient long _numberOfItemNodes = 0;
	
	private HashMap<String,String> _attributesMap = new HashMap<String,String>();
	
	public OTRootNode (){
		super();
		initiate();
	}
	
	public OTRootNode (String name){
		super(name);
		
		initiate();
	}
	
	public void addAttribute(String name, String value){
		if(!_attributesMap.containsKey(name)){
			// add new attribute
			_attributesMap.put(name, value);
		} else {
			// set the value of an old attribute
			_attributesMap.put(name, value);
		}
	}
	
	/**
	 * Adds an unchecked Child to this node.
	 *
	 */
	public void addUncheckedChild () {
		this._uncheckedChildren++;
	}
	
	public Iterator<Entry<String, String>> getAttributesIterator(){
		return _attributesMap.entrySet().iterator();
	}
	
	/**
	 * @return the creationDateTime
	 */
	public String getCreationDateTime() {
		if(creationDateTime != null)
			return creationDateTime;
		else
			return "";
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		if(description != null)
			return description;
		else
			return "";
	}
	
	/**
	 * @return the fileType
	 */
	public String getFileType() {
		if(fileType != null)
			return fileType;
		else
			return "";
	}
	/**
	 * @return the originalFileName
	 */
	public String getOriginalFileName() {
		return _originalFileName;
	}
	/**
	 * @return the sourceSystem
	 */
	public String getSourceSystem() {
		if(sourceSystem != null)
			return sourceSystem;
		else
			return "";
	}
	/**
	 * @return the sourceSystemVersion
	 */
	public String getSourceSystemVersion() {
		if(sourceSystemVersion != null)
			return sourceSystemVersion;
		else
			return "";
	}
	/**
	 * Returns the number of unchecked children of this node.
	 *
	 * @return	the number of unchecked children
	 */
	public int getUncheckedChildren () {
		return (int)this._uncheckedChildren;
	}
	/**

	/**
	 * @return the Uploader
	 */
	public String getUploader() {
		if(originator != null)
			return originator;
		else
			return "";
	}
	private void initiate(){
		this.setNodeType(NodeType.META);
		this.setID("odm");
		if (this.getName() != null && this.getName().isEmpty())
			this.setName("ODM");
	}
	/**
	 * Removes an unchecked Child to this node.
	 *
	 */
	public void removeUncheckedChild () {
		this._uncheckedChildren--;
	}
	/**
	 * @param creationDateTime the creationDateTime to set
	 */
	public void setCreationDateTime(String creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param originalFileName the originalFileName to set
	 */
	public void setOriginalFileName(String originalFileName) {
		final int lastPeriodPos = originalFileName.lastIndexOf(".");
		 if (lastPeriodPos == -1){
			 // do nothing
		 } else {
			 originalFileName = originalFileName.substring(0, lastPeriodPos);
		 }
		_originalFileName = originalFileName;
	}

	
	
	/**
	 * @param sourceSystem the sourceSystem to set
	 */
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}
	
	/**
	 * @param sourceSystemVersion the sourceSystemVersion to set
	 */
	public void setSourceSystemVersion(String sourceSystemVersion) {
		this.sourceSystemVersion = sourceSystemVersion;
	}
	
	/**
	 * @param Uploader the Uploader to set
	 */
	public void setUploader(String originator) {
		this.originator = originator;
	}
}
