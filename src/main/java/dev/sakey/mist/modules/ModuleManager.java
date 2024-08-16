package dev.sakey.mist.modules;

import dev.sakey.mist.modules.impl.HUD.FPS;
import dev.sakey.mist.modules.impl.HUD.Keystrokes;
import dev.sakey.mist.modules.impl.HUD.Radar;
import dev.sakey.mist.modules.impl.combat.KillAura;
import dev.sakey.mist.modules.impl.combat.KillInsults;
import dev.sakey.mist.modules.impl.combat.Reach;
import dev.sakey.mist.modules.impl.exploit.Canceller;
import dev.sakey.mist.modules.impl.exploit.Crasher;
import dev.sakey.mist.modules.impl.exploit.Disabler;
import dev.sakey.mist.modules.impl.misc.AntiIdiot;
import dev.sakey.mist.modules.impl.misc.anticheat.AntiCheat;
import dev.sakey.mist.modules.impl.movement.*;
import dev.sakey.mist.modules.impl.player.*;
import dev.sakey.mist.modules.impl.render.*;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ModuleManager {

	private final CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<Module>();

	public ModuleManager() {
		addModules();
	}


	public void addModule(Module m) {
		modules.add(m);
	}

	private void addModules() {
		modules.add(new HUD());
		modules.add(new Fly());
		modules.add(new Sprint());
		modules.add(new ESP());
		modules.add(new Scaffold());
		modules.add(new LongJump());
		modules.add(new KillAura());
		modules.add(new ClickGuiMod());
		modules.add(new SafeWalk());
		modules.add(new NoFall());
		modules.add(new Velocity());
		modules.add(new Phase());
		modules.add(new Speed());
		modules.add(new AutoArmor());
		modules.add(new KillInsults());
		modules.add(new AntiIdiot());
		modules.add(new Canceller());
		modules.add(new FPS());
		modules.add(new Radar());
		modules.add(new Tracers());
		modules.add(new ChestStealer());
		modules.add(new InventoryCleaner());
		modules.add(new Chams());
		modules.add(new TargetHUD());
		modules.add(new ShaderESP());
		modules.add(new NotificationsMod());
		modules.add(new Disabler());
		modules.add(new Keystrokes());
		modules.add(new Animations());
		modules.add(new FullBright());
		modules.add(new XRay());
		modules.add(new Crasher());
		modules.add(new Reach());
		modules.add(new AntiCheat());
	}

	public CopyOnWriteArrayList<Module> getModules() {
		return modules;
	}

	public Module getModule(String name) {
		return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}

	public Module getModule(Class c) {
		return modules.stream().filter(module -> module.getClass() == c).findFirst().orElse(null);
	}

	public CopyOnWriteArrayList<Module> getModules(Category moduleCategory) {
		return modules.stream().filter(module -> module.getCategory().equals(moduleCategory)).collect(Collectors.toCollection(CopyOnWriteArrayList::new));
	}

	public int modulesEnabled() {
		int modulesEnabled = 0;
		for (Module m :
				modules)
			if (m.isEnabled())
				modulesEnabled++;
		return modulesEnabled;
	}

}
