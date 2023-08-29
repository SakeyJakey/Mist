package dev.sakey.mist.modules.impl.combat;

import dev.sakey.mist.Mist;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.player.EventMotion;
import dev.sakey.mist.events.impl.render.EventRenderHUD;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.ModuleInfo;
import dev.sakey.mist.modules.settings.impl.BoolSetting;
import dev.sakey.mist.modules.settings.impl.ModeSetting;
import dev.sakey.mist.modules.settings.impl.NumberSetting;
import dev.sakey.mist.utils.client.Colours;
import dev.sakey.mist.utils.client.RenderUtils;
import dev.sakey.mist.utils.client.TimerUtil;
import dev.sakey.mist.utils.client.animation.Animation;
import dev.sakey.mist.utils.player.RotationUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C0APacketAnimation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class KillSluts extends Module {
    @ModuleInfo(name = "KillSluts", description = "Insults players when they die.", key = Keyboard.KEY_L, category = Category.COMBAT)
    public KillSluts() {
    }

    EventHandler<EventMotion> motionEventHandler = event -> {
        for (Entity e :
                (List<Entity>)mc.theWorld.loadedEntityList) {
            if(e.isDead) {
                mc.thePlayer.sendChatMessage(e.getName() + " died. What an L!");
            }
        }
    };
}
