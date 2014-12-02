package de.umg.mi.idrt.idrtimporttool.server.Settings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;

import de.umg.mi.idrt.idrtimporttool.importidrt.Application;

/**
 * Implements own TransferType for i2b2 Projects.
 * @author Benjamin Baum
 *
 */
public class I2b2ProjectTransferType extends ByteArrayTransfer   {

	private static final String MYTYPENAME = "I2b2Project_Type";

	private static final int MYTYPEID = registerType(MYTYPENAME);

	private static I2b2ProjectTransferType _instance = new I2b2ProjectTransferType();

	public static I2b2ProjectTransferType getInstance() {
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
		if (object == null || !(object instanceof I2b2Project)) {
			return false;
		}
		return true;
	}
	@Override
	public void javaToNative(Object object, TransferData transferData) {
	try {
		if (!checkMyType(object) || !isSupportedType(transferData)) {
			MessageDialog.openError(Application.getShell(), "Error", "You cannot drop this item here!");
			DND.error(DND.ERROR_INVALID_DATA);
		}
		I2b2Project server = (I2b2Project) object;
		
			// write data to a byte array and then ask super to convert to pMedium
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream writeOut = new ObjectOutputStream(out);
			writeOut.writeObject(server);
			byte[] buffer = out.toByteArray();
			writeOut.close();
			super.javaToNative(buffer, transferData);
		} catch (IOException e) {
			
		}
	}

	@Override
	public Object nativeToJava(TransferData transferData) {
		if (isSupportedType(transferData)) {
			byte[] buffer = (byte[]) super.nativeToJava(transferData);
			if (buffer == null)
				return null;

			I2b2Project server;
			try {
				ByteArrayInputStream in = new ByteArrayInputStream(buffer);
				ObjectInputStream readIn = new ObjectInputStream(in);
				server = (I2b2Project) readIn.readObject();

				readIn.close();
			} catch (IOException | ClassNotFoundException ex) {
				return null;
			}
			return server;
		}

		return null;
	}

}
