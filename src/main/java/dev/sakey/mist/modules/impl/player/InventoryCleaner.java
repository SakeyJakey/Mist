package dev.sakey.mist.modules.impl.player;


import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.player.EventMotion;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.modules.settings.impl.BoolSetting;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.*;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;

public class InventoryCleaner extends Module {

	BoolSetting onInv = new BoolSetting("When inv open", true);

	@ModuleInfo(name = "InventoryCleaner", description = "Cleans your inventory.", category = Category.PLAYER)
    public InventoryCleaner(){
		addSettings();
    }

    protected void onEnable() {
        registerEvent(EventMotion.class, eventMotion);
    }

    protected void onDisable() {
        unregisterEvent(eventMotion);
    }

	private int worstSwordSlot = -1;
	private int worstPickaxeSlot = -1;
	private int worstAxeSlot = -1;

    EventHandler<EventMotion> eventMotion = e -> {

		if(onInv.isEnabled() && !(mc.currentScreen instanceof GuiInventory)) return;

        search();
		if(worstSwordSlot != -1) {
			mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, worstSwordSlot, 0, 4, mc.thePlayer);
			mc.thePlayer.inventory.removeStackFromSlot(worstSwordSlot);
		}

		if(worstPickaxeSlot != -1) {
			mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, worstPickaxeSlot, 0, 4, mc.thePlayer);
			mc.thePlayer.inventory.removeStackFromSlot(worstPickaxeSlot);
		}

		if(worstAxeSlot != -1) {
			mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, worstPickaxeSlot, 0, 4, mc.thePlayer);
			mc.thePlayer.inventory.removeStackFromSlot(worstAxeSlot);
		}
    };

	private void search() {
		searchSwords();
		searchPickaxes();
		searchAxes();
	}

	private void searchSwords() {
		worstSwordSlot = -1;

		float worstSwordDamage = -1;

		int iterationsCompleted = 0;

		for (int i = 0; i < 9 * 4; i++) {
			ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);

			if(itemStack == null || itemStack.getItem() == null) continue;

			if(itemStack.getItem() instanceof ItemSword) {
				ItemSword sword = (ItemSword) itemStack.getItem();
				if(worstSwordDamage > sword.getDamageVsEntity() || worstSwordDamage == -1) {
					worstSwordDamage = sword.getDamageVsEntity();
					worstSwordSlot = i;
				}
			}
			else continue;

/*			if(itemStack.getItem() instanceof ItemTool) {
				ItemTool tool = (ItemTool) itemStack.getItem();

				float damage = tool.getToolMaterial().getDamageVsEntity();

				if(worstSwordDamage > damage || worstSwordDamage == -1) {
					worstSwordDamage = damage;
					worstSwordSlot = i;
				}
			}*/
			iterationsCompleted++;
		}

		if(iterationsCompleted <= 1) {
			worstSwordSlot = -1; // don't throw the last item
		}
	}


	private void searchAxes() {
		worstAxeSlot = -1;

		float worstAxeDamage = -1;

		int iterationsCompleted = 0;

		for (int i = 0; i < 9 * 4; i++) {
			ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);

			if(itemStack == null || itemStack.getItem() == null) continue;

			if(itemStack.getItem() instanceof ItemAxe) {
				ItemAxe axe = (ItemAxe) itemStack.getItem();
				if(worstAxeDamage > axe.getToolMaterial().getEfficiencyOnProperMaterial() || worstAxeDamage == -1) {
					worstAxeDamage = axe.getToolMaterial().getEfficiencyOnProperMaterial();
					worstAxeSlot = i;
				}
			}
			else continue;

			iterationsCompleted++;
		}

		if(iterationsCompleted <= 1) {
			worstAxeSlot = -1; // don't throw the last item
		}
	}
	
	private void searchPickaxes() {
		worstPickaxeSlot = -1;
		
		float worstPickaxeDamage = -1;

		int iterationsCompleted = 0;

		for (int i = 0; i < 9 * 4; i++) {
			ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);

			if(itemStack == null || itemStack.getItem() == null) continue;

			if(itemStack.getItem() instanceof ItemPickaxe) {
				ItemPickaxe pickaxe = (ItemPickaxe) itemStack.getItem();
				if(worstPickaxeDamage > pickaxe.getToolMaterial().getEfficiencyOnProperMaterial() || worstPickaxeDamage == -1) {
					worstPickaxeDamage = pickaxe.getToolMaterial().getEfficiencyOnProperMaterial();
					worstPickaxeSlot = i;
				}
			}
			else continue;

			iterationsCompleted++;
		}

		if(iterationsCompleted <= 1) {
			worstPickaxeSlot = -1; // don't throw the last item
		}
	}
}
