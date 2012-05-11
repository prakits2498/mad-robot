package com.oishii.mobile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.madrobot.di.plist.NSArray;
import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSNumber;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
import com.oishii.mobile.beans.AccountInformation;
import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.beans.Address;
import com.oishii.mobile.beans.MultipleMessageResult;
import com.oishii.mobile.util.HttpSettings.HttpMethod;
import com.oishii.mobile.util.tasks.HttpRequestTask;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;
import com.oishii.mobile.util.tasks.IHttpCallback;

public class Locations extends AccountDetails {

	@Override
	protected int getParentScreenId() {
		return R.id.myacc;
	}

	LinearLayout parent;

	@Override
	protected void hookInChildViews() {
		Button btnArbit = getArbitartButton();
		btnArbit.setBackgroundResource(R.drawable.btn_title_selector);
		btnArbit.setOnClickListener(addLocation);
		parent = (LinearLayout) findViewById(R.id.parent);
		populateLocations();
	}

	private void populateLocations() {
		parent.removeAllViews();
		List<Address> address = AccountStatus
				.getInstance(getApplicationContext()).getAccInformation()
				.getAddresses();
		int size=address.size();
		if(address.isEmpty()){
			parent.setVisibility(View.GONE);
			findViewById(R.id.noLocations).setVisibility(View.VISIBLE);
		}else{
			parent.setVisibility(View.VISIBLE
					);
			findViewById(R.id.noLocations).setVisibility(View.GONE);
		}
		LayoutInflater inflater = getLayoutInflater();
		View v;
		TextView tv;
		Address add;
		System.out.println("LOcation size=>" + address.size());
		for (int i = 0; i < address.size(); i++) {
			add = address.get(i);
			v = inflater.inflate(R.layout.address_field, null);
			tv = (TextView) v.findViewById(R.id.address);
			tv.setText(add.toString());
			tv = (TextView) v.findViewById(R.id.type);
			if (add.isBilling()) {
				tv.setText("Billing");

			}
			if (add.isShipping()) {
				tv.setText("Shipping");
			}
			if (i == (address.size() - 1)) {
				v.findViewById(R.id.sep).setVisibility(View.GONE);
			}

			parent.addView(v);
		}

	}

	Dialog addLocationDialog;
	private View.OnClickListener addLocation = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			addLocationDialog = new Dialog(Locations.this);
			addLocationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			addLocationDialog.setContentView(R.layout.add_location_dialog);
			addLocationDialog.setTitle(R.string.btn_add);
			company = (EditText) addLocationDialog.findViewById(R.id.fld_comp);
			floor = (EditText) addLocationDialog.findViewById(R.id.fld_floor);
			address = (EditText) addLocationDialog
					.findViewById(R.id.fld_address);
			city = (EditText) addLocationDialog.findViewById(R.id.fld_city);
			postCode = (EditText) addLocationDialog
					.findViewById(R.id.fld_postcode);
			mobile = (EditText) addLocationDialog.findViewById(R.id.fld_mobile);

