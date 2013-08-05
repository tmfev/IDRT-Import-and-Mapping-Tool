package de.umg.mi.idrt.ioe;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;


public class LogListener implements ILogListener {

	private final static String LOG_DIRECTORY = "log";
	private final static String FILENAME_EXTENSION = "log";
	private final static String ERROR_LOG_FILE = "iit_error";
	private final static String ERROR_TIMESTAMP_FORMAT = "yyyyMMdd HH:mm:ss";
	
	
    private File logFile = null;
    
    public LogListener(){
    	
        File outputDir = new File( LOG_DIRECTORY );
        
        if (!outputDir.exists()) {
			try {
				outputDir.mkdir();
				
			} catch (SecurityException se) {
				System.err.println(se.toString());
			}
		}
        
        
        logFile = new File( LOG_DIRECTORY + "/" + ERROR_LOG_FILE + "." + FILENAME_EXTENSION );
        System.out.println(logFile.getAbsolutePath());
        try {
        	logFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    @Override
    public void logging(IStatus status, String plugin) {
    	
        try {

			DateFormat dateFormat = new SimpleDateFormat( ERROR_TIMESTAMP_FORMAT );
			Date date = new Date();
        	
            BufferedWriter bos = new BufferedWriter( new FileWriter(logFile,true) );
            StringBuffer str = new StringBuffer(plugin);
            str.append( " - " + dateFormat.format(date) + ": ");
            str.append( status.getMessage() );
            String stackTrace = getStackTraceAsString( (Exception) status.getException() );
            str.append( stackTrace );
            str.append( "\n" );
            bos.write( str.toString()  );
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    
    public static String getStackTraceAsString(Throwable exception){

    	String text = "";

    	try { 
    		
    		// exception seems to be null sometimes, so test for it
            if (exception != null){
            	ByteArrayOutputStream baos = new ByteArrayOutputStream();
            	exception.printStackTrace(new PrintStream(baos));
            	text += ": >" + exception.getMessage() + "<\n" + baos.toString() + "\n";
            	baos.close();
            } else {
            	text += ": Throwable is null\n";
            }

            
        }
        catch (IOException ioe) 
        {
            Debug.e("In LogListener.getStackTraceAsString() "
            		+ "error converting exception to string. "
            		+ "Exception was " + ioe.toString());
        }
        return text;
	}
}