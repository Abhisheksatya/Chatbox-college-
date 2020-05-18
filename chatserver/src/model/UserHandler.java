package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UserHandler implements Runnable {
	
	private User user;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private List<User> allUsers;
	private UserEventListener listener;
	
	public UserHandler(ObjectInputStream in, ObjectOutputStream out, User user) {
		this.in = in;
		this.out = out;
		this.user = user;
	}
	
	@Override
	public void run() {
		try {
			User[] users = new User[allUsers.size()];
			int i = 0;
			for(User user : allUsers) {
				users[i] = (User)user;
				i++;
			}
			out.writeObject(users);
			out.flush();
			Timer timer = new Timer();
//			timer.schedule(new TimerTask() {
//				public void run() {
					//TODO decide what to do
//				}
//			}, 10000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while(true) {
			try {
				Object obj = in.readObject();
				if(listener != null)
				listener.userEventOccured(new UserEvent(obj,user));
			} catch (ClassNotFoundException | IOException e) {
				//e.printStackTrace();
			}
		}
	}
	
	public void setAllUsers(List<User> allUsers) {
		this.allUsers = allUsers;
	}
	
	public void addUserEventListener(UserEventListener listener) {
		this.listener = listener;
	}
	
	public void sendObject(Object object) {
		try {
			out.writeObject(object);
		} catch (IOException e) {
			//TODO
			e.printStackTrace();
		}
	}
	
	

}
