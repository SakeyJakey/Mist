package dev.sakey.mist.utils.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class NonMinecraftNetworkUtils {

	public static String getUrlContents(String theUrl)
	{
		StringBuilder content = new StringBuilder();
		try
		{
			URL url = new URL(theUrl);
			URLConnection urlConnection = url.openConnection();

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String line;

			while ((line = bufferedReader.readLine()) != null)
			{
				content.append(line).append("\n");
			}
			bufferedReader.close();
		}
		catch(Exception ignored) { }
		return content.toString();
	}
}
