package dev.sakey.mist.ui.notifications;

import dev.sakey.mist.Mist;
import dev.sakey.mist.modules.impl.render.NotificationsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class NotificationRenderer {

	public static ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

	public static void draw(ScaledResolution sr) {

		if (!Mist.instance.getModuleManager().getModule(NotificationsMod.class).isEnabled()) return;

		NotificationRenderer.sr = sr;

		NotificationManager.renderer.drawOnce(sr, NotificationManager.notifications);

		for (Notification n : NotificationManager.notifications) {
			NotificationManager.renderer.draw(sr, n, NotificationManager.notifications.indexOf(n));
			n.check();
		}
	}
}
