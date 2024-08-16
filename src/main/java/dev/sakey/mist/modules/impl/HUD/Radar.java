package dev.sakey.mist.modules.impl.HUD;

import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.ui.draggables.Draggable;
import dev.sakey.mist.ui.draggables.ResizeMode;
import dev.sakey.mist.utils.render.ColourUtil;
import dev.sakey.mist.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Radar extends Module {

	RadarDraggable draggable = new RadarDraggable();

	@ModuleInfo(name = "Radar", description = "Shows the players near you on a map.", category = Category.HUD)
	public Radar() {
		draggable.add();
	}

	protected void onEnable() {
		draggable.show();
	}


	protected void onDisable() {
		draggable.hide();
	}

	public double getDistanceSqToEntityXZ(Entity entityIn) {
		double d0 = mc.thePlayer.posX - entityIn.posX;
		double d2 = mc.thePlayer.posZ - entityIn.posZ;
		return d0 * d0 + d2 * d2;
	}

	class RadarDraggable extends Draggable {

		public RadarDraggable() {
			super(10, 70, 100, 100);
			minWidth = minHeight = 20;
			resizeMode = ResizeMode.EQUAL;
		}

		public void draw() {
			if (mc.theWorld == null || mc.thePlayer == null) return;

			GL11.glPushMatrix();
			RenderUtils.draw2DPolygon(xPos + width / 2, yPos + height / 2, 5f, 3, ColourUtil.black()); //self

			for (Object en : mc.theWorld.loadedEntityList) {
//	            if (!(en instanceof EntityPlayer)) {
//	                continue;
//	            }
				if (en == mc.thePlayer) continue;
				double dist_sq = getDistanceSqToEntityXZ((Entity) en);

				double x = ((Entity) en).posX - mc.thePlayer.posX, z = ((Entity) en).posZ - mc.thePlayer.posZ;
				double calc = Math.atan2(x, z) * 57.2957795131f;
				double angle = ((mc.thePlayer.rotationYaw + calc) % 360) * 0.01745329251f;
				double hypotenuse = dist_sq / 5;
				double x_shift = hypotenuse * Math.sin(angle),
						y_shift = hypotenuse * Math.cos(angle);
				RenderUtils.draw2DPolygon(xPos + width / 2 - x_shift, yPos + height / 2 - y_shift, 2.5, 16, ColourUtil.black());
				RenderUtils.draw2DPolygon(xPos + width / 2 - x_shift, yPos + height / 2 - y_shift, 2, 16, Color.red.getRGB());
			}

			//GL11.glDisable(GL11.GL_SCISSOR_TEST);
			RenderUtils.draw2DPolygonOutline(xPos + width / 2, yPos + height / 2, 5f, 3, -1); // outline on top but bg behind
			GL11.glPopMatrix();
			GlStateManager.disableBlend();
		}
	}

}
