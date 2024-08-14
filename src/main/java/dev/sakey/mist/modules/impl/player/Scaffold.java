//package dev.sakey.mist.modules.impl.player;
//
//import dev.sakey.mist.Mist;
//import dev.sakey.mist.events.EventHandler;
//import dev.sakey.mist.events.impl.player.EventMotion;
//import dev.sakey.mist.events.impl.render.EventRenderHUD;
//import dev.sakey.mist.events.impl.render.EventRenderWorld;
//import dev.sakey.mist.modules.Category;
//import dev.sakey.mist.modules.Module;
//import dev.sakey.mist.modules.annotations.ModuleInfo;
//import dev.sakey.mist.modules.settings.impl.BoolSetting;
//import dev.sakey.mist.modules.settings.impl.ModeSetting;
//import dev.sakey.mist.modules.settings.impl.NumberSetting;
//import dev.sakey.mist.utils.client.TimerUtil;
//import dev.sakey.mist.utils.player.RotationUtils;
//import net.minecraft.block.BlockAir;
//import net.minecraft.client.gui.ScaledResolution;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.entity.projectile.EntityEgg;
//import net.minecraft.init.Blocks;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemBlock;
//import net.minecraft.network.play.client.C09PacketHeldItemChange;
//import net.minecraft.network.play.client.C0APacketAnimation;
//import net.minecraft.util.*;
//import org.lwjgl.input.Keyboard;
//import org.lwjgl.opengl.GL11;
//
//
//public class Scaffold extends Module {
//
//
//    @ModuleInfo(name = "Scaffold", key = Keyboard.KEY_K, description = "Places blocks underneath you.", category = Category.PLAYER)
//    public Scaffold() {
//        addSettings(mode, noSwing, safewalk, tower, towerTimer, rots, delay, ninja);
//        towerTimer.addParent(tower, m -> m.isEnabled());
//    }
//
//    EnumFacing currentFace;
//    BlockPos currentBlock;
//    boolean rotated = false;
//
//    TimerUtil timer = new TimerUtil();
//
//    public static BoolSetting safewalk = new BoolSetting("Safewalk", true);
//
//    BoolSetting tower = new BoolSetting("Tower", true);
//    NumberSetting towerTimer = new NumberSetting("Tower Timer", 1, 0.2, 5, 0.2);
//    NumberSetting delay = new NumberSetting("Delay", 0.6, 0, 5, 0.2);
//    BoolSetting ninja = new BoolSetting("Ninja", false);
//    BoolSetting noSwing = new BoolSetting("NoSwing", true);
//
//    ModeSetting rots = new ModeSetting("Rotations", "Head", "Full", "Hide");
//
//    ModeSetting mode = new ModeSetting("Mode", "Pre", "Post");
//
//    BlockPos under;
//
//    int lastItem;
//    int item;
//    EventHandler<EventMotion> eventMotion = e -> {
//
//        under = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
//
//        boolean done = false;
//        for (int i = 0; i < 8; i++) {
//            if(done) break;
//            if(mc.thePlayer.inventory.mainInventory[i] != null && canPlace(mc.thePlayer.inventory.mainInventory[i].getItem()) && mc.thePlayer.inventory.mainInventory[i].getItem() instanceof ItemBlock && mc.thePlayer.inventory.mainInventory[i].stackSize != 0){
//                done = true;
//                item = i;
//            }
//        }
//
//        if(!done) return;
//
//        if(!ninja.isEnabled() || !mc.theWorld.isAirBlock(under))
//            mc.gameSettings.keyBindSneak.pressed = false;
//
//        if(mc.gameSettings.keyBindJump.isKeyDown() && tower.isEnabled() && !mc.theWorld.isAirBlock(under)) {
//            mc.timer.timerSpeed = (float) towerTimer.getValue();
//
//            mc.thePlayer.motionY *= 0.94325215; // an almost perfect amount that jumps just high enough for a block to be placed but not too high so that time is wasted falling
//            if(mc.thePlayer.onGround) {
//                mc.thePlayer.jump();
//                //mc.thePlayer.onGround = true;
//            }
//
//            //mc.thePlayer.motionY = 0.3;
//        }
//        else mc.timer.timerSpeed = 1f;
//
//
//        if(mc.theWorld.getBlockState(under).getBlock() instanceof BlockAir){
//
//
//            //todo: add water checks
//            //                mc.theWorld.getBlockState(under).getBlock() != Blocks.water &&
//            //                mc.theWorld.getBlockState(under).getBlock() != Blocks.lava &&
//            //                mc.theWorld.getBlockState(under).getBlock() != Blocks.flowing_water &&
//            //                mc.theWorld.getBlockState(under).getBlock() != Blocks.flowing_lava
//            setBlockAndFacing(under);
//            if(currentBlock == null) return;
//            float[] rotations = getDirectionToBlock(currentBlock.getX(), currentBlock.getY(), currentBlock.getZ(), currentFace);
//
//            float yaw = rotations[0];
//            float pitch = rotations[1];
//            //todo: inventory add
//
//            rotated = true;
//
//            mc.thePlayer.motionX = 0;
//            mc.thePlayer.motionZ = 0;
//            e.setYaw(yaw);
//            e.setPitch(pitch);
//
//            switch (rots.getMode()) {
//                case "Head":
//                    mc.thePlayer.rotationYawHead = yaw;
//                    mc.thePlayer.rotationPitchHead = pitch;
//                    break;
//                case "Full":
//                    mc.thePlayer.rotationYaw = yaw;
//                    mc.thePlayer.rotationPitch = pitch;
//                    break;
//                case "Hide":
//                    break;
//            }
//
//        }
//
//
//        switch (e.getType()){
//            case PRE:
//                if(mode.is("Pre")) {
//                    place(e);
//                }
//                break;
//            case POST:
//                if(mode.is("Post"))
//                    place(e);
//                break;
//        }
//    };
//
//    void place(EventMotion e){
//
//        int lastItemHeld = mc.thePlayer.inventory.currentItem;
//
//        float yaw = mc.thePlayer.rotationYaw;
//        float pitch = mc.thePlayer.rotationPitch;
//
///*        mc.thePlayer.rotationYaw = e.getYaw();
//        mc.thePlayer.rotationPitch = e.getPitch();
//
//        if(mc.objectMouseOver.getBlockPos() == null && mc.objectMouseOver.sideHit != currentFace) return;
//
//        mc.thePlayer.rotationYaw = yaw;
//        mc.thePlayer.rotationPitch = pitch;*/
//        if(currentBlock != null && timer.hasTimeElapsed((long) (delay.getValue() * 1000))) {
//
//            if(lastItem != item && mc.thePlayer.inventory.currentItem != item)
//                mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(item));
//
//            lastItem = item;
//
//            if (mc.thePlayer.inventory.mainInventory[item] != null && mc.thePlayer.inventory.mainInventory[item].getItem() instanceof ItemBlock && mc.thePlayer.inventory.mainInventory[item].stackSize > 0) {
//
//                if (!noSwing.isEnabled())
//                    mc.thePlayer.swingItem();
//                else
//                    mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
//
//                if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.mainInventory[item],
//                        currentBlock, currentFace,
//                        new Vec3(currentBlock.getX(), currentBlock.getY(), currentBlock.getZ()))) {
//                    timer.reset();
//                    if(ninja.isEnabled())
//                        mc.gameSettings.keyBindSneak.pressed = true;
//                }
//
//            }
//
//
//            if(lastItem != lastItemHeld && mc.thePlayer.inventory.currentItem != lastItemHeld)
//                mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(lastItemHeld));
//
//        }
//
//
//    }
//
//    EventHandler<EventRenderWorld> renderWorld = e -> {
//        //if(under != null) BoxUtils.drawBox(new BlockPos(Math.round(under.getX()),
//        //                                                Math.round(under.getY()),
//        //                                                Math.round(under.getZ())));
//    };
//
//    EventHandler<EventRenderHUD> renderOverlay = e -> {
//        if(mc.thePlayer.inventory.mainInventory[item] == null) return;
//        ScaledResolution sr = new ScaledResolution(mc);
//// todo: all blocks size not just one stack
//        Mist.instance.getFontRenderer().drawString("Blocks: " + mc.thePlayer.inventory.mainInventory[item].stackSize, sr.getScaledWidth() / 2 + 10, sr.getScaledHeight() / 2 + 10, -1, true);
//        GL11.glColor4d(1, 1, 1, 0);
//        mc.getRenderItem().zLevel = 100;
//        GlStateManager.enableLighting();
//        mc.getRenderItem().renderItemAndEffectIntoGUI(mc.thePlayer.inventory.mainInventory[item], sr.getScaledWidth() / 2 - 7, sr.getScaledHeight() / 2 + 7);
//        GlStateManager.disableLighting();
//    };
//
//    private void setBlockAndFacing(BlockPos pos) {
//
//        if (mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.air) {
//
//            currentBlock = pos.add(0, -1, 0);
//
//            currentFace = EnumFacing.UP;
//
//        }
//        else if (mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.air) {
//
//            currentBlock = pos.add(-1, 0, 0);
//
//            currentFace = EnumFacing.EAST;
//
//        }
//        else if (mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.air) {
//
//            currentBlock = pos.add(1, 0, 0);
//
//            currentFace = EnumFacing.WEST;
//
//        }
//        else if (mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.air) {
//
//            currentBlock = pos.add(0, 0, -1);
//
//            currentFace = EnumFacing.SOUTH;
//
//        }
//        else if (mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.air) {
//
//            currentBlock = pos.add(0, 0, 1);
//
//            currentFace = EnumFacing.NORTH;
//
//        }
//        else if (mc.theWorld.getBlockState(pos.add(-1, 0, 1)).getBlock() != Blocks.air){
//
//            currentBlock = pos.add(-1, 0, 1);
//
//            currentFace = EnumFacing.EAST;
//
//        }
//        else if (mc.theWorld.getBlockState(pos.add(1, 0, 1)).getBlock() != Blocks.air){
//
//            currentBlock = pos.add(1, 0, 1);
//
//            currentFace = EnumFacing.NORTH;
//
//        }
//        else if (mc.theWorld.getBlockState(pos.add(-1, 0, -1)).getBlock() != Blocks.air){
//
//            currentBlock = pos.add(-1, 0, -1);
//
//            currentFace = EnumFacing.SOUTH;
//
//        }
//        else if (mc.theWorld.getBlockState(pos.add(1, 0, -1)).getBlock() != Blocks.air){
//
//            currentBlock = pos.add(1, 0, -1);
//
//            currentFace = EnumFacing.SOUTH;
//
//        }
//        else {
//            currentBlock = null;
//            currentFace = null;
//        }
//
//    }
//
//    /*void setBlockAndFacing(BlockPos pos) {
//        if (mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.air) {
//            currentBlock = pos.add(0, -1, 0);
//            currentFace = EnumFacing.UP;
//            retried = false;
//        }
//
//        else if (mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.air) {
//            currentBlock = pos.add(-1, 0, 0);
//            currentFace = EnumFacing.EAST;
//            retried = false;
//        }
//
//        else if (mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.air) {
//            currentBlock = pos.add(1, 0, 0);
//            currentFace = EnumFacing.WEST;
//            retried = false;
//        }
//
//        else if (mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.air) {
//            currentBlock = pos.add(0, 0, -1);
//            currentFace = EnumFacing.SOUTH;
//            retried = false;
//        }
//
//        else if (mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.air) {
//            currentBlock = pos.add(0, 0, 1);
//            currentFace = EnumFacing.NORTH;
//            retried = false;
//        }
//
//        else {
//            currentBlock = null;
//            currentFace = null;
//        }
//    }*/
//
//
//    float[] getDirectionToBlock(int x, int y, int z, EnumFacing face){
//        EntityEgg pos = new EntityEgg(mc.theWorld);
//        pos.posX = (double) x + 0.5;
//        pos.posY = (double) y + 0.5;
//        pos.posZ = (double) z + 0.5;
//        pos.posX += (double) face.getDirectionVec().getX() * 0.25;
//        pos.posY += (double) face.getDirectionVec().getY() * 0.25;
//        pos.posZ += (double) face.getDirectionVec().getZ() * 0.25;
//        return RotationUtils.getRotationsNeeded(pos);
//    }
//
//
//
//    public boolean canPlace(Item item) {
//        if (item.getIdFromItem(item) == 116)
//            return false;
//        if (item.getIdFromItem(item) == 30)
//            return false;
//        if (item.getIdFromItem(item) == 31)
//            return false;
//        if (item.getIdFromItem(item) == 175)
//            return false;
//        if (item.getIdFromItem(item) == 28)
//            return false;
//        if (item.getIdFromItem(item) == 27)
//            return false;
//        if (item.getIdFromItem(item) == 66)
//            return false;
//        if (item.getIdFromItem(item) == 157)
//            return false;
//        if (item.getIdFromItem(item) == 31)
//            return false;
//        if (item.getIdFromItem(item) == 6)
//            return false;
//        if (item.getIdFromItem(item) == 31)
//            return false;
//        if (item.getIdFromItem(item) == 32)
//            return false;
//        if (item.getIdFromItem(item) == 140)
//            return false;
//        if (item.getIdFromItem(item) == 390)
//            return false;
//        if (item.getIdFromItem(item) == 37)
//            return false;
//        if (item.getIdFromItem(item) == 38)
//            return false;
//        if (item.getIdFromItem(item) == 39)
//            return false;
//        if (item.getIdFromItem(item) == 40)
//            return false;
//        if (item.getIdFromItem(item) == 69)
//            return false;
//        if (item.getIdFromItem(item) == 50)
//            return false;
//        if (item.getIdFromItem(item) == 75)
//            return false;
//        if (item.getIdFromItem(item) == 76)
//            return false;
//        if (item.getIdFromItem(item) == 54)
//            return false;
//        if (item.getIdFromItem(item) == 130)
//            return false;
//        if (item.getIdFromItem(item) == 146)
//            return false;
//        if (item.getIdFromItem(item) == 342)
//            return false;
//        if (item.getIdFromItem(item) == 12)
//            return false;
//        if (item.getIdFromItem(item) == 77)
//            return false;
//        if (item.getIdFromItem(item) == 143)
//            return false;
//        if (item.getIdFromItem(item) == 46)
//            return false;
//        return true;
//    }
//
//    public void onDisable() {
//        Mist.instance.getEventManager().unregisterEventHandler(eventMotion);
//        Mist.instance.getEventManager().unregisterEventHandler(renderWorld);
//        Mist.instance.getEventManager().unregisterEventHandler(renderOverlay);
//    }
//
//
//    public void onEnable() {
//        Mist.instance.getEventManager().registerEventHandler(EventMotion.class, eventMotion);
//        Mist.instance.getEventManager().registerEventHandler(EventRenderWorld.class, renderWorld);
//        Mist.instance.getEventManager().registerEventHandler(EventRenderHUD.class, renderOverlay);
//
//        lastItem = mc.thePlayer.inventory.currentItem;
//    }
//
//
//
//}

