package de.umg.mi.idrt.ioe.OntologyTree;

import java.util.HashSet;
import java.util.LinkedHashSet;

import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class TargetNodeAttributes {

	private String sourcePath = "";
	private String name = "";
	//private String nodeType = "";
	private String startDateSource = "";
	private String endDateSource = "";
	private String visualattribute = "";
	private Dimension dimension = Dimension.CONCEPT_DIMENSION;
	private LinkedHashSet<OntologyTreeSubNode> subNodeList;

	public TargetNodeAttributes(OntologyTreeNode parent) {
		subNodeList = new LinkedHashSet<OntologyTreeSubNode>();
		setParent(parent);
	}

	public boolean removeSubNode(OntologyTreeSubNode subNode) {
		return this.subNodeList.remove(subNode);
	}

	/**
	 * @return the subNodeList
	 */
	public LinkedHashSet<OntologyTreeSubNode> getSubNodeList() {
		return subNodeList;
	}

	/**
	 * @param hashSet the subNodeList to set
	 */
	public void setSubNodeList(LinkedHashSet<OntologyTreeSubNode> hashSet) {
		this.subNodeList = hashSet;
	}

	private OntologyTreeNode parent;

	boolean changed = false;

	/**
	 * @return the sourcePath
	 */
	public String getSourcePath() {
		return sourcePath;
	}

	/**
	 * @param stagingPath
	 *            the sourcePath to set
	 */
	public void addStagingPath(String stagingPath) {
		if (stagingPath.startsWith("\\\\"))
			stagingPath=stagingPath.substring(1); //removes wrong "\" from beginning of path
		if (!stagingPath.endsWith("\\"))
			stagingPath=stagingPath+"\\";
		if (!subNodeListContains(stagingPath)) {
			OntologyTreeSubNode subNode = new OntologyTreeSubNode(getParent());
			subNode.setStagingPath(stagingPath);
			if (OntologyEditorView.getI2b2ImportTool()!=null) {
				OntologyTreeNode stagingNode = OntologyEditorView.getI2b2ImportTool().getMyOntologyTrees().getOntologyTreeSource().getNodeLists().getNodeByPath(stagingPath);
				if (stagingNode==null) {
					stagingNode = OntologyEditorView.getI2b2ImportTool().getMyOntologyTrees().getOntologyTreeTarget().getNodeLists().getNodeByPath(stagingPath);
				}
				if (stagingNode != null)
					subNode.setStagingName(stagingNode.getName());
				else
					subNode.setStagingName("not found");
			}
			subNodeList.add(subNode);
			this.sourcePath = stagingPath;
		}
	}

	private boolean subNodeListContains(String pathToChek) {
		boolean found = false;
		for (OntologyTreeSubNode subNode : this.subNodeList) {
			if (subNode.getStagingPath().equals(pathToChek)) {
				found = true;
				break;
			}
		}
		return found;
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

//
//	/**
//	 * @return the nodeType
//	 */
//	public String getNodeType() {
//		return nodeType;
//	}
//
//	/**
//	 * @param nodeType
//	 *            the nodeType to set
//	 */
//	public void setNodeType(String nodeType) {
//		this.nodeType = nodeType;
//	}
	
	/**
	 * @return the startDateSource
	 */
	public String getStartDateSource() {
		return startDateSource;
	}

	/**
	 * @param startDateSource
	 *            the startDateSource to set
	 */
	public void setStartDateSourcePath(String startDateSource) {
		this.startDateSource = startDateSource;
	}

	/**
	 * @return the endDateSource
	 */
	public String getEndDateSource() {
		return endDateSource;
	}

	/**
	 * @param endDateSource
	 *            the endDateSource to set
	 */
	public void setEndDateSourcePath(String endDateSource) {
		this.endDateSource = endDateSource;
	}

	/**
	 * @return the visualattribute
	 */
	public String getVisualattribute() {
		return visualattribute;
	}

	/**
	 * @param visualattribute
	 *            the visualattribute to set
	 */
	public void setVisualattributes(String visualattribute) {
		this.visualattribute = visualattribute;
	}

	/**
	 * @return the changed
	 */
	public boolean isChanged() {
		return changed;
	}

	/**
	 * @param changed
	 *            the changed to set
	 */
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public String getChanged() {
		return changed ? "0" : "1";
	}

	public OntologyTreeNode getParent() {
		return parent;
	}

	public void setParent(OntologyTreeNode parent) {
		this.parent = parent;
	}

	/**
	 * 
	 */
	public void removeAllStagingPaths() {
		subNodeList.clear();
	}
	
	/**
	 * @return the dimension
	 */
	public Dimension getDimension() {
		return dimension;
	}

	/**
	 * @param dimension the dimension to set
	 */
	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

}
