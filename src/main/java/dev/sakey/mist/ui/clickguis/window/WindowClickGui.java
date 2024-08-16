package dev.sakey.mist.ui.clickguis.window;

import dev.sakey.mist.modules.Category;
import dev.sakey.mist.ui.draggables.Draggable;
import dev.sakey.mist.utils.render.RenderUtils;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class WindowClickGui extends GuiScreen {

	Draggable window;

	public WindowClickGui() {
		window = new Draggable(50, 50, 300, 200) {
			protected void draw() {

				drawBackground(0xffffffff);

				int count = 0;
				for (Category c : Category.values()) {
					RenderUtils.drawRect(0, yPos + count * 20, 50, yPos + (count + 1) * 20, 0xffff0000);

					count++;
				}
			}
		};
	}

	public void initGui() {
		super.initGui();
		window.show();
	}

	public void onGuiClosed() {
		super.onGuiClosed();
		window.hide();
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		// super.drawScreen(mouseX, mouseY, partialTicks);
		window.updateChat(mouseX, mouseY);
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
	}
}
