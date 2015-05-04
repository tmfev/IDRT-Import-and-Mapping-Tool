package de.umg.mi.idrt.ioe.wizards;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Properties;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.TOSConnector;
import de.umg.mi.idrt.importtool.misc.FileHandler;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;
import de.umg.mi.idrt.ioe.view.ProgressView;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class UploadProjectWizard extends Wizard {

	protected UploadProjectWizardPage2 two;

	private static Thread importThread;
	public UploadProjectWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	private HashMap<String, String> contextMap;

	@Override
	public void addPages() {
		two=new UploadProjectWizardPage2();
		addPage(two);
	}

	@Override
	public boolean canFinish() {
		return true;
	}

	@Override
	public boolean performFinish() {
		
//		String stagingServerName = ServerList.getUserServer().get(OntologyEditorView.getTargetSchemaName());
//		Server stagingServer = ServerList.getTargetServers().get(stagingServerName);
//		final String targetIPText = stagingServer.getIp();
//		final String targetPasswordText = stagingServer.getPassword();
//		final String targetDBUserText = stagingServer.getUser();
//		final String targetDBSID = stagingServer.getSID();
//		final String targetDBPort = stagingServer.getPort();
//		final String targetDBSchema = OntologyEditorView.getTargetSchemaName();
		
		String targetServerName = ServerList.getUserServer().get(OntologyEditorView.getStagingSchemaName());
		Server currentServer = ServerList.getTargetServers().get(targetServerName);
		currentServer.setSchema(OntologyEditorView.getStagingSchemaName());
//		final String stagingIPText = currentServer.getIp();
//		final String stagingPasswordText = currentServer.getPassword();
//		final String stagingDBUserText = currentServer.getUser();
//		final String stagingDBSID = currentServer.getSID();
//		final String stagingDBPort = currentServer.getPort();
//		final String stagingDBSchema = currentServer.getSchema();
		
		
		
		final boolean truncate = UploadProjectWizardPage2.getTruncate();
		final boolean truncateQueries = UploadProjectWizardPage2.getTruncateQueries();
		final boolean save = UploadProjectWizardPage2.getSaveContext();

		importThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					
					File properties = FileHandler.getBundleFile("/cfg/Default.properties");
					Properties defaultProps = new Properties();
					defaultProps.load(new FileReader(properties));
					
					System.out.println("OntologyEditorView " + OntologyEditorView.isInit());
					System.out.println("schema: " + OntologyEditorView.getTargetSchemaName());
						ProgressView.setProgress(0, "Uploading...", "");


						if (OntologyEditorView.isInit() && !OntologyEditorView.getTargetSchemaName().isEmpty() && !OntologyEditorView.getTargetSchemaName().startsWith("Drop i2b2")) {
							String stagingServerName = ServerList.getUserServer().get(OntologyEditorView.getTargetSchemaName());
							Server stagingServer = ServerList.getTargetServers().get(stagingServerName);
							if (stagingServer==null){
								MessageDialog.openError(Application.getShell(), "Target Server null", "Please expand the target server in the Server View and retry!");
							}
							else {
								final boolean stopIndex = UploadProjectWizardPage2.getStopIndex();
								final boolean dropIndex = UploadProjectWizardPage2.getDropIndex();
								
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

								File outputFolder = de.umg.mi.idrt.ioe.misc.FileHandler.getBundleFile("/temp/");
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
								System.out.println("TARGETID: " + OntologyEditorView.getTargetInstance().getSelectedTarget().getTargetID());
								contextMap.put("TargetID",""+ OntologyEditorView.getTargetInstance().getSelectedTarget().getTargetID());

								contextMap.put("truncateProject", String.valueOf(truncate));
								contextMap.put("truncateQueries", String.valueOf(truncateQueries));
								
								if (dropIndex){
									System.out.println("DROPPING INDEX");
									defaultProps.setProperty("IndexStop", "false");
									contextMap.put("IndexStop", "false");
									
									defaultProps.setProperty("IndexDrop", "true");
									contextMap.put("IndexDrop", "true");
								}
								else if (stopIndex){
									System.out.println("STOPPING INDEX");
									defaultProps.setProperty("IndexStop", "true");
									contextMap.put("IndexStop", "true");
									defaultProps.setProperty("IndexDrop", "false");
									contextMap.put("IndexDrop", "false");
								}
								else {
									System.out.println("IGNORING INDEX");
									defaultProps.setProperty("IndexStop", "false");
									contextMap.put("IndexStop", "false");
									defaultProps.setProperty("IndexDrop", "false");
									contextMap.put("IndexDrop", "false");
								}
								
								if (save) {
									defaultProps.store(new FileWriter(properties),
											"");
								}
								TOSConnector.setCompleteContext(contextMap);
								Application.executeCommand(Resource.ID.Command.IOE.STAGINGTOTARGET);
							}
						}
						else {
							MessageDialog.openError(Application.getShell(), "No Target Project", "No Target Project given!");
						}
					ProgressView.setProgress(0, "", "");
					
				}catch (Exception e) {
					e.printStackTrace();
					Console.error(e);
				}
			}
		});
		importThread.run();
		return true;
	}

}