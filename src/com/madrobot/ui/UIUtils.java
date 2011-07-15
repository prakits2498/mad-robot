
package com.madrobot.ui;

import android.content.Context;

public class UIUtils {
	/**
	 * Returns the scaled size for different resolutions.
	 * 
	 * @param fntSize
	 * @return
	 */
	public static int getDensityIndependentSize(int size, Context ctx) {
		int density = ctx.getResources().getDisplayMetrics().densityDpi;
		if(160 == density){
			int newSize = (int) (size / (1.5));
			return newSize;
		} else if(density == 120){
			int newSize = (size / (2));
			return newSize;
		}

		return size;
	}
}
