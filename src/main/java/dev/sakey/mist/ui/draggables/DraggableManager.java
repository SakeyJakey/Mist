package dev.sakey.mist.ui.draggables;

import java.util.concurrent.CopyOnWriteArrayList;

public class DraggableManager {

	private final CopyOnWriteArrayList<Draggable> draggables = new CopyOnWriteArrayList<Draggable>();

	public void add(Draggable d) {
		draggables.add(d);
	}

	public void remove(Draggable d) {
		draggables.remove(d);
	}

	public void clear() {
		draggables.clear();
	}

	public void updateChat(int mX, int mY) {
		for (Draggable d :
				draggables) {
			d.updateChat(mX, mY);
		}
	}

	public void updateChat() {
		for (Draggable d :
				draggables) {
			if (d.isVisible())
				d.updateChat();
		}
	}
}
