package com.madrobot.security;

public class SecurityUtils {

	/**
	 * Converts a array of bytes to hex
	 * 
	 * @param data
	 * @return
	 */
	public static String convertToHex(final byte[] data) {
		final StringBuilder buf = new StringBuilder();
		final int mask = 0x0F, ten = 10, nine = 9, shiftLength = 4;
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> shiftLength) & mask;
			int twoHalfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= nine)) {
					buf.append((char) ('0' + halfbyte));
				} else {
					buf.append((char) ('a' + (halfbyte - ten)));
				}
				halfbyte = data[i] & mask;
			} while (twoHalfs++ < 1);
		}
		return buf.toString();
	}
}
