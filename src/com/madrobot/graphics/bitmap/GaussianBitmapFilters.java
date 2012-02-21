package com.madrobot.graphics.bitmap;

import android.graphics.Bitmap;

import com.madrobot.graphics.PixelUtils;

/**
 * Gaussian theory based bitmap filters
 * 
 * <p>
 * <b>Gaussian</b><br/>
 * Gaussian using the <code>radius</code> of <code>2</code><br/>
 * <img src="../../../../resources/gaussian.png"><br/>
 * 
 * <b>Glow</b><br/>
 * Glow using the <code>glowAmount</code> of <code>0.5</code> and <code>glowRadius</code> of <code>2</code><br/>
 * <img src="../../../../resources/glow.png"><br/>
 * </p>
 * 
 * @author elton.stephen.kent
 * 
 */
public class GaussianBitmapFilters {

	/**
	 * Apply gaussian filter
	 * 
	 * @param src
	 * @param radius
	 *            filter radius
	 * @param convolveAlpha
	 * @param premultiplyAlpha
	 * @param outputConfig
	 * @return
	 */
	public static Bitmap gaussian(Bitmap src, int radius, boolean convolveAlpha, boolean premultiplyAlpha, Bitmap.Config outputConfig) {
		int width = src.getWidth();
		int height = src.getHeight();
		int[] inPixels = BitmapUtils.getPixels(src);
		int[] outPixels = new int[inPixels.length];
		Kernel kernel = makeKernel(radius);
		if (radius > 0) {
			convolveAndTranspose(kernel, inPixels, outPixels, width, height, convolveAlpha, convolveAlpha
					&& premultiplyAlpha, false, BitmapFilters.CLAMP_EDGES);
			convolveAndTranspose(kernel, outPixels, inPixels, height, width, convolveAlpha, false, convolveAlpha
					&& premultiplyAlpha, BitmapFilters.CLAMP_EDGES);
		}

		return Bitmap.createBitmap(inPixels, src.getWidth(), src.getHeight(), outputConfig);

	}

	/**
	 * Blur and transpose a block of ARGB pixels.
	 * 
	 * @param kernel
	 *            the blur kernel
	 * @param inPixels
	 *            the input pixels
	 * @param outPixels
	 *            the output pixels
	 * @param width
	 *            the width of the pixel array
	 * @param height
	 *            the height of the pixel array
	 * @param alpha
	 *            whether to blur the alpha channel
	 * @param edgeAction
	 *            what to do at the edges
	 */
	public static void convolveAndTranspose(Kernel kernel, int[] inPixels, int[] outPixels, int width, int height, boolean alpha, boolean premultiply, boolean unpremultiply, int edgeAction) {
		float[] matrix = kernel.getKernelData(null);
		int cols = kernel.getWidth();
		int cols2 = cols / 2;

		for (int y = 0; y < height; y++) {
			int index = y;
			int ioffset = y * width;
			for (int x = 0; x < width; x++) {
				float r = 0, g = 0, b = 0, a = 0;
				int moffset = cols2;
				for (int col = -cols2; col <= cols2; col++) {
					float f = matrix[moffset + col];

					if (f != 0) {
						int ix = x + col;
						if (ix < 0) {
							if (edgeAction == BitmapFilters.CLAMP_EDGES)
								ix = 0;
							else if (edgeAction == BitmapFilters.WRAP_EDGES)
								ix = (x + width) % width;
						} else if (ix >= width) {
							if (edgeAction == BitmapFilters.CLAMP_EDGES)
								ix = width - 1;
							else if (edgeAction == BitmapFilters.WRAP_EDGES)
								ix = (x + width) % width;
						}
						int rgb = inPixels[ioffset + ix];
						int pa = (rgb >> 24) & 0xff;
						int pr = (rgb >> 16) & 0xff;
						int pg = (rgb >> 8) & 0xff;
						int pb = rgb & 0xff;
						if (premultiply) {
							float a255 = pa * (1.0f / 255.0f);
							pr *= a255;
							pg *= a255;
							pb *= a255;
						}
						a += f * pa;
						r += f * pr;
						g += f * pg;
						b += f * pb;
					}
				}
				if (unpremultiply && a != 0 && a != 255) {
					float f = 255.0f / a;
					r *= f;
					g *= f;
					b *= f;
				}
				int ia = alpha ? PixelUtils.clamp((int) (a + 0.5)) : 0xff;
				int ir = PixelUtils.clamp((int) (r + 0.5));
				int ig = PixelUtils.clamp((int) (g + 0.5));
				int ib = PixelUtils.clamp((int) (b + 0.5));
				outPixels[index] = (ia << 24) | (ir << 16) | (ig << 8) | ib;
				index += height;
			}
		}
	}

