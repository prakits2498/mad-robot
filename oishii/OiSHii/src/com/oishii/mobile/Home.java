package com.oishii.mobile;

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
					setBannerImage(images[imageIndex]);
					if (imageIndex < (images.length - 1))
						imageIndex++;
					else if (imageIndex == (images.length - 1)) {
						imageIndex = 0;
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

}
