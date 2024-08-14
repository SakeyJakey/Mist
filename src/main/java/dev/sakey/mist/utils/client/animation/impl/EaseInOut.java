package dev.sakey.mist.utils.client.animation.impl;


import dev.sakey.mist.utils.client.animation.Animation;

public class EaseInOut extends Animation {

    public EaseInOut(int ms, double endPoint) {
        super(ms, endPoint);
    }

    public EaseInOut(int ms, double endPoint, Direction direction) {
        super(ms, endPoint, direction);
    }

    protected double getEquation(double x1) {
        double x = x1 / length;
        return x < 0.5 ? 2 * Math.pow(x, 2) : 1 - Math.pow(-2 * x + 2, 2) / 2;
    }

}
