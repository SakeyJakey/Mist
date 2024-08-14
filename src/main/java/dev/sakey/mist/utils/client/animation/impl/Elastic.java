package dev.sakey.mist.utils.client.animation.impl;

import dev.sakey.mist.utils.client.animation.Animation;

public class Elastic extends Animation {

    float easeAmount;
    float smooth;
    boolean reallyElastic;

    public Elastic(int ms, double endPoint, float elasticity, float smooth, boolean moreElasticity) {
        super(ms, endPoint);
        this.easeAmount = elasticity;
        this.smooth = smooth;
        this.reallyElastic = moreElasticity;
    }

    public Elastic(int ms, double endPoint, float elasticity, float smooth, boolean moreElasticity, Direction direction) {
        super(ms, endPoint, direction);
        this.easeAmount = elasticity;
        this.smooth = smooth;
        this.reallyElastic = moreElasticity;
    }

    @Override
    protected double getEquation(double x) {
        double x1 = Math.pow(x / length, smooth); //Used to force input to range from 0 - 1
        double elasticity = easeAmount * .1f;
        return Math.pow(2, -10 * (reallyElastic ? Math.sqrt(x1) : x1)) * Math.sin((x1 - (elasticity / 4)) * ((2 * Math.PI) / elasticity)) + 1;
    }
}