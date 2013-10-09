package de.umg.mi.idrt.ioe.wizards;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.Wizard;
import org.osgi.framework.Bundle;

import de.umg.mi.idrt.idrtimporttool.ImportWizard.CSVWizardPage2;
import de.umg.mi.idrt.idrtimporttool.ImportWizard.CSVWizardPage3;
import de.umg.mi.idrt.idrtimporttool.ImportWizard.WizardPage1;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.ioe.Activator;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.OntologyTree.TOSConnector;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class UploadProjectWizard extends Wizard {

	protected UploadProjectWizardPage1 one;
	protected UploadProjectWizardPage2 two;

	private Properties defaultProps;
	private static Thread importThread;
	private HashMap<String, String> contextMap;

	public UploadProjectWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		one = new UploadProjectWizardPage1();
		two = new UploadProjectWizardPage2();
		addPage(one);
		addPage(two);
	}

	@Override
	public boolean canFinish() {
		return true;
	}

	@Override
	public boolean performFinish() {

		final String targetIPText = UploadProjectWizardPage1.getIpText();
		final String targetPasswordText = UploadProjectWizardPage1.getDBUserPasswordText();
		final String targetDBUserText = UploadProjectWizardPage1.getDBUserText();
		final String targetDBSID = UploadProjectWizardPage1.getDBSIDText();
		final String targetDBPort = UploadProjectWizardPage1.getPortText();
		final String targetDBSchema = UploadProjectWizardPage1.getDBSchemaText();
		
		
		Server currentServer = OntologyEditorView.getCurrentServer();
		final String stagingIPText = currentServer.getIp();
		final String stagingPasswordText = currentServer.getPassword();
		final String stagingDBUserText = currentServer.getUser();
		final String stagingDBSID = currentServer.getSID();
		final String stagingDBPort = currentServer.getPort();
		final String stagingDBSchema = currentServer.getSchema();
		
		final boolean truncate = UploadProjectWizardPage2.getTruncate();
		final boolean cleanUp = UploadProjectWizardPage2.getCleanUp();
		final boolean save = UploadProjectWizardPage2.getSaveContext();

		importThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					final long start = System.currentTimeMillis();

					Bundle bundle = Activator.getDefault().getBundle();
					Path path = new Path("/cfg/Default.properties"); //$NON-NLS-1$
					URL url = FileLocator.find(bundle, path,
							Collections.EMPTY_MAP);
					URL fileUrl = null;
					Path outputPath = new Path("/temp/"); //$NON-NLS-1$
					URL outputURL = FileLocator.find(bundle, outputPath,
							Collections.EMPTY_MAP);
					URL outputURL2 = FileLocator.toFileURL(outputURL);
					File outputFolder = new File(outputURL2.getPath());
					
					fileUrl = FileLocator.toFileURL(url);
					File properties = new File(fileUrl.getPath());
					defaultProps = new Properties();
					defaultProps.load(new FileReader(properties));

					contextMap = new HashMap<String, String>();

					contextMap.put("folderMain",outputFolder.getAbsolutePath().replaceAll("\\\\", "/"));
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
					contextMap.put("DB_TargetI2B2_jdbcurl","jdbc:oracle:thin:@134.76.124.17:1521:i2b2t");
					contextMap.put("DB_TargetI2B2_sqlclassname","oracle.jdbc.driver.OracleDriver");
					
					contextMap.put("DB_StagingI2B2_Host", stagingIPText);
					contextMap.put("DB_StagingI2B2_Password", stagingPasswordText);
					contextMap.put("DB_StagingI2B2_Username", stagingDBUserText);
					contextMap.put("DB_StagingI2B2_Instance", stagingDBSID);
					contextMap.put("DB_StagingI2B2_Port", stagingDBPort);
					contextMap.put("DB_StagingI2B2_Schema", stagingDBSchema);
					contextMap.put("DB_StagingI2B2_jdbcurl", "jdbc:oracle:thin:@134.76.124.17:1521:i2b2t");
					contextMap.put("DB_StagingI2B2_sqlclassname", "oracle.jdbc.driver.OracleDriver");
					
					
					System.out.println("TARGET:");
					System.out.println(targetIPText);
					System.out.println(targetPasswordText);
					System.out.println(targetDBUserText);
					System.out.println(targetDBSID);
					System.out.println(targetDBPort);
					System.out.println(targetDBSchema);
					System.out.println();
					System.out.println("STAGING:");
					System.out.println(stagingIPText);
					System.out.println(stagingPasswordText);
					System.out.println(stagingDBUserText);
					System.out.println(stagingDBSID);
					System.out.println(stagingDBPort);
					System.out.println(stagingDBSchema);
					/**
					 * page 2
					 */
					
					if (cleanUp) {
						defaultProps.setProperty("cleanUp", "true");
						contextMap.put("cleanUp", "true");
					} else {
						defaultProps.setProperty("cleanUp", "false");
						contextMap.put("cleanUp", "false");
					}
					
					if (save) {
						defaultProps.store(new FileWriter(properties), "");
					}
					if (truncate) {
						contextMap.put("truncateProject", "true");
					} else {
						contextMap.put("truncateProject", "false");
					}
					
					TOSConnector.setCompleteContext(contextMap);
					int exitCode = TOSConnector.uploadProject();
					System.out.println("job finshed: " + exitCode);
					
				}catch (Exception e) {
					e.printStackTrace();
					Console.error(e);
				}
			}
		});
		importThread.run();
		//TODO DB Access
		return true;
	}

}