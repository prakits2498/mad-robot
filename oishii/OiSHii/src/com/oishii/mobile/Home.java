package com.oishii.mobile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.madrobot.di.plist.NSArray;
import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSNumber;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
import com.oishii.mobile.TodaysMenuDetailList.ResultContainer;
import com.oishii.mobile.beans.MenuItem;
import com.oishii.mobile.beans.MenuItemCategory;
import com.oishii.mobile.beans.SideOrderContainer;
import com.oishii.mobile.util.tasks.HttpRequestTask;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;
import com.oishii.mobile.util.tasks.IHttpCallback;

public class Home extends OishiiBaseActivity {
	private class ImageRunner extends Thread {
		private boolean canRun = true;
		private int imageIndex = 1;
		private int[] images = new int[] { R.drawable.home_bg,
				R.drawable.home_bg2, R.drawable.home_bg3, R.drawable.home_bg4 };

		public void run() {
			System.out.println("Image roll started");
			while (canRun) {
				try {
					Thread.sleep(8000);
					if (canRun) {
						setBannerImage(images[imageIndex]);
						if (imageIndex < (images.length - 1))
							imageIndex++;
						else if (imageIndex == (images.length - 1)) {
							imageIndex = 0;
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Image roll stopped");
		}
	}

	private void stopRoll() {
		if (runner != null) {
			runner.canRun = false;
			runner = null;
		}
	}

	private ImageView iv;

	private ImageRunner runner;

	@Override
	protected int getChildViewLayout() {
		return R.layout.home;
	}

	@Override
	protected int getParentScreenId() {
		return R.id.about;
	}

	@Override
	protected int getSreenID() {
		return R.id.about;
	}

	@Override
	protected String getTitleString() {
		return "";
	}

	@Override
	protected boolean hasTitleBar() {
		return false;
	}

	@Override
	protected void hookInChildViews() {
		iv = (ImageView) findViewById(R.id.homeBanner);
		findViewById(R.id.todaysMenu).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						stopRoll();
						executeSideOrderMenuRequest(
								ApplicationConstants.CAT_ID_DRINKS,
								drinksCallback);
						executeSideOrderMenuRequest(
								ApplicationConstants.CAT_ID_SNACKS,
								snackCallback);
						Intent todays = new Intent(Home.this, TodaysMenu.class);
						startActivity(todays);
					}
				});
		runner = new ImageRunner();
		runner.start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (runner == null) {
			runner = new ImageRunner();
			runner.start();
		}
	}

	@Override
	protected void doBeforeMenuAction() {
		stopRoll();
	}

	private void setBannerImage(final int imgRes) {
		iv.post(new Runnable() {
			@Override
			public void run() {
				iv.setImageResource(imgRes);
			}

		});
	}

	private IHttpCallback snackCallback = new IHttpCallback() {

		@Override
		public Object populateBean(InputStream is, int operationId) {
			NSObject object = null;
			try {
				object = PropertyListParser.parse(is);
				if (object != null) {
					ArrayList<MenuItem> children = processPlist(object);
					return children;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public void onFailure(int message, int operationID) {

		}

		@Override
		public void bindUI(Object t, int operationId) {
			ArrayList<MenuItem> children = (ArrayList<MenuItem>) t;
			SideOrderContainer container = SideOrderContainer.getInstance();
			container.setSnacksList(children);
		}
	};

	private ArrayList<MenuItem> processPlist(NSObject obj)
			throws Exception {
		NSArray plist = (NSArray) obj;
		int categories = plist.count();
		// ResultContainer container = new ResultContainer();
		// ArrayList<MenuItemCategory> parentGroups = new
		// ArrayList<MenuItemCategory>();
		ArrayList<MenuItem> childGroups = new ArrayList<MenuItem>();

		for (int i = 0; i < categories; i++) {
			NSDictionary dict = (NSDictionary) plist.objectAtIndex(i);
			// MenuItemCategory menuCategory = new MenuItemCategory();
			NSNumber id;
			// = (NSNumber) dict.objectForKey("id");
			// menuCategory.setId(id.intValue());
			// menuCategory.setName(dict.objectForKey("name").toString());
			// menuCategory.setDescription(dict.objectForKey("shortdescription")
			// .toString());
			// list.add(menuCategory);

			// parentGroups.add(menuCategory);
			NSArray items = (NSArray) dict.objectForKey("items");
//			ArrayList<MenuItem> childGroup = new ArrayList<MenuItem>();
			int number = items.count();

			for (int y = 0; y < number; y++) {
				NSDictionary menuItems = (NSDictionary) items.objectAtIndex(y);
				MenuItem menuItem = new MenuItem();
				id = (NSNumber) menuItems.objectForKey("id");
				menuItem.setId(id.intValue());
				menuItem.setName(menuItems.objectForKey("name").toString());
				// menuItem.setImage(menuItems.objectForKey("image").toString());
				// menuItem.setDescription(menuItems.objectForKey(
				// "shortdescription").toString());
				id = (NSNumber) menuItems.objectForKey("itemsremaining");
				menuItem.setItemsRemain(id.intValue());
				id = (NSNumber) menuItems.objectForKey("price");
				menuItem.setPrice(id.floatValue());
				// menuItem.setCategory(menuCategory);
				if (menuItem.getItemsRemain() > 0)
					childGroups.add(menuItem);
			}
//			childGroups.add(childGroup);
		}
		// container.parent = parentGroups;
		// container.children = childGroups;

		return childGroups;
	}

	private IHttpCallback drinksCallback = new IHttpCallback() {

		@Override
		public Object populateBean(InputStream is, int operationId) {
			NSObject object = null;
			try {
				object = PropertyListParser.parse(is);
				if (object != null) {
					ArrayList<MenuItem> children = processPlist(object);
					return children;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public void onFailure(int message, int operationID) {

		}

		@Override
		public void bindUI(Object t, int operationId) {
			ArrayList<MenuItem> children = (ArrayList<MenuItem>) t;
			SideOrderContainer container = SideOrderContainer.getInstance();
			container.setDrinksList(children);
		}
	};

	private void executeSideOrderMenuRequest(int category,
			IHttpCallback callback) {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper(
				getApplicationContext());
		requestWrapper.requestURI = ApplicationConstants.API_MENU_DETAILS;
		requestWrapper.callback = callback;
		requestWrapper.operationID = TodaysMenuDetailList.OPERATION_MENU_DETAILS;
		requestWrapper.httpSettings
				.setHttpMethod(ApplicationConstants.HTTP_METHOD);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		NameValuePair param = new BasicNameValuePair("catID",
				String.valueOf(category));
		params.add(param);
		requestWrapper.httpParams = params;
		new HttpRequestTask().execute(requestWrapper);
	}
}
