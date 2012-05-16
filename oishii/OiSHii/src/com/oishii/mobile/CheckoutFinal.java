package com.oishii.mobile;

import java.io.IOException;
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

public class CheckoutFinal extends OishiiBaseActivity {

	@Override
	protected int getParentScreenId() {
		return R.id.basket;
	}

	@Override
	protected void hookInChildViews() {
		executeValidateCheckout();

	}
	
	IHttpCallback validateCallback=new IHttpCallback() {
		
		@Override
		public Object populateBean(InputStream is, int operationId) {
			// TODO Auto-generated method stub
			try {
				System.out.println(asString(is));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	private void executeValidateCheckout() {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
		requestWrapper.requestURI = ApplicationConstants.API_VALIDATE_CHECKOUT;
		requestWrapper.callback = simpleResultCallback;//validateCallback;
		requestWrapper.operationID = 67;
		requestWrapper.httpSettings
				.setHttpMethod(ApplicationConstants.HTTP_METHOD);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		AccountStatus stat = AccountStatus.getInstance(getApplicationContext());
		OishiiBasket basket = stat.getBasket();
	
		/*Add params*/
		NameValuePair param = new BasicNameValuePair("sid", stat.getSid());
		params.add(param);
		param = new BasicNameValuePair("mac", stat.getMac());
		params.add(param);
		param = new BasicNameValuePair("couponcode", basket.getCurrentCouponCode());
		params.add(param);
		param = new BasicNameValuePair("billingid",String.valueOf(basket.getBillingAddressId()));
		params.add(param);
		param = new BasicNameValuePair("shippingid",String.valueOf(basket.getShippingAddressId()));
		params.add(param);
		param = new BasicNameValuePair("deliverytime",String.valueOf(basket.getDeliveryTime()));
		params.add(param);
		param = new BasicNameValuePair("token","sagepay");
		params.add(param);
		if(basket.getSavedToken()==null){
			param = new BasicNameValuePair("is_saved_cc",String.valueOf(basket.isSaveCC()));
			params.add(param);
		}else{
			param = new BasicNameValuePair("savedtoken",basket.getSavedToken());
			params.add(param);
//			param = new BasicNameValuePair("is_saved_cc","yes");
//			params.add(param);
		}
		List<BasketItem> items = basket.getBasketItems();
		int itemsCount = items.size();
		// String paramValue;
		String paramName = "shopping_cart[]";
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
