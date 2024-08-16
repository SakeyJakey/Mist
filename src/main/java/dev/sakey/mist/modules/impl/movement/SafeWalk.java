package dev.sakey.mist.modules.impl.movement;

import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;

public class SafeWalk extends Module {
	@ModuleInfo(name = "SafeWalk", description = "Doesn't walk of edges", category = Category.MOVEMENT)
	public SafeWalk() {
	}
}
