package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import de.goettingen.i2b2.importtool.idrt.StatusListener.StatusListener;
import de.umg.mi.idrt.ioe.ActionCommand;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.Dimension;
import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTrees;
import de.umg.mi.idrt.ioe.OntologyTree.NodeDropListener;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.Resource.I2B2.NODE.TYPE;
import de.umg.mi.idrt.ioe.misc.Progress;
import de.umg.mi.idrt.ioe.misc.Regex;
import de.umg.mi.idrt.ioe.view.EditorTargetInfoView;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;
import de.umg.mi.idrt.ioe.view.ProgressView;

public class CombineNodesCommand extends AbstractHandler {

	private static LinkedHashSet<Regex> regexSet = new LinkedHashSet<Regex>();
	private List<OntologyTreeNode> oldTreeNodeList;
	private List<OntologyTreeNode> newTreeNodeList;
	private String perfectPath;
	private static String OPSREGEX = "[135689]{1}\\-[a-z0-9]{3}\\.[a-z0-9]+";
	private static String ICDREGEX = "[A-TV-Z][0-9][A-Z0-9](\\.[A-Z0-9]{1,4})?";
	private static Progress pro;

	public static void addRegEx(Regex regex) {
		regexSet.add(regex);
	}

	/**
	 * 
	 */
	public static void clear() {
		regexSet.clear();

	}

