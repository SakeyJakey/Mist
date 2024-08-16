package dev.sakey.mist.events;

public class Event<T> {

	private boolean cancelled;
	private EventType type;

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public boolean isPre() {
		if (type == null)
			return false;
		return type == EventType.PRE;
	}

	public boolean isPost() {
		if (type == null)
			return false;
		return type == EventType.POST;
	}
}