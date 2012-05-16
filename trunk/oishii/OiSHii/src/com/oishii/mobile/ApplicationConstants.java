package com.oishii.mobile;

import java.net.URI;

import android.graphics.Color;

import com.oishii.mobile.util.HttpSettings.HttpMethod;

public class ApplicationConstants {
	public static final int SPLASH_DURATION = 4000;
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
	public static final URI API_DELETE_LOCATION = URI.create(PLIST_PATH
			+ "deleteLocation.php");
	public static final URI API_EDIT_LOCATION = URI.create(PLIST_PATH
			+ "editLocation.php");
	public static final URI API_DELIVERY_TIME = URI.create(PLIST_PATH
			+ "deliveryTime.php");
	public static final URI API_REDEEM_CODE = URI.create(PLIST_PATH
			+ "redeemCode.php");
	public static final URI API_VALIDATE_CHECKOUT = URI.create(PLIST_PATH
			+ "checkoutValidation.php");
	public static final URI API_FINAL_CHECKOUT = URI.create(PLIST_PATH
			+ "checkout.php");
	public static final HttpMethod HTTP_METHOD = HttpMethod.HTTP_GET;

	/**
	 * Drink & snack box cat id's
	 */
	public static final int CAT_ID_DRINKS = 30;
	public static final int CAT_ID_SNACKS = 29;
	public static final int CAT_ID_CORPORATE = 59;
	
	public static final int COLOR_SNACKS = Color.parseColor("#D60C8C");
	public static final int COLOR_DRINKS = Color.parseColor("#ff9000");
}