	public static LinkedHashSet<Regex> getRegex(){
		return regexSet;
	}
	/**
	 * @param regex
	 */
	public static void removeRegex(Regex regex) {
		regexSet.remove(regex);

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		oldTreeNodeList  = new LinkedList<OntologyTreeNode>();
		newTreeNodeList = new LinkedList<OntologyTreeNode>();
		perfectPath = "NOT FOUND YET";
		TreeViewer targetTreeViewer = OntologyEditorView.getTargetTreeViewer();
		OntologyEditorView.setNotYetSaved(true);

		OntologyEditorView.getTargetTreeViewer().getTree().setRedraw(false);
		if (NodeDropListener.getTargetNode() instanceof OntologyTreeNode) {
			final OntologyTreeNode targetNode = (OntologyTreeNode) NodeDropListener.getTargetNode();

			getOldTargetNodes(targetNode);
			for (OntologyTreeNode node : oldTreeNodeList) {
				//				System.out.println("Next OLD leaf: " + node.getName() + " parent: " + node.getParent().getName());

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

			Application.executeCommand(command);

			getnewTargetNodes(targetNode);

			//TODO start progress bar
			ProgressView.setProgress(0, "Merging...", "");
			
			String regex = MyOntologyTrees.getCurrentRegEx();
			generatePerfectPath(oldTreeNodeList, newTreeNodeList, regex);
			mergeLeafs(oldTreeNodeList, newTreeNodeList,regex);
			OntologyEditorView.getTargetTreeViewer().getTree().setRedraw(true);
			OntologyEditorView.getTargetTreeViewer().refresh();
			EditorTargetInfoView.refresh();
			StatusListener.setSubStatus(0, "");
			//			ProgressView.clearProgress();
			return null;
		}
		else {
			return null;
		}

	}

	private void generatePerfectPath(List<OntologyTreeNode> oldTreeNodeList2, List<OntologyTreeNode> newTreeNodeList2, String regex) {
		//TODO MAGICALLY MERGE
		Regex currentRegex = null;
		for (Regex r : regexSet) {
			if (r.getName().equals(regex)) {
				currentRegex=r;
				break;
			}
		}

		for (OntologyTreeNode node : newTreeNodeList2) {
			boolean found = false;
			for (OntologyTreeNode nodeToCheck : oldTreeNodeList2) {
				Pattern p = Pattern.compile(currentRegex.getRegex());
				Matcher m = p.matcher(nodeToCheck.getID());
				if (m.find()) {
					String icd = m.group();
					if (node.getID().contains(icd)) {
						perfectPath = nodeToCheck.getTargetNodeAttributes().getSourcePath().substring(0, nodeToCheck.getTargetNodeAttributes().getSourcePath().indexOf(nodeToCheck.getID()));
						found = true;
						break;
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
				if (!child2.isMerged() && child2.getTargetNodeAttributes().getVisualattribute().toLowerCase().startsWith("l")) {
					newTreeNodeList.add(child2);
				}
			}
		}
	}

	private void getOldTargetNodes(OntologyTreeNode child){
		if (child.hasChildren()) {
			for (OntologyTreeNode child2 : child.getChildren()) {
				getOldTargetNodes(child2);
				if (!child2.isMerged() && child2.getTargetNodeAttributes().getVisualattribute().toLowerCase().startsWith("l"))
					oldTreeNodeList.add(child2);
			}
		}
	}

	/**
	 * @param oldTreeNodeList2
	 * @param newTreeNodeList2
	 */
	private void mergeLeafs(List<OntologyTreeNode> oldTreeNodeList2, List<OntologyTreeNode> newTreeNodeList2, String regex) {
		//TODO MAGICALLY MERGE		
		OntologyTreeNode unmatchedFolder = null;
		int numberOfNotFoundItems = 0;

		Regex currentRegex = null;
		for (Regex r : regexSet) {
			if (r.getName().equals(regex)) {
				currentRegex=r;
				break;
			}
		}

		System.out.println("OLD LIST: " + oldTreeNodeList2.size());
		System.out.println("NEW LIST: " + newTreeNodeList2.size());
		int counter = 0;
		int mod = 1;
		int allRows = oldTreeNodeList2.size();
		if(allRows/100 > 0)
			mod=allRows/100;

		Pattern p = Pattern.compile(currentRegex.getRegex());
		for (OntologyTreeNode oldNode : oldTreeNodeList2) {
			counter++;
			if (counter%(mod)==0 || counter==allRows){
				StatusListener.setSubStatus((float)counter/allRows*100, (int)((float)counter/allRows*100)+"% " + "Merging: " + counter + "/"+allRows);
				ProgressView.setProgress(counter/allRows*100, "Merging...", counter + "/"+allRows);
				ProgressView.updateProgressBar();
			}
			boolean found = false;
			for (OntologyTreeNode newNode :  newTreeNodeList2) {
				Matcher m = p.matcher(oldNode.getID());
				if (m.find()) {
					String icd = m.group();
					//					if (newNode.getOntologyCellAttributes().getC_BASECODE().contains(icd)) {
					if (newNode.getOntologyCellAttributes().getOntologyTable().get(currentRegex.getTable()).contains(icd)) {
						//						System.out.println(currentRegex.getName() + " " + oldNode.getID() + " IN " + newNode.getID());
						//						newNode.getTargetNodeAttributes().removeAllStagingPaths();
						newNode.getTargetNodeAttributes().addStagingPath(oldNode.getTargetNodeAttributes().getSourcePath());
						newNode.setMerged(true);
						oldNode.setMerged(true);
						//						newTreeNodeList2.remove(newNode);
						//						System.out.println("removed " + newNode.getID());
						found = true;
						break;
					}
				}
				newNode.setMerged(true);
				//				oldNode.setMerged(true);
				if (found) {
					break;
				}
				else {
					//					newNode.getTargetNodeAttributes().removeAllStagingPaths();
					newNode.getTargetNodeAttributes().addStagingPath(perfectPath+newNode.getID()+"\\");
				}
			}
			if (!found) {
				numberOfNotFoundItems++;
				if (numberOfNotFoundItems==1) {
					unmatchedFolder = new OntologyTreeNode("Unmerged",true);
					
					unmatchedFolder.setType(TYPE.ONTOLOGY_TARGET);
					unmatchedFolder.getTargetNodeAttributes().addStagingPath("");
					unmatchedFolder.getTargetNodeAttributes().setDimension(Dimension.CONCEPT_DIMENSION);
					unmatchedFolder.getTargetNodeAttributes().setVisualattributes("FAE");
					unmatchedFolder.getTargetNodeAttributes().getTargetNodeMap().put(Resource.I2B2.NODE.TARGET.M_APPLIED_PATH, "@");
					unmatchedFolder.setID("Unmerged");
					unmatchedFolder.setName("Unmerged");
//					unmatchedFolder.getOntologyCellAttributes().setC_NAME("Unmerged");
					unmatchedFolder.setCustomNode(true);
					((OntologyTreeNode)NodeDropListener.getTargetNode()).add(unmatchedFolder);
					unmatchedFolder.setTreeAttributes();
					unmatchedFolder.getTargetNodeAttributes().getTargetNodeMap().put(Resource.I2B2.NODE.TARGET.C_BASECODE, unmatchedFolder.getTreePath().replaceAll("\\\\", "|").substring(0, unmatchedFolder.getTreePath().length()-1));
					OntologyEditorView.getOntologyTargetTree().getNodeLists().add(unmatchedFolder);
				}
				oldNode.getTargetNodeAttributes().addStagingPath(perfectPath+oldNode.getID()+"\\");
				unmatchedFolder.add(oldNode);
				oldNode.setTreeAttributes();
				
//				unmatchedFolder.setTreeAttributes();
				
				
				OntologyEditorView.getOntologyTargetTree().getNodeLists().add(oldNode);
			}
		}

		if (numberOfNotFoundItems>0) {
			ProgressView.setProgress(100,"Merging... (Finished)", numberOfNotFoundItems + " items not merged!");
			MessageDialog.openConfirm(Application.getShell(), "Merging finished!", "Merging finished. " + numberOfNotFoundItems + " items not merged!\nSee \"Unmerged\" Folder!");
		}
		else {
			ProgressView.setProgress(100,"Merging... (Finished)","OK");
		}
		System.out.println("not found: " + numberOfNotFoundItems);
	}
}