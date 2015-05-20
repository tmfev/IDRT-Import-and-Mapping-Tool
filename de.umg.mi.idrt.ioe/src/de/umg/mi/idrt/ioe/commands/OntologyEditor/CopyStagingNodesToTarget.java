package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTrees;
import de.umg.mi.idrt.ioe.OntologyTree.NodeDragListener;
import de.umg.mi.idrt.ioe.OntologyTree.NodeDropListener;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeSubNode;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class CopyStagingNodesToTarget extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("@Execute");
		if (NodeDropListener.getTargetNode() instanceof OntologyTreeNode) {
			OntologyTreeNode targetNode = (OntologyTreeNode) NodeDropListener.getTargetNode();
//			System.out.println("targetNodePath "+targetNodePath);
			if (targetNode != null) {
				System.out.println("TRUE");
				OntologyEditorView.getMyOntologyTree().copySourceNodeToTarget(null,targetNode);

			} else {
				System.out.println("ELSE");
//				System.err.println("Error while copying nodes: SourceNode (\""
//						+ sourceNodePath + "\") "
//						+ ") and TargetNode (\"" + targetNodePath + "\") "
//						+ (targetNode != null ? "found" : "NOT found") + ".");
			}
		}
		else if (NodeDropListener.getTargetNode() instanceof OntologyTreeSubNode) {
			System.out.println("ELSE IF");
			OntologyTreeSubNode subNode = (OntologyTreeSubNode) NodeDropListener.getTargetNode();
			System.out.println("DROPPED ON SUBNODE");
			OntologyEditorView.getMyOntologyTree().copySourceNodeToTarget(null,subNode.getParent());
		}
		return null;
	}

}
