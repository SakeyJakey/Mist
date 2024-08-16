package dev.sakey.mist.ui.notifications;

import dev.sakey.mist.Mist;
import dev.sakey.mist.ui.notifications.styles.NotificationStyleBase;
import dev.sakey.mist.ui.notifications.styles.NotificationStyleModern;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager {

	public static CopyOnWriteArrayList<Notification> notifications = new CopyOnWriteArrayList<Notification>();

	public static NotificationStyleBase renderer = new NotificationStyleModern();

	public static void TextNotification(String title, String message) {
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\247f[ \247d" + Mist.instance.name + " \247f] \2477- \247l" + title + ": \247r" + message));
	}

	public static void Notify(Notification n) {
		notifications.add(n);
	}

}
