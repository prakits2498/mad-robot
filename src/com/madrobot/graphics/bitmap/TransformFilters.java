package com.madrobot.graphics.bitmap;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.madrobot.geom.Rectangle;
import com.madrobot.graphics.bitmap.OutputConfiguration.BitmapMeta;

/**
 * Bitmap shapes/perception transforming filters.
 * <p>
 * <b>Water Ripple</b><br/>
 * Ripple effect with <code>wavelength</code> of 16, <code>amplitude</code> 10 and <code>radius</code> of 150. <br/>
 * <img src="../../../../resources/waterripple.png" width="300" height="224"><br/>
 * 
 * * <b>Lens</b><br/>
 * Lens with <code>refractionIndex</code> of 1.5 and <code>radius</code> of 150. <br/>
 * <img src="../../../../resources/lens.png" width="300" height="224"><br/>
 * 
 * <b>Reflection</b><br/>
 * <img src="../../../../resources/reflection.png" width="300" height="224"><br/>
 * <b>Flush 3D</b><br/>
 * <img src="../../../../resources/flush3d.png" width="300" height="224"><br/>
 * </p>
 * 
 * @author elton.stephen.kent
 * 
 */
public class TransformFilters {
	/**
	 * Treat pixels off the edge as zero.
	 */
	public final static int ZERO = 0;

	/**
	 * Clamp pixels to the image edges.
	 */
	public final static int CLAMP = 1;

	/**
	 * Wrap pixels off the edge onto the oppsoite edge.
	 */
	public final static int WRAP = 2;

	/**
	 * Clamp pixels RGB to the image edges, but zero the alpha. This prevents gray borders on your image.
	 */
	public final static int RGB_CLAMP = 3;

	/**
	 * Use nearest-neighbout interpolation.
	 */
	public final static int NEAREST_NEIGHBOUR = 0;

	/**
	 * Use bilinear interpolation.
	 */
	public final static int BILINEAR = 1;

	/**
	 * The action to take for pixels off the image edge.
	 */
	protected int edgeAction = RGB_CLAMP;

	/**
	 * Creates a water ripple effect on a bitmap
	 * 
	 * @param src
	 * @param wavelength
	 *            of the ripple waves. recommended: 16
	 * @param amplitude
	 *            of the ripple waves. recommended: 10
	 * @param phase
	 *            of the ripple waves. recommended:0
	 * @param centreX
	 *            x coordinate for the origin of the ripple. the value <code>0.5</code> represents the centre of the
	 *            bitmap
	 * @param centreY
	 *            Y coordinate for the origin of the ripple. the value <code>0.5</code> represents the centre of the
	 *            bitmap
	 * @param radius
	 *            radius of the ripple. could be any value that falls within the bounds of the image
	 * @param outputConfig
	 * @return
	 */
	public static Bitmap waterRipple(Bitmap src, float wavelength, float amplitude, float phase, float centreX, float centreY, float radius, Bitmap.Config outputConfig) {
		int width = src.getWidth();

		int height = src.getHeight();
		int[] inPixels = BitmapUtils.getPixels(src);

		float icentreX = src.getWidth() * centreX;
		float icentreY = src.getHeight() * centreY;
		if (radius == 0)
			radius = Math.min(icentreX, icentreY);
		float radius2 = radius * radius;

		// Rectangle originalSpace = new Rectangle(0, 0, width, height);
		Rectangle transformedSpace = new Rectangle(0, 0, width, height);
		// transformSpace(transformedSpace);

		int srcWidth = width;
		int srcHeight = height;
		int srcWidth1 = width - 1;
		int srcHeight1 = height - 1;
		int outWidth = transformedSpace.width;
		int outHeight = transformedSpace.height;
		int outX, outY;
		int index = 0;
		int[] outPixels = new int[outWidth];
		int[] destPixels = new int[outHeight * outWidth];
		outX = transformedSpace.x;
		outY = transformedSpace.y;
		float[] out = new float[2];

		for (int y = 0; y < outHeight; y++) {
			for (int x = 0; x < outWidth; x++) {
				waterRippleTransformInverse(outX + x, outY + y, out, icentreX, icentreY, radius2, amplitude,
						wavelength, phase, radius);
				int srcX = (int) Math.floor(out[0]);
				int srcY = (int) Math.floor(out[1]);
				float xWeight = out[0] - srcX;
				float yWeight = out[1] - srcY;
				int nw, ne, sw, se;

				if (srcX >= 0 && srcX < srcWidth1 && srcY >= 0 && srcY < srcHeight1) {
					// Easy case, all corners are in the image
					int i = srcWidth * srcY + srcX;
					nw = inPixels[i];
					ne = inPixels[i + 1];
					sw = inPixels[i + srcWidth];
					se = inPixels[i + srcWidth + 1];
				} else {
					// Some of the corners are off the image
					nw = getPixel(inPixels, srcX, srcY, srcWidth, srcHeight, CLAMP);
					ne = getPixel(inPixels, srcX + 1, srcY, srcWidth, srcHeight, CLAMP);
					sw = getPixel(inPixels, srcX, srcY + 1, srcWidth, srcHeight, CLAMP);
					se = getPixel(inPixels, srcX + 1, srcY + 1, srcWidth, srcHeight, CLAMP);
				}
				outPixels[x] = ImageMath.bilinearInterpolate(xWeight, yWeight, nw, ne, sw, se);
			}
			BitmapUtils.setPixelRow(outPixels, y, outWidth, destPixels);
		}

		return Bitmap.createBitmap(destPixels, outWidth, outHeight, outputConfig);
	}

