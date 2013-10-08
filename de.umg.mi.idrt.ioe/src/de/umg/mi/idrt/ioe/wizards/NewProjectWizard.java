package de.umg.mi.idrt.ioe.wizards;

import org.eclipse.jface.wizard.Wizard;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class NewProjectWizard extends Wizard {

	protected NewProjectWizardPage1 one;

	public NewProjectWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		one = new NewProjectWizardPage1();
		// two = new CSVWizardPageTwo();
		addPage(one);
		// addPage(two);
	}

	@Override
	public boolean canFinish() {
		return NewProjectWizardPage1.isComplete();
	}
	
	@Override
	public boolean performFinish() {
		
		System.out.println("Creating New Project:");
		System.out.println("Name: " + NewProjectWizardPage1.getNameText());
		System.out.println("Descr.: " + NewProjectWizardPage1.getDescriptionText());
		System.out.println("Created: " + NewProjectWizardPage1.getCreated());
		
		//TODO DB Access
		return true;
	}

}