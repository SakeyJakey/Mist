package dev.sakey.mist.modules.impl.render;

import dev.sakey.mist.Mist;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.render.EventRenderEntity;
import dev.sakey.mist.events.impl.render.EventRenderHUD;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.utils.render.MaskUtils;
import dev.sakey.mist.utils.render.ShaderUtils;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class ShaderESP extends Module {

	Framebuffer framebuffer = new Framebuffer(1, 1, false);
	EventHandler<EventRenderEntity> eventRenderEntity = e -> {
		//if(!(e.getEntity() instanceof EntityPlayer)) return;
		if (e.getEntity() == mc.thePlayer) return;

		switch (e.getType()) {
			case PRE:
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				MaskUtils.Stencil.beginDrawMask();
				break;
			case POST:
				MaskUtils.Stencil.beginDrawContent();
				try {
					ShaderUtils.drawBackgroundShader("bg4");
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
				MaskUtils.Stencil.endMask();
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				break;
		}
	};
	EventHandler<EventRenderHUD> eventRenderHUD = e -> {

//
//
//        ScaledResolution sr = new ScaledResolution(mc);
//        float width = (float) sr.getScaledWidth_double();
//        float height = (float) sr.getScaledHeight_double();
//
//
//        if(framebuffer.framebufferWidth != mc.displayWidth || framebuffer.framebufferHeight != mc.displayHeight) {
//            framebuffer.deleteFramebuffer();
//            framebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, false);
//        }
//
//        framebuffer.framebufferClear();
//
//        framebuffer.bindFramebuffer(false);
//
//
//
//        framebuffer.unbindFramebuffer();
//
//        mc.getFramebuffer().bindFramebuffer(false);
//
//
//        MaskUtils.Stencil.beginDrawContent();
//
//        framebuffer.bindFramebufferTexture();
//
//        //Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 100, 100, width, height);
//
//        framebuffer.unbindFramebufferTexture();
//
//        MaskUtils.Stencil.endMask();
	};

	@ModuleInfo(name = "ShaderESP", description = "Cool shader (may cause lag with many entities as i am stupid and render it for every entity)", category = Category.RENDER)
	public ShaderESP() {
	}

	protected void onEnable() {
		Mist.instance.getEventManager().registerEventHandler(EventRenderEntity.class, eventRenderEntity);
		Mist.instance.getEventManager().registerEventHandler(EventRenderHUD.class, eventRenderHUD);
	}

	protected void onDisable() {
		Mist.instance.getEventManager().unregisterEventHandler(eventRenderEntity);
		Mist.instance.getEventManager().unregisterEventHandler(eventRenderHUD);
	}
}
