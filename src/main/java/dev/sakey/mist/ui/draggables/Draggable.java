package dev.sakey.mist.ui.draggables;

import dev.sakey.mist.Mist;
import dev.sakey.mist.utils.render.MaskUtils;
import dev.sakey.mist.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;


public abstract class Draggable {

	private static Draggable otherIsDragging, otherIsResizing;
	private final double defaultWidth, defaultHeight; //TODO add resize modes so lock, none, any, vert for cgui
	protected double xPos, yPos,
			defaultX, defaultY,
			minWidth = 5, minHeight = 5,
			width, height;
	protected boolean isRounded = true;
	protected ResizeMode resizeMode = ResizeMode.FREE; //TODO: add gsetters
	protected int mouseX, mouseY, // for the draggables like cgui
			bgColour = 0x80000000;
	private boolean isVisible;
	private int prevMX, prevMY;

	public Draggable(double xPos, double yPos, double width, double height) {
		this.defaultX = this.xPos = xPos;
		this.defaultY = this.yPos = yPos;
		this.defaultWidth = this.width = width;
		this.defaultHeight = this.height = height;
	}

	public void updateChat(int mX, int mY) { //todo min size & right bottom anchors

		mouseX = mX;
		mouseY = mY;

		boolean isHovering = getHoveringMove(mX, mY);

		if ((isHovering && otherIsDragging == null) || otherIsDragging == this) {
			if (Mouse.isButtonDown(0)) {
				otherIsDragging = this;
				drag(mX, mY);
			} else {
				otherIsDragging = null;
			}
		}

		isHovering = getHoveringResize(mX, mY);

		if (((isHovering && otherIsResizing == null) || otherIsResizing == this) && resizeMode != ResizeMode.NONE) {
			if (Mouse.isButtonDown(1)) {
				otherIsResizing = this;

				width = Math.max(minWidth, mX - xPos);
				height = Math.max(minHeight, mY - yPos);

				width += 1;
				height += 1;

				if (resizeMode == ResizeMode.HORIZONTAL)
					height = defaultHeight;
				else if (resizeMode == ResizeMode.VERTICAL)
					width = defaultWidth;
				else if (resizeMode == ResizeMode.EQUAL)
					height = width = (height + width) / 2;

				onResize();
			} else
				otherIsResizing = null;
		}

		if (isHovering && Mouse.isButtonDown(2) && resizeMode != ResizeMode.NONE) {
			xPos = defaultX;
			yPos = defaultY;

			width = defaultWidth;
			height = defaultHeight;
		}

		Draw();

		prevMX = mX;
		prevMY = mY;

		isHovering = RenderUtils.isInside(mX, mY, xPos, yPos, getWPos(), getHPos());
		// Selection box
		if (
				isHovering && otherIsDragging == null && otherIsResizing == null ||
						otherIsDragging == this || otherIsResizing == this
		) // prevent if another dragable is being dragged or resized
			RenderUtils.drawOutlineRoundedRect(xPos, yPos, xPos + width, yPos + height, isRounded ? 5 : 0, 2, -1);
	}


	protected void onResize() {

	}

	// Default hover the entire draggable
	protected boolean getHoveringMove(int mX, int mY) {
		return RenderUtils.isInside(mX, mY, xPos, yPos, getWPos(), getHPos());
	}

	protected boolean getHoveringResize(int mX, int mY) {
		return RenderUtils.isInside(mX, mY, xPos, yPos, getWPos(), getHPos());
	}

	public void add() {
		Mist.instance.getDraggableManager().add(this);
	}

	public void remove() {
		Mist.instance.getDraggableManager().remove(this);
	}

	private void drag(int mX, int mY) {
		xPos += mX - prevMX;
		yPos += mY - prevMY;
	}

	public void updateChat() {
		Draw();
	}

	private void fillBackground(int colour) {


		RenderUtils.drawRoundedRect(xPos, yPos, xPos + width, yPos + height, isRounded ? 5 : 0, colour);
		MaskUtils.UI.beginDrawContent();
		RenderUtils.drawRoundedRect(xPos, yPos, xPos + width, yPos + height, isRounded ? 5 : 0, colour);
	}

	private void Draw() {
		if (Minecraft.getMinecraft().gameSettings.showDebugInfo) return;

		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

		MaskUtils.UI.beginDrawMask();

		fillBackground(bgColour);

		draw();

		MaskUtils.UI.endMask();
	}

	protected double getCentreX() {
		return xPos + width / 2;
	}

	protected double getCentreY() {
		return yPos + height / 2;
	}

	protected double getWPos() {
		return xPos + width;
	}

	protected double getHPos() {
		return yPos + height;
	}

	protected abstract void draw();

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean visible) {
		if (visible)
			show();
		else
			hide();
	}

	public void toggleVisible() {
		if (isVisible)
			hide();
		else
			show();
	}

	public void show() {
		isVisible = true;
		onShow();
	}

	public void hide() {
		isVisible = false;
		onHide();
	}

	protected void onShow() {

	}

	protected void onHide() {

	}
}
