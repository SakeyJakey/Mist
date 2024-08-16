package dev.sakey.mist.modules.impl.combat;

import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.modules.settings.impl.NumberSetting;

public class Reach extends Module {

	public NumberSetting reachDistance = new NumberSetting("Distance", 3, 0.2, 10, 0.1);

	@ModuleInfo(name = "Reach", description = "Reaches further.", category = Category.COMBAT)
	public Reach() {
		addSettings(reachDistance);
	}

	public String getSuffix() {
		return String.valueOf(reachDistance.getValue());
	}
}