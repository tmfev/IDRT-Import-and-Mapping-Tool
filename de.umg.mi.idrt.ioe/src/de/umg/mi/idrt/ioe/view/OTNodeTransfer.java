package de.umg.mi.idrt.ioe.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;

import de.umg.mi.idrt.ioe.OntologyTree.ViewTreeNode;


public class OTNodeTransfer extends ByteArrayTransfer {

	private static final String OTNODE_NAME = "OTNode";

	private static final int OTNODE_ID = registerType(OTNODE_NAME);

	private static OTNodeTransfer instance = new OTNodeTransfer();

	public static OTNodeTransfer getInstance() {
		return instance;
	}

	@Override
	protected int[] getTypeIds() {
		return new int[] { OTNODE_ID };
	}

	@Override
	protected String[] getTypeNames() {
		return new String[] { OTNODE_NAME };
	}

	@Override
	public void javaToNative(Object object, TransferData transferData) {
		if (!validate(object) || !isSupportedType(transferData)) {
			DND.error(DND.ERROR_INVALID_DATA);
		}
		try {
			super.javaToNative(OTNodeTransfer.getBytes(object),
					transferData);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Object nativeToJava(TransferData transferData) {
		if (!isSupportedType(transferData))
			return null;
		byte[] bs = (byte[]) super.nativeToJava(transferData);
		Object o = null;
		try {
			o = OTNodeTransfer.getObject(bs);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		return o;
	}

	@Override
	protected boolean validate(Object object) {
		if (object == null || !(object instanceof ViewTreeNode))
			return false;
		return super.validate(object);
	}

	public static byte[] getBytes(Object obj) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		oos.close();
		bos.close();
		return bos.toByteArray();
	}

	public static Object getObject(byte[] bs) throws IOException,
			ClassNotFoundException {
		Object obj;
		ByteArrayInputStream bis = new ByteArrayInputStream(bs);
		ObjectInputStream ois = new ObjectInputStream(bis);
		obj = ois.readObject();
		ois.close();
		bis.close();
		return obj;
	}

}