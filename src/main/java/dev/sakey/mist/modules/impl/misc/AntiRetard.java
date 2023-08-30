package dev.sakey.mist.modules.impl.misc;

import dev.sakey.mist.Mist;
import dev.sakey.mist.events.Direction;
import dev.sakey.mist.events.Event;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.EventManager;
import dev.sakey.mist.events.impl.client.EventPacket;
import dev.sakey.mist.events.impl.flag.EventLagback;
import dev.sakey.mist.events.impl.flag.EventWorldChange;
import dev.sakey.mist.events.impl.player.EventMotion;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.ModuleInfo;
import dev.sakey.mist.modules.settings.impl.BoolSetting;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.input.Keyboard;

public class AntiRetard extends Module {

    @ModuleInfo(name = "AntiRetard", description = "Detects lagbacks and world changes. Also prevents conflicting modules being on at the same time.", key = Keyboard.KEY_T, category = Category.MOVEMENT)
    public AntiRetard() { }

    public void onEnable(){
        Mist.instance.getEventManager().registerEventHandler(EventPacket.class, eventPacket);
    }
    public void onDisable(){
        Mist.instance.getEventManager().unregisterEventHandler(eventPacket);
    }


    EventHandler<EventPacket> eventPacket = e -> {
        if(e.getDirection() == Direction.OUT) return;
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) e.getPacket();
            if(mc.thePlayer == null || mc.thePlayer.ticksExisted < 4)
                Mist.instance.getEventManager().handleEvent(new EventWorldChange());
            else
                Mist.instance.getEventManager().handleEvent(new EventLagback());
        }
    };
}