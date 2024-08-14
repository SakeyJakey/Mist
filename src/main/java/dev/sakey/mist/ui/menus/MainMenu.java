/*
package dev.sakey.twen.ui.menus;

import dev.sakey.twen.Twen;
import dev.sakey.twen.utils.client.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class MainMenu extends GuiScreen {

    ScaledResolution sr;

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        sr = new ScaledResolution(mc);
        drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0xff0000ff, 0xffff00ff);

        int w = 75, h = 100;
        RenderUtils.drawRoundedRect(sr.getScaledWidth() / 2 - w / 2, sr.getScaledHeight() / 2 - h / 2, sr.getScaledWidth() / 2 + w / 2, sr.getScaledHeight() / 2 + h / 2, 20, -1);
        w -= 5;
        h -= 5;
        RenderUtils.drawRoundedRect(sr.getScaledWidth() / 2 - w / 2, sr.getScaledHeight() / 2 - h / 2, sr.getScaledWidth() / 2 + w / 2, sr.getScaledHeight() / 2 + h / 2, 20, 0x000000ff);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    public void initGui() {
        super.initGui();
    }
}
*/




package dev.sakey.mist.ui.menus;

import dev.sakey.mist.Mist;

import java.io.IOException;
import java.util.Random;

import dev.sakey.mist.utils.render.ColourUtil;
import dev.sakey.mist.utils.render.RenderUtils;
import dev.sakey.mist.utils.client.animation.Animation;
import dev.sakey.mist.utils.client.animation.impl.*;
import dev.sakey.mist.utils.render.ShaderUtils;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;


public class MainMenu extends GuiScreen
{
    Animation menuPanelAnimation = new EaseOut(4000, 2); //new Elastic(1000, 2, 5, 2, false);
    Animation menuPanelAnimation2 = new EaseInOut(4000, 2);

    private String[] shaders;
    int shader = 0;

    private long initTime = System.currentTimeMillis(); // Initialize as a failsafe

    public MainMenu(){
        shaders = new String[] {
                "bg1",
                "bg2",
                "bg3",
                "bg4",
                "bg5",
                "bg6",
                "bg7",
                "bg8",
                "bg9",
                "bg10"
        };

        initTime = System.currentTimeMillis();
    }

    int lastScreenW = 0,
            lastScreenH = 0;

    public void initGui() {
        if(lastScreenW == mc.displayWidth && lastScreenH == mc.displayHeight)
            shader = MathHelper.getRandomIntegerInRange(new Random(), 0, shaders.length - 1);

        lastScreenW = mc.displayWidth;
        lastScreenH = mc.displayHeight;

        super.initGui();
    }

    int count;

    String[] buttons = { "Play", "Solo", "Alts", "Settings", "Exit" };

    private boolean isInside(int mouseX, int mouseY, double x, double y, double x2, double y2) {
        return (mouseX > x && mouseX < x2) && (mouseY > y && mouseY < y2);
    }

    int mX = 0, mY = 0;

    ScaledResolution sr;


    int w = 100;
    int h = 150;

    String hovered = null;

    public void updateScreen() {
        sr = new ScaledResolution(mc);
        count = 1;
        boolean done = false;
        for(String button : buttons){
            if(
                    isInside(mX, mY,
                    sr.getScaledWidth() / 2 - w / 2,
                    sr.getScaledHeight() / menuPanelAnimation.getOutput() - h / 2 + ((count - 0.5) * (13 + Mist.instance.getFontRenderer().getFontHeight())) + h / 12,
                    sr.getScaledWidth() / 2 + w / 2,
                    sr.getScaledHeight() / menuPanelAnimation.getOutput() - h / 2 + ((count + 0.5) * (13 + Mist.instance.getFontRenderer().getFontHeight())) + h / 12
                    )){
                hovered = button;
                done = true;
            }
            count++;
        }
        if(!done) hovered = null;
        super.updateScreen();
    }


    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        sr = new ScaledResolution(mc);
        super.drawDefaultBackground(); //just in case

        // Background

        try {
            ShaderUtils.drawBackgroundShader(shaders[shader]);
        } catch (IOException e) {
            Mist.instance.getLogger().error("Failed to load background shaders! Falling back to Minecraft background");
        }

        mX = mouseX;
        mY = mouseY;

        w = 100;
        h = Mist.instance.getFontRenderer().getFontHeight() * buttons.length * 3 + 13;


