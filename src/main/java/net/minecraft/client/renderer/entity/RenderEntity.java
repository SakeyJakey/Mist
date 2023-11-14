package net.minecraft.client.renderer.entity;

import dev.sakey.mist.utils.render.MaskUtils;
import dev.sakey.mist.utils.render.ShaderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class RenderEntity extends Render<Entity>
{
    public RenderEntity(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        renderOffsetAABB(entity.getEntityBoundingBox(), x - entity.lastTickPosX, y - entity.lastTickPosY, z - entity.lastTickPosZ);
        GlStateManager.popMatrix();

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return null;
    }
}
