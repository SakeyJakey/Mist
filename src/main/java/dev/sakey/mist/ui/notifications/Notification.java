package dev.sakey.mist.ui.notifications;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Notification {

	public String title;
	public String description;
	public NotificationType type;
	
	public final long timeStart;
	private final long timeEnd;
	public int length;
	
	ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
	
	public double y = sr.getScaledHeight() - 30 - (NotificationManager.notifications.size() * 30);
	public double h = sr.getScaledHeight() - (NotificationManager.notifications.size() * 30);

	public Notification(String title, String description, NotificationType type, int length) {
		this.title = title;
		this.description = description;
		this.type = type;
		this.length = length;
		
		this.timeStart = System.currentTimeMillis();
		this.timeEnd = this.timeStart + length;
		
		NotificationManager.Notify(this);
	}
	
	public boolean check() {
		if(System.currentTimeMillis() > this.timeEnd) {
			NotificationManager.notifications.remove(this);
			return true;
		}
		return false;
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
