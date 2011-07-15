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

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.madrobot.graphics.Graphics3DUtil;

/**
 * 
 * ExpenseWheel.java
 * 
 * 
 */
public class SelectionWheel extends View {

	public SelectionWheel(Context context) {
		super(context);
	}

	public SelectionWheel(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SelectionWheel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Graphics3DUtil.draw3DRect(canvas, 0xADAAAD, 0xff0000, true, 2, 5, 5, 30, 40);
	}

}
