package dev.sakey.mist.utils.render;

import dev.sakey.mist.utils.client.MathUtils;

import java.awt.*;

public class ColourUtil {

    public static int getRainbow(double seconds) {
        return getRainbow((float)seconds);
    }

    public static int getRainbow(double seconds, long wave) {
        return getRainbow((float)seconds, wave);
    }

    public static int getRainbow(float seconds) {
        float hue = (System.currentTimeMillis() % (int)(seconds * 1000)) / (float)(seconds * 1000);
        return Color.HSBtoRGB(hue, 1, 1);
    }

    public static int interpolate() {

        //todo finish
        return 0;
    }

    public static int getRainbow(float seconds, long wave) {
        if(seconds == 0) return -1;

        float hue = ((wave + System.currentTimeMillis()) % (int)(seconds * 1000)) / (float)(seconds * 1000);
        return Color.HSBtoRGB(hue, 1, 1);
    }

    public static int dim(int colour, int amount) { //todo: make proper dim and bright
        Color c = new Color(colour);
        for (int i = 0; i < amount; i++)
            c = c.darker();
        return c.getRGB();
    }

    public static int alphaColour(int colour, float alpha) {
        Color original = new Color(colour);
        alpha = MathUtils.minMaxZeroToOneNumber(alpha).floatValue();
        return new Color(
                MathUtils.minMaxZeroTo255Number(original.getRed()).intValue(),
                MathUtils.minMaxZeroTo255Number(original.getGreen()).intValue(),
                MathUtils.minMaxZeroTo255Number(original.getBlue()).intValue(),
                MathUtils.minMaxZeroTo255Number(original.getAlpha() * alpha).intValue()
        ).getRGB();
    }

    public static Color decToColour(int colour) {
        return new Color(colour);
    }

    public static int primary() {
        return 0xffc51a71;
    }
    public static int white() {
        return 0xffffffff;
    }
    public static int grey() {
        return new Color(45,45,45).getRGB();
    }

    public static int black() {
        return 0xff000000;
    }
}
