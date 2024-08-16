package dev.sakey.mist.modules.impl.movement;

import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;

public class Phase extends Module {

	@ModuleInfo(name = "Phase", description = "Makes you float.", category = Category.MOVEMENT)
	public Phase() {
	}

	public void onEnable() {
		mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 3.2999, mc.thePlayer.posZ);
		quietToggle();
	}
}
