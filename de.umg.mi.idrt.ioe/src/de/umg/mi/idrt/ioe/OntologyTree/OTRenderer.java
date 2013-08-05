package de.umg.mi.idrt.ioe.OntologyTree;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.GUITools;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;

/**
 * A cutom JTree renderer for an ontology tree.
 * 
 * @author Christian Bauer
 * @version 0.9
 */
public class OTRenderer extends DefaultTreeCellRenderer {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 444085257679955370L;



	/**
	 * Constructor of the custume jTree renderer for ontology trees
	 * 
	 * @param myOT
	 *            the ontology tree to render
	 */
	public OTRenderer(MyOntologyTree myOT) {
		

	}

	/**
	 * Creates node specific rendering label for the ontology tree.
	 * 
	 * @param jTree
	 *            the ontology tree to render
	 * @param value
	 *            an OTNode
	 * @param sel
	 *            value to get the label to edit
	 * @param expanded
	 *            value to get the label to edit
	 * @param leaf
	 *            value to get the label to edit
	 * @param row
	 *            value to get the label to edit
	 * @param hasFocus
	 *            value to get the label to edit
	 */
	public Component getTreeCellRendererComponent(JTree jTree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		// extract the current JLabel for editing purpose
		final JLabel label = (JLabel) super.getTreeCellRendererComponent(jTree,
				value, sel, expanded, leaf, row, hasFocus);

		// System.out.println("getTreeCellRendererComponent!!!1");

		// convert the generic JTree value to a generic OTNode
		OntologyTreeNode node = null;
		String i2b2Icon = "";

		try {
			if (value instanceof OntologyTreeNode)
				node = (OntologyTreeNode) value;
		} catch (Exception e) {
			e.printStackTrace();
			return this;
		}

		if (node == null) {
			// something has been gone terribly wrong, so return
			return this;
		} else if (node.isRoot()) {

			return this;
		} else if (node.getOntologyCellAttributes() == null) {
			Console.error("Error while rendering a tree: Node did not have ontology cell attributes. Not even an Object of that type. ("
					+ node.getTreePath() + ")");
			return this;
		} else if ( 
				(node.getOntologyCellAttributes().getC_VISUALATTRIBUTES() == null || node
						.getOntologyCellAttributes().getC_VISUALATTRIBUTES()
						.isEmpty())) {

			Console.error("Error while rendering a tree: Node did not have ontology cell visual attributes. ("
					+ node.getTreePath()
					+ " # "
					+ node.getName()
					+ " # "
					+ node.getOntologyCellAttributes().getC_FULLNAME() + ")");

			return this;
		}

		String newLabelText = label.getText();

		String vis = node.getOntologyCellAttributes().getC_VISUALATTRIBUTES();

		String subString = "";

		if (!vis.isEmpty()) {

			if (vis.startsWith(Resource.I2B2.VisualAttributes.FOLDER)) {
				i2b2Icon = Resource.OntologyTree.VISIBILITY_ICON_FA;
			} else if (vis.startsWith(Resource.I2B2.VisualAttributes.CONTAINER)) {
				i2b2Icon = Resource.OntologyTree.VISIBILITY_ICON_CA;
			} else if (vis.startsWith(Resource.I2B2.VisualAttributes.MULTIPLE)) {
				i2b2Icon = Resource.OntologyTree.VISIBILITY_ICON_MA;
			} else if (vis.startsWith(Resource.I2B2.VisualAttributes.LEAF)) {
				i2b2Icon = Resource.OntologyTree.VISIBILITY_ICON_LA;
			}

			if (vis.length() > 1) {

				subString = vis.substring(1, 2);

				// System.out.println("length > 1 and vis.substring(1,2): " +
				// subString);

				if (subString.equals(Resource.I2B2.VisualAttributes.ACTIVE)) {
					// node is active, so don't add any image
					// label.setText(addItemImage(label.getText(),
					// Resource.OntologyTree.ITEMSTATUS_ICON_CHECKED_BY_AUTOMATION_DEFINITELY));
				} else if (subString
						.equals(Resource.I2B2.VisualAttributes.INACTIVE)) {
					// label.setText(addItemImage(label.getText(),
					// Resource.OntologyTree.ITEMSTATUS_ICON_CHECKED_BY_AUTOMATION_SURE));
					label.setText(label.getText() + "(inactive)");
				} else if (subString
						.equals(Resource.I2B2.VisualAttributes.HIDDEN)) {
					// label.setText(addItemImage(label.getText(),
					// Resource.OntologyTree.ITEMSTATUS_ICON_CHECKED_BY_AUTOMATION_UNSURE));
					label.setText(label.getText() + "(hidden)");
				}
			} else {
				// System.out.println("NOT length > 1: " + vis);
			}

			if (vis.length() > 2) {
				if (vis.substring(2, 3).equals(
						Resource.I2B2.VisualAttributes.EDITABLE)) {
					// label.setText(label.getText() + "(E)");
				}
			}

			/*
			 * if ( "FA".equals(visualAttribute) ) { i2b2Icon =
			 * Resource.OntologyTree.VISIBILITY_ICON_FA; } else if (
			 * "FI".equals(visualAttribute) ) { i2b2Icon =
			 * Resource.OntologyTree.VISIBILITY_ICON_FI; } else if (
			 * "CA".equals(visualAttribute) ) { i2b2Icon =
			 * Resource.OntologyTree.VISIBILITY_ICON_CA; } else if (
			 * "CI".equals(visualAttribute) ) { i2b2Icon =
			 * Resource.OntologyTree.VISIBILITY_ICON_CI; } else if (
			 * "MA".equals(visualAttribute) ) { i2b2Icon =
			 * Resource.OntologyTree.VISIBILITY_ICON_MA; } else if (
			 * "MI".equals(visualAttribute) ) { i2b2Icon =
			 * Resource.OntologyTree.VISIBILITY_ICON_MI; }
			 */
		}

		if (!i2b2Icon.isEmpty()) {
			label.setIcon(GUITools.createImageIcon(i2b2Icon));

		}

		if (node.getNodeType() != null) {
			// if its an identifiable node, edit the label accordingly

			if (node.getNodeType().equals(NodeType.STUDY)) {
				newLabelText = this.addHtmlTag(newLabelText, "b");
				// jTree.expandPath(jTree.getPathForRow(row));
			} else if (node.getNodeType().equals(NodeType.METAVERS)) {
				newLabelText = this.addHtmlTag(newLabelText, "i");
				// jTree.expandPath(jTree.getPathForRow(row));
			} else if (node.getNodeType().equals(NodeType.STUDYEVENT)) {
				// nothing
				// jTree.expandPath(jTree.getPathForRow(row));
			} else if (node.getNodeType().equals(NodeType.FORM)) {
				// nothing
				// jTree.expandPath(jTree.getPathForRow(row));
			} else if (node.getNodeType().equals(NodeType.ITEMGROUP)) {
				// nothing
				// jTree.expandPath(jTree.getPathForRow(row));
			} else if (node.getNodeType().equals(NodeType.ITEM)) {

				// get the itemNode for its unique properties
				/*
				 * OTItemNode itemNode = myOT.getItemLists().getItemNodeByPath(
				 * ((OTNode)value).getTreePath() );
				 * 
				 * // return early if something has gone wrong if ( itemNode ==
				 * null ) { return label; }
				 * 
				 * ItemStatus itemStatus =
				 * myOT.getOT().getItemLists().getItemStatusByPath
				 * (node.getTreePath());
				 * 
				 * // the item image //newLabelText =
				 * addItemImage(newLabelText,Resource.OT.ICON_ITEM);
				 * 
				 * if ( itemStatus == null ) { // do no nothing } else if
				 * (itemStatus.equals(ItemStatus.UNCHECKED)){ newLabelText =
				 * this.addHtmlTag( newLabelText , "font", "color='FF0000'"); }
				 * else if
				 * (itemStatus.equals(ItemStatus.CHECKED_BY_AUTOMATION_UNSURE)){
				 * newLabelText = this.addHtmlTag( newLabelText , "font",
				 * "color='FF6633'"); } else if
				 * (itemStatus.equals(ItemStatus.CHECKED_BY_AUTOMATION_SURE)){
				 * newLabelText = this.addHtmlTag( newLabelText , "font",
				 * "color='333333'"); }
				 */

				// add the item status image
				// newLabelText = addItemStatusImage(newLabelText, itemStatus);

				// make bold if unchecked
				/*
				 * if ( !itemNode.isChecked() ) { newLabelText =
				 * this.addHtmlTag( newLabelText , "b"); }
				 */

				// label.setIcon( GUITools.createImage(Resource.OT.ICON_ITEM));

				/*
				 * Image image = AbstractUIPlugin.imageDescriptorFromPlugin(
				 * "edu.goettingen.i2b2.importtool", "/images/answer.gif")
				 * .createImage();
				 * 
				 * ImageDescriptor test =
				 * AbstractUIPlugin.imageDescriptorFromPlugin(
				 * "edu.goettingen.i2b2.importtool", "/images/answer.gif");
				 */

				// label.setIcon(new ImageIcon("er"));

			} else if (node.getNodeType().equals(NodeType.ANSWER)) {

				/*
				 * 
				 * // set an icon according to the type of answer node if (
				 * ((OTNode)node).isAnswerGroup() ) { label.setIcon(
				 * GUITools.createImageIcon
				 * (Resource.OntologyTree.ICON_ANSWERGROUP)); } else {
				 * label.setIcon(
				 * GUITools.createImageIcon(Resource.OntologyTree.
				 * ICON_ANSWERGROUP)); }
				 */

			} else {
				// do nothing
			}

			// add invisble icon to all nodes
			if (node.isVisable() == false) {
				newLabelText = addHtmlTag(newLabelText, "font",
						"color=\"#B8B8B8\"");
			}

			// mark additionalData nodes
			/*
			 * if ( node.isAdditionalData() ) { newLabelText += " *"; }
			 */

			label.setText(addHtmlTag(newLabelText, "html"));

			String visualAttribute = node.getVisualattribute();

			if (node.getNodeType().equals(NodeType.ITEM)) {
				i2b2Icon = Resource.OntologyTree.ICON_ITEM;
			} else {
				if ("FA".equals(visualAttribute)) {
					i2b2Icon = Resource.OntologyTree.VISIBILITY_ICON_FA;
				} else if ("FI".equals(visualAttribute)) {
					i2b2Icon = Resource.OntologyTree.VISIBILITY_ICON_FI;
				} else if ("CA".equals(visualAttribute)) {
					i2b2Icon = Resource.OntologyTree.VISIBILITY_ICON_CA;
				} else if ("CI".equals(visualAttribute)) {
					i2b2Icon = Resource.OntologyTree.VISIBILITY_ICON_CI;
				} else if ("MA".equals(visualAttribute)) {
					i2b2Icon = Resource.OntologyTree.VISIBILITY_ICON_MA;
				} else if ("MI".equals(visualAttribute)) {
					i2b2Icon = Resource.OntologyTree.VISIBILITY_ICON_MI;
				}
			}
			/*
			 * } else if ( "LA".equals(visualAttribute) ){ i2b2Icon =
			 * "i2b2_la.jpg"; } else if ( "LI".equals(visualAttribute) ){
			 * i2b2Icon = "i2b2_li.gif"; }
			 */

		}
		return label;
	}

