package dev.sakey.mist.utils.client;

import dev.sakey.mist.ui.menus.MainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreenServerList;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;

public class NetworkingUtils {

	public static void Reconnect(){
		ServerData server = Minecraft.getMinecraft().getCurrentServerData();
		Minecraft.getMinecraft().theWorld.sendQuittingDisconnectingPacket();
		Minecraft.getMinecraft().displayGuiScreen(new GuiConnecting(
				new GuiMultiplayer(new MainMenu()), // if they escape, go to server select and if the escape, go to main menu
				Minecraft.getMinecraft(), server));
	}

}
