
package com.madrobot.taskpool;

/**
 * Task Pool Settings
 * <p>
 * If these settings are to be modified, It should be done before the very first
 * call to <code>TaskPoolManagerImpl.getTaskPool()</code>
 * 
 * @see TaskPoolManagerImpl
 *      </p>
 */
public class TaskPoolConstants {

	/**
	 * Total count of Task pool thread(s). Should be >1
	 */
	public static int MAX_THREADS_COUNT = 2;

	/**
	 * Initial queue size threshold limit. Should be >1
	 */
	public static int QUEUE_SIZE = 250;// 200;

}
