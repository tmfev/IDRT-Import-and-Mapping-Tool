package de.umg.mi.idrt.idrtimporttool.server.commands;

import java.io.File;
import java.util.HashMap;
import java.util.prefs.BackingStoreException;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import de.umg.mi.idrt.idrtimporttool.importidrt.ServerView;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.ServerContentProvider;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class ImportTargetServerCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		HashMap<String, Server> serverList = ServerList.getTargetServers();
		TreeViewer viewer = ServerView.getTargetServersViewer();
		System.out.println("import server");

		FileDialog dialog = new FileDialog(Display.getDefault()
				.getActiveShell(), SWT.OPEN);
		dialog.setFilterNames(new String[] { "All Files (*.*)" });
		dialog.setFilterExtensions(new String[] { "*.*" });
		dialog.setFilterPath(System.getProperty("user.dir"));
		String fileName = dialog.open();

		if (fileName != null) {
			File importServerFile = new File(fileName);

			Server newServer = ServerList
					.deserializeImportServer(importServerFile);
			if (newServer == null) {
				MessageDialog.openError(Display.getDefault()
						.getActiveShell(), "Error", "Import failed!");
			}
			if (serverList.containsKey(newServer.getUniqueID())
					|| ServerList.getSourceServers().containsKey(
							newServer.getUniqueID())) {
				MessageDialog.openError(Display.getDefault()
						.getActiveShell(), "Error",
						"Server already exists!");
				System.err.println("ERROR, Server already exists!");
			} else {
				ServerList.addServer(newServer);
			}
		} else {
			// MessageDialog.openError(shell, "Error", "Import failed!");
		}
		try {
			ServerList.loadServersfromProps();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		viewer.setContentProvider(new ServerContentProvider());
		return null;
	}
}
