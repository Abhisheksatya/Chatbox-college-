package gui;

import java.util.EventObject;

public class SettingEvent extends EventObject {

	private int port;
	
	SettingEvent(Object source, int port) {
		super(source);
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}
}
