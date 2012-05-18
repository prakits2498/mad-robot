package com.oishii.mobile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSNumber;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.beans.BasketItem;
import com.oishii.mobile.beans.OishiiBasket;
import com.oishii.mobile.util.tasks.HttpRequestTask;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;
import com.oishii.mobile.util.tasks.IHttpCallback;

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
		findViewById(R.id.btnPromoCode).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (!hasValidText(promoCode)) {
							Toast toast = Toast.makeText(
									getApplicationContext(),
									R.string.error_no_promo, 4000);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						} else {
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

	private IHttpCallback couponCallback = new IHttpCallback() {

		@Override
		public Object populateBean(InputStream is, int operationId) {
			PromoStatus status = new PromoStatus();
			try {
				NSObject object = PropertyListParser.parse(is);
				NSDictionary dict = (NSDictionary) object;
				NSObject dumb = dict.objectForKey("discount");
				if (dumb == null) {
					/* invalid discount code */
					status.isError = true;
					status.errorMessage = dict.objectForKey("message")
							.toString();
				} else {
					NSNumber number = (NSNumber) dumb;
					status.discountPercent = number.floatValue();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return status;
		}

		@Override
		public void onFailure(int message, int operationID) {
			processFailure(message);
		}

		@Override
		public void bindUI(Object t, int operationId) {
			PromoStatus status = (PromoStatus) t;
			hideDialog();
			if (status.isError) {
				showErrorDialog(status.errorMessage);
			} else {
				OishiiBasket basket = AccountStatus.getInstance(
						getApplicationContext()).getBasket();
				basket.setDiscountPercentage(status.discountPercent);
				basket.setCurrentCouponCode(promoCode.getText().toString());
				Intent intent = new Intent(PromoCode.this, Basket.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		}
	};

	private class PromoStatus {
		boolean isError;
		String errorMessage;
		float discountPercent;
	}

	private void executePromocodeRequest() {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
		requestWrapper.requestURI = ApplicationConstants.API_REDEEM_CODE;
		requestWrapper.callback = couponCallback;
		requestWrapper.operationID = 67;
		requestWrapper.httpSettings
				.setHttpMethod(ApplicationConstants.HTTP_METHOD);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		AccountStatus stat = AccountStatus.getInstance(getApplicationContext());
		NameValuePair param = new BasicNameValuePair("sid", stat.getSid());
		params.add(param);
		param = new BasicNameValuePair("mac", stat.getMac());
		params.add(param);
		param = new BasicNameValuePair("couponcode", promoCode.getText()
				.toString());
		params.add(param);
		OishiiBasket basket = stat.getBasket();
		List<BasketItem> items = basket.getBasketItems();
		int itemsCount = items.size();
		// String paramValue;
		String paramName ;
		for (int i = 0; i < itemsCount; i++) {
			BasketItem item = items.get(i);
			
			paramName="shopping_cart["+i+"][productid]";
			param = new BasicNameValuePair(paramName, String.valueOf(item
					.getProdId()));
			params.add(param);
			paramName="shopping_cart["+i+"][quantity]";
			param = new BasicNameValuePair(paramName, String.valueOf(item
					.getCount()));
			params.add(param);
		}
		requestWrapper.httpParams = params;
		showDialog(getString(R.string.loading_promo_code));
		new HttpRequestTask().execute(requestWrapper);

	}
}
