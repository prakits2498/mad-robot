package com.oishii.mobile;

import java.io.InputStream;
import java.util.ArrayList;

import android.widget.TextView;

import com.madrobot.di.plist.NSArray;
import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;
import com.oishii.mobile.util.tasks.IHttpCallback;

public class SpecialOffers extends ListOishiBase {
	private static final int OPERATION_SPL_OFFER = 10;

	@Override
	protected void hookInListData() {
		TextView tv = (TextView) findViewById(R.id.titleFirst);
		tv.setText(R.string.special);
		TextView tv2 = (TextView) findViewById(R.id.titleSecond);
		tv2.setText(R.string.offers);

	}

	protected void executeSpecialOffersRequest() {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
		requestWrapper.requestURI = ApplicationConstants.API_SPECIAL_OFFERS;
		requestWrapper.callback = splOffersCallback;
		requestWrapper.operationID = OPERATION_SPL_OFFER;
		showDialog(getString(R.string.loading_offers));
	}

	IHttpCallback splOffersCallback = new IHttpCallback() {
		@Override
		public Object populateBean(InputStream is, int operationId) {
			NSObject object = null;
			try {
				object = PropertyListParser.parse(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (object != null) {
				NSArray array = (NSArray) object;
				ArrayList<SpecialOffers> menuList = getArray(array);

				return menuList;
			} else {
				return null;
			}
		}

		@Override
		public void onFailure(String message, int operationID) {

		}

		@Override
		public void bindUI(Object t, int operationId) {

		}
	};

	private ArrayList<SpecialOffers> getArray(NSArray array) {
		int count = array.count();
		ArrayList<SpecialOffers> menus = new ArrayList<SpecialOffers>();
		for (int i = 0; i < count; i++) {
			NSDictionary d = (NSDictionary) array.objectAtIndex(i);
		}
		return menus;
	}

}
