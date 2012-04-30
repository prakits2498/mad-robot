//package com.oishii.mobile;
//
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.madrobot.di.plist.NSArray;
//import com.madrobot.di.plist.NSDictionary;
//import com.madrobot.di.plist.NSNumber;
//import com.madrobot.di.plist.NSObject;
//import com.madrobot.di.plist.PropertyListParser;
//import com.oishii.mobile.beans.MenuItem;
//import com.oishii.mobile.beans.MenuItemCategory;
//import com.oishii.mobile.beans.MenuItemDetails;
//import com.oishii.mobile.util.tasks.HttpRequestTask;
//import com.oishii.mobile.util.tasks.HttpRequestWrapper;
//import com.oishii.mobile.util.tasks.IHttpCallback;
//
//public class TodaysMenuDetails extends OishiiBaseActivity {
// 	final static String EXTRA_TITLE = "title";
//	final static String EXTRA_COLOR = "bgColor";
//	final static String EXTRA_CAT_ID = "catID";
//	int OPERATION_MNU_DET = 78;
//	private int color;
//
//	@Override
//	protected void hookInChildViews() {
//		showOnlyLogo();
//		Intent intent = getIntent();
//		String title = intent.getStringExtra(EXTRA_TITLE);
//		color = intent.getIntExtra(EXTRA_COLOR, 0x000000);
//		TextView tv = (TextView) findViewById(R.id.title);
//		tv.setText(title);
//		tv.setTextColor(color);
//		executeMenuDetailsRequest();
//	}
//
//	private MenuItemDetails processPlist(NSObject obj) {
//		NSArray plist = (NSArray) obj;
//		int categories = plist.count();
//		MenuItemDetails details = new MenuItemDetails();
//		for (int i = 0; i < categories; i++) {
//			NSDictionary dict = (NSDictionary) plist.objectAtIndex(i);
//			MenuItemCategory menuCategory = new MenuItemCategory();
//			NSNumber id = (NSNumber) dict.objectForKey("id");
//			menuCategory.setId(id.intValue());
//			menuCategory.setName(dict.objectForKey("name").toString());
//			menuCategory.setDescription(dict.objectForKey("shortdescription")
//					.toString());
//			NSArray items = (NSArray) dict.objectForKey("items");
//			int number = items.count();
//			for (int y = 0; y < number; y++) {
//				NSDictionary menuItems = (NSDictionary) items.objectAtIndex(y);
//				MenuItem menuItem = new MenuItem();
//				id = (NSNumber) menuItems.objectForKey("id");
//				menuItem.setId(id.intValue());
//				menuItem.setName(menuItems.objectForKey("name").toString());
//				menuItem.setImage(menuItems.objectForKey("image").toString());
//				menuItem.setDescription(menuItems.objectForKey(
//						"shortdescription").toString());
//				id = (NSNumber) menuItems.objectForKey("itemsremaining");
//				menuItem.setItemsRemain(id.intValue());
//				id = (NSNumber) menuItems.objectForKey("price");
//				menuItem.setPrice(id.floatValue());
//				menuCategory.addMenuItem(menuItem);
//			}
//			details.addMenuCategories(menuCategory);
//		}
//		return details;
//	}
//
//	IHttpCallback detailsCallback = new IHttpCallback() {
//
//		@Override
//		public Object populateBean(InputStream is, int operationId) {
//			NSObject object = null;
//			try {
//				object = PropertyListParser.parse(is);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			if (object != null) {
//				// NSArray array = (NSArray) object;
//				// ArrayList<MenuData> menuList = getArray(array);
//				MenuItemDetails det = processPlist(object);
//				return det;
//			} else {
//				return null;
//			}
//		}
//
//		@Override
//		public void onFailure(int message, int operationID) {
//			processFailure(message);
//		}
//
//		@Override
//		public void bindUI(Object t, int operationId) {
//
//			MenuItemDetails menuDetails = (MenuItemDetails) t;
//			List<MenuItemCategory> details = menuDetails.getMenuCategories();
//			LinearLayout parent = (LinearLayout) findViewById(R.id.menuContents);
//			LayoutInflater inflater = getLayoutInflater();
//			int size = details.size();
//			MenuItemCategory category;
//			List<MenuItem> items;
//			MenuItem item;
//			for (int i = 0; i < size; i++) {
//				category = details.get(i);
//				View v = inflater.inflate(R.layout.menu_item_header, null);
//				/*set the background color*/
//				v.findViewById(R.id.header).setBackgroundColor(color);
//				/*set the title and desc*/
//				TextView tv = (TextView) v.findViewById(R.id.mnuTitle);
//				tv.setText(category.getName());
//				tv = (TextView) v.findViewById(R.id.mnuDesc);
//				tv.setText(category.getDescription());
//				/*set the listener*/
//				v.findViewById(R.id.header).setOnClickListener(
//						menuItemClickListener);
//				
//				LinearLayout menuParent = (LinearLayout) v
//						.findViewById(R.id.menuParent);
//				items = category.getMenuItems();
//				int number = items.size();
//				System.out.println("Number of items in category->"+number);
//				for (int j = 0; j < number; j++) {
//					item = items.get(j);
//					System.out.println("Item->"+item);
//					View v2=inflater.inflate(R.layout.menu_item_contents, menuParent);
////					menuParent.addView(v2);
//					TextView title=(TextView) v2.findViewById(R.id.name);
//					title.setText(item.getName());
//					title=(TextView) v2.findViewById(R.id.desc);
//					title.setText(item.getDescription());
////					Button price=(Button) v2.findViewById(R.id.price);
////					price.setText(String.valueOf(item.getPrice())+" Left");
//				}
//
//				parent.addView(v);
//			}
//
//			hideDialog();
//		}
//	};
//
//	private View.OnClickListener menuItemClickListener = new View.OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//
//		}
//	};
//
//	private void executeMenuDetailsRequest() {
//		HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
//		requestWrapper.requestURI = ApplicationConstants.API_MENU_DETAILS;
//		requestWrapper.callback = detailsCallback;
//		requestWrapper.operationID = OPERATION_MNU_DET;
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		String catId = String.valueOf(getIntent().getIntExtra(EXTRA_CAT_ID, 0));
//		NameValuePair param = new BasicNameValuePair("catID", catId);
//		params.add(param);
//		requestWrapper.httpParams = params;
//		// requestWrapper.httpSettings.setHttpMethod(HttpMethod.HTTP_POST);
//		showDialog(getString(R.string.loading_det));
//		new HttpRequestTask().execute(requestWrapper);
//	}
//
//	@Override
//	protected int getSreenID() {
//		return R.layout.todaysmenu_details;
//	}
//
//	@Override
//	protected int getChildViewLayout() {
//		return R.layout.todaysmenu_details;
//	}
//
//	@Override
//	protected String getTitleString() {
//		return "";
//	}
//
//}
