package de.umg.mi.idrt.idrtimporttool.Log;

import de.umg.mi.idrt.importtool.views.ServerView;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class Log {

	public static void addLog(int code, String log) {
		ServerView.setLog(log);
	}

}
