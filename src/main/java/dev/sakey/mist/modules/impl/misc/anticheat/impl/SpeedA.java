package dev.sakey.mist.modules.impl.misc.anticheat.impl;

import dev.sakey.mist.modules.impl.misc.anticheat.Check;
import net.minecraft.entity.player.EntityPlayer;

public class SpeedA extends Check {

    public SpeedA() {
        super("speed");
    }

    public void check(EntityPlayer player) {
        final float deltaYaw = player.cameraYaw = player.prevCameraYaw;
        final float deltaPitch = player.cameraPitch = player.prevCameraPitch;

        final boolean invalidPitch = deltaPitch > 0 && deltaPitch < 0.005 && deltaYaw > 2.5;
        final boolean invalidYaw = deltaYaw > 0 && deltaYaw < 0.005 && deltaPitch > 2.5;

        if(invalidPitch || invalidYaw) {

        }
    }
}
