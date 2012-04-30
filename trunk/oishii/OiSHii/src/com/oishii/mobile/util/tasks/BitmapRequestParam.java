package com.oishii.mobile.util.tasks;

import java.net.URI;

import android.graphics.BitmapFactory;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class BitmapRequestParam {
	public URI bitmapUri;
	public ImageView image;
	public ProgressBar progress;
	public BitmapFactory.Options bitmapOptions;
	public ViewGroup parent;

}
