package de.umg.mi.idrt.ioe.OntologyTree;

import java.util.ArrayList;
import java.util.List;

import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class TransmartOntologyTreeContentProvider {

	public List<TransmartOntologyTreeItem> getStagingModel(){
			List<TransmartOntologyTreeItem> list2 = new ArrayList<TransmartOntologyTreeItem>();
			list2.add(OntologyEditorView.getTransmartTree().getRoot());
			return list2;
	}

	public List<TransmartOntologyTreeItem> getTargetModel(){
		List<TransmartOntologyTreeItem> list2 = new ArrayList<TransmartOntologyTreeItem>();
		list2.add(OntologyEditorView.getTransmartTree().getRoot());
		return list2;
	}


}
