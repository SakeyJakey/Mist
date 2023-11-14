package dev.sakey.mist.ui.notifications.styles;

import dev.sakey.mist.ui.notifications.Notification;
import dev.sakey.mist.utils.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationStyleClean extends NotificationStyleBase {

	public void drawOnce(ScaledResolution sr, CopyOnWriteArrayList<Notification> n) {
		int count = n.size();

		RenderUtils.drawRoundedRect(
				sr.getScaledWidth() / 2 - count * 10,
				10,
				sr.getScaledWidth() / 2 + count * 10,
				30,
				10, -1
		);
	}

	public void draw(ScaledResolution sr, ArrayList<Notification> n, int index) {

	}

	@Override
	public double initY() {
		return 0;
	}
}
