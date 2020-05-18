package gui;

import java.util.EventObject;

public class ChatEvent extends EventObject {
	
	private String action;
	
	public ChatEvent(Object source, String action) {
		super(source);
		this.action = action; 
	}
	
	public String getAction() {
		return action;
	}
	
	

}
