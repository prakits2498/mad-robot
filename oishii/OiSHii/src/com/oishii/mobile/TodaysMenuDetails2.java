package com.oishii.mobile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.madrobot.di.plist.NSArray;
import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSNumber;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
import com.oishii.mobile.beans.MenuItem;
import com.oishii.mobile.beans.MenuItemCategory;
import com.oishii.mobile.util.tasks.HttpRequestTask;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;
import com.oishii.mobile.util.tasks.IHttpCallback;

public class TodaysMenuDetails2 extends ListOishiBase {
	class MainMenuDetailsAdapter extends ArrayAdapter<Object> {

		public MainMenuDetailsAdapter(Context context, int textViewResourceId,
				List<Object> objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			Object item = getItem(position);
			int layout;
			boolean isHeader;
			if (item instanceof MenuItemCategory) {
				layout = R.layout.menu_item_header;
				isHeader = true;

			} else {// menu data instance
				layout = R.layout.menu_item_contents;
				isHeader = false;
			}
			if (view == null) {
				view = getLayoutInflater().inflate(layout, null);
				if (isHeader) {
					view.findViewById(R.id.header).setBackgroundColor(color);
				}
			}

			// if (view == null) {
			//
			// view = getLayoutInflater().inflate(
			// R.layout.list_todaysmenu_item, null);
			// view.setBackgroundColor(item.getColor());
			// ViewHolder viewHolder = new ViewHolder();
			// viewHolder.text1 = (TextView) view.findViewById(R.id.textView1);
			// viewHolder.image = (ImageView) view
			// .findViewById(R.id.imageView1);
			// viewHolder.bg = view.findViewById(R.id.bg);
			// viewHolder.bar = (ProgressBar) view
			// .findViewById(R.id.imageProgress);
			// view.setTag(viewHolder);
			// }
			// System.out.println("color->" + item.getColor());
			// ViewHolder viewHolder = (ViewHolder) view.getTag();
			// viewHolder.text1.setText(item.getTitle());
			// Object loaded = viewHolder.image.getTag();
			// if (loaded == null) {
			// BitmapRequestParam req = new BitmapRequestParam();
			// req.bitmapUri = URI.create(item.getBitmapUrl());
			// req.image = viewHolder.image;
			// req.image.setTag(new Object());
			// req.progress = viewHolder.bar;
			// new BitmapHttpTask().execute(req);
			// }
			return view;

		}
	}
	final static String EXTRA_TITLE = "title";
	final static String EXTRA_COLOR = "bgColor";
	final static String EXTRA_CAT_ID = "catID";
	int OPERATION_MNU_DET = 78;

	private int color;

	IHttpCallback detailsCallback = new IHttpCallback() {

		@Override
		public void bindUI(Object t, int operationId) {
			MainMenuDetailsAdapter adapter = new MainMenuDetailsAdapter(
					TodaysMenuDetails2.this, R.layout.list_todaysmenu_item,
					(List<Object>) t);
			ListView listview = getListView();
			listview.setAdapter(adapter);
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

	private View.OnClickListener menuItemClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

		}
	};

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

	@Override
	protected int getChildViewLayout() {
		return R.layout.todaysmenu_details;
	}

	@Override
	protected int getSreenID() {
		return R.layout.todaysmenu_details;
	}

	@Override
	protected String getTitleString() {
		return "";
	}

	@Override
	protected void hookInListData() {
		// TODO Auto-generated method stub
		showOnlyLogo();
		Intent intent = getIntent();
		String title = intent.getStringExtra(EXTRA_TITLE);
		color = intent.getIntExtra(EXTRA_COLOR, 0x000000);
		TextView tv = (TextView) findViewById(R.id.title);
		tv.setText(title);
		tv.setTextColor(color);
		executeMenuDetailsRequest();
	}

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
				list.add(menuItem);
			}
		}
		return list;
	}
}
