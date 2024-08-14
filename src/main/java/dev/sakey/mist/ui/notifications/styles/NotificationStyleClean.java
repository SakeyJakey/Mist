package dev.sakey.mist.ui.notifications.styles;

import dev.sakey.mist.ui.notifications.Notification;
import dev.sakey.mist.utils.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationStyleClean extends NotificationStyleBase {

	public void drawOnce(ScaledResolution sr, CopyOnWriteArrayList<Notification> notifications) {
		int width = 0;
		for (Notification n : notifications) {
		}
	}

	public void draw(ScaledResolution sr, ArrayList<Notification> n, int index) {

	}

	@Override
	public double initY() {
		return 0;
	}
}
