package com.oishii.mobile;

import android.content.Intent;
import android.view.View;

public class OutOfSession extends OishiiBaseActivity {

	public static final String SRC_KEY = "SOURCE_SCRN";
	int notLoggedScreenID = 0x45;

	@Override
	protected void hookInChildViews() {
		findViewById(R.id.createAccount).setOnClickListener(actionListener);
		findViewById(R.id.signIn).setOnClickListener(actionListener);
	}

	View.OnClickListener actionListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent=new Intent();
			intent.putExtra(SRC_KEY,getIntent().getIntExtra(SRC_KEY, 0));
			switch (v.getId()) {
			case R.id.signIn:
				intent.setClass(OutOfSession.this, Login.class);
				break;
			case R.id.createAccount:
				intent.setClass(OutOfSession.this, CreateAccount.class);
				break;
			}
			startActivity(intent);
		}
	};

	@Override
	protected int getChildViewLayout() {
		return R.layout.no_signed_in;
	}

	@Override
	protected String getTitleString() {
		int source = getIntent().getIntExtra(SRC_KEY, 0);
		int titleString = R.string.login_title;
		switch (source) {
		case R.id.basket:
			titleString = R.string.checkout;
			break;
		case R.id.myacc:
			titleString = R.string.acc_title;
			break;
		case R.id.history:
			titleString = R.string.history_title;
			break;
		}
		return getString(titleString);
	}

	@Override
	protected int getSreenID() {
		return notLoggedScreenID;
	}

}
