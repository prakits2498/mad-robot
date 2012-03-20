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

import com.madrobot.geom.Rectangle;
import com.madrobot.graphics.bitmap.AestheticTransformFilters;
import com.madrobot.graphics.bitmap.OutputConfiguration;
import com.madrobot.text.English;

public class TestActivity extends Activity {
	/** Called when the activity is first created. */
	Bitmap src;
	OutputConfiguration outputConfig;
	ImageView img;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		src = BitmapFactory.decodeResource(getResources(), R.drawable.two);
		// Bitmap.Config outputConfig = Bitmap.Config.ARGB_8888;
		long time = System.currentTimeMillis();
		outputConfig = new OutputConfiguration();
		outputConfig.setAffectedArea(new Rectangle(0, 0, src.getWidth() / 2, src.getHeight()));
		img = (ImageView) findViewById(R.id.text);
		System.out.println("Time->"+English.timeToString(System.currentTimeMillis()));
//		try {
//			Method method = Surface.class.getMethod("screenshot", new Class[] { Integer.class, Integer.class });
//			Bitmap bitmap=(Bitmap) method.invoke(null, 50,50);
//			img.setImageBitmap(bitmap);
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Bitmap bitmap2 =AestheticTransformFilters.sketch(src, outputConfig.getConfig());  

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

		img.setImageBitmap(bitmap2);
		// new DownloadFilesTask().execute();
	}

}
