package dev.sakey.mist.events.impl.render;

import dev.sakey.mist.events.Event;
import net.minecraft.entity.Entity;

public class EventRenderEntity extends Event {

	private final Entity entity;

	public EventRenderEntity(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() { //TODO: maybe add setter for making everything a sheep or smn
		return entity;
	}
}
