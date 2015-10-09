package com.ztesoft.ip.ui;

import jodd.util.StringUtil;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ztesoft.ip.update.DownFile;
import com.ztesoft.ip.utils.BatUtils;
import com.ztesoft.ip.utils.DialogUtil;
import com.ztesoft.ip.utils.IpAddress;
import com.ztesoft.ip.utils.LayoutUtils;
import com.ztesoft.ip.utils.PropertiesUtil;

public class MainApp {
	protected Object result;
	protected static Shell shello;
	protected static Composite shell;
	private static StyledText ip;
	private static StyledText ym;
	private static StyledText wg;
	private static StyledText dns1;
	private static StyledText dns2;
	private static String namestr;
	private static String dns2str;
	private static String dns1str;
	private static String wgstr;
	private static String ymstr;
	private static String ipstr;
	private static Combo name;
	private static Text txtipdns;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			Display display = Display.getDefault();
			shello = new Shell(display, 0);
			shello.setSize(636, 435);
			shello.setLayout(new FormLayout());
			{
				PropertiesUtil.load("resource/config", "value.config");

				String version = PropertiesUtil.getProperty("version");

				shell = LayoutUtils.centerDWdefult(shello, "局域网网络环境配置工具" + version, true, false);
			}
			CTabFolder tabFolder = new CTabFolder(shell, SWT.BORDER);
			FormData fd_tabFolder = new FormData();
			fd_tabFolder.left = new FormAttachment(0);
			fd_tabFolder.right = new FormAttachment(100);
			fd_tabFolder.top = new FormAttachment(0);
			fd_tabFolder.bottom = new FormAttachment(100);
			tabFolder.setLayoutData(fd_tabFolder);
			tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

			CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
			tabItem.setText("自动获取");

			Composite composite = new Composite(tabFolder, SWT.NONE);
			tabItem.setControl(composite);
			composite.setLayout(new FormLayout());

			Label lblip = new Label(composite, SWT.NONE);
			FormData fd_lblip = new FormData();
			fd_lblip.top = new FormAttachment(0, 33);
			fd_lblip.right = new FormAttachment(100, -284);
			lblip.setLayoutData(fd_lblip);
			lblip.setText("自动获取ip");

			txtipdns = new Text(composite, SWT.BORDER);
			txtipdns.setText("1.适合局域网自动获取   自动获取dns");
			FormData fd_txtipdns = new FormData();
			fd_txtipdns.bottom = new FormAttachment(lblip, 108, SWT.BOTTOM);
			fd_txtipdns.top = new FormAttachment(lblip, 25);
			fd_txtipdns.right = new FormAttachment(100, -167);
			fd_txtipdns.left = new FormAttachment(0, 192);
			txtipdns.setLayoutData(fd_txtipdns);

