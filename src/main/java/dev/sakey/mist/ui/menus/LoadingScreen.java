package dev.sakey.mist.ui.menus;

import dev.sakey.mist.Mist;
import dev.sakey.mist.utils.render.ShaderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class LoadingScreen {

        public static void draw(TextureManager tm) {
                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
                int scale = sr.getScaleFactor();

                Framebuffer framebuffer = new Framebuffer(
                        sr.getScaledWidth() * scale,
                        sr.getScaledHeight() * scale,
                        true);
                framebuffer.bindFramebuffer(false);

                GlStateManager.matrixMode(GL11.GL_PROJECTION);
                GlStateManager.loadIdentity();
                GlStateManager.ortho(
                        0,
                        sr.getScaledWidth(),
                        sr.getScaledHeight(),
                        0,
                        1000,
                        3000
                );
                GlStateManager.matrixMode(GL11.GL_MODELVIEW);
                GlStateManager.loadIdentity();
                GlStateManager.translate(0, 0, -2000);
                GlStateManager.disableLighting();
                GlStateManager.disableFog();
                GlStateManager.disableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.resetColor();
                GlStateManager.color(1, 1, 1, 1);

/*
        Gui.drawScaledCustomSizeModalRect(
                0, 0, 0, 0,
                1920, 1080,
                sr.getScaledWidth(), sr.getScaledHeight(),
                1920, 1080
        );
*/


                try {
                        ShaderUtils.drawBackgroundShader("loading");
                } catch (IOException e) {
                        Mist.instance.getLogger().error("Failed to load background shaders! Falling back to Minecraft background");
                }


                framebuffer.unbindFramebuffer();
                framebuffer.framebufferRender(
                        sr.getScaledWidth() * scale,
                        sr.getScaledHeight() * scale
                );

                GlStateManager.enableAlpha();
                GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);

                Minecraft.getMinecraft().updateDisplay();


		/*
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		GL11.glClearColor(0, 0, 0, 1);
		try {
			Mist.instance.getFontRenderer(Mist.Constants.largeFontSize).drawString(
					"Loading Mist",
					sr.getScaledWidth() / 2 - Mist.instance.getFontRenderer().getStringWidth("Loading Mist") / 2,
					sr.getScaledWidth() / 2 - Mist.instance.getFontRenderer().getFontHeight() / 2,
					Colours.white(),
					false
			);
		} catch(Exception ignored) {}*/
        }

}
