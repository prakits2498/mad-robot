package com.oishii.mobile;

public class OutOfSession extends OishiiBaseActivity {

	public static final String SRC_KEY = "SOURCE_SCRN";
	int notLoggedScreenID = 0x45;

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
		int source = getIntent().getIntExtra(SRC_KEY, 0);
		int titleString=R.string.login_title;
		switch (source) {
		case R.id.basket:
			titleString=R.string.checkout;
			break;
		case R.id.myacc:
			titleString=R.string.acc_title;
			break;
		case R.id.history:
			titleString=R.string.history_title;
			break;
		}
		return getString(titleString);
	}

	@Override
	protected int getSreenID() {
		// TODO Auto-generated method stub
		return notLoggedScreenID;
	}

}
