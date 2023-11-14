package dev.sakey.mist.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.IOException;

public class ShaderUtils {

	private static long initTime = System.currentTimeMillis();

	public static void use(String shader) throws IOException {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

		GlStateManager.disableCull();

		//shaders[shader].useShader(sr.getScaledWidth(), sr.getScaledHeight(), mouseX, mouseY, (System.currentTimeMillis() - initTime) / 1000f);


		new Shader(shader).useShader(sr.getScaledWidth(), sr.getScaledHeight(), 0, 0, (System.currentTimeMillis() - initTime) / 1000f);
	}

	public static void stop() {
		GL20.glUseProgram(0);

		GlStateManager.disableColorLogic();
	}

	public static void draw() {
		GL11.glBegin(GL11.GL_QUADS);

		GL11.glVertex2f(-1f, -1f);
		GL11.glVertex2f(-1f, 1f);
		GL11.glVertex2f(1f, 1f);
		GL11.glVertex2f(1f, -1f);

		GL11.glEnd();
	}


	public static void drawBackgroundShader(String shader) throws IOException {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

		GlStateManager.disableCull();

		//shaders[shader].useShader(sr.getScaledWidth(), sr.getScaledHeight(), mouseX, mouseY, (System.currentTimeMillis() - initTime) / 1000f);


		new Shader(shader).useShader(sr.getScaledWidth(), sr.getScaledHeight(), 0, 0, (System.currentTimeMillis() - initTime) / 1000f);

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glVertex2f(-1f, -1f);
		GL11.glVertex2f(-1f, 1f);
		GL11.glVertex2f(1f, 1f);
		GL11.glVertex2f(1f, -1f);

		GL11.glEnd();

		// Unbind shader
		GL20.glUseProgram(0);

		GlStateManager.disableColorLogic();
	}

}
