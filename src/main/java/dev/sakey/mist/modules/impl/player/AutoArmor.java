package dev.sakey.mist.modules.impl.player;


import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.player.EventMotion;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.ModuleInfo;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

public class AutoArmor extends Module {

    @ModuleInfo(name = "AutoArmor", description = "Automatically equips armor", key = Keyboard.KEY_O, category = Category.PLAYER)
    public AutoArmor(){

    }

    protected void onEnable() {
        registerEvent(EventMotion.class, eventMotion);
    }

    protected void onDisable() {
        unregisterEvent(eventMotion);
    }

    EventHandler<EventMotion> eventMotion = e -> {
        for (ItemStack i :
                mc.thePlayer.getInventory()) {
            if(i == null || i.isStackable()) return;

            if(
                i.getItem() == Items.diamond_helmet.getContainerItem() ||
                i.getItem() == Items.iron_helmet.getContainerItem() ||
                i.getItem() == Items.golden_helmet.getContainerItem() ||
                i.getItem() == Items.chainmail_helmet.getContainerItem() ||
                i.getItem() == Items.leather_helmet.getContainerItem()
            ){
                mc.thePlayer.inventory.setInventorySlotContents(103, i);
            }

            else if(
                    i.getItem() == Items.diamond_chestplate.getContainerItem() ||
                    i.getItem() == Items.iron_chestplate.getContainerItem() ||
                    i.getItem() == Items.golden_chestplate.getContainerItem() ||
                    i.getItem() == Items.chainmail_chestplate.getContainerItem() ||
                    i.getItem() == Items.leather_chestplate.getContainerItem()
            ){
                mc.thePlayer.inventory.setInventorySlotContents(102, i);
            }

            else if(
                i.getItem() == Items.diamond_leggings.getContainerItem() ||
                i.getItem() == Items.iron_leggings.getContainerItem() ||
                i.getItem() == Items.golden_leggings.getContainerItem() ||
                i.getItem() == Items.chainmail_leggings.getContainerItem() ||
                i.getItem() == Items.leather_leggings.getContainerItem()
            ){
                mc.thePlayer.inventory.setInventorySlotContents(101, i);
            }
            else if(
                i.getItem() == Items.diamond_boots.getContainerItem() ||
                i.getItem() == Items.iron_boots.getContainerItem() ||
                i.getItem() == Items.golden_boots.getContainerItem() ||
                i.getItem() == Items.chainmail_boots.getContainerItem()  ||
                i.getItem() == Items.leather_boots.getContainerItem()
            ){
                mc.thePlayer.inventory.setInventorySlotContents(100, i);
            }
        }
    };
}
