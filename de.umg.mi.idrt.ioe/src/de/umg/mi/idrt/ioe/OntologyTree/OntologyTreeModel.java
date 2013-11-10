package de.umg.mi.idrt.ioe.OntologyTree;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import de.umg.mi.idrt.ioe.Console;

/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> Department of
 *         Medical Informatics Goettingen www.mi.med.uni-goettingen.de
 * 
 *         
 */

@SuppressWarnings("serial")
public class OntologyTreeModel extends DefaultTreeModel {

    private Vector<TreeModelListener> treeModelListeners =
        new Vector<TreeModelListener>();
    private OntologyTreeNode rootPerson;


    
	  public OntologyTreeModel(OntologyTreeNode ontologyTreeNode) {
		  super(ontologyTreeNode);
	        rootPerson = ontologyTreeNode;
	    }

	    /**
		* Registers a listere to the model.
		* 
		* @param listener the listener to add
		*/
		public void addTreeModelListener(TreeModelListener listener){
		    	listenerList.add(TreeModelListener.class, listener);
		}


	//////////////// Fire events //////////////////////////////////////////////

	    public void fireTreeNodesChanged(TreeModelEvent e) {
        	Enumeration<TreeModelListener> listeners = this.treeModelListeners.elements();
            while (listeners.hasMoreElements()) {
                TreeModelListener listener = 
                        listeners.nextElement();
                listener.treeNodesChanged(e);
            }
        }


	//////////////// TreeModel interface implementation ///////////////////////

	    /**
	     * Adds a listener for the TreeModelEvent posted after the tree changes.
	     */
	    /*
	    public void addTreeModelListener(TreeModelListener l) {
	        treeModelListeners.addElement(l);
	    }
	     */
	    
	    protected void fireTreeNodesInserted(Object source, Object[] path, 
		    int[] childIndices, 
		    Object[] children) {
			Object[] listeners = listenerList.getListenerList();
			TreeModelEvent e = null;
			for (int i = listeners.length-2; i>=0; i-=2) {
				if (listeners[i]==TreeModelListener.class) {
					if (e == null)
					e = new TreeModelEvent(source, path, 
					               childIndices, children);
					((TreeModelListener)listeners[i+1]).treeNodesInserted(e);
				}      
			}
		}

	    /**
		 * fireTreeStructureChanged
		 * 
		 * @param source the node where the model has changed
		 * @param path the path to the root node
		 * @param childIndices the indices of the affected elements
		 * @param children the affected elements
		 */
		protected void fireTreeStructureChanged(Object source, Object[] path,
				int[] childIndices, Object[] children)
		{
			TreeModelEvent event = new TreeModelEvent(source, path, childIndices,
					children);
			TreeModelListener[] listeners = getTreeModelListeners();

			for (int i = listeners.length - 1; i >= 0; --i)
				listeners[i].treeStructureChanged(event);
		}

	    /**
	     * The only event raised by this model is TreeStructureChanged with the
	     * root as path, i.e. the whole tree has changed.
	     */
	    protected void fireTreeStructureChanged(OntologyTreeNode oldRoot) {
	        int len = treeModelListeners.size();
	        TreeModelEvent e = new TreeModelEvent(this, 
	                                              new Object[] {oldRoot});
	        for (TreeModelListener tml : treeModelListeners) {
	            tml.treeStructureChanged(e);
	        }
	    }

	    /**
	     * Returns the child of parent at index index in the parent's child array.
	     */
	    public Object getChild(Object parent, int index) {
	    	OntologyTreeNode p = (OntologyTreeNode)parent;
	        return p.getChildAt(index);
	    }

	    /**
	     * Returns the number of children of parent.
	     */
	    public int getChildCount(Object parent) {
	    	OntologyTreeNode p = (OntologyTreeNode)parent;
	        return p.getChildCount();
	    }

	    /**
	     * Removes a listener previously added with addTreeModelListener().
	     */
	    /*
	    public void removeTreeModelListener(TreeModelListener l) {
	        treeModelListeners.removeElement(l);
	    }
	    */

	    /**
	     * Returns the index of child in parent.
	     */
	    public int getIndexOfChild(Object parent, Object child) {
	    	OntologyTreeNode p = (OntologyTreeNode)parent;
	        return p.getIndex((OntologyTreeNode)child);
	    }
	    
	    /**
	     * Returns the root of the tree.
	     */
	    public Object getRoot() {
	        return rootPerson;
	    }
	    
        /**
	     * Returns true if node is a leaf.
	     */
	    public boolean isLeaf(Object node) {
	    	OntologyTreeNode p = (OntologyTreeNode)node;
	        return p.getChildCount() == 0;
	    } 
        
        
    public void nodeChanged(OntologyTreeNode node) {
		Console.info("nodeChanged");
		OntologyTreeNode parent = (OntologyTreeNode)node.getParent();
		int[] childIndices = new int[1];
		childIndices[0] = getIndexOfChild(parent, node);
		Object[] children = new Object[1];
		children[0] = node;
		//fireTreeNodesChanged(this, getPathToRoot(node), childIndices, children);
		this.reload(node);
		
		
	}
    
    /**
	 * Invoke this method if you've modified the TreeNodes upon which this model
	 * depends. The model will notify all of its listeners that the model has
	 * changed. It will fire the events, necessary to update the layout caches and
	 * repaint the tree. The tree will <i>not</i> be properly refreshed if you
	 * call the JTree.repaint instead.
	 * 
	 * @param node - the tree node, from which the tree nodes have changed
	 *          (inclusive). If you do not know this node, call {@link #reload()}
	 *          instead.
	 */
	    public void reload(OntologyTreeNode node)
	    {
	    	int n = getChildCount(node);
	    	int[] childIdx = new int[n];
	    	Object[] children = new Object[n];
	
	    	for (int i = 0; i < n; i++)
	    	{
	    		childIdx[i] = i;
	    		children[i] = getChild(node, i);
	    	}
	
	    	fireTreeStructureChanged(this, getPathToRoot(node), childIdx, children);
	    }
    

/**
 * Used to toggle between show ancestors/show descendant and
 * to change the root of the tree.
 */
public void showAncestor(boolean b, Object newRoot) {
    OntologyTreeNode oldRoot = rootPerson;
    if (newRoot != null) {
       rootPerson = (OntologyTreeNode)newRoot;
    }
    fireTreeStructureChanged(oldRoot);
}

    /**
	 * Messaged when the user has altered the value for the item
	 * identified by path to newValue.  Not used by this model.
	 */
	public void valueForPathChanged(TreePath path, Object newValue) {
	    Console.info("*** valueForPathChanged : "
	                       + path + " --> " + newValue);
	}


}
