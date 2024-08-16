package dev.sakey.mist.events.impl.client;

import dev.sakey.mist.events.Direction;
import dev.sakey.mist.events.Event;
import net.minecraft.network.Packet;

public class EventPacket extends Event {

	private Packet<?> packet;
	private Direction direction;

	public EventPacket(Packet<?> packet, Direction direction) {
		this.packet = packet;
		this.direction = direction;
	}

	public Packet<?> getPacket() {
		return packet;
	}

	public void setPacket(Packet<?> packet) {
		this.packet = packet;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
