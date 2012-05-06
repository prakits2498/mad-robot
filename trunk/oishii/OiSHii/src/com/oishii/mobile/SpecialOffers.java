package com.oishii.mobile;

import java.io.InputStream;
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
import android.widget.ListView;
import android.widget.TextView;

import com.madrobot.di.plist.NSArray;
import com.madrobot.di.plist.NSDictionary;
import com.madrobot.di.plist.NSObject;
import com.madrobot.di.plist.PropertyListParser;
import com.oishii.mobile.beans.CurrentScreen;
import com.oishii.mobile.beans.MenuData;
import com.oishii.mobile.beans.SpecialOffer;
import com.oishii.mobile.util.tasks.HttpRequestTask;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;
import com.oishii.mobile.util.tasks.IHttpCallback;

public class SpecialOffers extends ListOishiBase {
	private static final int OPERATION_SPL_OFFER = 10;

	@Override
	protected void hookInListData() {
		TextView tv = (TextView) findViewById(R.id.titleFirst);
		tv.setText(R.string.special);
		TextView tv2 = (TextView) findViewById(R.id.titleSecond);
		tv2.setText(R.string.offers);
		executeSpecialOffersRequest();
	}

	protected void executeSpecialOffersRequest() {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
		requestWrapper.requestURI = ApplicationConstants.API_SPECIAL_OFFERS;
		requestWrapper.callback = splOffersCallback;
		requestWrapper.operationID = OPERATION_SPL_OFFER;
		requestWrapper.httpSettings.setHttpMethod(ApplicationConstants.HTTP_METHOD);
		showDialog(getString(R.string.loading_offers));
		new HttpRequestTask().execute(requestWrapper);
	}

	IHttpCallback splOffersCallback = new IHttpCallback() {
		@Override
		public Object populateBean(InputStream is, int operationId) {
			NSObject object = null;
			try {
				object = PropertyListParser.parse(is, "US-ASCII");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (object != null) {
				NSArray array = (NSArray) object;
				ArrayList<SpecialOffer> offerList = getArray(array);

				return offerList;
			} else {
				return null;
			}
		}

		@Override
		public void onFailure(int message, int operationID) {
			processFailure(message);
		}

		@Override
		public void bindUI(Object t, int operationId) {
			SplOfferAdapter adapter = new SplOfferAdapter(
					getApplicationContext(), R.layout.specialoffers,
					(List<SpecialOffer>) t);
			ListView listview = getListView(true);
			listview.setOnItemClickListener(listViewClickListener);
			listview.setAdapter(adapter);
			hideDialog();
		}
	};

	AdapterView.OnItemClickListener listViewClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (arg2 == 0)
				return;
			showNotImplToast();
		}
	};

	private ArrayList<SpecialOffer> getArray(NSArray array) {
		int count = array.count();
		ArrayList<SpecialOffer> offers = new ArrayList<SpecialOffer>();
		for (int i = 0; i < count; i++) {
			NSDictionary d = (NSDictionary) array.objectAtIndex(i);
			SpecialOffer offer = new SpecialOffer();
			offer.setOfferName(d.objectForKey("name").toString());
			offer.setShortDesc(d.objectForKey("shortdescription").toString());
			
			offer.setColor(Color.parseColor(d.objectForKey("color").toString()));
			offers.add(offer);
		}
		return offers;
	}

	class SplOfferAdapter extends ArrayAdapter<SpecialOffer> {

		public SplOfferAdapter(Context context, int textViewResourceId,
				List<SpecialOffer> objects) {
			super(context, textViewResourceId, objects);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			SpecialOffer item = getItem(position);
			if (view == null) {
				view = getLayoutInflater()
						.inflate(R.layout.specialoffers, null);
				view.setBackgroundColor(item.getColor());
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.text1 = (TextView) view
						.findViewById(R.id.offerTitle);
				viewHolder.text2 = (TextView) view.findViewById(R.id.offerDesc);
				view.setTag(viewHolder);
			}
			
			ViewHolder viewHolder = (ViewHolder) view.getTag();
			viewHolder.text1.setText(item.getOfferName());
			viewHolder.text2.setText(item.getShortDesc());
			return view;

		}

	}

	private static class ViewHolder {
		TextView text1;
		TextView text2;
	}

	@Override
	protected int getSreenID() {
		return R.id.offers;
	}

	@Override
	protected int getParentScreenId() {
		// TODO Auto-generated method stub
		return R.id.offers;
	}
}
