package com.oishii.mobile;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.madrobot.di.plist.NSArray;
import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSNumber;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.beans.BasketItem;
import com.oishii.mobile.beans.MenuItem;
import com.oishii.mobile.beans.MenuItemCategory;
import com.oishii.mobile.beans.OishiiBasket;
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
		showOnlyLogo();
		Intent intent = getIntent();
		String title = intent.getStringExtra(EXTRA_TITLE);
		color = intent.getIntExtra(EXTRA_COLOR, Color.parseColor("#F98E00"));
		TextView tv = (TextView) findViewById(R.id.titleFirst);
		tv.setText(title);
		tv.setTextColor(color);
		executeMenuDetailsRequest();
	}

	View.OnClickListener btnListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			MenuItem item = (MenuItem) v.getTag();
			Intent intent = new Intent(TodaysMenuDetailList.this,
					TodaysMenuItemDetail.class);
			intent.putExtra(TodaysMenuItemDetail.COLOR, color);
			intent.putExtra(TodaysMenuItemDetail.PROD_ID, item.getId());
			startActivity(intent);

		}
	};

	View.OnClickListener addToBasketListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			final MenuItem item = (MenuItem) v.getTag();
			final Dialog dialog = new Dialog(TodaysMenuDetailList.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.add_to_basket_dailog);
			dialog.setTitle(null);
			TextView tv = (TextView) dialog.findViewById(R.id.itemName);
			tv.setText(item.getName());
			final EditText number = (EditText) dialog.findViewById(R.id.number);
			final TextView count = (TextView) dialog
					.findViewById(R.id.itemCount);
			dialog.findViewById(R.id.btnCheckout).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View arg0) {
							AccountStatus status = AccountStatus
									.getInstance(getApplicationContext());
							OishiiBasket basket = status.getBasket();
							BasketItem basItem = new BasketItem();
							basItem.setColor(color);
							int total = Integer.parseInt(number.getText()
									.toString());
							basItem.setCount(total);
							basItem.setName(item.getName());
							basItem.setPrice(item.getPrice());
							basItem.setProdId(item.getId());
							basket.addItem(basItem);

							Intent intent = new Intent();
							Class clz;
							if (!status.isSignedIn()) {

								clz = OutOfSession.class;
								// intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							} else {
								clz = Basket.class;
							}
							intent.setClass(TodaysMenuDetailList.this, clz);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.putExtra(OutOfSession.SRC_KEY, R.id.basket);
							dialog.dismiss();
							startActivity(intent);
						}
					});
			dialog.findViewById(R.id.add).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View arg0) {
							int total = Integer.parseInt(number.getText()
									.toString());
							if (total < 0) {
								total = 1;
							}
							if (total < 99)
								total++;
							number.setText(String.valueOf(total));
							count.setText(total + "X");
						}
					});

			dialog.findViewById(R.id.minus).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							int total = Integer.parseInt(number.getText()
									.toString());
							if (total > 1) {
								total--;
							}
							number.setText(String.valueOf(total));
							count.setText(total + "X");
						}
					});
			LayoutParams params = getWindow().getAttributes();
			params.height = LayoutParams.FILL_PARENT;
			dialog.getWindow().setLayout(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT);
			dialog.show();
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
		requestWrapper.httpSettings
				.setHttpMethod(ApplicationConstants.HTTP_METHOD);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String catId = String.valueOf(getIntent().getIntExtra(EXTRA_CAT_ID, 0));
		NameValuePair param = new BasicNameValuePair("catID", catId);
		params.add(param);
		requestWrapper.httpParams = params;
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
				if (object != null) {
					ResultContainer det = processPlist(object);
					return det;
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	};

	private class ResultContainer {
		private ArrayList<MenuItemCategory> parent;
		private ArrayList<ArrayList<MenuItem>> children;
	}

	private ResultContainer processPlist(NSObject obj) throws Exception {
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
			tv.setBackgroundColor(color);
			Button btn = (Button) v.findViewById(R.id.btnAddBasket);
			int itemsRemain = item.getItemsRemain();
			if (itemsRemain == 0) {
				tv.setText(R.string.sold_out);
				btn.setVisibility(View.GONE);
			} else {
				btn.setTag(item);
				btn.setOnClickListener(addToBasketListner);
				tv.setText(item.getItemsRemain() + " Left");
			}
			ImageView image = (ImageView) v.findViewById(R.id.menuImg);
			image.setId(group + child);
			TextView price = (TextView) v.findViewById(R.id.price);
			price.setText("£" + item.getPrice());

			btn = (Button) v.findViewById(R.id.detail);
			btn.setTag(item);
			btn.setOnClickListener(btnListener);

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
				req.parent = parent;
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
			View v = getLayoutInflater().inflate(R.layout.menu_item_header,
					null);
			v.setBackgroundColor(color);
			TextView tv = (TextView) v.findViewById(R.id.mnuTitle);
			tv.setText(category.getName());
			tv = (TextView) v.findViewById(R.id.mnuDesc);
			tv.setText(category.getDescription());
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
		return R.id.about;
	}

}