	/**
	 * Make a Gaussian blur kernel.
	 * 
	 * @param radius
	 *            the blur radius
	 * @return the kernel
	 */
	public static Kernel makeKernel(float radius) {
		int r = (int) Math.ceil(radius);
		int rows = r * 2 + 1;
		float[] matrix = new float[rows];
		float sigma = radius / 3;
		float sigma22 = 2 * sigma * sigma;
		float sigmaPi2 = 2 * ImageMath.PI * sigma;
		float sqrtSigmaPi2 = (float) Math.sqrt(sigmaPi2);
		float radius2 = radius * radius;
		float total = 0;
		int index = 0;
		for (int row = -r; row <= r; row++) {
			float distance = row * row;
			if (distance > radius2)
				matrix[index] = 0;
			else
				matrix[index] = (float) Math.exp(-(distance) / sigma22) / sqrtSigmaPi2;
			total += matrix[index];
			index++;
		}
		for (int i = 0; i < rows; i++)
			matrix[i] /= total;

		return new Kernel(rows, 1, matrix);
	}

	/**
	 * A filter which adds Gaussian blur to Bitmap, producing a glowing effect.
	 * 
	 * @param src
	 *            Source Bitmap
	 * @param glowAmount
	 *            Amount of glow. Should be from 0 to 1.Recommended:0.5f
	 * @param blurRadius
	 *            recommended :2
	 * @param processAlpha
	 *            process alpha for this image, recommended:true
	 * @param premultiplyAlpha
	 *            premulitply alpha. recommended:true
	 * @param outputConfig
	 * @return
	 */
	public static Bitmap applyGlow(Bitmap src, float glowAmount, float blurRadius, boolean processAlpha, boolean premultiplyAlpha, Bitmap.Config outputConfig) {

		int width = src.getWidth();
		int height = src.getHeight();
		int[] argb = BitmapUtils.getPixels(src);
		int[] dest = new int[argb.length];
		Kernel kernel = makeKernel(blurRadius);
		if (blurRadius > 0) {
			convolveAndTranspose(kernel, argb, dest, width, height, processAlpha, processAlpha && premultiplyAlpha,
					false, BitmapFilters.CLAMP_EDGES);
			convolveAndTranspose(kernel, dest, argb, height, width, processAlpha, false, processAlpha
					&& premultiplyAlpha, BitmapFilters.CLAMP_EDGES);
		}
		dest = BitmapUtils.getPixels(src);
		float a = 4 * glowAmount;
		int index = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb1 = dest[index];
				int r1 = (rgb1 >> 16) & 0xff;
				int g1 = (rgb1 >> 8) & 0xff;
				int b1 = rgb1 & 0xff;

				int rgb2 = argb[index];
				int r2 = (rgb2 >> 16) & 0xff;
				int g2 = (rgb2 >> 8) & 0xff;
				int b2 = rgb2 & 0xff;

				r1 = PixelUtils.clamp((int) (r1 + a * r2));
				g1 = PixelUtils.clamp((int) (g1 + a * g2));
				b1 = PixelUtils.clamp((int) (b1 + a * b2));

				argb[index] = (rgb1 & 0xff000000) | (r1 << 16) | (g1 << 8) | b1;
				index++;
			}
		}

		return Bitmap.createBitmap(argb, src.getWidth(), src.getHeight(), outputConfig);
	}

	/**
	 * Gaussian high pass filter.
	 * 
	 * @param src
	 * @param radius
	 *            recommended: 10
	 * @param processAlpha
	 * @param premultiplyAlpha
	 * @param outputConfig
	 * @return
	 */
	public static Bitmap applyHighPass(Bitmap src, float radius, boolean processAlpha, boolean premultiplyAlpha, Bitmap.Config outputConfig) {
		int width = src.getWidth();
		int height = src.getHeight();
		int[] inPixels = BitmapUtils.getPixels(src);
		int[] outPixels = new int[inPixels.length];
		Kernel kernel = makeKernel(radius);
		if (radius > 0) {
			convolveAndTranspose(kernel, inPixels, outPixels, width, height, processAlpha, processAlpha
					&& premultiplyAlpha, false, BitmapFilters.CLAMP_EDGES);
			convolveAndTranspose(kernel, outPixels, inPixels, height, width, processAlpha, false, processAlpha
					&& premultiplyAlpha, BitmapFilters.CLAMP_EDGES);
		}
		outPixels = BitmapUtils.getPixels(src);

		int index = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb1 = outPixels[index];
				int r1 = (rgb1 >> 16) & 0xff;
				int g1 = (rgb1 >> 8) & 0xff;
				int b1 = rgb1 & 0xff;

				int rgb2 = inPixels[index];
				int r2 = (rgb2 >> 16) & 0xff;
				int g2 = (rgb2 >> 8) & 0xff;
				int b2 = rgb2 & 0xff;

				r1 = (r1 + 255 - r2) / 2;
				g1 = (g1 + 255 - g2) / 2;
				b1 = (b1 + 255 - b2) / 2;

				inPixels[index] = (rgb1 & 0xff000000) | (r1 << 16) | (g1 << 8) | b1;
				index++;
			}
		}
		return Bitmap.createBitmap(inPixels, src.getWidth(), src.getHeight(), outputConfig);
	}
}
