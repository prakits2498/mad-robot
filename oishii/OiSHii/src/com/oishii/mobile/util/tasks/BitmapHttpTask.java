package com.oishii.mobile.util.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.oishii.mobile.util.tasks.BitmapHttpTask.ResponseParam;

public class BitmapHttpTask extends AsyncTask<BitmapRequestParam, Void, Bitmap> {

	class ResponseParam {
		Bitmap bitmap;
		ImageView image;
		ProgressBar bar;
		ViewGroup parent;
	}

	private BitmapRequestParam param;

	@Override
	protected Bitmap doInBackground(BitmapRequestParam... params) {
		param = params[0];

		com.oishii.mobile.util.HttpTaskHelper helper = new com.oishii.mobile.util.HttpTaskHelper(
				param.bitmapUri);
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
		// ResponseParam resp = new ResponseParam();
		// resp.bitmap = bitmap;
		// resp.image = params[0].image;
		// resp.bar = params[0].progress;
		// resp.parent = params[0].parent;

		System.out.println("REtrn resp for->" + params[0].bitmapUri);
		return bitmap;
	}

	protected void onPostExecute(Bitmap bitmap) {
		if (bitmap != null && param.image != null) {
			if (param.progress != null) {
				param.progress.setVisibility(View.GONE);
				if (param.parent != null) {
					System.out.println("removing progress");
					param.parent.removeView(param.progress);
				}
			}
			param.image.setImageBitmap(bitmap);
			param.image.setVisibility(View.VISIBLE);
			if (param.bean != null) {
				param.bean.setBitmap(bitmap);
				System.out.println("Setting bitmap bean");
			}
		}
	}
}
