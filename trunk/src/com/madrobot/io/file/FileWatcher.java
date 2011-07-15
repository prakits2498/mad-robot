
package com.madrobot.io.file;

import java.io.File;

import android.os.FileObserver;

/**
 * 
 * Watches a file/directory with the path specified in the constructor
 * 
 * @author Elton Kent
 */
public class FileWatcher extends FileObserver {
	private FileListener listener;

	public FileWatcher(String path, int mask) {
		super(path, mask);
	}

	@Override
	public void onEvent(int i, String s) {
		if(listener != null){
			listener.onFileStatusChanged(i, new File(s));
		}
	}

	public void setFileListener(FileListener listener) {
		this.listener = listener;
	}

}
