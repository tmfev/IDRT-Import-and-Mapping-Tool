package de.umg.mi.idrt.ioe.OntologyTree;

import javax.swing.tree.DefaultMutableTreeNode;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 * Department of Medical Informatics Goettingen
 * www.mi.med.uni-goettingen.de
 */

public class OntologyTreeSubNode extends DefaultMutableTreeNode{
	private static final long serialVersionUID = 1L;
	private OntologyTreeNode parent;
	private String stagingPath;
	private String stagingName;
	private boolean highlighted;
	private OntologyTreeNode stagingParentNode;
	private TargetSubNodeAttributes targetSubNodeAttributes;

	public OntologyTreeSubNode(OntologyTreeNode parent) {
		if (parent != null) {
			if (parent.getTargetNodeAttributes().getSourcePath() != null)
				this.setStagingPath(parent.getTargetNodeAttributes().getSourcePath());
			
			
			targetSubNodeAttributes = new TargetSubNodeAttributes(this);
//			targetNodeAttributes = new TargetNodeAttributes(parent);
		this.parent = parent;
		}
		else {
			System.err.println("Parent null");
		}
//		this.setStagingParentNode(parent);
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {

//		System.err.println("SN FINALIZE: " + this.getStagingName());
		super.finalize();
	}
	
	/**
	 * @return the parent
	 */
	public OntologyTreeNode getParent() {
		return parent;
	}

	public String getStagingName() {
		return stagingName;
	}

	public OntologyTreeNode getStagingParentNode() {
		return stagingParentNode;
	}

	public String getStagingPath() {
		return stagingPath;
	}

	public TargetSubNodeAttributes getTargetSubNodeAttributes() {
		return targetSubNodeAttributes;
	}

	public boolean isHighlighted() {
		return this.highlighted;
	}

	public void setEndDate(String endDateStagingPath) {
		this.targetSubNodeAttributes.setEndDateSourcePath(endDateStagingPath);
	}
	/**
	 * @param b
	 */
	public void setHighlighted(boolean b) {
		this.highlighted = b;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(OntologyTreeNode parent) {
		this.parent = parent;
	}

	public void setStagingName(String stagingName) {
		this.stagingName = stagingName;
	}

	public void setStagingParentNode(OntologyTreeNode stagingParentNode) {
		this.stagingParentNode = stagingParentNode;
	}

	public void setStagingPath(String sourcePath) {
		this.stagingPath = sourcePath;
		
		if (OntologyEditorView.getMyOntologyTree() !=null) {
			OntologyTreeNode parentNode = OntologyEditorView.getMyOntologyTree().getOntologyTreeSource().getNodeLists().getNodeByPath(sourcePath);
			if (parentNode == null) {
				parentNode = OntologyEditorView.getMyOntologyTree().getOntologyTreeTarget().getNodeLists().getNodeByPath(sourcePath);
			}
			this.setStagingParentNode(parentNode);
		}
	}
	
	public void setStartDate(String startDateStagingPath) {
		this.targetSubNodeAttributes.setStartDateSourcePath(startDateStagingPath);
	}
	public void setTargetNodeAttributes(TargetSubNodeAttributes targetSubNodeAttributes) {
		this.targetSubNodeAttributes = targetSubNodeAttributes;
	}

}
