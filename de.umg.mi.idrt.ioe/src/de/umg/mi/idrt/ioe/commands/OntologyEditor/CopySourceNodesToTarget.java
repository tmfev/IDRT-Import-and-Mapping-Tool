package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.umg.mi.idrt.ioe.Activator;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Debug;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;

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

		MyOntologyTree myOT = Activator.getDefault().getResource()
				.getI2B2ImportTool().getMyOntologyTrees();

		OntologyTreeNode sourceNode = myOT.getOntologyTreeSource()
				.getNodeLists().getNodeByPath(sourceNodePath);

		OntologyTreeNode targetNode = myOT.getOntologyTreeTarget()
				.getNodeLists().getNodeByPath(targetNodePath);

		Debug.dd(" - sourceNode: "
				+ (sourceNode != null ? "found" : "NOT found"));
		Debug.dd(" - targetNode: "
				+ (targetNode != null ? "found" : "NOT found"));

		Debug.dd(" - nodeLists: "
				+ myOT.getOntologyTreeTarget().getNodeLists().stringPathToNode
						.keySet().toString());

		if (sourceNode != null && targetNode != null) {

			OntologyTreeNode newNode = myOT.copySourceNodeToTarget(sourceNode,
					targetNode);
			Application.getEditorTargetView().setSelection(newNode);

		} else {
			Console.error("Error while copying nodes: SourceNode (\""
					+ sourceNodePath + "\") "
					+ (sourceNode != null ? "found" : "NOT found")
					+ ") and TargetNode (\"" + targetNodePath + "\") "
					+ (targetNode != null ? "found" : "NOT found") + ".");
		}

		return null;
	}

}
