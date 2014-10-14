package routines;

import java.util.HashMap;

/*
 * user specification: the function's comment should contain keys as follows: 1. write about the function's comment.but
 * it must be before the "{talendTypes}" key.
 * 
 * 2. {talendTypes} 's value must be talend Type, it is required . its value should be one of: String, char | Character,
 * long | Long, int | Integer, boolean | Boolean, byte | Byte, Date, double | Double, float | Float, Object, short |
 * Short
 * 
 * 3. {Category} define a category for the Function. it is required. its value is user-defined .
 * 
 * 4. {param} 's format is: {param} <type>[(<default value or closed list values>)] <name>[ : <comment>]
 * 
 * <type> 's value should be one of: string, int, list, double, object, boolean, long, char, date. <name>'s value is the
 * Function's parameter name. the {param} is optional. so if you the Function without the parameters. the {param} don't
 * added. you can have many parameters for the Function.
 * 
 * 5. {example} gives a example for the Function. it is optional.
 */
public class SIC_old {
	
	private String center;
//	private Visit visit;
	private HashMap<String, Visit> visitMap;
	
	private String sic;
	
	public void addVisit(String visit, String date, boolean inChili){
		visitMap.put(visit, new Visit(visit, date, inChili));
	}
	
	public SIC_old(String sic, String date, String center, String visit, boolean inChili ) {
	
			visitMap = new HashMap<String, Visit>();
		
		this.setCenter(center);
		this.setSic(sic);
		visitMap.put(visit,new Visit(visit, date, inChili));
	}
	
	public HashMap<String, Visit> getVisitMap() {
		return visitMap;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public String getCenter() {
		return center;
	}

	public void setSic(String sic) {
		this.sic = sic;
	}

	public String getSic() {
		return sic;
	}
	
	@Override
	public String toString() {
	return sic + " " + visitMap;
	}
}
