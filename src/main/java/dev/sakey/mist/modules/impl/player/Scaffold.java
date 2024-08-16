package dev.sakey.mist.modules.impl.player;

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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class Scaffold extends Module {

	BoolSetting noSwing = new BoolSetting("No-swing", true);
	BoolSetting tower = new BoolSetting("Tower", true);
	BoolSetting jump = new BoolSetting("Jump", true);
	ModeSetting placeMode = new ModeSetting("Place Mode", "Post", "Pre", "Any");
	ModeSetting swingMode = new ModeSetting("Swing Mode", "Pre", "Post", "Any");
	NumberSetting towerTimer = new NumberSetting("Tower Timer", 1, 0.2, 5, 0.2);


	// TODO: add delay
	BlockPos below;
	BlockPos target;
	EnumFacing face;
	int itemSlot = -1;
	int previousSlotPacket = -1;
	double previousY;
	float yaw, pitch;
	EventHandler<EventMotion> eventMotion = e -> {

		// SETUP AND CHECKS

		if (!selectSlot()) {
			disable();
			new Notification("Disabled " + getName(), "No blocks left", NotificationType.WARNING, 3000);
			return;
		}

		look(e);

		below = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);

		if (!mc.theWorld.isAirBlock(below)) {
			// TOWER
			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				if (tower.isEnabled() && !MovementUtils.isMoving()) {
					mc.timer.timerSpeed = (float) towerTimer.getValue();
					mc.thePlayer.onGround = true;
					mc.thePlayer.jump();
					mc.thePlayer.onGround = false;
				}
			} else {
				mc.timer.timerSpeed = 1;
			}

			return;
		} else {
			mc.timer.timerSpeed = 1;
		}

		if (mc.thePlayer.onGround && jump.isEnabled() && !mc.gameSettings.keyBindJump.isKeyDown())
			mc.thePlayer.jump();

		if (mc.gameSettings.keyBindJump.isKeyDown())
			previousY = mc.thePlayer.posY;


		below = new BlockPos(mc.thePlayer.posX, previousY - 1, mc.thePlayer.posZ);

		if (!setPreviousAndFace()) {
			BlockPos currentPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
			// Save from falling
			for (int x = -4; x < 4; x++) {
				for (int z = -4; z < 4; z++) {
					for (int y = -4; y < -1; y++) {
						if (!mc.theWorld.isAirBlock(currentPos.add(x, y, z))) {

							for (int cx = -1; x < 1; x++) {
								for (int cz = -1; z < 1; z++) {
									for (int cy = -1; y < 1; y++) {
										if (mc.theWorld.isAirBlock(currentPos.add(x, y, z).add(cx, cy, cz))) {
											below = currentPos.add(x, y, z).add(cx, cy, cz);
											if (setPreviousAndFace()) break;
										}
									}
								}
							}
						}
					}
				}
			}

			look(e);

			return;

		} else {
			look(e);
			below = new BlockPos(mc.thePlayer.posX, previousY - 1, mc.thePlayer.posZ);
		}

		BoxUtils.drawBox(below);

		if (placeMode.is("Pre") && e.isPre())
			place(e);
		else if (placeMode.is("Post") && e.isPost())
			place(e);

	};
	EventHandler<EventRenderWorld> eventRenderWorld = e -> {
		if (target == null) return;
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

	@ModuleInfo(name = "Scaffold", description = "Places blocks underneath you.", category = Category.MOVEMENT)
	@SearchTags(tags = {"BlockFly", "Tower", "Placer"})
	public Scaffold() {
		addSettings(
				noSwing, jump,
				placeMode, swingMode,
				tower, towerTimer
		);
		towerTimer.addParent(tower, BoolSetting::isEnabled);
	}

	private boolean selectSlot() {
		for (int i = 0; i < 8; i++) {
			ItemStack item = mc.thePlayer.inventory.mainInventory[i];
			if (item != null && item.isStackable() && item.getItem() instanceof ItemBlock) {
				itemSlot = i;
				return true;
			}
		}
		itemSlot = -1;
		return false;
	}

	private void swing() {
		if (!noSwing.isEnabled())
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
		if (!mc.theWorld.isAirBlock(below.add(0, -1, 0))) {
			target = below.add(0, -1, 0);
			face = EnumFacing.UP;
		}

		// SIDE
		else if (!mc.theWorld.isAirBlock(below.add(1, 0, 0))) {
			target = below.add(1, 0, 0);
			face = EnumFacing.WEST;
		} else if (!mc.theWorld.isAirBlock(below.add(0, 0, 1))) {
			target = below.add(0, 0, 1);
			face = EnumFacing.NORTH;
		} else if (!mc.theWorld.isAirBlock(below.add(-1, 0, 0))) {
			target = below.add(-1, 0, 0);
			face = EnumFacing.EAST;
		} else if (!mc.theWorld.isAirBlock(below.add(0, 0, -1))) {
			target = below.add(0, 0, -1);
			face = EnumFacing.SOUTH;
		}

		// SIDE UP
		else if (!mc.theWorld.isAirBlock(below.add(1, -1, 0))) {
			target = below.add(1, -1, 0);
			face = EnumFacing.WEST;
		} else if (!mc.theWorld.isAirBlock(below.add(0, -1, 1))) {
			target = below.add(0, -1, 1);
			face = EnumFacing.NORTH;
		} else if (!mc.theWorld.isAirBlock(below.add(-1, -1, 0))) {
			target = below.add(-1, -1, 0);
			face = EnumFacing.EAST;
		} else if (!mc.theWorld.isAirBlock(below.add(0, -1, -1))) {
			target = below.add(0, -1, -1);
			face = EnumFacing.SOUTH;
		}

		// DIAGONAL
		else if (!mc.theWorld.isAirBlock(below.add(1, 0, -1))) {
			target = below.add(1, 0, -1);
			face = EnumFacing.WEST;
		} else if (!mc.theWorld.isAirBlock(below.add(1, 0, 1))) {
			target = below.add(1, 0, 1);
			face = EnumFacing.NORTH;
		} else if (!mc.theWorld.isAirBlock(below.add(-1, 0, 1))) {
			target = below.add(-1, 0, 1);
			face = EnumFacing.EAST;
		} else if (!mc.theWorld.isAirBlock(below.add(-1, 0, -1))) {
			target = below.add(-1, 0, -1);
			face = EnumFacing.SOUTH;
		}

		// DIAGONAL UP
		else if (!mc.theWorld.isAirBlock(below.add(1, -1, -1))) {
			target = below.add(1, -1, -1);
			face = EnumFacing.WEST;
		} else if (!mc.theWorld.isAirBlock(below.add(1, -1, 1))) {
			target = below.add(1, -1, 1);
			face = EnumFacing.NORTH;
		} else if (!mc.theWorld.isAirBlock(below.add(-1, -1, 1))) {
			target = below.add(-1, -1, 1);
			face = EnumFacing.EAST;
		} else if (!mc.theWorld.isAirBlock(below.add(-1, -1, -1))) {
			target = below.add(-1, -1, -1);
			face = EnumFacing.SOUTH;
		} else {

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
