package dev.sakey.mist.modules.impl.misc.anticheat;

import dev.sakey.mist.Mist;
import dev.sakey.mist.ui.notifications.Notification;
import dev.sakey.mist.ui.notifications.NotificationType;
import net.minecraft.entity.player.EntityPlayer;

public abstract class Check {

    protected String name;

    public Check(String name) {
        this.name = name;
    }

    public abstract void check(EntityPlayer player);

    public void flag(EntityPlayer player) {
        player.mistViolationLevel++;

        AntiCheat ac = (AntiCheat) Mist.instance.getModuleManager().getModule(AntiCheat.class);

        if(player.mistViolationLevel > ac.threshold.getValue()) {
            new Notification("AntiCheat", player.getName() + " might be hacking (" + this.name + ")", NotificationType.INFO, 3000);
            player.mistViolationLevel = (int) ac.resetViolationLevel.getValue();
        }
    }
}
