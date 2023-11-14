package dev.sakey.mist.ui.clickguis.main;

import dev.sakey.mist.Mist;
import dev.sakey.mist.scripts.Script;
import dev.sakey.mist.ui.draggables.Draggable;
import dev.sakey.mist.ui.draggables.ResizeMode;
import dev.sakey.mist.utils.client.font.GlyphPageFontRenderer;
import dev.sakey.mist.utils.render.ColourUtil;
import dev.sakey.mist.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

import java.util.concurrent.CopyOnWriteArrayList;

public class ScriptPanel extends Draggable {
	private final GlyphPageFontRenderer fr = Mist.instance.getFontRenderer(18);

	private CopyOnWriteArrayList<String> scripts = new CopyOnWriteArrayList<String>();

	private final int settingSize = 3;

	private double tHeight;

	public ScriptPanel(double xPos, double yPos) {
		super(xPos, yPos, 100, 0);
		tHeight = calculateHeight();
		resizeMode = ResizeMode.NONE;
	}

	protected void onShow() {
		height = 0;
		getScripts();
	}

	private void getScripts() {
		scripts.clear();
		scripts.addAll(Mist.instance.getScriptLoader().getScripts());
	}

	private double calculateHeight() {
		int count = 1 /* 1 for title */ + scripts.size();
		return count * fr.getFontHeight();
	}

	protected boolean getHoveringMove(int mX, int mY) {
		return mX > xPos && mY > yPos && mX < getWPos() && mY < yPos + fr.getFontHeight();
	}

	protected boolean getHoveringResize(int mX, int mY) {
		return false;
	}

	private boolean wasClicked;
	public void draw() {
		final double speed = Minecraft.getDebugFPS() / 8;

		tHeight = calculateHeight();

		height += (tHeight - height) / speed;

		double x, y, w, h;

		x = xPos;
		y = yPos;
		w = getWPos();
		h = yPos + fr.getFontHeight();

		for (String c :
				scripts) {

			y += fr.getFontHeight();
			h += fr.getFontHeight();

			char[] script = c.toCharArray();
			if(script.length > 0)
				script[0] = c.toUpperCase().charAt(0);

			fr.drawString(String.valueOf(script), x,  y, -1, false);

			if (getMouseButtonOnce(x, y, w, h, 0))
				Mist.instance.getScriptLoader().loadScript(new Script(c));
		}

		// Make top and bottom black
		RenderUtils.drawRect(xPos, yPos, getWPos(), yPos + fr.getFontHeight(), ColourUtil.black());

		fr.drawString("§lScripts", xPos,  yPos, -1, false);

		wasClicked = Mouse.isButtonDown(0) || Mouse.isButtonDown(1) || Mouse.isButtonDown(2);
	}

	public boolean getMouseButtonOnce(double x, double y, double w, double h, int button) {
		if(RenderUtils.isInside(mouseX, mouseY, x, y, w, h)) {
			if (Mouse.isButtonDown(button)) {
				if(!wasClicked) {
					wasClicked = true;
					return true;
				}
			}
			else if(!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1) && !Mouse.isButtonDown(2)) {
				wasClicked = false;
			}
		}
		return false;
	}
}
