
package com.madrobot.log;

import android.util.Log;

public class LoggerStrategyDDMSLog extends ALogMethod implements LoggerStrategy {

	void d(String tag, String message) {
		Log.d(tag, message);
	}

	void e(String tag, String message) {
		Log.e(tag, message);
	}

	void i(String tag, String message) {
		Log.i(tag, message);
	}

	void shutdown() {
		// TODO Auto-generated method stub

	}

	void v(String tag, String message) {
		Log.v(tag, message);
	}

	void w(String tag, String message) {
		Log.w(tag, message);
	}

	void write(int level, String tag, String message) {
		Log.println(level, tag, message);
	}

}
