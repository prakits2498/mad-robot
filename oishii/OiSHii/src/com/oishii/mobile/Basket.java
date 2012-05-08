package com.oishii.mobile;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
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
			findViewById(R.id.todaysMenu).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent();
							intent.setClass(Basket.this, TodaysMenu.class);
							startActivity(intent);
						}
					});

		} else {
			findViewById(R.id.topParent).setBackgroundColor(Color.WHITE);
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
				tv = (TextView) basketItem.findViewById(R.id.no);
				temp = item.getCount() + " X ";
				tv.setText(temp);
				tv = (TextView) basketItem.findViewById(R.id.item);
				tv.setText(item.getName());
				tv.setTextColor(item.getColor());
				temp = "£" + item.getPrice();
				tv = (TextView) basketItem.findViewById(R.id.price);
				tv.setText(temp);
				basketParent.addView(basketItem);
			}
			System.out.println("Total->" + basket.getCurrentTotal());
			String total = "£" + basket.getCurrentTotal();
			tv = (TextView) findViewById(R.id.subtotal);
			tv.setText(total);
			tv = (TextView) findViewById(R.id.totalPrice);
			tv.setText(total);
			findViewById(R.id.btnCheckout).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getApplicationContext(),
									PromoCode.class);
							startActivity(intent);
						}
					});
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
