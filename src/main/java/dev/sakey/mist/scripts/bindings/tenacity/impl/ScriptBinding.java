package dev.sakey.mist.scripts.bindings.tenacity.impl;

import dev.sakey.mist.modules.Module;
import dev.sakey.mist.events.Event;
import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.Mist;
import dev.sakey.mist.events.impl.render.EventRenderWorld;
import java.util.function.Consumer;

public class ScriptBinding {
	private String name, description;
	private ScriptModule module;

	Runnable onEnable = () -> {};
	Runnable onDisable = () -> {};

	public ScriptBinding(String name, String description, String author) {
		module = new ScriptModule(name, description + " (by " + author + ")");
		Mist.instance.getModuleManager().addModule(module);
	}

	public void onRender3D(Consumer<Event> cons) {
		EventHandler eh = e -> { if(module.isEnabled()) cons.accept(e); };
		Mist.instance.getEventManager().registerEventHandler(EventRenderWorld.class, eh);
	}

	public void onEnable(Runnable run) {
		onEnable = run;
	}

	public void onDisable(Runnable run) {
		onDisable = run;
	}

	private class ScriptModule extends Module {
		public ScriptModule(String name, String description) {
			this.name = name;
			this.description = description;
		}

		protected void onEnable() { onEnable.run(); }
		protected void onDisable() { onDisable.run(); }
	}
}
