package com.madrobot.graphics.bitmap;

import com.madrobot.geom.Rectangle;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * Configuration settings for the output bitmap when using any of the bitmap filters.
 * 
 * @author elton.stephen.kent
 * 
 */
public class OutputConfiguration {

	Bitmap.Config config = Config.ARGB_8888;
	Rectangle affectedArea;
	boolean canRecycleSrc;

	public boolean isCanRecycleSrc() {
		return canRecycleSrc;
	}

	/**
	 * Recycle the src image.
	 * <p>
	 * The default is false.
	 * </p>
	 * @param canRecycleSrc
	 */
	public void setCanRecycleSrc(boolean canRecycleSrc) {
		this.canRecycleSrc = canRecycleSrc;
	}

	/**
	 * Get the configuration of the o/p bitmap.
	 * 
	 * @return
	 */
	public Bitmap.Config getConfig() {
		return config;
	}

	/**
	 * Set the configuration of the o/p bitmap.
	 * <p>
	 * The default configuration is ARGB_8888.<br/>
	 * This field is not used if {@link #setModifySource(boolean)} is set to <code>true</code>.
	 * </p>
	 * 
	 * @param config
	 */
	public void setConfig(Bitmap.Config config) {
		this.config = config;
	}

	/**
	 * Area of the bitmap that is affected by the filter
	 * 
	 * @return
	 */
	public Rectangle getAffectedArea() {
		return affectedArea;
	}

	/**
	 * Set the area of the bitmap that should be affected by the filter.
	 * 
	 * @param affectedArea
	 *            if null, the filter is applied to the whole bitmap.
	 */
	public void setAffectedArea(Rectangle affectedArea) {
		this.affectedArea = affectedArea;
	}

	void checkRectangleBounds(int width, int height) {

		if (!new Rectangle(0, 0, width, height).contains(affectedArea)) {

			throw new IllegalArgumentException("affected area bounds exceeds the bounds of the image ");
		}
	}
}
