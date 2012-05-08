package com.oishii.mobile;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.beans.BasketItem;
import com.oishii.mobile.beans.OishiiBasket;

public class Basket extends OishiiBaseActivity {

	@Override
	protected int getParentScreenId() {
		return R.id.basket;
	}

	@Override
	protected void hookInChildViews() {
		populateBasket();
	}

	private void populateBasket() {
		OishiiBasket basket = AccountStatus
				.getInstance(getApplicationContext()).getBasket();
		List<BasketItem> items = basket.getBasketItems();
		int count = items.size();
		if (items.isEmpty()) {
			findViewById(R.id.basketParent).setVisibility(View.GONE);
			findViewById(R.id.emptyBasket).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.basketParent).setVisibility(View.VISIBLE);
			findViewById(R.id.emptyBasket).setVisibility(View.GONE);
			LinearLayout basketParent = (LinearLayout) findViewById(R.id.itemsParent);
			basketParent.removeAllViews();
			LayoutInflater inflater = getLayoutInflater();
			View basketItem;
			BasketItem item;
			String temp;
			TextView tv;
			for (int i = 0; i < count; i++) {
				item = items.get(i);
				basketItem = inflater.inflate(R.layout.basket_item, null);
				tv = (TextView) basketItem.findViewById(R.id.item);
				temp = item.getCount() + " X " + item.getName();
				tv.setText(temp);
				temp = "£" + item.getPrice();
				tv = (TextView) basketItem.findViewById(R.id.price);
				tv.setText(temp);
				basketParent.addView(basketItem);
			}
			System.out.println("Total->"+basket.getCurrentTotal());
			String total = "£" + basket.getCurrentTotal();
			tv = (TextView) findViewById(R.id.subtotal);
			tv.setText(total);
			tv = (TextView) findViewById(R.id.totalPrice);
			tv.setText(total);
		}
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
