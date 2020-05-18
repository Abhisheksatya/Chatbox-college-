package gui;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Message;
import model.User;

public class RightPanel extends JPanel {
	
	public RightPanel(Message message, User sender) {
		
		JLabel messageTime = new JLabel(message.getSentAt().toString(),null,JLabel.LEFT);
		JLabel messageText = new JLabel(message.getMessageText(),null,JLabel.RIGHT);
		messageTime.setFont(new Font("time",4,10));
		add(messageText);
		add(messageTime);
		setPreferredSize(getPreferredSize());
	}
}
