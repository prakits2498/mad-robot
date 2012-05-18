package com.oishii.mobile.beans;

import android.graphics.Bitmap;

public class BitmapBean {
	private Bitmap bitmap;

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		System.out.println("Setting bitmap->"+bitmap);
		this.bitmap = bitmap;
	}
	
}
