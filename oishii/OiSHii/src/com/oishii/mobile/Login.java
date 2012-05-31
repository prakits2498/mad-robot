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
import android.widget.Toast;

import com.madrobot.di.plist.NSArray;
import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSNumber;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
import com.oishii.mobile.beans.AccountInformation;
import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.beans.Address;
import com.oishii.mobile.beans.SavedCard;
import com.oishii.mobile.util.HttpSettings;
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
		// findViewById(R.id.footer).setVisibility(View.GONE);
		login = (EditText) findViewById(R.id.username_edit);
		pwd = (EditText) findViewById(R.id.password_edit);
		login.setText("venkatesh.prabhu@kieon.com");
		pwd.setText("kieon123");

	}

	private void executeLoginRequest() {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper(getApplicationContext());
		requestWrapper.requestURI = ApplicationConstants.API_LOGIN;
		requestWrapper.callback = loginCallback;
		requestWrapper.httpSettings
				.setHttpMethod(ApplicationConstants.HTTP_METHOD);
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
				// get account information
				executeAccountInfoRequest(accountDetailsCallback);

			} else {
				showErrorDialog(t.toString());
			}

		}
	};

	protected void executeAccountInfoRequest(IHttpCallback listener) {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper(getApplicationContext());
		requestWrapper.requestURI = ApplicationConstants.API_MY_ACCOUNT;
		requestWrapper.callback = listener;
		HttpSettings settings = new HttpSettings();
		settings.setHttpMethod(ApplicationConstants.HTTP_METHOD);
		requestWrapper.httpSettings = settings;
		requestWrapper.operationID = 63;
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

	protected IHttpCallback accountDetailsCallback = new IHttpCallback() {

		@Override
		public Object populateBean(InputStream is, int operationId) {
			NSObject object = null;
			try {
				object = PropertyListParser.parse(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (object != null) {
				NSArray array = (NSArray) object;
				AccountInformation menuList;
				try {
					menuList = getAccountInfo(array);
					return menuList;
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			return null;
		}

		@Override
		public void onFailure(int message, int operationID) {
			processFailure(message);
		}

		@Override
		public void bindUI(Object t, int operationId) {
			AccountInformation info = (AccountInformation) t;
			AccountStatus.getInstance(getApplicationContext())
					.setAccInformation(info);
			hideDialog();
			final int nextScreen = getIntent().getIntExtra(
					OutOfSession.SRC_KEY, 0);
			Class<? extends OishiiBaseActivity> targetClass = null;
			switch (nextScreen) {
			case R.id.basket:
				// targetClass=;//titleString = R.string.checkout;
				targetClass = PromoCode.class;//Basket.class;
				break;
			case R.id.myacc:
				targetClass = AccountDetails.class;
				break;
			case R.id.history:
				targetClass = History.class;
				break;
			}
			if (targetClass != null) {
				finish();
				Intent intent = new Intent(Login.this, targetClass);
				startActivity(intent);
			}

		}
	};

	protected AccountInformation getAccountInfo(NSArray obj) throws Exception {
		AccountInformation info = new AccountInformation();
		NSDictionary dict = (NSDictionary) obj.objectAtIndex(0);
		NSDictionary details = (NSDictionary) dict.objectForKey("details");
		String str = details.objectForKey("title").toString();
		info.setTitle(str);
		str = details.objectForKey("firstname").toString();
		info.setFirstname(str);
		str = details.objectForKey("lastname").toString();
		info.setLastname(str);
		str = details.objectForKey("email").toString();
		info.setEmail(str);
		NSNumber no = (NSNumber) details.objectForKey("subscribed");
		info.setSubscribed(no.intValue());
		/* set the addresses */
		NSArray arr = (NSArray) details.objectForKey("address");
		List<Address> addressList = new ArrayList<Address>();
		NSDictionary temp;
		int count = arr.count();
		for (int i = 0; i < count; i++) {
			temp = (NSDictionary) arr.objectAtIndex(i);
			com.oishii.mobile.beans.Address address = new com.oishii.mobile.beans.Address();
			no = (NSNumber) temp.objectForKey("id");
			address.setId(no.intValue());
			str = temp.objectForKey("company").toString();
			address.setCompany(str);
			str = temp.objectForKey("floor").toString();
			address.setFloor(str);
			str = temp.objectForKey("address").toString();
			address.setAddress(str);
			str = temp.objectForKey("city").toString();
			address.setCity(str);
			str = temp.objectForKey("postcode").toString();
			address.setPostCode(str);
			str = temp.objectForKey("mobile").toString();
			address.setMobile(str);
			str = temp.objectForKey("shipping").toString();
			
//			boolean isShipping = str.equals("1");
			address.setShipping(str);
			str = temp.objectForKey("billing").toString();
//			boolean isBilling = str.equals("1");
			address.setBilling(str);
			addressList.add(address);
			
		}
		info.setAddresses(addressList);
		arr = (NSArray) details.objectForKey("cc");
		count = arr.count();
		List<SavedCard> cardsList = new ArrayList<SavedCard>();
		for (int i = 0; i < count; i++) {
			temp = (NSDictionary) arr.objectAtIndex(i);
			SavedCard card = new SavedCard();
			str = temp.objectForKey("token").toString();
			card.setToken(str);
			str = temp.objectForKey("type").toString();
			card.setType(str);
			str = temp.objectForKey("number").toString();
			card.setNumber(str);
			cardsList.add(card);
		}
		info.setSavedCards(cardsList);
		return info;
	}

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
			// errors.append(newline);
		}
		// if(TextUtils.isValidEmailAddress(login.getText().toString())){
		// errors.append(getString(R.string.error_invalid_email));
		// errors.append(newline);
		// }
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
			titleString = R.string.title_history;
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
		return R.layout.login;
	}

	@Override
	protected int getParentScreenId() {
		int source = getIntent().getIntExtra(OutOfSession.SRC_KEY, 0);
		switch (source) {
		case R.id.basket:
			return R.id.basket;
		case R.id.myacc:
			return R.id.myacc;
		case R.id.history:
			return R.id.history;
		}
		return R.id.myacc;
	}

}