package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Debug;
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
		Debug.f("execute", this);

//		String sourceNodePath = event
//				.getParameter(Resource.ID.Command.OTCOPY_ATTRIBUTE_SOURCE_NODE_PATH);
//		String targetNodePath = event
//				.getParameter(Resource.ID.Command.OTCOPY_ATTRIBUTE_TARGET_NODE_PATH);

		Debug.d("CopySourceNodesToTarget: ");
//		Debug.dd("COPY_ATTRIBUTE_SOURCE_NODE_PATH:" + sourceNodePath);
//		Debug.dd("COPY_ATTRIBUTE_TARGET_NODE_PATH:" + targetNodePath);
//		Console.info("Copying files from source \"" + sourceNodePath
//				+ "\" to target \"" + targetNodePath + "\"");

		if (NodeDropListener.getTargetNode() instanceof OntologyTreeNode) {
			OntologyTreeNode targetNode = (OntologyTreeNode) NodeDropListener.getTargetNode();
//			System.out.println("targetNodePath "+targetNodePath);
			if (targetNode != null) {
				OntologyEditorView.getMyOntologyTree().copySourceNodeToTarget(null,targetNode);

			} else {
//				System.err.println("Error while copying nodes: SourceNode (\""
//						+ sourceNodePath + "\") "
//						+ ") and TargetNode (\"" + targetNodePath + "\") "
//						+ (targetNode != null ? "found" : "NOT found") + ".");
			}
		}
		else if (NodeDropListener.getTargetNode() instanceof OntologyTreeSubNode) {
			OntologyTreeSubNode subNode = (OntologyTreeSubNode) NodeDropListener.getTargetNode();
			System.out.println("DROPPED ON SUBNODE");
			OntologyEditorView.getMyOntologyTree().copySourceNodeToTarget(null,subNode.getParent());
		}
		return null;
	}

}
