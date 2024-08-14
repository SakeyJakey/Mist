package dev.sakey.mist.modules.impl.render;

import dev.sakey.mist.Mist;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.render.EventRenderHUD;
import dev.sakey.mist.events.impl.render.EventRenderWorld;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.modules.settings.impl.ModeSetting;
import dev.sakey.mist.utils.render.BoxUtils;
import dev.sakey.mist.utils.render.ColourUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static net.minecraft.client.renderer.GlStateManager.disableBlend;
import static net.minecraft.client.renderer.GlStateManager.enableTexture2D;

public class ESP extends Module {

	ModeSetting mode = new ModeSetting("Mode", "2D", "Box");

    @ModuleInfo(name = "ESP", description = "Gives players an outline so you can see them through walls.", category = Category.RENDER)
    public ESP() { addSettings(mode); }

    protected void onEnable() {
		Mist.instance.getEventManager().registerEventHandler(EventRenderHUD.class, eventRenderHUD);
		Mist.instance.getEventManager().registerEventHandler(EventRenderWorld.class, eventRenderWorld);
	}

    protected void onDisable() {
		Mist.instance.getEventManager().unregisterEventHandler(eventRenderHUD);
		Mist.instance.getEventManager().unregisterEventHandler(eventRenderWorld);
	}

    EventHandler<EventRenderWorld> eventRenderWorld = event -> {

		if(!mode.is("Box")) return;

		for(Entity e : (ArrayList<Entity>) mc.theWorld.loadedEntityList){
            if(e == mc.thePlayer) continue;
            BoxUtils.drawBox(e.getEntityBoundingBox());
        }
    };

    EventHandler<EventRenderHUD> eventRenderHUD = event -> {

		// From LiquidBounce bc they have big brains and i dont know projection

		if(!mode.is("2D")) return;

        GL11.glPushMatrix();

        ArrayList<EntityLivingBase> collectedEntities = (ArrayList<EntityLivingBase>) mc.theWorld.loadedEntityList;

        float partialTicks = mc.timer.renderPartialTicks;
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int scaleFactor = scaledResolution.getScaleFactor();
        double scaling = (double)scaleFactor / Math.pow(scaleFactor, 2.0D);
        GL11.glScaled(scaling, scaling, scaling);
        int black = ColourUtil.black();
        float scale = 0.65F;
        float upscale = 1.0F / scale;
        FontRenderer fr = mc.fontRendererObj;
        RenderManager renderMng = mc.getRenderManager();
        EntityRenderer entityRenderer = mc.entityRenderer;

        int i = 0;
        for(int collectedEntitiesSize = collectedEntities.size(); i < collectedEntitiesSize; ++i) {
            Entity entity = collectedEntities.get(i);

            if(entity == mc.thePlayer) continue;


            int color = ColourUtil.getRainbow(4);



            if (isInViewFrustrum(entity.getEntityBoundingBox())) {
                List vectors = getVectors(entity, (double) partialTicks);
                entityRenderer.setupCameraTransform(partialTicks, 0);
                Vector4f position = null;
                Iterator var38 = vectors.iterator();

                while(var38.hasNext()) {
                    Vector3f vector = (Vector3f)var38.next();
                    vector = project2D(scaleFactor, vector.x - renderMng.viewerPosX, vector.y - renderMng.viewerPosY, vector.z - renderMng.viewerPosZ);
                    if (vector != null && vector.z >= 0.0D && vector.z < 1.0D) {
                        if (position == null) {
                            position = new Vector4f((float) vector.x, (float) vector.y, (float) vector.z, 0.0F);
                        }

                        position.x = Math.min(vector.x, position.x);
                        position.y = Math.min(vector.y, position.y);
                        position.z = Math.max(vector.x, position.z);
                        position.w = Math.max(vector.y, position.w);
                    }
                }

                if (position != null) {
                    entityRenderer.setupOverlayRendering();
                    double posX = position.x;
                    double posY = position.y;
                    double endPosX = position.z;
                    double endPosY = position.w;
                    if (true) {
                        if (true) {
                            newDrawRect(posX - 1.0D, posY, posX + 0.5D, endPosY + 0.5D, black);
                            newDrawRect(posX - 1.0D, posY - 0.5D, endPosX + 0.5D, posY + 0.5D + 0.5D, black);
                            newDrawRect(endPosX - 0.5D - 0.5D, posY, endPosX + 0.5D, endPosY + 0.5D, black);
                            newDrawRect(posX - 1.0D, endPosY - 0.5D - 0.5D, endPosX + 0.5D, endPosY + 0.5D, black);
                            newDrawRect(posX - 0.5D, posY, posX + 0.5D - 0.5D, endPosY, color);
                            newDrawRect(posX, endPosY - 0.5D, endPosX, endPosY, color);
                            newDrawRect(posX - 0.5D, posY, endPosX, posY + 0.5D, color);
                            newDrawRect(endPosX - 0.5D, posY, endPosX, endPosY, color);
                        } else {
                            newDrawRect(posX + 0.5D, posY, posX - 1.0D, posY + (endPosY - posY) / 4.0D + 0.5D, black);
                            newDrawRect(posX - 1.0D, endPosY, posX + 0.5D, endPosY - (endPosY - posY) / 4.0D - 0.5D, black);
                            newDrawRect(posX - 1.0D, posY - 0.5D, posX + (endPosX - posX) / 3.0D + 0.5D, posY + 1.0D, black);
                            newDrawRect(endPosX - (endPosX - posX) / 3.0D - 0.5D, posY - 0.5D, endPosX, posY + 1.0D, black);
                            newDrawRect(endPosX - 1.0D, posY, endPosX + 0.5D, posY + (endPosY - posY) / 4.0D + 0.5D, black);
                            newDrawRect(endPosX - 1.0D, endPosY, endPosX + 0.5D, endPosY - (endPosY - posY) / 4.0D - 0.5D, black);
                            newDrawRect(posX - 1.0D, endPosY - 1.0D, posX + (endPosX - posX) / 3.0D + 0.5D, endPosY + 0.5D, black);
                            newDrawRect(endPosX - (endPosX - posX) / 3.0D - 0.5D, endPosY - 1.0D, endPosX + 0.5D, endPosY + 0.5D, black);
                            newDrawRect(posX, posY, posX - 0.5D, posY + (endPosY - posY) / 4.0D, color);
                            newDrawRect(posX, endPosY, posX - 0.5D, endPosY - (endPosY - posY) / 4.0D, color);
                            newDrawRect(posX - 0.5D, posY, posX + (endPosX - posX) / 3.0D, posY + 0.5D, color);
                            newDrawRect(endPosX - (endPosX - posX) / 3.0D, posY, endPosX, posY + 0.5D, color);
                            newDrawRect(endPosX - 0.5D, posY, endPosX, posY + (endPosY - posY) / 4.0D, color);
                            newDrawRect(endPosX - 0.5D, endPosY, endPosX, endPosY - (endPosY - posY) / 4.0D, color);
                            newDrawRect(posX, endPosY - 0.5D, posX + (endPosX - posX) / 3.0D, endPosY, color);
                            newDrawRect(endPosX - (endPosX - posX) / 3.0D, endPosY - 0.5D, endPosX - 0.5D, endPosY, color);
                        }
                    }
                }
            }
        }

        GL11.glPopMatrix();
        GlStateManager.enableBlend();
        GlStateManager.resetColor();
        entityRenderer.setupOverlayRendering();
    };

