package com.oishii.mobile;

import java.io.InputStream;

import android.content.Intent;
import android.view.View;

public class Home extends OishiiBaseActivity {

	@Override
	protected void hookInChildViews() {
		showOnlyLogo();
		findViewById(R.id.todaysMenu).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent todays = new Intent(Home.this, TodaysMenu.class);
						startActivity(todays);
					}
				});

	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.home;
	}

	@Override
	protected String getTitleString() {
		return "";
	}

	@Override
	protected boolean populateViewFromHttp(InputStream is, View v, int operation) {
		return false;
		// TODO Auto-generated method stub
		
	}



}
