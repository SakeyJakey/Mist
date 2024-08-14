package dev.sakey.mist.modules.impl.combat;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import dev.sakey.mist.Mist;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.player.EventMotion;
import dev.sakey.mist.events.impl.render.EventRenderHUD;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.annotations.DisableOnWorldChange;
import dev.sakey.mist.modules.settings.impl.BoolSetting;
import dev.sakey.mist.modules.settings.impl.ModeSetting;
import dev.sakey.mist.modules.settings.impl.NumberSetting;
import dev.sakey.mist.utils.render.ColourUtil;
import dev.sakey.mist.utils.client.font.GlyphPageFontRenderer;
import dev.sakey.mist.utils.render.MaskUtils;
import dev.sakey.mist.utils.render.RenderUtils;
import dev.sakey.mist.utils.client.TimerUtil;
import dev.sakey.mist.utils.player.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;


import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C0APacketAnimation;

public class KillAura extends Module {

    public EntityLivingBase target;
    List<EntityLivingBase> targets;

    public TimerUtil timer = new TimerUtil();

    NumberSetting speed = new NumberSetting("APS", 17, 1, 20, 0.5);

    NumberSetting reach = new NumberSetting("Reach", 3.5, 1, 10, 0.02);

    BoolSetting noSwing = new BoolSetting("No Swing", false);

    BoolSetting showRots = new BoolSetting("Show Rotations", false);

    BoolSetting thirdPerson = new BoolSetting("Auto 3rd Person", false);

    BoolSetting smoothBool = new BoolSetting("Smooth", false);
    NumberSetting smoothing = new NumberSetting("Smoothing", 2, 1, 5, 0.5);

    BoolSetting throughWalls = new BoolSetting("Through Walls", false);

    ModeSetting track = new ModeSetting("Track Mode", "Track", "Legit");

    BoolSetting targetPlayers = new BoolSetting("Players", true);
    BoolSetting targetAnimals = new BoolSetting("Animals", true);
    BoolSetting targetMobs = new BoolSetting("Mobs", true);
    BoolSetting targetInvisible = new BoolSetting("Invisible", false);

    @ModuleInfo(name = "KillAura", description = "Kills other entities automatically", category = Category.COMBAT)
    @DisableOnWorldChange
    public KillAura() {
        addSettings(speed, reach, showRots, noSwing, thirdPerson, smoothing, throughWalls, track, targetPlayers, targetAnimals, targetMobs, targetInvisible);
    }

    public String getSuffix() { return ""+ speed.getValue(); }


    public void onEnable(){
        Mist.instance.getEventManager().registerEventHandler(EventMotion.class, motionEventOrig);
        Mist.instance.getEventManager().registerEventHandler(EventRenderHUD.class, renderOverlay);

        alpha = 0;
    }

    public void onDisable() {
        Mist.instance.getEventManager().unregisterEventHandler(motionEventOrig);
        Mist.instance.getEventManager().unregisterEventHandler(renderOverlay);
    }

    float[] originalRot;

    EventHandler<EventMotion> motionEventOrig = e -> {
        if (e.isPost()) return;

        targets = new ArrayList<EntityLivingBase>();

        List<EntityLivingBase> tempTargets;

        tempTargets = (List<EntityLivingBase>) mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
        tempTargets = tempTargets.stream().filter(
                entity -> entity.getDistanceToEntity(mc.thePlayer) < reach.getValue() &&
                        entity != mc.thePlayer &&
                        !entity.isDead && entity.getHealth() > 0 &&
                        (!entity.isInvisible() || targetInvisible.isEnabled()) &&
                        !entity.getName().toLowerCase().contains("shop") &&
                        !entity.getName().toLowerCase().contains("item") &&
                        !entity.getName().toLowerCase().contains("upgrade")
        ).collect(Collectors.toList());
        tempTargets.sort(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(mc.thePlayer)));

