package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
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
import de.umg.mi.idrt.ioe.misc.Regex;
import de.umg.mi.idrt.ioe.view.EditorTargetInfoView;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;
import de.umg.mi.idrt.ioe.view.ProgressView;

public class CombineNodesCommand extends AbstractHandler {
	final static Logger logger = Logger.getLogger(CombineNodesCommand.class);
	private static LinkedHashSet<Regex> regexSet = new LinkedHashSet<Regex>();
	private TreeSet<OntologyTreeNode> oldTreeNodeList;
	private TreeSet<OntologyTreeNode> newTreeNodeList;
	private String perfectPath;
	private LinkedHashMap<String,OntologyTreeNode> terms;
	//	private static String OPSREGEX = "[135689]{1}\\-[a-z0-9]{3}\\.[a-z0-9]+";
	//	private static String ICDREGEX = "[A-TV-Z][0-9][A-Z0-9](\\.[A-Z0-9]{1,4})?";
	//	private static Progress pro;

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
		oldTreeNodeList  = new TreeSet<OntologyTreeNode>();
		newTreeNodeList = new TreeSet<OntologyTreeNode>();
		perfectPath = "NOT FOUND YET";
		TreeViewer targetTreeViewer = OntologyEditorView.getTargetTreeViewer();
		OntologyEditorView.setNotYetSaved(true);


		if (NodeDropListener.getTargetNode() instanceof OntologyTreeNode) {
			final OntologyTreeNode targetNode = (OntologyTreeNode) NodeDropListener.getTargetNode();
			if (targetNode.isModifier()){
				MessageDialog.openError(Application.getShell(), "Cannot Merge with Modifier", "You cannot merge terminologies with modifier.\nSorry!");
				System.err.println("NO CAN DO");
			}
			else {
				OntologyEditorView.getTargetTreeViewer().getTree().setRedraw(false);
				oldTreeNodeList = getOldTargetNodes(targetNode);
				for (OntologyTreeNode node : oldTreeNodeList) {
					//				System.out.println("Next OLD leaf: " + node.getName() + " parent: " + node.getParent().getName());
					//					logger.debug("node name: " + node.getName());
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
				System.out.println(regex);
				perfectPath = generatePerfectPath(oldTreeNodeList, newTreeNodeList, regex);
				terms = new LinkedHashMap<String, OntologyTreeNode>();
				for (OntologyTreeNode n : newTreeNodeList){
					terms.put(n.getID(),n);
				}
				mergeLeafs(oldTreeNodeList, newTreeNodeList,regex, terms);
				OntologyEditorView.getTargetTreeViewer().getTree().setRedraw(true);
				OntologyEditorView.getTargetTreeViewer().refresh();
				EditorTargetInfoView.refresh();
				StatusListener.setSubStatus(0, "");
				//			ProgressView.clearProgress();
			}
			return null;
		}
		else {
			return null;
		}

	}

