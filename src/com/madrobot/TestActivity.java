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

import com.madrobot.graphics.BimapUtils;

public class TestActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ImageView main = (ImageView) findViewById(R.id.text);
		ImageView main2 = (ImageView) findViewById(R.id.text2);
		main.setImageResource(R.drawable.icon);
		main2.setImageBitmap(BimapUtils.deBlurHorizontalHalftone(
				BitmapFactory.decodeResource(getResources(), R.drawable.icon),
				5, 4, 3, 2, 1, Bitmap.Config.ARGB_8888));

	}
}
