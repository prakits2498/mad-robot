package com.oishii.mobile;

import java.net.URI;

public class ApplicationConstants {
	static final String BASE_URL = "http://oishiidev.kieonstaging.com/";
	static final String PLIST_PATH = BASE_URL + "plist/";

	/* API's */
	public static final URI API_LOGIN = URI.create(PLIST_PATH + "login.php");
	public static final URI API_MENU_DATA = URI.create(PLIST_PATH
			+ "menuData.php");

}
