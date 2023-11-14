package dev.sakey.mist.utils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import net.minecraft.client.gui.GuiScreen;

public class MiscUtils {

	public static void copyToClipboard(String textToCopy) {
//		StringSelection stringSelection = new StringSelection(textToCopy);
//		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//		clipboard.setContents(stringSelection, null);
		GuiScreen.setClipboardString(textToCopy);
	}
}
