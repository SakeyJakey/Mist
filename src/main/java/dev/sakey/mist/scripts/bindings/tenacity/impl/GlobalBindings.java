package dev.sakey.mist.scripts.bindings.tenacity.impl;

import jdk.nashorn.api.scripting.JSObject;

import java.util.function.Function;

public class GlobalBindings {
	public static class InitScript implements Function<JSObject, ScriptBinding> {
		public ScriptBinding apply(JSObject object) {
			object.getMember("name");
			return new ScriptBinding();
		}
	}
}
