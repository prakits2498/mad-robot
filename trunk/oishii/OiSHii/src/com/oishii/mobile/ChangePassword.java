package com.oishii.mobile;

public class ChangePassword extends OishiiBaseActivity {

	@Override
	protected void hookInChildViews() {

	}

	@Override
	protected int getSreenID() {
		return R.layout.changepassword;
	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.changepassword;
	}

	@Override
	protected String getTitleString() {
		return getString(R.string.title_chng_pwd);
	}

}
