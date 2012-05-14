package com.oishii.mobile;

import java.io.IOException;
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
import android.widget.RadioButton;
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
import com.oishii.mobile.util.HttpSettings;
import com.oishii.mobile.util.HttpSettings.HttpMethod;
import com.oishii.mobile.util.tasks.HttpRequestTask;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;
import com.oishii.mobile.util.tasks.IHttpCallback;

public class Locations extends Login {

	protected IHttpCallback accountCallback = new IHttpCallback() {

		@Override
		public void bindUI(Object t, int operationId) {
			AccountInformation info = (AccountInformation) t;
			AccountStatus.getInstance(getApplicationContext())
					.setAccInformation(info);
			populateLocations();
			hideDialog();

		}

		@Override
		public void onFailure(int message, int operationID) {
			processFailure(message);
		}

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
	};

	private View.OnClickListener addLocation = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			showLocationDetaisDialog();
		}
	};

	Dialog addLocationDialog;

	View.OnClickListener addLocatioOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (isValid()) {
				executeAddLocation();
				resetBillingShipp();
				if (isAddressEdit)
					addLocationDialog.dismiss();
			}
		}
	};

	private EditText address;
	private int addressIndex;
	int billing;

	private boolean isAddressEdit;

	View.OnClickListener billShipListener = new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			String tag = (String) arg0.getTag();
			if (tag.equals("S")) {
				shipping = 1;
				billing = 0;
			} else if (tag.equals("B")) {
				billing = 1;
				shipping = 0;
			}
		}
	};

	IHttpCallback callback = new IHttpCallback() {

		@Override
		public void bindUI(Object t, int operationId) {
			MultipleMessageResult result = (MultipleMessageResult) t;
			hideDialog();
			if (result.isSuccess()) {
				addLocationDialog.dismiss();
				executeAccountInfoRequest(accountCallback);
			} else {
				showErrorDialog(result.getErrors().toString());
			}

		}

		@Override
		public void onFailure(int message, int operationID) {
			processFailure(message);
		}

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
	};

	private EditText city;
	private EditText company;

	private View.OnClickListener deleteListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			addressIndex = (Integer) v.getTag();
			showDeleteConfirmDialog();
		}
	};

	private View.OnClickListener editListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			addressIndex = (Integer) v.getTag();
			isAddressEdit = true;
			showLocationDetaisDialog();
		}
	};

	private EditText floor;
	private EditText mobile;

	LinearLayout parent;

	private EditText postCode;

	int shipping;

	private void executeAddLocation() {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper();

		requestWrapper.requestURI = isAddressEdit ? ApplicationConstants.API_EDIT_LOCATION
				: ApplicationConstants.API_ADD_LOCATION;
		requestWrapper.callback = isAddressEdit ? simpleResultCallback
				: callback;
		requestWrapper.operationID = isAddressEdit ? OPERATION_EDIT
				: OPERATION_ADD;
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
		if (billing == 1) {
			param = new BasicNameValuePair("billing", String.valueOf(billing));
			params.add(param);
		}
		if (shipping == 1) {
			param = new BasicNameValuePair("shipping", String.valueOf(shipping));
			params.add(param);
		}
		if (isAddressEdit) {
			/* add the address id if editing location */
			final List<Address> address = AccountStatus
					.getInstance(getApplicationContext()).getAccInformation()
					.getAddresses();
			address.get(addressIndex).getId();
			param = new BasicNameValuePair("id", String.valueOf(address.get(
					addressIndex).getId()));
			params.add(param);
		}
		requestWrapper.httpParams = params;
		showDialog(getString(isAddressEdit ? R.string.loading_edit_address
				: R.string.loading_add_location));
		isAddressEdit = false;
		new HttpRequestTask().execute(requestWrapper);

	}

	private final static int OPERATION_ADD = 10;
	private final static int OPERATION_EDIT = 20;
	private final static int OPERATION_DELETE = 30;

	private void executeDeleteAddressRequest(int id) {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
		requestWrapper.requestURI = ApplicationConstants.API_DELETE_LOCATION;
		requestWrapper.callback = simpleResultCallback;
		HttpSettings settings = new HttpSettings();
		settings.setHttpMethod(ApplicationConstants.HTTP_METHOD);
		requestWrapper.httpSettings = settings;
		requestWrapper.operationID = OPERATION_DELETE;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		AccountStatus accStat = AccountStatus
				.getInstance(getApplicationContext());
		NameValuePair param = new BasicNameValuePair("mac", accStat.getMac());
		params.add(param);
		param = new BasicNameValuePair("sid", accStat.getSid());
		params.add(param);
		param = new BasicNameValuePair("id", String.valueOf(id));
		params.add(param);
		requestWrapper.httpParams = params;
		showDialog(getString(R.string.loading_del_address));
		new HttpRequestTask().execute(requestWrapper);
	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.locations;
	}

	@Override
	protected int getParentScreenId() {
		return R.id.myacc;
	}

	@Override
	protected int getSreenID() {
		return R.layout.locations;
	}

	@Override
	protected String getTitleString() {
		return getString(R.string.title_locations);
	}

	/**
	 * Sent when simple result is a success
	 * 
	 * @param message
	 */
	@Override
	protected void handleSimpleResultResponse(String message, int operationId) {
		switch (operationId) {
		case OPERATION_EDIT:
			executeAccountInfoRequest(accountCallback);
			break;
		case OPERATION_DELETE:
			final List<Address> address = AccountStatus
					.getInstance(getApplicationContext()).getAccInformation()
					.getAddresses();
			address.remove(addressIndex);
			populateLocations();
			break;
		}

	}

	@Override
	protected void hookInChildViews() {
		Button btnArbit = getArbitartButton();
		btnArbit.setBackgroundResource(R.drawable.btn_title_selector);
		btnArbit.setOnClickListener(addLocation);
		parent = (LinearLayout) findViewById(R.id.parent);
		populateLocations();
	}

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

	private void populateLocations() {
		parent.removeAllViews();
		List<Address> address = AccountStatus
				.getInstance(getApplicationContext()).getAccInformation()
				.getAddresses();
		int size = address.size();
		if (address.isEmpty()) {
			parent.setVisibility(View.GONE);
			findViewById(R.id.noLocations).setVisibility(View.VISIBLE);
		} else {
			parent.setVisibility(View.VISIBLE);
			findViewById(R.id.noLocations).setVisibility(View.GONE);
		}
		LayoutInflater inflater = getLayoutInflater();
		View v;
		TextView tv;
		Address add;
		for (int i = 0; i < address.size(); i++) {
			add = address.get(i);
			v = inflater.inflate(R.layout.location_item, null);
			tv = (TextView) v.findViewById(R.id.address);
			tv.setText(add.toString());
			tv = (TextView) v.findViewById(R.id.type);
			if (add.isBilling().equals("1")) {
				tv.setText("Billing");
			}
			if (add.isShipping().equals("1")) {
				tv.setText("Shipping");
			}
			if (i == (address.size() - 1)) {
				v.findViewById(R.id.sep).setVisibility(View.GONE);
			}
			View view = v.findViewById(R.id.btnDelete);
			view.setTag(new Integer(i));
			view.setOnClickListener(deleteListener);
			view = v.findViewById(R.id.btnEdit);
			view.setTag(new Integer(i));
			view.setOnClickListener(editListener);
			parent.addView(v);
		}

	}

	private MultipleMessageResult processResult(NSDictionary dict)
			throws Exception {
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

	private void resetBillingShipp() {
		shipping = billing = 0;
	}

	private void showDeleteConfirmDialog() {
		final List<Address> address = AccountStatus
				.getInstance(getApplicationContext()).getAccInformation()
				.getAddresses();
		final Dialog dialog = new Dialog(Locations.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.modal_dialog);
		dialog.setTitle(null);
		TextView tv = (TextView) dialog.findViewById(R.id.errMsg);
		tv.setText("Delete Address" + address.get(addressIndex).toString()+"?");
		dialog.findViewById(R.id.btnOk).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						executeDeleteAddressRequest(address.get(addressIndex)
								.getId());
						dialog.dismiss();
					}
				});
		dialog.findViewById(R.id.btnCancel).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
		dialog.show();
	}

	private void showLocationDetaisDialog() {
		addLocationDialog = new Dialog(Locations.this);
		addLocationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		addLocationDialog.setContentView(R.layout.add_location_dialog);
		addLocationDialog.setTitle(R.string.btn_add);
		company = (EditText) addLocationDialog.findViewById(R.id.fld_comp);
		floor = (EditText) addLocationDialog.findViewById(R.id.fld_floor);
		address = (EditText) addLocationDialog.findViewById(R.id.fld_address);
		city = (EditText) addLocationDialog.findViewById(R.id.fld_city);
		postCode = (EditText) addLocationDialog.findViewById(R.id.fld_postcode);
		mobile = (EditText) addLocationDialog.findViewById(R.id.fld_mobile);

		RadioButton billing = (RadioButton) addLocationDialog
				.findViewById(R.id.radioBill);
		billing.setOnClickListener(billShipListener);
		RadioButton shipping = (RadioButton) addLocationDialog
				.findViewById(R.id.radioShip);
		shipping.setOnClickListener(billShipListener);
		Button doneButton = (Button) addLocationDialog
				.findViewById(R.id.btnAddLocation);
		doneButton.setOnClickListener(addLocatioOnClickListener);
		if (isAddressEdit) {
			final List<Address> addre = AccountStatus
					.getInstance(getApplicationContext()).getAccInformation()
					.getAddresses();
			Address add = addre.get(addressIndex);
			company.setText(add.getCompany());
			floor.setText(add.getFloor());
			address.setText(add.getAddress());
			city.setText(add.getCity());
			postCode.setText(add.getPostCode());
			mobile.setText(add.getMobile());
			if (add.isBilling().equals("1")) {
				billing.setChecked(true);
			}
			if (add.isShipping().equals("1")) {
				shipping.setChecked(true);
			}
			doneButton.setText(R.string.btn_editlocation);
		}
		addLocationDialog.show();
	}
}