			Button button_3 = new Button(composite, SWT.NONE);
			button_3.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					dosetauto();
				}
			});
			FormData fd_button_3 = new FormData();
			fd_button_3.bottom = new FormAttachment(100, -66);
			fd_button_3.right = new FormAttachment(lblip, 0, SWT.RIGHT);
			button_3.setLayoutData(fd_button_3);
			button_3.setText("设 置");
			
			Label label_3 = new Label(composite, SWT.NONE);
			FormData fd_label_3 = new FormData();
			fd_label_3.top = new FormAttachment(txtipdns, 0, SWT.TOP);
			fd_label_3.left = new FormAttachment(0, 28);
			label_3.setLayoutData(fd_label_3);
			label_3.setText("3.0");

			CTabItem tabItem_1 = new CTabItem(tabFolder, SWT.NONE);
			tabItem_1.setText("手动获取");

			Composite composite_1 = new Composite(tabFolder, SWT.NONE);
			tabItem_1.setControl(composite_1);
			composite_1.setLayout(null);

			CLabel lblIp = new CLabel(composite_1, SWT.NONE);
			lblIp.setBounds(93, 54, 70, 23);
			lblIp.setText("ip 地址:");

			CLabel label = new CLabel(composite_1, SWT.NONE);
			label.setBounds(93, 103, 70, 23);
			label.setText("子网掩码:");

			CLabel label_1 = new CLabel(composite_1, SWT.NONE);
			label_1.setBounds(93, 147, 70, 23);
			label_1.setText("默认网关:");

			CLabel lbldns = new CLabel(composite_1, SWT.NONE);
			lbldns.setBounds(93, 195, 70, 23);
			lbldns.setText("首选DNS:");

			CLabel lbldns_1 = new CLabel(composite_1, SWT.NONE);
			lbldns_1.setBounds(93, 242, 70, 23);
			lbldns_1.setText("备用DNS:");

			Button button = new Button(composite_1, SWT.NONE);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					dosethand();
				}
			});
			button.setBounds(193, 304, 40, 27);
			button.setText("设 置");

			Button button_1 = new Button(composite_1, SWT.NONE);
			button_1.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					shello.close();
				}
			});
			button_1.setBounds(296, 304, 40, 27);
			button_1.setText("取 消");

			Button button_2 = new Button(composite_1, SWT.NONE);
			button_2.setBounds(394, 304, 60, 27);
			button_2.setText("在线查询");

			ip = new StyledText(composite_1, SWT.BORDER);
			ip.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent arg0) {
					// TODO Auto-generated method stub
					if (IpAddress.isIP(ip.getText())) {
						if (!StringUtil.equals(ipstr, ip.getText().trim())) {
							ipstr = ip.getText().trim();

						}
					} else {
						MessageDialog.openWarning(shello, "ip格式不对", "ip格式不对");
						return;
					}
				}

				@Override
				public void focusGained(FocusEvent arg0) {
					// TODO Auto-generated method stub

				}
			});

			ip.setBounds(172, 56, 180, 21);

			ym = new StyledText(composite_1, SWT.BORDER);
			ym.setEditable(false);
			ym.setAlignment(SWT.CENTER);
			ym.setBounds(172, 105, 180, 21);

			wg = new StyledText(composite_1, SWT.BORDER);
			wg.setEditable(false);
			wg.setAlignment(SWT.CENTER);
			wg.setBounds(172, 149, 180, 21);

			dns1 = new StyledText(composite_1, SWT.BORDER);
			dns1.setEditable(false);
			dns1.setAlignment(SWT.CENTER);
			dns1.setBounds(172, 197, 180, 21);

			dns2 = new StyledText(composite_1, SWT.BORDER);
			dns2.setEditable(false);
			dns2.setAlignment(SWT.CENTER);
			dns2.setBounds(172, 244, 180, 21);

			CLabel label_2 = new CLabel(composite_1, SWT.NONE);
			label_2.setText("网络名称");
			label_2.setBounds(93, 10, 70, 23);

			name = new Combo(composite_1, SWT.NONE);
			name.setBounds(172, 10, 180, 25);

			CTabItem tabItem_2 = new CTabItem(tabFolder, SWT.NONE);
			tabItem_2.setText("系统管理");

			Composite composite_2 = new Composite(tabFolder, SWT.NONE);
			tabItem_2.setControl(composite_2);
			composite_2.setLayout(null);

			Button button_4 = new Button(composite_2, SWT.NONE);
			button_4.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					doupdate();
				}
			});
			button_4.setBounds(76, 49, 80, 27);
			button_4.setText("检查更新");

			initDate();
			shello.open();
			shello.layout();

			while (!shello.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected static void doupdate() {
		// TODO Auto-generated method stub
		DownFile f = new DownFile(shello, 0);
		f.open();
	}

	protected static void dosetauto() {
		// TODO Auto-generated method stub
		BatUtils.runAuto(namestr);
		DialogUtil.openInfo(shello, "自动获取ip dns设置成功!");
	}

	protected static void dosethand() {
		// TODO Auto-generated method stub
		if (!IpAddress.isIP(ip.getText())) {
			ip.setText("");
			ip.setToolTipText("请输入准确的ip");
			ip.forceFocus();
			return;
		}
		BatUtils.runHandSet(namestr, ipstr, ymstr, wgstr, dns1str, dns2str);
		DialogUtil.openInfo(shello, "手动获取ip dns设置成功,您现在ip：" + ipstr);
	}

	private static void initDate() {
		// TODO Auto-generated method stub

		PropertiesUtil.load("resource/config", "value.config");

		namestr = PropertiesUtil.getProperty("name");
		// #1 无线网络连接 2 本地连接
		if (StringUtil.equals(namestr, "1")) {
			namestr = "无线网络连接";
		}
		if (StringUtil.equals(namestr, "2")) {
			namestr = "本地连接";
		}
		ipstr = PropertiesUtil.getProperty("ip");
		ymstr = PropertiesUtil.getProperty("ym");
		wgstr = PropertiesUtil.getProperty("wg");
		dns1str = PropertiesUtil.getProperty("dns1");
		dns2str = PropertiesUtil.getProperty("dns2");

		name.setText(namestr);
		ip.setText(ipstr);
		ym.setText(ymstr);
		wg.setText(wgstr);
		dns1.setText(dns1str);
		dns2.setText(dns2str);

	}
}
