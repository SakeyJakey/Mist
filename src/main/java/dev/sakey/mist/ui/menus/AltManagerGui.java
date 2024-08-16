package dev.sakey.mist.ui.menus;

import dev.sakey.mist.Mist;
import dev.sakey.mist.ui.components.TuiPassword;
import dev.sakey.mist.ui.components.TuiTextField;
import dev.sakey.mist.utils.render.ColourUtil;
import dev.sakey.mist.utils.render.Shader;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.IOException;

public class AltManagerGui extends GuiScreen {

	Shader bg;
	long initTime = System.currentTimeMillis();
	GuiScreen parent;
	private TuiTextField username;
	private TuiPassword password;
	private String status;


	public AltManagerGui(GuiScreen parent) {
		this.parent = parent;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		ScaledResolution sr = new ScaledResolution(this.mc);

		this.drawDefaultBackground();

		GlStateManager.disableCull();

		bg.useShader(sr.getScaledWidth(), sr.getScaledHeight(), mouseX, mouseY, (System.currentTimeMillis() - initTime) / 1000f);

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glVertex2f(-1f, -1f);
		GL11.glVertex2f(-1f, 1f);
		GL11.glVertex2f(1f, 1f);
		GL11.glVertex2f(1f, -1f);

		GL11.glEnd();

		// Unbind shader
		GL20.glUseProgram(0);

		username.drawTextBox();
		password.drawTextBox();

		Mist.instance.getFontRenderer(Mist.Constants.largeFontSize).drawString(status, sr.getScaledWidth() / 2 - Mist.instance.getFontRenderer(Mist.Constants.largeFontSize).getStringWidth(status) / 2, 50, ColourUtil.getRainbow(4), true);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {

		try {
			bg = new Shader("loading");
		} catch (IOException e) {
			Mist.instance.getLogger().error("Failed to load background shader!");
		}

		ScaledResolution sr = new ScaledResolution(this.mc);
		this.buttonList.add(new GuiButton(0, width / 2 + 4 + 50, height - 24, 100, 20, "Cancel"));
		this.buttonList.add(new GuiButton(1, width / 2 + 4 + 50, height - 48, 100, 20, "Use Cracked"));
		this.buttonList.add(new GuiButton(2, width / 2 - 50, height - 48, 100, 20, "Use Microsoft"));
		this.buttonList.add(new GuiButton(3, width / 2 - 150 - 4, height - 48, 100, 20, "Use Mojang"));
		this.buttonList.add(new GuiButton(4, width / 2 - 150 - 4, height - 24, 200 + 4, 20, "Use Previous"));
		//this.buttonList.add(new GuiButton(5, width / 2 - 150 - 4, height - 24, 100, 20, "Use session"));


		(this.username = new TuiTextField(100, this.width / 2 - 50 - 10, sr.getScaledHeight() / 2 - 50, 120, 20)).setFocused(true);
		(this.password = new TuiPassword(100, this.width / 2 - 50 - 10, sr.getScaledHeight() / 2 - 25, 120, 20)).setFocused(false);

		status = "A§flts";

		super.initGui();
		//new Notification("Alts", "Session not supported yet!", NotificationType.WARNING, 2000);
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		status = "Logging in...";

		if (button.id == 0) {
			mc.displayGuiScreen(parent);
		}
		if (button.id == 1) {
			status = SessionChanger.getInstance().setUserOffline(username.getText());
		}
		if (button.id == 2) {
			status = SessionChanger.getInstance().setUserMicrosoft(username.getText(), password.getText());
		}
		if (button.id == 3) {
			status = SessionChanger.getInstance().setUser(username.getText(), password.getText());
		}
		if (button.id == 4) {
			//todo: add
		}
	}

	public void updateScreen() {
		this.username.updateCursorCounter();
		this.password.updateCursorCounter();
	}

	@Override
	protected void keyTyped(final char character, final int key) {
		if (key == Keyboard.KEY_TAB && username.isFocused()) {
			username.setCanLoseFocus(true);
			username.setFocused(false);
			password.setFocused(true);
		}

		if (key == Keyboard.KEY_RETURN && password.getText() == "")
			status = SessionChanger.getInstance().setUserOffline(username.getText());

		else if (key == Keyboard.KEY_RETURN)
			status = SessionChanger.getInstance().setUserMicrosoft(username.getText(), password.getText());

		try {
			super.keyTyped(character, key);
		} catch (IOException e) {
			e.printStackTrace();
		}


		if (character == '\t' && !this.username.isFocused()) {
			this.username.setFocused(true);
		} else if (character == '\t') {
			this.username.setFocused(false);
			this.password.setFocused(true);
		}
		this.username.textboxKeyTyped(character, key);
		this.password.textboxKeyTyped(character, key);
	}

	@Override
	protected void mouseClicked(final int x2, final int y2, final int button) {
		try {
			super.mouseClicked(x2, y2, button);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.username.mouseClicked(x2, y2, button);
		this.password.mouseClicked(x2, y2, button);
	}

	@Override
	public void onGuiClosed() {
		buttonList.clear();
		mc.entityRenderer.stopUseShader();
		Keyboard.enableRepeatEvents(false);
	}

}
