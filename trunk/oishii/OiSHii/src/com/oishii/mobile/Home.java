package com.oishii.mobile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.oishii.mobile.util.tasks.HttpRequestTask;
import com.oishii.mobile.util.tasks.HttpRequestWrapper;
import com.oishii.mobile.util.tasks.IHttpCallback;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

public class Home extends OishiiBaseActivity {
	private class ImageRunner extends Thread {
		private boolean canRun = true;
		private int imageIndex = 1;
		private int[] images = new int[] { R.drawable.home_bg,
				R.drawable.home_bg2, R.drawable.home_bg3, R.drawable.home_bg4 };

		public void run() {
			System.out.println("Image roll started");
			while (canRun) {
				try {
					Thread.sleep(8000);
					if (canRun) {
						setBannerImage(images[imageIndex]);
						if (imageIndex < (images.length - 1))
							imageIndex++;
						else if (imageIndex == (images.length - 1)) {
							imageIndex = 0;
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Image roll stopped");
		}
	}

	private void stopRoll() {
		if (runner != null) {
			runner.canRun = false;
			runner = null;
		}
	}

	private ImageView iv;

	private ImageRunner runner;

	@Override
	protected int getChildViewLayout() {
		return R.layout.home;
	}

	@Override
	protected int getParentScreenId() {
		return R.id.about;
	}

	@Override
	protected int getSreenID() {
		return R.id.about;
	}

	@Override
	protected String getTitleString() {
		return "";
	}

	@Override
	protected boolean hasTitleBar() {
		return false;
	}

	@Override
	protected void hookInChildViews() {
		iv = (ImageView) findViewById(R.id.homeBanner);
		findViewById(R.id.todaysMenu).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						stopRoll();
						Intent todays = new Intent(Home.this, TodaysMenu.class);
						startActivity(todays);
					}
				});
		runner = new ImageRunner();
		runner.start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (runner == null) {
			runner = new ImageRunner();
			runner.start();
		}
	}

	@Override
	protected void doBeforeMenuAction() {
		stopRoll();
	}

	private void setBannerImage(final int imgRes) {
		iv.post(new Runnable() {
			@Override
			public void run() {
				iv.setImageResource(imgRes);
			}

		});
	}
	
	private IHttpCallback sideCallback=new IHttpCallback() {
		
		@Override
		public Object populateBean(InputStream is, int operationId) {
			return null;
		}
		
		@Override
		public void onFailure(int message, int operationID) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void bindUI(Object t, int operationId) {
			// TODO Auto-generated method stub
			
		}
	};
	
	
	
	private void executeSideOrderMenuRequest(int category,int operationID){
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper(
				getApplicationContext());
		requestWrapper.requestURI = ApplicationConstants.API_MENU_DETAILS;
		requestWrapper.callback = sideCallback;
		requestWrapper.operationID = operationID;
		requestWrapper.httpSettings
				.setHttpMethod(ApplicationConstants.HTTP_METHOD);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		NameValuePair param = new BasicNameValuePair("catID",
				String.valueOf(category));
		params.add(param);
		requestWrapper.httpParams = params;
		requestWrapper.canCache=false;
		new HttpRequestTask().execute(requestWrapper);
	}
}
