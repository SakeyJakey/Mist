package dev.sakey.mist.ui.clickguis.main;

import dev.sakey.mist.Mist;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.impl.render.ClickGuiMod;
import dev.sakey.mist.modules.settings.Setting;
import dev.sakey.mist.modules.settings.impl.BoolSetting;
import dev.sakey.mist.modules.settings.impl.KeySetting;
import dev.sakey.mist.modules.settings.impl.ModeSetting;
import dev.sakey.mist.modules.settings.impl.NumberSetting;
import dev.sakey.mist.ui.draggables.Draggable;
import dev.sakey.mist.ui.draggables.ResizeMode;
import dev.sakey.mist.utils.render.ColourUtil;
import dev.sakey.mist.utils.client.font.GlyphPageFontRenderer;
import dev.sakey.mist.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.concurrent.CopyOnWriteArrayList;

public class Panel extends Draggable {

	private final Category category;
	private CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<Module>();
	private final GlyphPageFontRenderer fr = Mist.instance.getFontRenderer(18);
	private final GlyphPageFontRenderer sfr = Mist.instance.getFontRenderer(9);

	private final MainClickGui cgui;

	private double tHeight;
	private final int index;
	public Panel(double xPos, double yPos, Category c, MainClickGui cgui, int index) {
		super(xPos, yPos, 100, 0);
		this.cgui = cgui;
		category = c;
		modules = Mist.instance.getModuleManager().getModules(category);
		tHeight = calculateHeight();
		resizeMode = ResizeMode.NONE;
		this.index = index;
	}

	protected void onShow() {
		height = 0;
	}

	private double calculateHeight() {
		int count = 1; // 1 for title
		for (Module m :
				modules) {
			if(!m.isSearched(cgui.search.getText()) && ((ClickGuiMod)Mist.instance.getModuleManager().getModule(ClickGuiMod.class)).hideNonSearch.isEnabled()) continue;

			count++;

			if(!m.areSettingsOpen()) continue;
			for(Setting s : m.getSettings())
				if(!s.isHidden())
					count++;
		}
		return count * fr.getFontHeight();
	}

	protected boolean getHoveringMove(int mX, int mY) {
		return mX > xPos && mY > yPos && mX < getWPos() && mY < yPos + fr.getFontHeight();
	}

	protected boolean getHoveringResize(int mX, int mY) {
		return false;
	}

	private NumberSetting numberSettingEditing;

