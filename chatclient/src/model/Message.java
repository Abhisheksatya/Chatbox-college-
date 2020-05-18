package model;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.io.Serializable;
import java.sql.Date;

public class Message implements Serializable{
	
	private static SimpleDateFormat sdf = new SimpleDateFormat();
	private String messageText;
	private String sender;
	private String receiver;
	private Date sentOn;
	private Time sentAt;
	
	public Message(String messageText, String sender, String receiver, Date sentOn, Time sentAt) {
		this.messageText = messageText;
		this.sender = sender;
		this.receiver = receiver;
		this.sentOn = sentOn;
		this.sentAt = sentAt;
	}

	public String getMessageText() {
		return messageText;
	}

	public String getSender() {
		return sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public Date getSentOn() {
		return sentOn;
	}
	
	public Time getSentAt() {
		return sentAt;
	}
		
}
