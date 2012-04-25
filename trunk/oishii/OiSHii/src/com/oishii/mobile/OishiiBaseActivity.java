package com.oishii.mobile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oishii.mobile.util.CarrierHelper;
import com.oishii.mobile.util.HttpSettings;
import com.oishii.mobile.util.HttpTaskHelper;

public abstract class OishiiBaseActivity extends Activity {

	@Override
	public final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hookInViews();

	}

	protected void hookInViews() {
		ViewGroup parent = (ViewGroup) View.inflate(this, R.layout.oishiibase,
				null);

		RelativeLayout contentArea = (RelativeLayout) parent
				.findViewById(R.id.contentArea);
		getLayoutInflater().inflate(getChildViewLayout(), contentArea);
		TextView title = (TextView) parent.findViewById(R.id.headertitle);
		title.setText(getTitleString());
		setContentView(parent);

		hookInChildViews();
	}

	/**
	 * This method should be used to hook in the listeners
	 */
	protected abstract void hookInChildViews();

	/**
	 * Get the layout contents of the child view
	 * 
	 * @return
	 */
	protected abstract int getChildViewLayout();

	protected abstract String getTitleString();

	protected void showOnlyLogo() {
		findViewById(R.id.logo).setVisibility(View.VISIBLE);
		findViewById(R.id.headertitle).setVisibility(View.GONE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.defaultmenu, menu);

		return true;
	}

	AlertDialog alertDialog;

	protected void hideDialog() {
		alertDialog.dismiss();
	}

	protected void showDialog() {
		alertDialog = new AlertDialog.Builder(OishiiBaseActivity.this).create();
		alertDialog.setTitle("Loading niggars");
		alertDialog.show();
	}

	/**
	 * 
	 * @param is
	 * @param v
	 * @param operation
	 * @return true if the operation was successful
	 */
	protected abstract boolean populateViewFromHttp(InputStream is, View v,
			int operation);

	protected void httpSucess(int operation) {

	}

	protected void httpFailure(int operation, String message) {
		// show error message
		Log.e("ERROR->", message);
	}

	protected class OishiiHttpTask extends
			AsyncTask<HttpUIWrapper, View, ResultWrapper> {
		protected void onPreExecute() {

			showDialog();
		}

		@Override
		protected ResultWrapper doInBackground(HttpUIWrapper... wrapper) {
			if(wrapper[0].operation==0){
				throw new IllegalArgumentException("No operation code set!");
			}
			ResultWrapper resultWrap = new ResultWrapper();
			resultWrap.operationCode = wrapper[0].operation;
			CarrierHelper commHelper = CarrierHelper
					.getInstance(getApplicationContext());
			if (commHelper.getCurrentCarrier() == null) {
				Log.d("Oishii","No Data carrier available");
				resultWrap.resultMessage = "No data network available!";
				resultWrap.result = false;
				return resultWrap;
			}
			HttpTaskHelper helper = new com.oishii.mobile.util.HttpTaskHelper(
					wrapper[0].uri);
			helper.setHttpSettings(wrapper[0].httpSettings);
			boolean result = false;
			try {
				HttpEntity entity = helper.execute();
				result = populateViewFromHttp(entity.getContent(),
						wrapper[0].view, wrapper[0].operation);
			} catch (IOException e) {
				Log.e("Oishii","I/O Exception");
				e.printStackTrace();
				resultWrap.resultMessage = "No data network available!";
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}

			resultWrap.result = result;
			resultWrap.showDialog = wrapper[0].showLoadingDialog;
			return resultWrap;
		}

		protected void onPostExecute(ResultWrapper obj) {
			hideDialog();
			if (obj.result) {
				httpSucess(obj.operationCode);
			} else {
				httpFailure(obj.operationCode, obj.resultMessage);
			}
		}
	}

	protected class SlientOishiHttpTask extends OishiiHttpTask {
		protected void onPreExecute() {

		}

		protected void onPostExecute(ResultWrapper obj) {
		}
	}

	protected class HttpUIWrapper {
		protected View view;
		protected URI uri;
		protected HttpSettings httpSettings = new HttpSettings();
		protected int operation;
		boolean showLoadingDialog = true;

	}

	private class ResultWrapper {
		boolean result;
		int operationCode;
		String resultMessage;
		boolean showDialog;
	}
}
