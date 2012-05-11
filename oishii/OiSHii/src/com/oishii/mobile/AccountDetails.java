package com.oishii.mobile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.view.View;

import com.madrobot.di.plist.NSArray;
import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSNumber;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
import com.oishii.mobile.beans.AccountInformation;
import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.beans.Address;
import com.oishii.mobile.beans.MenuData;
import com.oishii.mobile.beans.SavedCard;
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
			executeAccountInfoRequest(accountDetailsCallback);

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
			Class<? extends OishiiBaseActivity> clz = null;
			switch (v.getId()) {
			case R.id.myAccDetails:
				clz = EditAccountDetails.class;
				break;
			case R.id.myAccLocation:
				clz = Locations.class;
				break;
			case R.id.myAccPayment:
				clz = StoredPayments.class;
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

	protected void executeAccountInfoRequest(IHttpCallback listener) {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
		requestWrapper.requestURI = ApplicationConstants.API_MY_ACCOUNT;
		requestWrapper.callback = listener;
		HttpSettings settings = new HttpSettings();
		settings.setHttpMethod(ApplicationConstants.HTTP_METHOD);
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

	protected AccountInformation getAccountInfo(NSArray obj)throws Exception {
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
			boolean isShipping = str.equals("1");
			address.setShipping(isShipping);
			str = temp.objectForKey("billing").toString();
			boolean isBilling = str.equals("1");
			address.setShipping(isBilling);
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

	@Override
	protected int getParentScreenId() {
		// TODO Auto-generated method stub
		return R.id.myacc;
	}

}
