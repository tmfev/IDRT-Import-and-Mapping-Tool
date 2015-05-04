package de.umg.mi.idrt.ioe.wizards;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.eclipse.jface.wizard.Wizard;

import au.com.bytecode.opencsv.CSVWriter;

import de.umg.mi.idrt.ioe.commands.OntologyEditor.CombineNodesCommand;
import de.umg.mi.idrt.ioe.misc.FileHandler;
import de.umg.mi.idrt.ioe.misc.Regex;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class RegexWizard extends Wizard {

	public RegexWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	protected RegexWizardPage1 one;

	@Override
	public void addPages() {
		one = new RegexWizardPage1();
		addPage(one);
	}

	@Override
	public boolean canFinish() {
		return true;
	}

	@Override
	public boolean performFinish() {

		try {
			CSVWriter writer = new CSVWriter(new FileWriter(new File(FileHandler.getCFGFilePath("regex.csv"))), ';');
			writer.writeNext(new String[] {"Name","Regex"});
			for (Regex r : CombineNodesCommand.getRegex()) {
				if (!r.getName().isEmpty())
					writer.writeNext(new String[] {r.getName(),r.getRegex().replaceAll("\\\\", "\\\\\\\\"), r.getTable()});
			}
			writer.flush();
			writer.close();
//			MyOntologyTrees.createRegexMenu();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}