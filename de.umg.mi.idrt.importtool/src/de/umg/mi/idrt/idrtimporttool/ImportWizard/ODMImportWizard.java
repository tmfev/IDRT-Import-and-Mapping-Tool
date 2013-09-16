package de.umg.mi.idrt.idrtimporttool.ImportWizard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;

import de.goettingen.i2b2.importtool.idrt.StatusListener.StatusListener;
import de.umg.mi.idrt.idrtimporttool.Log.Log;
import de.umg.mi.idrt.idrtimporttool.importidrt.Activator;
import de.umg.mi.idrt.idrtimporttool.importidrt.IDRTImport;
import de.umg.mi.idrt.idrtimporttool.importidrt.ServerView;
import de.umg.mi.idrt.idrtimporttool.messages.Messages;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class ODMImportWizard extends Wizard {

	private Properties defaultProps;
	public static boolean started = false;
	private static Thread b;

	private HashMap<String, String> contextMap;

	protected WizardPage1 one;
	protected ODMWizardPage2 two;

	public boolean done = false;
	
	@SuppressWarnings("deprecation")
	public static void killThread() {
		if (started) {
			b.stop();
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					Log.addLog(1, Messages.ODMImportWizard_ODMImportFailed); 
					MessageDialog
							.openError(
									Display.getDefault().getActiveShell(),
									Messages.ODMImportWizard_ODMImportFailed, 
									Messages.ODMImportWizard_ODMImportFailed
											+ ". (User aborted!)\nYou might want to redo your last action!"); 
				}
			});
		}
		StatusListener.notifyListener();
		started = false;
	}

	@SuppressWarnings("deprecation")
	public static void killThreadRemote(final String error,
			final String fileName) {
		if (started) {
			b.stop();

			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
//					closeBar(error, fileName, 1);
					Log.addLog(1, "ODM Import Failed: " + error); 
					MessageDialog.openError(Display.getDefault()
							.getActiveShell(),
							Messages.ODMImportWizard_ODMImportFailed, 
							Messages.ODMImportWizard_ImpFailedSeeLog);
				}
			});
		}
		StatusListener.notifyListener();
		started = false;
	}

	public static void updateStatus() {
		if (started) {
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					ServerView.setProgress((int) StatusListener.getPercentage());
					ServerView.setProgressTop(StatusListener.getFile());
					ServerView.setProgressBottom("" 
							+ StatusListener.getStatus());
				}
			});
		}
	}

	

	public ODMImportWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		one = new WizardPage1();
		two = new ODMWizardPage2();
		addPage(one);
		addPage(two);
	}

	@Override
	public boolean performFinish() {
		try {
			final boolean cleanUp = ODMWizardPage2.getCleanUp();
			final boolean terms = ODMWizardPage2.getTerms();
			Bundle bundle = Activator.getDefault().getBundle();
			Path path = new Path("/cfg/Default.properties"); 
			URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
			URL fileUrl = FileLocator.toFileURL(url);
			File properties = new File(fileUrl.getPath());
			defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));

			contextMap = new HashMap<String, String>();

			/*
			 * page 1
			 */
			contextMap.put("DBHost", WizardPage1.getIpText()); 
			defaultProps.setProperty("DBHost", WizardPage1.getIpText()); 

			contextMap.put("DBPassword", WizardPage1.getDBUserPasswordText()); 
			contextMap.put("DBUsername", WizardPage1.getDBUserText()); 
			contextMap.put("DBInstance", WizardPage1.getDBSIDText()); 
			contextMap.put("DBPort", WizardPage1.getPortText()); 
			contextMap.put("DBSchema", WizardPage1.getDBSchemaText()); 

			/*
			 * page 2
			 */
			contextMap.put("folderODM", ODMWizardPage2.getFolderODMText()); 
			defaultProps.setProperty("folderODM", ODMWizardPage2.getFolderODMText());
			
			Path miscPath = new Path("/misc/"); 
			URL miscUrl = FileLocator.find(bundle, miscPath,
					Collections.EMPTY_MAP);
			URL miscFileUrl = FileLocator.toFileURL(miscUrl);
			File misc = new File(miscFileUrl.getPath());
			String miscPathReplaced = misc.getAbsolutePath().replaceAll("\\\\", 
					"/") 
					+ "/"; 

			contextMap.put("folderMain", miscPathReplaced); 

			if (ODMWizardPage2.getTerms()) {
				/**
				 * ST-Import
				 */
				Path cfgPath = new Path("/cfg/"); //$NON-NLS-1$
				URL cfgUrl = FileLocator.find(bundle, cfgPath,
						Collections.EMPTY_MAP);
				URL cfgFileUrl = FileLocator.toFileURL(cfgUrl);
				File rootDir = new File(cfgFileUrl.getPath()
						+ "/Standardterminologien/".replaceAll("\\\\", "/"));
				contextMap.put("icd10Dir", rootDir.getAbsolutePath()+ "/ICD-10-GM/" + "/");
				contextMap.put("tnmDir",rootDir.getAbsolutePath() + "/TNM/" + "/");
				contextMap.put("rootDir",rootDir.getAbsolutePath()+ "/");
				contextMap.put("loincDir", rootDir.getAbsolutePath() + "/LOINC/" + "/");
				contextMap.put("opsDir",rootDir.getAbsolutePath() + "/OPS/" + "/");
				contextMap.put("p21Dir",rootDir.getAbsolutePath() + "/P21/" + "/");
				contextMap.put("drgDir",rootDir.getAbsolutePath() + "/DRG/" + "/");
				contextMap.put("icdoDir",rootDir.getAbsolutePath() + "/ICD-O-3/" + "/"); 
			}

			if (cleanUp) {
				defaultProps.setProperty("cleanUp", "true");  
				contextMap.put("cleanUp", "true");  
			} else {
				defaultProps.setProperty("cleanUp", "false");  
				contextMap.put("cleanUp", "false");  
			}

			
			if (ODMWizardPage2.getCompleteCodelist()) {
				contextMap.put("importCodelist", "true");  
				defaultProps.setProperty("importCodelist", "true");  
			} else {
				contextMap.put("importCodelist", "false");  
				defaultProps.setProperty("importCodelist", "false");  
			}
			if (ODMWizardPage2.getIncludePids()) {
				contextMap.put("includePids", "true");  
				defaultProps.setProperty("includePids", "true");  
			} else {
				contextMap.put("includePids", "false");  
				defaultProps.setProperty("includePids", "false");  
			}

			if (ODMWizardPage2.getSaveContext()) {
				defaultProps.store(new FileWriter(properties), ""); 
			}

			if (ODMWizardPage2.getTruncate()) {
				contextMap.put("truncateProject", "true");  
			} else {
				contextMap.put("truncateProject", "false");  
			}

			done = false;

			b = new Thread(new Runnable() {

				@Override
				public void run() {
					started = true;
					final long start = System.currentTimeMillis();
					IDRTImport.setCompleteContext(contextMap);
					int exitCode = IDRTImport.runODMImport(terms);
					done = true;
					started = false;
					StatusListener.setStatus(0, "", "");  
					StatusListener.setSubStatus(0.0f, "");
					StatusListener.notifyListener();

					if (exitCode == 0) {
						Display.getDefault().syncExec(new Runnable() {

							@Override
							public void run() {
								Log.addLog(0,
										Messages.ODMImportWizard_ODMImpFinished);
								long end = System.currentTimeMillis();
								long time = end - start;
								Log.addLog(0, Messages.ODMImportWizard_Duration
										+ (time / 1000) + " s"); 
								MessageDialog
										.openInformation(
												Display.getDefault()
														.getActiveShell(),
												Messages.ODMImportWizard_ODMImpFinished,
												Messages.ODMImportWizard_ODMImpFinished);  
							}
						});
					}
				}
			});
			if (!started) {
				b.start();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}