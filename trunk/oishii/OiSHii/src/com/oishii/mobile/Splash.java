package com.oishii.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				finish();
				startActivity(new Intent(getApplicationContext(), Login.class));
			}
		}, ApplicationConstants.SPLASH_DURATION);
	}
}
