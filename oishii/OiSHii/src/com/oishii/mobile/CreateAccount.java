package com.oishii.mobile;


public class CreateAccount extends OishiiBaseActivity {

	@Override
	protected void hookInChildViews() {
	}

	@Override
	protected int getChildViewLayout() {
		// TODO Auto-generated method stub
		return R.layout.create_account;
	}

	@Override
	protected String getTitleString() {
		// TODO Auto-generated method stub
		return getString(R.string.create_acc_title);
	}

	@Override
	protected int getSreenID() {
		// TODO Auto-generated method stub
		return R.id.myacc;
	}

}
