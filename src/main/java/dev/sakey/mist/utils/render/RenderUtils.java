package dev.sakey.mist.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class RenderUtils {

	public static void drawRect(double x, double y, double x1, double y1, int color) {
		Gui.drawRect(x, y, x1, y1, color);
    }

	public static void drawRoundedRect(double x, double y, double x1, double y1, double radius, int color) {
		drawRoundedRect(x, y, x1, y1, radius, true, true, true, true, color);
	}

	public static void drawOutlineRect(double x, double y, double x1, double y1, float width, int color) {
		if (x < x1)
		{
			double i = x;
			x = x1;
			x1 = i;
		}

		if (y < y1)
		{
			double j = y;
			y = y1;
			y1 = j;
		}

		float f3 = (float)(color >> 24 & 255) / 255.0F;
		float f = (float)(color >> 16 & 255) / 255.0F;
		float f1 = (float)(color >> 8 & 255) / 255.0F;
		float f2 = (float)(color & 255) / 255.0F;
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(f, f1, f2, f3);
		GL11.glLineWidth(width);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex2d(x, y1);
		GL11.glVertex2d(x1, y1);
		GL11.glVertex2d(x1, y);
		GL11.glVertex2d(x, y);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glLineWidth(1);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void drawRoundedRect(double x, double y, double x1, double y1, double radius, boolean tl, boolean tr, boolean bl, boolean br, int color) {
		// makes a pill instead of glitching

		if(radius > Math.abs(x - x1) / 2)
			radius = Math.abs(x - x1) / 2;
		if(radius > Math.abs(y - y1) / 2)
			radius = Math.abs(y - y1) / 2;

		//real stuff

		float var11 = (float)(color >> 24 & 255) / 255.0F;
		float var6 = (float)(color >> 16 & 255) / 255.0F;
		float var7 = (float)(color >> 8 & 255) / 255.0F;
		float var8 = (float)(color & 255) / 255.0F;

		GL11.glPushAttrib(0);
		GL11.glScaled(0.5D, 0.5D, 0.5D);
		x *= 2.0D;
		y *= 2.0D;
		x1 *= 2.0D;
		y1 *= 2.0D;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GlStateManager.color(var6, var7, var8, var11);
		GL11.glEnable(2848);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		int i;
		if(tl)
			for (i = 0; i <= 90; i += 3)
				GL11.glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
		else
			GL11.glVertex2d(x, y);

		if(bl)
			for (i = 90; i <= 180; i += 3)
				GL11.glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
		else
			GL11.glVertex2d(x, y1);

		if(br)
			for (i = 0; i <= 90; i += 3)
				GL11.glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius);
		else
			GL11.glVertex2d(x1, y1);
		if(tr)
			for (i = 90; i <= 180; i += 3)
				GL11.glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius);
		else
			GL11.glVertex2d(x1, y);


		GL11.glEnd();
		GL11.glLineWidth(1);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
		GL11.glEnable(3553);
		GL11.glScaled(2.0D, 2.0D, 2.0D);
		GL11.glPopAttrib();

		GlStateManager.disableBlend(); // that was causing the problem the whole time. im angry but at peace
	}

	public static void drawOutlineRoundedRect(double x, double y, double x1, double y1, double radius, float width, int color) {
		// makes a pill instead of glitching

		if(radius > Math.abs(x - x1) / 2)
			radius = Math.abs(x - x1) / 2;
		if(radius > Math.abs(y - y1) / 2)
			radius = Math.abs(y - y1) / 2;

		//real stuff

		float var11 = (float)(color >> 24 & 255) / 255.0F;
		float var6 = (float)(color >> 16 & 255) / 255.0F;
		float var7 = (float)(color >> 8 & 255) / 255.0F;
		float var8 = (float)(color & 255) / 255.0F;

		GL11.glPushAttrib(0);
		GL11.glScaled(0.5D, 0.5D, 0.5D);
		x *= 2.0D;
		y *= 2.0D;
		x1 *= 2.0D;
		y1 *= 2.0D;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GlStateManager.color(var6, var7, var8, var11);
		GL11.glEnable(2848);
		GL11.glLineWidth(width);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		int i;
			for (i = 0; i <= 90; i += 3)
				GL11.glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);

			for (i = 90; i <= 180; i += 3)
				GL11.glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);


			for (i = 0; i <= 90; i += 3)
				GL11.glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius);

			for (i = 90; i <= 180; i += 3)
				GL11.glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius);


		GL11.glEnd();
		GL11.glLineWidth(1);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
		GL11.glEnable(3553);
		GL11.glScaled(2.0D, 2.0D, 2.0D);
		GL11.glPopAttrib();

		GlStateManager.disableBlend(); // that was causing the problem the whole time. im angry but at peace
	}


	public static boolean isInside(double x, double y, double miX, double miY, double maX, double maY) {
		return x > miX && y > miY && x < maX && y < maY;
	}

    public static void drawGradientOutlineRoundedRect(double x, double y, double x1, double y1, double radius, float width, int color) {

        // makes a pill instead of glitching

        if(radius > Math.abs(x - x1) / 2)
            radius = Math.abs(x - x1) / 2;
        if(radius > Math.abs(y - y1) / 2)
            radius = Math.abs(y - y1) / 2;

        //real stuff

        float var11 = (float)(color >> 24 & 255) / 255.0F;
        float var6 = (float)(color >> 16 & 255) / 255.0F;
        float var7 = (float)(color >> 8 & 255) / 255.0F;
        float var8 = (float)(color & 255) / 255.0F;

        GL11.glPushAttrib(0);
        GL11.glScaled(0.5D, 0.5D, 0.5D);
        x *= 2.0D;
        y *= 2.0D;
        x1 *= 2.0D;
        y1 *= 2.0D;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GlStateManager.color(var6, var7, var8, var11);
        GL11.glEnable(2848);
        GL11.glLineWidth(width);

        WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();
        wr.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_COLOR);
        int i;
        for (i = 0; i <= 90; i += 3)
            wr.pos(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D, 0).color(1, 0, 0, 1).endVertex();

        for (i = 90; i <= 180; i += 3)
            wr.pos(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D, 0).color(0, 1, 0, 1).endVertex();

        for (i = 0; i <= 90; i += 3)
            wr.pos(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius, 0).color(1, 0, 1, 1).endVertex();

        for (i = 90; i <= 180; i += 3)
            wr.pos(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius, 0).color(0, 0, 1, 1).endVertex();
        wr.finishDrawing();
        GL11.glLineWidth(1);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glScaled(2.0D, 2.0D, 2.0D);
        GL11.glPopAttrib();
    }

    public static void drawEntityOnScreen(double p_147046_0_, double p_147046_1_, int p_147046_2_, float p_147046_3_, float p_147046_4_, EntityLivingBase p_147046_5_)
    {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_147046_0_, (float)p_147046_1_, 50.0F);
        GlStateManager.scale((float)(-p_147046_2_), (float)p_147046_2_, (float)p_147046_2_);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float var6 = p_147046_5_.renderYawOffset;
        float var7 = p_147046_5_.rotationYaw;
        float var8 = p_147046_5_.rotationPitch;
        float var9 = p_147046_5_.prevRotationYawHead;
        float var10 = p_147046_5_.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float)Math.atan((double)(p_147046_4_ / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        p_147046_5_.renderYawOffset = (float)Math.atan((double)(p_147046_3_ / 40.0F)) * 20.0F;
        p_147046_5_.rotationYaw = (float)Math.atan((double)(p_147046_3_ / 40.0F)) * 40.0F;
        p_147046_5_.rotationPitch = -((float)Math.atan((double)(p_147046_4_ / 40.0F))) * 20.0F;
        p_147046_5_.rotationYawHead = p_147046_5_.rotationYaw;
        p_147046_5_.prevRotationYawHead = p_147046_5_.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager var11 = Minecraft.getMinecraft().getRenderManager();
        var11.setPlayerViewY(180.0F);
        var11.setRenderShadow(false);
        var11.renderEntityWithPosYaw(p_147046_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        var11.setRenderShadow(true);
        p_147046_5_.renderYawOffset = var6;
        p_147046_5_.rotationYaw = var7;
        p_147046_5_.rotationPitch = var8;
        p_147046_5_.prevRotationYawHead = var9;
        p_147046_5_.rotationYawHead = var10;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }


	public static void draw2DPolygonOutline(double x, double y, double radius, int sides, int color) {
		if (sides < 3) return;
		float a = (color >> 24 & 0xFF) / 255.0F;
		float r = (color >> 16 & 0xFF) / 255.0F;
		float g = (color >> 8 & 0xFF) / 255.0F;
		float b = (color & 0xFF) / 255.0F;

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();

		GlStateManager.enableBlend();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GL11.glColor4f(r, g, b, a);

		worldrenderer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
		for (int i = 0; i < sides; i++) {
			double angle = (Math.PI * 2 * i / sides) + Math.toRadians(180);
			worldrenderer.pos((float) (x + Math.sin(angle) * radius), (float) (y + Math.cos(angle) * radius), 0).endVertex();
		}
		tessellator.draw();

		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GL11.glColor4f(1, 1, 1, 1);
		drawRect(0, 0, 0, 0, 0x00000000); // fixes for some reason
	}

	public static void draw2DPolygon(double x, double y, double radius, int sides, int color) {
		if (sides < 3) return;
		float a = (color >> 24 & 0xFF) / 255.0F;
		float r = (color >> 16 & 0xFF) / 255.0F;
		float g = (color >> 8 & 0xFF) / 255.0F;
		float b = (color & 0xFF) / 255.0F;

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();

		GlStateManager.enableBlend();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GL11.glColor4f(r, g, b, a);

		worldrenderer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);
		for (int i = 0; i < sides; i++) {
			double angle = (Math.PI * 2 * i / sides) + Math.toRadians(180);
			worldrenderer.pos((float) (x + Math.sin(angle) * radius), (float) (y + Math.cos(angle) * radius), 0).endVertex();
		}
		tessellator.draw();

		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GL11.glColor4f(1, 1, 1, 1);
		drawRect(0, 0, 0, 0, 0x00000000); // fixes for some reason todo
	}
}
