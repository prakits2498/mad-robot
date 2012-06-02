		package com.oishii.mobile;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;

public class MenuItemGallery extends OishiiBaseActivity {

	@Override
	protected int getParentScreenId() {
		return R.id.about;
	}

	@Override
	protected void hookInChildViews() {
		Gallery menuGallery = (Gallery) findViewById(R.id.gallery1);
		menuGallery.setAdapter(new ImageAdapter(getApplicationContext()));
	}

	@Override
	protected int getSreenID() {
		return R.layout.menugallery;
	}

	@Override
	protected int getChildViewLayout() {
		return R.layout.menugallery;
	}

	@Override
	protected String getTitleString() {
		return null;
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
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			// imageView.setBackgroundResource(mGalleryItemBackground);

			return imageView;
		}
	}

}