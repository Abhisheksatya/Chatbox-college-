package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import dao.Database;	

public class Server implements UserEventListener{
	
	private ServerSocket serverSocket;
	private List<User> allUsers;
	private Map<String, UserHandler> connectedUsers;
	private int port;
	private ServerEventListener listener;
	volatile private boolean done; 
	
	public Server(int port) throws IOException {
		allUsers = new Vector<>();
		connectedUsers = new HashMap<>();
		this.port = port;
		done = false;
	}
	
	public void stopServer() {
		
		try {
			done = true;
			serverSocket.close();
			connectedUsers.clear();
			if(listener != null)
				listener.serverEventOccured(new ServerEvent(this,"Server Stopped on "+new java.util.Date()));
		} catch (IOException e) {
			//e.printStackTrace();
			if(listener != null)
				listener.serverEventOccured(new ServerEvent(this, e.getMessage()));
		}
		
	}
	
	public void startServer() {
		done = false;
		try {
			serverSocket  = new ServerSocket(port);
			if(listener != null)
				listener.serverEventOccured(new ServerEvent(this,"Server Started at port "+port+" on "+new java.util.Date()));
			while(!done) {
					try {
						Socket socket = serverSocket.accept();
						ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
						Object obj = ois.readObject();
						if(obj instanceof AuthPacket) {
							AuthPacket ap = (AuthPacket)obj;
							int size = connectedUsers.size();
							allUsers.stream().parallel().forEach(user -> {
								if(user.getUserId().equals(ap.getUserId()) && user.getPassword().equals(ap.getPassword())) {
									try {
										ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
										oos.flush();
										UserHandler userHandler = new UserHandler(ois,oos,user);
										userHandler.addUserEventListener(Server.this);
										userHandler.setAllUsers(allUsers);
										Thread thread = new Thread(userHandler);
										connectedUsers.put(user.getUserId(), userHandler);
										listener.serverEventOccured(new ServerEvent(Server.this,user+" logged in on " + new java.util.Date()));
										oos.writeObject(user);
										thread.start();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}	
							});
							if(size == connectedUsers.size()) {
								ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
								oos.writeObject(ap);
							}
						}
					} catch (Exception e) {
						//TODO something
					}
					
			}
		}catch(IOException ex) {
			//ex.printStackTrace();
			if(listener != null)
				listener.serverEventOccured(new ServerEvent(this, ex.getMessage()));
		}
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public void addServerEventListener(ServerEventListener listener) {
		this.listener = listener;
	}
	
	public Vector<User> getAllUsers() {
		return (Vector<User>) allUsers;
	}
	
	public void initAllUsers() {
		try {
			Database.initConnection();
			Database.getAllUsers((Vector<User>) allUsers);
		} catch (SQLException e) {
			if(listener != null)
				listener.serverEventOccured(new ServerEvent(this, "ERROR!!! Unable to conncet to database..."));
		}
	}
	
	public void addUser(User user) {
		try {
			Database.addUser(user);
		} catch (SQLException e) {
			if(listener != null)
				listener.serverEventOccured(new ServerEvent(this, "Failed to add user "+user.getUserId()));
			e.printStackTrace();
		}
	}

	public void removeUser(User user) {
		try {
			Database.removeUser(user);
		} catch (SQLException e) {
			if(listener != null)
				listener.serverEventOccured(new ServerEvent(this, "Failed to remove user "+user.getUserId()));
			e.printStackTrace();
		}
	}
	
	public void updateUser(User user) {
		try {
			Database.updateUser(user);
		} catch (SQLException e) {
			if(listener != null)
				listener.serverEventOccured(new ServerEvent(this, "Failed to update user "+user.getUserId()));
			e.printStackTrace();
		}
	}
	
	public Vector<Date> getDates(User user) {
		try {
			return (Vector<Date>) Database.getDates(user);
		} catch (SQLException e) {
			if(listener != null)
				listener.serverEventOccured(new ServerEvent(this, "Failed to get dates.Sorry"));
			e.printStackTrace();
			return null;
		}
	}
	
	public Vector<User> getConversed(Object obj[]) {
		try {
			return (Vector<User>) Database.getConversed(obj);
		} catch (SQLException e) {
			if(listener != null)
				listener.serverEventOccured(new ServerEvent(this, "Failed to get con.Sorry"));
			e.printStackTrace();
			return null;
		}
	}
	
	public Vector<Message> getChatHistory(Object[] obj) {
		try {
			return (Vector<Message>) Database.getChatHistory(obj);
		} catch (SQLException e) {
			if(listener != null)
				listener.serverEventOccured(new ServerEvent(this, "Failed to get Chat History"));
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void userEventOccured(UserEvent ue) {
		Object obj = ue.getSource();
		if(obj instanceof User) {
			User sender = ue.getSender();
			ArrayList<Message> temp;
			try {
				temp = (ArrayList<Message>) Database.getChatHistory(sender, (User)obj);
				Message [] messages = new Message[temp.size()];
				int i = 0;
				for(Message message : temp) {
					messages[i] = message;
					i++;
				}
				connectedUsers.get(sender.getUserId()).sendObject(messages);
			} catch (SQLException e) {
				//TODO
				e.printStackTrace();
			}
		}
		else if(obj instanceof Message) {
			String receiverId = ((Message)obj).getReceiver();
			if(connectedUsers.get(receiverId) != null)
				connectedUsers.get(receiverId).sendObject((Message)obj);
			else {
				//TODO
			}
			try {
				Database.addNewMessage((Message)obj);
			} catch (SQLException e) {
				//TODO
				e.printStackTrace();
			}			
		}
	}
}