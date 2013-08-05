package de.umg.mi.idrt.idrtimporttool.server.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.TreeViewer;

import de.umg.mi.idrt.idrtimporttool.importidrt.ServerView;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.ServerContentProvider;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class DeleteAllTargetServersCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("adding server");
		TreeViewer viewer = ServerView.getTargetServersViewer();

		System.out.println("removing all servers");
		ServerList.removeAll();
		viewer.setContentProvider(new ServerContentProvider());

		viewer.refresh();
		return viewer;
	}
}
