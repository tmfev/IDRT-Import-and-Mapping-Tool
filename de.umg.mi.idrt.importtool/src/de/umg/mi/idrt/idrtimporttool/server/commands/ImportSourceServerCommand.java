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

import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.ServerSourceContentProvider;
import de.umg.mi.idrt.importtool.views.ServerView;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class ImportSourceServerCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		HashMap<String, Server> serverList = ServerList.getSourceServers();
		TreeViewer viewer = ServerView.getSourceServerViewer();
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
					|| ServerList.getTargetServers().containsKey(
							newServer.getUniqueID())) {
				MessageDialog.openError(Display.getDefault()
						.getActiveShell(), "Error",
						"Server already exists!");
				System.err.println("ERROR, Server already exists!");
			} else {
				ServerList.addSourceServer(newServer);
				try {
					ServerList.loadServersfromProps();
					viewer.setContentProvider(new ServerSourceContentProvider());
				} catch (BackingStoreException e) {
					e.printStackTrace();
				}
			}
		} else {
		}
		return null;
	}
}
