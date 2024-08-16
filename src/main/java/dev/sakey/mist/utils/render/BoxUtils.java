package dev.sakey.mist.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

public class BoxUtils {

	static Minecraft mc = Minecraft.getMinecraft();
	static RenderManager rm = mc.getRenderManager();

	public static void drawBox(BlockPos block) {
		if (block == null) return;

		block = block.add(-rm.renderPosX, -rm.renderPosY, -rm.renderPosZ);

		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineWidth(2);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(1, 1, 1, 1);
		RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(block, block.add(1, 1, 1)));
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void drawBox(AxisAlignedBB temp) {
		AxisAlignedBB aabb = new AxisAlignedBB(
				temp.minX - rm.renderPosX,
				temp.minY - rm.renderPosY,
				temp.minZ - rm.renderPosZ,
				temp.maxX - rm.renderPosX,
				temp.maxY - rm.renderPosY,
				temp.maxZ - rm.renderPosZ
		);

		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineWidth(2);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(1, 1, 1, 1);
		RenderGlobal.drawSelectionBoundingBox(aabb);
		RenderGlobal.drawOutlinedBoundingBox(aabb, 1, 1, 1, 1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
	}

}
