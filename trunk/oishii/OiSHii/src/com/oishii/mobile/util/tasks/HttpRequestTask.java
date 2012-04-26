package com.oishii.mobile.util.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;

import android.os.AsyncTask;
import android.util.Log;

import com.oishii.mobile.util.HttpTaskHelper;

public class HttpRequestTask extends
		AsyncTask<HttpRequestWrapper, CommWrapper, HttpResponseWrapper> {

	@Override
	protected HttpResponseWrapper doInBackground(HttpRequestWrapper... params) {
		HttpResponseWrapper response = new HttpResponseWrapper();
		response.callback = params[0].callback;
		HttpTaskHelper helper = new com.oishii.mobile.util.HttpTaskHelper(
				params[0].requestURI);
		helper.setHttpSettings(params[0].httpSettings);
		helper.setRequestParameter(params[0].httpParams);
		try {
			HttpResponse httpEntity = helper.execute();
			int statusCode = httpEntity.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				InputStream is = httpEntity.getEntity().getContent();
				if (is != null) {
					Object obj = params[0].callback.populateBean(is,
							params[0].operationID);
					if (obj != null) {
						System.out.println("Object=>"+obj);
						response.responseBean = obj;
						response.isSuccess=true;
						response.operationId = params[0].operationID;
					} else {// deserialization failed
						Log.e("Oishi", "Could not read server response!"
								+ statusCode);
						response.errorMessage = "Could not read server response!";
						response.isSuccess = false;
					}
				}
			} else {
				Log.e("Oishi", "Invalid response received from server!==>"
						+ statusCode);
				response.errorMessage = "Invalid Server Response";
				response.isSuccess = false;
			}
			// if(httpEntity.)
		} catch (IOException e) {
			response.errorMessage = "Invalid Server Response!";
			response.isSuccess = false;
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return response;
	}

	protected void onPostExecute(HttpResponseWrapper result) {
		Log.e("Oishi", "on Post execute");
		if (result.isSuccess) {
			result.callback.bindUI(result.responseBean, result.operationId);
		}else{
			result.callback.onFailure(result.errorMessage, result.operationId);
		}
	}
}

class CommWrapper {
	IHttpCallback callback;
	Object bean;
	int id;
}