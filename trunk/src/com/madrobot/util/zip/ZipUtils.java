
package com.madrobot.util.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import com.madrobot.io.file.BinaryFileFunctions;

public class ZipUtils extends BinaryFileFunctions {
	public final byte[] inflate(byte bytes[]) throws IOException
	// slow, probably.
	{
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		InflaterInputStream zIn = new InflaterInputStream(in);
		return getStreamBytes(zIn);
	}

	public final byte[] deflate(byte bytes[]) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DeflaterOutputStream dos = new DeflaterOutputStream(baos);
		dos.write(bytes);
		dos.close();
		return baos.toByteArray();
	}

	public byte[] decompressLZW(byte compressed[], int LZWMinimumCodeSize, int expectedSize, int byteOrder)
			throws IOException {
		InputStream is = new ByteArrayInputStream(compressed);

		LZWDecompressor decompressor = new LZWDecompressor(LZWMinimumCodeSize, byteOrder);
		byte[] result = decompressor.decompress(is, expectedSize);

		return result;
	}

	public byte[] compressLZW(byte src[], int LZWMinimumCodeSize, int byteOrder, boolean earlyLimit)
			throws IOException

	{
		LZWCompressor compressor = new LZWCompressor(LZWMinimumCodeSize, byteOrder, earlyLimit);

		byte compressed[] = compressor.compress(src);

		return compressed;
	}

	public byte[] decompressPackBits(byte compressed[], int expectedSize, int byteOrder) throws IOException {
		byte unpacked[] = new PackBits().decompress(compressed, expectedSize);
		return unpacked;
	}

	public byte[] compressPackBits(byte decompressed[]) throws IOException {
		byte unpacked[] = new PackBits().compress(decompressed);
		return unpacked;
	}

}
