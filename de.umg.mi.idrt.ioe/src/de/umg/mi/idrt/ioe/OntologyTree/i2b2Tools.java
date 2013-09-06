package de.umg.mi.idrt.ioe.OntologyTree;

import de.umg.mi.idrt.ioe.Debug;

public class i2b2Tools {

	/**
	 * This method converts PIDs with alphanumeric chracters to integer-only
	 * PIDs by replacing alphabetic characters with ascii-code
	 * 
	 * @param pid
	 *            the string to convert
	 * @return the pid or new pid as an integer
	 */
	public long convertAlphanumericPIDtoNumberOld(String pid) {

		String newString = "";
		String tmpString = "";

		byte[] bytes = pid.getBytes();
		for (int x = 0; x < bytes.length; x++) {

			byte byte2 = bytes[x];
			tmpString = String.valueOf(byte2);
			Debug.d("**** " + bytes[x] + " -1:'" + tmpString + "'");
			if (tmpString.length() == 3) {
				// nothing
			} else if (tmpString.length() == 2) {
				tmpString = "0" + tmpString;
			} else if (tmpString.length() == 1) {
				tmpString = "00" + tmpString;
			} else {
				Debug.d("**** ###FEHLER convert");
			}
			newString += tmpString;
			Debug.d("**** " + bytes[x] + " -2:'" + tmpString + "'");
		}

		return 0;
	}

	public static String convertAlphanumericPIDtoNumber(String pid) {
		// Debug.f("convertAlphanumericPIDtoNumber", this);
		String newString = "";
		String tmpString = "";

		byte[] bytes = pid.getBytes();
		for (int x = 0; x < bytes.length; x++) {

			byte byte2 = bytes[x];
			tmpString = String.valueOf(byte2);
			Debug.d("**** " + bytes[x] + " -1:'" + tmpString + "'");
			if (tmpString.length() == 3) {
				// nothing
			} else if (tmpString.length() == 2) {
				tmpString = "0" + tmpString;
			} else if (tmpString.length() == 1) {
				tmpString = "00" + tmpString;
			} else {
				Debug.d("**** ###FEHLER convert");
			}
			newString += tmpString;
			Debug.d("**** " + bytes[x] + " -2:'" + tmpString + "'");
		}
		return newString;
	}

	public String deconvertAlphanumericPIDfromNumber(String pidString) {


		if (pidString.length() % 3 != 0) {
			System.err
					.println("@deconvertAlphanumericPIDfromNumber length%3 != 0");
			return "";
		}
		String byteString = "";
		for (int x = 0; x < pidString.length(); x = x + 3) {
			byteString += new String(new byte[] { Byte.parseByte(pidString
					.substring(x, x + 3)) });
		}
		return byteString;
	}

	/**
	 * convertAlphanumericPIDtoNumber: This method converts PIDs with
	 * alphanumeric chracters to integer-only PIDs by replacing alphabetic
	 * characters with ascii-code
	 * 
	 * @param pid
	 *            the string to convert
	 * @return the pid or new pid as an integer
	 */
	public String deconvertAlphanumericPIDfromNumberOld(long pid) {

		if (true) {
			// return pid.hashCode();
		}

		long newPidInt = 0;
		String newPidString = "";
		boolean isInteger = true; //
		String newPid = "";
		String pidString = Long.toString(pid);

		if (pidString.length() > 8) {
			// pid is a converted pid
			for (int i = 0; i + 1 < pidString.length(); i = i + 2) {
				if ("0".equals(pidString.charAt(i))) {
					// following char was an integer bevor converting
					newPidString += pidString.charAt(i);
				} else {
					int charInt = Integer.valueOf(pidString.charAt(i)) * 10
							+ Integer.valueOf(pidString.charAt(i + 1));
					// Debug.d( "** char[0]:'" + Integer.valueOf(
					// pidString.charAt(i) ) + "' chaar[1]:'" + Integer.valueOf(
					// pidString.charAt(i+1) ) + "'" );

					// Debug.d( "** charInt:" + charInt );
					char newChar = (char) charInt;
					newPidString += String.valueOf(newChar);

				}

			}
		} else {
			newPidString = pidString;
		}

		return newPidString;
	}

	
}
