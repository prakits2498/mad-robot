package com.madrobot.graphics.bitmap;

import java.util.Random;

import android.graphics.Bitmap;

import com.madrobot.geom.Rectangle;
import com.madrobot.graphics.PixelUtils;

/**
 * Bitmap filters using various whole image algorithms.
 * <p>
 * <b>DeSpeckle</b><br/>
 * <img src="../../../../resources/despeckle.png"><br/>
 * 
 * <b>Edge Detection</b><br/>
 * <img src="../../../../resources/wholeimage_edge.png"><br/>
 * 
 * <b>Oil Paint</b><br/>
 * <img src="../../../../resources/wholeimage_oilpaint.png"><br/>
 * 
 * <b>Quantize</b><br/>
 * <table>
 * <tr>
 * <th>With 128 Colors</th>
 * <th>With 256 Colors</th>
 * </tr>
 * <tr>
 * <td><img src="../../../../resources/quantize128.png"></td>
 * <td><img src="../../../../resources/quantize256.png"></td>
 * </tr>
 * </table>
 * <br/>
 * <b>Flush 3D</b><br/>
 * <img src="../../../../resources/flush32.png"><br/>
 * 
 * <table>
 * <tr>
 * <th>Emboss with <code>emboss</code> set to false and <code>bumpHeight</code> of 3</th>
 * <th>Emboss with <code>emboss</code> set to true and <code>bumpHeight</code> of 3</th>
 * </tr>
 * <tr>
 * <td><img src="../../../../resources/emboss_false.png"></td>
 * <td><img src="../../../../resources/emboss_true.png"></td>
 * </tr>
 * </table>
 * </p>
 * 
 * @author elton.stephen.kent
 * 
 */
public class WholeImageBitmapFilters {

	private final static float R2 = (float) Math.sqrt(2);
	public static float[] FREI_CHEN_H = { -1, -R2, -1, 0, 0, 0, 1, R2, 1, };

	public final static float[] FREI_CHEN_V = { -1, 0, 1, -R2, 0, R2, -1, 0, 1, };

	public final static float[] PREWITT_H = { -1, -1, -1, 0, 0, 0, 1, 1, 1, };

	public final static float[] PREWITT_V = { -1, 0, 1, -1, 0, 1, -1, 0, 1, };

	public final static float[] ROBERTS_H = { -1, 0, 0, 0, 1, 0, 0, 0, 0, };

