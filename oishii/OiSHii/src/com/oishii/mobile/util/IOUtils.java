package com.oishii.mobile.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils {
	
	public static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	/**
	 * Check if the device has an SDcard.
	 * 
	 * @return
	 */
	public static boolean isSDCardMounted() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 
	 * Copy bytes from an InputStream to an OutputStream.
	 * 
	 * This method buffers the input internally, so there is no need to use a
	 * BufferedInputStream.
	 * 
	 * Large streams (over 2GB) will return a bytes copied value of -1 after the
	 * copy has completed since the correct number of bytes cannot be returned
	 * as an int. For large streams use the copyLarge(InputStream, OutputStream)
	 * method.
	 * 
	 * @param input
	 *            the InputStream to read from
	 * @param output
	 *            the OutputStream to write to
	 * @return the number of bytes copied
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static int copy(InputStream input, OutputStream output)
			throws IOException {
		long count = copyLarge(input, output);
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) count;
	}

	/**
	 * Copy bytes from a large (over 2GB) InputStream to an OutputStream.
	 * 
	 * This method buffers the input internally, so there is no need to use a
	 * BufferedInputStream.
	 * 
	 * @param input
	 *            the InputStream to read from
	 * @param output
	 *            the OutputStream to write to
	 * @return the number of bytes copied
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	private static long copyLarge(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		output.flush();
		output.close();
		input.close();
		return count;
	}

}
