package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Debug;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.NodeDropListener;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeSubNode;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class SetTargetAttribute extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Debug.f("execute", this);

		String sourceNodePath = event
				.getParameter(Resource.ID.Command.OTSETTARGETATTRIBUTE_ATTRIBUTE_SOURCE_NODE_PATH);
		String targetNodePath = event
				.getParameter(Resource.ID.Command.OTSETTARGETATTRIBUTEY_ATTRIBUTE_TARGET_NODE_PATH);
		String attribute = event
				.getParameter(Resource.ID.Command.OTSETTARGETATTRIBUTEY_ATTRIBUTE_ATTRIBUTE);

		System.out.println("OTSetTargetAttribute: ");

		System.out.println(" - Paras size: " + event.getParameters().size());

		System.out.println(" - OTSETTARGETATTRIBUTE_ATTRIBUTE_SOURCE_NODE_PATH:"
				+ sourceNodePath);
		System.out.println(" - OTSETTARGETATTRIBUTEY_ATTRIBUTE_TARGET_NODE_PATH:"
				+ targetNodePath);
		System.out.println(" - OTSETTARGETATTRIBUTEY_ATTRIBUTE_ATTRIBUTE:"
				+ attribute);
		Debug.d("OTSETTARGETATTRIBUTE_ATTRIBUTE_SOURCE_NODE_PATH:" + sourceNodePath);
		Debug.d("OTSETTARGETATTRIBUTEY_ATTRIBUTE_TARGET_NODE_PATH:" + targetNodePath);
		Debug.d("OTSETTARGETATTRIBUTEY_ATTRIBUTE_ATTRIBUTE:" + attribute);

		MyOntologyTree myOT = OntologyEditorView.getI2b2ImportTool()
				.getMyOntologyTrees();

		OntologyTreeNode sourceNode = myOT.getOntologyTreeSource().getNodeLists()
				.getNodeByPath(sourceNodePath);

		if (NodeDropListener.getTargetNode() instanceof OntologyTreeNode) {
			OntologyTreeNode targetNode =  (OntologyTreeNode) NodeDropListener.getTargetNode();

			System.out.println(" - sourceNode: "
					+ (sourceNode != null ? "found" : "NOT found"));
			System.out.println(" - targetNode: "
					+ (targetNode != null ? "found" : "NOT found"));

			//		System.out.println(" - nodeLists: "
			//				+ myOT.getOntologyTreeTarget().getNodeLists().stringPathToNode.keySet()
			//						.toString());

			// myOT.getOTTarget().printTree();

			if (sourceNode != null && targetNode != null) {
				System.out.println("sourceNode found:" + sourceNode.getName());
				myOT.setTargetAttributesAsSourcePath(sourceNode, targetNode, attribute);



				Application.getEditorTargetInfoView().setNode( targetNode );

				Application.getEditorTargetInfoView().refresh();

				//Application.getEditorTargetView().getTreeViewer().refresh();

			} else {
				Console.error("Error while combining nodes: SourceNode (\""
						+ sourceNodePath + "\") and/or TargetNode (\""
						+ targetNodePath + "\") not found.");
			}
		}
		else if (NodeDropListener.getTargetNode() instanceof OntologyTreeSubNode) {
			OntologyTreeSubNode subNode = (OntologyTreeSubNode) NodeDropListener.getTargetNode();
			if (attribute.equals("startDateSource"))
				subNode.getTargetSubNodeAttributes().setStartDateSourcePath(sourceNodePath);
			else if (attribute.equals("endDateSource"))
				subNode.getTargetSubNodeAttributes().setEndDateSourcePath(sourceNodePath);
		}
		return null;
	}

}
