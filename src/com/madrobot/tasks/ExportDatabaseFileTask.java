package com.madrobot.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import com.madrobot.io.file.SDCardUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

/**
 * Task to back the application's database file to the sdcard
 * @author elton.kent
 *
 */
public class ExportDatabaseFileTask extends AsyncTask<String, Void, Boolean> {
	private static final String TAG = ExportDatabaseFileTask.class.getSimpleName();

	private final ProgressDialog dialog;
	private String dbPackage;
	private String dbName;
	private String dbPath;

	public ExportDatabaseFileTask(Context context, String dbPackage, String dbName) {
		dialog = new ProgressDialog(context);
		this.dbPackage = dbPackage;
		this.dbName = dbName;
		dbPath = Environment.getDataDirectory() + "/data/" + dbPackage + "/databases/";
	}

	// can use UI thread here
	@Override
	protected void onPreExecute() {
		this.dialog.setMessage("Exporting database...");
		this.dialog.show();
	}

	// automatically done on worker thread (separate from UI thread)
	@Override
	protected Boolean doInBackground(final String... args) {

		File dbFile = new File(dbPath + dbName);

		// path on sd by convention
		File exportDir = new File(Environment.getExternalStorageDirectory(), "/Android/data/" + dbPackage
				+ "/exported_db/");
		if (!exportDir.exists()) {
			// boolean result =
			exportDir.mkdirs();
			// Log.i(TAG, "create directory " + (result ? "succesful" :
			// "failed"));
		}

		File file = new File(exportDir, dbName);

		try {
			file.createNewFile();
			this.copyFile(dbFile, file);
			return true;
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			return false;
		}
	}

	// can use UI thread here
	@Override
	protected void onPostExecute(final Boolean success) {
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}
		if (success) {
			dialog.setMessage("Export successful!");
		} else {
			dialog.setMessage("Export failed");
		}
	}

	void copyFile(File src, File dst) throws IOException {
		FileChannel inChannel = new FileInputStream(src).getChannel();
		FileChannel outChannel = new FileOutputStream(dst).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} finally {
			if (inChannel != null)
				inChannel.close();
			if (outChannel != null)
				outChannel.close();
		}
	}
}