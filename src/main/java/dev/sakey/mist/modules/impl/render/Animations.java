package dev.sakey.mist.modules.impl.render;

import dev.sakey.mist.Mist;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.render.EventRenderEntity;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.modules.settings.impl.BoolSetting;
import dev.sakey.mist.modules.settings.impl.ModeSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class Animations extends Module {

    public ModeSetting mode = new ModeSetting("Mode", "Mist", "Night", "Totem");
	public BoolSetting fullSwing = new BoolSetting("Full Swing", true);

    @ModuleInfo(name = "Animations", description = "Item animations", category = Category.RENDER)
    public Animations() { addSettings(mode, fullSwing); }

    // ItemRenderer:transformFirstPersonItem
}
