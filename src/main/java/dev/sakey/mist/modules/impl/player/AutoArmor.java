package dev.sakey.mist.modules.impl.player;

import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.player.EventMotion;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.modules.settings.impl.BoolSetting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.Arrays;

public class AutoArmor extends Module {

	BoolSetting onInv = new BoolSetting("When inv open", true);
	private int[] bestArmorSlots;
	EventHandler<EventMotion> eventMotion = e -> {
		if (onInv.isEnabled() && !(mc.currentScreen instanceof GuiInventory)) return;
		search();

		for (int i = 0; i < 4; i++) {
			if (bestArmorSlots[i] != -1) {
				int bestSlot = bestArmorSlots[i];

				ItemStack oldArmor = mc.thePlayer.inventory.armorItemInSlot(i);

				if (oldArmor != null && oldArmor.getItem() != null) {
					mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 8 - i, 0, 1, mc.thePlayer);
				}
				mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestSlot < 9 ? bestSlot + 36 : bestSlot, 0, 1, mc.thePlayer);
			}
/*
		if(bestSwordSlot != -1 && bestSwordDamage != -1) {
			mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestSwordSlot < 9 ? bestSwordSlot + 36 : bestSwordSlot, 0, 2, mc.thePlayer);
		}*/
		}
	};

	@ModuleInfo(name = "AutoArmor", description = "Automatically equips armor", category = Category.PLAYER)
	public AutoArmor() {
		addSettings(onInv);
	}

	protected void onEnable() {
		registerEvent(EventMotion.class, eventMotion);
	}

	protected void onDisable() {
		unregisterEvent(eventMotion);
	}

	private void search() {
		int[] bestArmorDamage = new int[4];
		bestArmorSlots = new int[4];

		Arrays.fill(bestArmorDamage, -1);
		Arrays.fill(bestArmorSlots, -1);

		for (int i = 0; i < bestArmorSlots.length; i++) {
			ItemStack itemStack = mc.thePlayer.getCurrentArmor(i);

			if (itemStack != null && itemStack.getItem() != null) {
				if (itemStack.getItem() instanceof ItemArmor armor) {
					bestArmorDamage[i] = armor.damageReduceAmount;
				}
			}
		}

		for (int i = 0; i < 9 * 4; i++) {
			ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);

			if (itemStack == null || itemStack.getItem() == null) continue;

			if (itemStack.getItem() instanceof ItemArmor armor) {

				int armorType = 3 - armor.armorType;

				if (bestArmorDamage[armorType] < armor.damageReduceAmount) {
					bestArmorDamage[armorType] = armor.damageReduceAmount;
					bestArmorSlots[armorType] = i;
				}
			}
		}

		for (int i = 0; i < 9 * 4; i++) {
			ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);

			if (itemStack == null || itemStack.getItem() == null) continue;

			if (itemStack.getItem() instanceof ItemArmor armor) {

				int armorType = 3 - armor.armorType;

				if (bestArmorDamage[armorType] < armor.damageReduceAmount) {
					bestArmorDamage[armorType] = armor.damageReduceAmount;
					bestArmorSlots[armorType] = i;
				}
			}
		}
	}
}
