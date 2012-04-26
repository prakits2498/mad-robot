package com.oishii.mobile;

import java.net.URI;

import com.oishii.mobile.util.CurrentScreen;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

	public boolean onOptionsItemSelected(MenuItem item) {
		int currentId = CurrentScreen.getInstance().getCurrentScreenID();
		if (currentId == item.getItemId()) {
			return false;
		}
		switch (item.getItemId()) {
		case R.id.offers:
			Intent intent = new Intent(OishiiBaseActivity.this,
					SpecialOffers.class);
			startActivity(intent);
			return true;
		case R.id.about:
			 intent = new Intent(OishiiBaseActivity.this,
					Home.class);
			startActivity(intent);
			return true;
			
		}
		return false;
	}

	private Dialog dialog;

	protected void hideDialog() {
		dialog.dismiss();
	}

	protected void showDialog(String message) {
		// alertDialog = new
		// AlertDialog.Builder(OishiiBaseActivity.this).create();
		// alertDialog.setTitle("Loading niggars");
		// alertDialog.show();

		dialog = new Dialog(OishiiBaseActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.progressdialog);
		TextView tv = (TextView) dialog.findViewById(R.id.textView1);
		tv.setText(message);
		dialog.setCancelable(false);
		dialog.setTitle(null);
		dialog.show();
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
