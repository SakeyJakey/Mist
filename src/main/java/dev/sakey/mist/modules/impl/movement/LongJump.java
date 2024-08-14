package dev.sakey.mist.modules.impl.movement;

import dev.sakey.mist.Mist;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.player.EventMotion;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.utils.client.TimerUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

public class LongJump extends Module {

    @ModuleInfo(name = "LongJump", description = "Jumps farther", category = Category.MOVEMENT)
    public LongJump() { }

    TimerUtil time = new TimerUtil();

    public void onEnable(){
		hasJumped = false;
		time.reset();
        Mist.instance.getEventManager().registerEventHandler(EventMotion.class, eventMotionHandler);
    }
    public void onDisable(){
        Mist.instance.getEventManager().unregisterEventHandler(eventMotionHandler);
    }


    boolean hasJumped = false;
    EventHandler<EventMotion> eventMotionHandler = e -> {
        if(time.hasTimeElapsed(2500) && hasJumped){
            mc.thePlayer.motionX *= 5;
            mc.thePlayer.motionZ *= 5;
            quietToggle();
        }
        else if(time.hasTimeElapsed(2000) && !hasJumped) {
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.001, mc.thePlayer.posZ, false));
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.2, mc.thePlayer.posZ, true));

			mc.thePlayer.onGround = true;
			mc.thePlayer.jump();
            mc.thePlayer.motionY += 0.4;
            hasJumped = true;
        }
        else
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.lastTickPosX, mc.thePlayer.posY - 0.1, mc.thePlayer.lastTickPosZ, false));
    };
}
