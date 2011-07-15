
package com.madrobot.io.file;

import java.io.File;

public interface FileListener {
	/**
	 * 
	 * 
	 * @param status
	 *            The integer describing the event. As defined in {@code
	 *            android.os.FileObserver}
	 * @param file
	 *            The file which the event has occured
	 */
	public void onFileStatusChanged(int status, File file);
}
