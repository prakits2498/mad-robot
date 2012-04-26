package com.oishii.mobile;

import java.net.URI;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

	protected void httpSucess(int operation) {

	}

	protected void httpFailure(int operation, String message) {
		// show error message
		Log.e("ERROR->", message);
	}

	protected class RequestWrapper {
		protected URI requestURI;
		/* integer to identify the operation. an activity may user */
		protected int operationID;
	}

}
