/*******************************************************************************
 * Copyright (c) 2011 MadRobot.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *  Elton Kent - initial API and implementation
 ******************************************************************************/
package com.madrobot.util;

public final class PrimitiveUtils {
	public static byte[] toByteArray(final double data) {
		return toByta(Double.doubleToRawLongBits(data));
	}

	private static byte[] toByta(final long data) {
		return new byte[] { (byte) (data >> 56 & 0xff),
				(byte) (data >> 48 & 0xff), (byte) (data >> 40 & 0xff),
				(byte) (data >> 32 & 0xff), (byte) (data >> 24 & 0xff),
				(byte) (data >> 16 & 0xff), (byte) (data >> 8 & 0xff),
				(byte) (data >> 0 & 0xff), };
	}

	public static double toDouble(final byte[] data) {
		if (data == null || data.length != 8)
			return 0x0;
		// ---------- simple:
		return Double.longBitsToDouble(toLong(data));
	}

	private static long toLong(final byte[] data) {
		if (data == null || data.length != 8)
			return 0x0;
		// ----------
		return (long) (0xff & data[0]) << 56 | (long) (0xff & data[1]) << 48
				| (long) (0xff & data[2]) << 40 | (long) (0xff & data[3]) << 32
				| (long) (0xff & data[4]) << 24 | (long) (0xff & data[5]) << 16
				| (long) (0xff & data[6]) << 8 | (long) (0xff & data[7]) << 0;
	}

}