        List<EntityLivingBase> players = tempTargets.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());
        List<EntityLivingBase> mobs = tempTargets.stream().filter(EntityMob.class::isInstance).collect(Collectors.toList());
        List<EntityLivingBase> animals = tempTargets.stream().filter(EntityAnimal.class::isInstance).collect(Collectors.toList());

        if (targetPlayers.isEnabled()) targets.addAll(players);
        if (targetMobs.isEnabled()) targets.addAll(mobs);
        if (targetAnimals.isEnabled()) targets.addAll(animals);


        if (!targets.isEmpty()) {

            if (thirdPerson.isEnabled()) mc.gameSettings.thirdPersonView = 1;

            target = targets.get(0);

            if (showRots.isEnabled()) {
                mc.thePlayer.renderYawOffset = RotationUtils.getRotationsSmooth(target, smoothing.getValue())[0];
				mc.thePlayer.rotationYawHead = RotationUtils.getRotationsSmooth(target, smoothing.getValue())[0];
				mc.thePlayer.rotationPitchHead = RotationUtils.getRotationsSmooth(target, smoothing.getValue())[1];

                mc.thePlayer.rotationYaw = RotationUtils.getRotationsSmooth(target, smoothing.getValue())[0];
                mc.thePlayer.rotationPitch = RotationUtils.getRotationsSmooth(target, smoothing.getValue())[1];

                e.setYaw(RotationUtils.getRotationsSmooth(target, smoothing.getValue())[0]);
                e.setPitch(RotationUtils.getRotationsSmooth(target, smoothing.getValue())[1]);

            } else {
                e.setYaw(RotationUtils.getRotationsSmooth(target, smoothing.getValue())[0]);
                e.setPitch(RotationUtils.getRotationsSmooth(target, smoothing.getValue())[1]);
            }

            e.setYaw(RotationUtils.fixRotation(e.getYaw()));
            e.setPitch(RotationUtils.fixRotation(e.getPitch()));

            block(true);

            if(Objects.equals(getMouseOver(e.getYaw(), e.getPitch()).entityHit, target)) {
                if (timer.hasTimeElapsed((long) (1000 / speed.getValue()), true)) {

                    block(false);

                    if(noSwing.isEnabled())
                        mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                    else
                        mc.thePlayer.swingItem();

                    mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));

                    block(true);
                }
            }

        }
        else {
            if (thirdPerson.isEnabled()) mc.gameSettings.thirdPersonView = 0; //TODO: was already 3rd person?
        }
    };

    private void block(boolean start) {
        if(start) {
            if (mc.thePlayer.getHeldItem() != null)
                mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(), mc.thePlayer.getHeldItem().getMaxItemUseDuration());
            return;
        }
        mc.thePlayer.stopUsingItem();
    }

    private MovingObjectPosition getMouseOver(float yaw, float pitch) {
        float[] original = { mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch };

        mc.thePlayer.rotationYaw = yaw;
        mc.thePlayer.rotationPitch = pitch;

        mc.entityRenderer.getMouseOver(mc.timer.renderPartialTicks);

        MovingObjectPosition temp = mc.objectMouseOver;

        mc.thePlayer.rotationYaw = original[0];
        mc.thePlayer.rotationPitch = original[1];

        // Reset it?
        mc.entityRenderer.getMouseOver(mc.timer.renderPartialTicks);

        return temp;
    }

    double health = 0,
            alpha = 0,
            hurt = 0,
            lTHurt = 0;
    ;
    EventHandler<EventRenderHUD> renderOverlay = e -> {

        ScaledResolution sr = new ScaledResolution(mc);

        if(target == null || target.isDead || target.getHealth() <= 0 || target.getDistanceToEntity(mc.thePlayer) > reach.getValue()) {
            alpha -= alpha / (mc.getDebugFPS() / 4);
        } else {
            alpha += (1 - alpha) / (mc.getDebugFPS() / 4);
        }

        double tHurt = target == null ? 0 : Math.max(1, target.hurtTime) / 4;

        hurt += (tHurt - hurt) / (mc.getDebugFPS() / 8);

        if(Math.abs(lTHurt - tHurt) > 1) hurt = tHurt;
        lTHurt = tHurt;

        double
                x = sr.getScaledWidth() / 2 - 50 + hurt,
                y = sr.getScaledHeight() / 2 + 10 + hurt,
                w =	sr.getScaledWidth() / 2 + 50 - hurt,
                h =	sr.getScaledHeight() / 2 + 10 + 50 - hurt,
                tHealth = target == null ? w : x + ((w - x) / target.getMaxHealth()) * target.getHealth();


        if(health == 0 && target != null) health = x + ((w - x) / target.getMaxHealth()) * target.getHealth();
        else health += (tHealth - health) / (Minecraft.getDebugFPS() / 4);

        MaskUtils.UI.beginDrawMask();
        RenderUtils.drawRoundedRect(
                x, y, w, h,
                5,
                -1
        );
        MaskUtils.UI.beginDrawContent();
        RenderUtils.drawRoundedRect(
                x, y, w, h,
                5,
                getAlphaColour(ColourUtil.grey())
        );


        if(hurt > 1) {
            RenderUtils.drawOutlineRoundedRect(
                    x, y, w, h,
                    5, (float) (hurt / 2),
                    getAlphaColour(Color.red.getRGB())
            );
        }

        RenderUtils.drawRect(
                x, h - 10, health, h, getAlphaColour(-1)
        );



        GlyphPageFontRenderer fr = Mist.instance.getFontRenderer(20);
        if(alpha > 0.02)
            fr.drawString(target.getName(), x + (w - x) / 2, y + fr.getFontHeight(), getAlphaColour(ColourUtil.white()), false, true);

        MaskUtils.UI.endMask();
    };

    private int getAlphaColour(int orig) {
        return ColourUtil.alphaColour(orig, (float)alpha);
    }

}
