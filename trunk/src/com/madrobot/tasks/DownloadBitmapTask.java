
package com.madrobot.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

import com.madrobot.io.net.HttpTaskHelper;

/**
 * Async task to download a list of bitmaps
 *<p>
 * This task takes a list of java.net.URI instances that point to bitmaps. The
 * bitmaps are downloaded(using Http GET) sequentially and sent to the UI using
 * the {@link TaskNotifier#onSuccess(DataResponse)} method .<br/>
 * <b>Usage</b>
 * 
 * <pre>
 * URI[] urls = new URI[] { URI.create(&quot;http://www.foo.com/image1.png&quot;),
 * 		URI.create(&quot;http://www.foo.com/image2.png&quot;) };
 * 
 * BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
 * 
 * Object[] param = new Object[2];
 * param[0] = urls;
 * param[1] = bitmapOptions;
 * new DownloadBitmapTask(this, this).execute(param);
 * </pre>
 * 
 * </p>
 * 
 */
public class DownloadBitmapTask extends AbstractTask {

	public DownloadBitmapTask(Context context, TaskNotifier notifier) {
		super(context, notifier);
	}

	protected Object doInBackground(Object... params) {
		publishProgress(null);
		URI[] uri = (URI[]) params[0];
		BitmapFactory.Options opt = (Options) params[1];
		for(int i = 0; i < uri.length; i++){
			DataResponse response = new DataResponse();
			response.setResponseId(i);
			HttpTaskHelper helper = new HttpTaskHelper(uri[i]);
			try{
				HttpEntity entity = helper.execute();
				InputStream is = entity.getContent();
				Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
				response.setData(bitmap);
				response.setResponseStatus(1);
				publishProgress(response);
			} catch(IOException e){
				response.setResponseStatus(-1);
				response.setT(e);
				e.printStackTrace();
			} catch(URISyntaxException e){
				response.setResponseStatus(-1);
				response.setT(e);
				e.printStackTrace();
			}

		}
		return null;

	}

	protected void onProgressUpdate(Object... objects) {
		/*
		 * if null is received then it indicates that the task has just started
		 */
		if(objects == null){
			notifier.onTaskStarted();
		} else{
			DataResponse response = (DataResponse) objects[0];
			if(response.getResponseStatus() > 0){
				notifier.onSuccess(response);
			} else{
				notifier.onError(response.getT());
			}
		}
	}

	protected void onPostExecute(Object result) {
		notifier.onTaskCompleted();
	}

}