package dev.sakey.mist.modules.impl.player;

import dev.sakey.mist.Mist;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.player.EventMotion;
import dev.sakey.mist.events.impl.render.EventRenderHUD;
import dev.sakey.mist.events.impl.render.EventRenderWorld;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.modules.annotations.SearchTags;
import dev.sakey.mist.modules.settings.impl.BoolSetting;
import dev.sakey.mist.modules.settings.impl.ModeSetting;
import dev.sakey.mist.modules.settings.impl.NumberSetting;
import dev.sakey.mist.ui.notifications.Notification;
import dev.sakey.mist.ui.notifications.NotificationType;
import dev.sakey.mist.utils.player.MovementUtils;
import dev.sakey.mist.utils.player.RotationUtils;
import dev.sakey.mist.utils.render.BoxUtils;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.*;

public class Scaffold extends Module {

    BoolSetting noSwing = new BoolSetting("No-swing", true);
    BoolSetting tower = new BoolSetting("Tower", true);
    BoolSetting jump = new BoolSetting("Jump", true);
    ModeSetting placeMode = new ModeSetting("Place Mode", "Post", "Pre", "Any");
    ModeSetting swingMode = new ModeSetting("Swing Mode", "Pre", "Post", "Any");
    NumberSetting towerTimer = new NumberSetting("Tower Timer", 1, 0.2, 5, 0.2);


