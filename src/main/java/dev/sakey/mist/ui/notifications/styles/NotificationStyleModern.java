package dev.sakey.mist.ui.notifications.styles;

import dev.sakey.mist.Mist;
import dev.sakey.mist.ui.notifications.Notification;
import dev.sakey.mist.ui.notifications.NotificationManager;
import dev.sakey.mist.ui.notifications.NotificationRenderer;
import dev.sakey.mist.utils.render.ColourUtil;
import dev.sakey.mist.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class NotificationStyleModern extends NotificationStyleBase {

	double alpha;
	int margin = 20;
	int width = 100;
	int height = 30;

	public void draw(ScaledResolution sr, Notification n, int index) {
		//Notification n = new Notification(n); // so we don't override with + More

		if (n.getPercent() <= 0) return;

		double x = sr.getScaledWidth() - width -
				(width > Mist.instance.getFontRenderer().getStringWidth(n.description) ? 0 :
						Mist.instance.getFontRenderer().getStringWidth(n.description) - 85) - margin;

		alpha = 1;

		if (n.getPercent() >= 90)
			alpha = (10 - (n.getPercent() - 90)) / 10;
		else if (n.getPercent() <= 10)
			alpha = n.getPercent() / 10;
		else
			alpha = 1;

		if (n.getPercent() >= 100) return;

		double ty = (sr.getScaledHeight() - height * (index + 1));

		if (n.getPercent() >= 90)
			ty += height;

		ty = Math.max(margin, ty);

		//todo: make everything vars

		double speed = 1.5;
		double smoothing = Minecraft.getDebugFPS() / speed;

		if (n.y < ty) {
			n.y -= (n.y - ty) / smoothing;
		} else if (n.y > ty) {
			n.y -= (n.y - ty) / smoothing;
		}

		double w = sr.getScaledWidth() - margin;

		double y = n.y - margin;
		double h = y + height;

		int color;

		switch (n.type) {
			case SUCCESS:
				color = getDarkAlphaColour(0xff00ff00);
				break;

			case INFO:
				color = getDarkAlphaColour(0xff00ffff);
				break;

			case ERROR:
				color = getDarkAlphaColour(0xffff0000);
				break;

			case WARNING:
				color = getDarkAlphaColour(0xffff8000);
				break;

			default:
			case OTHER:
				color = getDarkAlphaColour(0xffffffff);
				break;

			case RAINBOW:
				color = getDarkAlphaColour(ColourUtil.getRainbow(3));
				break;
		}

		if (NotificationManager.notifications.size() == 1)
			RenderUtils.drawRoundedRect(x, y, w, h, 5, color);
		else if (index == 0)
			RenderUtils.drawRoundedRect(x, y, w, h, 5, false, false, true, true, color);
		else if (index == NotificationManager.notifications.size() - 1)
			RenderUtils.drawRoundedRect(x, y, w, h, 5, true, true, false, false, color);
		else
			RenderUtils.drawRect(x, y, w, h, color);

		//Gui.drawRect(x, h - 1, x + n.getPercent(), h, getAlphaColour(Colours.white()));
		//RenderUtils.drawRoundedRect(x, y + (h - y) * (n.getPercent() / 100), x + 2, h, 2, true, false, true, false, getAlphaColour(Colours.white()));

		if (index != 0)
			Gui.drawRect(x, h, w, h + 1, getAlphaColour(ColourUtil.white()));

		if (n.getPercent() <= 1 || n.getPercent() >= 99) return;
		Mist.instance.getFontRenderer().drawString(("§l" + n.title), x + 5, y + 5, getAlphaColour(0xffffffff), false);
		Mist.instance.getFontRenderer().drawString(n.description, x + 5, y + 5 + Mist.instance.getFontRenderer().getFontHeight(), getAlphaColour(0xffffffff), false);
	}


	public double initY() {
		return Math.max(NotificationRenderer.sr.getScaledHeight() - (NotificationManager.notifications.size() + 1) * height, margin);
	}

	private int getAlphaColour(int orig) {
		return ColourUtil.alphaColour(orig, (float) alpha);
	}

	private int getDarkAlphaColour(int orig) {
		Color colour = ColourUtil.decToColour(orig);
		colour = new Color(colour.getRed(), colour.getGreen(), colour.getBlue(), (int) (alpha * colour.getAlpha()));
		colour = colour.darker().darker().darker();
		return colour.getRGB();
	}
}
