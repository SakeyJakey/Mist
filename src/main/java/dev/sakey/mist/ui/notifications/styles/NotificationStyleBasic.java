package dev.sakey.mist.ui.notifications.styles;

import dev.sakey.mist.Mist;
import dev.sakey.mist.ui.notifications.Notification;
import dev.sakey.mist.ui.notifications.NotificationManager;
import dev.sakey.mist.ui.notifications.NotificationRenderer;
import dev.sakey.mist.utils.render.ColourUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class NotificationStyleBasic extends NotificationStyleBase {

	public void draw(ScaledResolution sr, Notification n, int index) {
		double x = sr.getScaledWidth() - Math.min(n.getPercent() * 10, 100) -
				(100 > Mist.instance.getFontRenderer().getStringWidth(n.description) ? 0 :
						Mist.instance.getFontRenderer().getStringWidth(n.description) - 85);

		if(n.getPercent() >= 90) {
			x = sr.getScaledWidth() - 100 + ((n.getPercent() - 90) * 10);
		}

		double w = sr.getScaledWidth();
		double ty = (sr.getScaledHeight() - 30
				* (index + 1));


		if(n.y < ty) { n.y += (ty - n.y) / 4; }
		else if(n.y > ty) { n.y += (ty - n.y) / 4; }

		double y = n.y;
		double h = n.y + 30;

		Gui.drawRect(x, n.y, w, h, ColourUtil.black());
		Gui.drawRect(x, h - 2, x + n.getPercent(), h,
				ColourUtil.white());


		switch(n.type)
		{
			case SUCCESS:
				Gui.drawRect(x - 3, y, x, h, 0xff00ff00);
				break;
			case INFO:
				Gui.drawRect(x - 3, y, x, h, 0xff00ffff);
				break;
			case ERROR:
				Gui.drawRect(x - 3, y, x, h, 0xffff0000);
				break;
			case WARNING:
				Gui.drawRect(x - 3, y, x, h, 0xffff8000);
				break;
			case OTHER:
				Gui.drawRect(x - 3, y, x, h, 0xffffffff);
				break;
			case RAINBOW:
				Gui.drawRect(x - 3, y, x, h, ColourUtil.getRainbow(3));
				break;
			default:
				break;

		}

		Mist.instance.getFontRenderer().drawString("§l" + n.title, x + 5, y + 5, 0xffffffff, false);
		Mist.instance.getFontRenderer().drawString(n.description, x + 5, y + 5 + Mist.instance.getFontRenderer().getFontHeight(), 0xffffffff, false);

	}

	@Override
	public double initY() {
		return NotificationRenderer.sr.getScaledHeight() - 30 - NotificationManager.notifications.size() * 30;
	}
}
