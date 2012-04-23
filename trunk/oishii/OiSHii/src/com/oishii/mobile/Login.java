package com.oishii.mobile;

import android.view.Menu;


public class Login extends OishiiBaseActivity {

	@Override
	protected void hookInChildViews() {
		// TODO Auto-generated method stub

	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.login;
	}

	@Override
	protected String getTitleString() {
		return getResources().getString(R.string.login_title);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return false;
	}

}