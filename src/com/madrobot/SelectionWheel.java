
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
