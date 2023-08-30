package dev.sakey.mist.ui.notifications;

import java.util.concurrent.CopyOnWriteArrayList;

import dev.sakey.mist.Mist;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class NotificationManager {

	public static CopyOnWriteArrayList<Notification> notifications = new CopyOnWriteArrayList<Notification>();

	public static void TextNotification(NotificationType type, String title, String message) {
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\247f[ \247d" + Mist.instance.name + " \247f] \2477- \247l" + title + ": \247r" + message));
	}

	public static void Notify(Notification n) {
		notifications.add(n);
	}
	
}
