package gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.sql.Time;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.Message;
import model.User;

public class ChatFrame extends JFrame {
	
	private JTextArea chatArea;
	private JTextField messageField;
	private JButton send;
	private User receiver;
	private User sender;
	private ChatFrameListener listener;
	
	public ChatFrame(User receiver, User sender) {
		super(receiver.toString());
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				listener.chatFrameEventOccured(new ChatFrameEvent(sender));
			}

			@Override
			public void windowClosed(WindowEvent e) {
				ChatFrame.this.setVisible(false);
				setReceiver(null);
				listener.chatFrameEventOccured(new ChatFrameEvent(sender));
			}
		});
		
		this.receiver = receiver;
		this.sender = sender;
		
		chatArea = new JTextArea();
		chatArea.setFocusable(false);
		chatArea.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		messageField = new JTextField();
		messageField.setColumns(25);
		
		
		send = new JButton("Send");
		send.addActionListener(ae -> {
			Message message = new Message(messageField.getText(), sender.getUserId(),
					receiver.getUserId(), new Date(new java.util.Date().getTime()),
					new Time(new java.util.Date().getTime()));
			addMessage(message);
			messageField.setText("");
			if(listener != null)
				listener.chatFrameEventOccured(new ChatFrameEvent(message));
			
		});
		
		JPanel panel = new JPanel();
		panel.add(messageField,BorderLayout.CENTER);
		panel.add(send,BorderLayout.EAST);
		getContentPane().add(new JScrollPane(chatArea),BorderLayout.CENTER);
		getContentPane().add(panel,BorderLayout.SOUTH);
		setSize(450,300);
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
		setTitle(receiver.toString());
		chatArea.setText("");
		messageField.setText("");
	}

	public void addMessage(Message message) {
		synchronized (this) {
			if(message.getSender().equals(receiver.getUserId()))
				chatArea.append(receiver.getName()+": "+message.getMessageText()+"\n");
			else
				chatArea.append(sender.getName()+": "+message.getMessageText()+"\n");
		}
	}
	
	public void addChatFrameListner(ChatFrameListener listener) {
		this.listener = listener;
	}	

}