	public final static float[] ROBERTS_V = { 0, 0, -1, 0, 1, 0, 0, 0, 0, };
	public static float[] SOBEL_H = { -1, -2, -1, 0, 0, 0, 1, 2, 1, };
	public final static float[] SOBEL_V = { -1, 0, 1, -2, 0, 2, -1, 0, 1, };

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
	 * An edge-detection filter.
	 * 
	 * @param src
	 * @param vEdgeMatrix
	 *            can be {@link #ROBERTS_V}, {@link #PREWITT_V},{@link #SOBEL_V} or {@link #FREI_CHEN_V}. recommended:
	 *            {@link #SOBEL_V}
	 * @param hEdgeMatrix
	 *            can be {@link #ROBERTS_H}, {@link #PREWITT_H},{@link #SOBEL_H} or {@link #FREI_CHEN_H}. recommended:
	 *            {@link #SOBEL_H}
	 * @param outputConfig
	 * @return
	 */
	public static Bitmap detectEdge(Bitmap src, float[] vEdgeMatrix, float[] hEdgeMatrix, Bitmap.Config outputConfig) {
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

	/**
	 * A filter which produces a "oil-painting" effect.
	 * <p>
	 * Extremely CPU intensive. Run on dedicated thread.<br/>
	 * 
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

	/**
	 * A filter which quantizes an image to a set number of colors
	 * <p>
	 * useful for producing images which are to be encoded using an index color model. The filter can perform
	 * Floyd-Steinberg error-diffusion dithering if required. At present, the quantization is done using an octtree
	 * algorithm but I eventually hope to add more quantization methods such as median cut. Note: at present, the filter
	 * produces an image which uses the RGB color model (because the application it was written for required it).
	 * </p>
	 * 
	 * @param src
	 * @param numColors
	 *            the number of colors to quantize to. Usually:256
	 * @param dither
	 *            Set whether to use dithering or not. If not, the image is posterized.
	 * @param serpentine
	 *            Set whether to use a serpentine pattern for return or not. This can reduce 'avalanche' artifacts in
	 *            the output.
	 * @param outputConfig
	 * @return
	 */
	public static Bitmap quantize(Bitmap src, int numColors, boolean dither, boolean serpentine, Bitmap.Config outputConfig) {
		int width = src.getWidth();
		int height = src.getHeight();
		int[] inPixels = BitmapUtils.getPixels(src);
		int[] outPixels = new int[inPixels.length];
		int sum = 3 + 5 + 7 + 1;
		/**
		 * Floyd-Steinberg dithering matrix.
		 */
		int[] matrix = { 0, 0, 0, 0, 0, 7, 3, 5, 1, };

		int count = width * height;
		OctTreeQuantizer quantizer = new OctTreeQuantizer();
		quantizer.setup(numColors);
		quantizer.addPixels(inPixels, 0, count);
		int[] table = quantizer.buildColorTable();

		if (!dither) {
			for (int i = 0; i < count; i++)
				outPixels[i] = table[quantizer.getIndexForColor(inPixels[i])];
		} else {
			int index = 0;
			for (int y = 0; y < height; y++) {
				boolean reverse = serpentine && (y & 1) == 1;
				int direction;
				if (reverse) {
					index = y * width + width - 1;
					direction = -1;
				} else {
					index = y * width;
					direction = 1;
				}
				for (int x = 0; x < width; x++) {
					int rgb1 = inPixels[index];
					int rgb2 = table[quantizer.getIndexForColor(rgb1)];

					outPixels[index] = rgb2;

					int r1 = (rgb1 >> 16) & 0xff;
					int g1 = (rgb1 >> 8) & 0xff;
					int b1 = rgb1 & 0xff;

					int r2 = (rgb2 >> 16) & 0xff;
					int g2 = (rgb2 >> 8) & 0xff;
					int b2 = rgb2 & 0xff;

					int er = r1 - r2;
					int eg = g1 - g2;
					int eb = b1 - b2;

					for (int i = -1; i <= 1; i++) {
						int iy = i + y;
						if (0 <= iy && iy < height) {
							for (int j = -1; j <= 1; j++) {
								int jx = j + x;
								if (0 <= jx && jx < width) {
									int w;
									if (reverse)
										w = matrix[(i + 1) * 3 - j + 1];
									else
										w = matrix[(i + 1) * 3 + j + 1];
									if (w != 0) {
										int k = reverse ? index - j : index + j;
										rgb1 = inPixels[k];
										r1 = (rgb1 >> 16) & 0xff;
										g1 = (rgb1 >> 8) & 0xff;
										b1 = rgb1 & 0xff;
										r1 += er * w / sum;
										g1 += eg * w / sum;
										b1 += eb * w / sum;
										inPixels[k] = (PixelUtils.clamp(r1) << 16) | (PixelUtils.clamp(g1) << 8)
												| PixelUtils.clamp(b1);
									}
								}
							}
						}
					}
					index += direction;
				}
			}
		}
		return Bitmap.createBitmap(outPixels, src.getWidth(), src.getHeight(), outputConfig);
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
	 * This filter tries to apply the Swing "flush 3D" effect to the black lines in an image.
	 * 
	 * @param src
	 * @param outputConfig
	 * @return
	 */
	public static Bitmap flush3D(Bitmap src, Bitmap.Config outputConfig) {
		int width = src.getWidth();
		int height = src.getHeight();
		int[] inPixels = BitmapUtils.getPixels(src);
		int[] outPixels = new int[inPixels.length];
		int index = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel = inPixels[y * width + x];

				if (pixel != 0xff000000 && y > 0 && x > 0) {
					int count = 0;
					if (inPixels[y * width + x - 1] == 0xff000000)
						count++;
					if (inPixels[(y - 1) * width + x] == 0xff000000)
						count++;
					if (inPixels[(y - 1) * width + x - 1] == 0xff000000)
						count++;
					if (count >= 2)
						pixel = 0xffffffff;
				}
				outPixels[index++] = pixel;
			}

		}
		return Bitmap.createBitmap(outPixels, src.getWidth(), src.getHeight(), outputConfig);
	}

	/**
	 * Emboss the given bitmap
	 * @param src
	 * @param azimuth Recommended value: <code>135.0f * Math.PI / 180.0f</code>
	 * @param elevation Recommended value:<code>30.0f * Math.PI / 180f</code>
	 * @param bumpHeight Height of the emobssed parts of the image. Recommended:  value <10
	 * @param emboss if true, the bitmap is embossed retaining its color. else a grayscale image is embossed.
	 * @param outputConfig
	 * @return
	 */
	public static Bitmap emboss(Bitmap src, float azimuth, float elevation, float bumpHeight, boolean emboss, Bitmap.Config outputConfig) {
		int width = src.getWidth();
		int height = src.getHeight();
		int index = 0;
		int[] inPixels = BitmapUtils.getPixels(src);
		int[] outPixels = new int[inPixels.length];
		int[] bumpPixels;
		int bumpMapWidth, bumpMapHeight;
		float pixelScale = 255.9f;
		float width45 = 3 * bumpHeight;

		bumpMapWidth = width;
		bumpMapHeight = height;
		bumpPixels = new int[bumpMapWidth * bumpMapHeight];
		for (int i = 0; i < inPixels.length; i++)
			bumpPixels[i] = PixelUtils.brightness(inPixels[i]);

		int Nx, Ny, Nz, Lx, Ly, Lz, Nz2, NzLz, NdotL;
		int shade, background;

		Lx = (int) (Math.cos(azimuth) * Math.cos(elevation) * pixelScale);
		Ly = (int) (Math.sin(azimuth) * Math.cos(elevation) * pixelScale);
		Lz = (int) (Math.sin(elevation) * pixelScale);

		Nz = (int) (6 * 255 / width45);
		Nz2 = Nz * Nz;
		NzLz = Nz * Lz;

		background = Lz;

		int bumpIndex = 0;

		for (int y = 0; y < height; y++, bumpIndex += bumpMapWidth) {
			int s1 = bumpIndex;
			int s2 = s1 + bumpMapWidth;
			int s3 = s2 + bumpMapWidth;

			for (int x = 0; x < width; x++, s1++, s2++, s3++) {
				if (y != 0 && y < height - 2 && x != 0 && x < width - 2) {
					Nx = bumpPixels[s1 - 1] + bumpPixels[s2 - 1] + bumpPixels[s3 - 1] - bumpPixels[s1 + 1]
							- bumpPixels[s2 + 1] - bumpPixels[s3 + 1];
					Ny = bumpPixels[s3 - 1] + bumpPixels[s3] + bumpPixels[s3 + 1] - bumpPixels[s1 - 1] - bumpPixels[s1]
							- bumpPixels[s1 + 1];

					if (Nx == 0 && Ny == 0)
						shade = background;
					else if ((NdotL = Nx * Lx + Ny * Ly + NzLz) < 0)
						shade = 0;
					else
						shade = (int) (NdotL / Math.sqrt(Nx * Nx + Ny * Ny + Nz2));
				} else
					shade = background;

				if (emboss) {
					int rgb = inPixels[index];
					int a = rgb & 0xff000000;
					int r = (rgb >> 16) & 0xff;
					int g = (rgb >> 8) & 0xff;
					int b = rgb & 0xff;
					r = (r * shade) >> 8;
					g = (g * shade) >> 8;
					b = (b * shade) >> 8;
					outPixels[index++] = a | (r << 16) | (g << 8) | b;
				} else
					outPixels[index++] = 0xff000000 | (shade << 16) | (shade << 8) | shade;
			}
		}
		return Bitmap.createBitmap(outPixels, src.getWidth(), src.getHeight(), outputConfig);
	}
}
