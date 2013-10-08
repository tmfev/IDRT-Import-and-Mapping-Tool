package de.umg.mi.irdrt.ioe.wizards;

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

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		return NewProjectWizardPage1.isComplete();
	}
	
	@Override
	public boolean performFinish() {
		return true;
	}

}