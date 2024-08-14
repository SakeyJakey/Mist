package dev.sakey.mist.scripts;
import dev.sakey.mist.Mist;
import dev.sakey.mist.scripts.bindings.ScriptBindings;
import dev.sakey.mist.scripts.bindings.mist.MistBindings;
import dev.sakey.mist.scripts.bindings.mist.impl.PlayerBinding;
import dev.sakey.mist.scripts.bindings.mist.impl.ScriptBinding;
import dev.sakey.mist.scripts.bindings.tenacity.TenacityBindings;
import dev.sakey.mist.ui.notifications.Notification;
import dev.sakey.mist.ui.notifications.NotificationType;

import javax.script.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import org.graalvm.polyglot.Context;

public class ScriptLoader {


	public File ROOT_DIR = new File(Mist.instance.getPermName());
	public File SCRIPT_DIR = new File(ROOT_DIR, "Scripts");

	public File CEDOSCRIPT_DIR = new File(SCRIPT_DIR, "cedoscripts");
	public File NOVOSCRIPT_DIR = new File(SCRIPT_DIR, "novosScripts");

	public void loadScript(Script script) {

		if(!ROOT_DIR.exists())
			if(!ROOT_DIR.mkdirs())
				new Notification("Failed to load script", "Could not create Mist folder", NotificationType.WARNING, 5000);

		if(!SCRIPT_DIR.exists())
			if(!SCRIPT_DIR.mkdirs())
				new Notification("Failed to load script", "Could not create Scripts folder", NotificationType.WARNING, 5000);

		if(!CEDOSCRIPT_DIR.exists())
			if(!CEDOSCRIPT_DIR.mkdirs())
				new Notification("Failed to load script", "Could not create cedoscripts folder", NotificationType.WARNING, 5000);

		if(!NOVOSCRIPT_DIR.exists())
			if(!NOVOSCRIPT_DIR.mkdirs())
				new Notification("Failed to load script", "Could not create cedoscripts folder", NotificationType.WARNING, 5000);

		ScriptBindings binder;

		binder = new MistBindings();

		File currentScript = new File(SCRIPT_DIR, script.getName() + ".js");

		if(!currentScript.exists()) {
			currentScript = new File(CEDOSCRIPT_DIR, script.getName() + ".js");
			binder = new TenacityBindings();
		}

		if(!currentScript.exists()) {
			currentScript = new File(NOVOSCRIPT_DIR, script.getName() + ".js");
		}

		if(!currentScript.exists()) {
			new Notification("Failed to load script", "Script not found", NotificationType.WARNING, 5000);
		}


		String scriptContents;

		try {
			BufferedReader scriptReader = new BufferedReader(new FileReader(currentScript));
			scriptContents = scriptReader.lines().collect(Collectors.joining("\n"));
		} catch (FileNotFoundException e) {
			new Notification("Failed to load script", "File not found", NotificationType.WARNING, 5000);
			return;
		}

//		Bindings bindings = binder.bind(script);

		Context context = binder.bind();

		try {
//			CompiledScript compiledScript = ((Compilable) script.getEngine()).compile(scriptContents);
//			compiledScript.eval(bindings);

//			script.getEngine().eval(
//					scriptContents,
//					bindings
//			);

			context.eval("js", scriptContents);
		}

		catch (Exception e) {
			new Notification("Failed to parse script", e.getMessage(), NotificationType.WARNING, 5000);
		}
	}

	public ArrayList<String> getScripts() {
		ArrayList<String> configs =
				Arrays.stream(
						Objects.requireNonNull(
								SCRIPT_DIR.listFiles(
										(dir, name) -> name.endsWith(".js")
								)
						)
				).map(
						f -> f.getName().substring(0, f.getName().length() - 3)
				).collect(
						Collectors.toCollection(ArrayList::new)
				);

		return configs;
	}
}
