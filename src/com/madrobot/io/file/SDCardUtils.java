/*******************************************************************************
 * Copyright (c) 2011 MadRobot.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *  Elton Kent - initial API and implementation
 ******************************************************************************/
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
