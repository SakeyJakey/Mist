package dev.sakey.mist.ui.notifications;

public class Notification {

	public final long timeStart;
	private final long timeEnd;
	public String title;
	public String description;
	public NotificationType type;
	public int length;

	public double y = NotificationManager.renderer.initY();

	public Notification(String title, String description, NotificationType type, int length) {
		this.title = title;
		this.description = description;
		this.type = type;
		this.length = length;

		this.timeStart = System.currentTimeMillis();
		this.timeEnd = this.timeStart + length;

		NotificationManager.Notify(this);
	}

	public Notification(Notification n) {
		this.title = n.title;
		this.description = n.description;
		this.type = n.type;
		this.y = n.y;
		this.timeStart = n.timeStart;
		this.length = n.length;
		this.timeEnd = n.timeStart + n.length;
	}

	public boolean check() {
		if (System.currentTimeMillis() < this.timeEnd) return false;
		NotificationManager.notifications.remove(this);
		return true;
	}

	public double getPercent() {
		//return length / (System.currentTimeMillis() - timeStart);
		//return (System.currentTimeMillis() - timeStart) / (timeEnd - timeStart);
		// TODO: add percentage thing
		double time = System.currentTimeMillis() - timeStart;
		double result = (time / length) * 100;
		return result <= 100 ? result : 100;
	}

}
