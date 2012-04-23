package com.oishii.mobile;

import android.widget.TextView;

public abstract class ListOishiBase extends OishiiBaseActivity {

	@Override
	protected void hookInChildViews() {
		showOnlyLogo();
	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.oishiilistbase;
	}

	@Override
	protected String getTitleString() {
		return "";
	}

	protected void setTitleFirstPart(String title, int color) {
		TextView tv = (TextView) findViewById(R.id.titleFirst);
		tv.setText(title);
		tv.setTextColor(color);
	}

	protected void setTitleSecondPart(String title, int color) {
		TextView tv = (TextView) findViewById(R.id.titleSecond);
		tv.setText(title);
		tv.setTextColor(color);
	}
	
	protected abstract void hookInListData();
}
