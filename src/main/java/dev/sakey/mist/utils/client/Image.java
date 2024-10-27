package dev.sakey.mist.utils.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Image {

	ResourceLocation file;
	int w, h;

	public Image(String file, int width, int height) {
		this.file = new ResourceLocation(file);
		this.w = width;
		this.h = height;
	}

	public void drawImageTopLeft(int offsetX, int offsetY, double scaleX, double scaleY) {
		GL11.glClearColor(1, 1, 1, 1);
		Minecraft.getMinecraft().getTextureManager().bindTexture(file);
		Gui.drawModalRectWithCustomSizedTexture(offsetX, offsetY, 0, 0, (int) (w * scaleX), (int) (h * scaleY), w * (float)scaleX, h * (float)scaleY);
	}

	public void drawImageTopLeft(int offsetX, int offsetY) {
		GL11.glClearColor(1, 1, 1, 1);
		Minecraft.getMinecraft().getTextureManager().bindTexture(file);
		Gui.drawModalRectWithCustomSizedTexture(offsetX, offsetY, 0, 0, w, h, w, h);
	}

	public void drawImageTopLeft() {
		GL11.glClearColor(1, 1, 1, 1);
		Minecraft.getMinecraft().getTextureManager().bindTexture(file);
		Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, w, h, w, h);
	}

}
