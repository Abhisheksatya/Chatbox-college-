package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

public class Client {

	private Socket socket;
	private int port;
	private String ipAddress;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private User user;
	private User[] allUsers;
	private Set<String> departments;
	private ClientListener listener;
	
	public Client(String ipAddress, int port) {
		departments = new HashSet<>();
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public void connect(AuthPacket ap) throws UnknownHostException, IOException {
		socket = new Socket(ipAddress,port);
		out = new ObjectOutputStream(socket.getOutputStream());
		out.flush();
		out.writeObject(ap);
		in = new ObjectInputStream(socket.getInputStream());
	}
	public void readResponse() throws ClassNotFoundException, IOException {
		while(true) {
			Object obj = in.readObject();
			if(obj instanceof User)
				user = (User)obj;
			listener.clientEventOccured(new ClientEvent(obj));
		}
	}
	
	public void sendObject(Object obj) throws IOException {
		out.writeObject(obj);
	}
	
	public User getUser() {
		return user;
	}
	
	public User[] getAllUsers() {
		return allUsers;
	}
	
	public void setClientListener(ClientListener listener) {
		this.listener = listener;
	}
	
	public void setAllUsers(User[] allUsers) {
		this.allUsers = allUsers;
		setDepartments();
	}
	
	public void setDepartments() {
		departments.clear();
		for (User user : allUsers) {
			departments.add(user.getDepartment().toUpperCase());
		}
	}
	
	public Set<String> getDepartments() {
		return departments;
	}
	
}
