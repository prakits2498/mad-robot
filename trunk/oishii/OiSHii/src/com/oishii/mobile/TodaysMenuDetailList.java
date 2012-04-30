package com.oishii.mobile;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.madrobot.di.plist.NSArray;
import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSNumber;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
import com.oishii.mobile.beans.MenuItem;
import com.oishii.mobile.beans.MenuItemCategory;
import com.oishii.mobile.util.tasks.BitmapHttpTask;
import com.oishii.mobile.util.tasks.BitmapRequestParam;
import com.oishii.mobile.util.tasks.HttpRequestTask;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;
import com.oishii.mobile.util.tasks.IHttpCallback;

public class TodaysMenuDetailList extends ListOishiBase {

	final static String EXTRA_TITLE = "title";
	final static String EXTRA_COLOR = "bgColor";
	final static String EXTRA_CAT_ID = "catID";
	int OPERATION_MNU_DET = 78;
	private int color;

	@Override
	protected void hookInListData() {
		// TODO Auto-generated method stub
		ListView lv = getListView();
		System.out.println(lv);
		showOnlyLogo();
		Intent intent = getIntent();
		String title = intent.getStringExtra(EXTRA_TITLE);
		color = intent.getIntExtra(EXTRA_COLOR, 0x000000);
		TextView tv = (TextView) findViewById(R.id.titleFirst);
		tv.setText(title);
		tv.setTextColor(color);
		// TextView tv = (TextView) findViewById(R.id.title);
		// tv.setText(title);
		// tv.setTextColor(color);
		executeMenuDetailsRequest();
	}

	@Override
	protected int getSreenID() {
		// TODO Auto-generated method stub
		return R.layout.menu_item_header;
	}

	private void executeMenuDetailsRequest() {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
		requestWrapper.requestURI = ApplicationConstants.API_MENU_DETAILS;
		requestWrapper.callback = detailsCallback;
		requestWrapper.operationID = OPERATION_MNU_DET;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String catId = String.valueOf(getIntent().getIntExtra(EXTRA_CAT_ID, 0));
		NameValuePair param = new BasicNameValuePair("catID", catId);
		params.add(param);
		requestWrapper.httpParams = params;
		// requestWrapper.httpSettings.setHttpMethod(HttpMethod.HTTP_POST);
		showDialog(getString(R.string.loading_det));
		new HttpRequestTask().execute(requestWrapper);
	}

	IHttpCallback detailsCallback = new IHttpCallback() {

		@Override
		public void bindUI(Object t, int operationId) {
			ArrayList<Object> list = (ArrayList<Object>) t;
			Object obj;
			LinearLayout layout = getManualListView();
			LayoutInflater inflater = getLayoutInflater();
			MenuItemCategory category;
			MenuItem item;
			for (int i = 0; i < list.size(); i++) {
				obj = list.get(i);
				View v;
				if (obj instanceof MenuItemCategory) {
					category = (MenuItemCategory) obj;
					v = inflater.inflate(R.layout.menu_item_header, null);
					v.setBackgroundColor(color);
					TextView tv = (TextView) v.findViewById(R.id.mnuTitle);
					tv.setText(category.getName());
					tv = (TextView) v.findViewById(R.id.mnuDesc);
					tv.setText(category.getDescription());
					v.setTag(category);
				} else {
					item = (MenuItem) obj;
					v = inflater.inflate(R.layout.item_contents, null);
					TextView tv = (TextView) v.findViewById(R.id.title);
					tv.setText(item.getName());
					tv = (TextView) v.findViewById(R.id.desc);
					tv.setText(item.getDescription());
					tv = (TextView) v.findViewById(R.id.left);
					tv.setText(item.getItemsRemain() + " Left");
					ImageView image = (ImageView) v.findViewById(R.id.menuImg);
					image.setId(i);
					Button price = (Button) v.findViewById(R.id.price);
					price.setText("£"+item.getPrice());
					BitmapRequestParam req = new BitmapRequestParam();
					req.bitmapUri = URI.create(item.getImage());
					req.image = image;
					ProgressBar progress = (ProgressBar) v
							.findViewById(R.id.imageProgress);
					progress.setId(i);
					req.progress = progress;
					req.parent = (LinearLayout) findViewById(R.id.progressParent);
					new BitmapHttpTask().execute(req);

				}
				layout.addView(v);
			}
			hideDialog();
		}

		@Override
		public void onFailure(int message, int operationID) {
			processFailure(message);
		}

		@Override
		public Object populateBean(InputStream is, int operationId) {
			NSObject object = null;
			try {
				object = PropertyListParser.parse(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (object != null) {
				// NSArray array = (NSArray) object;
				// ArrayList<MenuData> menuList = getArray(array);
				ArrayList<Object> det = processPlist(object);
				return det;
			} else {
				return null;
			}
		}
	};

	private ArrayList<Object> processPlist(NSObject obj) {
		NSArray plist = (NSArray) obj;
		int categories = plist.count();
		ArrayList<Object> list = new ArrayList<Object>();

		for (int i = 0; i < categories; i++) {
			NSDictionary dict = (NSDictionary) plist.objectAtIndex(i);
			MenuItemCategory menuCategory = new MenuItemCategory();
			NSNumber id = (NSNumber) dict.objectForKey("id");
			menuCategory.setId(id.intValue());
			menuCategory.setName(dict.objectForKey("name").toString());
			menuCategory.setDescription(dict.objectForKey("shortdescription")
					.toString());
			list.add(menuCategory);
			NSArray items = (NSArray) dict.objectForKey("items");
			int number = items.count();

			for (int y = 0; y < number; y++) {
				NSDictionary menuItems = (NSDictionary) items.objectAtIndex(y);
				MenuItem menuItem = new MenuItem();
				id = (NSNumber) menuItems.objectForKey("id");
				menuItem.setId(id.intValue());
				menuItem.setName(menuItems.objectForKey("name").toString());
				menuItem.setImage(menuItems.objectForKey("image").toString());
				menuItem.setDescription(menuItems.objectForKey(
						"shortdescription").toString());
				id = (NSNumber) menuItems.objectForKey("itemsremaining");
				menuItem.setItemsRemain(id.intValue());
				id = (NSNumber) menuItems.objectForKey("price");
				menuItem.setPrice(id.floatValue());
				menuItem.setCategory(menuCategory);
				list.add(menuItem);
			}
		}
		return list;
	}

}
