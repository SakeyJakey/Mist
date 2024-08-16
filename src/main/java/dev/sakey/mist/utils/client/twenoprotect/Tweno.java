package dev.sakey.mist.utils.client.twenoprotect;

import net.minecraft.client.Minecraft;

public class Tweno {
	public static int check() {
		return 1;
	}
/*
    public static int check() {
        try {
            final String currentHWID = HWID.getHWID();

            try
            {
                URL url = new URL("http://localhost:6969/auth/" + currentHWID);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int code = connection.getResponseCode();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = bufferedReader.readLine();

                if(code == 200) {
                    return Integer.parseInt(line);
                }
            }
            catch(Exception ignored) { }

            Display.destroy();

            MiscUtils.copyToClipboard(currentHWID);
            JOptionPane.showMessageDialog(null, "Mist was unable to verify your ID. If you have paid for Mist, please give staff your ID(in your clipboard) in case you were not added", "Authentication Error", JOptionPane.WARNING_MESSAGE);

            ABORTtheMISSIONthisGUYisCRACKING();
            return -1;
        }
        catch(Exception e) {
            Display.destroy();

            JOptionPane.showMessageDialog(null, "Mist was unable to verify your ID. Please report this to staff: " + getOSIdentifier(), "Authentication Error", JOptionPane.WARNING_MESSAGE);
            if(e.getMessage().contains("lshw"))
                JOptionPane.showMessageDialog(null, "Detected issue: Please install the package 'lshw'", "Authentication Error", JOptionPane.WARNING_MESSAGE);

            ABORTtheMISSIONthisGUYisCRACKING();
            return -1;
        }
    }
	*/

	public static void ABORTtheMISSIONthisGUYisCRACKING() {
		Minecraft mc = Minecraft.getMinecraft();
		mc.thePlayer = null;
		mc.timer = null;
		mc.renderGlobal = null;
		mc.entityRenderer = null;
		mc.gameSettings = null;
		System.exit(0);
	}

	public static String getOSIdentifier() {
		String osName = System.getProperty("os.name").toLowerCase();

		if (osName.contains("win")) {
			return "W";
		} else if (osName.contains("mac")) {
			return "M";
		} else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
			return "N";
		} else {
			return "U";
		}
	}
}
