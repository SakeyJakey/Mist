package dev.sakey.mist.modules.impl.render;

import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.modules.settings.impl.BoolSetting;
import dev.sakey.mist.modules.settings.impl.ModeSetting;

public class Animations extends Module {

	public ModeSetting mode = new ModeSetting("Mode", "Mist", "Night", "Totem");
	public BoolSetting fullSwing = new BoolSetting("Full Swing", true);

	@ModuleInfo(name = "Animations", description = "Item animations", category = Category.RENDER)
	public Animations() {
		addSettings(mode, fullSwing);
	}

	public String getSuffix() {
		return mode.getMode();
	}

	// ItemRenderer:transformFirstPersonItem
}
