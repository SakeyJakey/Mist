package dev.sakey.mist.scripts.bindings;

import dev.sakey.mist.scripts.Script;

import javax.script.Bindings;

public abstract class ScriptBindings {
	public abstract Bindings bind(Script script);
}
