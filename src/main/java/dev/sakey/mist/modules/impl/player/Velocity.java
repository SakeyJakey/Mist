package dev.sakey.mist.modules.impl.player;

import dev.sakey.mist.Mist;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.client.EventPacket;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.modules.settings.impl.ModeSetting;
import dev.sakey.mist.modules.settings.impl.NumberSetting;
import net.minecraft.network.play.server.S12PacketEntityVelocity;


public class Velocity extends Module {

	ModeSetting mode = new ModeSetting("Mode", "Reduce", "Cancel");
	NumberSetting reduceAmountX = new NumberSetting("Reduce X", 0.5, 0.01, 1, 0.01);
	NumberSetting reduceAmountY = new NumberSetting("Reduce Y", 0.5, 0.01, 1, 0.01);
	NumberSetting reduceAmountZ = new NumberSetting("Reduce Z", 0.5, 0.01, 1, 0.01);
	EventHandler<EventPacket> eventPacket = e -> {
		if (e.getPacket() instanceof S12PacketEntityVelocity p && e.isPre()) {
			if (p.getEntityID() != mc.thePlayer.getEntityId()) return;

			if (mode.is("Cancel"))
				e.setCancelled(true);

			else {
				p.setMotionX((int) (p.getMotionX() * (1 - reduceAmountX.getValue())));
				p.setMotionY((int) (p.getMotionY() * (1 - reduceAmountX.getValue())));
				p.setMotionZ((int) (p.getMotionZ() * (1 - reduceAmountX.getValue())));
				e.setPacket(p);
			}
		}
	};

	@ModuleInfo(name = "Velocity", description = "Negates knockback.", category = Category.PLAYER)
	public Velocity() {
		reduceAmountX.addParent(mode, mode -> mode.is("Reduce"));
		reduceAmountY.addParent(mode, mode -> mode.is("Reduce"));
		reduceAmountZ.addParent(mode, mode -> mode.is("Reduce"));
		addSettings(mode, reduceAmountX, reduceAmountY, reduceAmountZ);
	}

	public void onDisable() {
		Mist.instance.getEventManager().unregisterEventHandler(eventPacket);
	}


	public void onEnable() {
		Mist.instance.getEventManager().registerEventHandler(EventPacket.class, eventPacket);
	}
}