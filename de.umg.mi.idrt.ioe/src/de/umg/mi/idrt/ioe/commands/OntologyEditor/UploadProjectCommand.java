package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import java.io.File;
import java.util.HashMap;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;

import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.TOSConnector;
import de.umg.mi.idrt.ioe.misc.FileHandler;
import de.umg.mi.idrt.ioe.misc.IDRTMessageDialog;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class UploadProjectCommand extends AbstractHandler {
	private HashMap<String, String> contextMap;
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("OntologyEditorView " + OntologyEditorView.isInit());
		System.out.println("schema: " + OntologyEditorView.getTargetSchemaName());
	boolean confirm = MessageDialog.openConfirm(Application.getShell(), "Warning", "This will TRUNCATE the target project!\nDo you want to" +
				" proceed?");
	
	if(confirm) {
		if (OntologyEditorView.isInit() && !OntologyEditorView.getTargetSchemaName().isEmpty() && !OntologyEditorView.getTargetSchemaName().startsWith("Drop i2b2")) {
			String stagingServerName = ServerList.getUserServer().get(OntologyEditorView.getTargetSchemaName());
			Server stagingServer = ServerList.getTargetServers().get(stagingServerName);
			final String targetIPText = stagingServer.getIp();
			final String targetPasswordText = stagingServer.getPassword();
			final String targetDBUserText = stagingServer.getUser();
			final String targetDBSID = stagingServer.getSID();
			final String targetDBPort = stagingServer.getPort();
			final String targetDBSchema = OntologyEditorView.getTargetSchemaName();

			String targetServerName = ServerList.getUserServer().get(OntologyEditorView.getStagingSchemaName());
			Server currentServer = ServerList.getTargetServers().get(targetServerName);
			currentServer.setSchema(OntologyEditorView.getStagingSchemaName());
			final String stagingIPText = currentServer.getIp();
			final String stagingPasswordText = currentServer.getPassword();
			final String stagingDBUserText = currentServer.getUser();
			final String stagingDBSID = currentServer.getSID();
			final String stagingDBPort = currentServer.getPort();
			final String stagingDBSchema = currentServer.getSchema();

			File outputFolder = FileHandler.getBundleFile("/temp/");
			System.out.println("outputFolder " + outputFolder.getAbsolutePath());
			contextMap = new HashMap<String, String>();

			contextMap.put("folderMain",outputFolder.getAbsolutePath().replaceAll("\\\\", "/")+"/");
			contextMap.put("folderOutput","/output");
			System.out.println(outputFolder.getAbsolutePath().replaceAll("\\\\", "/")+"/output/");
			
			/**
			 * page 1
			 */
			contextMap.put("DB_TargetI2B2_Host", targetIPText);
			contextMap.put("DB_TargetI2B2_Password", targetPasswordText);
			contextMap.put("DB_TargetI2B2_Username", targetDBUserText);
			contextMap.put("DB_TargetI2B2_Instance", targetDBSID);
			contextMap.put("DB_TargetI2B2_Port", targetDBPort);
			contextMap.put("DB_TargetI2B2_Schema", targetDBSchema);

			contextMap.put("DB_StagingI2B2_Host", stagingIPText);
			contextMap.put("DB_StagingI2B2_Password", stagingPasswordText);
			contextMap.put("DB_StagingI2B2_Username", stagingDBUserText);
			contextMap.put("DB_StagingI2B2_Instance", stagingDBSID);
			contextMap.put("DB_StagingI2B2_Port", stagingDBPort);
			contextMap.put("DB_StagingI2B2_Schema", stagingDBSchema);
			System.out.println("TARGETID: " + OntologyEditorView.getTargetProjects().getSelectedTarget().getTargetID());
			contextMap.put("TargetID",""+ OntologyEditorView.getTargetProjects().getSelectedTarget().getTargetID());
			TOSConnector.setCompleteContext(contextMap);
			Application.executeCommand(Resource.ID.Command.IOE.STAGINGTOTARGET);

			//			WizardDialog wizardDialog = new WizardDialog(OntologyEditorView.getTargetTreeViewer().getControl().getShell(), new UploadProjectWizard());
			//			wizardDialog.open();
		}
		else {
			MessageDialog.openError(Application.getShell(), "No Target Project", "No Target Project given!");
		}
	}
		return null;
	}
}