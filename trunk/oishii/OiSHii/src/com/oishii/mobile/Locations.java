package com.oishii.mobile;

public class Locations extends OishiiBaseActivity {

	@Override
	protected int getParentScreenId() {
		return R.id.myacc;
	}

	@Override
	protected void hookInChildViews() {
		// TODO Auto-generated method stub

	}

	@Override
	protected int getSreenID() {
		// TODO Auto-generated method stub
		return R.layout.locations;
	}

	@Override
	protected int getChildViewLayout() {
		// TODO Auto-generated method stub
		return R.layout.locations;
	}

	@Override
	protected String getTitleString() {
		// TODO Auto-generated method stub
		return null;
	}

}
