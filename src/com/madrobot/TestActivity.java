/*******************************************************************************
 * Copyright (c) 2011 MadRobot.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *  Elton Kent - initial API and implementation
 ******************************************************************************/
package com.madrobot;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.madrobot.graphics.bitmap.WholeImageBitmapFilters;

public class TestActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.two);
		Bitmap.Config outputConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap2=WholeImageBitmapFilters.emboss(src,  (float)(135.0f * Math.PI / 180.0f), (float)(30.0f * Math.PI / 180f), 3f, true, outputConfig);// = TransformBitmapFilters.circle(src, 20, 0, (float)Math.PI, 10, 0.5f, 0.5f, TransformBitmapFilters.INTERPOLATION_BILINEAR, outputConfig);
		// SimpleBitmapFilters.motionBlur(src, 1.0f, 5.0f, 0.0f, 0.0f, true, false, outputConfig);// (src,
		// outputConfig);//(src,
		// outputConfig);//(bitmap,
		// 3, 256,
		// Config.ARGB_8888);//
		// (bitmap,Config.ARGB_8888);//(bitmap,
		// BitmapFilters.CLAMP_EDGES,
		// true, true,
		// Bitmap.Config.ARGB_8888);
		ImageView img = (ImageView) findViewById(R.id.text);
		img.setImageBitmap(bitmap2);
	}
}
