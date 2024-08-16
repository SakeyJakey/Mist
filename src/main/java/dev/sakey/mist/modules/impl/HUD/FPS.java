package dev.sakey.mist.modules.impl.HUD;

import dev.sakey.mist.Mist;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.ui.draggables.Draggable;
import dev.sakey.mist.ui.draggables.ResizeMode;
import net.minecraft.client.Minecraft;

public class FPS extends Module {

	FPSDraggable draggable = new FPSDraggable();

	@ModuleInfo(name = "FPS", description = "Displays FPS", category = Category.HUD)
	public FPS() {
		draggable.add();
	}

	protected void onEnable() {
		draggable.show();
	}

	protected void onDisable() {
		draggable.hide();
	}

	private class FPSDraggable extends Draggable {
		public FPSDraggable() {
			super(10, 50, 40, 10);
			resizeMode = ResizeMode.NONE;
		}

		public void draw() {
			Mist.instance.getFontRenderer().drawString("FPS: " + Minecraft.getDebugFPS(), getCentreX(), getCentreY(), -1, false, true);
		}
	}

}