	private static void waterRippleTransformInverse(int x, int y, float[] out, float icentreX, float icentreY, float radius2, float amplitude, float wavelength, float phase, float radius) {
		float dx = x - icentreX;
		float dy = y - icentreY;
		float distance2 = dx * dx + dy * dy;
		if (distance2 > radius2) {
			out[0] = x;
			out[1] = y;
		} else {
			float distance = (float) Math.sqrt(distance2);
			float amount = amplitude * (float) Math.sin(distance / wavelength * ImageMath.TWO_PI - phase);
			amount *= (radius - distance) / radius;
			if (distance != 0)
				amount *= wavelength / distance;
			out[0] = x + dx * amount;
			out[1] = y + dy * amount;
		}
	}

	static private int getPixel(int[] pixels, int x, int y, int width, int height, int edgeAction) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			switch (edgeAction) {
			case ZERO:
			default:
				return 0;
			case WRAP:
				return pixels[(ImageMath.mod(y, height) * width) + ImageMath.mod(x, width)];
			case CLAMP:
				return pixels[(ImageMath.clamp(y, 0, height - 1) * width) + ImageMath.clamp(x, 0, width - 1)];
			case RGB_CLAMP:
				return pixels[(ImageMath.clamp(y, 0, height - 1) * width) + ImageMath.clamp(x, 0, width - 1)] & 0x00ffffff;
			}
		}
		return pixels[y * width + x];
	}

	/**
	 * Simulates a magnifying lens placed over the given coordinate of an image
	 * 
	 * @param src
	 * @param centreX
	 *            the X coordinate of the center of the lens. The value <code>0.5</code> represents the centre of the
	 *            bitmap
	 * @param centreY
	 *            the Y coordinate of the center of the lens. The value <code>0.5</code> represents the centre of the
	 *            bitmap
	 * @param radius
	 *            Radius of the lens
	 * @param refractionIndex
	 *            lens refraction index. recommended:1.5
	 * @param outputConfig
	 * @return
	 */
	public static Bitmap lens(Bitmap src, float centreX, float centreY, float radius, float refractionIndex, Bitmap.Config outputConfig) {
		int width = src.getWidth();
		int height = src.getHeight();
		float a = radius;
		float b = radius;
		float a2 = 0;
		float b2 = 0;
		float icentreX = width * centreX;
		float icentreY = height * centreY;
		if (a == 0)
			a = width / 2;
		if (b == 0)
			b = height / 2;
		a2 = a * a;
		b2 = b * b;

		// Rectangle originalSpace = new Rectangle(0, 0, width, height);
		Rectangle transformedSpace = new Rectangle(0, 0, width, height);
		// transformSpace(transformedSpace);

		int[] inPixels = BitmapUtils.getPixels(src);
		int srcWidth = width;
		int srcHeight = height;
		int srcWidth1 = width - 1;
		int srcHeight1 = height - 1;
		int outWidth = transformedSpace.width;
		int outHeight = transformedSpace.height;
		int outX, outY;
		// int index = 0;
		int[] outPixels = new int[outWidth];
		int[] destPixels = new int[outHeight * outWidth];

		outX = transformedSpace.x;
		outY = transformedSpace.y;
		float[] out = new float[2];
		// Bitmap dest = Bitmap.createBitmap(outWidth, outHeight, outputConfig);
		for (int y = 0; y < outHeight; y++) {
			for (int x = 0; x < outWidth; x++) {
				transformInverse(outX + x, outY + y, out, icentreX, icentreY, refractionIndex, a, b, a2, b2);
				int srcX = (int) Math.floor(out[0]);
				int srcY = (int) Math.floor(out[1]);
				float xWeight = out[0] - srcX;
				float yWeight = out[1] - srcY;
				int nw, ne, sw, se;

				if (srcX >= 0 && srcX < srcWidth1 && srcY >= 0 && srcY < srcHeight1) {
					// Easy case, all corners are in the image
					int i = srcWidth * srcY + srcX;
					nw = inPixels[i];
					ne = inPixels[i + 1];
					sw = inPixels[i + srcWidth];
					se = inPixels[i + srcWidth + 1];
				} else {
					// Some of the corners are off the image
					nw = getPixel(inPixels, srcX, srcY, srcWidth, srcHeight, CLAMP);
					ne = getPixel(inPixels, srcX + 1, srcY, srcWidth, srcHeight, CLAMP);
					sw = getPixel(inPixels, srcX, srcY + 1, srcWidth, srcHeight, CLAMP);
					se = getPixel(inPixels, srcX + 1, srcY + 1, srcWidth, srcHeight, CLAMP);
				}
				outPixels[x] = ImageMath.bilinearInterpolate(xWeight, yWeight, nw, ne, sw, se);
			}
			// set pixel
			BitmapUtils.setPixelRow(outPixels, y, outWidth, destPixels);
		}
		return Bitmap.createBitmap(destPixels, outWidth, outHeight, outputConfig);
	}

	private static void transformInverse(int x, int y, float[] out, float icentreX, float icentreY, float refractionIndex, float a, float b, float a2, float b2) {
		float dx = x - icentreX;
		float dy = y - icentreY;
		float x2 = dx * dx;
		float y2 = dy * dy;
		if (y2 >= (b2 - (b2 * x2) / a2)) {
			out[0] = x;
			out[1] = y;
		} else {
			float rRefraction = 1.0f / refractionIndex;

			float z = (float) Math.sqrt((1.0f - x2 / a2 - y2 / b2) * (a * b));
			float z2 = z * z;

			float xAngle = (float) Math.acos(dx / Math.sqrt(x2 + z2));
			float angle1 = ImageMath.HALF_PI - xAngle;
			float angle2 = (float) Math.asin(Math.sin(angle1) * rRefraction);
			angle2 = ImageMath.HALF_PI - xAngle - angle2;
			out[0] = x - (float) Math.tan(angle2) * z;

			float yAngle = (float) Math.acos(dy / Math.sqrt(y2 + z2));
			angle1 = ImageMath.HALF_PI - yAngle;
			angle2 = (float) Math.asin(Math.sin(angle1) * rRefraction);
			angle2 = ImageMath.HALF_PI - yAngle - angle2;
			out[1] = y - (float) Math.tan(angle2) * z;
		}
	}

	/**
	 * Create a reflection of an image
	 * 
	 * @param image
	 *            source image
	 * @param reflectionHeight
	 *            height of the reflection
	 * @param outputConfig
	 *            Bitmap configuration of the output bitmap
	 * 
	 * @return Image the height of the image is increased to accommodate the reflection height.
	 */
	public static final Bitmap createReflection(final Bitmap image, final int reflectionHeight, Bitmap.Config outputConfig) {
		/* Tested Apr 27 2011 */
		int w = image.getWidth();
		int h = image.getHeight();

		final Bitmap reflectedImage = Bitmap.createBitmap(w, h + reflectionHeight, outputConfig);
		reflectedImage.setDensity(image.getDensity());
		Canvas canvas = new Canvas(reflectedImage);

		Paint bitmapPaint2 = new Paint();
		bitmapPaint2.setAntiAlias(true);

		canvas.drawBitmap(image, 0, 0, bitmapPaint2);

		int[] rgba = new int[w];
		int currentY = -1;

		for (int i = 0; i < reflectionHeight; i++) {
			int y = (h - 1) - (i * h / reflectionHeight);

			if (y != currentY) {
				image.getPixels(rgba, 0, w, 0, y, w, 1);
			}

			int alpha = 0xff - (i * 0xff / reflectionHeight);

			for (int j = 0; j < w; j++) {
				int origAlpha = (rgba[j] >> 24);
				int newAlpha = (alpha & origAlpha) * alpha / 0xff;

				rgba[j] = (rgba[j] & 0x00ffffff);
				rgba[j] = (rgba[j] | (newAlpha << 24));
			}
			canvas.drawBitmap(rgba, 0, w, 0, h + i, w, 1, true, bitmapPaint2);
		}
		return reflectedImage;
	}

	/**
	 * Generates only the border pixels of the given image
	 * 
	 * @param bitmap
	 * 
	 * @param outputConfig
	 *            Bitmap configuration of the output bitmap
	 */
	public static Bitmap doImageRim(Bitmap bitmap, Bitmap.Config outputConfig) {
		byte[][] filter = { { 0, -1, 0 }, { -1, 5, -1 }, { 0, -1, 0 } };
		return BitmapFilters.applyFilter(bitmap, 0, filter, outputConfig);
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
	public static Bitmap sparkle(Bitmap src, int color, int amount, int rays, int radius, int randomness, OutputConfiguration outputConfig) {
		int width = src.getWidth();
		int height = src.getHeight();

		Random randomNumbers = new Random();
		int centreX = width / 2;
		int centreY = height / 2;
		randomNumbers.setSeed(371);
		float[] rayLengths = new float[rays];
		for (int i = 0; i < rays; i++)
			rayLengths[i] = radius + randomness / 100.0f * radius * (float) randomNumbers.nextGaussian();

		BitmapMeta meta = outputConfig.getBitmapMeta(src);
		int[] inPixels = BitmapUtils.getPixels(src);
		int position, rgb;
		for (int y = meta.y; y < meta.targetHeight; y++) {
			for (int x = meta.x; x < meta.targetWidth; x++) {
				position = (y * meta.bitmapWidth) + x;
				rgb = inPixels[position];

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
				inPixels[position] = ImageMath.mixColors(f, rgb, color);
			}
		}
		return Bitmap.createBitmap(inPixels, meta.bitmapWidth, meta.bitmapHeight, outputConfig.config);
	}

}
