package com.oishii.mobile;

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
	protected int getSreenID() {
		return R.id.about;
	}

	@Override
	protected int getParentScreenId() {
		// TODO Auto-generated method stub
		return R.id.about;
	}




}
