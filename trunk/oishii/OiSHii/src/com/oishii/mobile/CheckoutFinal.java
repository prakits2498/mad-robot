package com.oishii.mobile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSNumber;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.beans.BasketItem;
import com.oishii.mobile.beans.OishiiBasket;
import com.oishii.mobile.util.HttpSettings.HttpMethod;
import com.oishii.mobile.util.tasks.HttpRequestTask;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;
import com.oishii.mobile.util.tasks.IHttpCallback;

public class CheckoutFinal extends OishiiBaseActivity {

	@Override
	protected int getParentScreenId() {
		return R.id.basket;
	}

	WebView webView;

	@Override
	protected void hookInChildViews() {
		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new CheckOutClient());
		executeValidateCheckout();

	}

	class CheckOutClient extends WebViewClient {
		public void onPageFinished(WebView view, String url) {
			// do your stuff here
			System.out.println("LOading complete"+url);
			hideDialog();
		}
	}

	IHttpCallback finalCallback=new IHttpCallback() {
		
		@Override
		public Object populateBean(InputStream is, int operationId) {
			// TODO Auto-generated method stub
			String test = null;
			try {
				test=asString(is);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return test;
		}
		
		@Override
		public void onFailure(int message, int operationID) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void bindUI(Object t, int operationId) {
			// TODO Auto-generated method stub
			hideDialog();
			showDialog(getString(R.string.loading_directing));
			webView.loadDataWithBaseURL(ApplicationConstants.API_FINAL_CHECKOUT.toString(), t.toString(),
		            "text/html", HTTP.UTF_8, null);
		}
	};
	
	private void executeFinalCheckout(){
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
		requestWrapper.requestURI = ApplicationConstants.API_FINAL_CHECKOUT;
		requestWrapper.callback = finalCallback;// validateCallback;
		requestWrapper.operationID = 67;
		requestWrapper.httpSettings
				.setHttpMethod(HttpMethod.HTTP_POST);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		AccountStatus stat = AccountStatus.getInstance(getApplicationContext());
		OishiiBasket basket = stat.getBasket();

		/* Add params */
		NameValuePair param = new BasicNameValuePair("sid", stat.getSid());
		params.add(param);
		param = new BasicNameValuePair("mac", stat.getMac());
		params.add(param);
		param = new BasicNameValuePair("couponcode",
				basket.getCurrentCouponCode());
		params.add(param);
		param = new BasicNameValuePair("billingid", String.valueOf(basket
				.getBillingAddressId()));
		params.add(param);
		param = new BasicNameValuePair("shippingid", String.valueOf(basket
				.getShippingAddressId()));
		params.add(param);
		param = new BasicNameValuePair("deliverytime", String.valueOf(basket
				.getDeliveryTime()));
		params.add(param);
		param = new BasicNameValuePair("token", "sagepay");
		params.add(param);
		if (basket.getSavedToken() == null) {
			param = new BasicNameValuePair("is_saved_cc", String.valueOf(basket
					.isSaveCC()));
			params.add(param);
		} else {
			param = new BasicNameValuePair("savedtoken", basket.getSavedToken());
			params.add(param);
			// param = new BasicNameValuePair("is_saved_cc","yes");
			// params.add(param);
		}
		List<BasketItem> items = basket.getBasketItems();
		int itemsCount = items.size();
		// String paramValue;
		String paramName = "shopping_cart[]";
		for (int i = 0; i < itemsCount; i++) {
			BasketItem item = items.get(i);

			paramName = "shopping_cart[" + i + "][productid]";
			param = new BasicNameValuePair(paramName, String.valueOf(item
					.getProdId()));
			params.add(param);
			paramName = "shopping_cart[" + i + "][quantity]";
			param = new BasicNameValuePair(paramName, String.valueOf(item
					.getCount()));
			params.add(param);
		}
		requestWrapper.httpParams = params;
		showDialog(getString(R.string.loading_processing));
		new HttpRequestTask().execute(requestWrapper);
//		showDialog("Loading webview");
//		webView.loadUrl("http://google.com");
		
		
	}
	
	
	class ValidateResponse {
		@Override
		public String toString() {
			return "ValidateResponse [message=" + message + ", success="
					+ success + "]";
		}
		String message;
		boolean success;
	}

	IHttpCallback validateCallback = new IHttpCallback() {

		@Override
		public Object populateBean(InputStream is, int operationId) {
			ValidateResponse response = new ValidateResponse();
			try {
				NSObject object = PropertyListParser.parse(is);
				NSDictionary dict = (NSDictionary) object;
				NSNumber sucessFalg = (NSNumber) dict.objectForKey("success");
				response.success = sucessFalg.boolValue();
				response.message = dict.objectForKey("message").toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return response;
		}

		@Override
		public void onFailure(int message, int operationID) {
			processFailure(message);
		}

		@Override
		public void bindUI(Object t, int operationId) {
			ValidateResponse resp=(ValidateResponse) t;
			hideDialog();
			if(resp.success){
				executeFinalCheckout();
			}else{
				showErrorDialog(resp.message);
			}
		}
	};

	private void executeValidateCheckout() {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
		requestWrapper.requestURI = ApplicationConstants.API_VALIDATE_CHECKOUT;
		requestWrapper.callback = validateCallback;// validateCallback;
		requestWrapper.operationID = 67;
		requestWrapper.httpSettings
				.setHttpMethod(ApplicationConstants.HTTP_METHOD);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		AccountStatus stat = AccountStatus.getInstance(getApplicationContext());
		OishiiBasket basket = stat.getBasket();

		/* Add params */
		NameValuePair param = new BasicNameValuePair("sid", stat.getSid());
		params.add(param);
		param = new BasicNameValuePair("mac", stat.getMac());
		params.add(param);
		param = new BasicNameValuePair("couponcode",
				basket.getCurrentCouponCode());
		params.add(param);
		param = new BasicNameValuePair("billingid", String.valueOf(basket
				.getBillingAddressId()));
		params.add(param);
		param = new BasicNameValuePair("shippingid", String.valueOf(basket
				.getShippingAddressId()));
		params.add(param);
		param = new BasicNameValuePair("deliverytime", String.valueOf(basket
				.getDeliveryTime()));
		params.add(param);
		param = new BasicNameValuePair("token", "sagepay");
		params.add(param);
		if (basket.getSavedToken() == null) {
			param = new BasicNameValuePair("is_saved_cc", String.valueOf(basket
					.isSaveCC()));
			params.add(param);
		} else {
			param = new BasicNameValuePair("savedtoken", basket.getSavedToken());
			params.add(param);
			// param = new BasicNameValuePair("is_saved_cc","yes");
			// params.add(param);
		}
		List<BasketItem> items = basket.getBasketItems();
		int itemsCount = items.size();
		// String paramValue;
		String paramName = "shopping_cart[]";
		for (int i = 0; i < itemsCount; i++) {
			BasketItem item = items.get(i);

			paramName = "shopping_cart[" + i + "][productid]";
			param = new BasicNameValuePair(paramName, String.valueOf(item
					.getProdId()));
			params.add(param);
			paramName = "shopping_cart[" + i + "][quantity]";
			param = new BasicNameValuePair(paramName, String.valueOf(item
					.getCount()));
			params.add(param);
		}
		requestWrapper.httpParams = params;
		showDialog(getString(R.string.loading_validate));
		new HttpRequestTask().execute(requestWrapper);
	}

	@Override
	protected int getSreenID() {
		// TODO Auto-generated method stub
		return R.layout.checkout_final;
	}

	@Override
	protected int getChildViewLayout() {
		// TODO Auto-generated method stub
		return R.layout.checkout_final;
	}

	@Override
	protected String getTitleString() {
		// TODO Auto-generated method stub
		return getString(R.string.title_checkout);
	}

}
