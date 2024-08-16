package dev.sakey.mist.modules.settings.impl;

import dev.sakey.mist.modules.settings.Setting;

public class KeySetting extends Setting {

	private int code;

	public KeySetting(int keyCode) {
		this.name = "Key";
		this.code = keyCode;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int keyCode) {
		this.code = keyCode;
	}
}
