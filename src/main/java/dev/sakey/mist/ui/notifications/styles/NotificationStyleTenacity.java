package dev.sakey.mist.ui.notifications.styles;

import dev.sakey.mist.Mist;
import dev.sakey.mist.ui.notifications.Notification;
import dev.sakey.mist.ui.notifications.NotificationManager;
import dev.sakey.mist.ui.notifications.NotificationRenderer;
import dev.sakey.mist.utils.render.ColourUtil;
import dev.sakey.mist.utils.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class NotificationStyleTenacity extends NotificationStyleBase {

	public void draw(ScaledResolution sr, Notification n, int index) {

		double length = Mist.instance.getFontRenderer().getStringWidth(n.description);

		double x = sr.getScaledWidth() - Math.min(n.getPercent() * 10, length);

		if(n.getPercent() >= 90) {
			x = sr.getScaledWidth() - length + ((n.getPercent() - 90) * 10);
		}

		x -= 15;

		double w = x + length + 5;
		double ty = (sr.getScaledHeight() - (30 + 5) * (index + 1));


		if(n.y < ty) { n.y += (ty - n.y) / 2; }
		else if(n.y > ty) { n.y += (ty - n.y) / 2; }

		double y = n.y;
		double h = y + 30;


		int colour;

		switch(n.type)
		{
			case SUCCESS:
				colour = getDarkColour(0xff00ff00);
				break;

			case INFO:
				colour = getDarkColour(0xff00ffff);
				break;

			case ERROR:
				colour = getDarkColour(0xffff0000);
				break;

			case WARNING:
				colour = getDarkColour(0xffff8000);
				break;

			default:
			case OTHER:
				colour = getDarkColour(0xffffffff);
				break;

			case RAINBOW:
				colour = getDarkColour(ColourUtil.getRainbow(3));
				break;
		}

		RenderUtils.drawRoundedRect(x - 5, y, w + 5, h, 4, colour);

		Mist.instance.getFontRenderer().drawString("§l" + n.title, x + 5, y + 5, 0xffffffff, false);
		Mist.instance.getFontRenderer().drawString(n.description, x + 5, y + 5 + Mist.instance.getFontRenderer().getFontHeight(), 0xffffffff, false);

	}

	private int getDarkColour(int orig) {
		Color colour = ColourUtil.decToColour(orig);
		colour = colour.darker();
		return colour.getRGB();
	}

	@Override
	public double initY() {
		return NotificationRenderer.sr.getScaledHeight() - (NotificationManager.notifications.size() + 1) * (30 + 5);
	}
}
