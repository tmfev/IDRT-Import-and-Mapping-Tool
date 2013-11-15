package de.umg.mi.idrt.ioe.misc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 * Department of Medical Informatics Goettingen
 * www.mi.med.uni-goettingen.de
 */
public class Regex {

	private String name;
	private String regex;
	private String test;
	
	/**
	 * 
	 */
	public Regex(String name, String regex) {
		setName(name);
		setRegex(regex);
		setTest("");
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
	
}
