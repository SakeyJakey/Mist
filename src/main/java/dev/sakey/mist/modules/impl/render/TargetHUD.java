package dev.sakey.mist.modules.impl.render;

import dev.sakey.mist.Mist;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.render.EventRenderWorld;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.modules.impl.combat.KillAura;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class TargetHUD extends Module {

	EventHandler<EventRenderWorld> eventRenderWorld = e -> {
		if (((KillAura) Mist.instance.getModuleManager().getModule(KillAura.class)).target == null) return;
		Entity target = ((KillAura) Mist.instance.getModuleManager().getModule(KillAura.class)).target;

		final double posX = target.posX - mc.getRenderManager().renderPosX;
		final double posY = target.posY - mc.getRenderManager().renderPosY;
		final double posZ = target.posZ - mc.getRenderManager().renderPosZ;

		GL11.glPushMatrix();

		GL11.glRotated(-mc.thePlayer.rotationYaw, 0, 1, 0);
		GL11.glRotated(mc.thePlayer.rotationPitch, 1, 0, 0);

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3d(posX - 2.5, posY - 1, posZ);
		GL11.glVertex3d(posX - 2.5, posY + 1, posZ);
		GL11.glVertex3d(posX + 2, posY + 1, posZ);
		GL11.glVertex3d(posX + 2, posY - 1, posZ);
		GL11.glEnd();

		GL11.glScaled(0.05, 0.05, 0.05);
		GL11.glRotated(180, 0, 0, 1);
		GL11.glTranslated(-1.25 * 32, -32, -1);
		Mist.instance.getFontRenderer(32).drawString(target.getName(), 0, 0, -1, false);

		GL11.glPopMatrix();
	};

	@ModuleInfo(name = "TargetHUD", description = "Shows target entity info.", category = Category.RENDER)
	public TargetHUD() {
	}

	protected void onEnable() {
		registerEvent(EventRenderWorld.class, eventRenderWorld);
	}

	protected void onDisable() {
		unregisterEvent(eventRenderWorld);
	}

}
