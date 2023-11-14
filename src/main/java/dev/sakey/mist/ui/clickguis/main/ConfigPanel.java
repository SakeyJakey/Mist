package dev.sakey.mist.ui.clickguis.main;

import dev.sakey.mist.Mist;
import dev.sakey.mist.ui.draggables.Draggable;
import dev.sakey.mist.ui.draggables.ResizeMode;
import dev.sakey.mist.ui.notifications.Notification;
import dev.sakey.mist.ui.notifications.NotificationType;
import dev.sakey.mist.utils.client.font.GlyphPageFontRenderer;
import dev.sakey.mist.utils.render.ColourUtil;
import dev.sakey.mist.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigPanel extends Draggable {
	private final GlyphPageFontRenderer fr = Mist.instance.getFontRenderer(18);

	private ConcurrentHashMap<String, Boolean> configs = new ConcurrentHashMap<String, Boolean>();

	private final int settingSize = 3;

	private double tHeight;

	MainClickGui cgui;

	public ConfigPanel(double xPos, double yPos, MainClickGui cgui) {
		super(xPos, yPos, 100, 0);
		tHeight = calculateHeight();
		resizeMode = ResizeMode.NONE;
		this.cgui = cgui;
	}

	protected void onShow() {
		height = 0;
		getConfigs();
	}

	private void getConfigs() {
		configs.clear();
		for (String c :
				Mist.instance.getConfigManager().getConfigs()) {
			configs.put(c, false);
		}
	}

	private double calculateHeight() {
		int count = 2 /* 2 for title and bottom */ + configs.size();

		for (String c :
				configs.keySet()) {
			if (configs.get(c))
				count += settingSize;
		}

		return count * fr.getFontHeight();
	}

	protected boolean getHoveringMove(int mX, int mY) {
		return mX > xPos && mY > yPos && mX < getWPos() && mY < yPos + fr.getFontHeight();
	}

	protected boolean getHoveringResize(int mX, int mY) {
		return false;
	}

	private boolean wasClicked;
	public void draw() {
		final double speed = Minecraft.getDebugFPS() / 8;

		tHeight = calculateHeight();

		height += (tHeight - height) / speed;

		double x, y, w, h;

		x = xPos;
		y = yPos;
		w = getWPos();
		h = yPos + fr.getFontHeight();

		int count = 0;
		for (String c :
				configs.keySet()) {
			count++; // start at 1 for title

			y += fr.getFontHeight();
			h += fr.getFontHeight();

			if (Mist.instance.getConfigManager().isCurrentlySelected(c)) // todo add is config loaded add
				RenderUtils.drawRect(x, y, w, h + (configs.get(c) ? settingSize * fr.getFontHeight() : 0), ColourUtil.dim(ColourUtil.getRainbow(4, count * 50), 1));
			else if (configs.get(c)) {
				RenderUtils.drawRect(x, y, w, h, 0x80000000);
				RenderUtils.drawRect(x, y, w, h, 0x80000000);
			}

			char[] config = c.toCharArray();
			if(config.length > 0)
				config[0] = c.toUpperCase().charAt(0);

			fr.drawString(String.valueOf(config), x,  y, -1, false);

/*			if(RenderUtils.isInside(mouseX, mouseY, x, y, w, h)) {
				if (Mouse.isButtonDown(0)) {
					if(!wasClicked) {
						wasClicked = true;
						m.toggle();
					}
				}
				else if(Mouse.isButtonDown(1)) {
					if(!wasClicked) {
						wasClicked = true;
						m.toggleSettingsOpen();
					}
				}
				else {
					wasClicked = false;
				}
			}*/


			if (getMouseButtonOnce(x, y, w, h, 0))
				Mist.instance.getConfigManager().loadConfig(c);
			else if (getMouseButtonOnce(x, y, w, h, 1)) {
				configs.replace(c, !configs.get(c));
			}

			if(!configs.get(c)) continue;

			for (int i = 0; i < settingSize; i++) {

				y += fr.getFontHeight();
				h += fr.getFontHeight();

				RenderUtils.drawRect(x, y, w, h, 0x80000000);

				switch (i) {
					case 0:
						fr.drawString("Save", x, y, -1, false);
						if(getMouseButtonOnce(x, y, w, h, 0))
							Mist.instance.getConfigManager().saveConfig(c);
						break;
					case 1:
						fr.drawString("Load", x, y, -1, false);
						if(getMouseButtonOnce(x, y, w, h, 0))
							Mist.instance.getConfigManager().loadConfig(c);
						break;
					case 2:
						fr.drawString("Delete", x, y, -1, false);
						if(getMouseButtonOnce(x, y, w, h, 0))
							new Notification("Configs", "Right click to confirm.", NotificationType.INFO, 3000);
						if(getMouseButtonOnce(x, y, w, h, 1)) {
							Mist.instance.getConfigManager().deleteConfig(c);
							getConfigs();
						}
						break;
				}
			}
		}

		// Make top and bottom black
		RenderUtils.drawRect(xPos, yPos, getWPos(), yPos + fr.getFontHeight(), ColourUtil.black());
		RenderUtils.drawRect(xPos, getHPos() - fr.getFontHeight(), getWPos(), getHPos(), ColourUtil.black());

		fr.drawString("+", xPos + 2,  getHPos() - fr.getFontHeight(), -1, false);

		if(getMouseButtonOnce(xPos + 2,  getHPos() - fr.getFontHeight(), xPos + 2 + fr.getStringWidth("+") + 2,  getHPos(), 0)) {
			Minecraft.getMinecraft().displayGuiScreen(new ConfigName(xPos, yPos, width, 20, cgui));
			getConfigs();
		}


		fr.drawString("§lConfigs", xPos,  yPos, -1, false);

		wasClicked = Mouse.isButtonDown(0) || Mouse.isButtonDown(1) || Mouse.isButtonDown(2);
	}

	public boolean getMouseButtonOnce(double x, double y, double w, double h, int button) {
		if(RenderUtils.isInside(mouseX, mouseY, x, y, w, h)) {
			if (Mouse.isButtonDown(button)) {
				if(!wasClicked) {
					wasClicked = true;
					return true;
				}
			}
			else if(!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1) && !Mouse.isButtonDown(2)) {
				wasClicked = false;
			}
		}
		return false;
	}
}
