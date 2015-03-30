/**
 * MacAddressUtil.java
 * com.fhd.fdc.utils
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-8-5 		吴德福
 *
 * Copyright (c) 2011, Firsthuida All Rights Reserved.
*/

package com.fhd.fdc.utils;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 读取本地mac地址.
 * @author   吴德福
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-8-5		上午11:32:32
 * @see 	 
 */
public class MacAddressUtil {
	
	public static String hexByte(byte b) {
		String s = "000000" + Integer.toHexString(b);
		return s.substring(s.length() - 2);
	}

	@SuppressWarnings("unused")
	public static String getMAC() {
		Enumeration<NetworkInterface> el;
		String mac_s = "";
		try {
			el = NetworkInterface.getNetworkInterfaces();
			while (el.hasMoreElements()) {
				byte[] mac = null;
				if (mac == null)
					continue;
				mac_s = hexByte(mac[0]) + "-" + hexByte(mac[1]) + "-"
					+ hexByte(mac[2]) + "-" + hexByte(mac[3]) + "-"
					+ hexByte(mac[4]) + "-" + hexByte(mac[5]);
					System.out.println("MAC地址="+mac_s);
			}

		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		return mac_s;
	}
	
	public static void main(String[] args) {
		MacAddressUtil.getMAC();
	}
}

