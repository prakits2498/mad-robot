package com.oishii.mobile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.view.View;

import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
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

		if (AccountStatus.getInstance(getApplicationContext())
				.getAccInformation() == null) {
			executeAccountInfoRequest();

		}
		findViewById(R.id.myAccDetails).setOnClickListener(
				accountDetailsListener);
		findViewById(R.id.myAccLocation).setOnClickListener(
				accountDetailsListener);
		findViewById(R.id.myAccPayment).setOnClickListener(
				accountDetailsListener);
		findViewById(R.id.myAccPwd).setOnClickListener(accountDetailsListener);
	}

	View.OnClickListener accountDetailsListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Class clz = null;
			switch (v.getId()) {
			case R.id.myAccDetails:
				break;
			case R.id.myAccLocation:
				break;
			case R.id.myAccPayment:
				break;
			case R.id.myAccPwd:
				clz = ChangePassword.class;
				break;
			}
			if (clz != null) {
				Intent intent = new Intent(AccountDetails.this, clz);
				startActivity(intent);
			}

		}
	};

	private void executeAccountInfoRequest() {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
		requestWrapper.requestURI = ApplicationConstants.API_MY_ACCOUNT;
		requestWrapper.callback = accountDetailsCallback;
		HttpSettings settings = new HttpSettings();
		settings.setHttpMethod(HttpMethod.HTTP_POST);
		requestWrapper.httpSettings = settings;
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
			NSObject object = null;
			try {
				object = PropertyListParser.parse(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public void onFailure(int message, int operationID) {
			processFailure(message);
		}

		@Override
		public void bindUI(Object t, int operationId) {
			hideDialog();

		}
	};

	@Override
	protected int getSreenID() {
		return SCR_ID;
	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.my_account;
	}

	@Override
	protected String getTitleString() {
		return getString(R.string.acc_title);
	}

}
