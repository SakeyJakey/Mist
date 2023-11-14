package dev.sakey.mist.utils.client;

public class MathUtils {

	public static Number minMaxZeroToOneNumber(Number number) {
		return Math.min(1, Math.max(0, number.doubleValue()));
	}

	public static Number minMaxZeroTo255Number(Number number) {
		return Math.min(255, Math.max(0, number.doubleValue()));
	}

}
