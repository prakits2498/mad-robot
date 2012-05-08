package com.oishii.mobile;

public class PromoCode extends OishiiBaseActivity {

	@Override
	protected int getParentScreenId() {
		return R.id.basket;
	}

	@Override
	protected void hookInChildViews() {

	}

	@Override
	protected int getSreenID() {
		return R.layout.promo_code;
	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.promo_code;
	}

	@Override
	protected String getTitleString() {
		return getString(R.string.title_checkout);
	}

}
