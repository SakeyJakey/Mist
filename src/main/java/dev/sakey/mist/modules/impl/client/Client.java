package dev.sakey.mist.modules.impl.client;

import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.player.EventMotion;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.modules.settings.impl.ModeSetting;
import dev.sakey.mist.modules.settings.impl.NumberSetting;
import net.minecraft.entity.Entity;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class Client extends Module {

	public ModeSetting theme = new ModeSetting("Theme", "Gamer", "");

	@ModuleInfo(name = "Client", category = Category.CLIENT)
	public Client() {
	}

}