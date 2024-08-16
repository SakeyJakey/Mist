package dev.sakey.mist.scripts.bindings.mist;

import dev.sakey.mist.scripts.bindings.ScriptBindings;
import dev.sakey.mist.scripts.bindings.mist.impl.PlayerBinding;
import dev.sakey.mist.scripts.bindings.mist.impl.ScriptBinding;
import org.graalvm.polyglot.Context;

public class MistBindings extends ScriptBindings {
//	public Bindings bind(Script script) {
//		Bindings bindings = script.getEngine().createBindings();
//		bindings.put("script", new ScriptBinding());
//		bindings.put("player", new PlayerBinding());
//		return bindings;
//	}

	public Context bind() {
		Context context = Context.newBuilder().allowAllAccess(true).build();
		context.getBindings("js").putMember("script", new ScriptBinding());
		context.getBindings("js").putMember("player", new PlayerBinding());
		return context;
	}
}
