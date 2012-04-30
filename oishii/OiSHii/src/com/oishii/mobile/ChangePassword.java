package com.oishii.mobile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSNumber;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.beans.SimpleResult;
import com.oishii.mobile.util.HttpSettings;
import com.oishii.mobile.util.HttpSettings.HttpMethod;
import com.oishii.mobile.util.tasks.HttpRequestTask;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;
import com.oishii.mobile.util.tasks.IHttpCallback;

import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends OishiiBaseActivity {

	EditText currPwd;
	EditText newPwd;
	private final int OPERATION_CHG_PWD = 56;

	@Override
	protected void hookInChildViews() {
		currPwd = (EditText) findViewById(R.id.pwdCurrent);
		newPwd = (EditText) findViewById(R.id.pwdNew);

		findViewById(R.id.btnCreateAcc).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!hasErrors()) {
							executeChangePasswordRequest();
						}
					}
				});
	}


	IHttpCallback changePwdCallback = new IHttpCallback() {

		@Override
		public Object populateBean(InputStream is, int operationId) {
			NSObject object = null;
			try {
				object = PropertyListParser.parse(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (object != null) {
				SimpleResult result = getSimpleResult(object);
				return result;
			} else {
				return null;
			}
		}

		@Override
		public void onFailure(int message, int operationID) {
			processFailure(message);
		}

		@Override
		public void bindUI(Object t, int operationId) {
			// TODO Auto-generated method stub
			hideDialog();
			SimpleResult result = (SimpleResult) t;
			if (result.isSucess()) {
				showChangedDialog(result.getErrorMessage());
			} else {
				showErrorDialog(result.getErrorMessage());
			}

		}
	};

	private void showChangedDialog(String message) {
		showErrorDialog(message, changedListener);

	}

	View.OnClickListener changedListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
			dismissErrorDialog();
		}
	};

	private void executeChangePasswordRequest() {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
		requestWrapper.requestURI = ApplicationConstants.API_CHANGE_PWD;
		requestWrapper.callback = changePwdCallback;
		requestWrapper.operationID = OPERATION_CHG_PWD;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		AccountStatus accStat = AccountStatus
				.getInstance(getApplicationContext());
		NameValuePair param = new BasicNameValuePair("mac", accStat.getMac());
		params.add(param);
		param = new BasicNameValuePair("sid", accStat.getSid());
		params.add(param);
		param = new BasicNameValuePair("npswd", newPwd.getText().toString());
		params.add(param);
		param = new BasicNameValuePair("cpswd", currPwd.getText().toString());
		params.add(param);

		requestWrapper.httpParams = params;
		// HttpSettings settings = new HttpSettings();
		// settings.setHttpMethod(HttpMethod.HTTP_POST);
		// requestWrapper.httpSettings = settings;
		showDialog(getString(R.string.loading_chg_pwd));
		new HttpRequestTask().execute(requestWrapper);
	}

	private boolean hasErrors() {
		String errors = validate();
		if (errors.length() > 0) {
			Toast toast = Toast.makeText(ChangePassword.this, errors, 8000);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return true;
		}
		return false;
	}

	private String validate() {
		String newline = System.getProperty("line.separator");
		StringBuilder errors = new StringBuilder();
		if (!hasValidText(currPwd)) {
			errors.append(getString(R.string.error_no_curr_pwd));
			errors.append(newline);
		}
		if (!hasValidText(newPwd)) {
			errors.append(getString(R.string.error_no_new_pwd));
		}
		return errors.toString();
	}

	@Override
	protected int getSreenID() {
		return R.layout.changepassword;
	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.changepassword;
	}

	@Override
	protected String getTitleString() {
		return getString(R.string.title_chng_pwd);
	}

}
