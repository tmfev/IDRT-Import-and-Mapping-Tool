package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import de.umg.mi.idrt.ioe.ActionCommand;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTrees;
import de.umg.mi.idrt.ioe.OntologyTree.NodeDropListener;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNodeList;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.view.EditorTargetInfoView;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class CombineNodesCommand extends AbstractHandler {
	
	private static String OPSREGEX = "[135689]{1}\\-[a-z0-9]{3}\\.[a-z0-9]+";
	private static String ICDREGEX = "[A-TV-Z][0-9][A-Z0-9](\\.[A-Z0-9]{1,4})?";
	
	private List<OntologyTreeNode> oldTreeNodeList;
	private List<OntologyTreeNode> newTreeNodeList;
	private String perfectPath;
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		oldTreeNodeList  = new LinkedList<OntologyTreeNode>();
		newTreeNodeList = new LinkedList<OntologyTreeNode>();
		perfectPath = "NOT FOUND YET";
		TreeViewer targetTreeViewer = OntologyEditorView.getTargetTreeViewer();
		OntologyEditorView.setNotYetSaved(true);
		System.out.println("INSERT CLICKED");
		OntologyEditorView.getTargetTreeViewer().refresh();

		
		if (NodeDropListener.getTargetNode() instanceof OntologyTreeNode) {
		final OntologyTreeNode targetNode = (OntologyTreeNode) NodeDropListener.getTargetNode();
		
		System.out.println(targetNode.getName());
		getOldTargetNodes(targetNode);
		for (OntologyTreeNode node : oldTreeNodeList) {
			System.out.println("Next OLD leaf: " + node.getName() + " parent: " + node.getParent().getName());
			node.removeFromParent();
			targetTreeViewer.update(node, null);
		}
		targetNode.removeAllChildren();
		targetTreeViewer.update(targetNode, null);
		ActionCommand command = new ActionCommand(
				Resource.ID.Command.OTCOPY);
		command.addParameter(
				Resource.ID.Command.OTCOPY_ATTRIBUTE_SOURCE_NODE_PATH,
				"");
		command.addParameter(
				Resource.ID.Command.OTCOPY_ATTRIBUTE_TARGET_NODE_PATH,
				targetNode.getTreePath());
		OntologyEditorView.getTargetTreeViewer().getTree().setRedraw(false);
		Application.executeCommand(command);
		OntologyEditorView.getTargetTreeViewer().getTree().setRedraw(true);
		OntologyEditorView.getTargetTreeViewer().refresh();


		getnewTargetNodes(targetNode);

		for (OntologyTreeNode node : newTreeNodeList) {
//			System.out.println("Next new leaf: " + node.getName());
		}

		generatePerfectPath(oldTreeNodeList, newTreeNodeList);
		mergeLeafs(oldTreeNodeList, newTreeNodeList);

		targetTreeViewer.refresh();
		EditorTargetInfoView.refresh();
		return null;
		}
		else {
			return null;
		}
	}
	private void generatePerfectPath(List<OntologyTreeNode> oldTreeNodeList2, List<OntologyTreeNode> newTreeNodeList2) {
		//TODO MAGICALLY MERGE		
		for (OntologyTreeNode node : newTreeNodeList2) {
			//			System.out.println("CHECKING " + node.getName());
			boolean found = false;
			for (OntologyTreeNode nodeToCheck : oldTreeNodeList2) {
//				System.out.println("nodeToCheck id: " + nodeToCheck.getID());
				Pattern p = Pattern.compile(ICDREGEX);
				Matcher m = p.matcher(nodeToCheck.getID());
				if (m.find()) {
					String icd = m.group();
					if (node.getID().contains(icd)) {
//						System.out.println(node.getID() + " IN " + nodeToCheck.getID());
//						System.out.println("SourcePATH: " + nodeToCheck.getTargetNodeAttributes().getSourcePath());
						perfectPath = nodeToCheck.getTargetNodeAttributes().getSourcePath().substring(0, nodeToCheck.getTargetNodeAttributes().getSourcePath().indexOf(nodeToCheck.getID()));
//						System.out.println(perfectPath);
						found = true;
						break;
					}
				}
				else {
					p = Pattern.compile(OPSREGEX);
					m = p.matcher(nodeToCheck.getID());
					if (m.find()) {
						String ops = m.group();
						if (node.getID().contains(ops)) {
//							System.out.println("OPS: " + node.getID() + " IN " + nodeToCheck.getID());
//							System.out.println("SourcePATH: " + nodeToCheck.getTargetNodeAttributes().getSourcePath());
							perfectPath = nodeToCheck.getTargetNodeAttributes().getSourcePath().substring(0, nodeToCheck.getTargetNodeAttributes().getSourcePath().indexOf(nodeToCheck.getID()));
//							System.out.println(perfectPath);
							found = true;
							break;
						}
					}
				}
				if (found)
					break;
			}
			if (found)
				break;
		}
		System.out.println("perfectPath: " + perfectPath);
	}

	private void getnewTargetNodes(OntologyTreeNode child){
		if (child.hasChildren()) {
			for (OntologyTreeNode child2 : child.getChildren()) {
				getnewTargetNodes(child2);
				if (child2.getTargetNodeAttributes().getVisualattribute().toLowerCase().startsWith("l")) {
					newTreeNodeList.add(child2);
				}
			}
		}
	}

	private void getOldTargetNodes(OntologyTreeNode child){
		if (child.hasChildren()) {
			for (OntologyTreeNode child2 : child.getChildren()) {
				getOldTargetNodes(child2);
				if (child2.getTargetNodeAttributes().getVisualattribute().toLowerCase().startsWith("l"))
					oldTreeNodeList.add(child2);
			}
		}
	}
	/**
	 * @param oldTreeNodeList2
	 * @param newTreeNodeList2
	 */
	private void mergeLeafs(List<OntologyTreeNode> oldTreeNodeList2, List<OntologyTreeNode> newTreeNodeList2) {
		//TODO MAGICALLY MERGE		
		for (OntologyTreeNode node : newTreeNodeList2) {
			boolean found = false;
			for (OntologyTreeNode nodeToCheck : oldTreeNodeList2) {
				Pattern p = Pattern.compile(ICDREGEX);
				Matcher m = p.matcher(nodeToCheck.getID());
				if (m.find()) {
					String icd = m.group();
					if (node.getID().contains(icd)) {
//						System.out.println("ICD: " + node.getID() + " IN " + nodeToCheck.getID());
						node.getTargetNodeAttributes().removeAllStagingPaths();
						node.getTargetNodeAttributes().addStagingPath(nodeToCheck.getTargetNodeAttributes().getSourcePath());
						found = true;
						break;
					}
				}
				else {
					 p = Pattern.compile(OPSREGEX);
					 m = p.matcher(nodeToCheck.getID());
					if (m.find()) {
						String icd = m.group();
						if (node.getID().contains(icd)) {
//							System.out.println("OPS: " + node.getID() + " IN " + nodeToCheck.getID());
							node.getTargetNodeAttributes().removeAllStagingPaths();
							node.getTargetNodeAttributes().addStagingPath(nodeToCheck.getTargetNodeAttributes().getSourcePath());
							found = true;
							break;
						}
					}
				}
			}
			if (!found) {
//				System.out.println("not found: " + node.getID() + " " +perfectPath+node.getID()+"\\");
				node.getTargetNodeAttributes().removeAllStagingPaths();
				node.getTargetNodeAttributes().addStagingPath(perfectPath+node.getID()+"\\");
			}
		}
	}
}