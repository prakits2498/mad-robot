package com.oishii.mobile;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Gallery;
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
import com.oishii.mobile.TodaysMenuDetailList.ResultContainer;
import com.oishii.mobile.beans.AccountStatus;
import com.oishii.mobile.beans.BasketItem;
import com.oishii.mobile.beans.MenuItem;
import com.oishii.mobile.beans.MenuItemCategory;
import com.oishii.mobile.beans.MenuItemDetail;
import com.oishii.mobile.beans.OishiiBasket;
import com.oishii.mobile.beans.SideOrderContainer;
import com.oishii.mobile.util.tasks.BitmapHttpTask;
import com.oishii.mobile.util.tasks.BitmapRequestParam;
import com.oishii.mobile.util.tasks.HttpRequestTask;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;
import com.oishii.mobile.util.tasks.IHttpCallback;

public class TodaysMenuItemDetail extends OishiiBaseActivity {

	static final String PROD_ID = "prod_id";
	static final String COLOR = "color";
	static final String SHOW_ADD_DRINKS_SNACKS = "show_add_DS";
	static final String CAT_ID = "cat_id";
	private int catID;
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

	IHttpCallback galleryCallback = new IHttpCallback() {

		@Override
		public Object populateBean(InputStream is, int operationId) {
			return null;
		}

		@Override
		public void onFailure(int message, int operationID) {
		}

		@Override
		public void bindUI(Object t, int operationId) {
			showGallery();
		}
	};

	private void executeGalleryRequest() {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper(
				getApplicationContext());
		requestWrapper.requestURI = ApplicationConstants.API_MENU_GALLERY;
		requestWrapper.callback = galleryCallback;
		requestWrapper.operationID = 10;
		requestWrapper.httpSettings
				.setHttpMethod(ApplicationConstants.HTTP_METHOD);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		NameValuePair param = new BasicNameValuePair("prodid",
				String.valueOf(productId));
		params.add(param);
		requestWrapper.httpParams = params;
		showDialog(getString(R.string.loading_gallery));
		new HttpRequestTask().execute(requestWrapper);
	}

