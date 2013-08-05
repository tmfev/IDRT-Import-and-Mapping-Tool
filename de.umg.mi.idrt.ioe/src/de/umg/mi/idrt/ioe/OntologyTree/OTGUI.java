package de.umg.mi.idrt.ioe.OntologyTree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.datatype.XMLGregorianCalendar;

import de.umg.mi.idrt.ioe.Debug;
import de.umg.mi.idrt.ioe.GUITools;
import de.umg.mi.idrt.ioe.Global;
import de.umg.mi.idrt.ioe.UserMessage;
import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OTItemLists;



public class OTGUI {

	private JFrame frame = new JFrame();
	private JLayeredPane layeredPane = new JLayeredPane();
	private JTabbedPane tabbedPane = new JTabbedPane();

	private JPanel propertiesPane = new JPanel();
	private JPanel editorWindowEditor = new JPanel();
	private JPanel leftWindowNextButtons = new JPanel();
	private JPanel nextButtonsPanel = new JPanel();
	private JPanel exportButtonsPanel = new JPanel();
	private int selectedTab = 0;
	private int previousSelectedTab = 0;
	private JPanel returnPanel;
	private Dimension maxDimension = new Dimension();
	private static boolean playWithLineStyle = false;
	private static String lineStyle = "Horizontal";
	private static boolean useSystemLookAndFeel = true;
	private OntologyTree OT = null;
	private JTextField indexLow = null;
	private JTextField indexHigh = null;
	private JTextField indexSteps = null;
	private JComboBox indexStepsJBox = null;
	private JTextField editItemRename = null;
	private Map<String,Object> editorFields = new HashMap<String,Object>();
	private OTItemLists itemLists = null;
	private UserMessage userMessage = new UserMessage();
	private Map<String, JCheckBox> castCheckboxList = new HashMap<String, JCheckBox>();
	private boolean isFirstCall = true;
	private MyOntologyTree MyOT = null;
	private JScrollPane _topWindow;
    

	public OTGUI(MyOntologyTree MyOT) {
		this.MyOT = MyOT;
	}
	
}
