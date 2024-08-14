package dev.sakey.mist.utils.client.animation.impl;

import dev.sakey.mist.utils.client.animation.Animation;

public class SmoothStep extends Animation {

    public SmoothStep(int ms, double endPoint) {
        super(ms, endPoint);
    }

    public SmoothStep(int ms, double endPoint, Direction direction) {
        super(ms, endPoint, direction);
    }

    protected double getEquation(double x) {
        double x1 = x / (double) length; //Used to force input to range from 0 - 1
        return -2 * Math.pow(x1, 3) + (3 * Math.pow(x1, 2));
    }

}