	View.OnClickListener addToBasketListner = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			final MenuItemDetail item = (MenuItemDetail) v.getTag();
			// final Dialog dialog = new Dialog(TodaysMenuItemDetail.this);
			// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			// dialog.setContentView(R.layout.add_to_basket_dailog);
			// dialog.setTitle(null);
			// TextView tv = (TextView) dialog.findViewById(R.id.itemName);
			// tv.setText(item.getName());
			// final EditText number = (EditText)
			// dialog.findViewById(R.id.number);
			// final TextView count = (TextView) dialog
			// .findViewById(R.id.itemCount);
			// dialog.findViewById(R.id.btnCheckout).setOnClickListener(
			// new View.OnClickListener() {
			// @Override
			// public void onClick(View arg0) {
			AccountStatus status = AccountStatus
					.getInstance(getApplicationContext());
			OishiiBasket basket = status.getBasket();
			if (catID == ApplicationConstants.CAT_ID_CORPORATE) {
				basket.setCorporate(true);
			}
			BasketItem basItem = new BasketItem();
			basItem.setColor(color);
			// int total = Integer.parseInt(number.getText()
			// .toString());
			// basItem.setCount(total);
			basItem.setCount(1);
			basItem.setName(item.getName());
			basItem.setPrice(item.getPrice());
			basItem.setProdId(item.getId());
			basket.addItem(basItem);
			setBasketPrice();
			// Intent intent = new Intent();
			// intent.setClass(TodaysMenuItemDetail.this,
			// Basket.class);
			// intent.putExtra(OutOfSession.SRC_KEY, R.id.basket);
			// dialog.dismiss();
			// startActivity(intent);
			// }
			// });
			// dialog.findViewById(R.id.add).setOnClickListener(
			// new View.OnClickListener() {
			// @Override
			// public void onClick(View arg0) {
			// int total = Integer.parseInt(number.getText()
			// .toString());
			// if (total < 0) {
			// total = 1;
			// }
			// if (total < 99)
			// total++;
			// number.setText(String.valueOf(total));
			// count.setText(total + "X");
			// }
			// });
			//
			// dialog.findViewById(R.id.minus).setOnClickListener(
			// new View.OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// int total = Integer.parseInt(number.getText()
			// .toString());
			// if (total > 1) {
			// total--;
			// }
			// number.setText(String.valueOf(total));
			// count.setText(total + "X");
			// }
			// });
			// LayoutParams params = getWindow().getAttributes();
			// params.height = LayoutParams.FILL_PARENT;
			// dialog.getWindow().setLayout(LayoutParams.FILL_PARENT,
			// LayoutParams.WRAP_CONTENT);
			// dialog.show();
		}
	};
	Dialog drinkDialog;
	Dialog snackDialog;

	private void showAddDrinkDialog() {
		drinkDialog = new Dialog(TodaysMenuItemDetail.this);
		drinkDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		drinkDialog.setContentView(R.layout.sideorderdialog);
		ListView listview = (ListView) drinkDialog.findViewById(R.id.listView1);
		drinkDialog.setTitle(null);
		ArrayList<MenuItem> result = SideOrderContainer.getInstance()
				.getDrinksList();
		ArrayAdapter<MenuItem> timeAdapter = new ArrayAdapter<MenuItem>(this,
				R.layout.side_listview, result);
		listview.setAdapter(timeAdapter);
		listview.setOnItemClickListener(drinkClick);
		drinkDialog.show();
	}

	AdapterView.OnItemClickListener drinkClick = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			final MenuItem item = SideOrderContainer.getInstance().getDrinksList()
					.get(position);
			AccountStatus status = AccountStatus
					.getInstance(getApplicationContext());
			OishiiBasket basket = status.getBasket();
			if (item.getId() == ApplicationConstants.CAT_ID_CORPORATE) {
				basket.setCorporate(true);
			}
			BasketItem basItem = new BasketItem();
			basItem.setColor(color);
			// int total = Integer.parseInt(number.getText()
			// .toString());
			// basItem.setCount(total);
			basItem.setCount(1);
			basItem.setName(item.getName());
			basItem.setPrice(item.getPrice());
			basItem.setProdId(item.getId());
			basket.addItem(basItem);
			setBasketPrice();
			final TextView tv = (TextView) findViewById(R.id.drinkText);
			tv.setText(item.getName());
			final ImageView iv=(ImageView)findViewById(R.id.drinkIcon);
			iv.setImageResource(R.drawable.delete);
			iv.setImageResource(R.drawable.delete);
			iv.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					tv.setText("Add a Drink");
					iv.setImageResource(R.drawable.arrow_green);
					final OishiiBasket basket = AccountStatus.getInstance(
							getApplicationContext()).getBasket();
					basket.removeItemByID(item.getId());
					setBasketPrice();
					v.setOnClickListener(null);
				}
			});
			drinkDialog.dismiss();
		}
	};

	AdapterView.OnItemClickListener snackClick = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			final MenuItem item = SideOrderContainer.getInstance().getSnacksList()
					.get(position);
			AccountStatus status = AccountStatus
					.getInstance(getApplicationContext());
			OishiiBasket basket = status.getBasket();
			if (item.getId() == ApplicationConstants.CAT_ID_CORPORATE) {
				basket.setCorporate(true);
			}
			BasketItem basItem = new BasketItem();
			
			basItem.setColor(color);
			// int total = Integer.parseInt(number.getText()
			// .toString());
			// basItem.setCount(total);
			basItem.setCount(1);
			basItem.setName(item.getName());
			basItem.setPrice(item.getPrice());
			basItem.setProdId(item.getId());
			basket.addItem(basItem);
			setBasketPrice();
			final TextView tv = (TextView) findViewById(R.id.snackText);
			tv.setText(item.getName());
			final ImageView iv=(ImageView)findViewById(R.id.snackIcon);
			iv.setImageResource(R.drawable.delete);
			iv.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					tv.setText("Add a Snack box");
					iv.setImageResource(R.drawable.arrow_green);
					final OishiiBasket basket = AccountStatus.getInstance(
							getApplicationContext()).getBasket();
					basket.removeItemByID(item.getId());
					setBasketPrice();
					v.setOnClickListener(null);
				}
			});
			snackDialog.dismiss();
		}
	};

	private void showAddSnackDialog() {
		snackDialog = new Dialog(TodaysMenuItemDetail.this);
		snackDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		snackDialog.setContentView(R.layout.sideorderdialog);
		ListView listview = (ListView) snackDialog.findViewById(R.id.listView1);
		snackDialog.setTitle(null);
		ArrayList<MenuItem> result = SideOrderContainer.getInstance()
				.getSnacksList();
		ArrayAdapter<MenuItem> timeAdapter = new ArrayAdapter<MenuItem>(this,
				R.layout.side_listview, result);
		listview.setAdapter(timeAdapter);
		listview.setOnItemClickListener(snackClick);
		snackDialog.show();
	}

	private void showGallery() {
		final Dialog dialog = new Dialog(TodaysMenuItemDetail.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.menugallery);
		Gallery menuGallery = (Gallery) dialog.findViewById(R.id.gallery1);
		menuGallery.setAdapter(new ImageAdapter(getApplicationContext()));
		dialog.setTitle(null);
		dialog.getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		dialog.show();
	}

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
			title = (TextView) findViewById(R.id.price);
			title.setText("£" + detail.getPrice());

			title = (TextView) findViewById(R.id.remaining);
			title.setBackgroundColor(color);
			int itemsRemain = detail.getRemaining();
			if (itemsRemain == 0) {
				title.setText(R.string.sold_out);
			} else {
				title.setText(itemsRemain + " Left");
			}

			Button btnAddBasket = (Button) findViewById(R.id.btnToBasket);
			int remain = detail.getRemaining();
			if (!showDrinksSnacks) {
				findViewById(R.id.extrasLayout).setVisibility(View.GONE);
			}
			if (remain == 0) {
				btnAddBasket.setVisibility(View.GONE);
				findViewById(R.id.soldOut).setVisibility(View.VISIBLE);
				findViewById(R.id.extrasLayout).setVisibility(View.GONE);
			} else {
				btnAddBasket.setTag(detail);
				btnAddBasket.setOnClickListener(addToBasketListner);
				/* add drink */
				findViewById(R.id.drinkLayout).setOnClickListener(
						new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								// Intent intent = new Intent(
								// TodaysMenuItemDetail.this,
								// TodaysMenuDetailList.class);
								// intent.putExtra(
								// TodaysMenuDetailList.EXTRA_TITLE,
								// "Add a drink");
								// intent.putExtra(
								// TodaysMenuDetailList.EXTRA_CAT_ID,
								// ApplicationConstants.CAT_ID_DRINKS);
								// intent.putExtra(
								// TodaysMenuDetailList.EXTRA_COLOR,
								// ApplicationConstants.COLOR_DRINKS);
								// startActivity(intent);
								showAddDrinkDialog();

							}
						});
				findViewById(R.id.snackLayout).setOnClickListener(
						new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// Intent intent = new Intent(
								// TodaysMenuItemDetail.this,
								// TodaysMenuDetailList.class);
								// intent.putExtra(
								// TodaysMenuDetailList.EXTRA_TITLE,
								// "Add a Snack box");
								// intent.putExtra(
								// TodaysMenuDetailList.EXTRA_CAT_ID,
								// ApplicationConstants.CAT_ID_SNACKS);
								// intent.putExtra(
								// TodaysMenuDetailList.EXTRA_COLOR,
								// ApplicationConstants.COLOR_SNACKS);
								// startActivity(intent);
								showAddSnackDialog();
							}
						});

			}
			ImageView detailImage = (ImageView) findViewById(R.id.image);
			// detailImage.setOnClickListener(new View.OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // Intent intent = new Intent(TodaysMenuItemDetail.this,
			// // MenuItemGallery.class);
			// // startActivity(intent);
			// if(IOUtils.isSDCardMounted()){
			// // showGallery();
			// executeGalleryRequest();
			// }else{
			// Toast.makeText(getApplicationContext(), R.string.error_no_sdcard,
			// 4000);
			// }
			// }
			// });
			BitmapRequestParam req = new BitmapRequestParam();
			req.bitmapUri = URI.create(detail.getImageUrl());
			req.image = detailImage;// (ImageView) findViewById(R.id.image);
			req.progress = (ProgressBar) findViewById(R.id.progress);
			req.parent = (ViewGroup) findViewById(R.id.parent);
			req.bitmapWidth = 300;
			req.bitmapHeight = 250;
			new BitmapHttpTask().execute(req);
			hideDialog();
		}
	};

	public static final int OPERATION_ID = 30;

	private void executeDetailsRequest() {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper(
				getApplicationContext());
		requestWrapper.requestURI = ApplicationConstants.API_MENU_SPECIFIC_DETAILS;
		requestWrapper.callback = details;
		requestWrapper.operationID = OPERATION_ID;
		requestWrapper.httpSettings
				.setHttpMethod(ApplicationConstants.HTTP_METHOD);
		requestWrapper.intExtra = productId;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		NameValuePair param = new BasicNameValuePair("prodid",
				String.valueOf(productId));
		params.add(param);
		requestWrapper.httpParams = params;
		showDialog(getString(R.string.loading));
		new HttpRequestTask().execute(requestWrapper);
	}

	boolean showDrinksSnacks;

	@Override
	protected void hookInChildViews() {
		productId = getIntent().getIntExtra(PROD_ID, 0);
		color = getIntent().getIntExtra(COLOR, 0);
		catID = getIntent().getIntExtra(CAT_ID, 0);
		showDrinksSnacks = getIntent().getBooleanExtra(SHOW_ADD_DRINKS_SNACKS,
				true);
		Button view = (Button) findViewById(R.id.btnHome);
		view.setVisibility(View.VISIBLE);
		view.setText("Menu");
		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent todays = new Intent(TodaysMenuItemDetail.this,
						TodaysMenu.class);
				todays.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(todays);
			}
		});
		showOnlyLogo();
		executeDetailsRequest();
	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.todays_menu_item_detail;
	}

	public class ImageAdapter extends BaseAdapter {
		int mGalleryItemBackground;
		private Context mContext;

		private Integer[] mImageIds = { R.drawable.acc, R.drawable.acc_sel,
				R.drawable.arrow_green, R.drawable.back,
				R.drawable.back_selector, R.drawable.basket,
				R.drawable.bg_selector };

		public ImageAdapter(Context c) {
			mContext = c;
			// TypedArray attr = mContext
			// .obtainStyledAttributes(R.styleable.HelloGallery);
			// mGalleryItemBackground = attr.getResourceId(
			// R.styleable.HelloGallery_android_galleryItemBackground, 0);
			// attr.recycle();
		}

		public int getCount() {
			return mImageIds.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = new ImageView(mContext);

			imageView.setImageResource(mImageIds[position]);
			imageView.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			// imageView.setBackgroundResource(mGalleryItemBackground);

			return imageView;
		}
	}
}
