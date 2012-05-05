package com.oishii.mobile;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
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

	View.OnClickListener btnListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showNotImplToast();
		}
	};

	View.OnClickListener expandCollapse = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			MenuItemCategory category = (MenuItemCategory) v.getTag();
			int visibility = category.isExpanded() ? View.GONE : View.VISIBLE;
			category.setExpanded(!category.isExpanded());
			int count = layout.getChildCount();
			View currentView;
			Object o;
			MenuItem menu;
			for (int i = 0; i < count; count++) {
				currentView = layout.getChildAt(i);
				o = currentView.getTag();
				if (o instanceof MenuItem) {
					menu = (MenuItem) o;
					if (menu.getCategory().equals(category)) {
						currentView.setVisibility(visibility);
					}
				}
			}

		}
	};

	@Override
	protected int getSreenID() {
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

	LinearLayout layout;

	IHttpCallback detailsCallback = new IHttpCallback() {

		@Override
		public void bindUI(Object t, int operationId) {

			ResultContainer result = (ResultContainer) t;
			ExpandableListView list = getExandableList(true);

			MenuDetailsExpandableAdapter adapter = new MenuDetailsExpandableAdapter(
					result.parent, result.children);
			list.setAdapter(adapter);
			list.setVisibility(View.VISIBLE);
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
				ResultContainer det = processPlist(object);
				return det;
			} else {
				return null;
			}
		}
	};

	private class ResultContainer {
		private ArrayList<MenuItemCategory> parent;
		private ArrayList<ArrayList<MenuItem>> children;
	}

	private ResultContainer processPlist(NSObject obj) {
		NSArray plist = (NSArray) obj;
		int categories = plist.count();
		ResultContainer container = new ResultContainer();
		ArrayList<MenuItemCategory> parentGroups = new ArrayList<MenuItemCategory>();
		ArrayList<ArrayList<MenuItem>> childGroups = new ArrayList<ArrayList<MenuItem>>();

		for (int i = 0; i < categories; i++) {
			NSDictionary dict = (NSDictionary) plist.objectAtIndex(i);
			MenuItemCategory menuCategory = new MenuItemCategory();
			NSNumber id = (NSNumber) dict.objectForKey("id");
			menuCategory.setId(id.intValue());
			menuCategory.setName(dict.objectForKey("name").toString());
			menuCategory.setDescription(dict.objectForKey("shortdescription")
					.toString());
			// list.add(menuCategory);

			parentGroups.add(menuCategory);
			NSArray items = (NSArray) dict.objectForKey("items");
			ArrayList<MenuItem> childGroup = new ArrayList<MenuItem>();
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
				childGroup.add(menuItem);
			}
			childGroups.add(childGroup);
		}
		container.parent = parentGroups;
		container.children = childGroups;

		return container;
	}

	private class MenuDetailsExpandableAdapter extends
			BaseExpandableListAdapter {
		private List<MenuItemCategory> parents;
		private ArrayList<ArrayList<MenuItem>> children;

		private MenuDetailsExpandableAdapter(List<MenuItemCategory> categories,
				ArrayList<ArrayList<MenuItem>> menuItem) {
			this.parents = categories;
			this.children = menuItem;
		}

		@Override
		public MenuItem getChild(int group, int child) {
			ArrayList<MenuItem> chil = children.get(group);
			return chil.get(child);
		}

		@Override
		public long getChildId(int arg0, int arg1) {
			return 0;
		}

		@Override
		public View getChildView(int group, int child, boolean arg2, View arg3,
				ViewGroup arg4) {

			ArrayList<MenuItem> menu = children.get(group);
			MenuItem item = menu.get(child);
			View v = getLayoutInflater().inflate(
					R.layout.todays_menu_item_contents, null);
			TextView tv = (TextView) v.findViewById(R.id.title);
			tv.setText(item.getName());
			tv = (TextView) v.findViewById(R.id.desc);
			tv.setText(item.getDescription());
			tv = (TextView) v.findViewById(R.id.left);
			tv.setText(item.getItemsRemain() + " Left");
			ImageView image = (ImageView) v.findViewById(R.id.menuImg);
			image.setId(group + child);
			TextView price = (TextView)v.findViewById(R.id.price);
			price.setText("£" + item.getPrice());

			Button detail = (Button) v.findViewById(R.id.detail);
			detail.setTag(item);
			detail.setOnClickListener(btnListener);
			ProgressBar progress = (ProgressBar) v
					.findViewById(R.id.imageProgress);
			LinearLayout parent = (LinearLayout) v
					.findViewById(R.id.progressParent);
			if (item.getBitmap() == null) {
				BitmapRequestParam req = new BitmapRequestParam();
				req.bitmapUri = URI.create(item.getImage());
				req.image = image;
				progress.setId(group + child);
				req.progress = progress;
				req.parent = parent;// (LinearLayout)
									// v.findViewById(R.id.progressParent);
				req.bean = item;
				new BitmapHttpTask().execute(req);
			} else {
				System.out.println("Setting  Bitmap" + item.getBitmap());
				image.setImageBitmap(item.getBitmap());
				image.setVisibility(View.VISIBLE);
				parent.removeView(progress);
			}
			return v;
		}

		@Override
		public int getChildrenCount(int arg0) {
			return children.get(arg0).size();
		}

		@Override
		public ArrayList<MenuItem> getGroup(int arg0) {
			return children.get(arg0);
		}

		@Override
		public int getGroupCount() {
			return parents.size();
		}

		@Override
		public long getGroupId(int arg0) {
			return 0;
		}

		@Override
		public View getGroupView(int arg0, boolean arg1, View arg2,
				ViewGroup arg3) {
			MenuItemCategory category = parents.get(arg0);
			// View v = arg2;
			// if (v == null) {
			View v = getLayoutInflater().inflate(R.layout.menu_item_header,
					null);
			v.setBackgroundColor(color);
			TextView tv = (TextView) v.findViewById(R.id.mnuTitle);
			tv.setText(category.getName());
			tv = (TextView) v.findViewById(R.id.mnuDesc);
			tv.setText(category.getDescription());
			// }
			return v;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			return true;
		}

	}

	@Override
	protected int getParentScreenId() {
		// TODO Auto-generated method stub
		return R.id.about;
	}

}
