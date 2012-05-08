package com.oishii.mobile;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.widget.TextView;

public class DeliveryTime extends OishiiBaseActivity {

	@Override
	protected int getParentScreenId() {
		return R.id.basket;
	}

	@Override
	protected void hookInChildViews() {
		SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
		String format = df.format(new Date(System.currentTimeMillis()));
		TextView date = (TextView) findViewById(R.id.day);
		date.setText(format);
	}

	@Override
	protected int getSreenID() {
		return R.layout.delivery_time;
	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.delivery_time;
	}

	@Override
	protected String getTitleString() {
		return getString(R.string.title_check_del);
	}

}
