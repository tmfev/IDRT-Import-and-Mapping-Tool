package de.umg.mi.idrt.ioe.wizards;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

import org.eclipse.jface.wizard.Wizard;

import au.com.bytecode.opencsv.CSVWriter;

import de.umg.mi.idrt.ioe.OntologyTree.FileHandling;
import de.umg.mi.idrt.ioe.commands.OntologyEditor.CombineNodesCommand;
import de.umg.mi.idrt.ioe.misc.Regex;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class RegexWizard extends Wizard {

	protected RegexWizardPage1 one;

	public RegexWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

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
			CSVWriter writer = new CSVWriter(new FileWriter(new File(FileHandling.getCFGFilePath("regex.csv"))), ';');
			writer.writeNext(new String[] {"Name","Regex"});
			for (Regex r : CombineNodesCommand.getRegex()) {
				if (!r.getName().isEmpty())
					writer.writeNext(new String[] {r.getName(),r.getRegex()});
				Pattern p = Pattern.compile(r.getRegex());
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}