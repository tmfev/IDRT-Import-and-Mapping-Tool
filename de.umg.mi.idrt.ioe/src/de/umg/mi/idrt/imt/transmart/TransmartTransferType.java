package de.umg.mi.idrt.imt.transmart;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;

import de.umg.mi.idrt.idrtimporttool.importidrt.Application;

/**
 * Implements own TransferType for i2b2 Projects.
 * @author Benjamin Baum
 *
 */
public class TransmartTransferType extends ByteArrayTransfer   {

	private static final String MYTYPENAME = "transmartConfigTreeItem_Type";

	private static final int MYTYPEID = registerType(MYTYPENAME);

	private static TransmartTransferType _instance = new TransmartTransferType();

	public static TransmartTransferType getInstance() {
		return _instance;
	}

	@Override
	protected int[] getTypeIds() {
		return new int[] { MYTYPEID };
	}

	@Override
	protected String[] getTypeNames() {
		return new String[] { MYTYPENAME };
	}
	boolean checkMyType(Object object) {
		
		if (object == null || !(object instanceof TransmartConfigTreeItem)) {
			return false;
		}
		System.out.println("checking type");
		return true;
	}
	@Override
	public void javaToNative(Object object, TransferData transferData) {
	try {
		if (!checkMyType(object) || !isSupportedType(transferData)) {
			MessageDialog.openError(Application.getShell(), "Error", "You cannot drop this item here!");
			DND.error(DND.ERROR_INVALID_DATA);
		}
		TransmartConfigTreeItem server = (TransmartConfigTreeItem) object;
		
			// write data to a byte array and then ask super to convert to pMedium
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream writeOut = new ObjectOutputStream(out);
			writeOut.writeObject(server);
			byte[] buffer = out.toByteArray();
			writeOut.close();
			super.javaToNative(buffer, transferData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object nativeToJava(TransferData transferData) {
		if (isSupportedType(transferData)) {
			byte[] buffer = (byte[]) super.nativeToJava(transferData);
			if (buffer == null)
				return null;

			TransmartConfigTreeItem server;
			try {
				ByteArrayInputStream in = new ByteArrayInputStream(buffer);
				ObjectInputStream readIn = new ObjectInputStream(in);
				server = (TransmartConfigTreeItem) readIn.readObject();

				readIn.close();
			} catch (IOException | ClassNotFoundException ex) {
				ex.printStackTrace();
				return null;
			}
			return server;
		}
		System.out.println("ELSE");

		return null;
	}

}
