package dev.sakey.mist.modules.impl.player;


import dev.sakey.mist.Mist;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.player.EventMotion;
import dev.sakey.mist.events.impl.render.EventRenderHUD;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.modules.settings.impl.BoolSetting;
import dev.sakey.mist.modules.settings.impl.NumberSetting;
import dev.sakey.mist.ui.draggables.Draggable;
import dev.sakey.mist.utils.client.TimerUtil;
import dev.sakey.mist.utils.render.RenderUtils;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import org.lwjgl.input.Keyboard;

public class ChestStealer extends Module {

	private NumberSetting delay = new NumberSetting("Delay", 100, 0, 300, 1);
	public BoolSetting stealingGUI = new BoolSetting("Stealing GUI", true);

	private final StealingDraggable stealingDraggable = new StealingDraggable();

    @ModuleInfo(name = "ChestStealer", description = "Steals everything from chests.", category = Category.PLAYER)
    public ChestStealer(){
		addSettings(delay, stealingGUI);
		Mist.instance.getDraggableManager().add(stealingDraggable);
    }

    protected void onEnable() {
		timer.reset();
		slot = 0;
		stage = 0;
        registerEvent(EventMotion.class, eventMotion);
		registerEvent(EventRenderHUD.class, eventRenderHUD);
    }

    protected void onDisable() {
        unregisterEvent(eventMotion);
		unregisterEvent(eventRenderHUD);
    }

	TimerUtil timer = new TimerUtil();
	TimerUtil total = new TimerUtil();

	int[] slots = {};
	int slot = 0;

	public int stage = 0;

	public boolean hideChestGUI = true;


    EventHandler<EventMotion> eventMotion = e -> {

		if(stage != -1)
			hideChestGUI = true;

        if(!(mc.thePlayer.openContainer instanceof ContainerChest)) {
			slot = 0;
			stage = 0;
			timer.reset();
			total.reset();
			hideChestGUI = true;
			return;
		}

		IInventory chest = ((ContainerChest) mc.thePlayer.openContainer).getLowerChestInventory();

		if( // Blacklist menus
				chest.getDisplayName().getUnformattedText().toLowerCase().contains("shop") ||
				chest.getDisplayName().getUnformattedText().toLowerCase().contains("kit") ||
				chest.getDisplayName().getUnformattedText().toLowerCase().contains("item") ||
				chest.getDisplayName().getUnformattedText().toLowerCase().contains("upgrade") ||
				chest.getDisplayName().getUnformattedText().toLowerCase().contains("game") ||
				chest.getDisplayName().getUnformattedText().toLowerCase().contains("play") ||
				chest.getDisplayName().getUnformattedText().toLowerCase().contains("again") ||
				chest.getDisplayName().getUnformattedText().toLowerCase().contains("select") ||
				chest.getDisplayName().getUnformattedText().toLowerCase().contains("click") ||
				chest.getDisplayName().getUnformattedText().toLowerCase().contains("spectate") ||
				chest.getDisplayName().getUnformattedText().toLowerCase().contains("vote") ||
				chest.getDisplayName().getUnformattedText().toLowerCase().contains("solo") ||
				chest.getDisplayName().getUnformattedText().toLowerCase().contains("doubles") ||
				chest.getDisplayName().getUnformattedText().toLowerCase().contains("wars") ||
				chest.getDisplayName().getUnformattedText().toLowerCase().contains("server") ||
				chest.getDisplayName().getUnformattedText().toLowerCase().contains("box") ||
				chest.getDisplayName().getUnformattedText().toLowerCase().contains("?")
		) {
			slot = 0;
			stage = -1;
			timer.reset();
			total.reset();
			hideChestGUI = false;
			return;
		}


		if(stage == 0) {
			search(chest);
			stage++;
			if(slots.length == 0) {
				stage = -1; // do nothing if empty
				hideChestGUI = false;
			} else {
				stealingDraggable.show();
				hideChestGUI = true;
			}
		}

		if(stage == 1 && timer.hasTimeElapsed((long) delay.getValue(), true)) {
			mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, slots[slot], 0, 1, mc.thePlayer);
			slot++;
			if(slot == slots.length)
				stage++;
		}

		if(stage == 2) {
			hideChestGUI = true;
			stealingDraggable.hide();
			mc.thePlayer.closeScreen();
		}
    };

	EventHandler<EventRenderHUD> eventRenderHUD = e -> {
	};

	private class StealingDraggable extends Draggable {

		public StealingDraggable() {
			super(100, 75, 100, 10);
		}

		protected void draw() {
			if(!stealingGUI.isEnabled()) return;
			if(slots.length == 0) return;
			if(!(mc.thePlayer.openContainer instanceof ContainerChest)) return;

			RenderUtils.drawRect(xPos, yPos, xPos + width * total.getTime() / (delay.getValue() * (slots.length + 2)), yPos + 10, -1);
		}
	}

	private void search(IInventory chest) {
		int availableSlots = 0;

		for (int i = 0; i < chest.getSizeInventory(); i++) {
			if(chest.getStackInSlot(i) != null && chest.getStackInSlot(i).getItem() != null)
				availableSlots++;
		}

		slots = new int[availableSlots];

		int item = 0;
		for (int i = 0; i < chest.getSizeInventory(); i++) {
			if(chest.getStackInSlot(i) != null && chest.getStackInSlot(i).getItem() != null) {
				slots[item] = i;
				item++;
			}
		}
	}
}
