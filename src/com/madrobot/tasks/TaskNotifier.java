
package com.madrobot.tasks;

/**
 * Task Progress callback
 * <p>
 * </p>
 * 
 */
public interface TaskNotifier {
	/**
	 * Called when the task completed successfully
	 * 
	 * @param response
	 */
	public void onSuccess(DataResponse response);

	/**
	 * Called if there is an error in
	 * 
	 * @param response
	 */
	public void onError(Throwable t);

	/**
	 * Called when there is a change in the current carrier . WiFi/3G
	 * 
	 * @param bearerStatus
	 */
	public void onCarrierChanged(int bearerStatus);

	/**
	 * Called when the task is started
	 */
	public void onTaskStarted();

	/**
	 * Called when the task is completed
	 */
	public void onTaskCompleted();

}
