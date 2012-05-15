package com.oishii.mobile;

public class CheckoutFinal extends OishiiBaseActivity {

	@Override
	protected int getParentScreenId() {
		return R.id.basket;
	}

	@Override
	protected void hookInChildViews() {

	}

	@Override
	protected int getSreenID() {
		// TODO Auto-generated method stub
		return R.layout.checkout_final;
	}

	@Override
	protected int getChildViewLayout() {
		// TODO Auto-generated method stub
		return R.layout.checkout_final;
	}

	@Override
	protected String getTitleString() {
		// TODO Auto-generated method stub
		return getString(R.string.title_checkout);
	}

}
