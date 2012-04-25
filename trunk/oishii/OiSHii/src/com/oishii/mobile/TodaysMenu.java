package com.oishii.mobile;

import java.io.InputStream;
import java.net.URI;

import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;

import android.util.Log;
import android.view.View;

public class TodaysMenu extends ListOishiBase {

	@Override
	protected void hookInListData() {
		// TODO Auto-generated method stub
		HttpUIWrapper ui=new HttpUIWrapper();
		ui.uri=URI.create("http://oishiidev.kieonstaging.com/plist/menuData.php");
		new OishiiHttpTask().execute(ui);
	}

	@Override
	protected void populateViewFromHttp(InputStream is, View v) {
		// TODO Auto-generated method stub
		try {
			NSObject object = PropertyListParser.parse(is);
			NSDictionary d = (NSDictionary) object;
			Log.d("Data","===>DATA"+d.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

