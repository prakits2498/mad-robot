
package com.madrobot.io.file;

import java.io.File;

import android.os.StatFs;

public class SDCardUtils {
	/**
	 * Check if the device has an SDcard.
	 * 
	 * @return
	 */
	public static boolean isSDCardMounted() {
		return android.os.Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * Get the SDcard directory
	 * 
	 * @return
	 */
	public static File getSDcardDirectory() {
		return android.os.Environment.getExternalStorageDirectory();
	}

	/**
	 * Get the free space on the SDcard
	 * 
	 * @return
	 */
	public static long getFreeSpaceOnSDCard() {
		StatFs cardStatistics = new StatFs(getSDcardDirectory().toString());
		long freeSpace = (long) cardStatistics.getBlockSize() * cardStatistics.getFreeBlocks();
		return freeSpace;
	}

}
