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
package com.madrobot.math;

public class MathUtils {
	/**
	 * Equivalent to Math.max(low, Math.min(high, amount));
	 */
	public static float constrain(final float amount, final float low,
			final float high) {
		return amount < low ? low : amount > high ? high : amount;
	}

	/**
	 * Equivalent to Math.max(low, Math.min(high, amount));
	 */
	public static int constrain(final int amount, final int low, final int high) {
		return amount < low ? low : amount > high ? high : amount;
	}

	/**
	 * Return logarithm to base 10.
	 * 
	 * @param x
	 *            Argument to take logarithm from (x>0)
	 */

	public static final double log10(double x) throws IllegalArgumentException {
		if (x <= 0)
			throw new IllegalArgumentException();
		else {
			double LN10 = Math.log(10.0);
			return Math.log(x) / LN10;
		}
	}

	/**
	 * Rounds the given double value
	 * 
	 * @param value
	 *            the value
	 * @return the rounded value, x.5 and higher is rounded to x + 1.
	 * @since CLDC 1.1
	 */
	public static long round(double value) {
		if (value < 0) {
			return (long) (value - 0.5);
		} else {
			return (long) (value + 0.5);
		}
	}

	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	/**
	 * Computes a float from mantissa and exponent.
	 */
	public static float buildFloat(int mant, int exp) {
		if (exp < -125 || mant == 0) {
			return 0.0f;
		}

		if (exp >= 128) {
			return (mant > 0) ? Float.POSITIVE_INFINITY
					: Float.NEGATIVE_INFINITY;
		}

		if (exp == 0) {
			return mant;
		}

		if (mant >= (1 << 26)) {
			mant++; // round up trailing bits if they will be dropped.
		}

		return (float) ((exp > 0) ? mant * pow10[exp] : mant / pow10[-exp]);
	}

	/**
	 * Array of powers of ten. Using double instead of float gives a tiny bit
	 * more precision.
	 */
	private static final double[] pow10 = new double[128];
	static {
		for (int i = 0; i < pow10.length; i++) {
			pow10[i] = Math.pow(10, i);
		}
	}
}
