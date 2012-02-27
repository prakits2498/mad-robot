package com.madrobot.graphics.bitmap;

import com.madrobot.graphics.bitmap.BitmapFilters.BitmapMeta;

import android.graphics.Bitmap;

/**
 * Basic bitmap enhancement operations.
 * <p>
 * <b>Exposure</b>
 * <table>
 * <tr>
 * <th>Normal Image</th>
 * <th>Exposure set to 3</th>
 * </tr>
 * <tr>
 * <td><img src="../../../../resources/src.png" width="300" height="224"></td>
 * <td><img src="../../../../resources/exposure.png" width="300" height="224"></td>
 * </tr>
 * </table>
 * 
 * <b>DeSpeckle</b><br/>
 * <img src="../../../../resources/despeckle.png" width="300" height="224"><br/>
 * </p>
 */
public class EnhancementFilters {

	/**
	 * A filter which performs a simple 3x3 sharpening operation.
	 * 
	 * @param src
	 * @param edgeAction
	 *            use the EdgeAction constants defined in {@link BitmapFilters} . Recommended:
	 *            {@link BitmapFilters#CLAMP_EDGES}
	 * @param processAlpha
	 * @param premultiplyAlpha
	 * @param outputConfig
	 * @return
	 */
	public static Bitmap sharpen(Bitmap src, int edgeAction, boolean processAlpha, boolean premultiplyAlpha, Bitmap.Config outputConfig) {
		float[] sharpenMatrix = { 0.0f, -0.2f, 0.0f, -0.2f, 1.8f, -0.2f, 0.0f, -0.2f, 0.0f };
		return ConvolveUtils.doConvolve(sharpenMatrix, src, edgeAction, processAlpha, premultiplyAlpha, outputConfig);
	}

	public static Bitmap exposure(Bitmap src, float exposure, OutputConfiguration outputConfig) {

		BitmapMeta meta = BitmapFilters.getMeta(src, outputConfig);
		int[] inPixels = BitmapUtils.getPixels(src);
		int[] rTable, gTable, bTable;
		rTable = gTable = bTable = makeTable(exposure);
		int position, rgb;
		for (int y = meta.y; y < meta.targetHeight; y++) {
			for (int x = meta.x; x < meta.targetWidth; x++) {
				position = (y * meta.bitmapWidth) + x;
				rgb = inPixels[position];
				int a = rgb & 0xff000000;
				int r = (rgb >> 16) & 0xff;
				int g = (rgb >> 8) & 0xff;
				int b = rgb & 0xff;
				r = rTable[r];
				g = gTable[g];
				b = bTable[b];
				inPixels[position] = a | (r << 16) | (g << 8) | b;
			}

		}

		if (outputConfig.canRecycleSrc) {
			src.recycle();
		}
		return Bitmap.createBitmap(inPixels, meta.bitmapWidth, meta.bitmapHeight, outputConfig.config);
	}

	private static int[] makeTable(float exposure) {
		int[] table = new int[256];
		for (int i = 0; i < 256; i++)
			table[i] = com.madrobot.graphics.PixelUtils.clamp((int) (255 * transferFunction(i / 255.0f, exposure)));
		return table;
	}

	private static float transferFunction(float f, float exposure) {
		return 1 - (float) Math.exp(-f * exposure);
	}

