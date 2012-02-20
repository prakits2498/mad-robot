/*******************************************************************************
 * Copyright (c) 2011 MadRobot.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *  Elton Kent - initial API and implementation
 ******************************************************************************/
package com.madrobot.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

public class UIUtils {
	public abstract static class ClickSpan extends ClickableSpan {

	}

	/**
	 * Set part of the text in a textview clickable.
	 * <p>
	 * The entire text needs to set to the textview before calling this method
	 * </p>
	 * 
	 * @param view
	 *            Textview to assign the clickage text to.
	 * @param clickableText
	 *            Text in the textview that needs to be made clickable.
	 * @param span
	 */
	public static void clickify(TextView view, final String clickableText, final ClickSpan span) {

		CharSequence text = view.getText();
		String string = text.toString();

		int start = string.indexOf(clickableText);
		int end = start + clickableText.length();
		if (start == -1)
			return;

		if (text instanceof Spannable) {
			((Spannable) text).setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} else {
			SpannableString s = SpannableString.valueOf(text);
			s.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			view.setText(s);
		}

		MovementMethod m = view.getMovementMethod();
		if ((m == null) || !(m instanceof LinkMovementMethod)) {
			view.setMovementMethod(LinkMovementMethod.getInstance());
		}
	}

	/**
	 * Returns the scaled size for different resolutions.
	 * 
	 * @param fntSize
	 * @return
	 */
	public static int getDensityIndependentSize(int size, Context ctx) {
		int density = ctx.getResources().getDisplayMetrics().densityDpi;
		if (160 == density) {
			int newSize = (int) (size / (1.5));
			return newSize;
		} else if (density == 120) {
			int newSize = (size / (2));
			return newSize;
		}

		return size;
	}

	public static int getScaledPixels(Context ctx, int unscaled) {
		return (int) (unscaled * ctx.getResources().getDisplayMetrics().density + 0.5f);
	}

	/**
	 * Display a system Toast using a custom ui view.
	 * 
	 * @param viewResId
	 *            the view res id
	 * @param duration
	 *            the duration
	 * @param gravity
	 *            the gravity
	 */
	public static void showCustomToast(Context mContext, int viewResId, int duration, int gravity) {
		View layout = getLayoutInflater(mContext).inflate(viewResId, null);

		Toast toast = new Toast(mContext.getApplicationContext());

		toast.setGravity(gravity, 0, 0);
		toast.setDuration(duration);
		toast.setView(layout);
		toast.show();
	}

	public static LayoutInflater getLayoutInflater(Context mContext) {
		return (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	/**
	 * Gets the optimal number of columns that can be used for the given width
	 * 
	 * @param drawable_width
	 *            the drawable_width
	 * @return the screen optimal columns
	 */
	public static int getScreenOptimalColumns(Context mContext, int drawable_width) {
		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		double a = (double) metrics.widthPixels / (double) metrics.densityDpi; // 2.25
		int b = (int) Math.ceil(a * 2.0); // 5

		if ((b * drawable_width) > metrics.widthPixels) {
			return metrics.widthPixels / drawable_width;
		}

		return Math.min(Math.max(b, 3), 10);
	}

	/**
	 * Get the Centre of the View
	 * 
	 * @return The centre of the given view.
	 */
	public static int getCenterOfView(View view) {
		return view.getLeft() + view.getWidth() / 2;
	}

	public static void makeGlow(TextView txtView, int glowColor) {
		txtView.setShadowLayer(2, 0, 0, glowColor);
	}

	/**
	 * Sets bitmap along with a text in a TextView
	 * <p>
	 * <img src="../../../../resources/bitmapinlined.png"><br/>
	 * </p>
	 * @param tv text view to set the text along with the image
	 * @param string string to set to the textview
	 * @param bitmap bitmap to insert in the string
	 * @param insertionIndex index in the string at which to insert the <code>bitmap</code>
	 */
	public static void insertBitmapIntoTextView(TextView tv, String string, Bitmap bitmap, int insertionIndex) {
		StringBuilder appender=new StringBuilder(string);
		appender.insert(insertionIndex, ' ');
		SpannableStringBuilder builder=new SpannableStringBuilder(appender.toString());
		ImageSpan imageSpan=new ImageSpan(bitmap);
		builder.setSpan(imageSpan, insertionIndex,insertionIndex+1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		tv.setText( builder, BufferType.SPANNABLE );
	}
}
