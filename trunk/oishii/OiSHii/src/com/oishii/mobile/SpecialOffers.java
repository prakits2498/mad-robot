package com.oishii.mobile;

public class SpecialOffers extends ListOishiBase {

	@Override
	protected void hookInListData() {
		setTitleFirstPart(getString(R.string.special), android.R.color.black);
		setTitleFirstPart(getString(R.string.offers), R.color.text_color);
	}

}
