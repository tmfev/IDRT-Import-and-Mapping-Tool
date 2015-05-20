package de.umg.mi.idrt.ioe.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.SWT;
import java.awt.Frame;
import org.eclipse.swt.awt.SWT_AWT;

import java.awt.GridBagLayout;
import java.awt.Panel;
import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JRootPane;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import org.eclipse.swt.layout.FillLayout;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;

public class InfoView extends ViewPart {
	public InfoView() {
	}

	@Override
	public void createPartControl(Composite parent) {

		parent.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				// TODO Auto-generated method stub
				System.out.println("PRTAONT");
			}
		});
		Composite composite = new Composite(parent, SWT.EMBEDDED);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				// TODO Auto-generated method stub
			System.out.println("poant");	
			}
		});
		Composite composite2 = new Composite(composite, SWT.EMBEDDED);
		Frame frame = SWT_AWT.new_Frame(composite);
		composite2.setLayout(new FillLayout(SWT.HORIZONTAL));

		SashForm sashForm = new SashForm(composite2, SWT.NONE);

		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));



		Tree tree = new Tree(composite_1, SWT.BORDER);

		TreeItem trtmNewTreeitem = new TreeItem(tree, SWT.NONE);
		trtmNewTreeitem.setText("New TreeItem");

		Composite composite_2 = new Composite(sashForm, SWT.NONE);
		composite_2.setLayout(new FillLayout(SWT.HORIZONTAL));

		Tree tree_1 = new Tree(composite_2, SWT.BORDER);

		TreeItem trtmNewTreeitem_1 = new TreeItem(tree_1, SWT.NONE);
		trtmNewTreeitem_1.setText("New TreeItem");
		sashForm.setWeights(new int[] {1, 1});


		Panel panel = new Panel();
		frame.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		JRootPane rootPane = new JRootPane();
		panel.add(rootPane);
		final JPanel glass = (JPanel) rootPane.getGlassPane();

		glass.setVisible(true);
		glass.setLayout(new GridBagLayout());
		JButton glassButton = new JButton("Hide");
		glass.add(glassButton);
	}


	@Override
	public void setFocus() {
	}
}
