
package com.madrobot.taskpool;

public interface TaskPoolManager {

	public void cancelAllTasks();

	public void shutdown();

	public <T> void submit(Task<T> task);

}
