package com.oishii.mobile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.madrobot.di.plist.NSArray;
import com.madrobot.di.plist.NSDate;
import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSNumber;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.beans.EmptyOrder;
import com.oishii.mobile.beans.HistoryContainer;
import com.oishii.mobile.beans.Item;
import com.oishii.mobile.beans.Order;
import com.oishii.mobile.beans.OrderCategory;
import com.oishii.mobile.util.HttpSettings;
import com.oishii.mobile.util.tasks.HttpRequestTask;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;
import com.oishii.mobile.util.tasks.IHttpCallback;

public class History extends ListOishiBase {
	private final int OPERATION_HISTORY = 45;

	@Override
	protected int getParentScreenId() {
		return R.id.history;
	}

	private HistoryContainer getHistoryContainer(NSArray array)
			throws Exception {
		HistoryContainer container = new HistoryContainer();
		List<OrderCategory> categories = new ArrayList<OrderCategory>();
		List<List<Order>> orderList = new ArrayList<List<Order>>();
		int count = array.count();
		NSDictionary dict;
		String str;
		NSArray orders;
		for (int i = 0; i < count; i++) {
			dict = (NSDictionary) array.objectAtIndex(i);
			OrderCategory category = new OrderCategory();
			str = dict.objectForKey("groups").toString();
			category.setCategoryName(str);
			ArrayList<Order> ordersInCat = new ArrayList<Order>();
			orders = (NSArray) dict.objectForKey("orders");
			int orderCount = orders.count();
			if (orders.count() == 0) {
				/* There are no orders in this category */
				ordersInCat.add(new EmptyOrder());
			} else {
				/* Add order to list */
				for (int j = 0; j < orderCount; j++) {
					dict = (NSDictionary) orders.objectAtIndex(j);
					ordersInCat.add(processOrder(dict));
				}
			}
			orderList.add(ordersInCat);
			categories.add(category);
		}

		container.setOrderCategories(categories);
		container.setOrderDetails(orderList);
		return container;
	}

	private Order processOrder(NSDictionary dict) {
		Order order = new Order();
		NSNumber no = (NSNumber) dict.objectForKey("orderid");
		order.setOrderId(no.intValue());
		String str = dict.objectForKey("deliverytime").toString();
		order.setDeliveryTime(str);
		no = (NSNumber) dict.objectForKey("discount");
		order.setDiscount(no.floatValue());
		no = (NSNumber) dict.objectForKey("subtotal");
		order.setSubtotal(no.floatValue());
		no = (NSNumber) dict.objectForKey("totalprice");
		order.setTotalPrice(no.floatValue());
		str = dict.objectForKey("status").toString();
		order.setStatus(str);
		NSDate date = (NSDate) dict.objectForKey("date");
		order.setDate(date.getDate());
		NSArray items = (NSArray) dict.objectForKey("items");
		int count = items.count();
		NSDictionary orderItems;
		List<Item> itemsList = new ArrayList<Item>();
		Item currentItem;
		for (int i = 0; i < count; i++) {
			orderItems = (NSDictionary) items.objectAtIndex(i);
			currentItem = new Item();
			no = (NSNumber) orderItems.objectForKey("id");
			currentItem.setId(no.intValue());
			str = orderItems.objectForKey("name").toString();
			currentItem.setName(str);
			no = (NSNumber) orderItems.objectForKey("price");
			currentItem.setPrice(no.floatValue());
			str = orderItems.objectForKey("sku").toString();
			currentItem.setSku(str);
			no = (NSNumber) orderItems.objectForKey("quantity");
			currentItem.setQuantity(no.intValue());
			itemsList.add(currentItem);
		}
		order.setItems(itemsList);
		return order;
	}