	private boolean wasClicked;
	public void draw() {
		final double speed = Minecraft.getDebugFPS() / 8;

		tHeight = calculateHeight();

		height += (tHeight - height) / speed;

		double x, y, w, h;

		int count = 0;
		for (Module m :
				modules) {
			if(!m.isSearched(cgui.search.getText()) && ((ClickGuiMod)Mist.instance.getModuleManager().getModule(ClickGuiMod.class)).hideNonSearch.isEnabled()) continue;

			count++;

			x = xPos;
			y = yPos + count * fr.getFontHeight();
			w = getWPos();
			h = yPos + (count + 1) * fr.getFontHeight();

			if (m.isEnabled())
				RenderUtils.drawRect(x, y, w, h + (m.areSettingsOpen() ? m.getSettings().size() * fr.getFontHeight() : 0), ColourUtil.dim(ColourUtil.getRainbow(4, (count + index) * 50), 1));
			else if (m.areSettingsOpen()) {
				RenderUtils.drawRect(x, y, w, h, 0x80000000);
				RenderUtils.drawRect(x, y, w, h, 0x80000000);
			}

			fr.drawString((m.isEnabled() ? "§l" : "") + m.getName(), x,  y, m.isSearched(cgui.search.getText()) ? -1 : ColourUtil.grey(), false);

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
				m.toggle();
			else if (getMouseButtonOnce(x, y, w, h, 1))
				m.toggleSettingsOpen();


			if(!m.areSettingsOpen()) continue;

			for (Setting s :
					m.getSettings()) {
				
				if(s.isHidden()) continue;

				count++;

				x = xPos;
				y = yPos + count * fr.getFontHeight();
				w = getWPos();
				h = yPos + (count + 1) * fr.getFontHeight();

				RenderUtils.drawRect(x, y, w, h, 0x80000000);


				if (s instanceof BoolSetting) {
					BoolSetting setting = (BoolSetting) s;
					if (!setting.isEnabled())
						RenderUtils.drawOutlineRoundedRect(w - fr.getFontHeight() - 2 + 1, y + 1, w - 2 - 1, h - 1, 2, 2, -1);
					else
						RenderUtils.drawRoundedRect(w - fr.getFontHeight() - 2 + 0.5, y + 0.5, w - 2 - 0.5, h - 0.5, 2, -1);

					if (getMouseButtonOnce(w - fr.getFontHeight(), y, w, h, 0))
						setting.toggle();

				}

				if (s instanceof KeySetting) {
					KeySetting setting = (KeySetting) s;


					if(getMouseButtonOnce(x, y, w, h, 0))
						if (cgui.currentlyTyping == setting)
							cgui.currentlyTyping = null;
						else
							cgui.currentlyTyping = setting;

					fr.drawStringRight(
						cgui.currentlyTyping != setting ? Keyboard.getKeyName(setting.getCode()) : "...", w - 2, y, -1, false
					);
				}

				if (s instanceof ModeSetting) {
					ModeSetting setting = (ModeSetting) s;
					fr.drawStringRight(
						setting.getMode(), w - 2, y, -1, false
					);

					if(getMouseButtonOnce(x, y, w, h, 0))
						setting.cycle();
				}

				if (s instanceof NumberSetting) {
					NumberSetting setting = (NumberSetting) s;

					RenderUtils.drawRect(x, y, x + ((w - x) / setting.getMax()) * (setting.getValue() - setting.getMin()), h, 0x80ffffff);

					fr.drawStringRight(
							String.valueOf(setting.getValue()), w - 2, y, -1, false
					);

					if (Mouse.isButtonDown(0)) {
						if (RenderUtils.isInside(mouseX, mouseY, x, y, w, h) || numberSettingEditing == setting) {
							numberSettingEditing = setting;
							setting.setValue(
									MathHelper.clamp_double(
											((mouseX - xPos) / width) *
												(setting.getMax() - setting.getMin())
												+ setting.getMin(),
											setting.getMin(),
											setting.getMax()
									)
							);
						}
					}
					else if (numberSettingEditing == setting) {
						numberSettingEditing = null;
					}
				}

				fr.drawString(s.getName(), x,  y, -1, false);
			}
		}

		// Make top black
		RenderUtils.drawRect(xPos, yPos, getWPos(), yPos + fr.getFontHeight(), ColourUtil.black());

		fr.drawString("§l" + category.name, xPos,  yPos, -1, false);

		wasClicked = Mouse.isButtonDown(0) || Mouse.isButtonDown(1) || Mouse.isButtonDown(2);
	}

	public void setCurrentlyTyping() {

	}

	public int getMouseButtonOnce(double x, double y, double w, double h) {
		if(RenderUtils.isInside(mouseX, mouseY, x, y, w, h)) {
			if (Mouse.isButtonDown(0)) {
				if(!wasClicked) {
					wasClicked = true;
					return 0;
				}
			}
			else if (Mouse.isButtonDown(1)) {
				if(!wasClicked) {
					wasClicked = true;
					return 1;
				}
			}
			else if (Mouse.isButtonDown(2)) {
				if(!wasClicked) {
					wasClicked = true;
					return 2;
				}
			}
			else {
				wasClicked = false;
			}
		}
		return -1;
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

	public boolean getMouseButtonOnce(int button) {
		if (Mouse.isButtonDown(button)) {
			if(!wasClicked) {
				wasClicked = true;
				return true;
			}
		}
		else if(!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1) && !Mouse.isButtonDown(2)) {
			wasClicked = false;
		}
		return false;
	}

}
