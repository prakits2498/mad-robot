package com.oishii.mobile;

import java.net.URI;

import com.oishii.mobile.util.HttpSettings.HttpMethod;

public class ApplicationConstants {
	public static final int SPLASH_DURATION = 1000;
	static final String BASE_URL = "http://oishiidev.kieonstaging.com/";
	static final String PLIST_PATH = BASE_URL + "plist/";

	/* API's */
	public static final URI API_LOGIN = URI.create(PLIST_PATH + "login.php");
	public static final URI API_MENU_DATA = URI.create(PLIST_PATH
			+ "menuData.php");
	public static final URI API_MENU_DETAILS = URI.create(PLIST_PATH
			+ "menuItemData.php");
	public static final URI API_MENU_SPECIFIC_DETAILS = URI.create(PLIST_PATH
			+ "menuDetailData.php");
	public static final URI API_SPECIAL_OFFERS = URI.create(PLIST_PATH
			+ "specialOffers.php");
	public static final URI API_REGISTRATION = URI.create(PLIST_PATH
			+ "registration.php");
	public static final URI API_MY_ACCOUNT = URI.create(PLIST_PATH
			+ "myaccount.php");
	public static final URI API_CHANGE_PWD = URI.create(PLIST_PATH
			+ "changePassword.php");
	public static final URI API_MY_HISTORY = URI.create(PLIST_PATH
			+ "historyData.php");
	public static final URI API_SAVE_ACC_DETAILS = URI.create(PLIST_PATH
			+ "savedetails.php");
	public static final URI API_ADD_LOCATION = URI.create(PLIST_PATH
			+ "addLocation.php");

	public static HttpMethod HTTP_METHOD = HttpMethod.HTTP_GET;
}
