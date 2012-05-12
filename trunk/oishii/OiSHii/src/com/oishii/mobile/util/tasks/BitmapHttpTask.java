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

import com.oishii.mobile.R;
import com.oishii.mobile.util.HttpTaskHelper;
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
		Bitmap bitmap = null;
		System.out.println("Bitmap URI->"+param.bitmapUri);
		System.out.println("Bitmap URI scheme->"+param.bitmapUri.getScheme());
		if (param.bitmapUri.getScheme()!=null) {
			com.oishii.mobile.util.HttpTaskHelper helper = new com.oishii.mobile.util.HttpTaskHelper(
					param.bitmapUri);
			HttpResponse entity;

			try {
				entity = helper.execute();
				InputStream is = entity.getEntity().getContent();
				bitmap = BitmapFactory.decodeStream(is, null,
						new BitmapFactory.Options());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			// ResponseParam resp = new ResponseParam();
			// resp.bitmap = bitmap;
			// resp.image = params[0].image;
			// resp.bar = params[0].progress;
			// resp.parent = params[0].parent;O

			System.out.println("REtrn resp for->" + params[0].bitmapUri);
		}
		return bitmap;
	}

	protected void onPostExecute(Bitmap bitmap) {
		param.image.setVisibility(View.VISIBLE);
		if (bitmap != null) {
			param.image.setImageBitmap(bitmap);
			if (param.bean != null) {
				param.bean.setBitmap(bitmap);
			}
		} else {
			param.image.setImageResource(R.drawable.error_bitmap);
		}
		if (param.progress != null) {
			param.progress.setVisibility(View.GONE);
			if (param.parent != null) {
				param.parent.removeView(param.progress);
			}
		}
	}
}
