package de.umg.mi.idrt.ioe.wizards;

import org.eclipse.jface.wizard.Wizard;

import de.umg.mi.idrt.ioe.OntologyTree.TargetProjects;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 * 		   Christian Bauer <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class EditInstanceWizard extends Wizard {

	protected EditInstanceWizardPage1 one;

	public EditInstanceWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		one = new EditInstanceWizardPage1();
		// two = new CSVWizardPageTwo();
		addPage(one);
		// addPage(two);
	}

	@Override
	public boolean canFinish() {
		return one.isComplete();
	}
	
	@Override
	public boolean performFinish() {
		
		System.out.println("Creating New Instance:");
		System.out.println("Name: " + one.getNameText());
		System.out.println("Descr.: " + one.getDescriptionText());
		System.out.println("Created: " + one.getCreated());
		
		//TODO DB Access
		OntologyEditorView.setTargetInstance(one.getNameText(),one.getDescriptionText());
		
		//TargetProjects targetProjects = OntologyEditorView.getTargetProjects().getSelectedTargetProject();
		
		return true;
	}

}