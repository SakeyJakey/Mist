package dev.sakey.mist.utils.client.animation;

import dev.sakey.mist.utils.client.TimerUtil;

public abstract class Animation {

    protected TimerUtil timer = new TimerUtil();
    protected double end;
    protected int length;
    protected Direction direction;

    public Animation(int length, double end){
        this.length = length;
        this.end = end;
        this.direction = Direction.FORWARD;
    }

    public Animation(int length, double end, Direction direction){
        this.length = length;
        this.end = end;
        this.direction = direction;
    }

    public boolean finished(Direction direction) {
        return isDone() && this.direction.equals(direction);
    }

    public double getLinearOutput() {
        return 1 - ((timer.getTime() / (double) length) * end);
    }

    public double getend() {
        return end;
    }

    public void setend(double end) {
        this.end = end;
    }

    public void reset() {
        timer.reset();
    }

    public boolean isDone() {
        return timer.hasTimeElapsed(length);
    }

    public void changeDirection() {
        setDirection(direction.opposite());
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        if (this.direction != direction) {
            this.direction = direction;
            timer.setTime(System.currentTimeMillis() - (length - Math.min(length, timer.getTime())));
        }
    }

    public void setlength(int length) {
        this.length = length;
    }

    protected boolean correctOutput() {
        return false;
    }

    public double getOutput() {
        if (direction == Direction.FORWARD) {
            if (isDone())
                return end;
            return (getEquation(timer.getTime()) * end);
        } else {
            if (isDone()) return 0;
            if (correctOutput()) {
                double revTime = Math.min(length, Math.max(0, length - timer.getTime()));
                return getEquation(revTime) * end;
            } else return (1 - getEquation(timer.getTime())) * end;
        }
    }


    //This is where the animation equation should go, for example, a logistic function. Output should range from 0 - 1.
    //This will take the timer's time as an input, x.
    protected abstract double getEquation(double x);

    public enum Direction {
        FORWARD,
        BACKWARD;

        public Direction opposite() {
            if (this == Direction.FORWARD) {
                return Direction.BACKWARD;
            } else return Direction.FORWARD;
        }
    }

}
