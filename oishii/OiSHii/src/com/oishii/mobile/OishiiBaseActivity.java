package com.oishii.mobile;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class OishiiBaseActivity extends Activity {

	@Override
	public final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hookInViews();

	}

	protected void hookInViews() {
		ViewGroup parent = (ViewGroup) View.inflate(this, R.layout.oishiibase,
				null);

		RelativeLayout contentArea = (RelativeLayout) parent
				.findViewById(R.id.contentArea);
		getLayoutInflater().inflate(getChildViewLayout(), contentArea);
		TextView title = (TextView) parent.findViewById(R.id.headertitle);
		title.setText(getTitleString());
		setContentView(parent);

		hookInChildViews();
	}

	/**
	 * This method should be used to hook in the listeners
	 */
	protected abstract void hookInChildViews();

	/**
	 * Get the layout contents of the child view
	 * 
	 * @return
	 */
	protected abstract int getChildViewLayout();

	protected abstract String getTitleString();
}
