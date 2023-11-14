package dev.sakey.mist.utils.render;

import dev.sakey.mist.Mist;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.IOException;

public class Blur {

	Shader blurShader;

	Blur() {
		try {
			blurShader = new Shader("blur");
		} catch (IOException e) {
			Mist.instance.getLogger().error("Failed to load blur shader!");
		}
	}

	void test() {

		Minecraft mc = Minecraft.getMinecraft();

        MaskUtils.Stencil.beginDrawMask();

        Gui.drawRect(0, 0, 100, 100, -1);

        MaskUtils.Stencil.beginDrawContent();



        Framebuffer fb = new Framebuffer(mc.displayWidth, mc.displayHeight, false);



        fb.bindFramebuffer(false);

        blurShader.useShader(mc.displayWidth, mc.displayHeight, 0, 0, 0);

        GL20.glUniform1i(blurShader.getUniformByName("texture"), 0);
        GL20.glUniform2f(blurShader.getUniformByName("texelSize"), (float) 1 / mc.displayWidth, (float) 1 / mc.displayHeight);
        GL20.glUniform1f(blurShader.getUniformByName("radius"), 30);
        GL20.glUniform2f(blurShader.getUniformByName("direction"), 1, 0);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.getFramebuffer().framebufferTexture);
        ShaderUtils.draw();

        blurShader.stop();

        fb.unbindFramebuffer();

        mc.getFramebuffer().bindFramebuffer(false);

        blurShader.useShader(mc.displayWidth, mc.displayHeight, 0, 0, 0);

        GL20.glUniform1i(blurShader.getUniformByName("texture"), 0);
        GL20.glUniform2f(blurShader.getUniformByName("texelSize"), (float) 1 / mc.displayWidth, (float) 1 / mc.displayHeight);
        GL20.glUniform1f(blurShader.getUniformByName("radius"), 30);
        GL20.glUniform2f(blurShader.getUniformByName("direction"), 0, 1);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fb.framebufferTexture);
        //Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, sr.getScaledWidth(), sr.getScaledHeight(), sr.getScaledWidth(), sr.getScaledHeight());
        ShaderUtils.draw();

        blurShader.stop();
        fb.deleteFramebuffer();
        fb = null;

        MaskUtils.Stencil.endMask();
	}

}
