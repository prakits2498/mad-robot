package com.oishii.mobile;

public class AccountDetails extends OishiiBaseActivity {
	final int SCR_ID = 67;

	@Override
	protected void hookInChildViews() {
		// TODO Auto-generated method stub

	}

	@Override
	protected int getSreenID() {
		// TODO Auto-generated method stub
		return SCR_ID;
	}

	@Override
	protected int getChildViewLayout() {
		// TODO Auto-generated method stub
		return R.layout.my_account;
	}

	@Override
	protected String getTitleString() {
		// TODO Auto-generated method stub
		return getString(R.string.acc_title);
	}

}
