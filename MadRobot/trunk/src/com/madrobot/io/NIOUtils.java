package com.madrobot.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;

public class NIOUtils {
	public static final int KB = 1024;
	public static final int MB = KB * KB;
	public static final int GB = KB * MB;
	public static final long TB = KB * GB;

	/**
	 * Slice the given byte buffer at the given postion to the given size
	 * 
	 * @param buf
	 * @param pos
	 * @param size
	 * @return
	 */
	public static ByteBuffer slice(ByteBuffer buf, int pos, int size) {
		int origPos = buf.position();
		int origLim = buf.limit();
		buf.clear();
		buf.position(pos);
		buf.limit(pos + size);
		ByteBuffer res = buf.slice();
		res.order(ByteOrder.nativeOrder());
		buf.clear();
		buf.position(origPos);
		buf.limit(origLim);
		return res;
	}

	public static void copyFile(final File in, final File out) throws IOException {
		RandomAccessFile f1 = new RandomAccessFile(in, "r");
		RandomAccessFile f2 = new RandomAccessFile(out, "rw");
		try {
			FileChannel c1 = f1.getChannel();
			FileChannel c2 = f2.getChannel();
			try {
				c1.transferTo(0, f1.length(), c2);
				c1.close();
				c2.close();
			} catch (IOException ex) {
				IOUtils.closeSilently(c1);
				IOUtils.closeSilently(c2);
				// Propagate the original exception
				throw ex;
			}
			f1.close();
			f2.close();
		} catch (IOException ex) {
			IOUtils.closeSilently(f1);
			IOUtils.closeSilently(f2);
			// Propagate the original exception
			throw ex;
		}
	}

	public static FloatBuffer toFloatBuffer(float[] v) {
		ByteBuffer buf = ByteBuffer.allocateDirect(v.length * 4);
		buf.order(ByteOrder.nativeOrder());
		FloatBuffer buffer = buf.asFloatBuffer();
		buffer.put(v);
		buffer.position(0);
		return buffer;
	}

	public static ShortBuffer toShortBuffer(short[] v) {
		ByteBuffer buf = ByteBuffer.allocateDirect(v.length * 2);
		buf.order(ByteOrder.nativeOrder());
		ShortBuffer buffer = buf.asShortBuffer();
		buffer.put(v);
		buffer.position(0);
		return buffer;
	}

}
