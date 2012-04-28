package com.oishii.mobile.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {
	public static boolean isValidEmailAddress(String address) {
		final String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(emailPattern);
		Matcher matcher = pattern.matcher(address);
		return matcher.matches();
	}
}
