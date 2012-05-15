package com.oishii.mobile;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PromoCode extends OishiiBaseActivity {

	@Override
	protected int getParentScreenId() {
		return R.id.basket;
	}

	private EditText promoCode;
	@Override
	protected void hookInChildViews() {
		promoCode = (EditText) findViewById(R.id.promoCode);
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
		findViewById(R.id.btnPromoCode).setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!hasValidText(promoCode)){
					Toast toast=Toast.makeText(getApplicationContext(), R.string.error_no_promo, 4000);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}else{
					executePromocodeRequest();
				}
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

	private void executePromocodeRequest() {

	}
}
