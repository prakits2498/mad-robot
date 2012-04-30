package com.oishii.mobile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSNumber;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.util.HttpSettings;
import com.oishii.mobile.util.HttpSettings.HttpMethod;
import com.oishii.mobile.util.TextUtils;
import com.oishii.mobile.util.tasks.HttpRequestTask;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;
import com.oishii.mobile.util.tasks.IHttpCallback;

public class Login extends OishiiBaseActivity {

	private final int OPERATION_LOGIN = 10;

	private EditText login;
	private EditText pwd;

	@Override
	protected void hookInChildViews() {
		findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (validate()) {
					executeLoginRequest();

				}
			}
		});
		findViewById(R.id.footer).setVisibility(View.GONE);
		login = (EditText) findViewById(R.id.username_edit);
		pwd = (EditText) findViewById(R.id.password_edit);

	}

	private void executeLoginRequest() {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
		requestWrapper.requestURI = ApplicationConstants.API_LOGIN;
		requestWrapper.callback = loginCallback;
		HttpSettings settings=new HttpSettings();
		settings.setHttpMethod(HttpMethod.HTTP_POST);
		requestWrapper.httpSettings=settings;
		requestWrapper.operationID = OPERATION_LOGIN;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		NameValuePair param = new BasicNameValuePair("mac", AccountStatus
				.getInstance(getApplicationContext()).getMac());
		params.add(param);
		param = new BasicNameValuePair("login", login.getText().toString());
		params.add(param);
		param = new BasicNameValuePair("pswd", pwd.getText().toString());
		params.add(param);
		requestWrapper.httpParams = params;
		showDialog(getString(R.string.loading_login));
		new HttpRequestTask().execute(requestWrapper);
	}

	IHttpCallback loginCallback = new IHttpCallback() {

		@Override
		public Object populateBean(InputStream is, int operationId) {

			NSObject object = null;
			try {
				object = PropertyListParser.parse(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (object != null) {
				NSDictionary loginResp = (NSDictionary) object;
				String sessionId = loginResp.objectForKey("sessionId")
						.toString();
				NSNumber customerId = (NSNumber) loginResp
						.objectForKey("customerId");
				long custId = customerId.longValue();
				String message = loginResp.objectForKey("message").toString();
				AccountStatus accStatus = AccountStatus
						.getInstance(getApplicationContext());
				if (!(sessionId.length() > 0) || !(custId > 0)) {
					// invalid user
					accStatus.setSignedIn(false);
				} else {
					accStatus.setSignedIn(true);
					accStatus.setCustomerId(custId);
					accStatus.setSid(sessionId);

				}
				return message;
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
			if (AccountStatus.getInstance(getApplicationContext()).isSignedIn()) {
				final int nextScreen = getIntent().getIntExtra(
						OutOfSession.SRC_KEY, 0);
				Class<? extends OishiiBaseActivity> targetClass = null;
				switch (nextScreen) {
				case R.id.basket:
					// targetClass=;//titleString = R.string.checkout;
					break;
				case R.id.myacc:
					// titleString = R.string.acc_title;
					targetClass=AccountDetails.class;
					break;
				case R.id.history:
					// titleString = R.string.history_title;
					break;
				}
				if(targetClass!=null){
					finish();
					Intent intent=new Intent(Login.this,targetClass);
					startActivity(intent);
				}
			} else {
				showErrorDialog(t.toString());
			}

		}
	};

	private boolean validate() {
		String errors = getErrors();
		if (errors.length() > 0) {
			// has errors
			Toast toast = Toast.makeText(Login.this, errors, 8000);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return false;
		}
		return true;
	}

	private String getErrors() {
		String newline = System.getProperty("line.separator");
		StringBuilder errors = new StringBuilder();
		if (!hasValidText(login)) {
			errors.append(getString(R.string.error_no_email));
			errors.append(newline);

		}
		if (!hasValidText(pwd)) {
			errors.append(getString(R.string.error_no_pwd));
//			errors.append(newline);
		}
//		 if(TextUtils.isValidEmailAddress(login.getText().toString())){
//		 errors.append(getString(R.string.error_invalid_email));
//		 errors.append(newline);
//		 }
		return errors.toString();
	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.login;
	}

	@Override
	protected String getTitleString() {
		int source = getIntent().getIntExtra(OutOfSession.SRC_KEY, 0);
		int titleString = R.string.login_title;
		switch (source) {
		case R.id.basket:
			titleString = R.string.checkout;
			break;
		case R.id.myacc:
			titleString = R.string.acc_title;
			break;
		case R.id.history:
			titleString = R.string.history_title;
			break;
		}
		return getString(titleString);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return false;
	}

	// TODO set screen id
	@Override
	protected int getSreenID() {
		// TODO Auto-generated method stub
		return 0;
	}

}