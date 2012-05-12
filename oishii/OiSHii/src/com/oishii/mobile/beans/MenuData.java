package com.oishii.mobile.beans;

import android.graphics.Bitmap;

public class MenuData extends BitmapBean{
	int color;
	int id;
	String title;
	String bitmapUrl;

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBitmapUrl() {
		return bitmapUrl;
	}

	public void setBitmapUrl(String bitmapUrl) {
		this.bitmapUrl = bitmapUrl;
	}

}
