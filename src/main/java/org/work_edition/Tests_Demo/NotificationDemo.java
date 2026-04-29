package org.work_edition.Tests_Demo;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

public class NotificationDemo {
    public static void showNotification(String title, String message) {
        if (!SystemTray.isSupported()) {
            System.out.println("System tray not supported");
            return;
        }

        try {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage(""); // icon

            TrayIcon trayIcon = new TrayIcon(image);
            trayIcon.setImageAutoSize(true);
            tray.add(trayIcon);
            trayIcon.displayMessage(title, message, MessageType.INFO);

            // tray.remove(trayIcon); // remove for win icon
        } catch (AWTException e) {
            System.out.println(e.getMessage());
        }
    }
}

