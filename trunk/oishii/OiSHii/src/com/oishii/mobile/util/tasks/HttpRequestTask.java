package com.oishii.mobile.util.tasks;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.oishii.mobile.ApplicationConstants;
import com.oishii.mobile.R;
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
		Context context = params[0].ctx;
		int operationID = params[0].operationID;
		CarrierHelper bearerHelper = CarrierHelper.getInstance(context);
		response = new HttpResponseWrapper();
		response.callback = params[0].callback;
		InputStream is = null;
		if (bearerHelper.getCurrentCarrier() != null) {
			HttpTaskHelper helper = new com.oishii.mobile.util.HttpTaskHelper(
					params[0].requestURI);
			helper.setHttpSettings(params[0].httpSettings);
			helper.setRequestParameter(params[0].httpParams);
			try {
				HttpResponse httpEntity = helper.execute();
				int statusCode = httpEntity.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					is = httpEntity.getEntity().getContent();
					if (params[0].canCache)
						if (!hasCacheAPI(operationID, context)) {
							is = cacheAPI(is, context, operationID);
						}
				} else {
					Log.e("Oishi", "Invalid response received from server!==>"
							+ statusCode);
					if (params[0].canCache)
						is = getCachedAPI(operationID, context); 
				}
			} catch (IOException e) {
				response.errorMessage = R.string.error_no_data;
				response.isSuccess = false;
				e.printStackTrace();

			}
		} else {
			/* Load from local cache. */
			System.out.println("No Carrier availale");
			if (params[0].canCache)
				is = getCachedAPI(operationID, context);
		}

		if (is != null) {
			deserialize(is);
		} else {
			response.errorMessage = R.string.error_bad_response;
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
			response.errorMessage = R.string.error_bad_response;// "Could not read server response!";
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

	private boolean hasCacheAPI(int operationId, Context ctx) {
		String[] file = ctx.fileList();
		String cacheName = ApplicationConstants.operationMap.get(operationId);
		Log.d("Oishii", "Caching API->" + cacheName);
		for (int i = 0; i < file.length; i++) {
			if (cacheName.equals(file[i]))
				return true;
		}
		return false;
	}

	private InputStream getCachedAPI(int operationId, Context ctx) {
		String cacheName = ApplicationConstants.operationMap.get(operationId);
		try {
			return ctx.openFileInput(cacheName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private InputStream cacheAPI(InputStream is, Context context,
			int operationId) {
		String apiName = ApplicationConstants.operationMap.get(operationId);
		try {
			FileOutputStream fos = context.openFileOutput(apiName,
					Context.MODE_PRIVATE);
			IOUtils.copy(is, fos);
			return context.openFileInput(apiName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return is;
		} catch (IOException e) {
			e.printStackTrace();
			return is;
		}

	}
}
