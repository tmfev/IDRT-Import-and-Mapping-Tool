package de.umg.mi.idrt.ioe.OntologyTree;

import java.util.HashMap;
import java.util.LinkedHashSet;

import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class TargetNodeAttributes {

	private String stagingPath = "";
	private String name = "";
	private String nodeType = "";
	private String startDateSource = "";
	private String endDateSource = "";
	private String visualattribute = "";
	private String dimension = "concept_dimension";
	private LinkedHashSet<OntologyTreeSubNode> subNodeList;

	private OntologyTreeNode parent;
	private HashMap<String, String> targetNodeMap;
	
	/**
	 * @return the targetNodeMap
	 */
	public HashMap<String, String> getTargetNodeMap() {
		return targetNodeMap;
	}

	/**
	 * @param targetNodeMap the targetNodeMap to set
	 */
	public void setTargetNodeMap(HashMap<String, String> targetNodeMap) {
		this.targetNodeMap = targetNodeMap;
	}
	

	public TargetNodeAttributes(OntologyTreeNode parent) {
		targetNodeMap = new HashMap<String, String>();
		targetNodeMap.put(Resource.I2B2.NODE.TARGET.M_APPLIED_PATH, "@");
		subNodeList = new LinkedHashSet<OntologyTreeSubNode>();
		setParent(parent);
	}

	/**
	 * @param stagingPath
	 *            the sourcePath to set
	 */
	public void addStagingPath(String stagingPath) {
//		System.out.println(stagingPath);
		if (stagingPath == null)
			stagingPath = "";
		
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
//					System.out.println("NULL");
					//					stagingNode = OntologyEditorView.getMyOntologyTree().getOntologyTreeTarget().getNodeLists().getNodeByPath(stagingPath);
					subNode.setStagingName("not found");
//					System.out.println(subNode.getTargetSubNodeAttributes().getParent().getStagingName());
				}
				else if (stagingNode != null)
					subNode.setStagingName(stagingNode.getName());
			}
			subNodeList.add(subNode);
			this.stagingPath = stagingPath;
		}
		
//		LinkedHashSet<OntologyTreeSubNode> tmp = new LinkedHashSet<OntologyTreeSubNode>();
//		tmp.addAll(subNodeList);
//		for (OntologyTreeSubNode s : tmp) {
//			if (subNodeList.size()>1 && s.getStagingName() != null)
//				if (s.getStagingName().equals("not found"))
//					subNodeList.remove(s);
//		}
	}

	/**
	 * @return the dimension
	 */
	public String getDimension() {
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
		return stagingPath;
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
	public void setDimension(String dimension) {
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