	public String generatePerfectPath(TreeSet<OntologyTreeNode> oldTreeNodeList2, TreeSet<OntologyTreeNode> newTreeNodeList2, String regex) {
		//TODO MAGICALLY MERGE
		String perfPath = "";
		Regex currentRegex = null;
		for (Regex r : regexSet) {
			if (r.getName().equals(regex)) {

				currentRegex=r;
				break;
			}
		}
		System.out.println(currentRegex.getRegex());
		System.out.println("-----------");
		System.out.println(oldTreeNodeList2);
		System.out.println("-----------");
		System.out.println(newTreeNodeList2);

		for (OntologyTreeNode node : newTreeNodeList2) {
			boolean found = false;
			for (OntologyTreeNode nodeToCheck : oldTreeNodeList2) {
				Pattern p = Pattern.compile(currentRegex.getRegex());


				String id = nodeToCheck.getID(); 
				if (currentRegex.getName().equalsIgnoreCase("ops") && !id.contains("-")){
					id = id.substring(0,1)+"-"+id.substring(1,4) + "."+id.substring(4,id.length());
					if (id.endsWith("."))
						id = id.substring(0,id.length()-1);
				}
				else if (currentRegex.getName().equalsIgnoreCase("icd") ){
					if (id.endsWith("+"))
						id = id.substring(0,id.length()-1);
					if (id.length()==6)
						id=id.substring(0,5);
				}
				Matcher m = p.matcher(id);
				if (m.find()) {
					String icd = m.group();
					if (node.getID().contains(icd)) {
						perfPath = nodeToCheck.getTargetNodeAttributes().getSourcePath().substring(0, nodeToCheck.getTargetNodeAttributes().getSourcePath().indexOf(nodeToCheck.getID()));
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
		logger.debug("Perfect Path: " + perfPath);
		return perfPath;
	}
	public void getnewTargetNodes(OntologyTreeNode child){
		if (child.hasChildren()) {
			for (OntologyTreeNode child2 : child.getChildren()) {
				getnewTargetNodes(child2);
				if (!child2.isMerged() && (child2.getTargetNodeAttributes().getVisualattribute().toLowerCase().startsWith("l") || child2.getTargetNodeAttributes().getVisualattribute().toLowerCase().startsWith("r"))) {
					newTreeNodeList.add(child2);
				}
			}
		}
	}

	public TreeSet<OntologyTreeNode> getOldTargetNodes(OntologyTreeNode child){
		TreeSet<OntologyTreeNode> oldTreeNodeList2 = new TreeSet<OntologyTreeNode>();
		if (child.hasChildren()) {
			for (OntologyTreeNode child2 : child.getChildren()) {
				getOldTargetNodes(child2);
				if (!child2.isMerged() && (child2.getTargetNodeAttributes().getVisualattribute().toLowerCase().startsWith("l") || child2.getTargetNodeAttributes().getVisualattribute().toLowerCase().startsWith("r")))
					oldTreeNodeList2.add(child2);
			}
		}
		return oldTreeNodeList2;
	}

	/**
	 * @param oldTreeNodeList2
	 * @param newTreeNodeList2
	 * @param terms2 
	 */
	public void mergeLeafs(TreeSet<OntologyTreeNode> oldTreeNodeList2, TreeSet<OntologyTreeNode> newTreeNodeList2, String regex, LinkedHashMap<String, OntologyTreeNode> terms2) {
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
		int fors = 0;
		Pattern p = Pattern.compile(currentRegex.getRegex());
		for (OntologyTreeNode oldNode : oldTreeNodeList2) {
			String id = oldNode.getID(); 
			if (currentRegex.getName().equalsIgnoreCase("ops") && !id.contains("-")){
				id = id.substring(0,1)+"-"+id.substring(1,4) + "."+id.substring(4,id.length());
				if (id.endsWith("."))
					id = id.substring(0,id.length()-1);
			}
			else if (currentRegex.getName().equalsIgnoreCase("icd") ){
				if (id.endsWith("+"))
					id = id.substring(0,id.length()-1);
				//				if (id.length()==6)
				//					id=id.substring(0,5);
			}
			logger.debug("Checking: " + id);
			counter++;
			if (counter%(mod)==0 || counter==allRows){
				StatusListener.setSubStatus((float)counter/allRows*100, (int)((float)counter/allRows*100)+"% " + "Merging: " + counter + "/"+allRows);
				ProgressView.setProgress(counter/allRows*100, "Merging...", counter + "/"+allRows);
				ProgressView.updateProgressBar();
			}
			boolean found = false;
			int innter = 0;
			if (terms.containsKey(id)){
				found = true;
				logger.debug("IN terms: " + terms.get(id).getName());
				OntologyTreeNode newNode = terms.get(id);
				if (!oldNode.isModifier()){
					fors++;
					innter++;

					Matcher m = p.matcher(id);
					if (m.find()) {
						String icd = m.group();
						//					if (newNode.getOntologyCellAttributes().getC_BASECODE().contains(icd)) {
						if (newNode.getOntologyCellAttributes().getOntologyTable().get(currentRegex.getTable()).contains(icd)) {
							System.out.println(currentRegex.getName() + " " + oldNode.getID() + " IN " + newNode.getID());
							//						newNode.getTargetNodeAttributes().removeAllStagingPaths();
							//							
							if (!newNode.isMerged())
								newNode.getTargetNodeAttributes().removeAllStagingPaths();
							newNode.getTargetNodeAttributes().addStagingPath(oldNode.getTargetNodeAttributes().getSourcePath());


							newNode.setMerged(true);
							oldNode.setMerged(true);
							//						newTreeNodeList2.remove(newNode);
							//						System.out.println("removed " + newNode.getID());
						}
					}
					//newNode.setMerged(true);
					//				oldNode.setMerged(true);
				}
			}

			else {
				id = id.substring(0,id.length()-1);
				if (terms.containsKey(id)){
					found = true;
					logger.debug("IN terms: " + terms.get(id).getName());
					OntologyTreeNode newNode = terms.get(id);
					if (!oldNode.isModifier()){
						fors++;
						innter++;

						Matcher m = p.matcher(id);
						if (m.find()) {
							String icd = m.group();
							//					if (newNode.getOntologyCellAttributes().getC_BASECODE().contains(icd)) {
							if (newNode.getOntologyCellAttributes().getOntologyTable().get(currentRegex.getTable()).contains(icd)) {
								System.out.println(currentRegex.getName() + " " + oldNode.getID() + " IN " + newNode.getID());
								//						newNode.getTargetNodeAttributes().removeAllStagingPaths();
								//							
								if (!newNode.isMerged())
									newNode.getTargetNodeAttributes().removeAllStagingPaths();
								newNode.getTargetNodeAttributes().addStagingPath(oldNode.getTargetNodeAttributes().getSourcePath());


								newNode.setMerged(true);
								oldNode.setMerged(true);
								//						newTreeNodeList2.remove(newNode);
								//						System.out.println("removed " + newNode.getID());
							}
						}
						//newNode.setMerged(true);
						//				oldNode.setMerged(true);
					}
				}
				else
					logger.debug("NOT IN terms");
			}
			//			for (OntologyTreeNode newNode :  newTreeNodeList2) {
			//				if (!oldNode.isModifier()){
			//					fors++;
			//					innter++;
			//					
			//					Matcher m = p.matcher(id);
			//					if (m.find()) {
			//						String icd = m.group();
			//						//					if (newNode.getOntologyCellAttributes().getC_BASECODE().contains(icd)) {
			//						if (newNode.getOntologyCellAttributes().getOntologyTable().get(currentRegex.getTable()).contains(icd)) {
			//							System.out.println(currentRegex.getName() + " " + oldNode.getID() + " IN " + newNode.getID());
			//							//						newNode.getTargetNodeAttributes().removeAllStagingPaths();
			//							newNode.getTargetNodeAttributes().removeAllStagingPaths();
			//							newNode.getTargetNodeAttributes().addStagingPath(oldNode.getTargetNodeAttributes().getSourcePath());
			//
			//
			//							newNode.setMerged(true);
			//							oldNode.setMerged(true);
			//							//						newTreeNodeList2.remove(newNode);
			//							//						System.out.println("removed " + newNode.getID());
			//							found = true;
			//							logger.debug("Found after: " + innter + " fors");
			//							break;
			//						}
			//					}
			//					//newNode.setMerged(true);
			//					//				oldNode.setMerged(true);
			//					if (found) {
			//						break;
			//					}
			//					else {
			//						if (!newNode.isMerged()){
			////							newNode.setMerged(true);
			//							System.out.println("ADDING PATH: " + perfectPath+newNode.getID()+"\\");
			//							newNode.getTargetNodeAttributes().removeAllStagingPaths();
			//							newNode.getTargetNodeAttributes().addStagingPath(perfectPath+newNode.getID()+"\\");
			//						}
			//					}
			//				}
			//			}
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
		logger.debug("FORS: " + fors);
	}
}