        Mist.instance.getFontRenderer().drawString("made by sakey",
                sr.getScaledWidth() - Mist.instance.getFontRenderer().getStringWidth("made by sakey "),
                sr.getScaledHeight() - Mist.instance.getFontRenderer().getFontHeight(),
                ColourUtil.white(), false);

        // Changelog

        Mist.instance.getFontRenderer().drawString("§kChangelog", 15, 15, ColourUtil.white(), false);

        String[] changes = {
                "[+] Scaffold",
                "[+] New Event System"
        };

        String longest = "";
        int changelogCount = 1;
        for(String change : changes) {
            if(Mist.instance.getFontRenderer().getStringWidth(longest) < Mist.instance.getFontRenderer().getStringWidth(change))
                longest = change;

            Mist.instance.getFontRenderer().drawString(change, 15, 15 + Mist.instance.getFontRenderer().getFontHeight() * changelogCount, ColourUtil.white(), false);
            changelogCount++;
        }


        // Title and menu

        Mist.instance.getFontRenderer(Mist.Constants.largeFontSize).drawRainbowWaveDanceString(Mist.instance.name, sr.getScaledWidth() / 2 - Mist.instance.getFontRenderer(Mist.Constants.largeFontSize).getStringWidth("Mist") / 2, sr.getScaledHeight() / menuPanelAnimation.getOutput() - h / 2 - 30, 4, 150, 5, 20, true);

        RenderUtils.drawRoundedRect(sr.getScaledWidth() / 2 - w / 2, sr.getScaledHeight() / menuPanelAnimation.getOutput() - h / 2, sr.getScaledWidth() / 2 + w / 2, sr.getScaledHeight() / menuPanelAnimation.getOutput() + h / 2, 10, -1); //Colours.getRainbow(2));
        w-=2; h-=2;
        RenderUtils.drawRoundedRect(sr.getScaledWidth() / 2 - w / 2, sr.getScaledHeight() / menuPanelAnimation.getOutput() - h / 2, sr.getScaledWidth() / 2 + w / 2, sr.getScaledHeight() / menuPanelAnimation.getOutput() + h / 2, 8, ColourUtil.grey());
        w-=1; h-=1;
        RenderUtils.drawRoundedRect(sr.getScaledWidth() / 2 - w / 2, sr.getScaledHeight() / menuPanelAnimation.getOutput() - h / 2, sr.getScaledWidth() / 2 + w / 2, sr.getScaledHeight() / menuPanelAnimation.getOutput() + h / 2, 7, 0x40000000);



        count = 1;
        for(String button : buttons) {
            int colour;
            if(button.equals(hovered)) {
                colour = ColourUtil.getRainbow(2);

/*                RenderUtils.drawRoundedRect(sr.getScaledWidth() / 2 - w / 2,
                        sr.getScaledHeight() / menuPanelAnimation.getOutput() - h / 2 + (count * (13 + Twen.instance.getFontRenderer().getFontHeight())),
                        sr.getScaledWidth() / 2 + w / 2,
                        sr.getScaledHeight() / menuPanelAnimation.getOutput() - h / 2 + ((count + 1) * (13 + Twen.instance.getFontRenderer().getFontHeight())),
                8, -1);*/
            }
            else colour = ColourUtil.white();


            Mist.instance.getFontRenderer().drawString(button, sr.getScaledWidth() / 2 - 2, sr.getScaledHeight() / menuPanelAnimation.getOutput() - h / 2 + (count * (13 + Mist.instance.getFontRenderer().getFontHeight())) + h / 12, colour, false, true);
            count++;
        }

        // Changelog outline
        RenderUtils.drawOutlineRoundedRect(10, 10, 20 + Mist.instance.getFontRenderer().getStringWidth(longest), 10 + Mist.instance.getFontRenderer().getFontHeight() * (changelogCount + 1), 10, 2, ColourUtil.getRainbow(3));

        // wierd order to prevent rounded stuff breaking everything         before now its fixed

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(hovered == null) { cycleShader(); return; }
        switch (hovered) {
            case "Play":
                mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case "Solo":
                mc.displayGuiScreen(new GuiSelectWorld(this));
                break;
            case "Alts":
                mc.displayGuiScreen(new AltManagerGui(this));
                break;
            case "Settings":
                mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                break;
            case "Exit":
                mc.shutdown();
                break;
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    void cycleShader(){
        shader++;
        if(shader >= shaders.length) shader = 0;
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if(keyCode != Keyboard.KEY_ESCAPE) // prevent closing it
            super.keyTyped(typedChar, keyCode);
    }
}