	/**
	 * Generic function to enclose some text with an html-tag and attributes.
	 * 
	 * @param text
	 *            the text to enclose
	 * @param tag
	 *            the html-tag
	 * @param attributes
	 *            the attributes as an string (i.g. 'color=\"#B8B8B8\"')
	 * @return the full new html string
	 */
	private String addHtmlTag(String text, String tag, String attributes) {
		return "<" + tag + (attributes.isEmpty() ? "" : " " + attributes) + ">"
				+ text + "</" + tag + ">";
	}

	/**
	 * Generic function to enclose some text with an html-tag.
	 * 
	 * @param text
	 *            the text to enclose
	 * @param tag
	 *            the html-tag
	 * @return the full new html string
	 */
	private String addHtmlTag(String text, String tag) {
		return addHtmlTag(text, tag, "");
	}

	private String addItemStatusImage(String text, ItemStatus itemStatus) {

		String itemStatusImage = "";

		if (ItemStatus.UNCHECKED.equals(itemStatus)) {
			itemStatusImage = Resource.OntologyTree.ITEMSTATUS_ICON_UNCHECKED;
		} else if (ItemStatus.CHECKED_BY_AUTOMATION_UNSURE.equals(itemStatus)) {
			itemStatusImage = Resource.OntologyTree.ITEMSTATUS_ICON_CHECKED_BY_AUTOMATION_UNSURE;
		} else if (ItemStatus.CHECKED_BY_AUTOMATION_SURE.equals(itemStatus)) {
			itemStatusImage = Resource.OntologyTree.ITEMSTATUS_ICON_CHECKED_BY_AUTOMATION_SURE;
		} else if (ItemStatus.CHECKED_BY_AUTOMATION_DEFINITELY
				.equals(itemStatus)) {
			itemStatusImage = Resource.OntologyTree.ITEMSTATUS_ICON_CHECKED_BY_AUTOMATION_DEFINITELY;
		} else if (ItemStatus.CHECKED_BY_USER.equals(itemStatus)) {
			itemStatusImage = Resource.OntologyTree.ITEMSTATUS_ICON_CHECKED_BY_USER;
		} else {
			itemStatusImage = Resource.OntologyTree.ITEMSTATUS_ICON_UNKNOWN;
		}

		java.net.URL imgURL = MyOntologyTree.class.getResource(itemStatusImage);

		// GUITools.createImageIcon( i2b2Icon );

		return text + "<img src=\"" + imgURL
				+ "\" alt=\"test\" align=\"baseline\">";
	}

	private String addItemImage(String text, String image) {

		java.net.URL imgURL = MyOntologyTree.class.getResource(image);

		return "<img src=\"" + imgURL + "\" alt=\"test\" align=\"baseline\">"
				+ text;
	}
}
