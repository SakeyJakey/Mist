package dev.sakey.mist.modules;

import dev.sakey.mist.Mist;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.impl.flag.EventLagback;
import dev.sakey.mist.events.impl.flag.EventWorldChange;
import dev.sakey.mist.modules.annotations.DisableOnLagback;
import dev.sakey.mist.modules.annotations.DisableOnWorldChange;
import dev.sakey.mist.modules.annotations.ModuleInfo;
import dev.sakey.mist.modules.annotations.SearchTags;
import dev.sakey.mist.modules.settings.Setting;
import dev.sakey.mist.modules.settings.impl.KeySetting;
import dev.sakey.mist.ui.notifications.Notification;
import dev.sakey.mist.ui.notifications.NotificationType;
import dev.sakey.mist.utils.client.animation.Animation;
import net.minecraft.client.Minecraft;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Module {

	protected static Minecraft mc = Minecraft.getMinecraft();
	private final List<Setting> settings = new ArrayList<Setting>();
	public Animation arrayAnimationX;
	public Animation arrayAnimationY;
	protected String name = "Unnamed Module";
	protected Category category = Category.MISC;
	protected String description = "";
	private boolean enabled = false;
	EventHandler<EventLagback> eventLagback = e -> {
		if (!enabled) return;
		quietToggle();
		new Notification(name, "Disabled " + name + " due to lagback.", NotificationType.INFO, 3000);
	};
	EventHandler<EventWorldChange> eventWorldChange = e -> {
		if (!enabled) return;
		quietToggle();
		new Notification(name, "Disabled " + name + " due to world change.", NotificationType.INFO, 3000);
	};
	private final KeySetting keyCode = new KeySetting(0);
	private String[] searchTags = {};
	private boolean hiddenInArray = false, settingsOpen = false;
	private boolean disableOnLagback = false,
			disableOnWorldChange = false;

	public Module() {
		try {

			if (getClass().getConstructor().getDeclaredAnnotationsByType(ModuleInfo.class).length > 0) {
				ModuleInfo mi = getClass().getConstructor().getAnnotation(ModuleInfo.class);

				name = mi.name();

				if (mi.enabledByDefault()) //todo make own annotation
					enable();

				keyCode.setCode(mi.key());
				category = mi.category();
				description = mi.description();
				hiddenInArray = mi.hiddenInArrayList();

				if (category.equals(Category.HUD)) // hide hud mods
					hiddenInArray = true;
			}

			if (getClass().getConstructor().getDeclaredAnnotationsByType(SearchTags.class).length > 0) {
				SearchTags st = getClass().getConstructor().getAnnotation(SearchTags.class);
				searchTags = st.tags();
			}

			if (getClass().getConstructor().getDeclaredAnnotationsByType(DisableOnLagback.class).length > 0)
				disableOnLagback = true;

			if (getClass().getConstructor().getDeclaredAnnotationsByType(DisableOnWorldChange.class).length > 0)
				disableOnWorldChange = true;

		} catch (Exception e) {
			Mist.instance.getLogger().error("Failed to load modules! Please report this to the staff: " + e);
		}

		if (disableOnLagback)
			registerEvent(EventLagback.class, eventLagback);

		if (disableOnWorldChange)
			registerEvent(EventWorldChange.class, eventWorldChange);

		addSettings(keyCode);
	}

	public List<Setting> getSettings() {
		return settings;
	}

	public String getSuffix() {
		return "";
	} // Default

	public boolean isSearched(String term) {
		term = term.toLowerCase().replace(" ", "");
		for (String tag :
				searchTags) {
			tag = tag.toLowerCase();

			if (tag.contains(term) || term.contains(tag))
				return true;
		}
		return getName().toLowerCase().contains(term) || term.contains(getName().toLowerCase()) || term.isEmpty();
	}

	protected void addSettings(Setting... settings) {
		this.settings.addAll(Arrays.asList(settings));
		this.settings.sort(Comparator.comparingInt(s -> s == keyCode ? 1 : 0));
	}

	public void quietToggle() {
		enabled = !enabled;
		if (enabled) onEnable();
		if (!enabled) onDisable();
	}

	public void toggle() {
		if (enabled)
			new Notification(name, "Disabled " + name, NotificationType.ERROR, 3000);
		else
			new Notification(name, "Enabled " + name, NotificationType.SUCCESS, 3000);
		quietToggle();
	}

	protected void registerEvent(Type type, EventHandler eventHandler) {
		Mist.instance.getEventManager().registerEventHandler(type, eventHandler);
	}

	protected void unregisterEvent(EventHandler eventHandler) {
		Mist.instance.getEventManager().unregisterEventHandler(eventHandler);
	}

	public void disable() {
		if (enabled)
			quietToggle();
	}

	public void enable() {
		if (!enabled)
			quietToggle();
	}

	protected void onEnable() {
	}

	protected void onDisable() {
	}

	public String getName() {
		return name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isHiddenInArray() {
		return hiddenInArray;
	}

	public int getKeyCode() {
		return keyCode.getCode();
	}

	public Category getCategory() {
		return category;
	}

	public String getDescription() {
		return description;
	}

	public int getTextLength() {
		if (getSuffix() == "") return Mist.instance.getFontRenderer().getStringWidth(name);
		else return Mist.instance.getFontRenderer().getStringWidth(name + " " + getSuffix());
	}

	public void toggleSettingsOpen() {
		settingsOpen = !settingsOpen;
	}

	public boolean areSettingsOpen() {
		return settingsOpen;
	}
}
