package de.umg.mi.idrt.ioe.OntologyTree;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

public class TargetProjects {

	List<TargetProject> list = new LinkedList<TargetProject>();
	int highestTargetID = -1;
	int highestTargetProject = -1;
	TargetProject selectedTargetProject = null;
	Target selectedTarget = null;
	

	public TargetProjects() {

	}

	/**
	 * @return the selectedTargetProject
	 */
	public TargetProject getSelectedTargetProject() {
		return selectedTargetProject;
	}

	/**
	 * @param selectedTargetProject the selectedTargetProject to set
	 */
	public void setSelectedTargetProject(TargetProject selectedTargetProject) {
		this.selectedTargetProject = selectedTargetProject;
	}

	/**
	 * @return the highestTargetID
	 */
	public int getHighestTargetID() {
		return highestTargetID;
	}

	/**
	 * @param highestTargetID the highestTargetID to set
	 */
	public void setHighestTargetID(int highestTargetID) {
		this.highestTargetID = highestTargetID;
	}

	/**
	 * @return the highestTargetProject
	 */
	public int getHighestTargetProject() {
		return highestTargetProject;
	}

	/**
	 * @param highestTargetProject the highestTargetProject to set
	 */
	public void setHighestTargetProject(int highestTargetProject) {
		this.highestTargetProject = highestTargetProject;
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

	public TargetProject getTargetProjectByID(int id) {
		for (TargetProject tmpTargetProject : list) {
			if (tmpTargetProject.getTargetProjectID() == id)
				return tmpTargetProject;
		}
		return null;
	}

	public TargetProject getTargetProjectByLastEdit() {

		TargetProject targetProject = null;
		for (TargetProject tmpTargetProject : list) {

		}
		return targetProject;
	}

	public List<TargetProject> getTargetProjectsList() {
		return list;
	}

	public void add(int id, TargetProject targetProject) {
		list.add(id, targetProject);
	}

	public void add(TargetProject targetProject) {
		list.add(targetProject);
	}
	
	public void addTargetByTargetProjectID(int targetID, int targetProjectID, int version, Date created, Date lastModified, String targetDBSchema){
		this.getTargetProjectByID(targetProjectID).add(targetID, targetProjectID, version, created, lastModified, targetDBSchema);
	}
	
	public void addTarget(Target target){
		TargetProject targetProject = this.getTargetProjectByID(target.getTargetProjectID());
		targetProject.add(target);
		
		if (this.getSelectedTarget() == null || target.getLastModified().after(this.getSelectedTarget().getLastModified())){
			this.setSelectedTarget(target);
			this.setSelectedTargetProject(targetProject);
		}
	}

	public boolean isEmpty(){
		return list.isEmpty();
	}

}
