package com.oishii.mobile;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.view.View;
import android.widget.EditText;

import com.oishii.mobile.beans.AccountInformation;
import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.util.TextUtils;
import com.oishii.mobile.util.tasks.HttpRequestTask;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;

public class EditAccountDetails extends CreateAccount {

	@Override
	protected int getParentScreenId() {
		return R.id.myacc;
	}

	// EditText title;
	// EditText fName;
	// EditText lName;
	// EditText email;

	@Override
	protected void hookInChildViews() {
		title = (EditText) findViewById(R.id.field_Title);
		fName = (EditText) findViewById(R.id.fld_FirstName);
		lName = (EditText) findViewById(R.id.fld_LastName);
		email = (EditText) findViewById(R.id.fld_Email);
		AccountInformation info = AccountStatus.getInstance(
				getApplicationContext()).getAccInformation();
		title.setText(info.getTitle());
		fName.setText(info.getFirstname());
		lName.setText(info.getLastname());
		email.setText(info.getEmail());
		findViewById(R.id.btnSave).setOnClickListener(saveListener);
	}

	View.OnClickListener saveListener = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			boolean hasErrors = checkForErrors();
			if (!hasErrors) {
				saveDetails();
			}
		}
	};

	protected String validate() {
		// Level 1 validation
		String newline = System.getProperty("line.separator");
		StringBuilder errors = new StringBuilder();
		if (!hasValidText(title)) {
			errors.append(getString(R.string.error_no_title));
			errors.append(newline);
		}
		if (!hasValidText(fName)) {
			errors.append(getString(R.string.error_no_fn));
			errors.append(newline);
		}
		if (!hasValidText(lName)) {
			errors.append(getString(R.string.error_no_ln));
			errors.append(newline);
		}
		if (!hasValidText(email)) {
			errors.append(getString(R.string.error_no_email));
			errors.append(newline);
		}
		if (errors.length() > 0)
			return errors.toString();

		// level 2 validation
		if (!TextUtils.isValidEmailAddress(email.getText().toString())) {
			errors.append(getString(R.string.error_invalid_email));
			errors.append(newline);
		}

		return errors.toString();
	}

	private void saveDetails() {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper(getApplicationContext());
		requestWrapper.requestURI = ApplicationConstants.API_SAVE_ACC_DETAILS;
		requestWrapper.callback = simpleResultCallback;
		requestWrapper.operationID = 67;
		requestWrapper.httpSettings
				.setHttpMethod(ApplicationConstants.HTTP_METHOD);
		requestWrapper.canCache=false;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		AccountStatus stat = AccountStatus.getInstance(getApplicationContext());
		NameValuePair param = new BasicNameValuePair("sid", stat.getSid());
		params.add(param);
		param = new BasicNameValuePair("mac", stat.getMac());
		params.add(param);
		param = new BasicNameValuePair("title", title.getText().toString());
		params.add(param);
		param = new BasicNameValuePair("firstname", fName.getText().toString());
		params.add(param);
		param = new BasicNameValuePair("lastname", lName.getText().toString());
		params.add(param);
		param = new BasicNameValuePair("email", email.getText().toString());
		params.add(param);
		param = new BasicNameValuePair("id", "1");
		params.add(param);
		param = new BasicNameValuePair("is_subcribed", String.valueOf(stat
				.getAccInformation().getSubscribed()));
		params.add(param);
		requestWrapper.httpParams = params;
		showDialog(getString(R.string.loading_save));
		new HttpRequestTask().execute(requestWrapper);
	}

	/**
	 * have to override because create account does something else
	 */
	protected void handleSimpleResultResponse(String message) {
		/* get the latest account details */
		executeAccountInfoRequest(accountDetailsCallback);
	}

	@Override
	protected int getSreenID() {
		return 0;
	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.edit_account_details;
	}

	@Override
	protected String getTitleString() {
		return getString(R.string.title_details);
	}

}
