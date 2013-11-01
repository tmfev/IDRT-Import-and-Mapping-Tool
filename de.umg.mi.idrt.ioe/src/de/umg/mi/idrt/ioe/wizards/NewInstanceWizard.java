package de.umg.mi.idrt.ioe.wizards;

import org.eclipse.jface.wizard.Wizard;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class NewInstanceWizard extends Wizard {

	protected NewInstanceWizardPage1 one;

	public NewInstanceWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		one = new NewInstanceWizardPage1();
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
		return true;
	}

}