    // TODO: add delay

    @ModuleInfo(name = "Scaffold", description = "Places blocks underneath you.", category = Category.MOVEMENT)
    @SearchTags(tags = { "BlockFly", "Tower", "Placer" })
    public Scaffold(){
        addSettings(
                noSwing, jump,
                placeMode, swingMode,
                tower, towerTimer
        );
        towerTimer.addParent(tower, BoolSetting::isEnabled);
    }

    BlockPos below;
    BlockPos target;
    EnumFacing face;

    int itemSlot = -1;
    int previousSlotPacket = -1;
    double previousY;

    float yaw, pitch;

    EventHandler<EventMotion> eventMotion = e -> {

        // SETUP AND CHECKS

        if(!selectSlot()) {
            disable();
            new Notification("Disabled " + getName(), "No blocks left", NotificationType.WARNING, 3000);
            return;
        }

        below = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);

        if(!mc.theWorld.isAirBlock(below)) {
            // TOWER
            if(mc.gameSettings.keyBindJump.isKeyDown()) {
                if(tower.isEnabled() && !MovementUtils.isMoving()) {
                    mc.timer.timerSpeed = (float) towerTimer.getValue();
                    mc.thePlayer.onGround = true;
                    mc.thePlayer.jump();
                    mc.thePlayer.onGround = false;
                }
            }
            else {
                mc.timer.timerSpeed = 1;
            }

            return;
        }
        else {
            mc.timer.timerSpeed = 1;
        }

