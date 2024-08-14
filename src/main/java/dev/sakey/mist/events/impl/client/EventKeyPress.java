package dev.sakey.mist.events.impl.client;

import dev.sakey.mist.events.Event;

public class EventKeyPress extends Event {
    private int key;

    public EventKeyPress(int key){
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
