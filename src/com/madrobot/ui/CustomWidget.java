
package com.madrobot.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public abstract class CustomWidget extends View {

	public CustomWidget(Context context) {
		super(context);
		setupDrawingTools();
	}

	public CustomWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupDrawingTools();
	}

	public CustomWidget(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setupDrawingTools();
	}

	protected abstract void setupDrawingTools();

}
