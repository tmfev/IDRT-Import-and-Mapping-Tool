package de.umg.mi.idrt.ioe.OntologyTree;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ViewTreeNode {


	private ViewTreeNode parent;
	private String name;
	private List<ViewTreeNode> children = new ArrayList<ViewTreeNode>();
	private OntologyTreeNode otNode = null;

	public ViewTreeNode(ViewTreeNode parent, OntologyTreeNode otNode, String name) {
		super();

		this.parent = parent;
		this.otNode = otNode;
		this.name = name;
		if (parent != null) {
			parent.children.add(this);
		}
	}

	public ViewTreeNode getParent() {
		return parent;
	}

	public void setParent(ViewTreeNode parent) {
		this.parent = parent;
	}

	public Collection<ViewTreeNode> getChildren() {
		return new ArrayList<ViewTreeNode>(children);
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

	public void setChildren(Collection<ViewTreeNode> newChildren) {
		if (newChildren != null) {
			children = new ArrayList<ViewTreeNode>(newChildren);
		}
	}

	public String getName() {
		return name;
	}

	public OntologyTreeNode getOTNode() {
		return otNode;
	}

	public boolean isRoot() {
		return parent == null ? true : false;
	}

	public void addChild(ViewTreeNode child) {
		if (child.getParent() != null) {
			((ViewTreeNode) child.getParent()).children.remove(child);
		}
		child.setParent(this);
		children.add(child);
	}

	public void removeChild(ViewTreeNode node) {
		children.remove(node);
		node.setParent(null);
	}

	public void insertAfter(Object targetObj) {
		ViewTreeNode target = convertToViewTreeNode(targetObj);
		List<ViewTreeNode> childrenList = target.parent.children;
		int targetIndex = childrenList.indexOf(target);
		int thisIndex;
		int index;
		if (target.parent == this.parent) {
			thisIndex = childrenList.indexOf(this);
			if (thisIndex < targetIndex)
				index = targetIndex;
			else
				index = targetIndex + 1;
		} else {
			index = targetIndex + 1;
		}
		insertChild(target, index);
	}

	public void insertBefore(Object targetObj) {
		ViewTreeNode target = convertToViewTreeNode(targetObj);
		int index;
		int thisIndex;
		int targetIndex = target.parent.children.indexOf(target);
		if (target.parent == this.parent) {
			thisIndex = target.parent.children.indexOf(this);
			if (thisIndex < targetIndex) {
				index = targetIndex - 1;
			} else {
				index = targetIndex;
			}
		} else {
			index = targetIndex;
		}
		insertChild(target, index);
	}

	private void insertChild(ViewTreeNode target, int index) {
		if (parent != null)
			parent.removeChild(this);
		target.parent.children.add(index, this);
		this.setParent(target.parent);
	}

	private ViewTreeNode convertToViewTreeNode(Object obj) {
		if (obj instanceof ViewTreeNode) {
			return (ViewTreeNode) obj;
		}
		return null;
	}

	/**
	 * Always treat de-serialization as a full-blown constructor, by validating
	 * the final state of the de-serialized object.
	 */
	private void readObject(ObjectInputStream aInputStream)
			throws ClassNotFoundException, IOException {
		// always perform the default de-serialization first
		aInputStream.defaultReadObject();

		// make defensive copy of the mutable Date field
		// fDateOpened = new Date(fDateOpened.getTime());

		// ensure that object state has not been corrupted or tampered with
		// maliciously
		// validateState();
	}

	/**
	 * This is the default implementation of writeObject. Customise if
	 * necessary.
	 */
	private void writeObject(ObjectOutputStream aOutputStream)
			throws IOException {
		// perform the default serialization for all non-transient, non-static
		// fields
		aOutputStream.defaultWriteObject();
	}
}
