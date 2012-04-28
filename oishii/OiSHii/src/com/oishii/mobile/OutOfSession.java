package com.oishii.mobile;


public class OutOfSession extends OishiiBaseActivity {
int notLoggedScreenID=0x45;
	@Override
	protected void hookInChildViews() {
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

	@Override
	protected int getSreenID() {
		// TODO Auto-generated method stub
		return notLoggedScreenID;
	}

}
