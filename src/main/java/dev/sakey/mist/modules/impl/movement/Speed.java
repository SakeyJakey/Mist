package dev.sakey.mist.modules.impl.movement;

import dev.sakey.mist.Mist;
import dev.sakey.mist.events.Direction;
import dev.sakey.mist.events.Event;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.EventManager;
import dev.sakey.mist.events.impl.client.EventPacket;
import dev.sakey.mist.events.impl.player.EventMotion;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.ModuleInfo;
import dev.sakey.mist.utils.player.MovementUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

public class Speed extends Module {
    @ModuleInfo(name = "Speed", description = "Makes you move faster", key = Keyboard.KEY_R, category = Category.MOVEMENT)
    public Speed() { }

    protected void onEnable() {
        Mist.instance.getEventManager().registerEventHandler(EventMotion.class, eventMotion);
        Mist.instance.getEventManager().registerEventHandler(EventPacket.class, eventPacket);
    }

    protected void onDisable(){
        Mist.instance.getEventManager().unregisterEventHandler(eventMotion);
        Mist.instance.getEventManager().unregisterEventHandler(eventPacket);
    }

    EventHandler<EventMotion> eventMotion = e -> {
        if(mc.thePlayer.onGround && MovementUtils.isMoving()) {
            MovementUtils.setSpeed(0);
            //mc.thePlayer.motionY = 0.1;
            mc.thePlayer.jump();
            mc.thePlayer.renderOffsetY = 1;
        } else {
            MovementUtils.setSpeed(0.5);
            mc.thePlayer.motionY = -0.1;
            mc.thePlayer.renderOffsetY = -1;
        }
    };

    EventHandler<EventPacket> eventPacket = e -> {
        if(e.getDirection() == Direction.IN) return;
        if(e.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition) {
            C03PacketPlayer.C04PacketPlayerPosition packet = (C03PacketPlayer.C04PacketPlayerPosition) e.getPacket();
            packet.setMoving(false);
            e.setPacket(packet);
        }
    };

}
