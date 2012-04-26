package com.oishii.mobile.util.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.oishii.mobile.util.tasks.BitmapHttpTask.ResponseParam;

public class BitmapHttpTask extends
		AsyncTask<BitmapRequestParam, Void, ResponseParam> {



	class ResponseParam {
		Bitmap bitmap;
		ImageView image;
	}

	@Override
	protected ResponseParam doInBackground(BitmapRequestParam... params) {
		com.oishii.mobile.util.HttpTaskHelper helper = new com.oishii.mobile.util.HttpTaskHelper(
				params[0].bitmapUri);
		HttpResponse entity;
		Bitmap bitmap = null;
		try {
			entity = helper.execute();
			InputStream is = entity.getEntity().getContent();
			bitmap = BitmapFactory.decodeStream(is, null,
					new BitmapFactory.Options());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseParam resp = new ResponseParam();
		resp.bitmap = bitmap;
		resp.image = params[0].image;
		return resp;
	}

	protected void onPostExecute(ResponseParam result) {
		if (result.bitmap != null && result.image != null) {
			result.image.setImageBitmap(result.bitmap);
		}
	}
}
