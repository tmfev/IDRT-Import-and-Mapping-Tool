package de.umg.mi.idrt.ioe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFile {

	public LogFile(String message) throws IOException {
		File outputDir = new File("log");
		File file = null;

		if (!outputDir.exists()) {
			try {
				outputDir.mkdir();

			} catch (SecurityException se) {
				System.err.println(se.toString());
			}
		}

		if (outputDir.exists()) {
			try {

				DateFormat dateFormatDay = new SimpleDateFormat("yyyyMMdd");
				DateFormat dateFormatMinutes = new SimpleDateFormat("HH:mm:ss");
				Date date = new Date();

				FileWriter fileWritter = new FileWriter("log/" + LOG_FILENAME
						+ "_" + dateFormatDay.format(date) + "."
						+ LOG_FILENAME_TYPE, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(dateFormatMinutes.format(date) + " - "
						+ message + "\n");
				bufferWritter.close();

			} catch (IOException e) {
				System.err.println(e.toString());
			}

		}

	}
	final String LOG_FILENAME = "iitlog";
	final String LOG_FILENAME_TYPE = "txt";

	public PrintWriter out = null;

}
