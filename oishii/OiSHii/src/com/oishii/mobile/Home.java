package com.oishii.mobile;

public class Home extends OishiiBaseActivity {

	@Override
	protected void hookInChildViews() {
		showOnlyLogo();
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
