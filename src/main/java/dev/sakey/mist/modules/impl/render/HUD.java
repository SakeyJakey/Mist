package dev.sakey.mist.modules.impl.render;

import dev.sakey.mist.Mist;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.render.EventRenderHUD;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.ui.notifications.NotificationRenderer;
import dev.sakey.mist.utils.client.Image;
import dev.sakey.mist.utils.client.animation.impl.EaseOut;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Comparator;

public class HUD extends Module {


	EventHandler<EventRenderHUD> renderHUD = e -> draw();
	Image real = new Image("Mist/hentai/real.png", 2316, 3088);
	Image monerochan = new Image("Mist/hentai/monerochan.png", 3507, 2480);

	@ModuleInfo(name = "HUD", description = "Heads up display.", category = Category.RENDER, enabledByDefault = true, hiddenInArrayList = true)
	public HUD() {
		onEnable();
	}

	public void onEnable() {
		Mist.instance.getEventManager().registerEventHandler(EventRenderHUD.class, renderHUD);
	}

	public void onDisable() {
		Mist.instance.getEventManager().unregisterEventHandler(renderHUD);
	}

	public void draw() {

/*        ResourceLocation cape = new ResourceLocation("Twen/cape.png");
        if(mc.thePlayer.getLocationCape() != cape)
            mc.thePlayer.setLocationOfCape(cape);*/

		GL11.glColor4f(1, 1, 1, 1);
		ScaledResolution sr = new ScaledResolution(mc);

//            if(((BoolSetting)HUD  [get setting]  ))
//                drawEntityOnScreen(50, sr.getScaledHeight() - 25, 50, 0, -mc.thePlayer.rotationPitch / 4, mc.thePlayer);


		Mist.instance.getModuleManager().getModules().sort(new ModuleComparator());


		drawOverlay(sr);
		drawList(sr);  //todo make separate mod
		if (!(mc.currentScreen instanceof GuiChat))
			Mist.instance.getDraggableManager().updateChat(); //otherwise GuiChat:drawScreen

		NotificationRenderer.draw(sr);
	}

	void drawOverlay(ScaledResolution sr) {
		//real.drawImageTopLeft(10, 10, 0.25, 0.25);
		//monerochan.drawImageTopLeft(60, 10, 0.25, 0.25);

		GlStateManager.pushMatrix();
		GlStateManager.enableAlpha();

		//RenderUtils.drawGradientOutlineRoundedRect(0, 0, 100, 100, 5, 2, -1);

		double size = 1;//((NumberSetting)Twen.settingsManager.getSettings("Size", "HUD")).getValue();
		GlStateManager.scale(size, size, 0);

		if (Mist.instance.name.isEmpty()) return;
//                    Mist.instance.getLargeFontRenderer().drawString(Mist.instance.name.charAt(0) + "§f" + Mist.instance.name.substring(1), 3, 0, Colours.getRainbow(4), false);
		Mist.instance.getFontRenderer(Mist.Constants.largeFontSize).drawRainbowWaveDanceString(Mist.instance.name, 3, 0, 4, 0, 5, 10, false);

		Mist.instance.getFontRenderer().drawRainbowWaveString(Mist.instance.versionName + " UID: " + Mist.instance.getUid(), Mist.instance.getFontRenderer(Mist.Constants.largeFontSize).getStringWidth(Mist.instance.name) + 3, Mist.instance.getFontRenderer(Mist.Constants.largeFontSize).getFontHeight() - Mist.instance.getFontRenderer().getFontHeight(), 4, 100, false);

		GlStateManager.popMatrix();
	}

