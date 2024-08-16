package dev.sakey.mist.modules.impl.HUD;

import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.ui.draggables.Draggable;
import dev.sakey.mist.utils.render.ColourUtil;
import dev.sakey.mist.utils.render.RenderUtils;

public class Keystrokes extends Module {

	KeystrokesDraggable draggable = new KeystrokesDraggable();

	@ModuleInfo(name = "Keystrokes", description = "Strokes", category = Category.HUD)
	public Keystrokes() {
		draggable.add();
	}

	protected void onEnable() {
		draggable.show();
	}


	protected void onDisable() {
		draggable.hide();
	}

	class KeystrokesDraggable extends Draggable {

		public KeystrokesDraggable() {
			super(10, 150, 100, 100);
		}

		public void draw() {

			int xPadding = 2, xSize = 15, yPadding = 2, ySize = 10;

			RenderUtils.drawRect(xPos + xSize + xPadding, yPos, xPos + 2 * xSize + xPadding, yPos + ySize, mc.gameSettings.keyBindForward.isPressed() ? 0xffff0000 : mc.gameSettings.keyBindForward.isKeyDown() ? -1 : ColourUtil.black());
			RenderUtils.drawRect(xPos, yPos + ySize + yPadding, xPos + xSize, yPos + 2 * ySize + yPadding, mc.gameSettings.keyBindLeft.isPressed() ? 0xffff0000 : mc.gameSettings.keyBindLeft.isKeyDown() ? -1 : ColourUtil.black());
			RenderUtils.drawRect(xPos + xSize + xPadding, yPos + ySize + yPadding, xPos + 2 * xSize + xPadding, yPos + 2 * ySize + yPadding, mc.gameSettings.keyBindBack.isPressed() ? 0xffff0000 : mc.gameSettings.keyBindBack.isKeyDown() ? -1 : ColourUtil.black());
			RenderUtils.drawRect(xPos + 2 * xSize + 2 * xPadding, yPos + ySize + yPadding, xPos + 3 * xSize + 2 * xPadding, yPos + 2 * ySize + yPadding, mc.gameSettings.keyBindRight.isPressed() ? 0xffff0000 : mc.gameSettings.keyBindRight.isKeyDown() ? -1 : ColourUtil.black());

			RenderUtils.drawRect(xPos, yPos + 2 * ySize + 2 * yPadding, xPos + 3 * xSize + 2 * xPadding, yPos + 3 * ySize + 2 * yPadding, mc.gameSettings.keyBindJump.isPressed() ? 0xffff0000 : mc.gameSettings.keyBindJump.isKeyDown() ? -1 : ColourUtil.black());

		}
	}
}