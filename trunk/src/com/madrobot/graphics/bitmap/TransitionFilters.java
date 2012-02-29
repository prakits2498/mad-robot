package com.madrobot.graphics.bitmap;

import java.util.Random;

import android.graphics.Bitmap;

/**
 * Simple transition filters
 * 
 * <p>
 * <b>Fade</b><br/>
 * Fade using the <code>fadeWidth</code> of 100<br/>
 * <img src="../../../../resources/fade.png"><br/>
 * </p>
 * 
 * @author elton.stephen.kent
 * 
 */
public class TransitionFilters {

	/**
	 * Performs fading effect on the width specified
	 * 
	 * @param src
	 * @param angle
	 * @param fadeStart
	 * @param fadeWidth
	 *            width of the image that should be faded
	 * @param invert
	 *            the specified bitmap width is inverted in the fade
	 * @param sides
	 *            number of sides. recommended:0
	 * @param outputConfig
	 * @return
	 */
	public static Bitmap fade(Bitmap src, float angle, float fadeStart, float fadeWidth, boolean invert, int sides, OutputConfiguration outputConfig) {

		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		float m00 = cos;
		float m01 = sin;
		float m10 = -sin;
		float m11 = cos;

		com.madrobot.graphics.bitmap.OutputConfiguration.BitmapMeta meta = outputConfig.getBitmapMeta(src);
		int[] inPixels = BitmapUtils.getPixels(src);
		int position, rgb;
		for (int y = meta.y; y < meta.targetHeight; y++) {
			for (int x = meta.x; x < meta.targetWidth; x++) {
				position = (y * meta.bitmapWidth) + x;
				rgb = inPixels[position];
				float nx = m00 * x + m01 * y;
				float ny = m10 * x + m11 * y;
				if (sides == 2)
					nx = (float) Math.sqrt(nx * nx + ny * ny);
				else if (sides == 3)
					nx = ImageMath.mod(nx, 16);
				else if (sides == 4)
					nx = symmetry(nx, 16);
				int alpha = (int) (ImageMath.smoothStep(fadeStart, fadeStart + fadeWidth, nx) * 255);
				if (invert)
					alpha = 255 - alpha;

				inPixels[position] = (alpha << 24) | (rgb & 0x00ffffff);
			}
		}
		return Bitmap.createBitmap(inPixels, meta.bitmapWidth, meta.bitmapHeight, outputConfig.config);
	}

	private static float symmetry(float x, float b) {

		x = ImageMath.mod(x, 2 * b);
		if (x > b)
			return 2 * b - x;
		return x;
	}

	/**
	 * A filter which "dissolves" an image by thresholding the alpha channel with random numbers.
	 * 
	 * @param src
	 * @param density
	 *            the density of the image in the range 0..1.
	 * @param softness
	 *            the softness of the dissolve in the range 0..1.
	 * @param outputConfig
	 * @return
	 */
	public static Bitmap dissolve(Bitmap src, float density, float softness, OutputConfiguration outputConfig) {
		float d = (1 - density) * (1 + softness);
		float minDensity = d - softness;
		float maxDensity = d;
		Random randomNumbers = new Random(0);
		com.madrobot.graphics.bitmap.OutputConfiguration.BitmapMeta meta = outputConfig.getBitmapMeta(src);
		int[] inPixels = BitmapUtils.getPixels(src);
		int position, rgb, a;
		float v, f;
		for (int y = meta.y; y < meta.targetHeight; y++) {
			for (int x = meta.x; x < meta.targetWidth; x++) {
				position = (y * meta.bitmapWidth) + x;
				rgb = inPixels[position];
				a = (rgb >> 24) & 0xff;
				v = randomNumbers.nextFloat();
				f = ImageMath.smoothStep(minDensity, maxDensity, v);
				inPixels[position] = ((int) (a * f) << 24) | rgb & 0x00ffffff;
			}
		}
		return Bitmap.createBitmap(inPixels, meta.bitmapWidth, meta.bitmapHeight, outputConfig.config);
	}

}
