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
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.widget.ImageView;

import com.madrobot.geom.Rectangle;
import com.madrobot.graphics.bitmap.BlurFilters;
import com.madrobot.graphics.bitmap.ColorFilters;
import com.madrobot.graphics.bitmap.EdgeFilters;
import com.madrobot.graphics.bitmap.EnhancementFilters;
import com.madrobot.graphics.bitmap.OutputConfiguration;
import com.madrobot.graphics.bitmap.TransformFilters;
import com.madrobot.graphics.bitmap.TransitionFilters;

public class TestActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.two);
		// Bitmap.Config outputConfig = Bitmap.Config.ARGB_8888;
		long time = System.currentTimeMillis();
		OutputConfiguration outputConfig = new OutputConfiguration();
//		outputConfig.setAffectedArea(new Rectangle(0, 0, src.getWidth() / 2, src.getHeight() / 2));
		Bitmap bitmap2 =TransitionFilters.dissolve(src, 0.65f, 0.50f, outputConfig);
		// 96, 0, 64, 96, 0, 64 }, outputConfig);
		 System.out.println("============================== DONE ====================");
		// + (System.currentTimeMillis() - time));
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
