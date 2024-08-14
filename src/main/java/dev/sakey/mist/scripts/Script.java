package dev.sakey.mist.scripts;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Script {


	@Getter private String name;
	@Getter private ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
	Invocable invocable = (Invocable) engine;
	public Script(String name) {
		this.name = name;
	}

	public Object executeJavascriptFunction(String functionName, Object... args) {
		try {
			return invocable.invokeFunction(functionName, args);
		} catch (Exception ignored) { return null; }
	}
}
