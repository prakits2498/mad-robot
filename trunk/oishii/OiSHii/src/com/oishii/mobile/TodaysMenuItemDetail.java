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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.madrobot.di.plist.NSArray;
import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSNumber;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.beans.BasketItem;
import com.oishii.mobile.beans.MenuItemDetail;
import com.oishii.mobile.beans.OishiiBasket;
import com.oishii.mobile.util.tasks.BitmapHttpTask;
import com.oishii.mobile.util.tasks.BitmapRequestParam;
import com.oishii.mobile.util.tasks.HttpRequestTask;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;
import com.oishii.mobile.util.tasks.IHttpCallback;

public class TodaysMenuItemDetail extends OishiiBaseActivity {

	static final String PROD_ID = "prod_id";
	static final String COLOR = "color";
	private int productId;
	private int color;

	@Override
	protected int getParentScreenId() {
		return R.id.about;
	}

	@Override
	protected int getSreenID() {
		return R.layout.todays_menu_item_detail;
	}

	@Override
	protected String getTitleString() {
		return "";
	}

	private MenuItemDetail processPlist(NSObject obj) {
		MenuItemDetail detail = new MenuItemDetail();
		NSArray array = (NSArray) obj;
		for (int i = 0; i < array.count(); i++) {
			NSDictionary dict = (NSDictionary) array.objectAtIndex(i);
			NSNumber id = (NSNumber) dict.objectForKey("id");
			detail.setId(id.intValue());
			String str = dict.objectForKey("name").toString();
			detail.setName(str);
			str = dict.objectForKey("image").toString();
			detail.setImageUrl(str);
			str = dict.objectForKey("description").toString();
			detail.setDescription(str);
			id = (NSNumber) dict.objectForKey("itemsremaining");
			detail.setRemaining(id.intValue());
			id = (NSNumber) dict.objectForKey("price");
			detail.setPrice(id.floatValue());
			str = dict.objectForKey("category").toString();
			detail.setCategory(str);
		}
		return detail;

	}
	
	View.OnClickListener addToBasketListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			final MenuItemDetail item = (MenuItemDetail) v.getTag();
			final Dialog dialog = new Dialog(TodaysMenuItemDetail.this);
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
							intent.setClass(TodaysMenuItemDetail.this, clz);
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


	IHttpCallback details = new IHttpCallback() {

		@Override
		public Object populateBean(InputStream is, int operationId) {
			NSObject object = null;
			try {
				object = PropertyListParser.parse(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (object != null) {
				MenuItemDetail det = processPlist(object);
				return det;
			}
			return null;

		}

		@Override
		public void onFailure(int message, int operationID) {
			processFailure(message);

		}

		@Override
		public void bindUI(Object t, int operationId) {
			MenuItemDetail detail = (MenuItemDetail) t;
			TextView title = (TextView) findViewById(R.id.title);
			title.setText(detail.getName());
			title.setTextColor(color);
			title = (TextView) findViewById(R.id.desc);
			title.setText(detail.getDescription());
			title = (TextView) findViewById(R.id.titleFirst);
			title.setText(detail.getCategory());
			title.setTextColor(color);
			Button btnAddBasket = (Button) findViewById(R.id.btnToBasket);
			int remain = detail.getRemaining();
			if (remain == 0) {
				btnAddBasket.setBackgroundResource(R.drawable.sold_out);
				btnAddBasket.setText(R.string.sold_out);
				btnAddBasket.setTextColor(Color.BLACK);
			} else {
				btnAddBasket.setTag(detail);
				btnAddBasket
						.setOnClickListener(addToBasketListner);
			}
			BitmapRequestParam req = new BitmapRequestParam();
			req.bitmapUri = URI.create(detail.getImageUrl());
			req.image = (ImageView) findViewById(R.id.image);
			req.progress = (ProgressBar) findViewById(R.id.progress);
			req.parent = (ViewGroup) findViewById(R.id.parent);
			new BitmapHttpTask().execute(req);
			hideDialog();
		}
	};

	private void executeDetailsRequest() {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
		requestWrapper.requestURI = ApplicationConstants.API_MENU_SPECIFIC_DETAILS;
		requestWrapper.callback = details;
		requestWrapper.operationID = 10;
		requestWrapper.httpSettings
				.setHttpMethod(ApplicationConstants.HTTP_METHOD);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		NameValuePair param = new BasicNameValuePair("prodid",
				String.valueOf(productId));
		params.add(param);
		requestWrapper.httpParams = params;
		showDialog(getString(R.string.loading));
		new HttpRequestTask().execute(requestWrapper);
	}

	@Override
	protected void hookInChildViews() {
		productId = getIntent().getIntExtra(PROD_ID, 0);
		color = getIntent().getIntExtra(COLOR, 0);
		showOnlyLogo();
		executeDetailsRequest();
	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.todays_menu_item_detail;
	}

}
