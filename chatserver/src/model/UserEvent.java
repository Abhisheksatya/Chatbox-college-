package model;

import java.util.EventObject;

public class UserEvent extends EventObject {

	private User sender;
	public UserEvent(Object source, User sender) {
		super(source);
		this.sender = sender;
	}	
	
	public User getSender() {
		return sender;
	}

}
