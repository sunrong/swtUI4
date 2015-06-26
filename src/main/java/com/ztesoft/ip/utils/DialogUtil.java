package com.ztesoft.ip.utils;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class DialogUtil {

	/**
	 * @param args
	 */
	public static void openInfo(Shell shell, String s) {
		// TODO Auto-generated method stub
		MessageDialog.openInformation(shell, "信息", s);
	}

}
