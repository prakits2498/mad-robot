package com.madrobot.graphics.bitmap;

import java.util.Random;

import android.graphics.Bitmap;

import com.madrobot.geom.Rectangle;
import com.madrobot.graphics.PixelUtils;

public class WholeImageBitmapFilters {

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

	/**
	 * 
	 * @param src
	 * @param turbulence
	 *            Specifies the turbulence of the texture. Min Value: 0,Max value: 10. recommended:1.0
	 * @param useImageColors
	 *            recommended: false.
	 * @param useColormap
	 *            Use color map for this filter. recommended:false.
	 * @param outputConfig
	 * @return
	 */
	public static Bitmap plasma(Bitmap src, float turbulence, boolean useImageColors, boolean useColormap, Bitmap.Config outputConfig) {
		int width = src.getWidth();
		int height = src.getHeight();
		int[] inPixels = BitmapUtils.getPixels(src);
		int[] outPixels = new int[inPixels.length];
		Random randomGenerator = new Random();
		randomGenerator.setSeed(System.currentTimeMillis());
		Colormap colormap = new LinearColormap();
		Rectangle originalSpace = new Rectangle(0, 0, width, height);
		int w1 = width - 1;
		int h1 = height - 1;
		PixelUtils.putPixel(0, 0, randomRGB(inPixels, 0, 0, useImageColors, originalSpace, randomGenerator), outPixels,
				width);
		PixelUtils.putPixel(w1, 0, randomRGB(inPixels, w1, 0, useImageColors, originalSpace, randomGenerator),
				outPixels, width);
		PixelUtils.putPixel(0, h1, randomRGB(inPixels, 0, h1, useImageColors, originalSpace, randomGenerator),
				outPixels, width);
		PixelUtils.putPixel(w1, h1, randomRGB(inPixels, w1, h1, useImageColors, originalSpace, randomGenerator),
				outPixels, width);
		PixelUtils.putPixel(w1 / 2, h1 / 2,
				randomRGB(inPixels, w1 / 2, h1 / 2, useImageColors, originalSpace, randomGenerator), outPixels, width);
		PixelUtils.putPixel(0, h1 / 2, randomRGB(inPixels, 0, h1 / 2, useImageColors, originalSpace, randomGenerator),
				outPixels, width);
		PixelUtils.putPixel(w1, h1 / 2,
				randomRGB(inPixels, w1, h1 / 2, useImageColors, originalSpace, randomGenerator), outPixels, width);
		PixelUtils.putPixel(w1 / 2, 0, randomRGB(inPixels, w1 / 2, 0, useImageColors, originalSpace, randomGenerator),
				outPixels, width);
		PixelUtils.putPixel(w1 / 2, h1,
				randomRGB(inPixels, w1 / 2, h1, useImageColors, originalSpace, randomGenerator), outPixels, width);
		int depth = 1;
		while (doPixel(0, 0, width - 1, height - 1, outPixels, width, depth, 0, turbulence))
			depth++;

		if (useColormap && colormap != null) {
			int index = 0;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					outPixels[index] = colormap.getColor((outPixels[index] & 0xff) / 255.0f);
					index++;
				}
			}
		}
		return Bitmap.createBitmap(outPixels, src.getWidth(), src.getHeight(), outputConfig);
	}

	private static boolean doPixel(int x1, int y1, int x2, int y2, int[] pixels, int stride, int depth, int scale, float turbulence) {
		int mx, my;

		if (depth == 0) {
			int ml, mr, mt, mb, mm, t;

			int tl = PixelUtils.getPixel(x1, y1, pixels, stride);
			int bl = PixelUtils.getPixel(x1, y2, pixels, stride);
			int tr = PixelUtils.getPixel(x2, y1, pixels, stride);
			int br = PixelUtils.getPixel(x2, y2, pixels, stride);

			float amount = (256.0f / (2.0f * scale)) * turbulence;

			mx = (x1 + x2) / 2;
			my = (y1 + y2) / 2;

			if (mx == x1 && mx == x2 && my == y1 && my == y2)
				return true;

			if (mx != x1 || mx != x2) {
				ml = PixelUtils.average(tl, bl);
				ml = PixelUtils.displace(ml, amount);
				PixelUtils.putPixel(x1, my, ml, pixels, stride);

				if (x1 != x2) {
					mr = PixelUtils.average(tr, br);
					mr = PixelUtils.displace(mr, amount);
					PixelUtils.putPixel(x2, my, mr, pixels, stride);
				}
			}

			if (my != y1 || my != y2) {
				if (x1 != mx || my != y2) {
					mb = PixelUtils.average(bl, br);
					mb = PixelUtils.displace(mb, amount);
					PixelUtils.putPixel(mx, y2, mb, pixels, stride);
				}

				if (y1 != y2) {
					mt = PixelUtils.average(tl, tr);
					mt = PixelUtils.displace(mt, amount);
					PixelUtils.putPixel(mx, y1, mt, pixels, stride);
				}
			}

			if (y1 != y2 || x1 != x2) {
				mm = PixelUtils.average(tl, br);
				t = PixelUtils.average(bl, tr);
				mm = PixelUtils.average(mm, t);
				mm = PixelUtils.displace(mm, amount);
				PixelUtils.putPixel(mx, my, mm, pixels, stride);
			}

			if (x2 - x1 < 3 && y2 - y1 < 3)
				return false;
			return true;
		}

		mx = (x1 + x2) / 2;
		my = (y1 + y2) / 2;

		doPixel(x1, y1, mx, my, pixels, stride, depth - 1, scale + 1, turbulence);
		doPixel(x1, my, mx, y2, pixels, stride, depth - 1, scale + 1, turbulence);
		doPixel(mx, y1, x2, my, pixels, stride, depth - 1, scale + 1, turbulence);
		return doPixel(mx, my, x2, y2, pixels, stride, depth - 1, scale + 1, turbulence);
	}

	private static int randomRGB(int[] inPixels, int x, int y, boolean useImageColors, Rectangle originalSpace, Random randomGenerator) {
		if (useImageColors) {
			return inPixels[y * originalSpace.width + x];
		} else {
			int r = (int) (255 * randomGenerator.nextFloat());
			int g = (int) (255 * randomGenerator.nextFloat());
			int b = (int) (255 * randomGenerator.nextFloat());
			return 0xff000000 | (r << 16) | (g << 8) | b;
		}
	}

	/**
	 * A filter which produces a "oil-painting" effect.
	 * <p>
	 * Extremely CPU intensive. Run on dedicated thread.<br/>
	 *  <p>
	 * Source<br/>
	 * <img src="../../../../resources/src.png"><br/>
	 * Output<br/>
	 * <img src="../../../../resources/wholeimage_oilpaint.png"><br/>
	 * </p>
	 * </p>
	 * 
	 * @param src
	 * @param range
	 *            Range of effect in pixels. Recommended:3.
	 * @param levels
	 *            Set the number of levels for the effect. Recommended:256
	 * @param outputConfig
	 * @return
	 */
	public static Bitmap oilPaint(Bitmap src, int range, int levels, Bitmap.Config outputConfig) {
		int width = src.getWidth();
		int height = src.getHeight();
		int[] inPixels = BitmapUtils.getPixels(src);
		int[] outPixels = new int[inPixels.length];
		int index = 0;
		int[] rHistogram = new int[levels];
		int[] gHistogram = new int[levels];
		int[] bHistogram = new int[levels];
		int[] rTotal = new int[levels];
		int[] gTotal = new int[levels];
		int[] bTotal = new int[levels];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				for (int i = 0; i < levels; i++)
					rHistogram[i] = gHistogram[i] = bHistogram[i] = rTotal[i] = gTotal[i] = bTotal[i] = 0;

				for (int row = -range; row <= range; row++) {
					int iy = y + row;
					int ioffset;
					if (0 <= iy && iy < height) {
						ioffset = iy * width;
						for (int col = -range; col <= range; col++) {
							int ix = x + col;
							if (0 <= ix && ix < width) {
								int rgb = inPixels[ioffset + ix];
								int r = (rgb >> 16) & 0xff;
								int g = (rgb >> 8) & 0xff;
								int b = rgb & 0xff;
								int ri = r * levels / 256;
								int gi = g * levels / 256;
								int bi = b * levels / 256;
								rTotal[ri] += r;
								gTotal[gi] += g;
								bTotal[bi] += b;
								rHistogram[ri]++;
								gHistogram[gi]++;
								bHistogram[bi]++;
							}
						}
					}
				}

				int r = 0, g = 0, b = 0;
				for (int i = 1; i < levels; i++) {
					if (rHistogram[i] > rHistogram[r])
						r = i;
					if (gHistogram[i] > gHistogram[g])
						g = i;
					if (bHistogram[i] > bHistogram[b])
						b = i;
				}
				r = rTotal[r] / rHistogram[r];
				g = gTotal[g] / gHistogram[g];
				b = bTotal[b] / bHistogram[b];
				outPixels[index] = (inPixels[index] & 0xff000000) | (r << 16) | (g << 8) | b;
				index++;
			}
		}
		return Bitmap.createBitmap(outPixels, src.getWidth(), src.getHeight(), outputConfig);
	}

	private final static float R2 = (float) Math.sqrt(2);
	public final static float[] ROBERTS_V = { 0, 0, -1, 0, 1, 0, 0, 0, 0, };
	public final static float[] ROBERTS_H = { -1, 0, 0, 0, 1, 0, 0, 0, 0, };
	public final static float[] PREWITT_V = { -1, 0, 1, -1, 0, 1, -1, 0, 1, };
	public final static float[] PREWITT_H = { -1, -1, -1, 0, 0, 0, 1, 1, 1, };
	public final static float[] SOBEL_V = { -1, 0, 1, -2, 0, 2, -1, 0, 1, };
	public static float[] SOBEL_H = { -1, -2, -1, 0, 0, 0, 1, 2, 1, };
	public final static float[] FREI_CHEN_V = { -1, 0, 1, -R2, 0, R2, -1, 0, 1, };
	public static float[] FREI_CHEN_H = { -1, -R2, -1, 0, 0, 0, 1, R2, 1, };
	
	/**
	 * An edge-detection filter.
	 * <p>
	 * Source<br/>
	 * <img src="../../../../resources/src.png"><br/>
	 * Output<br/>
	 * <img src="../../../../resources/wholeimage_edge.png"><br/>
	 * </p>
	 * @param src
	 * @param vEdgeMatrix can be  {@link #ROBERTS_V}, {@link #PREWITT_V},{@link #SOBEL_V} or {@link #FREI_CHEN_V}. recommended: {@link #SOBEL_V}
	 * @param hEdgeMatrix can be  {@link #ROBERTS_H}, {@link #PREWITT_H},{@link #SOBEL_H} or {@link #FREI_CHEN_H}. recommended: {@link #SOBEL_H}
	 * @param outputConfig
	 * @return
	 */
	public static Bitmap edge(Bitmap src, float[] vEdgeMatrix, float[] hEdgeMatrix, Bitmap.Config outputConfig) {
		int index = 0;
		int width = src.getWidth();
		int height = src.getHeight();
		int[] inPixels = BitmapUtils.getPixels(src);
		int[] outPixels = new int[inPixels.length];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int r = 0, g = 0, b = 0;
				int rh = 0, gh = 0, bh = 0;
				int rv = 0, gv = 0, bv = 0;
				int a = inPixels[y * width + x] & 0xff000000;

				for (int row = -1; row <= 1; row++) {
					int iy = y + row;
					int ioffset;
					if (0 <= iy && iy < height)
						ioffset = iy * width;
					else
						ioffset = y * width;
					int moffset = 3 * (row + 1) + 1;
					for (int col = -1; col <= 1; col++) {
						int ix = x + col;
						if (!(0 <= ix && ix < width))
							ix = x;
						int rgb = inPixels[ioffset + ix];
						float h = hEdgeMatrix[moffset + col];
						float v = vEdgeMatrix[moffset + col];

						r = (rgb & 0xff0000) >> 16;
						g = (rgb & 0x00ff00) >> 8;
						b = rgb & 0x0000ff;
						rh += (int) (h * r);
						gh += (int) (h * g);
						bh += (int) (h * b);
						rv += (int) (v * r);
						gv += (int) (v * g);
						bv += (int) (v * b);
					}
				}
				r = (int) (Math.sqrt(rh * rh + rv * rv) / 1.8);
				g = (int) (Math.sqrt(gh * gh + gv * gv) / 1.8);
				b = (int) (Math.sqrt(bh * bh + bv * bv) / 1.8);
				r = PixelUtils.clamp(r);
				g = PixelUtils.clamp(g);
				b = PixelUtils.clamp(b);
				outPixels[index++] = a | (r << 16) | (g << 8) | b;
			}

		}
		return Bitmap.createBitmap(outPixels, src.getWidth(), src.getHeight(), outputConfig);
	}
}
