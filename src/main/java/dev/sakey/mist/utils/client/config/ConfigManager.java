package dev.sakey.mist.utils.client.config;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.google.gson.*;

import dev.sakey.mist.Mist;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.settings.Setting;
import dev.sakey.mist.modules.settings.impl.BoolSetting;
import dev.sakey.mist.modules.settings.impl.KeySetting;
import dev.sakey.mist.modules.settings.impl.ModeSetting;
import dev.sakey.mist.modules.settings.impl.NumberSetting;
import dev.sakey.mist.ui.notifications.Notification;
import dev.sakey.mist.ui.notifications.NotificationType;
import lombok.Getter;

public class ConfigManager {
	private final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
	private final JsonParser jsonParser = new JsonParser();

	public File ROOT_DIR = new File(Mist.instance.getPermName());
	public File CONFIG_DIR = new File(ROOT_DIR, "Configs");
	public File config = new File(CONFIG_DIR, "default.json");
	public File readme = new File(ROOT_DIR, "readme.txt");

	@Getter
	public String currentlySelected;

	public boolean isCurrentlySelected(String name) {
		return Objects.equals(getCurrentlySelected(), name);
	}

	public void init() {
		if(!ROOT_DIR.exists()) { ROOT_DIR.mkdirs(); }
		if(!CONFIG_DIR.exists()) { CONFIG_DIR.mkdirs(); }
		
		try {
			readme.createNewFile();
			PrintWriter pw = new PrintWriter(new FileWriter(readme));
			pw.print("If the client doesn't load, try deleting the '.json' config(the default config)");
			pw.close();
		} catch (IOException e) {
			Mist.instance.getLogger().warn(Mist.instance.name + " couldn't create readme. Please report this to the devs: " + e);
		}
		
		if(!config.exists())
			saveConfig();
		else
			loadConfig();
	}

	public boolean deleteConfig(String name) {
		loadConfig();
		return new File(CONFIG_DIR, name + ".json").delete();
	}

	public ArrayList<String> getConfigs() {
		ArrayList<String> configs =
				Arrays.stream(
						Objects.requireNonNull(
								CONFIG_DIR.listFiles(
										(dir, name) -> name.endsWith(".json")
								)
						)
				).map(
						f -> f.getName().substring(0, f.getName().length() - 5)
				).collect(
						Collectors.toCollection(ArrayList::new)
				);

		return configs;
	}

	public void saveConfig() {
		saveConfig(config.getName(), true);
	}

	public void saveConfig(String name) {
		currentlySelected = name;
		saveConfig(name, false);
	}

	private void saveConfig(String name, boolean alreadyHasExt) {
		try {
			File currentConfig = new File(CONFIG_DIR, name.toLowerCase() + (alreadyHasExt ? "" : ".json"));
			if(!currentConfig.exists()) currentConfig.createNewFile();
			
			JsonObject json = new JsonObject();
			for(Module m : Mist.instance.getModuleManager().getModules()) {
				JsonObject jsonMod = new JsonObject();
				jsonMod.addProperty("enabled", m.isEnabled());
				JsonObject jsonSettings = new JsonObject();
				for(Setting s : m.getSettings()) {
					if(s instanceof BoolSetting)
						jsonSettings.addProperty(s.getName(), ((BoolSetting)s).isEnabled());
					if(s instanceof NumberSetting)
						jsonSettings.addProperty(s.getName(), ((NumberSetting)s).getValue());
					if(s instanceof ModeSetting)
						jsonSettings.addProperty(s.getName(), ((ModeSetting)s).getMode());
					if(s instanceof KeySetting)
						jsonSettings.addProperty(s.getName(), ((KeySetting)s).getCode());
				}
				jsonMod.add("settings", jsonSettings);
				json.add(m.getName(), jsonMod);
			}
			PrintWriter save = new PrintWriter(new FileWriter(currentConfig));
			save.println(prettyGson.toJson(json));
			save.close();
		}
		catch(Exception e){
			Mist.instance.getLogger().error(Mist.instance.name + " couldn't save config. Please report this to the devs: " + e.getMessage());
		}
	}

	
	private final HashSet<String> modBlacklist = Sets.newHashSet("ClickGUI");

	public boolean isModBlacklisted(String m) {
		return modBlacklist.contains(m);
	}

	public boolean isModBlacklisted(Module m) {
		return modBlacklist.contains(m.getName());
	}
	
	public void loadConfig() {
		currentlySelected = "default";
		loadConfig(config.getName(), true);
	}

	public void loadConfig(String name) {
		loadConfig(name, false);
		currentlySelected = name;
	}

	private void loadConfig(String name, boolean alreadyHasExt) {
		try {
			File currentConfig = new File(CONFIG_DIR, name.toLowerCase() + (alreadyHasExt ? "" : ".json"));

			if(!currentConfig.exists()) return;

			BufferedReader load = new BufferedReader(new FileReader(currentConfig));
			JsonObject json = jsonParser.parse(load).getAsJsonObject();
			load.close();
			for (Entry<String, JsonElement> entry : json.entrySet()) {
				Module m = Mist.instance.getModuleManager().getModule(entry.getKey());
				if (m != null && !modBlacklist.contains(m.getName())) {

					JsonObject jsonModule = (JsonObject) entry.getValue();
					boolean enabled = jsonModule.get("enabled").getAsBoolean();
					if (enabled)
						m.enable();
					else
						m.disable();
					for (Setting s : m.getSettings()) {
						if (s instanceof BoolSetting)
							((BoolSetting) s).setEnabled(jsonModule.get("settings").getAsJsonObject().get(s.getName()).getAsBoolean());

						if (s instanceof NumberSetting)
							((NumberSetting) s).setValue(jsonModule.get("settings").getAsJsonObject().get(s.getName()).getAsDouble());

						if (s instanceof ModeSetting) { //GREAT code!!!11!!1!!
							int cap = 0;
							while (!((ModeSetting) s).is(jsonModule.get("settings").getAsJsonObject().get(s.getName()).getAsString())) {
								if(cap > ((ModeSetting) s).getCap()) {
									new Notification("Faild to load config", "This config has an invalid mode.", NotificationType.WARNING, 5000);
									break;
								}
								((ModeSetting) s).cycle();
								cap++;
							}
						}
						if (s instanceof KeySetting)
							((KeySetting) s).setCode(jsonModule.get("settings").getAsJsonObject().get(s.getName()).getAsInt());

					}
				}
			}
		}
		catch(Exception e) {
			Mist.instance.getLogger().error(Mist.instance.name + " couldn't load config. Please report this to the devs: " + e.getMessage());
			new Notification("Faild to load config", "This config may be corrupted.", NotificationType.WARNING, 5000);
		}
	}
}
