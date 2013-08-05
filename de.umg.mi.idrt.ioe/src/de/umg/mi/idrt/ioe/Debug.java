package de.umg.mi.idrt.ioe;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Debug {
	
	private HashMap<String,Boolean> showDebugMap = new HashMap<String, Boolean>();
	boolean showFunctions = true;
	private static String DEBUG_SEPERATOR = "-";
	
	private static boolean SHOW_DEBUG_IMPORT = true;
	
	public Debug(){
		
		this.showDebugMap.put("", true);
		this.showDebugMap.put("pn", true); // patientNode
		this.showDebugMap.put("showPatientAnswers", true); // show every 50. added patientAnswer
		showFunctions = true;

	}
	
	private static void print(String string){
		// print to console
		//System.out.println(string);
		//TODO print to log-file
		try {
			new LogFile(string);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void printerror(String string){
		// print to console
		System.err.println(string);
		//TODO print to log-file
		try {
		new LogFile("#ERROR# " + string);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
    public static void createDebug(int row, String debug ){
    	String rows = "";
    	for(int x = 0; x < row; x++){
    		rows += DEBUG_SEPERATOR;
    	}
    	print( rows + " " + debug);
    }
    
    public static void createDebug(int row, Number debug ){
    	createDebug(row, debug.toString());
    
    }
    
    public static void createDebugClass(String debug ){
    	createDebug(0, "{"+debug+"}");
    }
    
    public static void createDebugAction(String action ){
    	createDebugAction(action, "");
    }
    
    public static void createDebugAction(String action, String where ){
    	createDebug(0, "-> performAction: " + action + (!where.isEmpty() ? " @" + where : ""));
    }
    
    public boolean showDebug(String id){
    	if (this.showDebugMap.get(id) != null
    			&& this.showDebugMap.get(id)){
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public void createDebug(String id, int row, String debug ){
    	String rows = "";
    	for(int x = 0; x < row; x++){
    		rows += "*";
    	}
    	if (showDebug(id))
    		print( rows + " " + debug);
    }
    
    public static void createDebug(int row, String debug, String value ){
    	createDebug(row,debug + ": >" + value + "<");
    }
    
    public void createDebug(String id, int row, String debug, String value ){
    	if (showDebug(id))
    		createDebug(row,debug + ": >" + value + "<");
    }
    
    public static void createDebug(int row, String debug, int value ){
    	createDebug(row,debug + ": >" + String.valueOf(value) + "<");
    }
    
    public void createDebug(String id, int row, String debug, int value ){
    	if (showDebug(id))
    		createDebug(row,debug + ": >" + String.valueOf(value) + "<");
    }
    
    public static void createDebug(int row, String debug, long value ){
    	createDebug(row,debug + ": >" + String.valueOf(value) + "<");
    }
    
    public void createDebug(String id, int row, String debug, long value ){
    	if (showDebug(id))
    		createDebug(row,debug + ": >" + String.valueOf(value) + "<");
    }
    
    public static void createDebugBoolean(int row, String debug, boolean b ){
    	
      	createDebug(row, debug + ": >" + ( b ? "true" : "false") +"<");
    }
    
    public void createDebugBoolean(String id, int row, String debug, boolean b ){
    	String rows = "";
    	for(int x = 0; x <= row; x++){
    		rows += "*";
    	}
    	if (showDebug(id))
    		createDebug(row, debug + ": >" + ( b ? "true" : "false") +"<");
    }
    
    public static void createDebugFunction(String debug){
    	createDebugFunction(debug, "");
    }
    
    public static void createDebugFunction(String function, String where ){
    	createDebug( 0, "# " + function + (!where.isEmpty() ? " @"+ where : ""));
    }
    
    public static void createDebugFunction(String function, Object where ){
    	createDebug( 0, "# " + function + " @"+ where.getClass().getSimpleName());
}
    
    public void createDebugHeadline(String debug ){
    	createDebug( 0, "" + debug);
    }
    
    public void createDebugHeadline(String id, String debug ){
    	if (showDebug(id))
    		createDebug( 0, "" + debug);
    }
    
    public void createDebugError(String debug ){
    	printerror(debug);
    }
    
    public void createDebugError(String id, String debug ){
    	if (showDebug(id))
    		printerror(debug);
    }
    
    public static void functionRPC(String debug ){
       	print( "[" + debug + "]");
    }
    
    public static void f(String debug ){
    	functionRPC(debug);
    }
    
    public static void f(String function, Object where ){
		print( "[" + function + "] @"+ where.getClass().getSimpleName());
    }
    
    public static void fcreateDebugFunction(String function, Object where ){
    	createDebugFunction(function, where);
    }
    
    public static void d(String debug){
       	createDebug(1,debug);
    }
    
    public static void dd(String debug){
       	createDebug(2,debug);
    }
    
    public static void d(int i, String debug){
       	createDebug(i,debug);
    }
    
    public static void d(int i, Number debug){
       	createDebug(i,debug);
    }
    
    public static void d(int i, String debug, Number value){
       	createDebug(i,debug + ": " + value);
    }
    
    public static void d(int i, String debug, String value){
       	createDebug(i,debug + ": " + value);
    }

    
    public static void d(String debug, String value){
       	createDebug(1,debug + ": " + value);
    }
    
    public static void d(String debug, boolean value){
       	createDebug(1,debug + ": " + (value == true ? "true" : "false"));
    }
    
    public static void d(String debug, int debugnumber){
       	createDebug(1,debug,debugnumber);
    }
    
    public static void db(String debug, boolean bool){
       	createDebugBoolean(1,debug, bool);
    }
    
    public static void db(int i, String debug, boolean bool){
       	createDebugBoolean(i,debug, bool);
    }
    
    public static void dn(String debug, Object object){
       	createDebug(1,debug + ":" + (object == null ? "isNull" : "isNotNull"));
    }
    
    public static void de(String debug){
    	print("\\Error " + debug);
    }
    
    public static void e(String debug){
    	de(debug);
    }
    
    public static void e(String debug, Object object){
    	de(debug + " @" + object.getClass().getSimpleName());
    }
    
    public static void check(boolean check, String debug, Object object){
    	if (check == false){
    		e(debug + " @" + object.getClass().getSimpleName());
    	}
    }
    
    public static void c(String debug){
    	createDebugClass(debug);
    }
    
    public static void c(Object debug){
    	createDebugClass(debug.getClass().getSimpleName());
    }
    
    public static void dImport(String debug){
    	if (SHOW_DEBUG_IMPORT == true)
    		d(debug);
    }

    public static void dm(String debug, Map<String,Object> map){
    	createDebug(1,debug+"(Map):");
    	Iterator<String> keys = map.keySet().iterator();
    	while(keys.hasNext()){
    		String key = keys.next();
    		createDebug(2,key+"->"+map.get(key));
    	}
    }
    
    public static void dh(String header){
    	createDebug(0, " ");
    	createDebug(0, ".........");
    	createDebug(0, "");
    	createDebug(0, "" + header);
    	createDebug(0, "===========");
    }
    
    public static void dh2(String header){

    	createDebug(0, "");
    	createDebug(0, "" + header);
    	createDebug(0, " ------------");
    }
}
