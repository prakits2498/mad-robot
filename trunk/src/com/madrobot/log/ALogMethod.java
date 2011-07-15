
package com.madrobot.log;

abstract class ALogMethod {

	abstract void d(String tag, String message);

	abstract void e(String tag, String message);

	abstract void i(String tag, String message);

	abstract void shutdown();

	abstract void v(String tag, String message);

	abstract void w(String tag, String message);

	abstract void write(int level, String tag, String message);

}
