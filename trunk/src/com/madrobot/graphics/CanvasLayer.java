package com.madrobot.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class CanvasLayer {
	private final Bitmap bitmap;
	public final Canvas canvas;
	
	public CanvasLayer(int width,int height){
		bitmap=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
		canvas=new Canvas(bitmap);
	}

}
