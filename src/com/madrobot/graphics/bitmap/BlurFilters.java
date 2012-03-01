package com.madrobot.graphics.bitmap;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.PointF;

import com.madrobot.geom.AffineTransform;
import com.madrobot.graphics.PixelUtils;

/**
 * Collection of various blur filters
 * <p>
 * <b>Gaussian Blur</b><br/>
 * Gaussian using the <code>radius</code> of <code>2</code><br/>
 * <img src="../../../../resources/gaussian.png"><br/>
 * 
 * <b>Motion Blur</b><br/>
 * Motion blur with <code>angle</code> 1.0f and <code>distance</code> 5.0f.<br/>
 * <img src="../../../../resources/motionBlur.png"><br/>
 * </p>
 * 
 * @author elton.stephen.kent
 * 
 */
public class BlurFilters {

	/**
	 * Apply Gaussian blur Filter to the given image data
	 * <p>
	 * <table border="0">
	 * <tr>
	 * <td><b>Before</b></td>
	 * <td><b>After</b></td>
	 * </tr>
	 * <tr>
	 * <td>
	 * <img src="../../../resources/before.png"></td>
	 * <td><img src="../../../resources/gaussian.png"></td>
	 * </tr>
	 * </table>
	 * </p>
	 * 
	 * @param bitmap
	 * @param brightness
	 *            of the result. Optimum values are within 200
	 * @param outputConfig
	 *            Bitmap configuration of the output bitmap
	 */
	public static final Bitmap fastGaussianBlur(Bitmap bitmap, int brightness, Bitmap.Config outputConfig) {
		byte[][] filter = { { 1, 2, 1 }, { 2, 4, 2 }, { 1, 2, 1 } };
		return BitmapFilters.applyFilter(bitmap, brightness, filter, outputConfig);
	}

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
	public static Bitmap gaussianBlur(Bitmap src, int radius, boolean convolveAlpha, boolean premultiplyAlpha, Bitmap.Config outputConfig) {
		int width = src.getWidth();
		int height = src.getHeight();
		int[] inPixels = BitmapUtils.getPixels(src);
		int[] outPixels = new int[inPixels.length];
		Kernel kernel = GaussianUtils.makeKernel(radius);
		if (radius > 0) {
			GaussianUtils.convolveAndTranspose(kernel, inPixels, outPixels, width, height, convolveAlpha, convolveAlpha
					&& premultiplyAlpha, false, BitmapFilters.CLAMP_EDGES);
			GaussianUtils.convolveAndTranspose(kernel, outPixels, inPixels, height, width, convolveAlpha, false,
					convolveAlpha && premultiplyAlpha, BitmapFilters.CLAMP_EDGES);
		}
		return Bitmap.createBitmap(inPixels, src.getWidth(), src.getHeight(), outputConfig);
	}

	/**
	 * Produces motion blur the slow, but higher-quality way.
	 * 
	 * @param src
	 * @param angle
	 *            the angle of blur
	 * @param distance
	 *            the distance of blur
	 * @param rotation
	 *            the blur rotation
	 * @param zoom
	 *            the blur zoom
	 * @param premultiplyAlpha
	 *            whether to premultiply the alpha channel.
	 * @param wrapEdges
	 *            Set whether to wrap at the image edges
	 * @param outputConfig
	 * @return
	 */
	public static Bitmap motionBlur(Bitmap src, float angle, float distance, float rotation, float zoom, boolean premultiplyAlpha, boolean wrapEdges, Bitmap.Config outputConfig) {
		int width = src.getWidth();
		int height = src.getHeight();
		int[] inPixels = BitmapUtils.getPixels(src);
		int[] outPixels = new int[inPixels.length];

		int cx = width / 2;
		int cy = height / 2;
		int index = 0;

		float imageRadius = (float) Math.sqrt(cx * cx + cy * cy);
		float translateX = (float) (distance * Math.cos(angle));
		float translateY = (float) (distance * -Math.sin(angle));
		float maxDistance = distance + Math.abs(rotation * imageRadius) + zoom * imageRadius;
		int repetitions = (int) maxDistance;
		AffineTransform t = new AffineTransform();
		PointF p = new PointF();

		if (premultiplyAlpha)
			ImageMath.premultiply(inPixels, 0, inPixels.length);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int a = 0, r = 0, g = 0, b = 0;
				int count = 0;
				for (int i = 0; i < repetitions; i++) {
					int newX = x, newY = y;
					float f = (float) i / repetitions;

					p.x = x;
					p.y = y;
					t.setToIdentity();
					t.translate(cx + f * translateX, cy + f * translateY);
					float s = 1 - zoom * f;
					t.scale(s, s);
					if (rotation != 0)
						t.rotate(-rotation * f);
					t.translate(-cx, -cy);
					t.transform(p, p);
					newX = (int) p.x;
					newY = (int) p.y;

					if (newX < 0 || newX >= width) {
						if (wrapEdges)
							newX = ImageMath.mod(newX, width);
						else
							break;
					}
					if (newY < 0 || newY >= height) {
						if (wrapEdges)
							newY = ImageMath.mod(newY, height);
						else
							break;
					}

					count++;
					int rgb = inPixels[newY * width + newX];
					a += (rgb >> 24) & 0xff;
					r += (rgb >> 16) & 0xff;
					g += (rgb >> 8) & 0xff;
					b += rgb & 0xff;
				}
				if (count == 0) {
					outPixels[index] = inPixels[index];
				} else {
					a = PixelUtils.clamp((int) (a / count));
					r = PixelUtils.clamp((int) (r / count));
					g = PixelUtils.clamp((int) (g / count));
					b = PixelUtils.clamp((int) (b / count));
					outPixels[index] = (a << 24) | (r << 16) | (g << 8) | b;
				}
				index++;
			}
		}
		if (premultiplyAlpha)
			ImageMath.unpremultiply(outPixels, 0, inPixels.length);

		return Bitmap.createBitmap(outPixels, src.getWidth(), src.getHeight(), outputConfig);
	}

	/**
	 * Apply image blur filter on the given image data
	 * 
	 * @param argbData
	 *            of the image
	 * 
	 * @param bitmapWidth
	 *            of the image
	 * @param height
	 *            of the image
	 */
	public static Bitmap simpleBlur(Bitmap bitmap, Bitmap.Config outputConfig) {
		byte[][] filter = { { -1, -1, -1 }, { -1, 0, -1 }, { -1, -1, -1 } };
		return BitmapFilters.applyFilter(bitmap, 100, filter, outputConfig);
	}

}
