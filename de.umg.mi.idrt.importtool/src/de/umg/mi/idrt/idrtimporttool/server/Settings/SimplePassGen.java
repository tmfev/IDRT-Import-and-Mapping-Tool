package de.umg.mi.idrt.idrtimporttool.server.Settings;

import java.util.Random;

/**
 * A simple password generator
 */
public class SimplePassGen {

	/**
	 * Random object for choosing and shuffling characters from StringBuffers
	 */
	private static Random random = new Random();

	/**
	 * Creates a String of the given length, containing random characters from
	 * the given alphabets, but at least one character from each alphabet. If
	 * there are more than 'length' alphabets, one random character is chosen
	 * from each of the 'length' first alphabets. <br />
	 * <br />
	 * At least one alphabet must be given. If an empty String or
	 * <code>null</code> is encountered as an alphabet, an
	 * IllegalArgumentException is thrown.
	 * 
	 * @param length
	 *            The length of the string to generate
	 * @param alphabets
	 *            The alphabets to use
	 * @return The generated string
	 * @throws IllegalArgumentException
	 *             if not alphabets are given, or an empty of <code>null</code>
	 *             alphabet is encountered
	 */
	public static String generate(int length, String... alphabets) {
		if (alphabets.length == 0) {
			throw new IllegalArgumentException(
					"At least one alphabet must be given");
		}
		StringBuffer result = new StringBuffer();
		StringBuffer all = new StringBuffer();
		for (String alphabet : alphabets) {
			if ((alphabet == null) || alphabet.equals("")) {
				throw new IllegalArgumentException("Invalid alphabet: "
						+ alphabet);
			}
			StringBuffer sb = new StringBuffer(alphabet);
			result.append(selectRandom(sb, 1));
			if (result.length() == length) {
				return shuffle(result).toString();
			}
			all.append(sb);
		}
		result.append(selectRandom(all, length - result.length()));
		return shuffle(result).toString();
	}

	/**
	 * Generates a string with the given length, that contains at least one
	 * random digit (if length > 0), at least one random special character (if
	 * length > 1), and possibly (but not necessarily) random characters from
	 * the given alphabet.
	 * 
	 * @param alphabet
	 *            The alphabet to choose characters from
	 * @param length
	 *            The length of the string to generate
	 * @return The generated String
	 */
	public static String generate(String alphabet, int length) {
		String digits = "0123456789";
		// String special = "!\"#$%&'()*+,-./:;<=>?@";
		return generate(length, digits, alphabet);
	}

	/**
	 * Returns a StringBuffer containing 'n' characters chosen randomly from the
	 * given alphabet.
	 * 
	 * @param alphabet
	 *            The alphabet to choose from
	 * @param n
	 *            The number of characters to choose
	 * @return A StringBufer with 'n' characters chosen randomly from the given
	 *         alphabet
	 */
	private static StringBuffer selectRandom(StringBuffer alphabet, int n) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < n; i++) {
			sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
		}
		return sb;
	}

	/**
	 * Shuffles the given StringBuffer in place and returns it
	 * 
	 * @param sb
	 *            The StringBuffer to shuffle and return
	 * @return The given StringBuffer, shuffled randomly
	 */
	private static StringBuffer shuffle(StringBuffer sb) {
		for (int i = 0; i < sb.length(); i++) {
			int i0 = random.nextInt(sb.length());
			int i1 = random.nextInt(sb.length());
			char c = sb.charAt(i0);
			sb.setCharAt(i0, sb.charAt(i1));
			sb.setCharAt(i1, c);
		}
		return sb;
	}

}