	/**
	 * A filter which performs reduces noise by looking at each pixel's 8 neighbours, and if it's a minimum or maximum,
	 * replacing it by the next minimum or maximum of the neighbours.
	 * 
	 * @param src
	 * @param outputConfig
	 * @return
	 */
	public static Bitmap reduceNoise(Bitmap src, Bitmap.Config outputConfig) {
		int width = src.getWidth();
		int height = src.getHeight();
		int index = 0;
		int[] r = new int[9];
		int[] g = new int[9];
		int[] b = new int[9];
		int[] inPixels = BitmapUtils.getPixels(src);
		int[] outPixels = new int[inPixels.length];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int k = 0;
				int irgb = inPixels[index];
				int ir = (irgb >> 16) & 0xff;
				int ig = (irgb >> 8) & 0xff;
				int ib = irgb & 0xff;
				for (int dy = -1; dy <= 1; dy++) {
					int iy = y + dy;
					if (0 <= iy && iy < height) {
						int ioffset = iy * width;
						for (int dx = -1; dx <= 1; dx++) {
							int ix = x + dx;
							if (0 <= ix && ix < width) {
								int rgb = inPixels[ioffset + ix];
								r[k] = (rgb >> 16) & 0xff;
								g[k] = (rgb >> 8) & 0xff;
								b[k] = rgb & 0xff;
							} else {
								r[k] = ir;
								g[k] = ig;
								b[k] = ib;
							}
							k++;
						}
					} else {
						for (int dx = -1; dx <= 1; dx++) {
							r[k] = ir;
							g[k] = ig;
							b[k] = ib;
							k++;
						}
					}
				}
				outPixels[index] = (inPixels[index] & 0xff000000) | (smooth(r) << 16) | (smooth(g) << 8) | smooth(b);
				index++;
			}
		}
		return Bitmap.createBitmap(outPixels, src.getWidth(), src.getHeight(), outputConfig);
	}

	private static int smooth(int[] v) {
		int minindex = 0, maxindex = 0, min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;

		for (int i = 0; i < 9; i++) {
			if (i != 4) {
				if (v[i] < min) {
					min = v[i];
					minindex = i;
				}
				if (v[i] > max) {
					max = v[i];
					maxindex = i;
				}
			}
		}
		if (v[4] < min)
			return v[minindex];
		if (v[4] > max)
			return v[maxindex];
		return v[4];
	}

	/**
	 * A filter which removes noise from an image using a "pepper and salt" algorithm.
	 * 
	 * @param src
	 * @param outputConfig
	 * @return
	 */
	public static Bitmap deSpeckle(Bitmap src, Bitmap.Config outputConfig) {
		int width = src.getWidth();
		int height = src.getHeight();
		int[] inPixels = BitmapUtils.getPixels(src);
		int[] outPixels = new int[inPixels.length];

		int index = 0;
		short[][] r = new short[3][width];
		short[][] g = new short[3][width];
		short[][] b = new short[3][width];

		for (int x = 0; x < width; x++) {
			int rgb = inPixels[x];
			r[1][x] = (short) ((rgb >> 16) & 0xff);
			g[1][x] = (short) ((rgb >> 8) & 0xff);
			b[1][x] = (short) (rgb & 0xff);
		}
		for (int y = 0; y < height; y++) {
			boolean yIn = y > 0 && y < height - 1;
			int nextRowIndex = index + width;
			if (y < height - 1) {
				for (int x = 0; x < width; x++) {
					int rgb = inPixels[nextRowIndex++];
					r[2][x] = (short) ((rgb >> 16) & 0xff);
					g[2][x] = (short) ((rgb >> 8) & 0xff);
					b[2][x] = (short) (rgb & 0xff);
				}
			}
			for (int x = 0; x < width; x++) {
				boolean xIn = x > 0 && x < width - 1;
				short or = r[1][x];
				short og = g[1][x];
				short ob = b[1][x];
				int w = x - 1;
				int e = x + 1;

				if (yIn) {
					or = pepperAndSalt(or, r[0][x], r[2][x]);
					og = pepperAndSalt(og, g[0][x], g[2][x]);
					ob = pepperAndSalt(ob, b[0][x], b[2][x]);
				}

				if (xIn) {
					or = pepperAndSalt(or, r[1][w], r[1][e]);
					og = pepperAndSalt(og, g[1][w], g[1][e]);
					ob = pepperAndSalt(ob, b[1][w], b[1][e]);
				}

				if (yIn && xIn) {
					or = pepperAndSalt(or, r[0][w], r[2][e]);
					og = pepperAndSalt(og, g[0][w], g[2][e]);
					ob = pepperAndSalt(ob, b[0][w], b[2][e]);

					or = pepperAndSalt(or, r[2][w], r[0][e]);
					og = pepperAndSalt(og, g[2][w], g[0][e]);
					ob = pepperAndSalt(ob, b[2][w], b[0][e]);
				}

				outPixels[index] = (inPixels[index] & 0xff000000) | (or << 16) | (og << 8) | ob;
				index++;
			}
			short[] t;
			t = r[0];
			r[0] = r[1];
			r[1] = r[2];
			r[2] = t;
			t = g[0];
			g[0] = g[1];
			g[1] = g[2];
			g[2] = t;
			t = b[0];
			b[0] = b[1];
			b[1] = b[2];
			b[2] = t;
		}

		return Bitmap.createBitmap(outPixels, src.getWidth(), src.getHeight(), outputConfig);
	}

	private static short pepperAndSalt(short c, short v1, short v2) {
		if (c < v1)
			c++;
		if (c < v2)
			c++;
		if (c > v1)
			c--;
		if (c > v2)
			c--;
		return c;
	}
}
