package dev.sakey.mist.modules.impl.misc.anticheat;

import dev.sakey.mist.Mist;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.player.EventMotion;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.modules.impl.misc.anticheat.impl.KillAuraA;
import dev.sakey.mist.modules.settings.impl.NumberSetting;
import net.minecraft.entity.player.EntityPlayer;

import java.util.concurrent.CopyOnWriteArrayList;

public class AntiCheat extends Module {

	public NumberSetting threshold = new NumberSetting("Threshold", 4, 1, 20, 1);
	public NumberSetting resetViolationLevel = new NumberSetting("Reset VL", -10, -20, 0, 1);
	CopyOnWriteArrayList<Check> checks = new CopyOnWriteArrayList<Check>();
	EventHandler<EventMotion> eventMotion = e -> {
		for (EntityPlayer p : mc.theWorld.playerEntities) {

			// if(p == mc.thePlayer) continue;

			for (Check c : checks) {
				c.check(p);
			}
		}
	};

	// EntityPlayer:82

	@ModuleInfo(name = "AntiCheat", description = "Alerts you if a player is cheating", category = Category.MISC)
	public AntiCheat() {
		checks.add(new KillAuraA());
	}

	public void onDisable() {
		Mist.instance.getEventManager().unregisterEventHandler(eventMotion);
	}


	public void onEnable() {
		Mist.instance.getEventManager().registerEventHandler(EventMotion.class, eventMotion);
	}
}
