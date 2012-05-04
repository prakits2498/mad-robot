package com.oishii.mobile;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
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

	protected ListView getListView(boolean addHead) {
		ListView lv = (ListView) findViewById(R.id.listView1);
		if (addHead) {
			lv.addHeaderView(getDummyHeaderView());
		}
		lv.setVisibility(View.VISIBLE);

		return lv;
	}
	
	private View getDummyHeaderView(){
		View v = getLayoutInflater().inflate(R.layout.list_dummy_header,
				null);
		v.setFocusable(false);
		v.setClickable(false);
	return v;
	}
	
	protected ExpandableListView getExandableList(){
		ExpandableListView list = (ExpandableListView) findViewById(R.id.expList);
		list.addHeaderView(getDummyHeaderView());
		return list;
	}

	//
	// protected LinearLayout getManualListView() {
	// findViewById(R.id.listView1).setVisibility(View.GONE);
	// View view = findViewById(R.id.manualList);
	// view.setVisibility(View.VISIBLE);
	// return (LinearLayout) view;
	// }

	protected abstract void hookInListData();
}
