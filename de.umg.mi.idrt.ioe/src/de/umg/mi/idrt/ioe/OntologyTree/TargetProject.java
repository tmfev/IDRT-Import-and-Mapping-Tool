package de.umg.mi.idrt.ioe.OntologyTree;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class TargetProject {

	/**
	 * the id of the target project
	 */
	private int targetProjectID = -1;
	private Target highestTarget;
	
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
		checkHighestTargetVersion(target);
		return target;
	}

	public void add(Target target){
		list.add(target);
		checkHighestTargetVersion(target);
	}

	/*
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
	*/

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the targetProject
	 */
	public int getTargetProjectID() {
		return targetProjectID;
	}

	public List<Target> getTargetsList() {
		return list;
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param targetProject
	 *            the targetProject to set
	 */
	public void setTargetProjectID(int targetProjectID) {
		this.targetProjectID = targetProjectID;
	}
	
	public void clear() {
		this.list.clear();
	}
	
	public void removeTarget(Target target){
		this.list.remove(target);
	
		OntologyEditorView.removeVersionFromCombo(String.valueOf( target.getVersion() ));
		
		
		if (target == OntologyEditorView.getTargetProjects().getSelectedTarget()){
			Console.info("Selected Target was deleted.");
			highestTarget = null;
			for (Target tmpTarget : list ){
				checkHighestTargetVersion(tmpTarget);
			}
			OntologyEditorView.getTargetProjects().setSelectedTarget(highestTarget );
		}
		
		OntologyEditorView.refreshTargetVersionGUI();
	
	}
	
	private void checkHighestTargetVersion(Target newTarget){
		
		if ( highestTarget != null ){
			
			if ( highestTarget.getVersion() < newTarget.getVersion() ){
				Console.info("Setting new HighestTargetVersion: " + newTarget.getVersion() + "(old: " + highestTarget.getVersion() + ")");
				highestTarget = newTarget;
			}
		} else {
			highestTarget = newTarget;
		}
	}
	
	public Target getHighestTarget(){
		return highestTarget;
	}

}
