package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Debug;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class CopySourceNodesToTarget extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Debug.f("execute", this);

		String sourceNodePath = event
				.getParameter(Resource.ID.Command.OTCOPY_ATTRIBUTE_SOURCE_NODE_PATH);
		String targetNodePath = event
				.getParameter(Resource.ID.Command.OTCOPY_ATTRIBUTE_TARGET_NODE_PATH);

		Debug.d("CopySourceNodesToTarget: ");
		Debug.dd("COPY_ATTRIBUTE_SOURCE_NODE_PATH:" + sourceNodePath);
		Debug.dd("COPY_ATTRIBUTE_TARGET_NODE_PATH:" + targetNodePath);
		Console.info("Copying files from source \"" + sourceNodePath
				+ "\" to target \"" + targetNodePath + "\"");

		MyOntologyTree myOT = OntologyEditorView.getI2b2ImportTool().getMyOntologyTrees();

//		OntologyTreeNode sourceNode = myOT.getOntologyTreeSource()
//				.getNodeLists().getNodeByPath(sourceNodePath);
		OntologyTreeNode targetNode = myOT.getOntologyTreeTarget()
				.getNodeLists().getNodeByPath(targetNodePath);
System.out.println("targetNodePath "+targetNodePath);
		if (targetNode != null) {

			myOT.copySourceNodeToTarget(null,targetNode);

		} else {
			System.err.println("Error while copying nodes: SourceNode (\""
					+ sourceNodePath + "\") "
					+ ") and TargetNode (\"" + targetNodePath + "\") "
					+ (targetNode != null ? "found" : "NOT found") + ".");
		}
		return null;
	}

}
