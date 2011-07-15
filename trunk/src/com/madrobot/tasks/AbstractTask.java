
package com.madrobot.tasks;

import android.content.Context;
import android.os.AsyncTask;

abstract class AbstractTask extends AsyncTask<Object, Object, Object> {
	protected TaskNotifier notifier;
	protected Context appContext;

	AbstractTask(Context context, TaskNotifier notifier) {
		this.notifier = notifier;
		this.appContext = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	public TaskNotifier getNotifier() {
		return notifier;
	}

	public void setNotifier(TaskNotifier notifier) {
		this.notifier = notifier;
	}

	public Context getAppContext() {
		return appContext;
	}

	public void setAppContext(Context appContext) {
		this.appContext = appContext;
	}
}
