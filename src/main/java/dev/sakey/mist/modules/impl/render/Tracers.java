package dev.sakey.mist.modules.impl.render;

import dev.sakey.mist.Mist;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.render.EventRenderHUD;
import dev.sakey.mist.events.impl.render.EventRenderWorld;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Tracers extends Module {

    @ModuleInfo(name = "Tracers", description = "Draws a line from the centre of your screen to an entity", category = Category.RENDER)
    public Tracers() { }

    protected void onEnable() { Mist.instance.getEventManager().registerEventHandler(EventRenderWorld.class, render); }

    protected void onDisable() { Mist.instance.getEventManager().unregisterEventHandler(render); }

    EventHandler<EventRenderWorld> render = event -> {
        for (Entity e :
                mc.theWorld.getLoadedEntityList()) {

			if(e == mc.thePlayer) continue;
			if(e.getDistanceToEntity(mc.thePlayer) > 100) continue;

            //todo: interpolate pos
            double x = e.posX - mc.getRenderManager().viewerPosX;
            double y = e.posY - mc.getRenderManager().viewerPosY;
            double z = e.posZ - mc.getRenderManager().viewerPosZ;

			GL11.glPushMatrix();

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);

			GL11.glColor4f(1, 1, 1, 1);


			// Counteract viewbobbing

/*			if (mc.getRenderViewEntity() instanceof EntityPlayer)
			{
				EntityPlayer entityplayer = (EntityPlayer)mc.getRenderViewEntity();
				float f = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
				float f1 = -(entityplayer.distanceWalkedModified + f * mc.timer.renderPartialTicks);
				float f2 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * mc.timer.renderPartialTicks;
				float f3 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * mc.timer.renderPartialTicks;
				GlStateManager.translate(MathHelper.sin(-f1 * (float)Math.PI) * f2 * 0.5F, Math.abs(MathHelper.cos(f1 * (float)Math.PI) * f2), 0.0F);
				GlStateManager.rotate(-MathHelper.sin(f1 * (float)Math.PI) * f2 * 3.0F, 0.0F, 0.0F, 1.0F);
				GlStateManager.rotate(-Math.abs(MathHelper.cos(f1 * (float)Math.PI - 0.2F) * f2) * 5.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(-f3, 1.0F, 0.0F, 0.0F);
			}*/


			GL11.glBegin(GL11.GL_LINES);

			GL11.glVertex3d(0, mc.thePlayer.getEyeHeight(), 0);
			GL11.glVertex3d(x, y + e.height / 2, z);

			GL11.glEnd();

			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			//GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);

			GL11.glPopMatrix();

        }
    };
}
