package dev.sakey.mist.scripts.bindings.tenacity;

import dev.sakey.mist.scripts.Script;
import dev.sakey.mist.scripts.bindings.ScriptBindings;
import dev.sakey.mist.scripts.bindings.tenacity.impl.GlobalBindings;

import javax.script.Bindings;

public class TenacityBindings extends ScriptBindings {

	// I did not skid this. I had to recreate the entire scripting api.

	public Bindings bind(Script script) {
		Bindings bindings = script.getEngine().createBindings();
		bindings.put("initScript", new GlobalBindings.InitScript());

		return bindings;
	}
}
