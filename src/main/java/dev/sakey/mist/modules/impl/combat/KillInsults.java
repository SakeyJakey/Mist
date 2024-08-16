package dev.sakey.mist.modules.impl.combat;

import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.player.EventMotion;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import java.util.List;
import java.util.Random;

public class KillInsults extends Module {
	EventHandler<EventMotion> motionEventHandler = event -> {
		if (event.isPost()) return;
		for (Entity e :
				(List<Entity>) mc.theWorld.loadedEntityList) {
			if (e.isDead && e instanceof EntityPlayer && e != mc.thePlayer) {
				mc.thePlayer.sendChatMessage(MathHelper.randomFloatClamp(new Random(), 0, 1) + e.getName() + " " + " died. What an L!");
			}
		}
	};

	@ModuleInfo(name = "KillInsults", description = "Insults players when they die.", category = Category.COMBAT)
	public KillInsults() {
	}

	protected void onEnable() {
		registerEvent(EventMotion.class, motionEventHandler);
	}

	protected void onDisable() {
		unregisterEvent(motionEventHandler);
	}
}
