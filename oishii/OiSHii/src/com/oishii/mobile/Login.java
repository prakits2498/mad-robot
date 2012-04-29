package com.oishii.mobile;

import android.view.Menu;
import android.view.View;

public class Login extends OishiiBaseActivity {

	@Override
	protected void hookInChildViews() {
		findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// finish();
				// Intent launch = new Intent(Login.this, Home.class);
				// startActivity(launch);

				if (validate()) {
					final int nextScreen = getIntent().getIntExtra(
							OutOfSession.SRC_KEY, 0);
					Class targetClass;
					switch (nextScreen) {

					case R.id.basket:
//						targetClass=;//titleString = R.string.checkout;
						break;
					case R.id.myacc:
//						titleString = R.string.acc_title;
						break;
					case R.id.history:
//						titleString = R.string.history_title;
						break;
					}
				}
				{
					// show validate toast
				}
			}
		});
		findViewById(R.id.footer).setVisibility(View.GONE);
	}

	private void executeLoginRequest() {

	}

	private boolean validate() {
		return false;
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

	// TODO set screen id
	@Override
	protected int getSreenID() {
		// TODO Auto-generated method stub
		return 0;
	}

}