package dev.sakey.mist.ui.menus;

import dev.sakey.mist.Mist;
import dev.sakey.mist.utils.client.animation.Animation;
import dev.sakey.mist.utils.client.animation.impl.EaseOut;
import dev.sakey.mist.utils.client.font.GlyphPageFontRenderer;
import dev.sakey.mist.utils.render.ColourUtil;
import dev.sakey.mist.utils.render.RenderUtils;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class Intro extends GuiScreen
{

    int w = 1236,
    h = 2000;
    int sw, sh;
    Animation anim;

    public Intro(){
        anim = new EaseOut(2000, 1, Animation.Direction.BACKWARD);
    }


    public void initGui() {
        super.initGui();
        mc.displayGuiScreen(new MainMenu());
    }

    ScaledResolution sr;

    public void updateScreen() {
        super.updateScreen();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        sr = new ScaledResolution(mc);

        sh = sr.getScaledHeight();
        sw = sh * (h / w);

        GlStateManager.enableAlpha();

        RenderUtils.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), ColourUtil.white());

        mc.getTextureManager().bindTexture(new ResourceLocation("Twen/Hentai.png"));
        Gui.drawModalRectWithCustomSizedTexture((int) (sr.getScaledWidth() - sw + (sw * anim.getOutput())), sr.getScaledHeight() - sh, 0, 0, w, h, sw, sh);

        GlyphPageFontRenderer fr = Mist.instance.getFontRenderer(Mist.Constants.maxFontSize);
        fr.drawRainbowWaveDanceString(Mist.instance.name, sr.getScaledWidth() / 2 - fr.getStringWidth(Mist.instance.name), sr.getScaledHeight() / 2 - fr.getFontHeight(), 4, 0, 5, 10, true);

    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if(keyCode != Keyboard.KEY_ESCAPE)
            super.keyTyped(typedChar, keyCode);
    }
}