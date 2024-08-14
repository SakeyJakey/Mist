package dev.sakey.mist.utils.client.animation.impl;

import dev.sakey.mist.utils.client.animation.Animation;

public class EaseOut extends Animation {

    public EaseOut(int ms, double endPoint) {
        super(ms, endPoint);
    }

    public EaseOut(int ms, double endPoint, Direction direction) {
        super(ms, endPoint, direction);
    }

    protected double getEquation(double x) {
        double x1 = x / length;
        return 1 - ((x1 - 1) * (x1 - 1));
    }

}
