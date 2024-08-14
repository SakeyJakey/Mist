package dev.sakey.mist.modules.impl.movement;

import dev.sakey.mist.Mist;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.player.EventMotion;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.DisableOnLagback;
import dev.sakey.mist.modules.annotations.DisableOnWorldChange;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.modules.settings.impl.ModeSetting;
import dev.sakey.mist.modules.settings.impl.NumberSetting;
import dev.sakey.mist.utils.player.MovementUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

public class Fly extends Module {

    ModeSetting mode = new ModeSetting("Mode", "Motion", "Watchdog");
    NumberSetting speed = new NumberSetting("Speed", 2, 1, 10, 0.5);

    @ModuleInfo(name = "Fly", description = "Makes you float.", category = Category.MOVEMENT)
    @DisableOnLagback
    public Fly() {
        //addSettings(mode, speed);
    }


    public void onEnable() {
        Mist.instance.getEventManager().registerEventHandler(EventMotion.class, eventMotionHandler);
        mc.thePlayer.performHurtAnimation();
        mc.thePlayer.capabilities.allowFlying = true;
        mc.thePlayer.capabilities.isFlying = true;

        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ);
    }

    public void onDisable() {
        Mist.instance.getEventManager().unregisterEventHandler(eventMotionHandler);
        mc.thePlayer.capabilities.allowFlying = false;
        mc.thePlayer.capabilities.isFlying = false;
    }

    EventHandler<EventMotion> eventMotionHandler = e -> {
        //e.setOnGround(true);

        if(mc.thePlayer.ticksExisted % 10 == 0) {
            e.setY(e.getY() - 0.1);
        }
        else {
            e.setY(e.getY() + (0.1 / 9));
        }
    };
}
