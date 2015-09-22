package de.umg.mi.idrt.imt.transmart;

import java.util.ArrayList;
import java.util.List;


public class TransmartConfigTreeModelProvider {
	public List<TransmartConfigTreeItem> getModel(){
		List<TransmartConfigTreeItem> list2 = new ArrayList<TransmartConfigTreeItem>();
		list2.add(TransmartConfigTree.getRoot());
		return list2;
}
}
