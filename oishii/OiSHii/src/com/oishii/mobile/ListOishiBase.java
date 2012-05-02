package com.oishii.mobile;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public abstract class ListOishiBase extends OishiiBaseActivity {

	@Override
	protected void hookInChildViews() {
		showOnlyLogo();
		hookInListData();
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

	protected ListView getListView() {
		return (ListView) findViewById(R.id.listView1);
	}
//
//	protected LinearLayout getManualListView() {
//		findViewById(R.id.listView1).setVisibility(View.GONE);
//		View view = findViewById(R.id.manualList);
//		view.setVisibility(View.VISIBLE);
//		return (LinearLayout) view;
//	}

	protected abstract void hookInListData();
}