	IHttpCallback historyCallback = new IHttpCallback() {
		@Override
		public Object populateBean(InputStream is, int operationId) {
			NSObject object = null;
			try {
				object = PropertyListParser.parse(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (object != null) {
				HistoryContainer det;
				try {
					det = getHistoryContainer((NSArray) object);
					return det;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		public void onFailure(int message, int operationID) {
			processFailure(message);
		}

		@Override
		public void bindUI(Object t, int operationId) {
			HistoryContainer container = (HistoryContainer) t;
			ExpandableListView list = getExandableList(false);

			HistoryExpandableAdapter adapter = new HistoryExpandableAdapter(
					container.getOrderCategories(), container.getOrderDetails());
			list.setAdapter(adapter);
			list.setVisibility(View.VISIBLE);
			int groupCount = container.getOrderCategories().size();
			for (int i = 0; i < groupCount; i++) {
				list.expandGroup(i);
			}
			hideDialog();
		}
	};

	private void executeHistoryRequest() {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
		requestWrapper.requestURI = ApplicationConstants.API_MY_HISTORY;
		requestWrapper.callback = historyCallback;
		HttpSettings settings = new HttpSettings();
		requestWrapper.httpSettings
				.setHttpMethod(ApplicationConstants.HTTP_METHOD);
		requestWrapper.httpSettings = settings;
		requestWrapper.operationID = OPERATION_HISTORY;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		AccountStatus accStat = AccountStatus
				.getInstance(getApplicationContext());
		NameValuePair param = new BasicNameValuePair("mac", accStat.getMac());
		params.add(param);
		param = new BasicNameValuePair("sid", accStat.getSid());
		params.add(param);
		requestWrapper.httpParams = params;
		showDialog(getString(R.string.loading_history));
		new HttpRequestTask().execute(requestWrapper);
	}

	@Override
	protected int getSreenID() {
		return R.string.title_history;
	}

	@Override
	protected String getTitleString() {
		return getString(R.string.title_history);
	}

	@Override
	protected void hookInListData() {
		executeHistoryRequest();
		findViewById(R.id.shadow_title).setVisibility(View.GONE);
		showOnlyTitle();

	}

	private class HistoryExpandableAdapter extends BaseExpandableListAdapter {

		private List<OrderCategory> parents;
		private List<List<Order>> children;

		private HistoryExpandableAdapter(List<OrderCategory> orderCategories,
				List<List<Order>> orderDetails) {
			this.parents = orderCategories;
			this.children = orderDetails;
		}

		@Override
		public Order getChild(int group, int child) {
			List<Order> chil = children.get(group);
			return chil.get(child);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			List<Order> menu = children.get(groupPosition);
			View v;
			if (menu.get(childPosition) instanceof EmptyOrder) {
				v = getLayoutInflater().inflate(R.layout.history_empty_order,
						null);
			} else {

				Order item = menu.get(childPosition);
				v = getLayoutInflater().inflate(R.layout.history_child, null);
				String str = item.getItems().size() + "X";
				TextView tv = (TextView) v.findViewById(R.id.itemCount);
				tv.setText(str);
				str = getString(R.string.items_text) + "(£"
						+ item.getTotalPrice() + ")";
				tv = (TextView) v.findViewById(R.id.itemNo);
				tv.setText(str);
				tv = (TextView) v.findViewById(R.id.status);
				tv.setText(item.getStatus());
				LinearLayout items = (LinearLayout) v.findViewById(R.id.items);
				List<Item> myItems = item.getItems();

				for (int i = 0; i < myItems.size(); i++) {
					tv = new TextView(getApplicationContext());
					tv.setTextColor(Color.BLACK);
					tv.setText(myItems.get(i).getName());
					items.addView(tv);
				}
			}
			return v;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return children.get(groupPosition).size();
		}

		@Override
		public List<Order> getGroup(int groupPosition) {
			return children.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return parents.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			OrderCategory category = parents.get(groupPosition);
			View v = getLayoutInflater().inflate(R.layout.history_header, null);
			TextView tv = (TextView) v.findViewById(R.id.title);
			tv.setText(category.getCategoryName());
			return v;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}

}
