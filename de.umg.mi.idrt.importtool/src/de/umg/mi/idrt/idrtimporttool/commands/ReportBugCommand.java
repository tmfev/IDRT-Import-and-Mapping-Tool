package de.umg.mi.idrt.idrtimporttool.commands;

import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;

import de.umg.mi.idrt.idrtimporttool.importidrt.Application;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */

public class ReportBugCommand extends AbstractHandler {

	private static String url = "https://vm04.mi.med.uni-goettingen.de/jira/secure/CreateIssue!default.jspa";
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		try {
		boolean result = MessageDialog.openConfirm(Application.getShell(),
				"Bug Report",
				"Do you want to report a Bug?\nThis will open your default browser.");
		if(result) {
			if(java.awt.Desktop.isDesktopSupported() ) {
		        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
		 
		        if(desktop.isSupported(java.awt.Desktop.Action.BROWSE) ) {
		          java.net.URI uri;
				
					uri = new java.net.URI(url);
				
		              desktop.browse(uri);
		        }
		      }
		}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
