package de.umg.mi.idrt.ioe.OntologyTree;

import java.util.LinkedHashSet;

import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class TargetNodeAttributes {

	String sourcePath = "";
	String name = "";
	String nodeType = "";
	String startDateSource = "";
	String endDateSource = "";
	String visualattribute = "";
	Dimension dimension = Dimension.CONCEPT_DIMENSION;
	private LinkedHashSet<OntologyTreeSubNode> subNodeList;

	private OntologyTreeNode parent;

	public TargetNodeAttributes(OntologyTreeNode parent) {
		subNodeList = new LinkedHashSet<OntologyTreeSubNode>();
		setParent(parent);
	}

	/**
	 * @param stagingPath
	 *            the sourcePath to set
	 */
	public void addStagingPath(String stagingPath) {
		if (stagingPath.startsWith("\\\\"))
			stagingPath=stagingPath.substring(1); //removes wrong "\" from beginning of path
		if (!stagingPath.endsWith("\\") && !stagingPath.isEmpty())
			stagingPath=stagingPath+"\\";
		if (!subNodeListContains(stagingPath)) {
			OntologyTreeSubNode subNode = new OntologyTreeSubNode(getParent());
			subNode.setStagingPath(stagingPath);
			if (OntologyEditorView.getMyOntologyTree()!=null) {
				OntologyTreeNode stagingNode = OntologyEditorView.getMyOntologyTree().getOntologyTreeSource().getNodeLists().getNodeByPath(stagingPath);
				if (stagingNode==null) {
					//					stagingNode = OntologyEditorView.getMyOntologyTree().getOntologyTreeTarget().getNodeLists().getNodeByPath(stagingPath);
					subNode.setStagingName("not found");
				}
				else if (stagingNode != null)
					subNode.setStagingName(stagingNode.getName());
			}
			subNodeList.add(subNode);
			this.sourcePath = stagingPath;
		}
		
		LinkedHashSet<OntologyTreeSubNode> tmp = new LinkedHashSet<OntologyTreeSubNode>();
		tmp.addAll(subNodeList);
		for (OntologyTreeSubNode s : tmp) {
			if (subNodeList.size()>1 && s.getStagingName() != null)
				if (s.getStagingName().equals("not found"))
					subNodeList.remove(s);
		}
	}

	/**
	 * @return the dimension
	 */
	public Dimension getDimension() {
		return dimension;
	}

	/**
	 * @return the endDateSource
	 */
	public String getEndDateSource() {
		return endDateSource;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public OntologyTreeNode getParent() {
		return parent;
	}

	/**
	 * @return the sourcePath
	 */
	public String getSourcePath() {
		return sourcePath;
	}

	/**
	 * @return the startDateSource
	 */
	public String getStartDateSource() {
		return startDateSource;
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
	 * @return the subNodeList
	 */
	public LinkedHashSet<OntologyTreeSubNode> getSubNodeList() {
		return subNodeList;
	}

	/**
	 * @return the visualattribute
	 */
	public String getVisualattribute() {
		return visualattribute;
	}

	/**
	 * @return the changed
	 */

	/**
	 * 
	 */
	public void removeAllStagingPaths() {
		subNodeList.clear();
	}

	public boolean removeSubNode(OntologyTreeSubNode subNode) {
		return this.subNodeList.remove(subNode);
	}

	/**
	 * @param dimension the dimension to set
	 */
	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	/**
	 * @param endDateSource
	 *            the endDateSource to set
	 */
	public void setEndDateSourcePath(String endDateSource) {
		this.endDateSource = endDateSource;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public void setParent(OntologyTreeNode parent) {
		this.parent = parent;
	}

	/**
	 * @param startDateSource
	 *            the startDateSource to set
	 */
	public void setStartDateSourcePath(String startDateSource) {
		this.startDateSource = startDateSource;
	}

	/**
	 * @param hashSet the subNodeList to set
	 */
	public void setSubNodeList(LinkedHashSet<OntologyTreeSubNode> hashSet) {
		this.subNodeList = hashSet;
	}

	/**
	 * @param visualattribute
	 *            the visualattribute to set
	 */
	public void setVisualattributes(String visualattribute) {
		this.visualattribute = visualattribute;
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

}
