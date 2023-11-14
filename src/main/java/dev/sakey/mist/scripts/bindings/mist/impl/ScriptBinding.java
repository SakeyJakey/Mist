package dev.sakey.mist.scripts.bindings.mist.impl;

public class ScriptBinding {
	public ModuleBinding registerModule(String name, String category) {
		return new ModuleBinding(name, category);
	}
}
