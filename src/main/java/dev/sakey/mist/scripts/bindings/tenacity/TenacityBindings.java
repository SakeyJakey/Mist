package dev.sakey.mist.scripts.bindings.tenacity;

import dev.sakey.mist.scripts.bindings.ScriptBindings;
import org.graalvm.polyglot.Context;

public class TenacityBindings extends ScriptBindings {
	public Context bind() {
		//Context context = Context.newBuilder().allowAllAccess(true).build();
		//context.getBindings("js").putMember("initScript", new GlobalBindings.InitScript());
		return null;
	}
}
