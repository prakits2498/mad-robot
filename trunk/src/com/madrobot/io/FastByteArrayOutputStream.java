
package com.madrobot.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * ByteArrayOutputStream thats NOT thread safe.
 */
public class FastByteArrayOutputStream extends OutputStream
{
	private final byte bytes[];

	public FastByteArrayOutputStream(int length) {
		bytes = new byte[length];
	}

	private int count = 0;

	@Override
	public void write(int value) throws IOException {
		if(count >= bytes.length){
			throw new IOException("Write exceeded expected length (" + count + ", " + bytes.length + ")");
		}

		bytes[count] = (byte) value;
		count++;
	}

	public byte[] toByteArray() {
		if(count < bytes.length){
			byte result[] = new byte[count];
			System.arraycopy(bytes, 0, result, 0, count);
			return result;
		}
		return bytes;
	}

	public int getBytesWritten() {
		return count;
	}
}
