package dev.sakey.mist.modules.impl.render;

import dev.sakey.mist.Mist;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.render.EventRenderBlock;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import net.minecraft.init.Blocks;

public class XRay extends Module {

	EventHandler<EventRenderBlock> eventRenderBlock = event -> {
		event.setHidden(true);

		if (
				event.getBlock() == Blocks.diamond_ore ||
						event.getBlock() == Blocks.iron_ore ||
						event.getBlock() == Blocks.emerald_ore ||
						event.getBlock() == Blocks.gold_ore ||
						event.getBlock() == Blocks.redstone_ore ||
						event.getBlock() == Blocks.lapis_ore ||
						event.getBlock() == Blocks.lit_redstone_ore ||
						event.getBlock() == Blocks.coal_ore
		) event.setHidden(false);
	};

	@ModuleInfo(name = "XRay", description = "See blocks through walls.", category = Category.RENDER)
	public XRay() {
	}

	protected void onEnable() {
		Mist.instance.getEventManager().registerEventHandler(EventRenderBlock.class, eventRenderBlock);
		mc.renderGlobal.loadRenderers();
	}

	protected void onDisable() {
		Mist.instance.getEventManager().unregisterEventHandler(eventRenderBlock);
		mc.renderGlobal.loadRenderers();
	}
}
