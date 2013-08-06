package de.umg.mi.idrt.ioe;

public class Console {
	
	public Console(){
		}

	public static void section(String text){
		
		String line = "";
		for (int x = 0; x < text.length()+3; x++){
			line += "*";
		}
		
		Debug.d("");
		Debug.d("/"+line);
		Debug.d("* " + text + " *");
		Debug.d(""+line+"/");
	}
	
	public static void info(String text){
		Debug.d("* " + text);
		System.out.println("* " + text);
	}
	
	public static void info(String text, Object object){
		info(text + ": '" + object.toString() + "'");
	}
	
	public static void subinfo(String text){
		Debug.d("* - " + text);
	}
	
	public static void subinfo(String text, Object object){
		subinfo(text + ": '" + object.toString() + "'");
	}
	
	public static void infoLine(String text){
		Debug.d(text);
		System.out.print(text);
	}
	
	public static void infoNoLine(String text){
		System.out.print(text);
	}
	
	public static void error(String text){
		Debug.e("#ERROR: " + text);

	}
	
	public static void error(String text, boolean dialog){
		error(text);
			
	}
	
	public static void error(String message, Exception exception){
		error(message + "(" + exception.getLocalizedMessage() + ")");
		exception.printStackTrace();
	}
	
	public static void error(Exception exception){
		error(exception.getLocalizedMessage());
		exception.printStackTrace();
	}
	
	public static void error(Exception exception, boolean dialog){
		exception.printStackTrace();
		error(exception.getLocalizedMessage(), dialog);
		
	}
	
	
	public static void message(String text){
		String border = "";
		for (int x = 0; x < text.length()-1; x++){
			border += "-";
		}
		Debug.d(0, " ");
		Debug.d(0, "  /" + "-msg" + border);
		Debug.d(0, "  | " + text + " |");
		Debug.d(0, "  ----" + border + "/");
		Debug.d(0, " ");
	}
}
