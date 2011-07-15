package com.madrobot.math;

public interface BinaryConstants
{
	public static final int BYTE_ORDER_INTEL = 'I';
	public static final int BYTE_ORDER_LEAST_SIGNIFICANT_BYTE = BYTE_ORDER_INTEL;
	public static final int BYTE_ORDER_LSB = BYTE_ORDER_INTEL;
	public static final int BYTE_ORDER_LITTLE_ENDIAN = BYTE_ORDER_INTEL;

	public static final int BYTE_ORDER_MOTOROLA = 'M';
	public static final int BYTE_ORDER_MOST_SIGNIFICANT_BYTE = BYTE_ORDER_MOTOROLA;
	public static final int BYTE_ORDER_MSB = BYTE_ORDER_MOTOROLA;
	public static final int BYTE_ORDER_NETWORK = BYTE_ORDER_MOTOROLA;
	public static final int BYTE_ORDER_BIG_ENDIAN = BYTE_ORDER_MOTOROLA;

}