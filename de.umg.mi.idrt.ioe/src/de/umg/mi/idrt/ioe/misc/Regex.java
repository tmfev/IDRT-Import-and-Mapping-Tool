package de.umg.mi.idrt.ioe.misc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.com.bytecode.opencsv.CSVReader;
import de.umg.mi.idrt.ioe.commands.OntologyEditor.CombineNodesCommand;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 * Department of Medical Informatics Goettingen
 * www.mi.med.uni-goettingen.de
 */
public class Regex {

	private String name;
	private String regex;
	private String test;
	private String table;
	
	/**
	 * @param table the table to set
	 */
	public void setTable(String table) {
		this.table = table.toLowerCase();
	}

	/**
	 * 
	 */
	public Regex(String name, String regex, String table) {
		setName(name);
		setRegex(regex);
		setTest("");
		setTable(table);
	}
	
	/**
	 * @param test the test to set
	 */
	public void setTest(String test) {
		this.test = test;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	/**
	 * @return
	 */
	public String getTest() {
		return test;
	}

	/**
	 * @return
	 */
	public boolean check() {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(test);
		return m.matches();
	}

	/**
	 * @return
	 */
	public String getTable() {
		return table.toLowerCase();
	}

	/**
	 * 
	 */
	public static void loadRegex() {
		File file = new File(FileHandler.getCFGFilePath("regex.csv"));

		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(file), ';');
		

		String[] line = reader.readNext();

		while ((line = reader.readNext()) != null) {
			Regex regex;
			if (line.length <= 2)
				regex = new Regex(line[0], line[1], "c_basecode");
			else
				regex = new Regex(line[0], line[1], line[2]);	
			CombineNodesCommand.addRegEx(regex);
		}

		reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
