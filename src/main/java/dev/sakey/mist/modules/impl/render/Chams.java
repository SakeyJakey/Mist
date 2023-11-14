package dev.sakey.mist.modules.impl.render;

import dev.sakey.mist.Mist;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.render.EventRenderEntity;
import dev.sakey.mist.events.impl.render.EventRenderWorld;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.utils.render.BoxUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Chams extends Module {

    //TODO: add entity settings

    @ModuleInfo(name = "Chams", description = "Shows players through walls.", category = Category.RENDER)
    public Chams() { }

    protected void onEnable() { Mist.instance.getEventManager().registerEventHandler(EventRenderEntity.class, eventRenderEntity); }

    protected void onDisable() { Mist.instance.getEventManager().unregisterEventHandler(eventRenderEntity); }

    EventHandler<EventRenderEntity> eventRenderEntity = e -> {
        switch (e.getType()) {
            case PRE:
                GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
                GL11.glPolygonOffset(1, -1000000);
                break;
            case POST:
                GL11.glPolygonOffset(1, 1000000);
                GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
                break;
        }
    };
}
