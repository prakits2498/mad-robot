package com.madrobot.graphics.bitmap;

import android.graphics.Bitmap;

/**
 * Transfer function bitmap filters
 * <p>
 * <b>Exposure</b>
 * <table>
 * <tr>
 * <th>Normal Image</th>
 * <th>Exposure set to 3</th>
 * </tr>
 * <tr>
 * <td><img src="../../../../resources/src.png"></td>
 * <td><img src="../../../../resources/exposure.png"></td>
 * </tr>
 * </table>
 * </p>
 * 
 * @author elton.stephen.kent
 * 
 */
public class TransferBitmapFilters {

	public static Bitmap exposure(Bitmap src, float exposure, Bitmap.Config outputConfig) {
		int width = src.getWidth();
		int height = src.getHeight();
		int[] inPixels = BitmapUtils.getPixels(src);
		int[] rTable, gTable, bTable;
		rTable = gTable = bTable = makeTable(exposure);
		for (int y = 0; y < height; y++) {
			int nRow = y * width;
			for (int x = 0; x < width; x++) {
				int rgb = inPixels[nRow + x];
				int a = rgb & 0xff000000;
				int r = (rgb >> 16) & 0xff;
				int g = (rgb >> 8) & 0xff;
				int b = rgb & 0xff;
				r = rTable[r];
				g = gTable[g];
				b = bTable[b];
				inPixels[nRow + x] = a | (r << 16) | (g << 8) | b;
			}

		}

		return Bitmap.createBitmap(inPixels, src.getWidth(), src.getHeight(), outputConfig);
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

}
