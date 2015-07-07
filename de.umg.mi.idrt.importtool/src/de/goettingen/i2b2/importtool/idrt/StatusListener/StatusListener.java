package de.goettingen.i2b2.importtool.idrt.StatusListener;


import java.io.File;
import java.lang.annotation.Target;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.widgets.Display;

import de.umg.mi.idrt.idrtimporttool.ImportWizard.CSVImportWizard;
import de.umg.mi.idrt.idrtimporttool.ImportWizard.DBImportWizard;
import de.umg.mi.idrt.idrtimporttool.ImportWizard.MDRImportWizard;
import de.umg.mi.idrt.idrtimporttool.ImportWizard.ODMImportWizard;
import de.umg.mi.idrt.idrtimporttool.ImportWizard.P21ImportWizard;
import de.umg.mi.idrt.idrtimporttool.Log.Log;
import de.umg.mi.idrt.importtool.views.ServerView;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class StatusListener {

	private static boolean interrupted;
	public static float perc;
	public static float subPerc;
	public static String subStatus = "";
	public static String status = "";
	public static String file = "";
	private static int logCounter = 0;
	private static int importErrorCounter = 0;
	public static boolean sleeping = false;
	public static String log;
	public static String filename;
	public static ScheduledExecutorService executor;

	public static void addError(final String logg, final String fileName) {
		final File file = new File(fileName);
		filename = file.getName();
		log = logg;
		logCounter++;
		setImportErrorCounter(getImportErrorCounter() + 1);
//		System.out.println("ERRORLOG: " + logCounter + " -- " + filename + " - " + log);
	}

	public static void addLog(final String log, final String fileName) {
		final File file = new File(fileName);
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				Log.addLog(0, log + " - " + file.getName());

			}});
	}

	public static void clearStatus() {
		perc = 0;
		subPerc = 0;
		subStatus = "";
		status = "";
		file = "";
		ServerView.updateStatus();
	}

	public static void error(final String msg, final String error,
			final String fileName) {

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
//				interruptExtern(error, fileName);
				Log.addLog(1, msg + " - " + error);
			}
		});
	}
	
	public static void errorInfo(final String msg, final String error,
			final String fileName) {
		final File file = new File(fileName);
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				Log.addLog(1, msg + " - " + error + " @ " + file.getName());
				System.err.println(error);
			}
		});
	}

	public static String getFile() {
		return file;
	}

	public static int getImportErrorCounter() {
		return importErrorCounter;
	}

	public static boolean getInterrupt() {
		return interrupted;
	}

	public static float getPercentage() {
			return perc;
	}

	public static String getStatus() {
		return status;
	}

	/**
	 * @return the subPerc
	 */
	public static float getSubPerc() {
		return subPerc;
	}

	/**
	 * @return the subStatus
	 */
	public static String getSubStatus() {
		return subStatus;
	}

	public static void interrupt() {
		interrupted = true;
		StatusListener.stopLogging();
		CSVImportWizard.killThread();
		ODMImportWizard.killThread();
		DBImportWizard.killThread();
		P21ImportWizard.killThread();
		MDRImportWizard.killThread();
//		TOSConnector.killThread();
	}
	
public static void startImport(){
	System.out.println("STARTING IMPORT");
	ServerView.btnStopSetEnabled(true);
	}
public static void stopImport(){
	System.out.println("STOPPING IMPORT");
	ServerView.btnStopSetEnabled(false);
}
	public static void interruptExtern(String error, String fileName) {
		interrupted = true;
		CSVImportWizard.killThreadRemote(error,fileName);
		ODMImportWizard.killThreadRemote(error,fileName);
		DBImportWizard.killThreadRemote(error,fileName);
		P21ImportWizard.killThreadRemote(error,fileName);
		MDRImportWizard.killThreadRemote(error,fileName);
	}

	public static void notifyListener() {
		interrupted = false;
	}

	public static void setImportErrorCounter(int importErrorCounter) {
		StatusListener.importErrorCounter = importErrorCounter;
	}

	public static void setStatus(float percentage, String currentFile) {
		File fileName = new File("STATUS: " + percentage + " " +currentFile);
		perc = percentage;
		status = "Getting PIDs...";
		file = fileName.getName();
		ServerView.updateStatus();
		System.out.println(getTime() + " STATUS: " + currentFile);
	}
	private static String getTime(){
		String DATE_FORMAT_NOW = "HH:mm:ss";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		String time = sdf.format(cal.getTime());
		return time;
	}

	public static void setStatus(float percentage, final String statusMsg,
			final String currentFile) {
		System.out.println(getTime() + " STATUS: " + percentage + " " +statusMsg);
		File fileName = new File(currentFile);
		perc = percentage;
		status = statusMsg;
		file = fileName.getName();
		ServerView.updateStatus();

		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				Log.addLog(0, statusMsg + " - " + currentFile);
			}
		});
	}
	
	public static void setStatus(final String statusMsg) {
		System.out.println(getTime() + " STATUS: " + statusMsg);
		status = statusMsg;
		ServerView.updateStatus();
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				Log.addLog(0, statusMsg);
			}
		});
	}
	
	public static void setStatusPID(float percentage, String msg,
			String currentFile) {
		System.out.println(getTime() + " STATUS: " + percentage + " "  + msg);
		File fileName = new File(currentFile);
		perc = percentage;
		status = msg;
		file = fileName.getName();
		ServerView.updateStatus();
	}

	/**
	 * @param subPerc the subPerc to set
	 */
	public static void setSubPerc(float subPerc) {
		StatusListener.subPerc = subPerc;
	}

	public static void setSubStatus(float percentage, final String statusMsg) {
//		System.out.println(getTime() + " SUBSTATUS: " + statusMsg);
		subPerc = percentage;
		subStatus = statusMsg;
		ServerView.updateStatus();
	}

	public static void startLogging() {
		System.out.println("START LOGGING");
		Runnable helloRunnable = new Runnable() {
			public void run() {
				try {
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
//							System.out.println("autolog");
							if (logCounter>0)
							Log.addLog(0, log + " - " + logCounter +" more Errors " + filename);
							logCounter=0;
						}});
					
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(helloRunnable, 3, 3, TimeUnit.SECONDS);
	}

	public static void stopLogging() {
		System.out.println("STOP LOGGING");
		executor.shutdown();
	}
}
