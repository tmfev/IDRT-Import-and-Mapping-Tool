package de.umg.mi.idrt.idrtimporttool.ImportWizard;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.TableItem;

import de.umg.mi.idrt.idrtimporttool.messages.Messages;

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
			if (item.getText(4).equalsIgnoreCase("FirstName")) { //$NON-NLS-1$
				firstName = true;
			}
			if (item.getText(4).equalsIgnoreCase("LastName")) { //$NON-NLS-1$
				name = true;
			}
			if (item.getText(4).equalsIgnoreCase("Reporting Date")) { //$NON-NLS-1$
				reportDate = true;
			}
			if (item.getText(4).equalsIgnoreCase("Birthday")) { //$NON-NLS-1$
				birthday = true;
			}
			if (item.getText(4).equalsIgnoreCase("Sex")) { //$NON-NLS-1$
				sex = true;
			}
		}
		List<String> metaOptionList = new LinkedList<String>();

		if (currentOption.isEmpty()) {
			metaOptionList.add(""); //$NON-NLS-1$
		} else {
			metaOptionList.add(""); //$NON-NLS-1$
			metaOptionList.add(currentOption);
		}

		if (!firstName) {
			metaOptionList.add("FirstName"); //$NON-NLS-1$
		}
		if (!name) {
			metaOptionList.add("LastName"); //$NON-NLS-1$
		}
		if (!reportDate) {
			metaOptionList.add("Reporting Date"); //$NON-NLS-1$
		}
		if (!birthday) {
			metaOptionList.add("Birthday"); //$NON-NLS-1$
		}
		if (!sex) {
			metaOptionList.add("Sex"); //$NON-NLS-1$
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

		boolean objectID = false;
		
		for (TableItem item : items) {
			if (item.getText(3).equalsIgnoreCase(Messages.ConfigMetaData_PatientID)) { 
				patientID = true;
			}
			if (item.getText(3).equalsIgnoreCase(Messages.ConfigMetaData_EncounterID)) { 
				encounterID = true;
			}
			if (item.getText(3).equalsIgnoreCase(Messages.ConfigMetaData_UpdateDate)) {
				updateDate = true;
			}
			if (item.getText(3).equalsIgnoreCase(Messages.ConfigMetaData_ImportDate)) { 
				importDate = true;
			}
			if (item.getText(3).equalsIgnoreCase(Messages.ConfigMetaData_DownloadDate)) { 
				downloadDate = true;
			}
			if (item.getText(3).equalsIgnoreCase(Messages.ConfigMetaData_StartDate)) { 
				startDate = true;
			}
			if (item.getText(3).equalsIgnoreCase(Messages.ConfigMetaData_EndDate)) {
				endDate = true;
			}
			if (item.getText(3).equalsIgnoreCase(Messages.ConfigMetaData_ObjectID)) {
				objectID = true;
			}
		}
		List<String> metaOptionList = new LinkedList<String>();

		if (currentOption.isEmpty()) {
			metaOptionList.add("");
		} else {
			metaOptionList.add("");
			metaOptionList.add(currentOption);
		}
		

		if (!patientID) {
			metaOptionList.add(Messages.ConfigMetaData_PatientID);
		}
		if (!encounterID) {
			metaOptionList.add(Messages.ConfigMetaData_EncounterID);
		}
		if (!updateDate) {
			metaOptionList.add(Messages.ConfigMetaData_UpdateDate);
		}
		if (!importDate) {
			metaOptionList.add(Messages.ConfigMetaData_ImportDate);
		}
		if (!downloadDate) {
			metaOptionList.add(Messages.ConfigMetaData_DownloadDate);
		}
		if (!startDate) {
			metaOptionList.add(Messages.ConfigMetaData_StartDate);
		}
		if (!endDate) {
			metaOptionList.add(Messages.ConfigMetaData_EndDate);
		}
		if (!objectID) {
			metaOptionList.add(Messages.ConfigMetaData_ObjectID);
		}
		
		metaOptionList.add("ignore");
		
		String[] metaString = new String[metaOptionList.size()];
		for (int i = 0; i < metaOptionList.size(); i++) {
			metaString[i] = metaOptionList.get(i);
		}

		return metaString;
	}
}
