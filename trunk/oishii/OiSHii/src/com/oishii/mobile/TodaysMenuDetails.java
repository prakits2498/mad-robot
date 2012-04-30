package com.oishii.mobile;

import android.content.Intent;
import android.widget.TextView;

public class TodaysMenuDetails extends OishiiBaseActivity {

	final static String EXTRA_TITLE = "title";
	final static String EXTRA_COLOR = "bgColor";
	final static String EXTRA_CAT_ID = "catID";

	@Override
	protected void hookInChildViews() {
		showOnlyLogo();
		Intent intent = getIntent();
		String title = intent.getStringExtra(EXTRA_TITLE);
		int color = intent.getIntExtra(EXTRA_COLOR, 0x000000);
		TextView tv = (TextView) findViewById(R.id.title);
		tv.setText(title);
		tv.setTextColor(color);
	}

	@Override
	protected int getSreenID() {
		return R.layout.todaysmenu_details;
	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.todaysmenu_details;
	}

	@Override
	protected String getTitleString() {
		return "";
	}

}
