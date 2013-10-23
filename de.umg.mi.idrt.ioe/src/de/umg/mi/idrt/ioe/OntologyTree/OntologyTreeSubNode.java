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

	public OntologyTreeSubNode(OntologyTreeNode parent) { //, String sourcePath
		if (parent != null) {
			if (parent.getTargetNodeAttributes().getSourcePath() != null)
				this.setStagingPath(parent.getTargetNodeAttributes().getSourcePath());
		
		this.parent = parent;
		}
		//		System.out.println(this.parent.getName() + " " + this.getSourcePath());
	}

	/**
	 * @return the parent
	 */
	public OntologyTreeNode getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(OntologyTreeNode parent) {
		this.parent = parent;
	}

	public String getStagingPath() {
		return stagingPath;
	}

	public void setStagingPath(String sourcePath) {
		this.stagingPath = sourcePath;
		if (OntologyEditorView.getI2b2ImportTool()!=null)
			this.setStagingParentNode(OntologyEditorView.getI2b2ImportTool().getMyOntologyTrees().getOntologyTreeSource().getNodeLists().getNodeByPath(sourcePath));
	}

	public String getStagingName() {
		return stagingName;
	}

	public void setStagingName(String stagingName) {
		this.stagingName = stagingName;
	}

	/**
	 * @param b
	 */
	public void setHighlighted(boolean b) {
		this.highlighted = b;

	}
	/**
	 * 
	 */
	public boolean isHighlighted() {
		// TODO Auto-generated method stub
		return this.highlighted;
	}

	public OntologyTreeNode getStagingParentNode() {
		return stagingParentNode;
	}

	public void setStagingParentNode(OntologyTreeNode stagingParentNode) {
		this.stagingParentNode = stagingParentNode;
	}

}
