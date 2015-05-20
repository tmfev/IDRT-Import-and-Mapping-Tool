package de.umg.mi.idrt.ioe.misc;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 * Department of Medical Informatics Goettingen
 * www.mi.med.uni-goettingen.de
 */
public class ProjectEmptyException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Parameterless Constructor
    public ProjectEmptyException() {
    	super("i2b2 project empty!");
    	
    }

    //Constructor that accepts a message
    public ProjectEmptyException(String message)
    {
       super(message);
    }
    
	

}
