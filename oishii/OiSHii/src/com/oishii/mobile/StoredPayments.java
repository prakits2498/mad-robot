package com.oishii.mobile;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.beans.SavedCard;

public class StoredPayments extends OishiiBaseActivity {

	@Override
	protected int getParentScreenId() {
		return R.id.myacc;
	}

	LinearLayout parent;

	@Override
	protected void hookInChildViews() {
		parent = (LinearLayout) findViewById(R.id.parent);
		populateSavedCards();
	}

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

			parent.addView(v);
		}
	}

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
