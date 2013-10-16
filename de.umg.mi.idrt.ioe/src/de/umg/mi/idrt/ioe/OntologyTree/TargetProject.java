package de.umg.mi.idrt.ioe.OntologyTree;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

public class TargetProject {

	/**
	 * the id of the target project
	 */
	private int targetProjectID = -1;

	private int highestTargetVersion = 0;
	Target selectedTarget = null;
	List<Target> list = new LinkedList<Target>();

	/**
	 * the name of this target project
	 */
	private String name = "";

	/**
	 * the description of this target project
	 */
	private String description = "";

	public TargetProject() {

	}

	/**
	 * @return the selectedTarget
	 */
	public Target getSelectedTarget() {
		return selectedTarget;
	}

	/**
	 * @param selectedTarget
	 *            the selectedTarget to set
	 */
	public void setSelectedTarget(Target selectedTarget) {
		this.selectedTarget = selectedTarget;
	}

	/**
	 * @return the highestVersion
	 */
	public int getHighestVersion() {
		return highestTargetVersion;
	}

	/**
	 * @param highestVersion
	 *            the highestVersion to set
	 */
	public void setHighestVersion(int highestVersion) {
		this.highestTargetVersion = highestVersion;
	}

	/**
	 * @return the targetProject
	 */
	public int getTargetProjectID() {
		return targetProjectID;
	}

	/**
	 * @param targetProject
	 *            the targetProject to set
	 */
	public void setTargetProjectID(int targetProjectID) {
		this.targetProjectID = targetProjectID;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public void createNewTargetVersion() {
		java.util.Date today = new java.util.Date();
		java.sql.Date date = new java.sql.Date(today.getTime());

		Target target = new Target();

		target.setTargetProjectID(this.getTargetProjectID());
		target.setCreated(date);
		target.setLastModified(date);
		target.setVersion(this.getHighestVersion() + 1);
		this.setHighestVersion(target.getVersion());

	}

	public List<Target> getTargetsList() {
		return list;
	}

	public Target add(int targetID, int targetProjectID, int version,
			Date created, Date lastModified, String targetDBSchema) {

		Target target = new Target();

		target.setTargetID(targetID);
		target.setTargetProjectID(targetProjectID);
		target.setVersion(version);
		target.setCreated(created);
		target.setLastModified(lastModified);

		// this.setHighestVersion(target.getVersion());

		list.add(targetID, target);
		return target;
	}
	
	public void add(Target target){
		list.add(target.getTargetID(), target);
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}
}
