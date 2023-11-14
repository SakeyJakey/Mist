package dev.sakey.mist.scripts.bindings.mist.impl;

import dev.sakey.mist.Mist;
import dev.sakey.mist.events.Event;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.client.EventKeyPress;
import dev.sakey.mist.events.impl.client.EventPacket;
import dev.sakey.mist.events.impl.flag.EventLagback;
import dev.sakey.mist.events.impl.flag.EventWorldChange;
import dev.sakey.mist.events.impl.player.EventMotion;
import dev.sakey.mist.events.impl.render.EventRenderEntity;
import dev.sakey.mist.events.impl.render.EventRenderHUD;
import dev.sakey.mist.events.impl.render.EventRenderWorld;
import dev.sakey.mist.modules.Category;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.settings.Setting;
import dev.sakey.mist.modules.settings.impl.BoolSetting;
import dev.sakey.mist.modules.settings.impl.ModeSetting;
import dev.sakey.mist.modules.settings.impl.NumberSetting;
import dev.sakey.mist.ui.notifications.Notification;
import dev.sakey.mist.ui.notifications.NotificationType;

import java.util.function.Consumer;

public class ModuleBinding {
	ScriptModule module;

	Runnable onEnable = () -> { };
	Runnable onDisable = () -> { };


	public ModuleBinding(String name, String category) {
		module = new ScriptModule();
		module.setName(name);
		module.setCategory(Category.getCategoryByName(category));
		Mist.instance.getModuleManager().addModule(module);
	}

	public void onEvent(String eventName, Consumer<Event> cons) {

		Class e;

		switch (eventName) {
			case "keyPress":
				e = EventKeyPress.class;
				break;
			case "packet":
				e = EventPacket.class;
				break;
			case "lagback":
				e = EventLagback.class;
				break;
			case "worldChange":
				e = EventWorldChange.class;
				break;
			case "motion":
				e = EventMotion.class;
				break;
			case "renderEntity":
				e = EventRenderEntity.class;
				break;
			case "renderHUD":
				e = EventRenderHUD.class;
				break;
			case "renderWorld":
				e = EventRenderWorld.class;
				break;

			default:
				new Notification("Failed to parse script", "Event '" + eventName + "' does not exist", NotificationType.WARNING, 5000);
				return;
		}

		EventHandler eventHandler = event -> {
			if(module.isEnabled())
				cons.accept(event);
		};

		Mist.instance.getEventManager().registerEventHandler(e, eventHandler);
	}

	public void onEnable(Runnable runnable) {
		onEnable = runnable;
	}

	public void onDisable(Runnable runnable) {
		onDisable = runnable;
	}

	public String addNumberSetting(String name, double value, double min, double max, double increment) {
		module.addSetting(new NumberSetting(name, value, min, max, increment));
		return name;
	}

	public String addBoolSetting(String name, boolean enabled) {
		module.addSetting(new BoolSetting(name, enabled));
		return name;
	}

	public String addModeSetting(String name, String defaultMode, String... modes) {
		module.addSetting(new ModeSetting(name, defaultMode, modes));
		return name;
	}
	
	public boolean getBoolSetting(String name) {
		for (Setting s :
				module.getSettings()) {
			if (s.getName().equalsIgnoreCase(name))
				return ((BoolSetting)s).isEnabled();
		}
		new Notification("Failed to parse script", "Setting '" + name + "' does not exist", NotificationType.WARNING, 5000);
		return false;
	}

	public double getNumberSetting(String name) {
		for (Setting s :
				module.getSettings()) {
			if (s.getName().equalsIgnoreCase(name))
				return ((NumberSetting)s).getValue();
		}
		new Notification("Failed to parse script", "Setting '" + name + "' does not exist", NotificationType.WARNING, 5000);
		return 0;
	}

	public String getModeSetting(String name) {
		for (Setting s :
				module.getSettings()) {
			if (s.getName().equalsIgnoreCase(name))
				return ((ModeSetting)s).getMode();
		}
		new Notification("Failed to parse script", "Setting '" + name + "' does not exist", NotificationType.WARNING, 5000);
		return "";
	}

	private class ScriptModule extends Module {
		@Override
		protected void onEnable() {
			onEnable.run();
		}

		@Override
		protected void onDisable() {
			onDisable.run();
		}

		public void setCategory(Category category) {
			this.category = category;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void addSetting(Setting setting) {
			this.addSettings(setting);
		}
	};

}