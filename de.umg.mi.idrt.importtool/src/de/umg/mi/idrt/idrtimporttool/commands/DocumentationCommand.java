package de.umg.mi.idrt.idrtimporttool.commands;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.osgi.framework.Bundle;

import de.umg.mi.idrt.idrtimporttool.importidrt.Activator;
import de.umg.mi.idrt.idrtimporttool.importidrt.Application;
import de.umg.mi.idrt.importtool.misc.FileHandler;

public class DocumentationCommand extends AbstractHandler  {
	private static String url = "https://community.i2b2.org/wiki/display/IDRT/IDRT+-+Integrated+Data+Repository+Toolkit";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		try {
		boolean result = MessageDialog.openConfirm(Application.getShell(),
				"Open Documentation",
				"Do you want to open the documentation in a web browser?");
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
