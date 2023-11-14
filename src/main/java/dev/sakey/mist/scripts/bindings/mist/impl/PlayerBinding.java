package dev.sakey.mist.scripts.bindings.mist.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.ChatComponentText;

public class PlayerBinding {

	private static Minecraft mc = Minecraft.getMinecraft();

	public boolean isOnGround() {
		return mc.thePlayer.onGround;
	}

	public boolean isInAir() {
		return mc.thePlayer.isAirBorne;
	}

	public void jump() {
		mc.thePlayer.jump();
	}

	public boolean isMoving() {
		return Math.abs(mc.thePlayer.moveStrafing) > 0 || Math.abs(mc.thePlayer.moveForward) > 0;
	}

	public boolean isMovingForward() {
		return mc.thePlayer.moveForward > 0;
	}

	public boolean isMovingBackwards() {
		return mc.thePlayer.moveForward < 0;
	}

	public boolean isMovingForwardOrBackward() {
		return Math.abs(mc.thePlayer.moveForward) > 0;
	}

	public boolean isStrafing() {
		return Math.abs(mc.thePlayer.moveStrafing) > 0;
	}

	public void setPosition(double x, double y, double z) {
		mc.thePlayer.setPosition(x, y, z);
	}

	public void sendChatMessage(String message) {
		mc.thePlayer.sendChatMessage(message);
	}

	public void showChatMessage(String message) {
		mc.thePlayer.addChatMessage(new ChatComponentText(message));
	}

	public void swing() {
		swing(false);
	}

	public void swing(boolean silent) {
		if(silent)
			mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
		else
			mc.thePlayer.swingItem();
	}

	//todo finish
}