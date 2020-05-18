package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import model.Message;
import model.User;

public class Database {
	private static String url = "jdbc:mysql://localhost:3306/Chat";
	private static String userName = "root";
	private static String password = "pass";
	private static Connection con;
	private static PreparedStatement selectStatement;
	private static PreparedStatement insertStatement;
	private static PreparedStatement deleteStatement;
	private static PreparedStatement updateStatement;
	private static PreparedStatement getDateStatement;
	private static PreparedStatement getConStatement1;
	private static PreparedStatement getConStatement2;
	private static PreparedStatement getMessages;
	private static PreparedStatement getChatHistory;
	private static PreparedStatement addNewMessage;
	
	public static void initConnection() throws SQLException {
		con = DriverManager.getConnection(url, userName, password);
		String selectQuery = "SELECT * FROM users";
		selectStatement = con.prepareStatement(selectQuery);
		String insertQuery = "INSERT INTO users VALUES(?,?,?,?)";
		insertStatement = con.prepareStatement(insertQuery);
		String deleteQuery = "DELETE FROM users WHERE userId = ?";
		deleteStatement = con.prepareStatement(deleteQuery);
		String updateQuery = "UPDATE users SET name=?,department=?,password=? WHERE userId=?";
		updateStatement = con.prepareStatement(updateQuery);
		String getDateQuery = "SELECT DISTINCT sentOn FROM messages WHERE sender = ? OR receiver = ?";
		getDateStatement = con.prepareStatement(getDateQuery);
		String getConQuery1 = "SELECT * FROM users WHERE userId in "
				+ "(SELECT DISTINCT sender FROM messages WHERE receiver = ? AND sentOn = ?)";
		String getConQuery2 = "SELECT * FROM users WHERE userId in "
				+ "(SELECT DISTINCT receiver FROM messages WHERE sender = ? and sentOn = ?)";
		getConStatement1 = con.prepareStatement(getConQuery1);
		getConStatement2 = con.prepareStatement(getConQuery2);
		String getMessagesQuery = "SELECT * FROM messages WHERE ((sender=? AND receiver=?)"
				+ " OR (sender=? AND receiver=?)) AND sentOn = ? ORDER BY sentAt";
		getMessages = con.prepareStatement(getMessagesQuery);
		String getChatHistoryQuery = "SELECT * FROM messages WHERE ((sender=? AND receiver=?)" 
				+" OR (sender=? AND receiver=?)) ORDER BY sentOn, sentAt";
		getChatHistory = con.prepareStatement(getChatHistoryQuery);
		String addNewMessageQuery = "INSERT INTO messages VALUES(?,?,?,?,?)";
		addNewMessage = con.prepareStatement(addNewMessageQuery);
	}
	
	public static void getAllUsers(Vector<User> allUsers) throws SQLException {
		ResultSet resultSet = selectStatement.executeQuery();
		while(resultSet.next()) {
			String userId = resultSet.getString("userId");
			String name = resultSet.getString("name");
			String department = resultSet.getString("department");
			String password = resultSet.getString("password");
			User user = new User(userId, name, department, password);
			allUsers.add(user);
		}
	}
	
	public static void addUser(User user) throws SQLException {
		insertStatement.setString(1, user.getUserId());
		insertStatement.setString(2, user.getName());
		insertStatement.setString(3, user.getDepartment());
		insertStatement.setString(4, user.getPassword());
		insertStatement.executeUpdate();
	}
	
	public static void removeUser(User user) throws SQLException { 
		deleteStatement.setString(1, user.getUserId());
		deleteStatement.executeUpdate();
	}
	
	public static void updateUser(User user) throws SQLException {
		updateStatement.setString(1,user.getName());
		updateStatement.setString(2, user.getDepartment());
		updateStatement.setString(3,user.getPassword());
		updateStatement.setString(4, user.getUserId());
		updateStatement.executeUpdate();
	}
	
	public static List<Date> getDates(User user) throws SQLException {
		List<Date> dates = new Vector<>();
		getDateStatement.setString(1, user.getUserId());
		getDateStatement.setString(2, user.getUserId());
		ResultSet result = getDateStatement.executeQuery();
		while(result.next()) {
			dates.add(result.getDate("sentOn"));
		}
		return dates;
		
	}
	
	public static List<User> getConversed(Object[] obj) throws SQLException {
		List<User> con = new Vector<>();
		User usr = (User)obj[0];
		Date date = (Date)obj[1];
		getConStatement1.setString(1, usr.getUserId());
		getConStatement1.setDate(2, date);
		getConStatement2.setString(1, usr.getUserId());
		getConStatement2.setDate(2, date);
		ResultSet resultSet = getConStatement1.executeQuery();
		while(resultSet.next()) {
			String userId = resultSet.getString("userId");
			String name = resultSet.getString("name");
			String department = resultSet.getString("department");
			String password = resultSet.getString("password");
			User user = new User(userId, name, department, password);
			con.add(user);
		}
		resultSet = getConStatement2.executeQuery();
		while(resultSet.next()) {
			String userId = resultSet.getString("userId");
			String name = resultSet.getString("name");
			String department = resultSet.getString("department");
			String password = resultSet.getString("password");
			User user = new User(userId, name, department, password);
			if(!con.contains(user))
				con.add(user);
		}
		return con;
	}
	
	public static List<Message> getChatHistory(Object[] obj) throws SQLException {
		List<Message> messages = new Vector<Message>();
		User usr1 = (User)obj[0];
		User usr2 = (User)obj[1];
		Date date = (Date)obj[2];
		getMessages.setString(1,usr1.getUserId());
		getMessages.setString(2,usr2.getUserId());
		getMessages.setString(3,usr2.getUserId());
		getMessages.setString(4,usr1.getUserId());
		getMessages.setDate(5, date);
		ResultSet resultSet = getMessages.executeQuery();
		while(resultSet.next()) {
			String sender = resultSet.getString("sender");
			String receiver = resultSet.getString("receiver");
			Date sentOn = resultSet.getDate("sentOn");
			Time sentAt = resultSet.getTime("sentAt");
			String messageText = resultSet.getString("message");
			Message message = new Message(messageText, sender, receiver, sentOn, sentAt);
			messages.add(message);
		}
		
		return messages;
	}
	
	public static List<Message> getChatHistory(User sender, User receiver) throws SQLException {
		List<Message> messages = new ArrayList<>();
		getChatHistory.setString(1,sender.getUserId());
		getChatHistory.setString(2,receiver.getUserId());
		getChatHistory.setString(3,receiver.getUserId());
		getChatHistory.setString(4,sender.getUserId());
		ResultSet resultSet = getChatHistory.executeQuery();
		while(resultSet.next()) {
			String senderId = resultSet.getString("sender");
			String receiverId = resultSet.getString("receiver");
			Date sentOn = resultSet.getDate("sentOn");
			Time sentAt = resultSet.getTime("sentAt");
			String messageText = resultSet.getString("message");
			Message message = new Message(messageText, senderId, receiverId, sentOn, sentAt);
			messages.add(message);
		}
		return messages;
	}
	
	public static void addNewMessage(Message message) throws SQLException {
		addNewMessage.setString(1,message.getSender());
		addNewMessage.setString(2, message.getReceiver());
		addNewMessage.setDate(3, message.getSentOn());
		addNewMessage.setTime(4, message.getSentAt());
		addNewMessage.setString(5, message.getMessageText());
		addNewMessage.executeUpdate();
	}
	
}
