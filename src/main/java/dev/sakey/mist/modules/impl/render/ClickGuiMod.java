package dev.sakey.mist.modules.impl.render;

import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.modules.settings.impl.BoolSetting;
import dev.sakey.mist.ui.clickguis.main.MainClickGui;
import dev.sakey.mist.ui.clickguis.window.WindowClickGui;
import org.lwjgl.input.Keyboard;

public class ClickGuiMod extends Module {

    public MainClickGui mcgui;
    public BoolSetting hideNonSearch = new BoolSetting("Hide Non Searched", true);

    @ModuleInfo(name = "ClickGUI", description = "Toggles da mods.", key = Keyboard.KEY_RSHIFT, category = Category.MISC, hiddenInArrayList = true)
    public ClickGuiMod() { addSettings(hideNonSearch); }

    protected void onEnable() {
        if(mcgui == null)
            mcgui = new MainClickGui();
        quietToggle();
        mc.displayGuiScreen(mcgui);
    }
}
