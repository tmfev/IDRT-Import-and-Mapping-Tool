package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import java.util.Iterator;

import javax.swing.tree.MutableTreeNode;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTrees;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeModel;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeSubNode;
import de.umg.mi.idrt.ioe.view.EditorTargetInfoView;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class DeleteNodeCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		TreeViewer targetTreeViewer = OntologyEditorView.getTargetTreeViewer();
		IStructuredSelection selection = (IStructuredSelection) targetTreeViewer
				.getSelection();

		Iterator<MutableTreeNode> nodeIterator = selection.iterator();
		
		while (nodeIterator.hasNext()) {
			MutableTreeNode mNode = nodeIterator.next();
			if ( mNode instanceof OntologyTreeNode) {
				
				OntologyTreeNode node = (OntologyTreeNode) mNode;
				OntologyEditorView.setNotYetSaved(true);
				if (!(node ==OntologyEditorView.getOntologyTargetTree().getI2B2RootNode())) {
					node.removeFromParent();
				}
				else {
					
					node.removeFromParent();
					node.getChildren().clear();
					OntologyEditorView.getOntologyTargetTree()
					.getNodeLists().add(node);
					//			editorTargetView.setComposite(OTTarget);
					//TODO Ontology Editor
					//			firstElement.removeFromParent();
//					for (OntologyTreeNode child : node.getChildren()) {
//						System.out.println(child.getName());
//						node.remove(child);
//					}
				}
			}
			else if (mNode instanceof OntologyTreeSubNode) {
				OntologyTreeSubNode subNode = (OntologyTreeSubNode) mNode;
				subNode.getParent().getTargetNodeAttributes().removeSubNode(subNode);
			}
			
		}
		targetTreeViewer.refresh();
		return null;

	}

}