package com.oishii.mobile;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.beans.CurrentScreen;

public abstract class OishiiBaseActivity extends Activity {
	public static final String TAG = "Oishii";

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
		hookInMenu();
		hookInChildViews();
		CurrentScreen.getInstance().setCurrentScreenID(getSreenID());

	}

	protected void hookInMenu() {
		View v = findViewById(R.id.about);
		v.setOnClickListener(menuListener);
		v = findViewById(R.id.offers);
		v.setOnClickListener(menuListener);
		v = findViewById(R.id.history);
		v.setOnClickListener(menuListener);
		v = findViewById(R.id.myacc);
		v.setOnClickListener(menuListener);
		v = findViewById(R.id.basket);
		v.setOnClickListener(menuListener);

	}

	// static boolean isMenuScreen;
	// static boolean isHomeScreen;
	// public void onBackPressed() {
	// if(isMenuScreen){
	// Intent intent=new Intent(getApplicationContext(),Home.class);
	// startActivity(intent);
	// isMenuScreen=false;
	// isHomeScreen=true;
	// }
	// if(isHomeScreen){
	// System.exit(0);
	// }
	//
	// };

	View.OnClickListener menuListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d(TAG, "Menu clicked");
			int currentId = CurrentScreen.getInstance().getCurrentScreenID();
			if (currentId == v.getId()) {
				return;
			}
			// isMenuScreen=true;
			// isHomeScreen=false;
			// finish();
			Class clz = null;
			switch (v.getId()) {
			case R.id.offers:

				clz = SpecialOffers.class;
				break;
			case R.id.about:
				clz = Home.class;
				break;
			case R.id.myacc:
				if (!AccountStatus.getInstance(getApplicationContext())
						.isSignedIn()) {
					clz = OutOfSession.class;
				} else {
					clz = AccountDetails.class;
				}
				break;

			}
			if (clz != null) {
				Intent intent = new Intent(OishiiBaseActivity.this, clz);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra(OutOfSession.SRC_KEY, v.getId());
				startActivity(intent);
			}
		}
	};

	/**
	 * This method should be used to hook in the listeners
	 */
	protected abstract void hookInChildViews();

	/**
	 * Activities with the same flow should return the same screen id
	 * 
	 * @return
	 */
	protected abstract int getSreenID();

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

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.defaultmenu, menu);
	// return true;
	// }

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
			intent = new Intent(OishiiBaseActivity.this, Home.class);
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

		dialog = new Dialog(OishiiBaseActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.progressdialog);
		// dialog.setCancelable(false);
		dialog.setTitle(null);
		TextView tv = (TextView) dialog.findViewById(R.id.textView1);
		tv.setText(message);
		dialog.show();
	}

}
