package dev.sakey.mist.utils.client;

import dev.sakey.mist.Mist;

public class MistParser {

	public static String parse(String text) {
		return text
				.replace("client.name", Mist.instance.name)
				.replace("client.id", Mist.instance.versionId)
				.replace("client.version", Mist.instance.versionName);
	}

}
