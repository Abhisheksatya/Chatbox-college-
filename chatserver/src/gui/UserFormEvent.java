package gui;

import java.util.EventObject;

import model.User;

public class UserFormEvent extends EventObject {
	private User user;

	public UserFormEvent(Object source, User user) {
		super(source);
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
