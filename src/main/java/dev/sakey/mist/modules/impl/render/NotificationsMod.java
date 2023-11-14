package dev.sakey.mist.modules.impl.render;

import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.render.EventRenderHUD;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.modules.settings.impl.ModeSetting;
import dev.sakey.mist.modules.settings.impl.NumberSetting;
import dev.sakey.mist.ui.clickguis.main.MainClickGui;
import dev.sakey.mist.ui.notifications.NotificationManager;
import dev.sakey.mist.ui.notifications.styles.NotificationStyleBasic;
import dev.sakey.mist.ui.notifications.styles.NotificationStyleClean;
import dev.sakey.mist.ui.notifications.styles.NotificationStyleModern;
import dev.sakey.mist.ui.notifications.styles.NotificationStyleTenacity;
import org.lwjgl.input.Keyboard;

public class NotificationsMod extends Module {

    ModeSetting style = new ModeSetting("Style", "Mist", "Old", "Tenacity", "Clean");
    public NumberSetting smoothing = new NumberSetting("Smoothing Speed", 1.5, 0.1, 5, 0.1);

    @ModuleInfo(name = "Notifications", description = "notifyes u", category = Category.MISC, hiddenInArrayList = true, enabledByDefault = true)
    public NotificationsMod() {
        registerEvent(EventRenderHUD.class, eventRenderHUD);
        addSettings(style);
    }

    EventHandler<EventRenderHUD> eventRenderHUD = e -> {
        switch (style.getMode()) {
            case "Mist":
                if(!(NotificationManager.renderer instanceof NotificationStyleModern))
                    NotificationManager.renderer = new NotificationStyleModern();
                break;
            case "Old":
                if(!(NotificationManager.renderer instanceof NotificationStyleBasic))
                    NotificationManager.renderer = new NotificationStyleBasic();
                break;
            case "Tenacity":
                if(!(NotificationManager.renderer instanceof NotificationStyleTenacity))
                    NotificationManager.renderer = new NotificationStyleTenacity();
                break;
            case "Clean":
                if(!(NotificationManager.renderer instanceof NotificationStyleClean))
                    NotificationManager.renderer = new NotificationStyleClean();
                break;
        }
    };

}
