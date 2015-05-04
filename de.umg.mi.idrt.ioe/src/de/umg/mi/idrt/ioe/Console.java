package de.umg.mi.idrt.ioe;

/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> Department of
 *         Medical Informatics Goettingen www.mi.med.uni-goettingen.de
 * 
 *         
 */

public class Console {
	
	public static void error(Exception exception){
		error(exception.getLocalizedMessage());
		exception.printStackTrace();
	}
	
	public static void error(Exception exception, boolean dialog){
		exception.printStackTrace();
		error(exception.getLocalizedMessage(), dialog);
		
	}
	
	public static void error(String text){
		System.out.println("#ERROR: " + text);

	}
	
	public static void error(String text, boolean dialog){
		error(text);
			
	}
	
	public static void error(String message, Exception exception){
		error(message + "(" + exception.getLocalizedMessage() + ")");
		exception.printStackTrace();
	}
	
	public static void info(String text){
		System.out.println("* " + text);
	}
	
	public static void info(String text, Object object){
		info(text + ": '" + object.toString() + "'");
	}
	
	public static void infoLine(String text){
		System.out.println(text);
		//System.out.print(text);
	}
	
	public static void infoNoLine(String text){
		System.out.print(text);
	}
	
	public static void message(String text){
		String border = "";
		for (int x = 0; x < text.length()-1; x++){
			border += "-";
		}
		System.out.println(" ");
		System.out.println("  /" + "-msg" + border);
		System.out.println("  | " + text + " |");
		System.out.println("  ----" + border + "/");
		System.out.println(" ");
	}
	
	public static void section(String text){
		
		String line = "";
		for (int x = 0; x < text.length()+3; x++){
			line += "*";
		}
		
		System.out.println("");
		System.out.println("/"+line);
		System.out.println("* " + text + " *");
		System.out.println(""+line+"/");
	}
	
	public static void subinfo(String text){
		System.out.println("* - " + text);
	}
	
	
	public static void subinfo(String text, Object object){
		subinfo(text + ": '" + object.toString() + "'");
	}
}
