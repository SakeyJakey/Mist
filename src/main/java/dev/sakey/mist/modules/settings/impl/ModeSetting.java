package dev.sakey.mist.modules.settings.impl;

import dev.sakey.mist.modules.settings.Setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting {

	private int index;
	private List<String> modes;

	public ModeSetting(String name, String defaultMode, String... modes) {
		this.name = name;
		
		List<String> allModes = new ArrayList<String>();
		allModes.add(defaultMode);
		allModes.addAll(Arrays.asList(modes));
		
		this.modes = allModes;
		index = this.modes.indexOf(defaultMode);
	}
	
	public String getMode() {
		return modes.get(index);
	}

	public int getCap() {
		return modes.size();
	}
	
	public boolean is(String mode) {
		return index == modes.indexOf(mode);
	}
	 
	public void cycle() {
		if(index < modes.size() - 1) 
			index++;
		else 
			index = 0;
	}
	
}
