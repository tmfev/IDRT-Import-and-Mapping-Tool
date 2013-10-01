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

public class DocumentationCommand extends AbstractHandler  {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			boolean result = MessageDialog.openConfirm(Application.getShell(),
					"Open Documentation",
					"Do you want to open the PDF Documentation?");
			if(result) {
				Bundle bundle = Activator.getDefault().getBundle();
				Path path = new Path("/misc/IDRT-Documentation.pdf"); 
				URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
				URL fileUrl = FileLocator.toFileURL(url);
				URI uri = new URI(fileUrl.getPath().substring(1));
				Desktop.getDesktop().browse(uri);
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
