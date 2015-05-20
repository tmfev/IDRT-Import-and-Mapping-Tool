package de.umg.mi.idrt.ioe.OntologyTree;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

public class TargetInstances {

	private List<TargetInstance> list = new LinkedList<TargetInstance>();
//	private int highestTargetID = -1;
//	private int highestTargetProject = -1;
	private TargetInstance selectedTargetProject = null;
	private Target selectedTarget = null;
	private Target previousSelectedTarget;
	


	public TargetInstances() {

	}

	public void setPreviousSelectedTarget(Target lastSelectedTarget) {
		System.out.println("lastSelectedTargetVersoin: " + lastSelectedTarget.getVersion());
		this.previousSelectedTarget = lastSelectedTarget;
	}

	public Target getPreviousSelectedVersion(){
		if (previousSelectedTarget == null)
			return selectedTarget;
		else
			return previousSelectedTarget;

	}

	public void add(int id, TargetInstance targetProject) {
		list.add(id, targetProject);
	}

	public void add(TargetInstance targetProject) {
		list.add(targetProject);
	}

	public void addTarget(Target target){
		TargetInstance targetProject = this.getTargetProjectByID(target.getTargetProjectID());
		targetProject.add(target);

		/*
		if ( this.getSelectedTarget() != null )
			System.out.println("#addTarget compare " + target.getLastModified() + " _after_ " + this.getSelectedTarget().getLastModified() );
		else 
			System.out.println("#addTarget compare " + target.getLastModified() + " _after_ null" );
		 */
		if (this.getSelectedTarget() == null || target.getLastModified().after(this.getSelectedTarget().getLastModified())){
			//System.out.println("#--> yes. set selected!");
			this.setSelectedTarget(target);
			this.setSelectedTargetProject(targetProject);
		}
	}

	public void addTargetByTargetProjectID(int targetID, int targetProjectID, int version, Date created, Date lastModified, String targetDBSchema){
		this.getTargetProjectByID(targetProjectID).add(targetID, targetProjectID, version, created, lastModified, targetDBSchema);
	}

//	/**
//	 * @return the highestTargetID
//	 */
//	public int getHighestTargetID() {
//		return highestTargetID;
//	}
//
//	/**
//	 * @return the highestTargetProject
//	 */
//	public int getHighestTargetProject() {
//		return highestTargetProject;
//	}

	/**
	 * @return the selectedTarget
	 */
	public Target getSelectedTarget() {
		return selectedTarget;
	}

	/**
	 * @return the selectedTargetProject
	 */
	public TargetInstance getSelectedTargetInstance() {
		return selectedTargetProject;
	}

	public TargetInstance getTargetProjectByID(int id) {
		for (TargetInstance tmpTargetProject : list) {
			if (tmpTargetProject.getTargetInstanceID() == id)
				return tmpTargetProject;
		}
		return null;
	}

	public TargetInstance getTargetProjectByLastEdit() {

		TargetInstance targetProject = null;
		for (TargetInstance tmpTargetProject : list) {

		}
		return targetProject;
	}

	public List<TargetInstance> getTargetProjectsList() {
		return list;
	}

	public Target incrementVersion(Target oldTarget) {
		Target newTarget = new Target();
		newTarget.setTargetProjectID(oldTarget.getTargetProjectID());
		newTarget.setTargetDBSchema(oldTarget.getTargetDBSchema());
		this.getSelectedTargetInstance().add(newTarget);
		return newTarget;
	}

	public boolean isEmpty(){
		return list.isEmpty();
	}

//	/**
//	 * @param highestTargetID the highestTargetID to set
//	 */
//	public void setHighestTargetID(int highestTargetID) {
//		this.highestTargetID = highestTargetID;
//	}
//
//	/**
//	 * @param highestTargetProject the highestTargetProject to set
//	 */
//	public void setHighestTargetProject(int highestTargetProject) {
//		this.highestTargetProject = highestTargetProject;
//	}

	/**
	 * @param selectedTarget
	 *            the selectedTarget to set
	 */
	public void setSelectedTarget(Target newSelectedTarget) {
		if (selectedTarget!= null) {
//			System.out.println("Setting selected Target (V:"+newSelectedTarget.getVersion()+") with previousSelected (V:"+selectedTarget.getVersion()+")");
			setPreviousSelectedTarget(selectedTarget);
		}
		this.selectedTarget = newSelectedTarget;
	}

	/**
	 * @param selectedTargetProject the selectedTargetProject to set
	 */
	public void setSelectedTargetProject(TargetInstance selectedTargetProject) {
		this.selectedTargetProject = selectedTargetProject;
	}

	public void clear(){
		setSelectedTarget(null);
		setSelectedTargetProject(null);
		for (TargetInstance targetProject: getTargetProjectsList()) {
			targetProject.clear();
		}
		getTargetProjectsList().clear();


	}


	public Target getTargetByVersion(int version) {

		for (Target tmpTarget : getSelectedTargetInstance().getTargetsList()) {
			if (tmpTarget.getVersion() == version)
				return tmpTarget;
		}
		return null;
	}

	public Target getTargetByID(int id) {

		for (Target tmpTarget : getSelectedTargetInstance().getTargetsList()) {
			if (tmpTarget.getTargetID() == id)
				return tmpTarget;
		}
		return null;
	}

}
