package dev.sakey.mist.utils.themes.impl;

import dev.sakey.mist.utils.themes.Theme;

import java.awt.*;

public class MistTheme extends Theme {

	public int getPrimaryColour() {
		return new Color(156, 136, 255).getRGB();
	}

	public int getPrimaryDimColour() {
		return new Color(140, 122, 230).getRGB();
	}

	public int getSecondaryColour() {
		return new Color(76, 209, 55).getRGB();
	}

	public int getSecondaryDimColour() {
		return new Color(68, 189, 50).getRGB();
	}

	public int getBackgroundColour() {
		return new Color(53, 59, 72).getRGB();
	}

	public int getBackgroundDimColour() {
		return new Color(47, 54, 64).getRGB();
	}

	public int getForegroundColour() {
		return new Color(245, 246, 250).getRGB();
	}

	public int getForegroundDimColour() {
		return new Color(220, 221, 225).getRGB();
	}
}