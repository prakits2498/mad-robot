package com.oishii.mobile;

import com.oishii.mobile.beans.CurrentScreen;

public class AccountDetails extends OishiiBaseActivity {

	@Override
	protected void hookInChildViews() {
		// TODO Auto-generated method stub
CurrentScreen.getInstance().setCurrentScreenID(R.id.myacc);
	}

	@Override
	protected int getChildViewLayout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected String getTitleString() {
		// TODO Auto-generated method stub
		return getString(R.string.acc_title);
	}

}
