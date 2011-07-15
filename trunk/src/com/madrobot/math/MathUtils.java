
package com.madrobot.math;

public class MathUtils {
	public static double round(double value, int places) {
		if(places < 0){
			throw new IllegalArgumentException();
		}

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
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
		if(value < 0){
			return (long) (value - 0.5);
		} else{
			return (long) (value + 0.5);
		}
	}
}
