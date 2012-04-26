package com.oishii.mobile;

import android.widget.TextView;

public class SpecialOffers extends ListOishiBase {

	@Override
	protected void hookInListData() {
		TextView tv = (TextView) findViewById(R.id.titleFirst);
		tv.setText(R.string.special);
		TextView tv2 = (TextView) findViewById(R.id.titleSecond);
		tv2.setText(R.string.offers);
	}

}
