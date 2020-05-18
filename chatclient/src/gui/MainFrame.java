package gui;

import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Client;
import model.Message;
import model.User;

public class MainFrame extends JFrame implements ChatFrameListener {  
	
	private UserDetailsPanel userDetailsPanel;
	private AllUsersPanel allUsersPanel;
	private User sender;
	private User receiver;
	private ChatFrame chatFrame;
	private MainFrameListener listener;
	
	public MainFrame(User sender, Set<String> departments, User[] allUsers) {
		super(sender.getName());
		
		this.sender = sender;
		
		userDetailsPanel = new UserDetailsPanel(sender);
		userDetailsPanel.setBounds(12, 12, 474, 150);
		userDetailsPanel.setBorder(BorderFactory.createTitledBorder("Profile"));
		
		allUsersPanel = new AllUsersPanel(sender, departments, allUsers);
		allUsersPanel.setBounds(12, 174, 474, 235);
		allUsersPanel.setBorder(BorderFactory.createTitledBorder("All Users"));
		allUsersPanel.setLayout(null);
		allUsersPanel.addUserPanelListener((e) -> {
			receiver = (User)e.getSource();
			if(chatFrame == null)
				chatFrame = new ChatFrame(receiver,sender);
			chatFrame.addChatFrameListner(this);
			chatFrame.setReceiver(receiver);
			chatFrame.setVisible(true);
			if(listener != null)
				listener.mainFrameEventOccured(new MainFrameEvent(receiver));
		});
		
		
		getContentPane().add(userDetailsPanel);
		getContentPane().add(allUsersPanel);
		getContentPane().setLayout(null);
		
		
		setSize(500,450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	public void setDepartments(Set<String> departments) {
		allUsersPanel.setDepartments(departments);
	}
	
	public void setAllUsers(User[] allUsers) {
		allUsersPanel.setAllUsers(allUsers);
	}
	
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
	
	public User getReceiver() {
		return receiver;
	}

	public void addMessage(Message message) {
		chatFrame.addMessage(message);
	}
	
	public void addMainFrameListener(MainFrameListener listener) {
		this.listener = listener;
	}

	@Override
	public void chatFrameEventOccured(ChatFrameEvent ce) {
		if(ce.getSource().equals(sender)) {
			setReceiver(null);
		}
		else if(listener != null)
			listener.mainFrameEventOccured(new MainFrameEvent(ce.getSource()));
	}
	
}
