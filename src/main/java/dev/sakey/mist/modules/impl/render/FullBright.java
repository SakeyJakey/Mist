package dev.sakey.mist.modules.impl.render;

import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;

public class FullBright extends Module {

	@ModuleInfo(name = "FullBright", description = "Makes everything bright.", category = Category.RENDER)
	public FullBright() {
	}

	protected void onEnable() {
		mc.gameSettings.gammaSetting = 100;
	}

	protected void onDisable() {
		mc.gameSettings.gammaSetting = 10;
	}
}