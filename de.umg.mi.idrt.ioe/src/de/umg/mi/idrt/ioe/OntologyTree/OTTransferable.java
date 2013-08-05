package de.umg.mi.idrt.ioe.OntologyTree;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class OTTransferable implements Transferable{
    
    String data;
    
    public OTTransferable(String data){
        this.data= data;
    }
 
    //Gibt das Objekt zurück das die zu transferierenden Daten darstellt
    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return data;
    }
 
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] {DataFlavor.stringFlavor};
    }
 
    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(DataFlavor.stringFlavor);
    }
}