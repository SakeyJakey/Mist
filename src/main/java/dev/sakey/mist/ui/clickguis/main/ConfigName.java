package dev.sakey.mist.ui.clickguis.main;

import dev.sakey.mist.Mist;
import dev.sakey.mist.ui.components.TuiTextField;
import dev.sakey.mist.utils.render.ColourUtil;
import dev.sakey.mist.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ConfigName extends GuiScreen {

	private final MainClickGui cgui;
	TuiTextField name;
	double x, y, w, h;

	public ConfigName(double x, double y, double w, double h, MainClickGui owner) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		cgui = owner;
	}

	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		(name = new TuiTextField(0, (int) x, (int) y + 20, (int) w, 20)).setEnableBackgroundDrawing(false).setFocused(true);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		Gui.drawGradientRectStatic(x, y + 40 - 5, x + w, y + 40 + 20, ColourUtil.grey(), 0x00000000);
		Gui.drawGradientRectStatic(x, y - 20, x + w, y + 5, 0x00000000, ColourUtil.grey());
		RenderUtils.drawRoundedRect(x, y, x + w, y + h * 2, 10, ColourUtil.black());

		Mist.instance.getFontRenderer().drawString("Name your config", x + w / 2, y + 10, -1, true, true);
		name.drawTextBox();
	}

	public void updateScreen() {
		name.updateCursorCounter();
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {

		if (keyCode == Keyboard.KEY_ESCAPE)
			Minecraft.getMinecraft().displayGuiScreen(cgui);

		if (keyCode == Keyboard.KEY_RETURN && !name.getText().isEmpty()) {
			Mist.instance.getConfigManager().saveConfig(name.getText());
			Minecraft.getMinecraft().displayGuiScreen(cgui);
		} else
			name.textboxKeyTyped(typedChar, keyCode);
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}
}
