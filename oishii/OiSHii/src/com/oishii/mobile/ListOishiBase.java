package com.oishii.mobile;

public class ListOishiBase extends OishiiBaseActivity {

	@Override
	protected void hookInChildViews() {
		showOnlyLogo();
	}

	@Override
	protected int getChildViewLayout() {
		return 0;
	}

	@Override
	protected String getTitleString() {
		return "";
	}

}