        if(mc.thePlayer.onGround && jump.isEnabled() && !mc.gameSettings.keyBindJump.isKeyDown())
            mc.thePlayer.jump();

        if(mc.gameSettings.keyBindJump.isKeyDown())
            previousY = mc.thePlayer.posY;


        below = new BlockPos(mc.thePlayer.posX, previousY - 1, mc.thePlayer.posZ);

        if(!setPreviousAndFace()) {
            BlockPos currentPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
            // Save from falling
            for (int x = -4; x < 4; x++) {
                for (int z = -4; z < 4; z++) {
                    for (int y = -4; y < -1; y++) {
                        if(!mc.theWorld.isAirBlock(currentPos.add(x, y, z))) {

                            for (int cx = -1; x < 1; x++) {
                                for (int cz = -1; z < 1; z++) {
                                    for (int cy = -1; y < 1; y++) {
                                        if(mc.theWorld.isAirBlock(currentPos.add(x, y, z).add(cx, cy, cz))) {
                                            below = currentPos.add(x, y, z).add(cx, cy, cz);
                                            if(setPreviousAndFace()) break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return;
        } else {
            below = new BlockPos(mc.thePlayer.posX, previousY - 1, mc.thePlayer.posZ);
        }

        BoxUtils.drawBox(below);

        look(e);

        if (placeMode.is("Pre") && e.isPre())
            place(e);
        else if (placeMode.is("Post") && e.isPost())
            place(e);

    };

    private boolean selectSlot() {
        for(int i = 0; i < 8; i++) {
            ItemStack item = mc.thePlayer.inventory.mainInventory[i];
            if(item != null && item.isStackable() && item.getItem() instanceof ItemBlock) {
                itemSlot = i;
                return true;
            }
        }
        itemSlot = -1;
        return false;
    }

    private void swing() {
        if(!noSwing.isEnabled())
            mc.thePlayer.swingItem();
        else
            mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
    }

    private void place(EventMotion e) {

		swing();

        int preiousSlot = mc.thePlayer.inventory.currentItem;
        // todo: check for other packets
        if (previousSlotPacket != itemSlot) {
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(itemSlot));
            previousSlotPacket = itemSlot;
        }

        mc.playerController.onPlayerRightClick(
                mc.thePlayer, mc.theWorld,
                mc.thePlayer.inventory.getStackInSlot(itemSlot),
                target,
                face,
                getVecRotations()
        );

        if (previousSlotPacket != preiousSlot) {
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(preiousSlot));
            previousSlotPacket = preiousSlot;
        }
    }

    EventHandler<EventRenderWorld> eventRenderWorld = e -> {
        if(target == null) return;
        RenderGlobal.drawOutlinedBoundingBox(
                new AxisAlignedBB(
                        target.getX(),
                        target.getY(),
                        target.getZ(),
                        target.getX() + 1,
                        target.getY() + 1,
                        target.getZ() + 1
                ),
                1, 1, 1, 1
        );
    };

    EventHandler<EventRenderHUD> eventRenderHUD = e -> {
//        if(itemSlot == -1) return;
//
//        int offset = 10;
//        int width = 50;
//        int height = 20;
//
//        ScaledResolution sr = new ScaledResolution(mc);
//        RenderUtils.drawRect(
//                sr.getScaledWidth() / 2 - offset - width,
//                sr.getScaledHeight() / 2 - height,
//                sr.getScaledWidth() / 2 - offset,
//                sr.getScaledHeight() / 2,
//                Colours.grey());
//
//        mc.getRenderItem().renderItemOverlays(
//                mc.fontRendererObj, mc.thePlayer.inventory.getStackInSlot(itemSlot),
//                0, 0
//                //"" + mc.thePlayer.inventory.getStackInSlot(itemSlot).stackSize // TODO: add up all sizes of same block
//        );
    };

    private Vec3 getVecRotations() {
        BlockPos pos = target.offset(face);
        return new Vec3(pos.getX(), pos.getY(), pos.getZ());

        //float[] rots = getRotations();
        //return RotationUtils.getVecRotations(rots[0], rots[1]);
    }

    private void look(EventMotion e) {
        e.setYaw(yaw);
        e.setPitch(pitch);

        mc.thePlayer.rotationYawHead = e.getYaw();
        mc.thePlayer.rotationPitchHead = e.getPitch();
    }

    private void setRotations() {
        BlockPos offset = target;

        float[] rots = RotationUtils.getBlockRotations(
                offset.getX(),
                offset.getY(),
                offset.getZ(),
                face
        );

        yaw = rots[0];
        pitch = rots[1];
    }

    private boolean setPreviousAndFace() {
        // UP
        if(!mc.theWorld.isAirBlock(below.add(0, -1, 0))) {
            target = below.add(0, -1, 0);
            face = EnumFacing.UP;
        }

        // SIDE
        else if(!mc.theWorld.isAirBlock(below.add(1, 0, 0))) {
            target = below.add(1, 0, 0);
            face = EnumFacing.WEST;
        }
        else if(!mc.theWorld.isAirBlock(below.add(0, 0, 1))) {
            target = below.add(0, 0, 1);
            face = EnumFacing.NORTH;
        }
        else if(!mc.theWorld.isAirBlock(below.add(-1, 0, 0))) {
            target = below.add(-1, 0, 0);
            face = EnumFacing.EAST;
        }
        else if(!mc.theWorld.isAirBlock(below.add(0, 0, -1))) {
            target = below.add(0, 0, -1);
            face = EnumFacing.SOUTH;
        }

        // SIDE UP
        else if(!mc.theWorld.isAirBlock(below.add(1, -1, 0))) {
            target = below.add(1, -1, 0);
            face = EnumFacing.WEST;
        }
        else if(!mc.theWorld.isAirBlock(below.add(0, -1, 1))) {
            target = below.add(0, -1, 1);
            face = EnumFacing.NORTH;
        }
        else if(!mc.theWorld.isAirBlock(below.add(-1, -1, 0))) {
            target = below.add(-1, -1, 0);
            face = EnumFacing.EAST;
        }
        else if(!mc.theWorld.isAirBlock(below.add(0, -1, -1))) {
            target = below.add(0, -1, -1);
            face = EnumFacing.SOUTH;
        }

        // DIAGONAL
        else if(!mc.theWorld.isAirBlock(below.add(1, 0, -1))) {
            target = below.add(1, 0, -1);
            face = EnumFacing.WEST;
        }
        else if(!mc.theWorld.isAirBlock(below.add(1, 0, 1))) {
            target = below.add(1, 0, 1);
            face = EnumFacing.NORTH;
        }
        else if(!mc.theWorld.isAirBlock(below.add(-1, 0, 1))) {
            target = below.add(-1, 0, 1);
            face = EnumFacing.EAST;
        }
        else if(!mc.theWorld.isAirBlock(below.add(-1, 0, -1))) {
            target = below.add(-1, 0, -1);
            face = EnumFacing.SOUTH;
        }

        // DIAGONAL UP
        else if(!mc.theWorld.isAirBlock(below.add(1, -1, -1))) {
            target = below.add(1, -1, -1);
            face = EnumFacing.WEST;
        }
        else if(!mc.theWorld.isAirBlock(below.add(1, -1, 1))) {
            target = below.add(1, -1, 1);
            face = EnumFacing.NORTH;
        }
        else if(!mc.theWorld.isAirBlock(below.add(-1, -1, 1))) {
            target = below.add(-1, -1, 1);
            face = EnumFacing.EAST;
        }
        else if(!mc.theWorld.isAirBlock(below.add(-1, -1, -1))) {
            target = below.add(-1, -1, -1);
            face = EnumFacing.SOUTH;
        }

        else {

            // FAIL
            target = null;
            face = null;
            return false;
        }

        setRotations();

        return true;
    }

    protected void onEnable() {
        registerEvent(EventMotion.class, eventMotion);
        registerEvent(EventRenderWorld.class, eventRenderWorld);
        registerEvent(EventRenderHUD.class, eventRenderHUD);
        previousY = mc.thePlayer.posY;
    }

    protected void onDisable() {
        unregisterEvent(eventMotion);
        unregisterEvent(eventRenderWorld);
        unregisterEvent(eventRenderHUD);
        mc.timer.timerSpeed = 1;
    }

}
