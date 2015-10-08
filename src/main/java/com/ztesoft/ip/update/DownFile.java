package com.ztesoft.ip.update;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import com.ztesoft.ip.ui.MainApp;
import com.ztesoft.ip.utils.DialogUtil;
import com.ztesoft.ip.utils.HttpUtils;
import com.ztesoft.ip.utils.LayoutUtils;
import com.ztesoft.ip.utils.PropertiesUtil;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class DownFile extends Dialog {

	private static String downurl;
	private static String fileName;
	private static String filePath;
	private static String chekupdate;
	private static String version;
	protected Object result;
	protected Shell shellww;
	protected Composite shell;

	private ProgressBar updatepro;
	private Button begin;
	private Button cacel;
	static {
		PropertiesUtil.load("resouce/config", "value.config");

		downurl = PropertiesUtil.getProperty("updateUrl");
		fileName = PropertiesUtil.getProperty("downloadFilename");
		filePath = PropertiesUtil.getProperty("downloadFilefolder");
		chekupdate = PropertiesUtil.getProperty("checkUpdate");
		version = PropertiesUtil.getProperty("version");
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public DownFile(Shell parent, int style) {
		super(parent, style);
		setText("更新程序");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		Display display = getParent().getDisplay();
		shellww = new Shell(getParent(), 0);
		shellww.setSize(450, 316);
		{
			shell = LayoutUtils.centerDWdefult(shellww, "更新程序", true, false);
		}
		shell.setLayout(new FormLayout());

		updatepro = new ProgressBar(shell, 0);
		FormData fd_updatepro = new FormData();
		fd_updatepro.right = new FormAttachment(0, 406);
		fd_updatepro.top = new FormAttachment(0, 131);
		fd_updatepro.left = new FormAttachment(0, 127);
		updatepro.setLayoutData(fd_updatepro);

		updatepro.setMaximum(100);
		updatepro.setMinimum(0);

		Label label = new Label(shell, SWT.NONE);
		FormData fd_label = new FormData();
		fd_label.right = new FormAttachment(0, 121);
		fd_label.top = new FormAttachment(0, 131);
		fd_label.left = new FormAttachment(0, 61);
		label.setLayoutData(fd_label);
		label.setText("下载进度");

		Label label_1 = new Label(shell, SWT.NONE);
		FormData fd_label_1 = new FormData();
		fd_label_1.right = new FormAttachment(0, 337);
		fd_label_1.top = new FormAttachment(0, 46);
		fd_label_1.left = new FormAttachment(0, 146);
		label_1.setLayoutData(fd_label_1);
		label_1.setText("下载完成将自动重启更新!");

		begin = new Button(shell, SWT.NONE);
		FormData fd_begin = new FormData();
		fd_begin.right = new FormAttachment(0, 166);
		fd_begin.top = new FormAttachment(0, 215);
		fd_begin.left = new FormAttachment(0, 98);
		begin.setLayoutData(fd_begin);
		begin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				down();
			}
		});
		begin.setText("开 始");

		cacel = new Button(shell, SWT.NONE);
		FormData fd_cacel = new FormData();
		fd_cacel.right = new FormAttachment(0, 406);
		fd_cacel.top = new FormAttachment(0, 215);
		fd_cacel.left = new FormAttachment(0, 338);
		cacel.setLayoutData(fd_cacel);
		cacel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shellww.close();
			}
		});
		cacel.setText("取 消");

		shellww.open();
		shellww.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	protected void down() {
		// TODO Auto-generated method stub
		cacel.setEnabled(false);

		if (!canUpdate()) {
			DialogUtil.openInfo(shellww, "没有发下新版本!");
			return;
		}
		HttpURLConnection httpURLConnection = null;
		URL url = null;
		BufferedInputStream bis = null;
		byte[] buf = new byte[10240];
		int size = 0;
		File filea = new File(filePath);
		if (!filea.exists()) {
			filea.mkdir();
		}
		String remoteUrl = downurl;

		// 检查本地文件
		RandomAccessFile rndFile = null;
		File file = new File(filePath + "//" + fileName);
		long remoteFileSize = getRemoteFileSize(remoteUrl);
		long nPos = 0;

		// System.out.println("remote size" + remoteFileSize + "--" + (int)
		// remoteFileSize);
		long every = remoteFileSize / 100;
		// System.out.println("every size" + every);

		if (file.exists()) {
			long localFileSzie = file.length();
			if (localFileSzie < remoteFileSize) {
				System.out.println("文件续传...");
				nPos = localFileSzie;
			} else {
				System.out.println("文件存在，重新下载...");
				file.delete();
				try {
					file.createNewFile();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

		} else {
			// 建立文件
			try {
				file.createNewFile();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		// 下载文件
		try {
			url = new URL(remoteUrl);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			// 设置User-Agent
			httpURLConnection.setRequestProperty("User-Agent", "Net");
			// 设置续传开始
			httpURLConnection.setRequestProperty("Range", "bytes=" + nPos + "-");
			// 获取输入流
			bis = new BufferedInputStream(httpURLConnection.getInputStream());
			rndFile = new RandomAccessFile(filePath + "\\" + fileName, "rw");
			rndFile.seek(nPos);
			long upsize = 0L;// 递增计数
			int num = 0;
			while ((size = bis.read(buf)) != -1) {
				// if (i > 500) break;
				rndFile.write(buf, 0, size);
				upsize += size;
				num = (int) (upsize / every);
				System.out.println(num + "///////////");
				updatepro.setSelection(num);
			}
			updatepro.setSelection(100);
			httpURLConnection.disconnect();
			MessageDialog.openInformation(shellww, "下载成功！", "重新启动!");
		} catch (Exception e) {
			// TODO: handle exception
			MessageDialog.openInformation(shellww, "exception", e.getMessage());
			MessageDialog.openError(shellww, "网络异常", "网络连接失败,请检查网络环境!");
			return;
		}
		dorestart();
	}

	private boolean canUpdate() {
		// TODO Auto-generated method stub
		DialogUtil.openInfo(shellww, HttpUtils.getHtml(chekupdate) + "--" + version);
		return (HttpUtils.getHtml(chekupdate) > Double.valueOf(version));
	}

	private void dorestart() {
		// TODO Auto-generated method stub
		File thisfile = new File(fileName);
		System.out.println(thisfile.getAbsolutePath());
		// thisfile.renameTo(new File("old" + fileName));
		copy(filePath + "/" + fileName, fileName);
		DialogUtil.openInfo(shellww, "更新成功,立马重启");
		shellww.getDisplay().dispose();

		MainApp.main(new String[] {});
	}

	private void copy(String here, String to) {
		// TODO Auto-generated method stub
		File s = new File(here);
		File t = new File(to);
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Create contents of the dialog.
	 */

	public static long getRemoteFileSize(String url) {
		long size = 0;
		try {
			HttpURLConnection httpUrl = (HttpURLConnection) (new URL(url)).openConnection();
			size = httpUrl.getContentLength();
			httpUrl.disconnect();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return size;
	}
}
