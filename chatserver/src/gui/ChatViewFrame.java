package gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.Message;
import model.User;

public class ChatViewFrame extends JFrame {
	
	public ChatViewFrame(Vector<Message> history, User user1, User user2) {
		JPanel panel = new JPanel();
		GridLayout layout = new GridLayout(history.size(),1,5,5);
		panel.setLayout(layout);
		
		history.forEach(message -> {
			JPanel temp = new JPanel();
			if(message.getReceiver().equals(user1.getUserId())) {
				temp.setLayout(new FlowLayout(FlowLayout.LEFT));
				LeftPanel lp = new LeftPanel(message, user2);
				temp.add(lp);
			}
			else {
				temp.setLayout(new FlowLayout(FlowLayout.RIGHT));
				RightPanel rp = new RightPanel(message, user1);
				temp.add(rp);
			}
			temp.setPreferredSize(temp.getPreferredSize());
			panel.add(temp);
			
		});
		
		getContentPane().setPreferredSize(getContentPane().getPreferredSize());
		add(new JScrollPane(panel));
		setSize(400,400);
		setVisible(true);
	}
}
