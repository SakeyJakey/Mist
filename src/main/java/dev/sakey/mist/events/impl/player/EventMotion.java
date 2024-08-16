package dev.sakey.mist.events.impl.player;

import dev.sakey.mist.events.Event;
import dev.sakey.mist.events.EventType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventMotion extends Event {

	private double x, y, z;
	private float yaw, pitch;
	private boolean onGround;

	public EventMotion(EventType type, double x, double y, double z, float yaw, float pitch, boolean onGround) {
		setType(type);
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
	}
}