package dev.sakey.mist.modules;

import dev.sakey.mist.Mist;

public enum Category {
	CLIENT(Mist.instance.name),
	COMBAT("Combat"),
	MOVEMENT("Movement"),
	RENDER("Render"),
	PLAYER("Player"),
	EXPLOIT("Exploit"),
	HUD("HUD"),
	MISC("Misc");

	public final String name;

	Category(String name) {
		this.name = name;
	}

	public static Category getCategoryByName(String categoryName) {
		for (Category c :
				Category.values()) {
			if (c.name.equals(categoryName))
				return c;
		}
		return null;
	}
}