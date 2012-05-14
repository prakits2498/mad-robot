package com.oishii.mobile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.view.View;

import com.madrobot.di.plist.NSArray;
import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSNumber;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
import com.oishii.mobile.beans.AccountInformation;
import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.beans.Address;
import com.oishii.mobile.beans.MenuData;
import com.oishii.mobile.beans.SavedCard;
import com.oishii.mobile.util.HttpSettings;
import com.oishii.mobile.util.HttpSettings.HttpMethod;
import com.oishii.mobile.util.tasks.HttpRequestTask;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;
import com.oishii.mobile.util.tasks.IHttpCallback;

public class AccountDetails extends OishiiBaseActivity {
	final int SCR_ID = 67;

	@Override
	protected void hookInChildViews() {
		findViewById(R.id.myAccDetails).setOnClickListener(
				accountDetailsListener);
		findViewById(R.id.myAccLocation).setOnClickListener(
				accountDetailsListener);
		findViewById(R.id.myAccPayment).setOnClickListener(
				accountDetailsListener);
		findViewById(R.id.myAccPwd).setOnClickListener(accountDetailsListener);
	}

	View.OnClickListener accountDetailsListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Class<? extends OishiiBaseActivity> clz = null;
			switch (v.getId()) {
			case R.id.myAccDetails:
				clz = EditAccountDetails.class;
				break;
			case R.id.myAccLocation:
				clz = Locations.class;
				break;
			case R.id.myAccPayment:
				clz = StoredPayments.class;
				break;
			case R.id.myAccPwd:
				clz = ChangePassword.class;
				break;
			}
			if (clz != null) {
				Intent intent = new Intent(AccountDetails.this, clz);
				startActivity(intent);
			}

		}
	};

	@Override
	protected int getSreenID() {
		return SCR_ID;
	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.my_account;
	}

	@Override
	protected String getTitleString() {
		return getString(R.string.acc_title);
	}

	@Override
	protected int getParentScreenId() {
		return R.id.myacc;
	}

}
