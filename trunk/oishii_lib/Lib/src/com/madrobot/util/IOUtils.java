package com.madrobot.util;

import java.io.ByteArrayOutputStream;
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

	/**
	 * This convenience method allows to read a InputStream into a string. The
	 * platform's default character encoding is used for converting bytes into
	 * characters.
	 * 
	 * @param pStream
	 *            The input stream to read.
	 * @see #asString(InputStream, String)
	 * @return The streams contents, as a string.
	 * @throws IOException
	 *             An I/O error occurred.
	 */
	public static String asString(InputStream pStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		copy(pStream, baos, true);
		return baos.toString();
	}

	/**
	 * Copies the contents of the given {@link InputStream} to the given
	 * {@link OutputStream}. Shortcut for
	 * 
	 * <pre>
	 * copy(pInputStream, pOutputStream, new byte[8192]);
	 * </pre>
	 * 
	 * @param pInputStream
	 *            The input stream, which is being read. It is guaranteed, that
	 *            {@link InputStream#close()} is called on the stream.
	 * @param pOutputStream
	 *            The output stream, to which data should be written. May be
	 *            null, in which case the input streams contents are simply
	 *            discarded.
	 * @param pClose
	 *            True guarantees, that {@link OutputStream#close()} is called
	 *            on the stream. False indicates, that only
	 *            {@link OutputStream#flush()} should be called finally.
	 * 
	 * @return Number of bytes, which have been copied.
	 * @throws IOException
	 *             An I/O error occurred.
	 */
	public static long copy(InputStream pInputStream,
			OutputStream pOutputStream, boolean pClose) throws IOException {
		return copy(pInputStream, pOutputStream, pClose, new byte[1024 * 4]);
	}

	/**
	 * Copies the contents of the given {@link InputStream} to the given
	 * {@link OutputStream}.
	 * 
	 * @param pIn
	 *            The input stream, which is being read. It is guaranteed, that
	 *            {@link InputStream#close()} is called on the stream.
	 * @param pOut
	 *            The output stream, to which data should be written. May be
	 *            null, in which case the input streams contents are simply
	 *            discarded.
	 * @param pClose
	 *            True guarantees, that {@link OutputStream#close()} is called
	 *            on the stream. False indicates, that only
	 *            {@link OutputStream#flush()} should be called finally.
	 * @param pBuffer
	 *            Temporary buffer, which is to be used for copying data.
	 * @return Number of bytes, which have been copied.
	 * @throws IOException
	 *             An I/O error occurred.
	 */
	public static long copy(InputStream pIn, OutputStream pOut, boolean pClose,
			byte[] pBuffer) throws IOException {
		OutputStream out = pOut;
		InputStream in = pIn;
		try {
			long total = 0;
			for (;;) {
				int res = in.read(pBuffer);
				if (res == -1) {
					break;
				}
				if (res > 0) {
					total += res;
					if (out != null) {
						out.write(pBuffer, 0, res);
					}
				}
			}
			if (out != null) {
				if (pClose) {
					out.close();
				} else {
					out.flush();
				}
				out = null;
			}
			in.close();
			in = null;
			return total;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Throwable t) {
					/* Ignore me */
				}
			}
			if (pClose && out != null) {
				try {
					out.close();
				} catch (Throwable t) {
					/* Ignore me */
				}
			}
		}
	}

}
