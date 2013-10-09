package de.umg.mi.idrt.ioe.OntologyTree;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import de.umg.mi.idrt.ioe.ActionCommand;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> Department of
 *         Medical Informatics Goettingen www.mi.med.uni-goettingen.de
 * 
 *         drop listener for getting data from the source tree to the target
 *         tree
 * 
 */

public class NodeDropListener extends ViewerDropAdapter {

	private MyOntologyTree myOT;
	private final Viewer viewer;
	private OntologyTreeNode sourceNode = null;
	private OntologyTreeNode targetNode = null;

	public NodeDropListener(Viewer viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	@Override
	public void drop(DropTargetEvent event) {
		int location = this.determineLocation(event);
		// String target = (String) determineTarget(event);
		if (event.item.getData() instanceof OntologyTreeNode) {
			OntologyTreeNode treeNode = (OntologyTreeNode) event.item.getData();

			String translatedLocation = "";

			Console.info("getDate: "
					+ (treeNode.getName()));

			event.item.getData();

			targetNode = (OntologyTreeNode) determineTarget(event);

			switch (location) {
			case 1:
				translatedLocation = "Dropped before the target ";
				break;
			case 2:
				translatedLocation = "Dropped after the target ";
				break;
			case 3:
				translatedLocation = "Dropped on the target ";
				break;
			case 4:
				translatedLocation = "Dropped into nothing ";
				break;
			}

			Console.info(translatedLocation);

			if (location != 4) {
				Console.info("The drop was done on the element: "
						+ targetNode.getName());

				/* copy nodes */
			}
		}
		else {
			MessageDialog.openError(Application.getShell(), "Error", "You cannot drop this item here!");
		}
		super.drop(event);
	}

	// This method performs the actual drop
	// We simply add the String we receive to the model and trigger a refresh of
	// the
	// viewer by calling its setInput method.
	@Override
	public boolean performDrop(Object data) {
		myOT = OntologyEditorView.getI2b2ImportTool().getMyOntologyTrees();

		Console.info("Dropped performed with data:");
		// Console.info(" - selectedObjectClass: " +
		// this..getSelectedObject().getClass().getSimpleName());
		Console.info(" - data: " + data.getClass().getSimpleName());
		Console.info(" - data_toString: " + (String) data.toString());

		if (this.targetNode != null) {
			Console.info(" - targetNode: " + targetNode.getName());
		} else {
			Console.info(" - targetNode is Null!");
		}

		Console.info(" - data this.myOT?: "
				+ (this.myOT == null ? "isNull" : "is NOT null"));
		Console.info(" - data this.myOT.getViewTree()?: "
				+ (this.myOT.getViewTreeSource() == null ? "isNull"
						: "is NOT null"));

		Console.info(" - data size?: "
				+ this.myOT.getViewTreeSource().stringPathToViewTreeNode.size());
		System.out
		.println(" - data size2?: "
				+ this.myOT.getViewTreeTarget().stringPathToViewTreeNode
				.size());

		Console.info("OTCOPY:");

		Console.info(" - things to copy: SourceNode (\""
				+ data.toString() + "\") or TargetNode (\""
				+ targetNode.getTreePath() + "\") ");

		myOT.dropCommandCopyNodes(data.toString(), targetNode.getTreePath());

		viewer.refresh();

		return false;
	}

	@Override
	public boolean validateDrop(Object target, int operation,
			TransferData transferType) {
		return true;

	}

}