package com.madrobot.graphics.bitmap;

import android.graphics.Bitmap;

public class BitmapFilters {
	/**
	 * Treat pixels off the edge as zero.
	 */
	public static int ZERO_EDGES = 0;

	/**
	 * Clamp pixels off the edge to the nearest edge.
	 */
	public static int CLAMP_EDGES = 1;

	/**
	 * Wrap pixels off the edge to the opposite edge.
	 */
	public static int WRAP_EDGES = 2;

	private static Bitmap applyFilter(Bitmap bitmap, int value, byte[][] filter, Bitmap.Config outputConfig) {
		int[] argbData = BitmapUtils.getPixels(bitmap);
		applyFilter(filter, value, argbData, bitmap.getWidth(), bitmap.getHeight());
		return Bitmap.createBitmap(argbData, bitmap.getWidth(), bitmap.getHeight(), outputConfig);

	}

	/**
	 * Performs a convolution of an image with a given matrix.
	 * 
	 * @param filterMatrix
	 *            a matrix, which should have odd rows an colums (not neccessarily a square). The matrix is used for a
	 *            2-dimensional convolution. Negative values are possible.
	 * 
	 * @param brightness
	 *            you can vary the brightness of the image measured in percent. Note that the algorithm tries to keep
	 *            the original brightness as far as is possible.
	 * 
	 * @param argbData
	 *            the image (RGB+transparency)
	 * 
	 * @param width
	 *            of the given Image
	 * 
	 * @param height
	 *            of the given Image Be aware that the computation time depends on the size of the matrix.
	 * @throws IllegalArgumentException
	 *             if the filter matrix length is an even number
	 */
	private final static void applyFilter(byte[][] filterMatrix, int brightness, int[] argbData, int width, int height) {
		// ############ tested by $t3p#3n on 29-july-08 #################//
		int COLOR_BIT_MASK = 0x000000FF;
		// check whether the matrix is ok
		if ((filterMatrix.length % 2 != 1) || (filterMatrix[0].length % 2 != 1)) {
			throw new IllegalArgumentException();
		}

		int fhRadius = filterMatrix.length / 2 + 1;
		int fwRadius = filterMatrix[0].length / 2 + 1;
		int currentPixel = 0;
		int newTran, newRed, newGreen, newBlue;

		// compute the brightness
		int divisor = 0;
		for (int fCol, fRow = 0; fRow < filterMatrix.length; fRow++) {
			for (fCol = 0; fCol < filterMatrix[0].length; fCol++) {
				divisor += filterMatrix[fRow][fCol];
			}
		}
		// TODO: if (divisor==0), because of negativ matrixvalues
		if (divisor == 0) {
			return; // no brightness
		}

		// copy the neccessary imagedata into a small buffer
		int[] tmpRect = new int[width * (filterMatrix.length)];
		System.arraycopy(argbData, 0, tmpRect, 0, width * (filterMatrix.length));

		for (int fCol, fRow, col, row = fhRadius - 1; row + fhRadius < height + 1; row++) {
			for (col = fwRadius - 1; col + fwRadius < width + 1; col++) {

				// perform the convolution
				newTran = 0;
				newRed = 0;
				newGreen = 0;
				newBlue = 0;

				for (fRow = 0; fRow < filterMatrix.length; fRow++) {

					for (fCol = 0; fCol < filterMatrix[0].length; fCol++) {

						// take the Data from the little buffer and skale the
						// color
						currentPixel = tmpRect[fRow * width + col + fCol - fwRadius + 1];
						if (((currentPixel >>> 24) & COLOR_BIT_MASK) != 0) {
							newTran += filterMatrix[fRow][fCol] * ((currentPixel >>> 24) & COLOR_BIT_MASK);
							newRed += filterMatrix[fRow][fCol] * ((currentPixel >>> 16) & COLOR_BIT_MASK);
							newGreen += filterMatrix[fRow][fCol] * ((currentPixel >>> 8) & COLOR_BIT_MASK);
							newBlue += filterMatrix[fRow][fCol] * (currentPixel & COLOR_BIT_MASK);
						}

					}
				}

				// calculate the color
				newTran = newTran * brightness / 100 / divisor;
				newRed = newRed * brightness / 100 / divisor;
				newGreen = newGreen * brightness / 100 / divisor;
				newBlue = newBlue * brightness / 100 / divisor;

				newTran = Math.max(0, Math.min(255, newTran));
				newRed = Math.max(0, Math.min(255, newRed));
				newGreen = Math.max(0, Math.min(255, newGreen));
				newBlue = Math.max(0, Math.min(255, newBlue));
				argbData[(row) * width + col] = (newTran << 24 | newRed << 16 | newGreen << 8 | newBlue);

			}

			// shift the buffer if we are not near the end
			if (row + fhRadius != height) {
				System.arraycopy(tmpRect, width, tmpRect, 0, width * (filterMatrix.length - 1)); // shift
				// it
				// back
				System.arraycopy(argbData, width * (row + fhRadius), tmpRect, width * (filterMatrix.length - 1), width); // add
				// new
				// data
			}
		}
		// return Image.createRGBImage(argbData, width, height, true);
	}

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
	public static final Bitmap doGaussianBlurFilter(Bitmap bitmap, int brightness, Bitmap.Config outputConfig) {
		byte[][] filter = { { 1, 2, 1 }, { 2, 4, 2 }, { 1, 2, 1 } };
		return applyFilter(bitmap, brightness, filter, outputConfig);
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
		return applyFilter(bitmap, 0, filter, outputConfig);
	}

