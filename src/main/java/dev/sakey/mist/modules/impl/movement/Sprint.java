package dev.sakey.mist.modules.impl.movement;

import dev.sakey.mist.Mist;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.player.EventMotion;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.modules.settings.impl.BoolSetting;

public class Sprint extends Module {

	BoolSetting omni = new BoolSetting("Omni", false);
	EventHandler<EventMotion> eventMotionHandler = e -> {
		if (e.isPost()) return;
		if ((mc.thePlayer.moveForward > 0 || omni.isEnabled()) && !mc.thePlayer.isUsingItem() && !mc.thePlayer.isSneaking() && !mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isSprinting())
			mc.thePlayer.setSprinting(true);
	};

	@ModuleInfo(name = "Sprint", description = "Automatically sprints.", category = Category.MOVEMENT)
	public Sprint() {
		addSettings(omni);
	}

	public void onEnable() {
		Mist.instance.getEventManager().registerEventHandler(EventMotion.class, eventMotionHandler);
	}

	public void onDisable() {
		Mist.instance.getEventManager().unregisterEventHandler(eventMotionHandler);
		mc.thePlayer.setSprinting(false);
	}
}
