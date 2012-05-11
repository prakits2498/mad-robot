package com.oishii.mobile;

import android.content.Intent;
import android.view.View;

public class PromoCode extends OishiiBaseActivity {

	@Override
	protected int getParentScreenId() {
		return R.id.basket;
	}

	@Override
	protected void hookInChildViews() {
		findViewById(R.id.btnContinue).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(PromoCode.this,
								DeliveryTime.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						startActivity(intent);
					}
				});
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
