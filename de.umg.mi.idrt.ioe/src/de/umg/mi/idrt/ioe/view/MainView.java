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
import de.umg.mi.idrt.ioe.Resource.Global;


import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;


public class MainView extends ViewPart {

	
	private Label _label;
	private Global global;
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
		Debug.f("createPartControl",this);
		
		parent.setLayout(new FillLayout());
		ScrolledComposite scrollBox = new ScrolledComposite(parent, SWT.V_SCROLL);
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

		// TODO EXTENSIONS
		//runGreeterExtension();
		//checkImportPlugins();
		//checkImportExportPlugIns();
		
		_label = new Label(mParent, SWT.LEFT);
		_label.setBackground(SWTResourceManager.getColor(255, 255, 255));
		_label.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/IDRT_ME.gif"));
		
		this.global = new Resource().new Global();
		
		this.getGlobal().setGlobalFromPropertiesFiles();
	}
	
	
	// TODO 
	/*
	private void runGreeterExtension() {
		
		Debug.d("/- run PlugInCheck --------------");
		
		final String IGREETER_ID = "edu.goettingen.i2b2.importtool.TOSImport";
		Debug.d("| *1");
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(IGREETER_ID);
		try {
			Debug.d("| *2");
			Debug.d("| *2a: " + config.toString() + "/length:" + config.length); 
			for (IConfigurationElement e : config) {
				Debug.d("| *3");
				Debug.d("| Evaluating extension");
				final Object o = e.createExecutableExtension("class");
				Debug.d("| name: " + e.getAttribute("name"));
				if (o instanceof OntologyTreeCreator) {
					Debug.d("| is instanceOf OntologyTreeCreator");
					ISafeRunnable runnable = new ISafeRunnable() {
						@Override
						public void handleException(Throwable exception) {
							Debug.d("| Exception in client");
						}

						@Override
						public void run() throws Exception {
							//was greet: ((OntologyTreeCreator) o).greet();
						}
					};
					SafeRunner.run(runnable);
				}
			}
		} catch (CoreException ex) {
			Debug.d("| *4");
			Debug.e("| " + ex.getMessage());
		}
		
		Debug.d("--------------- run PlugInCheck -/");
	}
	*/
	
	/*
	
	private void checkImportPlugins() {
		
		


		
		Debug.d("trying to find some import plugins already ...");

		
		final String IGREETER_ID = "edu.goettingen.i2b2.importtool.ExtensionPoint.ImportInterface";
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(IGREETER_ID);
		try {

			Debug.d("   ... found something ... looks like " + config.toString() + " with a length of " + config.length); 
			for (IConfigurationElement e : config) {
				Debug.d("| Evaluating extension");
				final Object o = e.createExecutableExtension("class");
				Debug.d("      ... its name is ... hmmm ... yes ... its ... " + e.getAttribute("name"));
				if (o instanceof edu.goettingen.i2b2.importtool.ExtensionPoint.definition.ImportInterface) {
					Debug.d("       ... and it seems to by some kind of ontologyTreeCreator class or something");
					ISafeRunnable runnable = new ISafeRunnable() {
						@Override
						public void handleException(Throwable exception) {
							Debug.d("            ... wait ... got an execption in the client ... over and out");
						}

						@Override
						public void run() throws Exception {
							((ImportInterface) o).test();
						}
					};
					SafeRunner.run(runnable);
				}
			}
		} catch (CoreException ex) {
			Debug.e("         ... some message it is sending ... " + ex.getMessage());
		}
		
	}
	*/
	
	/*
	private void checkImportExportPlugIns() {
		
		Debug.f("* checkImportExportPlugIns");
		
		final String IGREETER_ID = "edu.goettingen.i2b2.importtool.TOSImport";
		
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(IGREETER_ID);
		try {
			for (IConfigurationElement e : config) {
				Debug.d("checking Extension");
				final Object o = e.createExecutableExtension("class");
				Debug.d("extension name: " + e.getAttribute("name"));
				if (o instanceof OntologyTreeCreator) {
					Debug.d("is instanceOf OntologyTreeCreator");
					ISafeRunnable runnable = new ISafeRunnable() {
						@Override
						public void handleException(Throwable exception) {
							Debug.d("Exception in client");
						}

					
						@Override
						public void run() throws Exception {
							((OntologyTreeCreator) o).greet();
						}
					};
					SafeRunner.run(runnable);
				}
			}
		} catch (CoreException ex) {
			Debug.d(ex.getMessage());
		}
	}
	*/
	
	public Global getGlobal(){
		return this.global;
	}

	@Override
	public void setFocus() {
		// TODO add something

	}
	
	public void setResource(Resource resource){
		this._resource = resource;
	}
	
	public Resource getResource(){
		return this._resource;
	}
}
