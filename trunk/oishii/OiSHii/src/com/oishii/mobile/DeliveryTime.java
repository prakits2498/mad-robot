package com.oishii.mobile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.oishii.mobile.util.tasks.HttpRequestTask;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;
import com.oishii.mobile.util.tasks.IHttpCallback;

import android.widget.TextView;

public class DeliveryTime extends OishiiBaseActivity {

	@Override
	protected int getParentScreenId() {
		return R.id.basket;
	}

	@Override
	protected void hookInChildViews() {
		SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
		String format = df.format(new Date(System.currentTimeMillis()));
		TextView date = (TextView) findViewById(R.id.day);
		date.setText(format);
		executeDeliveryTimeRequest();
	}

	IHttpCallback delTimeCallback=new IHttpCallback() {
		
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
	
	protected void executeDeliveryTimeRequest(){
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
		requestWrapper.requestURI = ApplicationConstants.API_MENU_DATA;
		requestWrapper.callback = delTimeCallback;
		requestWrapper.operationID = 45;
		requestWrapper.httpSettings.setHttpMethod(ApplicationConstants.HTTP_METHOD);
		showDialog(getString(R.string.loading_mnu));
		new HttpRequestTask().execute(requestWrapper);
	}
	
	@Override
	protected int getSreenID() {
		return R.layout.delivery_time;
	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.delivery_time;
	}

	@Override
	protected String getTitleString() {
		return getString(R.string.title_check_del);
	}

}
