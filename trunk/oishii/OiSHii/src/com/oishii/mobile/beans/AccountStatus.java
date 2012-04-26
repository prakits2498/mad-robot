package com.oishii.mobile.beans;

import android.content.Context;
import android.telephony.TelephonyManager;

public class AccountStatus {
	boolean isSignedIn;
	String sid;
	String mac;

	public boolean isSignedIn() {
		return isSignedIn;
	}

	public void setSignedIn(boolean isSignedIn) {
		this.isSignedIn = isSignedIn;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getMac() {
		return mac;
	}

	private static AccountStatus instance;

	private AccountStatus(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		mac = manager.getDeviceId();
		System.err.println("Device id->" + mac);
	}

	public static synchronized AccountStatus getInstance(Context context) {
		if (instance == null) {
			instance = new AccountStatus(context);
		}
		return instance;
	}
}
