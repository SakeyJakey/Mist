package dev.sakey.mist.ui.menus;

import dev.sakey.mist.Mist;
import dev.sakey.mist.utils.render.RenderUtils;
import dev.sakey.mist.utils.render.ShaderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class LoadingScreen {

        private static final int MAX = 7;
        private static int PROGRESS = 0;
        private static String CURRENT = "";
        private static ResourceLocation splash;

        public static void update() {
                if(Minecraft.getMinecraft() == null || Minecraft.getMinecraft().getLanguageManager() == null) return;

                drawSplash(Minecraft.getMinecraft().getTextureManager());
        }

        public static void setProgress(int progress, String text) {
                PROGRESS = progress;
                CURRENT = text;
                update();
        }

        public static void drawSplash(TextureManager tm) {
                Minecraft mc = Minecraft.getMinecraft();

                ScaledResolution sr = new ScaledResolution(mc);
                int scaleFactor = sr.getScaleFactor();

                Framebuffer fb = new Framebuffer(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor, true);
                fb.bindFramebuffer(false);

                GlStateManager.matrixMode(GL11.GL_PROJECTION);
                GlStateManager.loadIdentity();
                GlStateManager.ortho(0d, sr.getScaledWidth_double(), sr.getScaledHeight_double(), 0d, 1000d, 3000d);
                GlStateManager.matrixMode(GL11.GL_MODELVIEW);
                GlStateManager.loadIdentity();
                GlStateManager.translate(0f, 0f, -2000f);
                GlStateManager.disableLighting();
                GlStateManager.disableFog();
                GlStateManager.disableDepth();
                GlStateManager.enableTexture2D();

                if(splash == null) {
                        splash = new ResourceLocation("Mist/splash.png");
                }

                tm.bindTexture(splash);

                GlStateManager.resetColor();
                GlStateManager.color(1f, 1f, 1f, 1f);

                Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, 1688, 710, sr.getScaledWidth(), sr.getScaledHeight(), 1688, 710);
                drawProgress();
                fb.unbindFramebuffer();
                fb.framebufferRender(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor);

                GlStateManager.enableAlpha();
                GlStateManager.alphaFunc(516, 0.1f);

                Minecraft.getMinecraft().updateDisplay();
        }

        public static void drawProgress() {
                if(Minecraft.getMinecraft().gameSettings == null || Minecraft.getMinecraft().getTextureManager() == null) return;

                if(!Mist.instance.isLoadedFontRenderers()) {
                      Mist.instance.setupFonts();
                }

                Minecraft mc = Minecraft.getMinecraft();

                ScaledResolution sr = new ScaledResolution(mc);

                double progress = PROGRESS;
                double calc = (progress / MAX) * (sr.getScaledWidth() - 10);

                GlStateManager.resetColor();
                resetTextureState();

                Mist.instance.getFontRenderer(20).drawString(CURRENT, 20, sr.getScaledHeight() - 25, 0xffffffff, false);

                String step = PROGRESS + "/" + MAX;
                Mist.instance.getFontRenderer(20).drawString(step, sr.getScaledWidth() - Mist.instance.getFontRenderer(20).getStringWidth(step), sr.getScaledHeight() - 25, 0xffffffff, false);

                GlStateManager.resetColor();
                resetTextureState();

                RenderUtils.drawRoundedRect(5, sr.getScaledHeight() - 5 - 3, calc, sr.getScaledHeight() - 5, 2, 0xffffffff);

        }

        public static void resetTextureState() {
                GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName = -1;
        }
}
