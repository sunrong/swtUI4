package com.ztesoft.ip.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class BatUtils {

	/**
	 * @param args
	 */
	public static void runHandSet(String namestr, String ipstr, String ymstr, String wgstr, String dns1str, String dns2str) {
		// TODO Auto-generated method stub
		String c1 = " netsh interface ip set address name=\"" + namestr + "\" static " + ipstr + " " + ymstr + " " + wgstr + " >nul ";
		String c2 = " netsh interface ip add dns \"" + namestr + "\" " + dns1str + " index=1 >nul ";
		String c3 = " netsh interface ip add dns \"" + namestr + "\" " + dns2str + " index=2 >nul ";
		try {
			File f1 = new File("resource/config/updateIp.bat");
			if (f1.exists()) {
				f1.delete();
			}
			f1.createNewFile();

			FileOutputStream fos = new FileOutputStream(f1);
			String str = System.getProperty("line.separator");
			str = c1 + str + c2 + str + c3 + str;
			OutputStreamWriter osw = new OutputStreamWriter(fos, "GB2312");
			osw.write(str);
			osw.flush();
			osw.close();
			String cmd1 = f1.getAbsolutePath();// pass runbat(cmd1);
			System.out.println(cmd1);
			runbat(cmd1);
			PropertiesUtil.setProperty("ip", ipstr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void runbat(String batName) {
		try {
			Runtime.getRuntime().exec(batName);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void runAuto(String namestr) {
		// TODO Auto-generated method stub
		String c1 = " netsh interface ip set address name=\"" + namestr + "\"  source=dhcp >nul ";
		String c2 = " netsh interface ip set dns \"" + namestr + "\"   source=dhcp >nul ";
		try {
			File f1 = new File("resource/config/updateIp.bat");
			if (f1.exists()) {
				f1.delete();
			}
			f1.createNewFile();

			FileOutputStream fos = new FileOutputStream(f1);
			String str = System.getProperty("line.separator");
			str = c1 + str + c2 + str;
			OutputStreamWriter osw = new OutputStreamWriter(fos, "GB2312");
			osw.write(str);
			osw.flush();
			osw.close();
			String cmd1 = f1.getAbsolutePath();// pass runbat(cmd1);
			System.out.println(cmd1);
			runbat(cmd1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
