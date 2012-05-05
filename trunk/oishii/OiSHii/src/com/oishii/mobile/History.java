package com.oishii.mobile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.view.View;

import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.util.HttpSettings;
import com.oishii.mobile.util.tasks.HttpRequestTask;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;
import com.oishii.mobile.util.tasks.IHttpCallback;

public class History extends ListOishiBase {
	private final int OPERATION_HISTORY = 45;

	@Override
	protected int getParentScreenId() {
		return R.id.history;
	}

	IHttpCallback historyCallback = new IHttpCallback() {

		@Override
		public Object populateBean(InputStream is, int operationId) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void onFailure(int message, int operationID) {
			// TODO Auto-generated method stub
			processFailure(message);
		}

		@Override
		public void bindUI(Object t, int operationId) {
			// TODO Auto-generated method stub
			hideDialog();
		}
	};

	private void executeHistoryRequest() {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
		requestWrapper.requestURI = ApplicationConstants.API_MY_HISTORY;
		requestWrapper.callback = historyCallback;
		HttpSettings settings = new HttpSettings();
		// settings.setHttpMethod(HttpMethod.HTTP_POST);
		requestWrapper.httpSettings = settings;
		requestWrapper.operationID = OPERATION_HISTORY;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		AccountStatus accStat = AccountStatus
				.getInstance(getApplicationContext());
		NameValuePair param = new BasicNameValuePair("mac", accStat.getMac());
		params.add(param);
		param = new BasicNameValuePair("sid", accStat.getSid());
		params.add(param);
		requestWrapper.httpParams = params;
		showDialog(getString(R.string.loading_history));
		new HttpRequestTask().execute(requestWrapper);
	}

	@Override
	protected int getSreenID() {
		// TODO Auto-generated method stub
		return R.layout.history;
	}

	@Override
	protected String getTitleString() {
		// TODO Auto-generated method stub
		return getString(R.string.title_history);
	}

	@Override
	protected void hookInListData() {
		// TODO Auto-generated method stub
		executeHistoryRequest();
		findViewById(R.id.shadow_title).setVisibility(View.GONE);
		showOnlyTitle();

	}

}
