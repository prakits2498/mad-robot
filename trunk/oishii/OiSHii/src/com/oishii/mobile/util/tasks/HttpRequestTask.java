package com.oishii.mobile.util.tasks;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.oishii.mobile.ApplicationConstants;
import com.oishii.mobile.R;
import com.oishii.mobile.TodaysMenuDetailList;
import com.oishii.mobile.TodaysMenuItemDetail;
import com.oishii.mobile.util.CarrierHelper;
import com.oishii.mobile.util.HttpTaskHelper;
import com.oishii.mobile.util.IOUtils;

public class HttpRequestTask extends
		AsyncTask<HttpRequestWrapper, Void, HttpResponseWrapper> {

	private HttpRequestWrapper wrapper;
	private HttpResponseWrapper response;

	@Override
	protected HttpResponseWrapper doInBackground(HttpRequestWrapper... params) {
		this.wrapper = params[0];
		Context context = wrapper.ctx;
		CarrierHelper bearerHelper = CarrierHelper.getInstance(context);
		response = new HttpResponseWrapper();
		response.callback = wrapper.callback;
		InputStream is = null;
		if (bearerHelper.getCurrentCarrier() != null) {
			HttpTaskHelper helper = new com.oishii.mobile.util.HttpTaskHelper(
					wrapper.requestURI);
			helper.setHttpSettings(wrapper.httpSettings);
			helper.setRequestParameter(wrapper.httpParams);
			try {
				HttpResponse httpEntity = helper.execute();
				int statusCode = httpEntity.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					is = httpEntity.getEntity().getContent();
					if (params[0].canCache)
						if (!hasCacheAPI()) {
							is = cacheAPI(is);
						}
				} else {
					Log.e("Oishi", "Invalid response received from server!==>"
							+ statusCode);
					if (params[0].canCache)
						is = getCachedAPI();
				}
			} catch (IOException e) {
				response.errorMessage = R.string.error_bad_response;
				response.isSuccess = false;
				e.printStackTrace();

			}
		} else {
			/* Load from local cache. */
			System.out.println("No Carrier availale");
			if (wrapper.canCache)
				is = getCachedAPI();
		}

		if (is != null) {
			deserialize(is);
		} else {
			response.errorMessage = R.string.error_no_data;
			response.isSuccess = false;
		}
		return response;
	}

	private void deserialize(InputStream is) {
		Object obj = wrapper.callback.populateBean(is, wrapper.operationID);
		if (obj != null) {
			System.out.println("Object=>" + obj);
			response.responseBean = obj;
			response.isSuccess = true;
			response.operationId = wrapper.operationID;
		} else {// deserialization failed
			Log.e("Oishi", "Error in deserialization!");
			response.errorMessage = R.string.error_bad_response;
			response.isSuccess = false;
		}
	}

	protected void onPostExecute(HttpResponseWrapper result) {
		Log.e("Oishi", "on Post execute");
		if (result.isSuccess) {
			result.callback.bindUI(result.responseBean, result.operationId);
		} else {
			result.callback.onFailure(result.errorMessage, result.operationId);
		}
	}

	private boolean hasCacheAPI() {
		String[] file = wrapper.ctx.fileList();
		String cacheName = getCachedAPIName();
		Log.d("Oishii", "Caching API->" + cacheName);
		for (int i = 0; i < file.length; i++) {
			if (cacheName.equals(file[i]))
				return true;
		}
		return false;
	}

	private String getCachedAPIName() {
		switch (wrapper.operationID) {
		case TodaysMenuDetailList.OPERATION_MENU_DETAILS:
		case TodaysMenuItemDetail.OPERATION_ID:
			String name = ApplicationConstants.operationMap
					.get(wrapper.operationID) + wrapper.intExtra + ".plist";
			return name;
		default:
			return ApplicationConstants.operationMap.get(wrapper.operationID);
		}

	}

	private InputStream getCachedAPI() {
		String cacheName = getCachedAPIName();
		try {
			Log.e("Cache", "Loading Cached API "+cacheName);
			return wrapper.ctx.openFileInput(cacheName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private InputStream cacheAPI(InputStream is) {
		String apiName =getCachedAPIName();
		try {
			FileOutputStream fos = wrapper.ctx.openFileOutput(apiName,
					Context.MODE_PRIVATE);
			IOUtils.copy(is, fos);
			Log.e("Cache", "Caching API =>"+apiName);
			return wrapper.ctx.openFileInput(apiName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return is;
		} catch (IOException e) {
			e.printStackTrace();
			return is;
		}

	}
}
