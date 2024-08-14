package dev.sakey.mist.scripts.bindings.tenacity.impl;

import org.graalvm.polyglot.Value;

import java.util.function.Function;

public class GlobalBindings {
	public static class InitScript implements Function<Value, ScriptBinding> {

		public ScriptBinding apply(Value object) {
			return new ScriptBinding(
					String.valueOf(object.getMember("name")),
					String.valueOf(object.getMember("description")),
					String.valueOf(object.getMember("author"))
			);
		}

	}
}
