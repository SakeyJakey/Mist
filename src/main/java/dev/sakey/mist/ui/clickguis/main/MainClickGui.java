package dev.sakey.mist.ui.clickguis.main;

import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.settings.Setting;
import dev.sakey.mist.modules.settings.impl.KeySetting;
import dev.sakey.mist.ui.components.ClickGUISearch;
import dev.sakey.mist.ui.draggables.Draggable;
import dev.sakey.mist.utils.render.ColourUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;

public class MainClickGui extends GuiScreen {

	public MainClickGui() {
		int count = 0;

		int gap = 10;
		int sGapX = 15;
		int sGapY = 15;

		for (Category c :
				Category.values()) {

			panels.add(new Panel(sGapX + (100 + gap) * count, sGapY, c, this, count));
			count++;
		}
		panels.add(new ConfigPanel(sGapX, 150, this));
		panels.add(new ScriptPanel(sGapX + 100 + gap, 150));
	}

	private final ArrayList<Draggable> panels = new ArrayList<Draggable>();

	public Setting currentlyTyping;
	ClickGUISearch search = new ClickGUISearch(0, 0, 0, 200, 20);

	public void initGui() {
		Minecraft.getMinecraft().entityRenderer.loadShader(new ResourceLocation("Twen/Shaders/blur.json"));
		panels.forEach(Draggable::show);
		search.setFocused(true);
		Keyboard.enableRepeatEvents(true);
	}

	public void onGuiClosed() {
		Minecraft.getMinecraft().entityRenderer.stopUseShader();
		Keyboard.enableRepeatEvents(false);
		panels.forEach(Draggable::hide);
	}

	double tGradX = 2,
			gradX = 0.001;

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution sr = new ScaledResolution(mc);

		search.xPosition = sr.getScaledWidth() / 2 - search.getWidth() / 2;
		search.yPosition = sr.getScaledHeight() - 30;

		search.drawTextBox();
		for (Draggable p :
				panels) {
			p.updateChat(mouseX, mouseY);
		}
	}


	protected void keyTyped(char typedChar, int keyCode) throws IOException { //TODO add a text setting with key repeats
		if (currentlyTyping != null) {
			if (keyCode != Keyboard.KEY_ESCAPE) {
				if (currentlyTyping instanceof KeySetting) {
					((KeySetting) currentlyTyping).setCode(keyCode);
					currentlyTyping = null;
				}
			}
			else {
				((KeySetting) currentlyTyping).setCode(Keyboard.KEY_NONE);
				currentlyTyping = null;
			}
		}
		else if (keyCode == Keyboard.KEY_ESCAPE) {
			if(search.getText().isEmpty()) {
				this.mc.displayGuiScreen(null);

				if (this.mc.currentScreen == null) {
					this.mc.setIngameFocus();
				}
			}
			else {
				search.setText("");
			}
		}
		else {
			search.textboxKeyTyped(typedChar, keyCode);
		}
	}
}
