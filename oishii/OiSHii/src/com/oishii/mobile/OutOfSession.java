package com.oishii.mobile;

public class OutOfSession extends OishiiBaseActivity {

	@Override
	protected void hookInChildViews() {
		// TODO Auto-generated method stub

	}

	@Override
	protected int getChildViewLayout() {
		// TODO Auto-generated method stub
		return R.layout.my_account_no_signed_in;
	}

	@Override
	protected String getTitleString() {
		// TODO Auto-generated method stub
		return getString(R.string.acc_title);
	}

}