	/**
	 * Apply image blur filter on the given image data
	 * <p>
	 * <table border="0">
	 * 
	 * <tr>
	 * <td><b>Before</b></td>
	 * <td><b>After</b></td>
	 * </tr>
	 * <tr>
	 * <td>
	 * <img src="../../../resources/before.png"></td>
	 * <td><img src="../../../resources/blur.png"></td>
	 * </tr>
	 * </table>
	 * </p>
	 * 
	 * @param argbData
	 *            of the image
	 * 
	 * @param width
	 *            of the image
	 * @param height
	 *            of the image
	 */
	public static Bitmap doSimpleBlur(Bitmap bitmap, Bitmap.Config outputConfig) {
		byte[][] filter = { { -1, -1, -1 }, { -1, 0, -1 }, { -1, -1, -1 } };
		return applyFilter(bitmap, 100, filter, outputConfig);
	}

	/**
	 * Apply emboss filter to the given image data
	 * <p>
	 * <table border="0">
	 * <tr>
	 * <td><b>Before</b></td>
	 * <td><b>After</b></td>
	 * </tr>
	 * <tr>
	 * <td>
	 * <img src="../../../resources/before.png"></td>
	 * <td><img src="../../../resources/emboss.png"></td>
	 * </tr>
	 * </table>
	 * </p>
	 * 
	 * @param bitmap
	 *            of the image
	 * @param outputConfig
	 *            Bitmap configuration of the output bitmap
	 * @return
	 */
	public static Bitmap emboss(Bitmap bitmap, Bitmap.Config outputConfig) {
		byte[][] filter = { { -2, 0, 0 }, { 0, 1, 0 }, { 0, 0, 2 } };
		return applyFilter(bitmap, 100, filter, outputConfig);
	}
	
	/**
	 * Converts
	 * 
	 * @param src
	 * @param saturation
	 *            Grayscale saturation level
	 * @param outputConfig
	 *            Bitmap configuration of the output bitmap
	 * @return
	 */
	public static final Bitmap doGrayscaleFilter(Bitmap src, int saturation, Bitmap.Config outputConfig) {
		int[] rgbInput = BitmapUtils.getPixels(src);
		int[] rgbOutput = new int[src.getWidth() * src.getHeight()];

		int alpha, red, green, blue;
		int output_red, output_green, output_blue;

		// We will use the standard NTSC color quotiens, multiplied by 1024
		// in order to be able to use integer-only math throughout the code.
		int RW = 306; // 0.299 * 1024
		int RG = 601; // 0.587 * 1024
		int RB = 117; // 0.114 * 1024

		// Define and calculate matrix quotients
		final int a, b, c, d, e, f, g, h, i;
		a = (1024 - saturation) * RW + saturation * 1024;
		b = (1024 - saturation) * RW;
		c = (1024 - saturation) * RW;
		d = (1024 - saturation) * RG;
		e = (1024 - saturation) * RG + saturation * 1024;
		f = (1024 - saturation) * RG;
		g = (1024 - saturation) * RB;
		h = (1024 - saturation) * RB;
		i = (1024 - saturation) * RB + saturation * 1024;

		int pixel = 0;
		for (int p = 0; p < rgbOutput.length; p++) {
			pixel = rgbInput[p];
			alpha = (0xFF000000 & pixel);
			red = (0x00FF & (pixel >> 16));
			green = (0x0000FF & (pixel >> 8));
			blue = pixel & (0x000000FF);

			// Matrix multiplication
			output_red = ((a * red + d * green + g * blue) >> 4) & 0x00FF0000;
			output_green = ((b * red + e * green + h * blue) >> 12) & 0x0000FF00;
			output_blue = (c * red + f * green + i * blue) >> 20;

			rgbOutput[p] = alpha | output_red | output_green | output_blue;
		}
		return BitmapUtils.getBitmap(rgbOutput, src.getWidth(), src.getHeight(), outputConfig);
	}

}
