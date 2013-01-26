package com.madrobot.ui.widgets.imageview;

/**
 * Helps notifying the loading of SVG,GIF imageviews
 * 
 * @author ekent4
 * 
 */
public interface LoadingCallback {

	public void onResourceAssigned();

	public void onResourceLoaded();

}
