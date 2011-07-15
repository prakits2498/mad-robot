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
package com.madrobot.io.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

public class NetUtils {
	/**
	 * Finds a local, non-loopback, IPv4 address
	 * 
	 * @return The first non-loopback IPv4 address found, or <code>null</code>
	 *         if no such addresses found
	 * @throws SocketException
	 *             If there was a problem querying the network
	 *             interfaces
	 */
	public static InetAddress getLocalAddress() throws SocketException {
		Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
		while(ifaces.hasMoreElements()){
			NetworkInterface iface = ifaces.nextElement();
			Enumeration<InetAddress> addresses = iface.getInetAddresses();

			while(addresses.hasMoreElements()){
				InetAddress addr = addresses.nextElement();
				if((addr instanceof Inet4Address) && !addr.isLoopbackAddress()){
					return addr;
				}
			}
		}
		return null;
	}
	
	

	/**
	 * Finds this computer's global IP address
	 * 
	 * @param url
	 *            the url to find the global IP
	 * @return The global IP address, or null if a problem occurred
	 */
	public static Inet4Address getGlobalAddress(String url) {
		try{
			URLConnection uc = new URL(url).openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			return (Inet4Address) InetAddress.getByName(br.readLine());
		} catch(MalformedURLException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return the encoded ip
	 */
	public static int encodeIP(int a, int b, int c, int d) {
		int ip = 0;
		ip |= a << 24;
		ip |= b << 16;
		ip |= c << 8;
		ip |= d;
		return ip;
	}

	/**
	 * Encodes an IP as an int
	 * 
	 * @param ip
	 * @return the encoded IP
	 */
	public static int encode(Inet4Address ip) {
		int i = 0;
		byte[] b = ip.getAddress();
		i |= b[0] << 24;
		i |= b[1] << 16;
		i |= b[2] << 8;
		i |= b[3] << 0;

		return i;
	}

}
