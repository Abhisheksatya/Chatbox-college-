package gui;

import java.util.EventObject;

import model.User;

public class UsersPanelEvent extends EventObject {
	private String action;
	public UsersPanelEvent(Object source, String action) {
		super(source);
		this.action = action;
	}
	
	public String getAction() {
		return action;
	}

}
