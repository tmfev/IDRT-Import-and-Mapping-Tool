package de.umg.mi.idrt.idrtimporttool.server.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import de.umg.mi.idrt.idrtimporttool.importidrt.ServerView;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class ExportSourceServerCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("exporting server");
		TreeViewer viewer = ServerView.getSourceServerViewer();

		String serverName = viewer.getTree().getSelection()[0].getText();
		String serverStoreName = viewer.getTree().getSelection()[0].getText();
		HashMap<String, Server> serverList = ServerList.getSourceServers();
		Server currentServer = serverList.get(serverName);
		try {
			FileDialog dialog = new FileDialog(Display.getDefault()
					.getActiveShell(), SWT.SAVE);
			dialog.setFilterNames(new String[] { "All Files (*.*)" });
			dialog.setFilterExtensions(new String[] { "*.*" });
			dialog.setFilterPath(System.getProperty("user.dir"));
			dialog.setFileName(serverStoreName);
			String fileName = dialog.open();
			File serverFile = new File(fileName);
			if (fileName != null) {
				ObjectOutputStream os = new ObjectOutputStream(
						new FileOutputStream(serverFile));

				os.writeObject(currentServer);
				os.flush();
				os.close();

				MessageDialog.openInformation(Display.getDefault()
						.getActiveShell(), "Success", serverStoreName
						+ " exported to " + fileName);
			} else {
				MessageDialog.openError(Display.getDefault()
						.getActiveShell(), "Error", "Export failed!");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
