package dev.sakey.mist.ui.notifications;

import dev.sakey.mist.Mist;
import dev.sakey.mist.utils.client.Colours;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class NotificationRenderer {

	public static void draw(ScaledResolution sr) {

		for(Notification n : NotificationManager.notifications) {

		double count = NotificationManager.notifications.indexOf(n);
		double x = sr.getScaledWidth() - Math.min(n.getPercent() * 10, 100) -
				  (100 > Mist.instance.getFontRenderer().getStringWidth(n.description) ? 0 :
						  Mist.instance.getFontRenderer().getStringWidth(n.description) - 85);

		if(n.getPercent() >= 90) {
			  x = sr.getScaledWidth() - 100 + ((n.getPercent() - 90) * 10);
		}

		double w = sr.getScaledWidth();
		double ty = (int) ((sr.getScaledHeight() - 30
		* (count + 1)));
		double th = sr.getScaledHeight() - 30 * count;


		if(n.y < ty) { n.y+=2; }
		else if(n.y > ty) { n.y-=10; }

		if(n.h < th) { n.h+=2; }
		else if(n.h > th) { n.h-=10; }

		double y = n.y;
		double h = n.h;

		Gui.drawRect(x, n.y, w, n.h, Colours.black());
		Gui.drawRect(x, n.h - 2, x + n.getPercent(), n.h,
		Colours.white());


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
			  Gui.drawRect(x - 3, y, x, h, Colours.getRainbow(3));
			  break;
		default:
			  break;

		}
		Mist.instance.getFontRenderer().drawString(n.title, x + 5, y + 5, 0xffffffff, false);
			  Mist.instance.getFontRenderer().drawString(n.description, x + 5, y + 5 + 4 + Mist.instance.getFontRenderer().getFontHeight(), 0xffffffff, false);

		n.check();

	}
		 

//		for(Notification n : NotificationManager.notifications) {
//			
//			double count = NotificationManager.notifications.indexOf(n);
//			double x = sr.getScaledWidth() - Math.min(n.getPercent() * 10, 100) - (100 > fr.getStringWidth(n.description) ? 0 : fr.getStringWidth(n.description) - 85) - 10;
//			if(n.getPercent() >= 90) {
//				x = sr.getScaledWidth() - 100 + ((n.getPercent() - 90) * 10);
//			}
//			
//			double w =  sr.getScaledWidth()  - 10;
//			double ty = sr.getScaledHeight() - 10 - 30 * (count + 1);
//			double th = sr.getScaledHeight() - 10 - 30 * count;
//			
//			
//			if(n.y < ty) {
//				n.y+=2;
//			} else if(n.y > ty) {
//				n.y-=10;
//			}
//			
//			if(n.h < th) {
//				n.h+=2;
//			} else if(n.h > th) {
//				n.h-=10;
//			}
//			
//			double y = n.y - 10;
//			double h = n.h - 10;
//			
//			//Gui.drawRect(x, n.y, w, n.h, Colours.getColour(Colours.white));
//			//Gui.drawRect(x, n.h - 2, x + n.getPercent(), n.h, Colours.getColour(Colours.white));
//			
//			
//			switch(n.type) {
//			case SUCCESS:
//				Gui.drawRoundedRect(x, n.y, w, n.h, 10, 0x00ff00ff);
//				break;
//			case INFO:
//				Gui.drawRoundedRect(x, n.y, w, n.h, 10, 0x00ffffff);
//				break;
//			case ERROR:
//				Gui.drawRoundedRect(x, n.y, w, n.h, 10, 0xff0000ff);
//				break;
//			case WARNING:
//				Gui.drawRoundedRect(x, n.y, w, n.h, 10, 0xffff80ff);
//				break;
//			case OTHER:
//				Gui.drawRoundedRect(x, n.y, w, n.h, 10, Colours.getColour(Colours.white));
//				break;
//			case RAINBOW:
//				Gui.drawRoundedRect(x, n.y, w, n.h, 10, Colours.getRainbow(3));
//			default:
//				break;
//			
//			}
//			fr.drawString(n.title, x + 5, y + 5, 0xffffffff, false);
//			fr.drawString(n.description, x + 5, y + 5 + 4 + fr.FONT_HEIGHT, 0xffffffff, false);
//			
//			n.check();
//			
//			
//		}
	}

}
