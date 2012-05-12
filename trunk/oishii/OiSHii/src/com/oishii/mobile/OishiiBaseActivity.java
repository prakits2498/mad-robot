package com.oishii.mobile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSNumber;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
import com.oishii.mobile.R.id;
import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.beans.CurrentScreen;
import com.oishii.mobile.beans.OishiiBasket;
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
			if (menuParent.getVisibility() == View.VISIBLE) {
				menuParent.setVisibility(View.GONE);
			} else {
				menuParent.setVisibility(View.VISIBLE);
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

	View.OnClickListener menuListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d(TAG, "Menu clicked");
			int currentId = CurrentScreen.getInstance().getCurrentScreenID();
			if (currentId == v.getId()) {
				return;
			}

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
				if (!AccountStatus.getInstance(getApplicationContext())
						.isSignedIn()) {

					clz = OutOfSession.class;
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				} else {
					clz = Basket.class;
				}
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
		setSelectedMenu();
		setBasketPrice();
	}

	private void setBasketPrice() {
		OishiiBasket basket = AccountStatus
				.getInstance(getApplicationContext()).getBasket();
		TextView total = (TextView) findViewById(R.id.totalPrice);
		if (basket.getCurrentTotal() > 0.0f) {
			total.setText("£" + basket.getCurrentTotal());
			total.setVisibility(View.VISIBLE);
		} else {
			total.setVisibility(View.GONE);
		}
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

	protected SimpleResult getSimpleResult(NSObject object) throws Exception {
		NSDictionary dict = (NSDictionary) object;
		SimpleResult res = new SimpleResult();
		NSNumber sucessFalg = (NSNumber) dict.objectForKey("success");
		res.setSucess(sucessFalg.boolValue());
		res.setErrorMessage(dict.objectForKey("message").toString());
		return res;
	}

	IHttpCallback simpleResultCallback = new IHttpCallback() {

		@Override
		public Object populateBean(InputStream is, int operationId) {
			NSObject object = null;
			try {
				object = PropertyListParser.parse(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (object != null) {
				SimpleResult result;
				try {
					result = getSimpleResult(object);
					return result;
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
	 * 
	 * @param message
	 */
	protected void handleSimpleResultResponse(String message) {

	}

	protected void showNotImplToast() {
		Toast t = Toast.makeText(getApplicationContext(),
				"Implementation unknown.", 4000);
		t.show();
	}

	protected Button getArbitartButton() {
		Button btnArbit = (Button) findViewById(R.id.btnArbitary);
		btnArbit.setVisibility(View.VISIBLE);
		return btnArbit;
	}

	/**
	 * This convenience method allows to read a InputStream into a string. The
	 * platform's default character encoding is used for converting bytes into
	 * characters.
	 * 
	 * @param pStream
	 *            The input stream to read.
	 * @see #asString(InputStream, String)
	 * @return The streams contents, as a string.
	 * @throws IOException
	 *             An I/O error occurred.
	 */
	public static String asString(InputStream pStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		copy(pStream, baos, true);
		return baos.toString();
	}

	/**
	 * Copies the contents of the given {@link InputStream} to the given
	 * {@link OutputStream}. Shortcut for
	 * 
	 * <pre>
	 * copy(pInputStream, pOutputStream, new byte[8192]);
	 * </pre>
	 * 
	 * @param pInputStream
	 *            The input stream, which is being read. It is guaranteed, that
	 *            {@link InputStream#close()} is called on the stream.
	 * @param pOutputStream
	 *            The output stream, to which data should be written. May be
	 *            null, in which case the input streams contents are simply
	 *            discarded.
	 * @param pClose
	 *            True guarantees, that {@link OutputStream#close()} is called
	 *            on the stream. False indicates, that only
	 *            {@link OutputStream#flush()} should be called finally.
	 * 
	 * @return Number of bytes, which have been copied.
	 * @throws IOException
	 *             An I/O error occurred.
	 */
	public static long copy(InputStream pInputStream,
			OutputStream pOutputStream, boolean pClose) throws IOException {
		return copy(pInputStream, pOutputStream, pClose, new byte[1024 * 4]);
	}

	/**
	 * Copies the contents of the given {@link InputStream} to the given
	 * {@link OutputStream}.
	 * 
	 * @param pIn
	 *            The input stream, which is being read. It is guaranteed, that
	 *            {@link InputStream#close()} is called on the stream.
	 * @param pOut
	 *            The output stream, to which data should be written. May be
	 *            null, in which case the input streams contents are simply
	 *            discarded.
	 * @param pClose
	 *            True guarantees, that {@link OutputStream#close()} is called
	 *            on the stream. False indicates, that only
	 *            {@link OutputStream#flush()} should be called finally.
	 * @param pBuffer
	 *            Temporary buffer, which is to be used for copying data.
	 * @return Number of bytes, which have been copied.
	 * @throws IOException
	 *             An I/O error occurred.
	 */
	public static long copy(InputStream pIn, OutputStream pOut, boolean pClose,
			byte[] pBuffer) throws IOException {
		OutputStream out = pOut;
		InputStream in = pIn;
		try {
			long total = 0;
			for (;;) {
				int res = in.read(pBuffer);
				if (res == -1) {
					break;
				}
				if (res > 0) {
					total += res;
					if (out != null) {
						out.write(pBuffer, 0, res);
					}
				}
			}
			if (out != null) {
				if (pClose) {
					out.close();
				} else {
					out.flush();
				}
				out = null;
			}
			in.close();
			in = null;
			return total;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Throwable t) {
					/* Ignore me */
				}
			}
			if (pClose && out != null) {
				try {
					out.close();
				} catch (Throwable t) {
					/* Ignore me */
				}
			}
		}
	}
}
