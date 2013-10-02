package de.umg.mi.idrt.ioe.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import de.umg.mi.idrt.ioe.Activator;
import de.umg.mi.idrt.ioe.Debug;
import de.umg.mi.idrt.ioe.Resource;

import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

public class MainView extends ViewPart {

	private Label _label;
	private Resource _resource;

	public MainView() {
		super();
		Debug.c("MainView");
		Activator.getDefault().getResource().setMainView(this);
		_resource = new Resource();
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		Debug.f("createPartControl", this);

		parent.setLayout(new FillLayout());
		ScrolledComposite scrollBox = new ScrolledComposite(parent,
				SWT.V_SCROLL);
		scrollBox.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		scrollBox.setExpandVertical(true);
		scrollBox.setExpandHorizontal(true);
		Composite mParent = new Composite(scrollBox, SWT.NONE);
		mParent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		scrollBox.setContent(mParent);
		// Adds a bunch of controls here
		mParent.layout();
		mParent.setSize(mParent.computeSize(SWT.DEFAULT, SWT.DEFAULT, true));
		mParent.setLayout(new FillLayout(SWT.HORIZONTAL));

		_label = new Label(mParent, SWT.LEFT);
		_label.setBackground(SWTResourceManager.getColor(255, 255, 255));
		_label.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe",
				"images/IDRT_ME.gif"));

	}

	@Override
	public void setFocus() {
	}

	public void setResource(Resource resource) {
		this._resource = resource;
	}

	public Resource getResource() {
		return this._resource;
	}
	

}
