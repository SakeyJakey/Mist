package dev.sakey.mist.modules.impl.misc;

import dev.sakey.mist.Mist;
import dev.sakey.mist.events.Direction;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.client.EventPacket;
import dev.sakey.mist.events.impl.flag.EventLagback;
import dev.sakey.mist.events.impl.flag.EventWorldChange;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.utils.client.TimerUtil;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.input.Keyboard;

public class AntiIdiot extends Module {

    @ModuleInfo(name = "AntiIdiot", description = "Detects lagbacks and world changes. Also prevents conflicting modules being on at the same time.", category = Category.MOVEMENT)
    public AntiIdiot() { }

    public void onEnable(){
        Mist.instance.getEventManager().registerEventHandler(EventPacket.class, eventPacket);
        timeout.reset();
    }
    public void onDisable(){
        Mist.instance.getEventManager().unregisterEventHandler(eventPacket);
    }

    TimerUtil timeout = new TimerUtil();

    EventHandler<EventPacket> eventPacket = e -> {

        if(e.getDirection() == Direction.OUT) return;
        //TODO: or op and conflicts

        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) e.getPacket();
            if(mc.thePlayer == null || mc.thePlayer.ticksExisted < 4) {
                Mist.instance.getEventManager().handleEvent(new EventWorldChange());
                if(!timeout.hasTimeElapsed(5000)) return;
                //new Notification(getName(), "Detected world change", NotificationType.INFO, 5000);
            }
            else {
                Mist.instance.getEventManager().handleEvent(new EventLagback());
                if(!timeout.hasTimeElapsed(5000)) return;
                //new Notification(getName(), "Detected lagback", NotificationType.WARNING, 5000);
            }
            timeout.reset();
        }
    };
}