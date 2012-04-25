package com.oishii.mobile;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import com.madrobot.di.plist.NSArray;
import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
import com.oishii.mobile.beans.MenuItem;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

public class TodaysMenu extends ListOishiBase {

	@Override
	protected void hookInListData() {
		// TODO Auto-generated method stub
		HttpUIWrapper ui = new HttpUIWrapper();
		ui.uri = URI
				.create("http://oishiidev.kieonstaging.com/plist/menuData.php");
		new OishiiHttpTask().execute(ui);
	}

	@Override
	protected void populateViewFromHttp(InputStream is, View v) {
		// TODO Auto-generated method stub
		try {
			NSObject object = PropertyListParser.parse(is);
			NSArray array = (NSArray) object;
			int count = array.count();
			for (int i = 0; i < count; i++) {
				NSDictionary d = (NSDictionary) array.objectAtIndex(i);
				System.out.println(d.objectForKey("image"));

			}
			// NSDictionary d = (NSDictionary) object;
			Log.d("Data", "===>DATA" + object.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class MainMenuAdapter extends ArrayAdapter<MenuItem> {

		public MainMenuAdapter(Context context, int resource,
				int textViewResourceId, List<MenuItem> objects) {
			super(context, resource, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}

	}

}
