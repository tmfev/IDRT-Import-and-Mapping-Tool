package de.umg.mi.idrt.idrtimporttool.ImportWizard;

import java.util.LinkedList;
import java.util.List;
import org.eclipse.swt.widgets.TableItem;

/**
 * Contains all possible metadata for CSV/DB imports.
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 * Department of Medical Informatics Goettingen
 * www.mi.med.uni-goettingen.de
 */
public class ConfigMetaData {

	public static String[] optionsData = { "String", "Date", "Float", "Integer" };
	
	/**
	 * 
	 * @param items TableItems
	 * @param currentOption The current selected option.
	 * @return The current Combo for the PIDGen metadata column.
	 */
	public static String[] getMetaComboPIDGen(
			TableItem[] items, String currentOption) {
		boolean firstName = false;
		boolean name = false;
		boolean reportDate = false;
		boolean birthday = false;
		boolean sex = false;
		
		for (TableItem item : items) {
			if (item.getText(4).equalsIgnoreCase("FirstName")) {
				firstName = true;
			}
			if (item.getText(4).equalsIgnoreCase("LastName")) {
				name = true;
			}
			if (item.getText(4).equalsIgnoreCase("Reporting Date")) {
				reportDate = true;
			}
			if (item.getText(4).equalsIgnoreCase("Birthday")) {
				birthday = true;
			}
			if (item.getText(4).equalsIgnoreCase("Sex")) {
				sex = true;
			}
		}
		List<String> metaOptionList = new LinkedList<String>();

		if (currentOption.isEmpty()) {
			metaOptionList.add("");
		} else {
			metaOptionList.add("");
			metaOptionList.add(currentOption);
		}

		if (!firstName) {
			metaOptionList.add("FirstName");
		}
		if (!name) {
			metaOptionList.add("LastName");
		}
		if (!reportDate) {
			metaOptionList.add("Reporting Date");
		}
		if (!birthday) {
			metaOptionList.add("Birthday");
		}
		if (!sex) {
			metaOptionList.add("Sex");
		}
	
		String[] metaString = new String[metaOptionList.size()];
		for (int i = 0; i < metaOptionList.size(); i++) {
			metaString[i] = metaOptionList.get(i);
		}

		return metaString;
	}
	
	/**
	 * Fills the Combo of the table
	 * 
	 * @param optionsMeta Old options.
	 * @param items Options already assigned.
	 * @param currentOption Current option.
	 * @return New combo list.
	 */
	public static String[] getMetaCombo(TableItem[] items,
			String currentOption) {
		
		boolean patientID = false;
		boolean encounterID = false;
		boolean updateDate = false;
		boolean importDate = false;
		boolean downloadDate = false;
		boolean startDate = false;
		boolean endDate = false;

		boolean imageSic = false;
		boolean biomaterialSic = false;
		boolean otherSic = false;
		
		for (TableItem item : items) {
			if (item.getText(3).equalsIgnoreCase("PatientID")) {
				patientID = true;
			}
			if (item.getText(3).equalsIgnoreCase("EncounterID")) {
				encounterID = true;
			}
			if (item.getText(3).equalsIgnoreCase("UpdateDate")) {
				updateDate = true;
			}
			if (item.getText(3).equalsIgnoreCase("ImportDate")) {
				importDate = true;
			}
			if (item.getText(3).equalsIgnoreCase("DownloadDate")) {
				downloadDate = true;
			}
			if (item.getText(3).equalsIgnoreCase("StartDate")) {
				startDate = true;
			}
			if (item.getText(3).equalsIgnoreCase("EndDate")) {
				endDate = true;
			}
			if (item.getText(4).equalsIgnoreCase("imagesic")) {
				imageSic = true;
			}
			if (item.getText(4).equalsIgnoreCase("BiomaterialSic")) {
				biomaterialSic = true;
			}
			if (item.getText(4).equalsIgnoreCase("OtherSic")) {
				otherSic = true;
			}
		}
		List<String> metaOptionList = new LinkedList<String>();

		if (currentOption.isEmpty()) {
			metaOptionList.add("");
		} else {
			metaOptionList.add("");
			metaOptionList.add(currentOption);
		}
		metaOptionList.add("ignore");

		if (!patientID) {
			metaOptionList.add("PatientID");
		}
		if (!encounterID) {
			metaOptionList.add("EncounterID");
		}
		if (!updateDate) {
			metaOptionList.add("UpdateDate");
		}
		if (!importDate) {
			metaOptionList.add("ImportDate");
		}
		if (!downloadDate) {
			metaOptionList.add("DownloadDate");
		}
		if (!startDate) {
			metaOptionList.add("StartDate");
		}
		if (!endDate) {
			metaOptionList.add("EndDate");
		}
		if (!imageSic) {
			metaOptionList.add("ImageSic");
		}
		if (!biomaterialSic) {
			metaOptionList.add("BiomaterialSic");
		}
		if (!otherSic) {
			metaOptionList.add("OtherSic");
		}

		String[] metaString = new String[metaOptionList.size()];
		for (int i = 0; i < metaOptionList.size(); i++) {
			metaString[i] = metaOptionList.get(i);
		}

		return metaString;
	}
}
