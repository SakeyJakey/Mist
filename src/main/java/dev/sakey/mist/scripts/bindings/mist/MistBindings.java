package dev.sakey.mist.scripts.bindings.mist;

import dev.sakey.mist.scripts.Script;
import dev.sakey.mist.scripts.bindings.ScriptBindings;
import dev.sakey.mist.scripts.bindings.mist.impl.PlayerBinding;
import dev.sakey.mist.scripts.bindings.mist.impl.ScriptBinding;

import javax.script.Bindings;

public class MistBindings extends ScriptBindings {
	public Bindings bind(Script script) {
		Bindings bindings = script.getEngine().createBindings();
		bindings.put("script", new ScriptBinding());
		bindings.put("player", new PlayerBinding());
		return bindings;
	}
}
