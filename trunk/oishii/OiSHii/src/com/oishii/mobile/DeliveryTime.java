package com.oishii.mobile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.madrobot.di.plist.NSArray;
import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.beans.OishiiBasket;
import com.oishii.mobile.util.tasks.HttpRequestTask;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;
import com.oishii.mobile.util.tasks.IHttpCallback;

public class DeliveryTime extends OishiiBaseActivity {

	@Override
	protected int getParentScreenId() {
		return R.id.basket;
	}

	@Override
	protected void hookInChildViews() {
		SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
		String format = df.format(new Date(System.currentTimeMillis()));
		TextView date = (TextView) findViewById(R.id.day);
		date.setText(format);
		findViewById(R.id.btnSelTime).setOnClickListener(timeListener);
		findViewById(R.id.btnContinue).setOnClickListener(timeListener);
		executeDeliveryTimeRequest();
	}

	private View.OnClickListener timeListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnContinue:
				//start same activity for billing address.
				Intent intent=new Intent(DeliveryTime.this,Locations.class);
				intent.putExtra(Locations.ACTION_SELECT, true);
				intent.putExtra(Locations.ACTION_SELECT_TYPE, Locations.ACTION_SHIPPING_ADDRESS);
				startActivity(intent);
				break;
			case R.id.btnSelTime:
				if (currentTimeDetails == null
						|| currentTimeDetails.time == null) {
					executeDeliveryTimeRequest();
				} else
					showTimeDialog();
				break;
			}
		}
	};
	private Dialog timeDialog;

	private void showTimeDialog() {
		timeDialog = new Dialog(DeliveryTime.this);
		timeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		timeDialog.setContentView(R.layout.delivery_time_dialog);
		ListView timeList = (ListView) timeDialog.findViewById(R.id.listView1);
		ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this,
				R.layout.time_listview, currentTimeDetails.time);
		timeList.setAdapter(timeAdapter);
		timeList.setOnItemClickListener(listClick);
		timeDialog.show();
	}

	AdapterView.OnItemClickListener listClick = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			String time = currentTimeDetails.time.get(arg2);
			OishiiBasket basket = AccountStatus.getInstance(
					getApplicationContext()).getBasket();
			basket.setDeliveryTime(time);
			TextView tv = (TextView) findViewById(R.id.time);
			tv.setText(time);
			findViewById(R.id.btnContinue).setVisibility(View.VISIBLE);
			timeDialog.dismiss();
		}
	};

	private TimeEnvelope getTime(NSArray array) throws Exception {
		TimeEnvelope envelope = new TimeEnvelope();
		ArrayList<String> time = new ArrayList<String>();
		int count = array.count();
		NSDictionary dict;
		String timeStr;
		for (int i = 0; i < count; i++) {
			dict = (NSDictionary) array.objectAtIndex(i);
			timeStr = dict.objectForKey("Time").toString();
			if (!timeStr.contains(":")) {
				envelope.success = false;
				envelope.message = "Delivery Closed!";
			}
			time.add(timeStr);
		}
		envelope.time = time;
		return envelope;
	}

	private class TimeEnvelope {
		ArrayList<String> time;
		boolean success = true;
		String message;
	}

	private TimeEnvelope currentTimeDetails;

	private IHttpCallback delTimeCallback = new IHttpCallback() {

		@Override
		public Object populateBean(InputStream is, int operationId) {
			NSObject object = null;
			try {
				object = PropertyListParser.parse(is);
				return getTime((NSArray) object);
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
			TimeEnvelope time = (TimeEnvelope) t;
			if (time.success) {
				currentTimeDetails = time;
			} else {
				showErrorDialog(time.message);
				findViewById(R.id.btnSelTime).setVisibility(View.GONE);
			}
		}
	};

	protected void executeDeliveryTimeRequest() {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
		requestWrapper.requestURI = ApplicationConstants.API_DELIVERY_TIME;
		requestWrapper.callback = delTimeCallback;
		requestWrapper.operationID = 45;
		requestWrapper.httpSettings
				.setHttpMethod(ApplicationConstants.HTTP_METHOD);
		showDialog(getString(R.string.loading_delivery_time));
		new HttpRequestTask().execute(requestWrapper);
	}

	@Override
	protected int getSreenID() {
		return R.layout.delivery_time;
	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.delivery_time;
	}

	@Override
	protected String getTitleString() {
		return getString(R.string.title_check_del);
	}

}
