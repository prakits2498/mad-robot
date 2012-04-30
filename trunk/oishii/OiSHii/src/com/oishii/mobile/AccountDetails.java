package com.oishii.mobile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.util.HttpSettings;
import com.oishii.mobile.util.HttpSettings.HttpMethod;
import com.oishii.mobile.util.tasks.HttpRequestTask;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;
import com.oishii.mobile.util.tasks.IHttpCallback;

public class AccountDetails extends OishiiBaseActivity {
	final int SCR_ID = 67;
	private final int OPERATION_ACC = 898;

	@Override
	protected void hookInChildViews() {
		// TODO Auto-generated method stub
		if (AccountStatus.getInstance(getApplicationContext())
				.getAccInformation() == null) {
			executeAccountInfoRequest();

		}
	}

	private void executeAccountInfoRequest() {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
		requestWrapper.requestURI = ApplicationConstants.API_MY_ACCOUNT;
		requestWrapper.callback = accountDetailsCallback;
//		HttpSettings settings = new HttpSettings();
//		settings.setHttpMethod(HttpMethod.HTTP_POST);
//		requestWrapper.httpSettings = settings;
		requestWrapper.operationID = OPERATION_ACC;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		AccountStatus accStat = AccountStatus
				.getInstance(getApplicationContext());
		NameValuePair param = new BasicNameValuePair("mac", accStat.getMac());
		params.add(param);
		param = new BasicNameValuePair("sid", accStat.getSid());
		params.add(param);
		requestWrapper.httpParams = params;
		showDialog(getString(R.string.loading_acc));
		new HttpRequestTask().execute(requestWrapper);
	}

	IHttpCallback accountDetailsCallback = new IHttpCallback() {

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

	@Override
	protected int getSreenID() {
		// TODO Auto-generated method stub
		return SCR_ID;
	}

	@Override
	protected int getChildViewLayout() {
		// TODO Auto-generated method stub
		return R.layout.my_account;
	}

	@Override
	protected String getTitleString() {
		// TODO Auto-generated method stub
		return getString(R.string.acc_title);
	}

}