	void drawList(ScaledResolution sr) {
		Color color_a = Color.YELLOW;
		Color color_b = Color.YELLOW.darker().darker();

		double diff = 10;
		double speed = 2;

		int count = 0;
		for (Module m : Mist.instance.getModuleManager().getModules()) {
			if (!m.isEnabled() || m.isHiddenInArray()) {
				m.arrayAnimationX = null;
				m.arrayAnimationY = null;
				continue;
			}
			if (m.arrayAnimationX == null) m.arrayAnimationX = new EaseOut(500, m.getTextLength() + 5);
			if (m.arrayAnimationY == null)
				m.arrayAnimationY = new EaseOut(500, count * Mist.instance.getFontRenderer().getFontHeight());


			Color color;
			double t = Math.sin((System.currentTimeMillis() * (speed / 1000))
					+ (count * (diff / 10)))
					* 0.5 + 0.5;
			color = new Color((int) (color_a.getRed() * t + (1 - t) * color_b.getRed()), (int) (color_a.getGreen() * t + (1 - t) * color_b.getGreen()), (int) (color_a.getBlue() * t + (1 - t) * color_b.getBlue()));

			Mist.instance.getFontRenderer().drawString(m.getName(), sr.getScaledWidth() - m.arrayAnimationX.getOutput(), m.arrayAnimationY.getOutput(), color.getRGB(), true);

			Mist.instance.getFontRenderer().drawString(m.getSuffix(), sr.getScaledWidth() - m.arrayAnimationX.getOutput() + Mist.instance.getFontRenderer().getStringWidth(m.getName() + " "), m.arrayAnimationY.getOutput(), -1, true);


			m.arrayAnimationY.setend(count * Mist.instance.getFontRenderer().getFontHeight());
			count++;
		}

		double t;
		t = Math.sin((System.currentTimeMillis() * (speed / 1000)) + ((count - 0.5) * (diff / 10))) * 0.5 + 0.5;
		Color color1 = new Color((int) (color_a.getRed() * t + (1 - t) * color_b.getRed()), (int) (color_a.getGreen() * t + (1 - t) * color_b.getGreen()), (int) (color_a.getBlue() * t + (1 - t) * color_b.getBlue()));

		t = Math.sin((System.currentTimeMillis() * (speed / 1000))
				+ ((count + 0.5) * (diff / 10))) * 0.5 + 0.5;
		Color color2 = new Color((int) (color_a.getRed() * t + (1 - t) * color_b.getRed()), (int) (color_a.getGreen() * t + (1 - t) * color_b.getGreen()), (int) (color_a.getBlue() * t + (1 - t) * color_b.getBlue()));

		mc.ingameGUI.drawGradientRect(sr.getScaledWidth() - 1, 0, sr.getScaledWidth(), count * Mist.instance.getFontRenderer().getFontHeight(), color1.getRGB(), color2.getRGB());
//todo: addd anim to rect

	}

	public class ModuleComparator implements Comparator<Module> {
		public int compare(Module o1, Module o2) {
			return Integer.compare(
					Mist.instance.getFontRenderer().getStringWidth(
							o2.getName() +
									(o2.getSuffix().isEmpty() ? "" :
											" " + o2.getSuffix())
					),

					Mist.instance.getFontRenderer().getStringWidth(
							o1.getName() +
									(o1.getSuffix().isEmpty() ? "" :
											" " + o1.getSuffix())
					)
			);
		}
	}

/*    void drawList(ScaledResolution sr) {

        int count = 0;
        for(Module m : Twen.instance.getModuleManager().getModules()) {
            if(!m.isEnabled()) {
                m.x = sr.getScaledWidth();
            }
            if(!m.isEnabled())
                continue;

            String suffix = m.getSuffix() == "" ? "" : " " + m.getSuffix();

            double tx = sr.getScaledWidth() - Twen.instance.getFontRenderer().getStringWidth(m.getName() + suffix) - 5;

            double ty = 5 + count * (5 + Twen.instance.getFontRenderer().getFontHeight());
            if(m.x > tx) {
                m.x -= 1;
            }
            else {
                m.x = tx;
            }

            if(m.y < ty) {
                m.y += 0.5;
            }
            else {
                m.y = ty;
            }


            Twen.instance.getFontRenderer().drawString(m.getName(),
                    (int) m.x,
                    (int) m.y,
                    -1, true);

            Twen.instance.getFontRenderer().drawString(suffix,
                    (int) m.x + Twen.instance.getFontRenderer().getStringWidth(m.getName()),
                    (int) m.y,
                    Color.GRAY.getRGB(), true);

            Gui.drawRect(m.x - 2, m.y - 5, m.x - 3, Twen.instance.getFontRenderer().getFontHeight() + m.y, Colours.getRainbow((float) (4 * ((NumberSetting)Twen.settingsManager.getSettings("Rainbow", "HUD")).value), count * 100));

            ArrayList<Module> mods = new ArrayList<Module>();

            for(Module mod : Twen.instance.getModuleManager().getModules()) {
                if(mod.isEnabled() && mod.getName() != "HUD")
                    mods.add(mod);
            }

            int extra = sr.getScaledWidth();
            if(mods.indexOf(m) + 1 < mods.size())
                extra = (int) (mods.get(mods.indexOf(m) + 1).x - 3);
            Gui.drawRect(m.x - 3, Twen.instance.getFontRenderer().getFontHeight() + m.y, extra, Twen.instance.getFontRenderer().getFontHeight() + m.y + 1, Colours.getRainbow((float) (4 * ((NumberSetting)Twen.settingsManager.getSettings("Rainbow", "HUD")).value), (count * 100 + 50)));

            if(mods.size() != 0 && ((BoolSetting)Twen.settingsManager.getSettings("Extra Lines", "HUD")).enabledfalse) {
                Gui.drawRect(sr.getScaledWidth() - Twen.instance.getFontRenderer().getStringWidth(mods.get(0).getName()) - 7, 0, sr.getScaledWidth(), 1, 0xffc51a71);
                Gui.drawRect(sr.getScaledWidth() - 1, 0, sr.getScaledWidth(), mods.size() * (Twen.instance.getFontRenderer().getFontHeight() + 5), 0xffc51a71);
            }

            mods.clear();
            count++;
        }
    }*/
}
