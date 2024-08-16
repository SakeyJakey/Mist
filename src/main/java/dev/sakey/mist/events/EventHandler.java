package dev.sakey.mist.events;

public interface EventHandler<T extends Event> {
	void handle(T event);
}