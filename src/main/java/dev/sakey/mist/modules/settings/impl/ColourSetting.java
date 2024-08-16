package dev.sakey.mist.modules.settings.impl;

import dev.sakey.mist.modules.settings.Setting;

import java.awt.*;

public class ColourSetting extends Setting {

	private int colour;

	public ColourSetting(String name, int colour) {
		this.name = name;
		this.colour = colour;
	}

	public int getColourInt() {
		return colour;
	}

	public void setColourInt(int colour) {
		this.colour = colour;
	}

	public Color getColour() {
		return new Color(colour);
	}

	public void setColour(Color colour) {
		this.colour = colour.getRGB();
	}
}
