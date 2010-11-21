package pl.jw.mbank.client.gui;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.application.SingleFrameApplication;

import pl.jw.mbank.client.data.ITableDataRefreshCallbackHanlder;

public class SystemTraySupport {
	private static Log log = LogFactory.getLog(SystemTraySupport.class);

	private static TrayIcon TRAY_ICON;

	private final ITableDataRefreshCallbackHanlder dataRefreshCallbackHanlder;

	private final Window window;

	private final PopupMenu popupMenu = new PopupMenu();

	private final MenuItem menuItemDisplay = new MenuItem("Display");
	private final MenuItem menuItemRefresh = new MenuItem("Refresh");
	private final MenuItem menuItemExit = new MenuItem("Exit");

	public SystemTraySupport(Window window, ITableDataRefreshCallbackHanlder dataRefreshCallbackHanlder) {
		this.window = window;
		this.dataRefreshCallbackHanlder = dataRefreshCallbackHanlder;
	}

	public void init() {
		ImageIcon image = null;
		URL imgURL = getClass().getResource("/pl/jw/mbank/client/resources/systemTray.png");

		if (imgURL != null) {
			image = new ImageIcon(imgURL, "mBankSfi");
		} else {
			log.error("Could not find system tray icon.");
		}

		if (SystemTray.isSupported()) {

			popupMenu.add(menuItemDisplay);
			popupMenu.add(menuItemRefresh);
			popupMenu.add(menuItemExit);

			TRAY_ICON = new TrayIcon(image.getImage());
			TRAY_ICON.setImageAutoSize(true);
			TRAY_ICON.setPopupMenu(popupMenu);
			TRAY_ICON.setToolTip("mBankSfi");

			try {
				SystemTray.getSystemTray().add(TRAY_ICON);
			} catch (AWTException e) {
				log.error("TrayIcon could not be added.", e);
			}

			setup();
		} else {
			log.info("SystemTray is not supported");
		}

	}

	private void setup() {
		menuItemRefresh.addActionListener(new TrayActionListener());
		menuItemDisplay.addActionListener(new TrayActionListener());
		menuItemExit.addActionListener(new TrayActionListener());

		TRAY_ICON.addActionListener(new TrayActionListener());
	}

	private void minimizeToTray() {
		if (window.isVisible()) {
			window.setVisible(false);
		} else {
			window.setVisible(true);
		}
	}

	private class TrayActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == menuItemDisplay) {
				window.setVisible(true);
			} else if (e.getSource() == menuItemExit) {
				SingleFrameApplication.getInstance().exit(e);
			} else if (e.getSource() == menuItemRefresh) {
				dataRefreshCallbackHanlder.refresh();
			} else if (e.getSource() == TRAY_ICON) {
				minimizeToTray();
			}
		}
	}

	public static void showTrayPopUpMessage(String msg) {
		TRAY_ICON.displayMessage("mBankSfi", msg, TrayIcon.MessageType.INFO);
	}
}
