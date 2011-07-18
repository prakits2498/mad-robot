package com.madrobot.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class CanvasLayer {
	private final Bitmap bitmap;
	/**
	 * Canvas to be used for drawing operations on this layer
	 */
	public final Canvas canvas;
	private Paint bmPaint;

	public CanvasLayer(int width, int height) {
		bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		bmPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	}

	/**
	 * The source canvas
	 * 
	 * @param src
	 *            The canvas to draw this layer onto.
	 */
	public void drawLayer(Canvas src) {
		drawLayer(src, 0, 0);
	}

	public void drawLayer(Canvas src, float x, float y) {
		src.drawBitmap(bitmap, x, y, bmPaint);
	}
	
}
