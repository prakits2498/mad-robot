package com.oishii.mobile;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.madrobot.di.plist.NSArray;
import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
import com.oishii.mobile.beans.CurrentScreen;
import com.oishii.mobile.beans.MenuData;
import com.oishii.mobile.util.HttpSettings.HttpMethod;
import com.oishii.mobile.util.tasks.BitmapHttpTask;
import com.oishii.mobile.util.tasks.BitmapRequestParam;
import com.oishii.mobile.util.tasks.HttpRequestTask;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;
import com.oishii.mobile.util.tasks.IHttpCallback;

public class TodaysMenu extends ListOishiBase {

	class MainMenuAdapter extends ArrayAdapter<MenuData> {

		public MainMenuAdapter(Context context, int textViewResourceId,
				List<MenuData> objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			MenuData item = getItem(position);
			if (view == null) {
				view = getLayoutInflater().inflate(
						R.layout.list_todaysmenu_item, null);
				view.setBackgroundColor(item.getColor());
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.text1 = (TextView) view.findViewById(R.id.textView1);
				viewHolder.image = (ImageView) view
						.findViewById(R.id.imageView1);
				viewHolder.bg = view.findViewById(R.id.bg);
				viewHolder.bar = (ProgressBar) view
						.findViewById(R.id.imageProgress);
				view.setTag(viewHolder);
			}
			System.out.println("color->" + item.getColor());
			ViewHolder viewHolder = (ViewHolder) view.getTag();
			viewHolder.text1.setText(item.getTitle());
			Object loaded = viewHolder.image.getTag();
			if (loaded == null) {
				BitmapRequestParam req = new BitmapRequestParam();
				req.bitmapUri = URI.create(item.getBitmapUrl());
				req.image = viewHolder.image;
				req.image.setTag(new Object());
				req.progress = viewHolder.bar;
				new BitmapHttpTask().execute(req);
			}
			return view;

		}
	}
	private static class ViewHolder {
		TextView text1;
		ImageView image;
		View bg;
		ProgressBar bar;
	}

	public final static int OPERATION_BITMAP = 10;

	public final static int OPERATION_LIST = 30;

	IHttpCallback menuCallaback = new IHttpCallback() {

		@Override
		public void bindUI(Object t, int operationID) {
			MainMenuAdapter adapter = new MainMenuAdapter(
					getApplicationContext(), R.layout.list_todaysmenu_item,
					(List<MenuData>) t);
			ListView listview = getListView();
			listview.setAdapter(adapter);
			listview.setOnItemClickListener(listViewClickListener);
			hideDialog();
		}

		@Override
		public void onFailure(int message, int operationID) {
			processFailure(message);
		}

		@Override
		public Object populateBean(InputStream is, int operationId) {
			switch (operationId) {
			case OPERATION_LIST:
				NSObject object = null;
				try {
					object = PropertyListParser.parse(is);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (object != null) {
					NSArray array = (NSArray) object;
					ArrayList<MenuData> menuList = getArray(array);

					return menuList;
				} else {
					return null;
				}
			case OPERATION_BITMAP:
				break;
			}
			Log.e("Oishii", "IDEALLY SHOULD NEVER GET HERE");
			return null;
		}

	};

	AdapterView.OnItemClickListener listViewClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Log.d("Oishii", "Item at position" + arg2);

			MenuData menu = (MenuData) getListView().getItemAtPosition(arg2);
			Intent intent = new Intent(TodaysMenu.this, TodaysMenuDetailList.class);
			intent.putExtra(TodaysMenuDetails2.EXTRA_TITLE, menu.getTitle());
			intent.putExtra(TodaysMenuDetails2.EXTRA_CAT_ID, menu.getId());
			intent.putExtra(TodaysMenuDetails2.EXTRA_COLOR, menu.getColor());
			startActivity(intent);
		}
	};

	private void executeMenuListRequest() {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
		requestWrapper.requestURI = ApplicationConstants.API_MENU_DATA;
		requestWrapper.callback = menuCallaback;
		requestWrapper.operationID = OPERATION_LIST;
		requestWrapper.httpSettings.setHttpMethod(HttpMethod.HTTP_POST);
		showDialog(getString(R.string.loading_mnu));
		new HttpRequestTask().execute(requestWrapper);
	}

	private ArrayList<MenuData> getArray(NSArray array) {
		int count = array.count();
		ArrayList<MenuData> menus = new ArrayList<MenuData>();
		for (int i = 0; i < count; i++) {
			NSDictionary d = (NSDictionary) array.objectAtIndex(i);
			MenuData menu = new MenuData();
			menu.setBitmapUrl(d.objectForKey("image").toString());
			menu.setTitle(d.objectForKey("name").toString());
			menu.setId(Integer.parseInt(d.objectForKey("id").toString()));
			// String color = d.objectForKey("color").toString().replace('#',
			// ' ')
			// .trim();
			menu.setColor(Color.parseColor(d.objectForKey("color").toString()));
			menus.add(menu);
		}
		return menus;
	}

	@Override
	protected int getSreenID() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	protected void hookInListData() {
		TextView tv = (TextView) findViewById(R.id.titleFirst);
		tv.setText(R.string.today);
		TextView tv2 = (TextView) findViewById(R.id.titleSecond);
		tv2.setText(R.string.menu);

		executeMenuListRequest();
	}
}
