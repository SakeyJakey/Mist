package dev.sakey.mist.utils.themes;

import dev.sakey.mist.utils.themes.impl.MistTheme;

import java.util.concurrent.CopyOnWriteArrayList;

public class ThemeUtil {

	static CopyOnWriteArrayList<Theme> themes = new CopyOnWriteArrayList<Theme>();

	public static void getTheme() {
		themes.add(new MistTheme());
	}
}
