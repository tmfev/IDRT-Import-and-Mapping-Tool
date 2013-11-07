package de.umg.mi.idrt.ioe.OntologyTree;

import java.util.Date;

import de.umg.mi.idrt.ioe.view.OntologyEditorView;


/**
 * This is the special node class for the ontology tree. It holds some basic
 * informations like name, stringpath, nodetype and the visual attribute for
 * I2B2 from its OntologyTreeNode parent and some specific information about
 * the target.
 * 
 * @author Christian Bauer
 * @version 0.9
 */


public class Target {
	
	/**
	 * the id of this target
	 */
	private int targetID = -1;
	
	/**
	 * a object for the project information
	 */
	private int targetProjectID = -1;
	
	/**
	 * the version of this target
	 */
	private int version = -1;
	
	/**
	 * the version of this target
	 */
	private Date created = null;
	
	/**
	 * the version of this target
	 */
	private Date lastModified = null;
	
	/**
	 * the version of this target
	 */
	private String userID = "";
	
	/**
	 * a string of the db schema
	 */
	private String targetDBSchema = "";

	public Target (){
	
		
		
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @return the last_modified
	 */
	public Date getLastModified() {
		return lastModified;
	}

	/**
	 * @return the targetDBSchema
	 */
	public String getTargetDBSchema() {
		return targetDBSchema;
	}

	/**
	 * @return the targetID
	 */
	public int getTargetID() {
		return targetID;
	}

	/**
	 * @return the targetProject
	 */
	public int getTargetProjectID() {
		return targetProjectID;
	}

	/**
	 * @return the userID
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @param lastModified the last_modified to set
	 */
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * @param targetDBSchema the targetDBSchema to set
	 */
	public void setTargetDBSchema(String targetDBSchema) {
		this.targetDBSchema = targetDBSchema;
	}

	/**
	 * @param targetID the targetID to set
	 */
	public void setTargetID(int targetID) {
		this.targetID = targetID;
	}

	/**
	 * @param targetProject the targetProject to set
	 */
	public void setTargetProjectID(int targetProjectID) {
		this.targetProjectID = targetProjectID;
	}

	/**
	 * @param userID the userID to set
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	/**
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}
	

	
}
