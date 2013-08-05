package de.umg.mi.idrt.ioe.OntologyTree;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

public class OTTransferHandler extends TransferHandler{
	 
    private String data;
    
    public OTTransferHandler(){
    }
    
    public OTTransferHandler(String data){
        this.data= data;
    }
    
    public Transferable createTransferable(JComponent c){       
        //this.tid = String.valueOf(((TextbausteinTree)(BuchungenKunde.getTree().getLastSelectedPathComponent())).getTid());
        
        //Transferable tData = new TransferableData(this.data)
    	this.data = "testData";
    	//Transferable tData = new Transferable(this.data);
        return null;//tData;
    }
    
    public int getSourceActions(JComponent c){
        return COPY;
    }
 
    //nur zur Veranschaulichung welche Methode wann gerufen wird
/*
    public void exportAsDrag(JComponent comp, java.awt.event.InputEvent e, int action){
        System.out.println("exportAsDrag");
        super.exportAsDrag(comp, e, action);
    }
     
    public void exportDone(JComponent source, Transferable data, int action){
        System.out.println("exportDone");
        super.exportDone(source, data, action);
    }
*/
}
