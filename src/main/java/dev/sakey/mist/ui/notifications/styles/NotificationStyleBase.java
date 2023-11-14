package dev.sakey.mist.ui.notifications.styles;

import dev.sakey.mist.ui.notifications.Notification;
import net.minecraft.client.gui.ScaledResolution;

import java.util.concurrent.CopyOnWriteArrayList;

public abstract class NotificationStyleBase {
	public void draw(ScaledResolution sr, Notification n, int index){}
	public void drawOnce(ScaledResolution sr, CopyOnWriteArrayList<Notification> n){}

	public abstract double initY();
}
