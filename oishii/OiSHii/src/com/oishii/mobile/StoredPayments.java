package com.oishii.mobile;

import java.util.List;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.beans.OishiiBasket;
import com.oishii.mobile.beans.SavedCard;

public class StoredPayments extends OishiiBaseActivity {
	protected static final String ACTION_SELECT = "select";
	private boolean isForSelecting;

	@Override
	protected int getParentScreenId() {
		return isForSelecting ? R.id.basket : R.id.myacc;
	}

	LinearLayout parent;

	@Override
	protected void hookInChildViews() {
		parent = (LinearLayout) findViewById(R.id.parent);
		isForSelecting = getIntent().getBooleanExtra(ACTION_SELECT, false);
		if (isForSelecting) {

			TextView title = (TextView) findViewById(R.id.headertitle);
			title.setText(getString(R.string.title_sel_payment));
		}
		populateSavedCards();
	}

	private CheckBox cbSave;

	private void populateSavedCards() {

		parent.removeAllViews();
		List<SavedCard> address = AccountStatus
				.getInstance(getApplicationContext()).getAccInformation()
				.getSavedCards();
		int size = address.size();
		if (address.isEmpty()) {
			parent.setVisibility(View.GONE);
			findViewById(R.id.noLocations).setVisibility(View.VISIBLE);
		} else {
			parent.setVisibility(View.VISIBLE);
			findViewById(R.id.noLocations).setVisibility(View.GONE);
		}
		LayoutInflater inflater = getLayoutInflater();
		View v;
		TextView tv;
		SavedCard add;
		for (int i = 0; i < size; i++) {
			add = address.get(i);
			v = inflater.inflate(R.layout.card_field, null);
			tv = (TextView) v.findViewById(R.id.address);
			tv.setText(add.getNumber());
			tv = (TextView) v.findViewById(R.id.type);
			tv.setText(add.getType());
			if (i == (address.size() - 1)) {
				v.findViewById(R.id.sep).setVisibility(View.GONE);
			}
			if (isForSelecting) {
				v.setTag(add.getToken());
				v.setOnClickListener(paymentListener);
			}
			parent.addView(v);
		}
		if (isForSelecting) {
			OishiiBasket baskt = AccountStatus.getInstance(
					getApplicationContext()).getBasket();
			baskt.setSaveCC(false);
			baskt.setSavedToken(null);
			View view = findViewById(R.id.btn_alternate);
			view.setVisibility(View.VISIBLE);
			cbSave = (CheckBox) findViewById(R.id.saveCC);
			cbSave.setVisibility(View.VISIBLE);
			findViewById(R.id.separator).setVisibility(View.VISIBLE);
			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					OishiiBasket basket = AccountStatus.getInstance(
							getApplicationContext()).getBasket();
					basket.setSaveCC(cbSave.isChecked());
					Intent intent = new Intent(StoredPayments.this,
							CheckoutFinal.class);
					startActivity(intent);
				}
			});
		}
	}

	View.OnClickListener paymentListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			OishiiBasket basket = AccountStatus.getInstance(
					getApplicationContext()).getBasket();
			basket.setSaveCC(cbSave.isChecked());
			basket.setSavedToken(v.getTag().toString());
			Intent intent = new Intent(StoredPayments.this, CheckoutFinal.class);
			startActivity(intent);
		}
	};

	@Override
	protected int getSreenID() {
		return R.layout.saved_payments;
	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.saved_payments;
	}

	@Override
	protected String getTitleString() {
		return getString(R.string.title_sc);
	}

}
