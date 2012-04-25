package com.oishii.mobile;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.madrobot.di.plist.NSArray;
import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
import com.oishii.mobile.beans.MenuItem;

public class TodaysMenu extends ListOishiBase {

	public final static int OPERATION_BITMAP = 10;
	public final static int OPERATION_LIST = 30;

	@Override
	protected void hookInListData() {
		// TODO Auto-generated method stub
		HttpUIWrapper ui = new HttpUIWrapper();
		ui.uri = URI
				.create("http://oishiidev.kieonstaging.com/plist/menuData.php");
		ui.operation = OPERATION_LIST;
		new OishiiHttpTask().execute(ui);
	}

	@Override
	protected boolean populateViewFromHttp(InputStream is, View v, int operation) {
		switch (operation) {
		case OPERATION_BITMAP:
			return false;
		case OPERATION_LIST:
			// TODO Auto-generated method stub
			try {
				NSObject object = PropertyListParser.parse(is);
				if (object != null) {
					NSArray array = (NSArray) object;
					ArrayList<MenuItem> menuList=getArray(array);
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		return false;
	}

	private ArrayList<MenuItem> getArray(NSArray array) {
		int count = array.count();
		ArrayList<MenuItem> menus = new ArrayList<MenuItem>();
		for (int i = 0; i < count; i++) {
			NSDictionary d = (NSDictionary) array.objectAtIndex(i);
			MenuItem menu = new MenuItem();
			menu.setBitmapUrl(d.objectForKey("image").toString());
			menu.setTitle(d.objectForKey("name").toString());
			System.out.println(d.objectForKey("id"));
			System.out.println(d.objectForKey("color"));
			menus.add(menu);

		}
		return menus;
	}

	class MainMenuAdapter extends ArrayAdapter<MenuItem> {

		public MainMenuAdapter(Context context, int resource,
				int textViewResourceId, List<MenuItem> objects) {
			super(context, resource, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			getItem(position);
			if (convertView != null) {

			}
			return parent;

		}

	}

}
