package net.nunnerycode.bukkit.libraries.config;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NunneryConfigurationEvent extends Event {

	private static HandlerList handlerList = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlerList;
	}

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}
}
