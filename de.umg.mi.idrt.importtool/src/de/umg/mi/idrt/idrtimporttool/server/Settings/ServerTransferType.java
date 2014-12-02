package de.umg.mi.idrt.idrtimporttool.server.Settings;

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
public class ServerTransferType extends ByteArrayTransfer   {

	private static final String MYTYPENAME = "Server_Type";

	private static final int MYTYPEID = registerType(MYTYPENAME);

	private static ServerTransferType _instance = new ServerTransferType();

	public static ServerTransferType getInstance() {
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
		if (object == null || !(object instanceof Server)) {
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
		Server server = (Server) object;
		
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

			Server server;
			try {
				ByteArrayInputStream in = new ByteArrayInputStream(buffer);
				ObjectInputStream readIn = new ObjectInputStream(in);
				server = (Server) readIn.readObject();

				readIn.close();
			} catch (IOException | ClassNotFoundException ex) {
				return null;
			}
			return server;
		}

		return null;
	}

}