    private List getVectors(Entity entity, double partialTicks) {
        double x = interpolate(entity.posX, entity.lastTickPosX, partialTicks);
        double y = interpolate(entity.posY, entity.lastTickPosY, partialTicks);
        double z = interpolate(entity.posZ, entity.lastTickPosZ, partialTicks);
        double width = (double) entity.width / 1.5D;
        double height = (double) entity.height + (entity.isSneaking() ? -0.3D : 0.2D);
        AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
        List vectors = Arrays.asList(
                new Vector3f((float) aabb.minX, (float) aabb.minY, (float) aabb.minZ),
                new Vector3f((float) aabb.minX, (float) aabb.maxY, (float) aabb.minZ),
                new Vector3f((float) aabb.maxX, (float) aabb.minY, (float) aabb.minZ),
                new Vector3f((float) aabb.maxX, (float) aabb.maxY, (float) aabb.minZ),
                new Vector3f((float) aabb.minX, (float) aabb.minY, (float) aabb.maxZ),
                new Vector3f((float) aabb.minX, (float) aabb.maxY, (float) aabb.maxZ),
                new Vector3f((float) aabb.maxX, (float) aabb.minY, (float) aabb.maxZ),
                new Vector3f((float) aabb.maxX, (float) aabb.maxY, (float) aabb.maxZ));
        return vectors;
    }

    //            GlStateManager.getFloat(2982, MODELVIEW);
    //            GlStateManager.getFloat(2983, PROJECTION);
    //            GL11.glGetInteger(GL11.GL_VIEWPORT, VIEWPORT);
    //            GLU.gluProject(e.posX, e.posY, e.posZ, ActiveRenderInfo.MODELVIEW, ActiveRenderInfo.PROJECTION, ActiveRenderInfo.VIEWPORT)
    //


    private final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);

	
	
    private Vector3f project2D(int scaleFactor, double x, double y, double z) {
        GL11.glGetFloat(2982, this.modelview);
        GL11.glGetFloat(2983, this.projection);
        GL11.glGetInteger(2978, this.viewport);
        return GLU.gluProject((float)x, (float)y, (float)z, this.modelview, this.projection, this.viewport, this.vector) ? new Vector3f((float) (this.vector.get(0) / (float)scaleFactor), (float) (((float)mc.displayHeight - this.vector.get(1)) / (float)scaleFactor), (float) this.vector.get(2)) : null;
    }

    private double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }


    private static final Frustum frustrum = new Frustum();
    private boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = mc.getRenderViewEntity();
        frustrum.setPosition(current.posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
    }


	private void newDrawRect(double left, double top, double right, double bottom, int color) {
		if (left < right) {
			double i = left;
			left = right;
			right = i;
		}

		if (top < bottom) {
			double j = top;
			top = bottom;
			bottom = j;
		}

		float f3 = (float) (color >> 24 & 255) / 255.0F;
		float f = (float) (color >> 16 & 255) / 255.0F;
		float f1 = (float) (color >> 8 & 255) / 255.0F;
		float f2 = (float) (color & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(f, f1, f2, f3);
		worldrenderer.begin(7, DefaultVertexFormats.POSITION);
		worldrenderer.pos(left, bottom, 0.0D).endVertex();
		worldrenderer.pos(right, bottom, 0.0D).endVertex();
		worldrenderer.pos(right, top, 0.0D).endVertex();
		worldrenderer.pos(left, top, 0.0D).endVertex();
		tessellator.draw();
		enableTexture2D();
		disableBlend();
	}
	
}