			addLocationDialog.findViewById(R.id.radioBill).setOnClickListener(
					billShipListener);
			addLocationDialog.findViewById(R.id.radioShip).setOnClickListener(
					billShipListener);
			addLocationDialog.findViewById(R.id.btnAddLocation)
					.setOnClickListener(addLocatioOnClickListener);
			addLocationDialog.show();

		}
	};
	int shipping;
	int billing;

	private void resetBillingShipp() {
		shipping = billing = 0;
	}

	View.OnClickListener billShipListener = new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			String tag = (String) arg0.getTag();
			if (tag.equals("S")) {
				shipping = 1;
				billing=0;
			} else if (tag.equals("B")) {
				billing = 1;
				shipping=0;
			}
			System.out.println("BIlling->"+billing+"Sh"+shipping);
		}
	};

	View.OnClickListener addLocatioOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (isValid()) {
				executeAddLocation();
				resetBillingShipp();
			}
		}
	};

	private EditText company;
	private EditText floor;
	private EditText address;
	private EditText city;
	private EditText postCode;
	private EditText mobile;

	private boolean isValid() {
		EditText[] text = new EditText[] { company, floor, address, city,
				postCode, mobile };
		String newline = System.getProperty("line.separator");
		StringBuilder errors = new StringBuilder();
		for (int i = 0; i < text.length; i++) {
			if (!hasValidText(text[i])) {
				errors.append("* Please enter your ");
				errors.append(text[i].getTag());
				errors.append(newline);
			}
		}
		if (errors.length() > 0) {
			Toast t = Toast.makeText(getApplicationContext(), errors, 4000);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
			return false;
		}
		return true;
	}

	private void executeAddLocation() {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
		requestWrapper.requestURI = ApplicationConstants.API_ADD_LOCATION;
		requestWrapper.callback = callback;
		requestWrapper.operationID = 67;
		requestWrapper.httpSettings.setHttpMethod(HttpMethod.HTTP_POST);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		AccountStatus stat = AccountStatus.getInstance(getApplicationContext());
		NameValuePair param = new BasicNameValuePair("sid", stat.getSid());
		params.add(param);
		param = new BasicNameValuePair("mac", stat.getMac());
		params.add(param);
		param = new BasicNameValuePair("comp", company.getText().toString());
		params.add(param);
		param = new BasicNameValuePair("city", city.getText().toString());
		params.add(param);
		param = new BasicNameValuePair("floor", floor.getText().toString());
		params.add(param);
		param = new BasicNameValuePair("address", address.getText().toString());
		params.add(param);
		param = new BasicNameValuePair("postcode", postCode.getText()
				.toString());
		params.add(param);
		param = new BasicNameValuePair("telephone", mobile.getText().toString());
		params.add(param);
		param = new BasicNameValuePair("billing", String.valueOf(billing));
		params.add(param);
		param = new BasicNameValuePair("shipping", String.valueOf(shipping));
		params.add(param);
		requestWrapper.httpParams = params;
		showDialog(getString(R.string.loading_add_location));
		new HttpRequestTask().execute(requestWrapper);

	}

	private MultipleMessageResult processResult(NSDictionary dict)throws Exception {
		MultipleMessageResult result = new MultipleMessageResult();
		NSNumber sucessFalg = (NSNumber) dict.objectForKey("success");
		result.setSuccess(sucessFalg.boolValue());
		StringBuilder errors = new StringBuilder();
		NSArray array = (NSArray) dict.objectForKey("messages");
		String str;
		NSDictionary msg;
		if (array != null) {
			String newline = System.getProperty("line.separator");
			for (int i = 0; i < array.count(); i++) {
				msg = (NSDictionary) array.objectAtIndex(i);
				str = msg.objectForKey("message").toString();
				errors.append(str);
				errors.append(newline);

			}
		}
		result.setErrors(errors);
		return result;

	}

	IHttpCallback callback = new IHttpCallback() {

		@Override
		public Object populateBean(InputStream is, int operationId) {
			NSObject object = null;
			try {
				object = PropertyListParser.parse(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (object != null) {
				NSDictionary array = (NSDictionary) object;
				MultipleMessageResult menuList;
				try {
					menuList = processResult(array);
					return menuList;
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			return null;
		}

		@Override
		public void onFailure(int message, int operationID) {
			// TODO Auto-generated method stub
			processFailure(message);
		}

		@Override
		public void bindUI(Object t, int operationId) {
			MultipleMessageResult result = (MultipleMessageResult) t;
			if (result.isSuccess()) {
				addLocationDialog.dismiss();
				hideDialog();
				executeAccountInfoRequest(accountCallback);
			} else {
				showErrorDialog(result.getErrors().toString());
			}

		}
	};

	protected IHttpCallback accountCallback = new IHttpCallback() {

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
			populateLocations();
			hideDialog();

		}
	};

	@Override
	protected int getSreenID() {
		return R.layout.locations;
	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.locations;
	}

	@Override
	protected String getTitleString() {
		return getString(R.string.title_locations);
	}

}
