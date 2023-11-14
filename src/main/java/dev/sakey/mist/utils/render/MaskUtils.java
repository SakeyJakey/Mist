package dev.sakey.mist.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.EXTPackedDepthStencil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL41;

public class MaskUtils {

	public static class UI {
		public static void beginDrawMask() {
			GL41.glClearDepthf(1.0f);
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glColorMask(false, false, false, false);
			GL11.glDepthFunc(GL11.GL_LESS);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(true);
		}

		public static void stopDrawMask() {
			GL11.glDisable(GL11.GL_DEPTH_TEST);
		}

		public static void continueDrawMask() {
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}

		public static void beginDrawContent() {
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glColorMask(true, true, true, true);
			GL11.glDepthMask(true);
			GL11.glDepthFunc(GL11.GL_EQUAL);
		}

		public static void endMask() {
			GL41.glClearDepthf(1.0f);
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
			GL11.glDepthMask(false);
		}
	}

	public static class Stencil {

		private static boolean isMasking = false;

		public static boolean isMasking() {
			return isMasking;
		}

		public static void checkSetupFBO(Framebuffer framebuffer) {
			if (framebuffer != null) {
				if (framebuffer.depthBuffer > -1) {
					setupFBO(framebuffer);
					framebuffer.depthBuffer = -1;
				}
			}
		}

		public static void setupFBO(Framebuffer framebuffer) {
			EXTFramebufferObject.glDeleteRenderbuffersEXT(framebuffer.depthBuffer);
			final int stencilDepthBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();
			EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencilDepthBufferID);
			EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, EXTPackedDepthStencil.GL_DEPTH_STENCIL_EXT, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
			EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_STENCIL_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencilDepthBufferID);
			EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencilDepthBufferID);
		}

		public static void beginDrawMask() {
			isMasking = true;
			Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(false);
			checkSetupFBO(Minecraft.getMinecraft().getFramebuffer());
			GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
			GL11.glColorMask(false, false, false, false);
			GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 1);
			GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
			GL11.glEnable(GL11.GL_STENCIL_TEST);
		}

		public static void pauseMask() {
			GL11.glDisable(GL11.GL_STENCIL_TEST);
		}

		public static void continueMask() {
			GL11.glEnable(GL11.GL_STENCIL_TEST);
		}

		public static void beginDrawContent() {
			GL11.glEnable(GL11.GL_STENCIL_TEST);
			GL11.glColorMask(true, true, true, true);
			GL11.glStencilFunc(GL11.GL_EQUAL, 1, 1);
			GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
		}

		public static void endMask() {
			isMasking = false;
			GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
			GL11.glDisable(GL11.GL_STENCIL_TEST);
		}
	}
}
