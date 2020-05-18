package model;

import java.util.EventObject;

public class ServerEvent extends EventObject {
	
	private String message; 

	public ServerEvent(Object source, String message) {
		super(source);
		this.message = message;
	}
	
	public String getEventMessage() {
		return message;
	}
	

}
