package com.oishii.mobile;

import java.io.InputStream;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSNumber;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.beans.CurrentScreen;
import com.oishii.mobile.beans.SimpleResult;
import com.oishii.mobile.util.tasks.IHttpCallback;

public abstract class OishiiBaseActivity extends Activity {
	public static final String TAG = "Oishii";

	@Override
	public final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hookInViews();

	}

	protected abstract int getParentScreenId();

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			// Do Stuff
			if (menuView.getVisibility() == View.VISIBLE) {
				menuView.setVisibility(View.GONE);
			} else {
				menuView.setVisibility(View.VISIBLE);
			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	protected boolean hasValidText(EditText et) {
		if (et != null) {
			String text = et.getText().toString().trim();
			if (text.length() > 0) {
				return true;
			}

		}
		return false;
	}

	View menuView;
	View menuParent;

	protected void hookInViews() {
		ViewGroup parent = (ViewGroup) View.inflate(this, R.layout.oishiibase,
				null);

		RelativeLayout contentArea = (RelativeLayout) parent
				.findViewById(R.id.contentArea);
		getLayoutInflater().inflate(getChildViewLayout(), contentArea);
		TextView title = (TextView) parent.findViewById(R.id.headertitle);
		title.setText(getTitleString());
		setContentView(parent);
		// Shader textShader=new LinearGradient(0, 0, 0, 20,
		// new int[]{Color.WHITE,Color.GRAY},
		// new float[]{0, 1}, TileMode.CLAMP);
		// TextView tv= (TextView) findViewById(R.id.txtAbout);
		// tv.getPaint().setShader(textShader);
		hookInMenu();
		hookInChildViews();
		setMenuView();
		setCurrentScreen();

	}

	private void setCurrentScreen() {
		CurrentScreen.getInstance().setCurrentScreenID(getSreenID());

	}

	private void setMenuView() {
		menuView = findViewById(getParentScreenId());
	}

	protected void hookInMenu() {
		menuParent = findViewById(R.id.footer);
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
			Intent intent = new Intent();
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
					// intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				} else {
					clz = AccountDetails.class;
				}
				break;
			case R.id.basket:
				showNotImplToast();
				break;
			case R.id.history:
				if (!AccountStatus.getInstance(getApplicationContext())
						.isSignedIn()) {

					clz = OutOfSession.class;
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				} else {
					clz = History.class;
				}
				break;
			}
			if (clz != null) {
				// Intent intent = new Intent(OishiiBaseActivity.this, clz);
				intent.setClass(OishiiBaseActivity.this, clz);
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

	protected void showOnlyTitle() {
		findViewById(R.id.logo).setVisibility(View.GONE);
		findViewById(R.id.headertitle).setVisibility(View.VISIBLE);
	}

	View.OnClickListener errorDialogListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			errorDialog.dismiss();
		}
	};

	protected void showErrorDialog(String errorMessage,
			OnClickListener dismissHandler) {
		errorDialog = new Dialog(OishiiBaseActivity.this);
		errorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		errorDialog.setContentView(R.layout.error_dialog);
		TextView errMsg = (TextView) errorDialog.findViewById(R.id.errMsg);
		errMsg.setText(errorMessage);
		errorDialog.findViewById(R.id.btnDismiss).setOnClickListener(
				dismissHandler);

		// TODO set message
		errorDialog.show();
	}

	protected void dismissErrorDialog() {
		if (errorDialog.isShowing())
			errorDialog.dismiss();
	}

	protected void showErrorDialog(String errorMessage) {
		showErrorDialog(errorMessage, errorDialogListener);
	}

	private Dialog dialog;
	private Dialog errorDialog;

	protected void hideDialog() {
		if (dialog.isShowing())
			dialog.dismiss();
	}

	protected void showDialog(String message) {

		dialog = new Dialog(OishiiBaseActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.progressdialog);
		dialog.setCancelable(false);
		dialog.setTitle(null);
		TextView tv = (TextView) dialog.findViewById(R.id.textView1);
		tv.setText(message);
		dialog.show();
	}

	protected void onResume() {
		super.onResume();
		setCurrentScreen();
		System.out.println("ON RESUMEEEE++++++++++++++");
		setSelectedMenu();
	}

	private void setSelectedMenu() {
		int parentScreen = getParentScreenId();
		// View v=findViewById(parentScreen);
		// menuView.setBackgroundColor(0x32ffffff);
		menuView.setBackgroundResource(R.drawable.menu_selected_bg);
		ImageView icon = (ImageView) menuView.findViewById(R.id.image);
		TextView text = (TextView) menuView.findViewById(R.id.text);
		int imageResource = 0;
		switch (parentScreen) {
		case R.id.offers:
			imageResource = R.drawable.offers_sel;
			break;
		case R.id.about:
			imageResource = R.drawable.home_sel;
			break;
		case R.id.myacc:
			imageResource = R.drawable.acc_sel;
			break;
		case R.id.basket:
			imageResource = R.drawable.basket_sel;
			break;
		case R.id.history:
			imageResource = R.drawable.clock_sel;
			break;
		}
		icon.setImageResource(imageResource);
		text.setTextColor(ColorStateList.valueOf(0xffffffff));
	}

	protected void processFailure(int message) {
		hideDialog();

		showErrorDialog(getString(message));
	}

	protected SimpleResult getSimpleResult(NSObject object) {
		NSDictionary dict = (NSDictionary) object;
		SimpleResult res = new SimpleResult();
		NSNumber sucessFalg = (NSNumber) dict.objectForKey("success");
		res.setSucess(sucessFalg.boolValue());
		res.setErrorMessage(dict.objectForKey("message").toString());
		return res;
	}

	
	IHttpCallback simpleResultCallback=new IHttpCallback() {
		
		@Override
		public Object populateBean(InputStream is, int operationId) {
			NSObject object = null;
			try {
				object = PropertyListParser.parse(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (object != null) {
				SimpleResult result = getSimpleResult(object);
				return result;
			} else {
				return null;
			}
		}
		
		@Override
		public void onFailure(int message, int operationID) {
			// TODO Auto-generated method stub
			processFailure(message);
		}
		
		@Override
		public void bindUI(Object t, int operationId) {
						hideDialog();
						SimpleResult result = (SimpleResult) t;
						if (result.isSucess()) {
							handleSimpleResultResponse(result.getErrorMessage());
						} else {
							showErrorDialog(result.getErrorMessage());
						}			
		}
	};
	/**
	 * Sent when simple result is a success
	 * @param message
	 */
	protected void handleSimpleResultResponse(String message){
		
	}
	protected void showNotImplToast() {
		Toast t = Toast.makeText(getApplicationContext(),
				"Sorry not implemented yet! :(", 4000);
		t.show();
	}

}
