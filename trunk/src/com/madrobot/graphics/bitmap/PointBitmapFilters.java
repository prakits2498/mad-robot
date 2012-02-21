package com.madrobot.graphics.bitmap;

import java.util.Random;

import android.graphics.Bitmap;

/**
 * Point based bitmap filters
 * <p>
 * <b>Fade</b><br/>
 * Fade using the <code>fadeWidth</code> of 100<br/>
 * <img src="../../../../resources/fade.png"><br/>
 * </p>
 * 
 * @author elton.stephen.kent
 * 
 */
public class PointBitmapFilters {

	/**
	 * Creates a sparkle effect from the middle of the bitmap
	 * 
	 * @param src
	 * @param color
	 * @param amount
	 * @param rays
	 * @param radius
	 *            Set the radius of the effect.
	 * @param randomness
	 * @param outputConfig
	 * @return
	 */
	public static Bitmap sparkle(Bitmap src, int color, int amount, int rays, int radius, int randomness, Bitmap.Config outputConfig) {
		int width = src.getWidth();
		int height = src.getHeight();
		int[] inPixels = BitmapUtils.getPixels(src);

		Random randomNumbers = new Random();
		int centreX = width / 2;
		int centreY = height / 2;
		randomNumbers.setSeed(371);
		float[] rayLengths = new float[rays];
		for (int i = 0; i < rays; i++)
			rayLengths[i] = radius + randomness / 100.0f * radius * (float) randomNumbers.nextGaussian();

		for (int y = 0; y < height; y++) {
			int nRow = y * width;
			for (int x = 0; x < width; x++) {
				int rgb = inPixels[nRow + x];

				float dx = x - centreX;
				float dy = y - centreY;
				float distance = dx * dx + dy * dy;
				float angle = (float) Math.atan2(dy, dx);
				float d = (angle + ImageMath.PI) / (ImageMath.TWO_PI) * rays;
				int i = (int) d;
				float f = d - i;

				if (radius != 0) {
					float length = ImageMath.lerp(f, rayLengths[i % rays], rayLengths[(i + 1) % rays]);
					float g = length * length / (distance + 0.0001f);
					g = (float) Math.pow(g, (100 - amount) / 50.0);
					f -= 0.5f;
					// f *= amount/50.0f;
					f = 1 - f * f;
					f *= g;
				}
				f = ImageMath.clamp(f, 0, 1);
				inPixels[nRow + x] = ImageMath.mixColors(f, rgb, color);
			}
		}
		return Bitmap.createBitmap(inPixels, src.getWidth(), src.getHeight(), outputConfig);
	}

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
	public static Bitmap fade(Bitmap src, float angle, float fadeStart, float fadeWidth, boolean invert, int sides, Bitmap.Config outputConfig) {
		int width = src.getWidth();
		int height = src.getHeight();
		int[] inPixels = BitmapUtils.getPixels(src);
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		float m00 = cos;
		float m01 = sin;
		float m10 = -sin;
		float m11 = cos;

		for (int y = 0; y < height; y++) {
			int nRow = y * width;
			for (int x = 0; x < width; x++) {
				int rgb = inPixels[nRow + x];

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

				inPixels[nRow + x] = (alpha << 24) | (rgb & 0x00ffffff);
			}
		}
		return Bitmap.createBitmap(inPixels, src.getWidth(), src.getHeight(), outputConfig);
	}

	private static float symmetry(float x, float b) {

		x = ImageMath.mod(x, 2 * b);
		if (x > b)
			return 2 * b - x;
		return x;
	}



}
