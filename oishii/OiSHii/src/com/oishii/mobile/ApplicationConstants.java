package com.oishii.mobile;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.util.SparseIntArray;

import com.madrobot.util.HttpSettings.HttpMethod;

public class ApplicationConstants {
	 static final int SPLASH_DURATION = 4000;
	static final String BASE_URL = "http://oishiidev.kieonstaging.com/";
	static final String PLIST_PATH = BASE_URL + "plist/";

	/* API's */
	 static final URI API_LOGIN = URI.create(PLIST_PATH + "login.php");
	 static final URI API_MENU_DATA = URI.create(PLIST_PATH
			+ "menuData.php");
	 static final URI API_MENU_DETAILS = URI.create(PLIST_PATH
			+ "menuItemData.php");
	 static final URI API_MENU_SPECIFIC_DETAILS = URI.create(PLIST_PATH
			+ "menuDetailData.php");
	
	//TODO change the end point for the gallery
	 static final URI API_MENU_GALLERY = URI.create(PLIST_PATH
			+ "menuDetailData.php");
	 static final URI API_SPECIAL_OFFERS = URI.create(PLIST_PATH
			+ "specialOffers.php");
	static final URI API_REGISTRATION = URI.create(PLIST_PATH
			+ "registration.php");
	static final URI API_MY_ACCOUNT = URI.create(PLIST_PATH
			+ "myaccount.php");
	 static final URI API_CHANGE_PWD = URI.create(PLIST_PATH
			+ "changePassword.php");
	 static final URI API_MY_HISTORY = URI.create(PLIST_PATH
			+ "historyData.php");
	 static final URI API_SAVE_ACC_DETAILS = URI.create(PLIST_PATH
			+ "savedetails.php");
	 static final URI API_ADD_LOCATION = URI.create(PLIST_PATH
			+ "addLocation.php");
	 static final URI API_DELETE_LOCATION = URI.create(PLIST_PATH
			+ "deleteLocation.php");
	 static final URI API_EDIT_LOCATION = URI.create(PLIST_PATH
			+ "editLocation.php");
	 static final URI API_DELIVERY_TIME = URI.create(PLIST_PATH
			+ "deliveryTime.php");
	 static final URI API_REDEEM_CODE = URI.create(PLIST_PATH
			+ "redeemCode.php");
	 static final URI API_VALIDATE_CHECKOUT = URI.create(PLIST_PATH
			+ "checkoutValidation.php");
	 static final URI API_FINAL_CHECKOUT = URI.create(PLIST_PATH
			+ "checkout.php");
	 static final HttpMethod HTTP_METHOD = HttpMethod.HTTP_GET;

	/**
	 * Drink & snack box cat id's
	 */
	 static final int CAT_ID_DRINKS = 30;
	 static final int CAT_ID_SNACKS = 29;
	 static final int CAT_ID_CORPORATE = 59;
	
	 static final int COLOR_SNACKS = Color.parseColor("#D60C8C");
	 static final int COLOR_DRINKS = Color.parseColor("#ff9000");
	
	public static final Map<Integer,String> operationMap=new HashMap<Integer,String>();
	static{
		operationMap.put(TodaysMenu.OPERATION_LIST, "todaysmenu.plist");
		operationMap.put(TodaysMenuDetailList.OPERATION_MENU_DETAILS, "todaysmenudetail");
		operationMap.put(TodaysMenuItemDetail.OPERATION_ID,"menuDetailData");
	}
}
