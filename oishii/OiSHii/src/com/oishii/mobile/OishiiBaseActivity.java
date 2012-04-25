package com.oishii.mobile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oishii.mobile.util.HttpTaskHelper;

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

	protected void showOnlyLogo() {
		findViewById(R.id.logo).setVisibility(View.VISIBLE);
		findViewById(R.id.headertitle).setVisibility(View.GONE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.defaultmenu, menu);
		
		return true;
	}
	
	protected void hideDialog(){
		
	}
	protected void showDialog(){
		
	}
	
	protected abstract void populateViewFromHttp(InputStream is,View v);
	
	protected class OishiiHttpTask extends AsyncTask<HttpUIWrapper, View, Object>{
		protected void onPreExecute (){
			showDialog();
		}
		@Override
		protected Object doInBackground(HttpUIWrapper... wrapper) {
			HttpTaskHelper helper = new com.oishii.mobile.util.HttpTaskHelper(wrapper[0].uri);
			try {
				HttpEntity entity = helper.execute();
				populateViewFromHttp(entity.getContent(),wrapper[0].view);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			
			
			return null;
		}
		
		protected void onPostExecute(Object obj){
			hideDialog();
		}
	}
	
	protected class HttpUIWrapper{
		protected View view;
		protected URI uri;
	}
}
