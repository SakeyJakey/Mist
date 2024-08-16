package dev.sakey.mist.modules.impl.player;

import dev.sakey.mist.Mist;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.player.EventMotion;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import net.minecraft.network.play.client.C03PacketPlayer;


public class NoFall extends Module {


	EventHandler<EventMotion> eventMotion = e -> {
		if (mc.thePlayer.fallDistance > 3) mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
	};

	@ModuleInfo(name = "NoFall", description = "Negates fall damage.", category = Category.PLAYER)
	public NoFall() {
	}

	public void onDisable() {
		Mist.instance.getEventManager().unregisterEventHandler(eventMotion);
	}


	public void onEnable() {
		Mist.instance.getEventManager().registerEventHandler(EventMotion.class, eventMotion);
	}
}