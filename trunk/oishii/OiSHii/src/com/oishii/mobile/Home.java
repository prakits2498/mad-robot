package com.oishii.mobile;

import com.oishii.mobile.util.CurrentScreen;

import android.content.Intent;
import android.view.View;

public class Home extends OishiiBaseActivity {

	@Override
	protected void hookInChildViews() {
		CurrentScreen.getInstance().setCurrentScreenID(R.id.about);
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




}
