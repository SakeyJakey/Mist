package dev.sakey.mist.modules.settings.impl;

import dev.sakey.mist.modules.settings.Setting;

public class BoolSetting extends Setting {

	private boolean enabled;
	
	public BoolSetting(String name, boolean enabled) {
		this.name = name;
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public void toggle() {
		enabled = !enabled;
	}
	
}
