package com.ztesoft.ip.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @类名:IpAddress
 * @描述:用正则表达式判断是否为IP
 * @Author：Administrator
 * @date: 2014年3月4日 下午10:55:06
 */
public class IpAddress {
	public static boolean isIP(String addr) {
		if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
			return false;
		}
		/**
		 * 判断IP格式和范围
		 */
		String rexp = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";

		Pattern pat = Pattern.compile(rexp);

		Matcher mat = pat.matcher(addr);

		boolean ipAddress = mat.find();

		return ipAddress;
	}

	/**
	 * @Title : main
	 * @Type : IpAddress
	 * @date : 2014年3月4日 下午10:55:06
	 * @Description : IP可能的范围是0-255.0-255.0-255.0-255
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * 符合IP地址的范围
		 */
		String oneAddress = "10.127.30.45";
		/**
		 * 符合IP地址的长度范围但是不符合格式
		 */
		String twoAddress = "127.30.45";
		/**
		 * 不符合IP地址的长度范围
		 */
		String threeAddress = "7.0.4";
		/**
		 * 不符合IP地址的长度范围但是不符合IP取值范围
		 */
		String fourAddress = "255.255.255.2567";

		// 判断oneAddress是否是IP
		System.out.println(isIP(oneAddress));

		// 判断twoAddress是否是IP
		System.out.println(isIP(twoAddress));

		// 判断threeAddress是否是IP
		System.out.println(isIP(threeAddress));

		// 判断fourAddress是否是IP
		System.out.println(isIP(fourAddress));
	}

}