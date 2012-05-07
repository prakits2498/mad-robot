package com.oishii.mobile;

public class Basket extends OishiiBaseActivity {

	@Override
	protected int getParentScreenId() {
		return R.id.basket;
	}

	@Override
	protected void hookInChildViews() {

	}

	@Override
	protected int getSreenID() {
		return R.layout.basket;
	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.basket;
	}

	@Override
	protected String getTitleString() {
		return getString(R.string.checkout);
	}

}
