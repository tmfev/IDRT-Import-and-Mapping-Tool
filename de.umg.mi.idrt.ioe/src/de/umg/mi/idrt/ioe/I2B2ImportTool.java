package de.umg.mi.idrt.ioe;


import java.util.Properties;
import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTree;

public class I2B2ImportTool  {

	public String programmTitle = "";
	protected Properties properties = new Properties();
	
	private MyOntologyTree myOT = null;


	/**
	 * 
	 * @param args
	 *            [0] the default input directory is input, but there is also a
	 *            parameter for den input directory
	 */
	public I2B2ImportTool(String[] args) {
	
		



		setMyOT( new MyOntologyTree() );

		
	}

	public void setMyOT (MyOntologyTree myOT){
		this.myOT = myOT;
	}
	
	public MyOntologyTree getMyOntologyTrees(){
		return this.myOT;
	}








}
