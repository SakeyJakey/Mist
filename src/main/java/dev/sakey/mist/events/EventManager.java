package dev.sakey.mist.events;

import dev.sakey.mist.Mist;
import net.minecraft.client.Minecraft;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager<T extends Event> {

	private final Map<Type, CopyOnWriteArrayList<EventHandler<T>>> handlerMap = new HashMap<>();

	public void handleEvent(T type) {
		if (Mist.instance.destructed) return;
		if (handlerMap.get(type.getClass()) == null) return;
		for (EventHandler<T> handler :
				handlerMap.get(type.getClass())) {
			if (handler != null && Minecraft.getMinecraft().thePlayer != null) handler.handle(type);
		}
	}

	public void registerEventHandler(Type type, EventHandler handler) {
		CopyOnWriteArrayList<EventHandler<T>> handlers;
		if (handlerMap.containsKey(type)) {
			handlers = handlerMap.get(type);
			handlers.add(handler);
			handlerMap.remove(type);
		} else {
			handlers = new CopyOnWriteArrayList<EventHandler<T>>();
			handlers.add(handler);
		}
		handlerMap.put(type, handlers);
	}

	public void unregisterEventHandler(EventHandler handler) {
		for (List<EventHandler<T>> handlers :
				handlerMap.values()) {
			handlers.remove(handler);
		}
	}
}
