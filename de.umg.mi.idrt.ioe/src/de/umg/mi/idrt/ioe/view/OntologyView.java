package de.umg.mi.idrt.ioe.view;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import de.umg.mi.idrt.ioe.Debug;
import de.umg.mi.idrt.ioe.I2B2ImportTool;

public class OntologyView extends ViewPart {

	private I2B2ImportTool _i2b2ImportTool;
	private Composite _composite;
	
	public OntologyView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		Debug.f("createPartControl",this);
		this._composite = parent;
		Text text = new Text(this._composite, SWT.BORDER);
		text.setText("OntologyView");

	}

	@Override
	public void setFocus() {
		
	}
	
	/*
	public I2B2ImportTool initI2B2ImportTool(){
		return _i2b2ImportTool = new I2B2ImportTool(null);
	}
	*/
	
	public I2B2ImportTool setI2B2ImportTool(I2B2ImportTool i2b2ImportTool){
		return _i2b2ImportTool = i2b2ImportTool;
	}
	
	public I2B2ImportTool getI2B2ImportTool(){
		return _i2b2ImportTool;
	}
	
	public void setComposite(){
		Debug.f("setComposite",this);
		
		Control[] children = this._composite.getChildren();
		
		for(int x = 0; x < children.length; x++){
			children[0].dispose();
		}
		
		/*
		Text text = new Text(this._composite, SWT.BORDER);
		text.setText("XX OntologyView (loaded)");
		
		text.update();
		text.redraw();
		*/
		
		Composite swtAwtComponent = new Composite(this._composite, SWT.EMBEDDED);
		java.awt.Frame frame = SWT_AWT.new_Frame( swtAwtComponent );
		//javax.swing.JPanel panel = new javax.swing.JPanel( );
		
		
		//this._i2b2ImportTool.getMyOntologyTree().getGUI().createGUI(this._i2b2ImportTool.getMyOntologyTree().getOntologyTree().getTreeRoot(), 10);
		/*
		Debug.dn("this._i2b2ImportTool", this._i2b2ImportTool);
		//Debug.dn("this._i2b2ImportTool.getOntologyTab()", this._i2b2ImportTool.getOntologyTab());
		
		Debug.dn("this._i2b2ImportTool.getMyOntologyTree()", this._i2b2ImportTool.getMyOntologyTree());
		Debug.dn("this._i2b2ImportTool.getMyOntologyTree().getGUI()", this._i2b2ImportTool.getMyOntologyTree().getGUI());
		Debug.dn("this._i2b2ImportTool.getMyOntologyTree().getGUI().getOntologyPanel()", this._i2b2ImportTool.getMyOntologyTree().getGUI().getOntologyPanel());
		*/
		
		//JScrollPane ontologyPanel = this._i2b2ImportTool.getMyOntologyTree().getGUI().getOntologyPanel();
		
		JPanel dumpPanel = new JPanel ();
		dumpPanel.setLayout(new FlowLayout());
		
		//frame.add(dumpPanel);
		//dumpPanel.add(this._i2b2ImportTool.getMyOntologyTree().getGUI().getOntologyPanel());
		
		//frame.add(ontologyPanel);

		//this._i2b2ImportTool.getFrame();
		this._composite.layout();
	}
	
	@Override
	public void dispose(){
		super.dispose();
		
		
		/*
		if (_i2b2ImportTool != null && _i2b2ImportTool.getMyOntologyTree() != null){
			_i2b2ImportTool.getMyOntologyTree().initiate();
		}
		*/
		
		//IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();		
		//IWorkbenchPage page = IWorkbench.getInstance().getActiveWorkbenchWindow().getActivePage();

		//TODO check perspective
		/*
		
		Perspective perspective = (Perspective) page.getPerspective();
		//get the reference for your viewId
		IViewReference ref = page.findViewReference(Resource.ID.View.ONTOLOGY_NODEEDITOR_VIEW);
		//release the view
		perspective.getViewFactory().releaseView(ref);
		
		ref = page.findViewReference(Resource.ID.View.ONTOLOGY_ANSWERSPREVIEW_VIEW);
		//release the view
		perspective.getViewFactory().releaseView(ref);
		*/

		
		
	}

}
