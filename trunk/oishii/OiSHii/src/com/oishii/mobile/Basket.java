package com.oishii.mobile;

import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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

	int removalIndex;
	View.OnClickListener removeItemListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			removalIndex = (Integer) v.getTag();
			showRemovalDialog();
		}
	};

	private void showRemovalDialog() {
		final OishiiBasket basket = AccountStatus.getInstance(
				getApplicationContext()).getBasket();
		BasketItem item = basket.getItem(removalIndex);
		final Dialog dialog = new Dialog(Basket.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.modal_dialog);
		dialog.setTitle(null);
		TextView tv = (TextView) dialog.findViewById(R.id.errMsg);

		StringBuilder builder = new StringBuilder();
		builder.append("Remove \"");
		builder.append("<b>");
		builder.append(item.getName());
		builder.append("\"</b>");
		builder.append(" ?");
		tv.setText(Html.fromHtml(builder.toString()));
		dialog.findViewById(R.id.btnOk).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						basket.removeItem(removalIndex);
						populateBasket();
						setBasketPrice();
						dialog.dismiss();
					}
				});
		dialog.findViewById(R.id.btnCancel).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
		dialog.show();
	}

	View.OnClickListener todaysMenu = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(Basket.this, TodaysMenu.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	};

	private void populateBasket() {
		final OishiiBasket basket = AccountStatus.getInstance(
				getApplicationContext()).getBasket();
		List<BasketItem> items = basket.getBasketItems();
		int count = items.size();
		if (items.isEmpty()) {
			findViewById(R.id.basketParent).setVisibility(View.GONE);
			findViewById(R.id.emptyBasket).setVisibility(View.VISIBLE);
			findViewById(R.id.todaysMenu).setOnClickListener(todaysMenu);

		} else {
			/* basket is not empty */
			findViewById(R.id.browseMenu).setOnClickListener(todaysMenu);
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
				temp = "£" + (item.getPrice() * item.getCount());
				tv = (TextView) basketItem.findViewById(R.id.price);
				tv.setText(temp);
				tv.setTag(new Integer(i));
				tv.setOnClickListener(removeItemListener);
				View v = basketItem.findViewById(R.id.btnDelete);
				v.setTag(new Integer(i));
				v.setOnClickListener(removeItemListener);
				basketParent.addView(basketItem);
			}
			System.out.println("Total->" + basket.getCurrentTotal());
			String subtotal = "£" + basket.getCurrentTotal();
			tv = (TextView) findViewById(R.id.subtotal);
			tv.setText(subtotal);
			float total = basket.isDiscountApplied() ? basket
					.getDiscountedTotal() : basket.getCurrentTotal();
			tv = (TextView) findViewById(R.id.totalPrice);
			tv.setText("£" + String.valueOf(total));
			Button checkout = (Button) findViewById(R.id.btnCheckout);
			if (basket.isDiscountApplied()) {
				checkout.setText(R.string.btn_deltime);
			}

			checkout.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (basket.isDiscountApplied()) {
						/* proceed to set delivery time */
						Intent intent = new Intent(Basket.this,
								DeliveryTime.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						startActivity(intent);

					} else {
						// Intent intent = new Intent(getApplicationContext(),
						// PromoCode.class);
						// intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						// startActivity(intent);
						Intent intent = new Intent();
						if (!AccountStatus.getInstance(getApplicationContext())
								.isSignedIn()) {
							intent.setClass(getApplicationContext(),
									OutOfSession.class);
							intent.putExtra(OutOfSession.SRC_KEY, R.id.basket);
						} else {
							intent = new Intent(getApplicationContext(),
									PromoCode.class);
						}
						startActivity(intent);

					}
				}
			});
			/* if the discount is set,proceed to check out */
			if (basket.isDiscountApplied()) {
				findViewById(R.id.discountParent).setVisibility(View.VISIBLE);
				findViewById(R.id.discountSeparator)
						.setVisibility(View.VISIBLE);
				tv = (TextView) findViewById(R.id.discount);
				tv.setText("£" + String.valueOf(basket.getDiscount()));
			}
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
