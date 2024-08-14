package dev.sakey.mist;

import dev.sakey.mist.events.EventHandler;
import dev.sakey.mist.events.EventManager;
import dev.sakey.mist.events.impl.client.EventKeyPress;
import dev.sakey.mist.modules.Module;
import dev.sakey.mist.modules.ModuleManager;
import dev.sakey.mist.scripts.Script;
import dev.sakey.mist.scripts.ScriptLoader;
import dev.sakey.mist.ui.draggables.DraggableManager;
import dev.sakey.mist.utils.client.config.ConfigManager;
import dev.sakey.mist.utils.client.Logger;
import dev.sakey.mist.utils.client.font.GlyphPage;
import dev.sakey.mist.utils.client.font.GlyphPageFontRenderer;
import dev.sakey.mist.utils.client.twenoprotect.Tweno;
import lombok.Getter;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public enum Mist {

    instance;

    public String name = "Mist";

	@Getter
	private final String permName = "Mist";

	@Getter
	private int uid;
    public final String versionId = "b2", versionName = "night";

    @Getter
	private Logger logger;
    @Getter
	private ModuleManager moduleManager;
    @Getter
	private EventManager eventManager;

	@Getter
	private ScriptLoader scriptLoader;

	@Getter
	private DraggableManager draggableManager;

	@Getter
	private ConfigManager configManager;

    public boolean destructed = true; // true at start, so we can draw splash screen after Mist has loaded

    private final ArrayList<GlyphPageFontRenderer> fontRenderers = new ArrayList<GlyphPageFontRenderer>();

	@Getter
	private boolean loadedFontRenderers = false;

    public GlyphPageFontRenderer getFontRenderer() {
        return getFontRenderer(Constants.fontSize);
    }

    public GlyphPageFontRenderer getFontRenderer(int size) {
        return fontRenderers.get(size - Constants.minFontSize + 1);
    }

    public GlyphPageFontRenderer getMaxSizeFontRenderer() {
        return fontRenderers.get(Constants.maxFontSize - Constants.minFontSize - 1);
    }

    public void setName(String name) {
        this.name = name;
        Display.setTitle(name + " " + versionId);
    }

    @SneakyThrows
	public void hook() {
        destructed = false;

        System.out.println(name + " " + versionId + " - Hooking to Minecraft...");

        logger = new Logger();

		logger.log("Check");
		uid = Tweno.check();
		if(uid == -1) // if it somehow gets here
			Tweno.ABORTtheMISSIONthisGUYisCRACKING();

		logger.log("Event");
        eventManager = new EventManager();

		//logger.log("Fonts");
		//setupFonts();

		logger.log("Draggable");
		draggableManager = new DraggableManager();

		logger.log("Modules");
        moduleManager = new ModuleManager();

		logger.log("Scripts");
		scriptLoader = new ScriptLoader();

		logger.log("Configs");
		configManager = new ConfigManager();
		configManager.init();

		logger.log("Events");
        eventManager.registerEventHandler(EventKeyPress.class, eventKeyPress);

		logger.log("Name");
		setName(name);

		logger.log("Logo");
        InputStream icon16 = getClass().getResourceAsStream("/logo16.png");
        InputStream icon32 = getClass().getResourceAsStream("/logo32.png");

        try {
            Display.setIcon(new ByteBuffer[] { Minecraft.getMinecraft().readImageToBuffer(icon16), Minecraft.getMinecraft().readImageToBuffer(icon32) });
        }
        catch (Exception e) {
            logger.error("Failed to set icon!");
        }

        logger.log("Hooked to Minecraft!");

		scriptLoader.loadScript(new Script("bhop"));
	}

    final EventHandler<EventKeyPress> eventKeyPress = e -> {
        if(e.getKey() == Keyboard.KEY_DELETE) {
			onClose();
            hook();
        }

        if(e.getKey() == Keyboard.KEY_GRAVE) {
            logger.log("Destroyed {client.name}.");
            moduleManager.getModules().forEach(Module::disable);

            Display.setTitle("Minecraft 1.8.9");
            Minecraft.getMinecraft().setWindowIcon();

            Minecraft.getMinecraft().getNetHandler().handleTitle(new S45PacketTitle(S45PacketTitle.Type.TITLE, new ChatComponentText(" ")));
            Minecraft.getMinecraft().getNetHandler().handleTitle(new S45PacketTitle(S45PacketTitle.Type.SUBTITLE, new ChatComponentText("Destroyed")));

            destructed = true;
            return;
        }

        for(Module m :
                moduleManager.getModules()){
            if(m.getKeyCode() == e.getKey()) m.toggle();
        }
    };

	public void onClose() {
		if(!destructed)
			configManager.saveConfig();
	}

	@SneakyThrows
	public void setupFonts() {
		loadedFontRenderers = true;

        char[] chars = new char[256];
        for(int i = 0; i < chars.length; i++) {
            chars[i] = (char)i;
        }

        InputStream fontResource = Mist.class.getResourceAsStream("/Mist/Fonts/Poppins-Regular.ttf");
		InputStream fontResourceBold = Mist.class.getResourceAsStream("/Mist/Fonts/Poppins-Bold.ttf");

		Font font;

		Font fontBold;

		font = Font.createFont(Font.TRUETYPE_FONT, fontResource);

		fontBold = Font.createFont(Font.TRUETYPE_FONT, fontResourceBold);

		for (int i = Constants.minFontSize; i < Constants.maxFontSize + 2 /* idc anymore*/; i++) {
			//regular

			GlyphPage gp = new GlyphPage(font.deriveFont(Font.PLAIN, i), true, true);
			gp.generateGlyphPage(chars);
			gp.setupTexture();

			GlyphPage gpi = new GlyphPage(font.deriveFont(Font.ITALIC, i), true, true);
			gpi.generateGlyphPage(chars);
			gpi.setupTexture();


			GlyphPage gpb = new GlyphPage(fontBold.deriveFont(Font.PLAIN, i), true, true);
			gpb.generateGlyphPage(chars);
			gpb.setupTexture();

			GlyphPage gpbi = new GlyphPage(fontBold.deriveFont(Font.ITALIC, i), true, true);
			gpbi.generateGlyphPage(chars);
			gpbi.setupTexture();

			fontRenderers.add(i - Constants.minFontSize, new GlyphPageFontRenderer(gp, gpb, gpi, gpbi));
		}
	}

    public static class Constants
    {
        public static final int
                fontSize = 13,
                largeFontSize = 32,
                minFontSize = 10,
                maxFontSize = 48;
    }
}
