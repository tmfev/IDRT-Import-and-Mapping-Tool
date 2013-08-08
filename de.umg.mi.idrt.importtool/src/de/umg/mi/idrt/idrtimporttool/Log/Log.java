package de.umg.mi.idrt.idrtimporttool.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.umg.mi.idrt.idrtimporttool.importidrt.ServerView;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class Log {

	public static void addLog(int code, String log) {
		String DATE_FORMAT_NOW = "HH:mm:ss";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		ServerView.setLog(sdf.format(cal.getTime()), log);
		
		
		
	}

}
