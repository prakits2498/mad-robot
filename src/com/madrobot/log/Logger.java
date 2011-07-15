
package com.madrobot.log;

/**
 * Logging framework entry point.
 * <p>
 * The logging strategy and logging calls are done here
 * </p>
 * 
 */
public class Logger {
	private static ALogMethod logger = new LoggerStrategyDDMSLog();

	/**
	 *Logs a debug message
	 * 
	 * @param tag
	 *            Log tag
	 * @param message
	 *            Log message
	 */
	public static void d(String tag, String message) {
		logger.d(tag, message);
	}

	/**
	 * Logs a error message
	 * 
	 * @param tag
	 *            Log tag
	 * @param message
	 *            Log message
	 */
	public static void e(String tag, String message) {
		logger.e(tag, message);
	}

	/**
	 * Logs a information message
	 * 
	 * @param tag
	 *            Log tag
	 * @param message
	 *            Log message
	 */
	public static void i(String tag, String message) {
		logger.i(tag, message);
	}

	/**
	 * Shuts down the logging framework.
	 * <p>
	 * This is primarily done before the application exits. Any logging calls
	 * performed after this method is called would be ignored.
	 * </p>
	 * 
	 */
	public static void shutdown() {
		logger.shutdown();
	}

	public static void v(String tag, String message) {
		logger.v(tag, message);
	}

	public static void w(String tag, String message) {
		logger.w(tag, message);
	}

	public static void write(int level, String tag, String message) {
		logger.write(level, tag, message);
	}

	private Logger() {
	}

	/**
	 *Any class that implements <code>ILoggerStrategy</code> can be used
	 * 
	 *@see LoggerStrategy
	 */
	public static final void setLoggerStragtegy(LoggerStrategy loggerStrategy) {
		logger = (ALogMethod) loggerStrategy;
	}

}
