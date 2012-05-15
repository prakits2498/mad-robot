package com.oishii.mobile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.beans.BasketItem;
import com.oishii.mobile.beans.OishiiBasket;
import com.oishii.mobile.util.tasks.HttpRequestTask;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;
import com.oishii.mobile.util.tasks.IHttpCallback;

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
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void onFailure(int message, int operationID) {
			// TODO Auto-generated method stub

		}

		@Override
		public void bindUI(Object t, int operationId) {
			// TODO Auto-generated method stub

		}
	};

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
		String paramName;
		String paramValue;
		for (int i = 0; i < itemsCount; i++) {
			BasketItem item = items.get(i);
			paramName = "shopping_cart[" + i + "]";
			paramValue = "productid=" + item.getProdId() + "&quantity="
					+ item.getCount();
			param = new BasicNameValuePair(paramName, paramValue);
			params.add(param);
		}
		requestWrapper.httpParams = params;
		showDialog(getString(R.string.loading_promo_code));
		new HttpRequestTask().execute(requestWrapper);

	}
}
