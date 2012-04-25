package com.oishii.mobile;

import java.io.InputStream;

import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class Login extends OishiiBaseActivity {

	@Override
	protected void hookInChildViews() {
		findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				Intent launch = new Intent(Login.this, Home.class);
				startActivity(launch);
			}
		});
	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.login;
	}

	@Override
	protected String getTitleString() {
		return getResources().getString(R.string.login_title);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return false;
	}


	@Override
	protected void populateViewFromHttp(InputStream is, View v, int operation) {
		// TODO Auto-generated method stub
		
	}

}